package pl.paullettuce.face_detector

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import pl.paullettuce.face_detector.camera.CameraActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openCameraBtn.setOnClickListener {
            openCameraActivity()
        }
    }

    private fun openCameraActivity() {
        startActivity(CameraActivity.launchIntent(this))
    }
}
