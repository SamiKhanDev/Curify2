package com.saim.curify.medicine.prescription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.saim.adapters.GenericListAdapter
import com.saim.curify.databinding.FragmentPrescriptionsBinding
import com.saim.curify.databinding.ItemPrescriptionBinding
import com.saim.domain.entities.PrescriptionData
import kotlinx.coroutines.launch
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrescriptionsFragment: Fragment() {
    private lateinit var binding: FragmentPrescriptionsBinding
    private lateinit var adapter: GenericListAdapter<PrescriptionData, ItemPrescriptionBinding>
    private val viewModel: PrescriptionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPrescriptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = GenericListAdapter(
            inflate = { parent -> ItemPrescriptionBinding.inflate(LayoutInflater.from(parent.context), parent, false) },
            bind = { binding, item ->
                binding.note.text = item.note
                binding.status.text = item.status.replaceFirstChar { 
                    if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() 
                }
                
                // Load image with error handling
                if (!item.imageUrl.isNullOrBlank()) {
                    com.bumptech.glide.Glide.with(binding.root.context)
                        .load(item.imageUrl)
                        .error(com.saim.curify.R.drawable.logo_curify)
                        .placeholder(com.saim.curify.R.drawable.logo_curify)
                        .into(binding.thumbnail)
                } else {
                    binding.thumbnail.setImageResource(com.saim.curify.R.drawable.logo_curify)
                }
            },
            diff = GenericListAdapter.simpleDiff(
                areItemsTheSame = { o, n -> o.id == n.id },
                areContentsTheSame = { o, n ->
                    o.id == n.id && o.imageUrl == n.imageUrl && o.note == n.note && o.status == n.status && o.timestamp == n.timestamp
                }
            )
        )
        binding.prescriptionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.prescriptionsRecyclerView.adapter = adapter

        val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModel.start(uid)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.items.collect { list ->
                adapter.submitList(list)
                binding.emptyState.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                if (list.isNotEmpty()) binding.prescriptionsRecyclerView.scrollToPosition(list.size - 1)
            }
        }
    }
}

