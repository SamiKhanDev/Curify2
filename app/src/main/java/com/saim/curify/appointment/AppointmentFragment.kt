package com.saim.curify.appointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.saim.adapters.GenericListAdapter
import com.saim.curify.R
import com.saim.curify.databinding.FragmentAppointmentsBinding
import com.saim.curify.databinding.ItemAppointmentBinding
import com.saim.domain.entities.Appointment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AppointmentFragment : Fragment() {

    private lateinit var binding: FragmentAppointmentsBinding
    private val viewModel: AppointmentViewModel by viewModels()
    private lateinit var adapter: GenericListAdapter<Appointment, ItemAppointmentBinding>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            viewModel.loadAppointments(user.uid)
        } else {
            binding.emptyState.visibility = View.VISIBLE
            binding.emptyState.text = "Please login to view appointments"
        }
    }

    private fun setupRecyclerView() {
        adapter = GenericListAdapter(
            inflate = { parent ->
                ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            },
            bind = { b, appointment ->
                b.doctorName.text = appointment.doctorName
                b.specialization.text = appointment.specialization
                b.hospital.text = appointment.hospital
                b.fee.text = "${appointment.fee} PKR"
                b.date.text = formatDate(appointment.date)
                b.timeSlot.text = formatTime(appointment.timeSlot)
                b.status.text = appointment.status.replaceFirstChar { 
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
                }
                
                // Set status color
                when (appointment.status.lowercase()) {
                    "confirmed" -> b.status.setTextColor(requireContext().getColor(R.color.success))
                    "pending" -> b.status.setTextColor(requireContext().getColor(R.color.warning))
                    "cancelled" -> b.status.setTextColor(requireContext().getColor(R.color.error))
                    "completed" -> b.status.setTextColor(requireContext().getColor(R.color.text_secondary))
                    else -> b.status.setTextColor(requireContext().getColor(R.color.text_secondary))
                }

                Glide.with(b.root.context)
                    .load(appointment.doctorPhoto)
                    .error(R.drawable.logo_curify)
                    .placeholder(R.drawable.logo_curify)
                    .into(b.doctorPhoto)

                // Cancel button visibility
                b.cancelButton.visibility = if (appointment.status == "pending" || appointment.status == "confirmed") {
                    View.VISIBLE
                } else {
                    View.GONE
                }

                b.cancelButton.setOnClickListener {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        viewModel.cancelAppointment(user.uid, appointment.id)
                    }
                }
            },
            diff = GenericListAdapter.simpleDiff(
                areItemsTheSame = { o, n -> o.id == n.id },
                areContentsTheSame = { o, n -> o == n }
            )
        )

        binding.appointmentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.appointmentsRecyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.appointments.collect { appointments ->
                adapter.submitList(appointments)
                binding.emptyState.visibility = if (appointments.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorMessage.collect { error ->
                error?.let {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    viewModel.clearError()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { loading ->
                // You can show/hide progress indicator here if needed
            }
        }
    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }

    private fun formatTime(timeString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val time = inputFormat.parse(timeString)
            time?.let { outputFormat.format(it) } ?: timeString
        } catch (e: Exception) {
            timeString
        }
    }
}

