package pl.paullettuce.face_detector.preview

import android.graphics.Matrix
import android.graphics.PointF
import android.view.Surface
import android.view.View

class MatrixTransformer(private val view: View) {
    val matrix = Matrix()

    fun transform() {
        rotate()
    }

    private fun rotate() {
        val rotationData = calculateRotationData()
        rotateMatrix(rotationData)
    }

    private fun calculateRotationData(): RotationData {
        val viewCenter = findCenter()
        val rotationDegrees = calculateRotation()
        return RotationData(viewCenter, rotationDegrees)
    }

    private fun rotateMatrix(rotationData: RotationData) {
        matrix.postRotate(
            -rotationData.rotationDegrees,
            rotationData.center.x,
            rotationData.center.y
        )
    }

    private fun findCenter() = PointF(view.width / 2f, view.height / 2f)

    private fun calculateRotation(): Float = when (view.display.rotation) {
        Surface.ROTATION_0 -> 0f
        Surface.ROTATION_90 -> 90f
        Surface.ROTATION_180 -> 180f
        Surface.ROTATION_270 -> 270f
        else -> 0f
    }

    data class RotationData(
        val center: PointF,
        val rotationDegrees: Float
    )
}