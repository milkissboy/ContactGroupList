package com.hyk.getcontactlist.extends

import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

const val REQUEST_READ_CONTACT = 1003

fun AppCompatActivity.checkPermissions(vararg permissions: String): Boolean {
    baseContext?.let {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.all { permission ->
                ContextCompat.checkSelfPermission(
                    it,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            false
        }
    } ?: return false
}

fun AppCompatActivity.checkPermissionDeny(
    permissions: Array<String>,
    @StringRes id: Int,
    alert: Boolean = false,
    func: (Boolean) -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val should = shouldShowRequestPermissionRationale(permissions[0])
        if (!should) { // deny
            if (alert) {
                //phoneSettingAlert(id)
            } else {
                //phoneSetting(id)
            }
        } else {
            func.invoke(true)
        }
    }
}