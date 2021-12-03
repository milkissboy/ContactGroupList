package com.hyk.getcontactlist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hyk.getcontactlist.contact.ContactListFragment
import com.hyk.getcontactlist.contact.GroupFragment

class ContactTabAdapter(fragment: FragmentActivity, private val func: (Any) -> Unit) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = count

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ContactListFragment(func)
        1 -> GroupFragment(func)
        else -> throw IllegalStateException("error")
    }

    companion object {
        private const val count = 2
    }
}