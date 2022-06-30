package com.volleyball.pickup.game.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.volleyball.pickup.game.MainViewModel
import com.volleyball.pickup.game.databinding.FragmentMyAttendEventsBinding
import com.volleyball.pickup.game.ui.home.PostAdapter
import com.volleyball.pickup.game.utils.PostViewType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAttendEventsFragment : Fragment() {
    private var _binding: FragmentMyAttendEventsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyAttendEventsBinding.inflate(inflater, container, false)

        val adapter = PostAdapter(PostViewType.MY_ATTEND)
        binding.myAttendListView.adapter = adapter

        viewModel.fetchAttendEvent()
        viewModel.attendEventList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}