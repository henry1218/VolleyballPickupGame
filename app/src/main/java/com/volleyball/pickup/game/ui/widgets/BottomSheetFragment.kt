package com.volleyball.pickup.game.ui.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.volleyball.pickup.game.databinding.FragmentBottomSheetBinding

class BottomSheetFragment(val edit: () -> Unit, val delete: () -> Unit) :
    BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        binding.btnEdit.setOnClickListener {
            edit.invoke()
            dismiss()
        }
        binding.btnDelete.setOnClickListener {
            delete.invoke()
            dismiss()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}