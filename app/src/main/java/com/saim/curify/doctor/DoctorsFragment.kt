package com.saim.curify.doctor

import DoctorsViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.saim.adapters.GenericListAdapter
import com.saim.curify.databinding.FragmentDoctorsBinding
import com.saim.curify.databinding.ItemDoctorBinding
import com.saim.domain.entities.Doctor
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class DoctorsFragment : Fragment() {

    lateinit var binding: FragmentDoctorsBinding
    private lateinit var adapter: GenericListAdapter<Doctor, ItemDoctorBinding>
    private val all = ArrayList<Doctor>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoctorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GenericListAdapter(
            inflate = { parent ->
                ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            },
            bind = { b, d ->

                b.doctorName.text = d.name
                b.specialization.text = d.specialization
                b.hospital.text = d.hospital
                b.fee.text = d.fee + " PKR"

                Glide.with(b.root.context).load(d.photo).into(b.doctorPhoto)

                b.status.text = calculateStatus(d.startTime, d.endTime)
                
                // Add click listener to navigate to doctor profile
                b.root.setOnClickListener {
                    val intent = android.content.Intent(
                        b.root.context,
                        com.saim.curify.ui.doctors.DoctorProfileActivity::class.java
                    )
                    intent.putExtra("doctor", com.google.gson.Gson().toJson(d))
                    b.root.context.startActivity(intent)
                }
            },
            diff = GenericListAdapter.simpleDiff(
                areItemsTheSame = { o, n -> o.id == n.id },
                areContentsTheSame = { o, n -> o == n }
            )
        )

        binding.doctorsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.doctorsRecyclerView.adapter = adapter

        val vm = DoctorsViewModel()

        viewLifecycleOwner.lifecycleScope.launch {
            vm.data.collect { list ->
                all.clear()
                all.addAll(list)
                adapter.submitList(list)

                binding.emptyState.visibility =
                    if (list.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val q = s?.toString()?.lowercase() ?: ""
                if (q.isEmpty()) {
                    adapter.submitList(all)
                } else {
                    adapter.submitList(
                        all.filter { d ->
                            d.name.lowercase().contains(q) ||
                                    d.specialization.lowercase().contains(q) ||
                                    d.hospital.lowercase().contains(q)
                        }
                    )
                }
            }
        })
    }

    private fun calculateStatus(start: String, end: String): String {
        val formatter = DateTimeFormatter.ofPattern("H:mm") // allows 1 or 2 digit hours
        val now = LocalTime.now()

        return try {
            val s = LocalTime.parse(start, formatter)
            val e = LocalTime.parse(end, formatter)

            if (now.isAfter(s) && now.isBefore(e)) "Available"
            else "Offline"
        } catch (e: DateTimeParseException) {
            "Offline" // fallback if the time format is invalid
        }
    }

}

