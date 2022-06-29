package com.volleyball.pickup.game.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
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

        viewModel.setBottomNavVisible(false)
        viewModel.fetchPostDetail(args.postId)
        viewModel.postDetail.observe(viewLifecycleOwner) {
            binding.toolbar.title = it.title
            binding.cvAvatar.setContent {
                MaterialTheme {
                    AvatarView(it.profilePic, R.dimen.post_avatar_size)
                }
            }
            binding.hostName.text = it.hostName
            binding.title.text = it.title
            binding.going.text = ("已報名${it.players.size}位")
            val left = if (it.needBoth > 0) it.needBoth else it.needMen + it.needWomen
            binding.left.text = (if (left > 0) "缺${left}位" else "人數不限")
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