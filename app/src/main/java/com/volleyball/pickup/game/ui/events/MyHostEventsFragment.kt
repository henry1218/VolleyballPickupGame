package com.volleyball.pickup.game.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.volleyball.pickup.game.MainViewModel
import com.volleyball.pickup.game.R
import com.volleyball.pickup.game.databinding.FragmentMyHostEventsBinding
import com.volleyball.pickup.game.ui.home.PostAdapter
import com.volleyball.pickup.game.ui.widgets.BottomSheetFragment
import com.volleyball.pickup.game.utils.PostViewType
import com.volleyball.pickup.game.utils.visibleIf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyHostEventsFragment : Fragment() {
    private var _binding: FragmentMyHostEventsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyHostEventsBinding.inflate(inflater, container, false)
        binding.fabCreateEvent.setOnClickListener {
            findNavController().navigate(R.id.action_create_post)
        }

        val adapter = PostAdapter(PostViewType.MY_HOST, actionClick = { post ->
            val bottomSheetFragment = BottomSheetFragment(
                edit = {
                    viewModel.setTempPostForEdit(post)
                    val action = EventsFragmentDirections.actionEditPost(true)
                    findNavController().navigate(action)
                }, delete = {
                    viewModel.deletePost(post.postId)
                })
            bottomSheetFragment.show(childFragmentManager, null)
        })

        binding.myHostListView.adapter = adapter

        viewModel.fetchHostEvent()
        viewModel.hostEventList.observe(viewLifecycleOwner) {
            binding.groupEmpty.visibleIf(it.isEmpty())
            adapter.submitList(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}