package pl.paullettuce.face_detector.models

import android.graphics.Rect
import com.google.firebase.ml.vision.common.FirebaseVisionPoint
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour
import pl.paullettuce.face_detector.drawing.DrawableFace

class DetectedFace(private val firebaseFace: FirebaseVisionFace): DrawableFace {
    override fun boundingRect(): Rect {
        return firebaseFace.boundingBox
    }

    override fun faceContour(): FloatArray {
        return getContourPoints(FirebaseVisionFaceContour.FACE).toFloatArray()
    }

    override fun eyesContour(): FloatArray {
        val eyes = mutableListOf<FirebaseVisionPoint>()
        eyes.addAll(getContourPoints(FirebaseVisionFaceContour.LEFT_EYE))
        eyes.addAll(getContourPoints(FirebaseVisionFaceContour.RIGHT_EYE))
        return eyes.toFloatArray()
    }

    private fun getContourPoints(contourType: Int): List<FirebaseVisionPoint> =
        firebaseFace.getContour(contourType).points

    private fun List<FirebaseVisionPoint>.toFloatArray(): FloatArray {
        val floatArray = FloatArray(this.size * 2)
        forEachIndexed { index, firebaseVisionPoint ->
            val firstOfEachPairIndex = index * 2
            floatArray[firstOfEachPairIndex] = firebaseVisionPoint.x
            floatArray[firstOfEachPairIndex + 1] = firebaseVisionPoint.y
        }
        return floatArray
    }
}