package pl.paullettuce.face_detector.drawing.shapes

import android.graphics.*
import pl.paullettuce.face_detector.drawing.GraphicOverlayView

class DottedContourOverlay(graphicOverlayView: GraphicOverlayView,
                           private val points: FloatArray,
                           private val contourColor: Int) :
    OverlayShape(graphicOverlayView) {

    private val paint: Paint = Paint().apply {
        color = contourColor
        style = Paint.Style.STROKE
        strokeWidth = 4.0f
    }

    init {
        postInvalidate()
    }

    override fun draw(canvas: Canvas) {
        val transformedPoints = transformPoints()
        canvas.drawPoints(transformedPoints, paint)
    }

    private fun transformPoints(): FloatArray {
        val transformedPoints = FloatArray(points.size)
        points.forEachIndexed { index, point ->
            transformedPoints[index] = translateSinglePoint(index, point)
        }
        return transformedPoints
    }

    private fun translateSinglePoint(index: Int, point: Float): Float {
        return if (isIndexOfX(index)) {
            translateX(point)
        } else {
            translateY(point)
        }
    }

    /**
     * Points list ex.[P1, P2] in FloatArray representation look like this: [xP1, yP1, xP2, yP2]
     */
    private fun isIndexOfX(index: Int) = index % 2 == 0
}