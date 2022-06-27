package com.volleyball.pickup.game.ui.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.volleyball.pickup.game.MainViewModel
import com.volleyball.pickup.game.R
import com.volleyball.pickup.game.databinding.FragmentCreateEventBinding
import com.volleyball.pickup.game.models.Post
import com.volleyball.pickup.game.utils.CityUtil
import com.volleyball.pickup.game.utils.NET_HEIGHT_BETWEEN
import com.volleyball.pickup.game.utils.NET_HEIGHT_MAN
import com.volleyball.pickup.game.utils.NET_HEIGHT_WOMAN
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CreateEventFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentCreateEventBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("yyyy/MM/dd EEE", Locale.TAIWAN)
    private val timeFormatter = SimpleDateFormat("k:mm", Locale.TAIWAN)
    private var startTime: Calendar? = null
    private var endTime: Calendar? = null
    private var netHeightId = NET_HEIGHT_MAN
    private lateinit var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.setBottomNavVisible(false)
        _binding = FragmentCreateEventBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_close)
        binding.toolbar.inflateMenu(R.menu.events_menu)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.setOnMenuItemClickListener {
            var hasEmptyFiled = false
            if (binding.title.text.isNullOrEmpty()) {
                binding.titleInputLayout.error = "不能為空"
                hasEmptyFiled = true
            }
            if (binding.date.text.isNullOrEmpty()) {
                binding.dateInputLayout.error = "不能為空"
                hasEmptyFiled = true
            }
            if (binding.startTime.text.isNullOrEmpty()) {
                binding.startTimeInputLayout.error = "不能為空"
                hasEmptyFiled = true
            }
            if (binding.endTime.text.isNullOrEmpty()) {
                binding.endTimeInputLayout.error = "不能為空"
                hasEmptyFiled = true
            }
            if (binding.location.text.isNullOrEmpty()) {
                binding.locationInputLayout.error = "不能為空"
                hasEmptyFiled = true
            }

            if (hasEmptyFiled) {
                return@setOnMenuItemClickListener false
            }

            when (it.itemId) {
                R.id.submit -> {
                    viewModel.addPost(
                        Post(
                            title = binding.title.text.toString(),
                            date = binding.date.text.toString(),
                            startTime = binding.startTime.text.toString(),
                            endTime = binding.endTime.text.toString(),
                            timestamp = Timestamp(startTime!!.time),
                            city = binding.citySelected.text.toString(),
                            locality = binding.localitySelected.text.toString(),
                            location = binding.location.text.toString(),
                            netHeight = netHeightId,
                            fee = numberFormat(binding.fee),
                            needMen = numberFormat(binding.playerManAmount),
                            needWomen = numberFormat(binding.playerWomanAmount),
                            needBoth = numberFormat(binding.playerBothAmount),
                            additionalInfo = binding.additional.text.toString(),
                            profilePic = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=880&q=80"
                        )
                    )
                    findNavController().popBackStack()
                    true
                }
                else -> false
            }
        }

        globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            if (!isAdded) return@OnGlobalLayoutListener
            requireActivity().currentFocus?.let { focusView ->
                val r = Rect()
                binding.root.getWindowVisibleDisplayFrame(r)
                val screenHeight = binding.root.rootView.height
                val keypadHeight = screenHeight - r.bottom
                if (keypadHeight < screenHeight * 0.15) {
                    focusView.clearFocus()
                }
            }
        }
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)

        binding.scrollView.setOnScrollChangeListener { view, _, _, _, _ ->
            requireActivity().currentFocus?.let {
                val r = Rect()
                view.getHitRect(r)
                if (!it.getLocalVisibleRect(r)) {
                    it.clearFocus()
                    hideKeyboard(requireView())
                }
            }
        }

        binding.title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.isNotEmpty()) {
                    binding.titleInputLayout.error = null
                }
            }
        })

        binding.dateInputLayout.setEndIconOnClickListener {
            val dialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    startTime?.set(year, month, dayOfMonth)
                    endTime?.set(year, month, dayOfMonth)
                    binding.date.setText(dateFormatter.format(calendar.timeInMillis))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.minDate = Calendar.getInstance().timeInMillis
            dialog.show()
        }

        binding.startTimeInputLayout.setEndIconOnClickListener {
            val dialog = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    setStartTime(hourOfDay, minute)
                },
                startTime?.get(Calendar.HOUR_OF_DAY) ?: Calendar.getInstance()
                    .get(Calendar.HOUR_OF_DAY),
                startTime?.get(Calendar.MINUTE) ?: Calendar.getInstance()
                    .get(Calendar.MINUTE),
                true
            )

            dialog.show()
        }

        binding.endTimeInputLayout.setEndIconOnClickListener {
            val dialog = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    setEndTime(hourOfDay, minute)
                },
                getEndTime(Calendar.HOUR_OF_DAY),
                getEndTime(Calendar.MINUTE),
                true
            )

            dialog.show()
        }

        val cityAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            CityUtil.getAllCity()
        )

        val localitiesList = CityUtil.getLocalities(viewModel.getAddress().city)
        var localitiesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            localitiesList
        )
        binding.citySelected.apply {
            setAdapter(cityAdapter)
            setText(
                cityAdapter.getItem(CityUtil.getIndexOfCity(viewModel.getAddress().city)),
                false
            )
            setOnItemClickListener { _, _, position, _ ->
                localitiesAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    CityUtil.getLocalities(position)
                )
                binding.localitySelected.setAdapter(localitiesAdapter)
                binding.localitySelected.setText(localitiesAdapter.getItem(0), false)
            }
        }
        binding.localitySelected.apply {
            setAdapter(localitiesAdapter)
            setText(
                localitiesAdapter.getItem(
                    CityUtil.getIndexOfLocality(viewModel.getAddress().locality, localitiesList)
                ),
                false
            )
        }

        binding.rgNetHeight.check(R.id.rb_net_man)
        binding.rgNetHeight.setOnCheckedChangeListener { _, id ->
            netHeightId = when (id) {
                R.id.rb_net_man -> NET_HEIGHT_MAN
                R.id.rb_net_woman -> NET_HEIGHT_WOMAN
                else -> NET_HEIGHT_BETWEEN
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
        _binding = null
        viewModel.setBottomNavVisible(true)
    }

    private fun setStartTime(hourOfDay: Int, minute: Int) {
        startTime = Calendar.getInstance().apply {
            set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                hourOfDay,
                minute
            )
            binding.startTime.setText(timeFormatter.format(this.timeInMillis))
        }
    }

    private fun setEndTime(hourOfDay: Int, minute: Int) {
        endTime = Calendar.getInstance().apply {
            set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                hourOfDay,
                minute
            )
            binding.endTime.setText(timeFormatter.format(this.timeInMillis))
        }
    }

    private fun getEndTime(field: Int): Int {
        endTime?.let {
            return it.get(field)
        }

        startTime?.let {
            return it.get(field)
        }

        return Calendar.getInstance().get(field)
    }

    private fun numberFormat(textInputEditText: TextInputEditText): Int {
        textInputEditText.apply {
            return if (text.toString().isEmpty())
                0
            else {
                text.toString().removePrefix("0").toInt()
            }
        }
    }
}