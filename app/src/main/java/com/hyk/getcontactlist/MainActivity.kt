package com.hyk.getcontactlist

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.hyk.getcontactlist.extends.checkPermissionDeny
import com.hyk.getcontactlist.extends.checkPermissions

class MainActivity : AppCompatActivity() {

    private val permission = Manifest.permission.READ_CONTACTS

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                d("in ok")
                setupLayout()
            } else {
                d("in deny")
                checkPermissionDeny(arrayOf(permission), R.string.permission_contact, true) {}
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!checkPermissions(permission)) {
            requestPermission.launch(permission)
        } else {
            setupLayout()
        }
    }

    private fun setupLayout() {
        supportFragmentManager.apply {
            val fragmentTransaction = beginTransaction()
            fragmentTransaction.add(R.id.frame, ContactTabFragment().apply {
                this.arguments = intent.extras
            }, "contact")
            fragmentTransaction.commitAllowingStateLoss()//.commit();
        }
    }
}