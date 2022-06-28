package com.volleyball.pickup.game.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.fragment.app.Fragment
import com.facebook.login.LoginManager
import com.volleyball.pickup.game.R
import com.volleyball.pickup.game.databinding.FragmentProfileBinding
import com.volleyball.pickup.game.ui.widgets.AvatarView
import com.volleyball.pickup.game.utils.ProfileUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var profileUtils: ProfileUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.cvAvatar.setContent {
            MaterialTheme {
                AvatarView(profileUtils.getLargeProfilePic(), R.dimen.profile_avatar_size)
            }
        }
        binding.tvName.text = profileUtils.getName()
        binding.btnLogout.setOnClickListener {
            LoginManager.getInstance().logOut()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}