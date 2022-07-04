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
import androidx.navigation.fragment.navArgs
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.volleyball.pickup.game.MainViewModel
import com.volleyball.pickup.game.R
import com.volleyball.pickup.game.databinding.FragmentCreateOrEditEventBinding
import com.volleyball.pickup.game.models.Post
import com.volleyball.pickup.game.utils.CityUtil
import com.volleyball.pickup.game.utils.NetHeight
import com.volleyball.pickup.game.utils.ProfileUtils
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CreateOrEditEventFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private val args: CreateOrEditEventFragmentArgs by navArgs()
    private var _binding: FragmentCreateOrEditEventBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("yyyy/MM/dd EEE", Locale.TAIWAN)
    private val timeFormatter = SimpleDateFormat("k:mm", Locale.TAIWAN)
    private var startTime: Calendar? = null
    private var endTime: Calendar? = null
    private var netHeightId = NetHeight.MAN.id
    private lateinit var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener

    @Inject
    lateinit var profileUtils: ProfileUtils

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.setBottomNavVisible(false)
        _binding = FragmentCreateOrEditEventBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_close)
        binding.toolbar.inflateMenu(R.menu.events_menu)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.setOnMenuItemClickListener {
            var hasEmptyFiled = false
            if (binding.title.text.isNullOrEmpty()) {
                binding.titleInputLayout.error = getString(R.string.required)
                hasEmptyFiled = true
            }
            if (binding.date.text.isNullOrEmpty()) {
                binding.dateInputLayout.error = getString(R.string.required)
                hasEmptyFiled = true
            }
            if (binding.startTime.text.isNullOrEmpty()) {
                binding.startTimeInputLayout.error = getString(R.string.required)
                hasEmptyFiled = true
            }
            if (binding.endTime.text.isNullOrEmpty()) {
                binding.endTimeInputLayout.error = getString(R.string.required)
                hasEmptyFiled = true
            }
            if (binding.location.text.isNullOrEmpty()) {
                binding.locationInputLayout.error = getString(R.string.required)
                hasEmptyFiled = true
            }

            if (hasEmptyFiled) {
                return@setOnMenuItemClickListener false
            }

            when (it.itemId) {
                R.id.submit -> {
                    val post = Post(
                        title = binding.title.text.toString(),
                        date = binding.date.text.toString(),
                        startTime = binding.startTime.text.toString(),
                        endTime = binding.endTime.text.toString(),
                        startTimestamp = Timestamp(startTime!!.time),
                        endTimestamp = Timestamp(endTime!!.time),
                        city = binding.citySelected.text.toString(),
                        locality = binding.localitySelected.text.toString(),
                        location = binding.location.text.toString(),
                        netHeight = netHeightId,
                        fee = numberFormat(binding.fee),
                        needMen = numberFormat(binding.playerManAmount),
                        needWomen = numberFormat(binding.playerWomanAmount),
                        needBoth = numberFormat(binding.playerBothAmount),
                        additionalInfo = binding.additional.text.toString(),
                        profilePic = profileUtils.getSmallProfilePic(),
                        hostName = profileUtils.getName()
                    )
                    if (args.isEdit) {
                        viewModel.updatePost(post)
                    } else {
                        viewModel.addPost(post)
                    }
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

        val localitiesList = CityUtil.getLocalities(viewModel.getLocation().city)
        var localitiesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            localitiesList
        )
        binding.citySelected.apply {
            setAdapter(cityAdapter)
            setText(
                cityAdapter.getItem(CityUtil.getIndexOfCity(viewModel.getLocation().city)),
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
                    CityUtil.getIndexOfLocality(viewModel.getLocation().locality, localitiesList)
                ),
                false
            )
        }

        binding.rgNetHeight.check(R.id.rb_net_man)
        binding.rgNetHeight.setOnCheckedChangeListener { _, id ->
            netHeightId = when (id) {
                R.id.rb_net_man -> NetHeight.MAN.id
                R.id.rb_net_woman -> NetHeight.WOMAN.id
                else -> throw Exception("Wrong NetHeight id($id)")
            }
        }

        if (args.isEdit) {
            viewModel.getTempPostForEdit().let {
                binding.toolbar.title = getString(R.string.edit)
                binding.title.setText(it.title)
                calendar.time = it.startTimestamp.toDate()
                startTime = Calendar.getInstance().apply { time = it.startTimestamp.toDate() }
                endTime = Calendar.getInstance().apply { time = it.endTimestamp.toDate() }
                binding.date.setText(it.date)
                binding.startTime.setText(it.startTime)
                binding.endTime.setText(it.endTime)
                localitiesAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    CityUtil.getLocalities(it.city)
                )
                binding.localitySelected.setAdapter(localitiesAdapter)
                binding.citySelected.setText(it.city, false)
                binding.localitySelected.setText(it.locality, false)
                binding.location.setText(it.location)
                binding.rgNetHeight.check(
                    when (it.netHeight) {
                        NetHeight.MAN.id -> R.id.rb_net_man
                        NetHeight.WOMAN.id -> R.id.rb_net_woman
                        else -> throw Exception("Wrong NetHeight id($id)")
                    }
                )
                binding.fee.setText(it.fee.toString())
                binding.playerManAmount.setText(it.needMen.toString())
                binding.playerWomanAmount.setText(it.needWomen.toString())
                binding.playerBothAmount.setText(it.needBoth.toString())
                binding.additional.setText(it.additionalInfo)
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
            return when {
                text.toString().isEmpty() -> 0
                else -> text.toString().toInt()
            }
        }
    }
}