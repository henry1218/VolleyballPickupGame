package com.volleyball.pickup.game.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.volleyball.pickup.game.MainViewModel
import com.volleyball.pickup.game.databinding.FragmentMySignUpEventsBinding
import com.volleyball.pickup.game.ui.home.PostAdapter
import com.volleyball.pickup.game.utils.PostViewType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MySignUpEventsFragment : Fragment() {
    private var _binding: FragmentMySignUpEventsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMySignUpEventsBinding.inflate(inflater, container, false)

        val adapter = PostAdapter(PostViewType.MY_SIGNED_UP, itemClick = { postId ->
            val action = EventsFragmentDirections.actionPostDetail(postId)
            findNavController().navigate(action)
        })
        binding.mySignedUpListView.adapter = adapter

        viewModel.fetchSignedUpEvent()
        viewModel.signedUpEventList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}