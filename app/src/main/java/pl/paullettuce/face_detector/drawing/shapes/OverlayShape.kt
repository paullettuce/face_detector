package pl.paullettuce.face_detector.drawing.shapes

import android.graphics.Canvas
import pl.paullettuce.face_detector.drawing.GraphicOverlayView

/**
 * Base class for a custom graphics object to be rendered within the graphic overlayView.  Subclass
 * this and implement the [Graphic.draw] method to define the
 * graphics element.  Add instances to the overlayView using [GraphicOverlayView.add].
 */
abstract class OverlayShape(private val overlayView: GraphicOverlayView) {
    /**
     * Draw the graphic on the supplied canvas.  Drawing should use the following methods to
     * convert to view coordinates for the graphics that are drawn:
     *
     *  1. [Graphic.scaleX] and [Graphic.scaleY] adjust the size of
     * the supplied value from the processed image scale to the view scale.
     *  1. [Graphic.translateX] and [Graphic.translateY] adjust the
     * coordinate from the processed image's coordinate system to the view coordinate system.
     *
     *
     * @param canvas drawing canvas
     */
    abstract fun draw(canvas: Canvas)

    fun scaleX(horizontal: Float): Float {
        return horizontal * overlayView.getWidthScaleFactor()
    }

    fun scaleY(vertical: Float): Float {
        return vertical * overlayView.getHeightScaleFactor()
    }

    /**
     * Adjusts the x coordinate from the preview's coordinate system to the view coordinate
     * system.
     */
    fun translateX(x: Float): Float {
        return if (overlayView.isCameraFacingFront()) {
            overlayView.width - scaleX(x)
        } else {
            scaleX(x)
        }
    }

    /**
     * Adjusts the y coordinate from the preview's coordinate system to the view coordinate
     * system.
     */
    fun translateY(y: Float): Float {
        return scaleY(y)
    }

    fun postInvalidate() {
        overlayView.postInvalidate()
    }
}