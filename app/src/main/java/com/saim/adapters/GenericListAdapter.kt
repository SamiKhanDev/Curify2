package com.saim.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * A small generic ListAdapter that works with ViewBinding-based view holders.
 * Usage:
 * GenericListAdapter(
 *   inflate = { parent -> ItemXBinding.inflate(LayoutInflater.from(parent.context), parent, false) },
 *   bind = { binding, item -> /* bind item to views */ },
 *   areItemsTheSame = { old, new -> old.id == new.id },
 *   areContentsTheSame = { old, new -> old == new }
 * )
 */
class GenericListAdapter<T, VB : androidx.viewbinding.ViewBinding>(
    private val inflate: (parent: ViewGroup) -> VB,
    private val bind: (binding: VB, item: T) -> Unit,
    diff: DiffUtil.ItemCallback<T>
) : ListAdapter<T, GenericListAdapter.GenericVH<VB>>(diff) {

    class GenericVH<VB : androidx.viewbinding.ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericVH<VB> {
        val binding = inflate(parent)
        return GenericVH(binding)
    }

    override fun onBindViewHolder(holder: GenericVH<VB>, position: Int) {
        bind(holder.binding, getItem(position))
    }

    companion object {
        fun <T : Any> simpleDiff(
            areItemsTheSame: (old: T, new: T) -> Boolean,
            areContentsTheSame: (old: T, new: T) -> Boolean
        ): DiffUtil.ItemCallback<T> = object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = areItemsTheSame(oldItem, newItem)
            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = areContentsTheSame(oldItem, newItem)
        }
    }
}

