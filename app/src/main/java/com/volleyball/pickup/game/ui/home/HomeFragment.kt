package com.volleyball.pickup.game.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.SimpleItemAnimator
import com.volleyball.pickup.game.MainViewModel
import com.volleyball.pickup.game.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val adapter = PostAdapter(({ postId ->
            // TODO item click
        }))
        binding.postView.adapter = adapter
        binding.postView.setHasFixedSize(true)
        (binding.postView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.fetchPosts()
        viewModel.postList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}