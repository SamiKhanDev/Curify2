package com.saim.curify.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.saim.adapters.GenericListAdapter
import com.saim.curify.MainActivity
import com.saim.curify.medicine.MedicineFragmentViewModel
import com.saim.curify.R
import com.saim.curify.databinding.FragmentHomeBinding
import com.saim.curify.databinding.ItemMedicineBinding
import com.saim.domain.entities.Drugs
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: GenericListAdapter<Drugs, ItemMedicineBinding>
    private val allMedicines = ArrayList<Drugs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Greeting
        binding.greetingText.text = "Hi, ${MainActivity.names.ifBlank { "there" }}"

        // Search input: focus for typing
        binding.searchInput.setOnClickListener {
            binding.searchInput.isFocusableInTouchMode = true
            binding.searchInput.isCursorVisible = true
            binding.searchInput.requestFocus()
        }

        binding.seeAll.setOnClickListener {
            findNavController().navigate(R.id.item_medicine)
        }

        adapter = GenericListAdapter(
            inflate = { parent -> ItemMedicineBinding.inflate(LayoutInflater.from(parent.context), parent, false) },
            bind = { b, medicine: Drugs ->
                b.title.text = medicine.title
                b.weight.text = medicine.weight
                b.status.text = medicine.status
                b.price.text = medicine.price.toString()
                com.bumptech.glide.Glide.with(b.root.context)
                    .load(medicine.image)
                    .error(com.saim.curify.R.drawable.logo_curify)
                    .placeholder(com.saim.curify.R.drawable.logo_curify)
                    .into(b.productImage)
                b.root.setOnClickListener {
                    b.root.context.startActivity(
                        android.content.Intent(
                            b.root.context,
                            com.saim.MedicalStoreModule.MedicineDetailActivity::class.java
                        ).putExtra("data", com.google.gson.Gson().toJson(medicine))
                    )
                }
            },
            diff = GenericListAdapter.simpleDiff(
                areItemsTheSame = { o, n -> o.id == n.id },
                areContentsTheSame = { o, n -> o == n }
            )
        )
        binding.recyclerPopular.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerPopular.adapter = adapter

        // ViewModel and lifecycle-aware collection
        val vm = MedicineFragmentViewModel()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                vm.data.collect { list ->
                    list?.let {
                        allMedicines.clear()
                        allMedicines.addAll(it)
                        // Default view: top 10
                        adapter.submitList(it.take(10))
                        binding.empty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                    }
                }
            }
        }

        // Inline filtering as user types
        binding.searchInput.addTextChangedListener { text ->
            val query = text?.toString()?.trim()?.lowercase().orEmpty()
            if (query.isEmpty()) {
                adapter.submitList(allMedicines.take(10))
            } else {
                val filtered = allMedicines.filter { med ->
                    med.title.lowercase().contains(query)
                }
                adapter.submitList(filtered)
            }
        }
    }
}