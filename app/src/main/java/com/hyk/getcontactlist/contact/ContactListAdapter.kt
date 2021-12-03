package com.hyk.getcontactlist.contact

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hyk.getcontactlist.databinding.ItemContactBinding
import com.hyk.getcontactlist.model.DataContact

class ContactListAdapter(private val func: (Any) -> Unit) : ListAdapter<DataContact, ContactListAdapter.HolderContact>(
    ChatListDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderContact {
        return HolderContact(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), func
        )
    }

    override fun onBindViewHolder(holder: HolderContact, position: Int) {
        val item: DataContact? = getItem(position)
        holder.bind(item)
    }

    class ChatListDiffCallback : DiffUtil.ItemCallback<DataContact>() {
        override fun areItemsTheSame(
            oldItem: DataContact,
            newItem: DataContact
        ): Boolean {
            return oldItem.lookupKey == newItem.lookupKey
        }

        override fun areContentsTheSame(
            oldItem: DataContact,
            newItem: DataContact
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()//super.getItemId(position)
    }

    class HolderContact(private val binding: ItemContactBinding, private val func: (Any) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataContact?) {
            data?.let {
                binding.apply {
                    model = data
                    executePendingBindings()

                    root.setOnClickListener {
                        func.invoke(data)
                    }
                }
            }
        }
    }
}