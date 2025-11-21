package com.saim.curify.ui.doctors

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.saim.adapters.GenericListAdapter
import com.saim.curify.R
import com.saim.curify.appointment.AppointmentViewModel
import com.saim.curify.databinding.ActivityDoctorProfileBinding
import com.saim.curify.databinding.ItemTimeSlotBinding
import com.saim.domain.entities.Doctor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class DoctorProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorProfileBinding
    private lateinit var doctor: Doctor
    private val appointmentViewModel: AppointmentViewModel by viewModels()
    private var selectedDate: String = ""
    private var selectedTimeSlot: String = ""
    private val calendar = Calendar.getInstance()
    private lateinit var timeSlotAdapter: GenericListAdapter<String, ItemTimeSlotBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get doctor data from intent
        val doctorJson = intent.getStringExtra("doctor")
        if (doctorJson != null) {
            doctor = Gson().fromJson(doctorJson, Doctor::class.java)
            setupViews()
            setupTimeSlots()
            observeViewModel()
        } else {
            Toast.makeText(this, "Doctor information not available", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupViews() {
        binding.doctorName.text = doctor.name
        binding.specialization.text = doctor.specialization
        binding.hospital.text = doctor.hospital
        binding.fee.text = "${doctor.fee} PKR"

        Glide.with(this)
            .load(doctor.photo)
            .error(R.drawable.logo_curify)
            .placeholder(R.drawable.logo_curify)
            .into(binding.doctorPhoto)

        binding.bookAppointmentButton.setOnClickListener {
            if (selectedDate.isEmpty()) {
                Snackbar.make(binding.root, "Please select a date", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedTimeSlot.isEmpty()) {
                Snackbar.make(binding.root, "Please select a time slot", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            bookAppointment()
        }
    }

    private fun setupTimeSlots() {
        // Generate time slots from doctor's start and end time
        val timeSlots = generateTimeSlots(doctor.startTime, doctor.endTime)
        
        timeSlotAdapter = GenericListAdapter(
            inflate = { parent ->
                ItemTimeSlotBinding.inflate(layoutInflater, parent, false)
            },
            bind = { b, timeSlot ->
                b.timeSlotText.text = formatTimeSlot(timeSlot as String)
                
                // Highlight selected slot
                if (timeSlot == selectedTimeSlot) {
                    b.root.setCardBackgroundColor(getColor(R.color.primary_container))
                    b.timeSlotText.setTextColor(getColor(R.color.primary))
                } else {
                    b.root.setCardBackgroundColor(getColor(R.color.surface))
                    b.timeSlotText.setTextColor(getColor(R.color.text_primary))
                }
                
                b.root.setOnClickListener {
                    selectedTimeSlot = timeSlot
                    timeSlotAdapter.notifyDataSetChanged()
                    
                    // Show date picker if time slot is selected
                    if (selectedDate.isEmpty()) {
                        showDatePicker()
                    }
                }
            },
            diff = GenericListAdapter.simpleDiff(
                areItemsTheSame = { o, n -> o == n },
                areContentsTheSame = { o, n -> o == n }
            )
        )

        binding.timeSlotsRecyclerView.layoutManager = 
            androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        binding.timeSlotsRecyclerView.adapter = timeSlotAdapter
        timeSlotAdapter.submitList(timeSlots)
    }

    private fun generateTimeSlots(startTime: String, endTime: String): List<String> {
        val slots = mutableListOf<String>()
        try {
            val startFormat = SimpleDateFormat("H:mm", Locale.getDefault())
            val endFormat = SimpleDateFormat("H:mm", Locale.getDefault())
            val start = startFormat.parse(startTime)
            val end = endFormat.parse(endTime)
            
            if (start != null && end != null) {
                val calendar = Calendar.getInstance()
                calendar.time = start
                
                while (calendar.time.before(end) || calendar.time == end) {
                    val slotFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    slots.add(slotFormat.format(calendar.time))
                    calendar.add(Calendar.MINUTE, 30) // 30-minute slots
                }
            }
        } catch (e: Exception) {
            // Default slots if parsing fails
            slots.addAll(listOf("09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00"))
        }
        return slots
    }

    private fun formatTimeSlot(timeSlot: String): String {
        return try {
            val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val time = inputFormat.parse(timeSlot)
            time?.let { outputFormat.format(it) } ?: timeSlot
        } catch (e: Exception) {
            timeSlot
        }
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                selectedDate = dateFormat.format(calendar.time)
                Snackbar.make(binding.root, "Date selected: $selectedDate", Snackbar.LENGTH_SHORT).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Set minimum date to today
        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()
    }

    private fun bookAppointment() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(this, "Please login to book appointment", Toast.LENGTH_SHORT).show()
            return
        }

        val appointment = com.saim.domain.entities.Appointment(
            doctorId = doctor.id,
            doctorName = doctor.name,
            doctorPhoto = doctor.photo,
            specialization = doctor.specialization,
            hospital = doctor.hospital,
            fee = doctor.fee,
            date = selectedDate,
            timeSlot = selectedTimeSlot,
            status = "pending"
        )

        appointmentViewModel.bookAppointment(user.uid, appointment)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            appointmentViewModel.isBookingSuccess.collect { success ->
                success?.let {
                    if (it) {
                        Snackbar.make(binding.root, getString(R.string.appointment_booked), Snackbar.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Snackbar.make(binding.root, "Failed to book appointment", Snackbar.LENGTH_SHORT).show()
                    }
                    appointmentViewModel.clearBookingSuccess()
                }
            }
        }

        lifecycleScope.launch {
            appointmentViewModel.errorMessage.collect { error ->
                error?.let {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    appointmentViewModel.clearError()
                }
            }
        }
    }
}

