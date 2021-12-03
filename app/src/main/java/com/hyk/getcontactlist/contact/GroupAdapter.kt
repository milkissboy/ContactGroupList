package com.hyk.getcontactlist.contact

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hyk.getcontactlist.databinding.ItemContactBinding
import com.hyk.getcontactlist.model.DataGroup

class GroupAdapter(private val func: (Any) -> Unit) : ListAdapter<DataGroup, GroupAdapter.HolderContact>(
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
        val item: DataGroup? = getItem(position)
        holder.bind(item)
    }

    class ChatListDiffCallback : DiffUtil.ItemCallback<DataGroup>() {
        override fun areItemsTheSame(
            oldItem: DataGroup,
            newItem: DataGroup
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: DataGroup,
            newItem: DataGroup
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()//super.getItemId(position)

    class HolderContact(private val binding: ItemContactBinding, private val func: (Any) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataGroup?) {
            data?.let {
                binding.apply {
                    executePendingBindings()
                    tvName.text = TextUtils.concat(it.name," (",it.contacts.size.toString(),")")
                    tvPhoneNum.visibility = View.GONE

                    root.setOnClickListener {
                        func.invoke(data)
                    }
                }
            }
        }
    }
}