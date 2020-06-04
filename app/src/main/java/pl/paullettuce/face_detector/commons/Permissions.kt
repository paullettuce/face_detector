package pl.paullettuce.face_detector.commons

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Permissions
private constructor(val permissionNames: Array<String>,
                    val requestCode: Int) {

    private val permissions: List<Permission> = permissionNames.map { Permission.fromName(it) }

    fun areGranted(context: Context): Boolean = permissions.all { it.isGranted(context) }

    fun request(activity: AppCompatActivity) {
        ActivityCompat.requestPermissions(
            activity, permissionNames, requestCode
        )
    }

    companion object {
        fun create(permissionNames: Array<String>, requestCode: Int) = Permissions(permissionNames, requestCode)
    }
}

class Permission
private constructor(private val permissionName: String) {

    fun isGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, permissionName
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        fun fromName(permissionName: String) = Permission(permissionName)
    }
}