package com.volleyball.pickup.game.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.fragment.app.Fragment
import com.facebook.Profile
import com.volleyball.pickup.game.R
import com.volleyball.pickup.game.databinding.FragmentProfileBinding
import com.volleyball.pickup.game.ui.widgets.AvatarView

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val uri = Profile.getCurrentProfile()?.getProfilePictureUri(200, 200)
        binding.cvAvatar.setContent {
            MaterialTheme {
                AvatarView(uri = uri, dimenRes = R.dimen.profile_avatar_size)
            }
        }
        binding.tvName.text = Profile.getCurrentProfile()?.let { "${it.firstName} ${it.lastName}" }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}