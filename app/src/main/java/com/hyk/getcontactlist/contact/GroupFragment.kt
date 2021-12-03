package com.hyk.getcontactlist.contact

import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyk.getcontactlist.BaseCoroutineFragment
import com.hyk.getcontactlist.R
import com.hyk.getcontactlist.databinding.FragmentListBinding
import com.hyk.getcontactlist.model.DataContact
import com.hyk.getcontactlist.model.DataGroup
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern

class GroupFragment(private val func: (Any) -> Unit) : BaseCoroutineFragment<FragmentListBinding>() {

    override val layoutResId: Int
        get() = R.layout.fragment_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        binding.rv.apply {
            isNestedScrollingEnabled = false
            setHasFixedSize(true)

            val dividerItemDecoration =
                DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                    setDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.divider_list
                        )!!
                    )
                }
            addItemDecoration(dividerItemDecoration)

            adapter = GroupAdapter(func).apply {
                setHasStableIds(true)    // scroll 시 깜빡임 문제 해결
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadContact()
    }

    private fun loadContact() {
        launch {
            val groups = getGroup(getContactList())
            binding.rv.adapter?.apply {
                (this as GroupAdapter).submitList(null)
                if (groups.size > 0)
                    submitList(groups)
            }
        }
        /*e("group size : ${groups.size}")
        for (d in groups) {
            e("d : ${d.name} , ${d.contacts.size}")
        }*/
    }

    private fun getContactList(): MutableList<DataContact> {

        val list = mutableListOf<DataContact>()

        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts._ID
        )

        val sortOrder =
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
        val emoticons = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+")
        var emoticonsMatcher: Matcher?

        requireContext().contentResolver.query(uri, projection, null, null, sortOrder)
            ?.let { cursor ->
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    var name = cursor.getString(0)
                    val number = cursor.getString(1).replace("-".toRegex(), "")
                    val lookupKey = cursor.getString(2)
                    if (!TextUtils.isEmpty(lookupKey)) {
                        emoticonsMatcher = emoticons.matcher(name)
                        name = emoticonsMatcher!!.replaceAll("")   // 이모지 제거
                        //e("lookupKey :[${emoticonsMatcher!!.replaceAll("")}] $name $number $lookupKey")
                        list.add(DataContact(lookupKey, name, number))
                    }
                    cursor.moveToNext()
                }
                cursor.close()
            }
        return list
    }

    private fun getGroup(contacts: MutableList<DataContact>): MutableList<DataGroup> {
        val groups = mutableListOf<DataGroup>()
        if(contacts.size > 0) {
            val projection = arrayOf(
                ContactsContract.Groups._ID, ContactsContract.Groups.TITLE
            )
            requireContext().contentResolver.query(
                ContactsContract.Groups.CONTENT_URI, projection, null, null, null
            )?.let { cursor ->
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val groupId = cursor.getColumnIndex(ContactsContract.Groups._ID)
                    val groupName = cursor.getColumnIndex(ContactsContract.Groups.TITLE)
                    val gId = cursor.getString(groupId)
                    val gName = cursor.getString(groupName)

                    getGroupContactList(gId, contacts).apply {
                        if (size > 0) {
                            groups.add(DataGroup(gId, gName, this))
                        }
                    }
                    cursor.moveToNext()
                }
                cursor.close()
            }
        }
        return groups
    }

    private fun getGroupContactList(
        groupId: String,
        contacts: MutableList<DataContact>
    ): MutableList<DataContact> {

        val temp = mutableListOf<DataContact>()

        val where = (ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "="
                + groupId + " AND "
                + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
                + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'")
        requireContext().contentResolver.query(
            ContactsContract.Data.CONTENT_URI, arrayOf(
                ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts._ID
            ), where, null, ContactsContract.Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
        )?.let { c ->
            c.moveToFirst()
            while (!c.isAfterLast) {
                val lookupKey = c.getString(3)
                contacts.filter {
                    it.lookupKey == lookupKey
                }.map {
                    temp.add(it)
                }
                c.moveToNext()
            }
            c.close()
        }
        return temp
    }
}