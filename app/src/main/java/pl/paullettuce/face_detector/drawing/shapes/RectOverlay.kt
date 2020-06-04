package pl.paullettuce.face_detector.drawing.shapes

import android.graphics.*
import pl.paullettuce.face_detector.drawing.GraphicOverlayView


class RectOverlay(graphicOverlayView: GraphicOverlayView, private val rect: Rect) :
    OverlayShape(graphicOverlayView) {

    private val paint: Paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 4.0f
    }

    init {
//        postInvalidate()
    }

    override fun draw(canvas: Canvas) {
        val rectF = RectF(rect)
        rectF.left = translateX(rectF.left)
        rectF.right = translateX(rectF.right)
        rectF.top = translateY(rectF.top)
        rectF.bottom = translateY(rectF.bottom)
        canvas.drawRect(rectF, paint)
    }
}