package pl.paullettuce.face_detector.image_analysis

import android.media.Image
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import pl.paullettuce.face_detector.commons.logd
import pl.paullettuce.face_detector.models.DetectedFacesData
import timber.log.Timber

class FaceDetector(private val imageAnalysisCallback: ImageAnalysisCallback) : ImageAnalysis.Analyzer {
    private val analysisOptions: FirebaseVisionFaceDetectorOptions
    private val firebaseFaceDetector: FirebaseVisionFaceDetector

    init {
        analysisOptions = buildAnalysisOptions()
        firebaseFaceDetector = buildDetector()
    }

    override fun analyze(imageProxy: ImageProxy?, degrees: Int) {
        val mediaImage: Image = imageProxy?.image ?: return
        logd("FaceDetector imagesize: (${mediaImage.width}, ${mediaImage.height}), degress: $degrees")
        performRotatedImageAnalysis(mediaImage, degrees)
    }


    private fun performRotatedImageAnalysis(image: Image, rotation: Int) {
        val firebaseRotation = degreesToFirebaseRotation(rotation)
        val firebaseVisionImage = FirebaseVisionImage.fromMediaImage(image, firebaseRotation)
        detectFace(firebaseVisionImage)
    }

    private fun detectFace(firebaseVisionImage: FirebaseVisionImage) {
        firebaseFaceDetector
            .detectInImage(firebaseVisionImage)
            .addOnSuccessListener { faces ->
                onFacesDetectionSuccess(firebaseVisionImage, faces)
            }
            .addOnFailureListener { e ->
                onFacesDetectionFailure(e)
            }
    }

    private fun onFacesDetectionSuccess(processedImage: FirebaseVisionImage, faces: MutableList<FirebaseVisionFace>) {
        if (faces.isEmpty()) {
            Timber.d("No faces detected")
            imageAnalysisCallback.noFacesDetected()
        } else {
            Timber.d("Faces detected: ${faces.size}")
            Timber.d("Processed image size: (${processedImage.bitmap.width}, ${processedImage.bitmap.height}")
            imageAnalysisCallback.someFacesDetected(DetectedFacesData(processedImage, faces))
        }
    }


    private fun onFacesDetectionFailure(e: Exception) {
        Timber.d(e, "Faces detection failure")
        imageAnalysisCallback.noFacesDetected()
    }

    private fun degreesToFirebaseRotation(degrees: Int): Int = when (degrees) {
        90 -> FirebaseVisionImageMetadata.ROTATION_90
        180 -> FirebaseVisionImageMetadata.ROTATION_180
        270 -> FirebaseVisionImageMetadata.ROTATION_270
        else -> FirebaseVisionImageMetadata.ROTATION_0
    }

    private fun buildAnalysisOptions() = FirebaseVisionFaceDetectorOptions.Builder()
            .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
            .build()

    private fun buildDetector() = FirebaseVision.getInstance().getVisionFaceDetector(analysisOptions)
}