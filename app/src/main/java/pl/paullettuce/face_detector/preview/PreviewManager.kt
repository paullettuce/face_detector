package pl.paullettuce.face_detector.preview

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.view.ViewGroup
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import pl.paullettuce.face_detector.camera.UseCaseManager
import pl.paullettuce.face_detector.drawing.PreviewViewsSupplier
import pl.paullettuce.face_detector.drawing.shapes.DottedContourOverlay
import pl.paullettuce.face_detector.drawing.shapes.RectOverlay
import pl.paullettuce.face_detector.models.DetectedFacesData

class PreviewManager(
    previewViewsSupplier: PreviewViewsSupplier
) : UseCaseManager {
    private val previewConfig: PreviewConfig
    private val previewUseCase: Preview

    private val graphicOverlay = previewViewsSupplier.graphicOverlay()
    private val cameraPreview = previewViewsSupplier.cameraPreview()

    init {
        setUpdateOnLayoutChangeListener()
        previewConfig = buildConfig()
        previewUseCase = buildUseCase()
    }

    override fun useCase() = previewUseCase

    fun updateFaceOverlays(detectedFacesData: DetectedFacesData) {
        clearFaceOverlays()
        graphicOverlay.setProcessedImageResolution(detectedFacesData.getProcessedImageResolution())
        drawFaceOverlays(detectedFacesData)
    }

    fun clearFaceOverlays() {
        graphicOverlay.clear()
    }

    private fun drawFaceOverlays(detectedFacesData: DetectedFacesData) {
        detectedFacesData.getDetectedFaces().forEach {
            val rectOverlay = RectOverlay(graphicOverlay, it.boundingRect())
            graphicOverlay.add(rectOverlay)

            val faceContourOverlay = DottedContourOverlay(graphicOverlay, it.faceContour(), Color.RED)
            graphicOverlay.add(faceContourOverlay)

            val eyesOverlay = DottedContourOverlay(graphicOverlay, it.eyesContour(), Color.GREEN)
            graphicOverlay.add(eyesOverlay)
        }
    }

    private fun setUpdateOnLayoutChangeListener() {
        cameraPreview.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            update()
        }
    }

    private fun update() {
        updateTransformMatrix()
    }

    private fun updateTransformMatrix() {
        val matrixTransformer = MatrixTransformer(cameraPreview)
        matrixTransformer.transform()

        cameraPreview.setTransform(matrixTransformer.matrix)
    }

    @SuppressLint("RestrictedApi")
    @Override
    private fun buildConfig(): PreviewConfig {
        return PreviewConfig.Builder()
            .build()
    }

    private fun buildUseCase(): Preview {
        val useCase = Preview(previewConfig)

        // Every time the viewfinder is updated, we have to recompute layout
        useCase.setOnPreviewOutputUpdateListener {
            reattachViewToParent()
            updateSurfaceTexture(it.surfaceTexture)
            updateTransformMatrix()
        }
        return useCase
    }

    private fun reattachViewToParent() {
        val parent = cameraPreview.parent as ViewGroup
        parent.removeView(cameraPreview)
        parent.addView(cameraPreview, 0)
    }

    private fun updateSurfaceTexture(surfaceTexture: SurfaceTexture) {
        cameraPreview.surfaceTexture = surfaceTexture
    }
}