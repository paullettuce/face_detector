package pl.paullettuce.face_detector.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_camera.*
import pl.paullettuce.face_detector.R
import pl.paullettuce.face_detector.commons.Permissions
import pl.paullettuce.face_detector.commons.toast
import pl.paullettuce.face_detector.drawing.GraphicOverlayView
import pl.paullettuce.face_detector.drawing.PreviewViewsSupplier

private val cameraPermissions: Permissions = Permissions.create(arrayOf(Manifest.permission.CAMERA), 10)

class CameraActivity : AppCompatActivity(), PreviewViewsSupplier {

    private lateinit var cameraManager: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        createCameraManager()
        checkPermissionsAndStartCamera()
        setupLayout()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == cameraPermissions.requestCode) {
            onCameraPermissionDialogResponse()
        }
    }

    override fun cameraPreview(): TextureView = camera_preview

    override fun graphicOverlay(): GraphicOverlayView = graphic_overlay

    private fun onCameraPermissionDialogResponse() {
        if (allPermissionsGranted()) {
            cameraPermissionGranted()
        } else {
            cameraPermissionNotGranted()
        }
    }

    private fun createCameraManager() {
        cameraManager = CameraManager(
            this,
            this,
            externalMediaDirs
        )
    }

    private fun checkPermissionsAndStartCamera() {
        if (allPermissionsGranted()) {
            cameraPermissionGranted()
        } else {
            requestRequiredPermissions()
        }
    }

    private fun cameraPermissionGranted() {
        executeAfterPreviewViewIsInflated {
            cameraManager.startCamera()
        }
    }

    private fun executeAfterPreviewViewIsInflated(executable: () -> Unit) {
        camera_preview.post { executable() }
    }

    private fun cameraPermissionNotGranted() {
        toast("Camera permissions not granted by the user.")
        finish()
    }

    private fun setupLayout() {
        capture_image.setOnClickListener {
            cameraManager.captureImage()
        }
    }

    private fun allPermissionsGranted() = cameraPermissions.areGranted(baseContext)

    private fun requestRequiredPermissions() = cameraPermissions.request(this)

    companion object {
        fun launchIntent(context: Context) = Intent(context, CameraActivity::class.java)
    }
}
