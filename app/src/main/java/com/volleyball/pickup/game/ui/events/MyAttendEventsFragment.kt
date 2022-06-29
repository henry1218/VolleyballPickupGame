package com.volleyball.pickup.game.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.volleyball.pickup.game.databinding.FragmentMyAttendEventsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAttendEventsFragment : Fragment() {
    private var _binding: FragmentMyAttendEventsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyAttendEventsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}