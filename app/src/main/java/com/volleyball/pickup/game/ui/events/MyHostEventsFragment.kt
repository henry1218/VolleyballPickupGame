package com.volleyball.pickup.game.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.volleyball.pickup.game.MainViewModel
import com.volleyball.pickup.game.databinding.FragmentMyHostEventsBinding
import com.volleyball.pickup.game.ui.home.PostAdapter
import com.volleyball.pickup.game.utils.MY_HOST_POST_VIEW_TYPE
import com.volleyball.pickup.game.utils.PostViewType
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

        val adapter = PostAdapter(PostViewType.MY_HOST)
        binding.myHostListView.adapter = adapter

        viewModel.fetchHostEvent()
        viewModel.hostEventList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}