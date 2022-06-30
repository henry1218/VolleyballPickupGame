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
import com.volleyball.pickup.game.utils.*
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(
    private val viewType: PostViewType,
    val itemClick: ((id: String) -> Unit)? = null
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
                    }
                }

                binding.title.text = title
                val need = if (needBoth > 0) needBoth else needMen + needWomen
                val left = need - players.size
                binding.playersStatus.text = when {
                    need == 0 -> "人數不限 已報名${players.size}位"
                    left > 0 -> "尚缺${left}位 已報名${players.size}位"
                    else -> "已滿"
                }
                binding.location.text = ("${city}${locality} $location")
                val dateFormat = SimpleDateFormat("yyyy/MM/dd EEE HH:mm", Locale.TAIWAN)
                binding.dateTime.text = ("${dateFormat.format(timestamp.toDate())} ~ $endTime")
                binding.netHeight.text = (
                        when (post.netHeight) {
                            NET_HEIGHT_MAN -> "男網"
                            NET_HEIGHT_WOMAN -> "女網"
                            else -> "介於男網與女網之間"
                        }
                        )
                binding.fee.text = (if (fee > 0) "\$${fee}/人" else "免費")
            }

            itemView.setOnClickListener {
                itemClick?.invoke(post.postId)
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