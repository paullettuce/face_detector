package pl.paullettuce.face_detector.drawing

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Size
import android.view.View
import com.google.android.gms.vision.CameraSource
import pl.paullettuce.face_detector.drawing.shapes.OverlayShape

class GraphicOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private val lock = Any()
    private var processedImageWidth: Int = 0
    private var processedImageHeight: Int = 0
    private var widthScaleFactor = 1.0f
    private var heightScaleFactor = 1.0f
    private var cameraFacing = CameraSource.CAMERA_FACING_BACK
    private val graphics = HashSet<OverlayShape>()

    /**
     * Draws the overlay with its associated graphic objects.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        synchronized(lock) {
            calculateScaleFactors(canvas)
            for (graphic in graphics) {
                graphic.draw(canvas)
            }
        }
    }

    fun clear() {
        synchronized(lock) {
            graphics.clear()
        }
        postInvalidate()
    }

    fun add(graphic: OverlayShape) {
        synchronized(lock) {
            graphics.add(graphic)
        }
        postInvalidate()
    }

    fun remove(graphic: OverlayShape) {
        synchronized(lock) {
            graphics.remove(graphic)
        }
        postInvalidate()
    }

    /**
     * Sets the camera attributes for size, which informs how to transform image coordinates later.
     */
    fun setProcessedImageResolution(resolution: Size) {
        synchronized(lock) {
            this.processedImageWidth = resolution.width
            this.processedImageHeight = resolution.height
        }
        postInvalidate()
    }

    fun getWidthScaleFactor() = widthScaleFactor

    fun getHeightScaleFactor() = heightScaleFactor

    fun isCameraFacingFront() = cameraFacing == CameraSource.CAMERA_FACING_FRONT

    private fun calculateScaleFactors(canvas: Canvas) {
        if (processedImageWidth != 0 && processedImageHeight != 0) {
            widthScaleFactor = canvas.width.toFloat() / processedImageWidth.toFloat()
            heightScaleFactor = canvas.height.toFloat() / processedImageHeight.toFloat()
        }
    }
}