package com.volleyball.pickup.game.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.volleyball.pickup.game.R
import com.volleyball.pickup.game.databinding.PostViewHolderItemBinding
import com.volleyball.pickup.game.models.Post
import com.volleyball.pickup.game.ui.widgets.AvatarView
import com.volleyball.pickup.game.utils.NetHeight
import com.volleyball.pickup.game.utils.PostViewType
import com.volleyball.pickup.game.utils.gone
import com.volleyball.pickup.game.utils.visible
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(
    private val viewType: PostViewType,
    val itemClick: ((id: String) -> Unit)? = null,
    val actionClick: ((post: Post) -> Unit)? = null
) :
    ListAdapter<Post, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.post_view_holder_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PostViewHolder).onBind(getItem(position))
    }

    override fun getItemId(position: Int) = position.toLong()

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = PostViewHolderItemBinding.bind(itemView)

        fun onBind(post: Post) {
            post.apply {
                when (viewType) {
                    PostViewType.HOME, PostViewType.MY_ATTEND -> {
                        binding.cvAvatar.setContent {
                            MaterialTheme {
                                AvatarView(profilePic, R.dimen.post_avatar_size)
                            }
                        }
                        binding.hostName.text = hostName
                    }
                    PostViewType.MY_HOST -> {
                        binding.cvAvatar.gone()
                        binding.hostName.gone()
                        binding.additionalText.visible()
                        binding.additionalInfo.visible()
                        binding.additionalInfo.text = additionalInfo
                        binding.btnAction.visible()
                    }
                }

                binding.title.text = title
                val need = if (needBoth > 0) needBoth else needMen + needWomen
                val left = need - players.size
                binding.playersStatus.text = when {
                    need == 0 -> itemView.context.getString(R.string.unlimited_joined, players.size)
                    left > 0 -> itemView.context.getString(R.string.left_joined, left, players.size)
                    else -> itemView.context.getString(R.string.filled)
                }
                binding.location.text = ("${city}${locality} $location")
                val dateFormat = SimpleDateFormat("yyyy/MM/dd EEE HH:mm", Locale.TAIWAN)
                binding.dateTime.text = ("${dateFormat.format(startTimestamp.toDate())} ~ $endTime")
                binding.netHeight.text = itemView.context.getString(NetHeight.getStringRes(post.netHeight))
                binding.fee.text =
                    if (fee > 0) itemView.context.getString(R.string.fee_per_person, fee)
                    else itemView.context.getString(R.string.free)
            }

            itemView.setOnClickListener {
                itemClick?.invoke(post.postId)
            }

            binding.btnAction.setOnClickListener {
                actionClick?.invoke(post)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}