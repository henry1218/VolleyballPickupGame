package com.volleyball.pickup.game.ui.post

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.volleyball.pickup.game.MainViewModel
import com.volleyball.pickup.game.R
import com.volleyball.pickup.game.databinding.FragmentPostDetailBinding
import com.volleyball.pickup.game.ui.widgets.AvatarView
import com.volleyball.pickup.game.utils.NET_HEIGHT_MAN
import com.volleyball.pickup.game.utils.NET_HEIGHT_WOMAN
import com.volleyball.pickup.game.utils.gone
import com.volleyball.pickup.game.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PostDetailFragment : Fragment() {
    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val args: PostDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_close)
            inflateMenu(R.menu.post_menu)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.more -> {
                        //TODO show bottom sheet
                        true
                    }
                    else -> false
                }
            }
        }

        binding.fabJoinEvent.setOnClickListener {
            viewModel.updateEventStatus()
            binding.fabJoinEvent.isEnabled = false
        }

        viewModel.setBottomNavVisible(false)
        viewModel.fetchPostDetail(args.postId)
        viewModel.postDetail.observe(viewLifecycleOwner) {
            binding.fabJoinEvent.isEnabled = true
            binding.toolbar.title = it.title
            binding.cvAvatar.setContent {
                MaterialTheme {
                    AvatarView(it.profilePic, R.dimen.post_avatar_size)
                }
            }
            binding.hostName.text = it.hostName
            binding.title.text = it.title
            binding.going.text = ("已報名${it.players.size}位")
            val need = if (it.needBoth > 0) it.needBoth else it.needMen + it.needWomen
            val left = need - it.players.size
            if (need > 0) {
                binding.need.text = "(${need})"
                binding.left.text = if (left > 0) {
                    "尚缺${left}位"
                } else {
                    "已滿"
                }
            } else {
                binding.need.text = "(不限)"
                binding.left.gone()
            }

            binding.location.text = ("${it.city}${it.locality} ${it.location}")
            val dateFormat = SimpleDateFormat("yyyy/MM/dd EEE HH:mm", Locale.TAIWAN)
            binding.dateTime.text = ("${dateFormat.format(it.timestamp.toDate())} ~ ${it.endTime}")
            binding.netHeight.text = (
                    when (it.netHeight) {
                        NET_HEIGHT_MAN -> "男網"
                        NET_HEIGHT_WOMAN -> "女網"
                        else -> "介於男網與女網之間"
                    }
                    )
            binding.fee.text = (if (it.fee > 0) "\$${it.fee}/人" else "免費")
            binding.additionalInfo.visible()
            binding.additionalInfo.text = it.additionalInfo
            if (it.players.contains(viewModel.getUid())) {
                binding.fabJoinEvent.setImageResource(R.drawable.ic_neg_1)
                binding.fabJoinEvent.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_secondary_variant
                    )
                )
            } else {
                binding.fabJoinEvent.setImageResource(R.drawable.ic_plus_1)
                binding.fabJoinEvent.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.orange
                    )
                )
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.removeRegistration()
        viewModel.setBottomNavVisible(true)
    }
}