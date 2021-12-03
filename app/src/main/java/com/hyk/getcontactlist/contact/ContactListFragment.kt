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
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern

class ContactListFragment(private val func: (Any) -> Unit) : BaseCoroutineFragment<FragmentListBinding>() {

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

            adapter = ContactListAdapter(func).apply {
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
            val contacts = getContactList()
            binding.rv.adapter?.apply {
                (this as ContactListAdapter).submitList(null)
                if (contacts.size > 0)
                    submitList(contacts)
            }
        }
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
}