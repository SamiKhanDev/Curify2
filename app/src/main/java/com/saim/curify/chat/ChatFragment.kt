package com.saim.curify.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.saim.adapters.GenericListAdapter
import com.saim.curify.databinding.FragmentChatBinding
import com.saim.curify.databinding.ItemChatMessageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var adapter: GenericListAdapter<ChatMessageUi, ItemChatMessageBinding>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = GenericListAdapter(
            inflate = { parent -> ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false) },
            bind = { b, item ->
                b.messageText.text = item.text
                b.messageRole.text = item.role
            },
            diff = GenericListAdapter.simpleDiff(
                areItemsTheSame = { o, n -> o.id == n.id },
                areContentsTheSame = { o, n -> o == n }
            )
        )
        binding.messages.layoutManager = LinearLayoutManager(requireContext())
        binding.messages.adapter = adapter

        binding.sendButton.setOnClickListener {
            val prompt = binding.input.text?.toString()?.trim().orEmpty()
            if (prompt.isNotEmpty()) {
                viewModel.send(prompt)
                binding.input.setText("")
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messages.collect { adapter.submitList(it) }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isSending.collect { binding.sendButton.isEnabled = !it }
        }
    }
}

