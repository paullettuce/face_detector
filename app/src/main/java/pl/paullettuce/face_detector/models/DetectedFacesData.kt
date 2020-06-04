package pl.paullettuce.face_detector.models

import android.util.Size
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import pl.paullettuce.face_detector.commons.resolution

class DetectedFacesData(
    private val processedImage: FirebaseVisionImage,
    private val faces: List<FirebaseVisionFace>
) {
    fun getProcessedImageResolution(): Size = processedImage.bitmap.resolution()

    fun getDetectedFaces(): List<DetectedFace> = faces.map { DetectedFace(it) }
}