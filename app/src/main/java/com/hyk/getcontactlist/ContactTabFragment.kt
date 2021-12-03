package com.hyk.getcontactlist

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.hyk.getcontactlist.databinding.FragmentTabBinding
import com.hyk.getcontactlist.extends.showCustomAlert
import com.hyk.getcontactlist.model.DataContact
import com.hyk.getcontactlist.model.DataGroup

class ContactTabFragment : BaseFragment<FragmentTabBinding>() {

    override val layoutResId: Int
        get() = R.layout.fragment_tab

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabLayoutWithViewPager()
    }

    private fun initTabLayoutWithViewPager() {
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = ContactTabAdapter(requireActivity()) { data ->
            if (data is DataContact) {
                val text = "[" + data.name + "] " + data.phoneNumber + "\n" + data.lookupKey
                showCustomAlert(text, "개인")
            } else if (data is DataGroup) {
                var text = ""
                for (sub in data.contacts) {
                    text += "[" + sub.name + "] " + sub.phoneNumber + "\n" + sub.lookupKey + "\n"
                }
                showCustomAlert(text, "그룹 [${data.name}]")
            }
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager, false, true) { tab, position ->
            tab.text = when (position) {
                0 -> "개인"
                1 -> "그룹"
                else -> ""
            }
        }.attach()
    }
}