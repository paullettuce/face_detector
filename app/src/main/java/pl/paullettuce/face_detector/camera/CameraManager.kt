package pl.paullettuce.face_detector.camera

import androidx.camera.core.CameraX
import androidx.lifecycle.LifecycleOwner
import pl.paullettuce.face_detector.drawing.PreviewViewsSupplier
import pl.paullettuce.face_detector.image_analysis.AnalysisManager
import pl.paullettuce.face_detector.image_analysis.ImageAnalysisCallback
import pl.paullettuce.face_detector.image_capture.ImageCaptureManager
import pl.paullettuce.face_detector.models.DetectedFacesData
import pl.paullettuce.face_detector.preview.PreviewManager
import java.io.File

class CameraManager(
    private val previewViewsSupplier: PreviewViewsSupplier,
    private val lifecycleOwner: LifecycleOwner,
    private val directoryToSaveImage: Array<File>
) : ImageAnalysisCallback {

    private lateinit var previewManager: PreviewManager
    private lateinit var analysisManager: AnalysisManager
    private lateinit var imageCaptureManager: ImageCaptureManager

    override fun someFacesDetected(detectedFacesData: DetectedFacesData) {
        previewManager.updateFaceOverlays(detectedFacesData)
    }

    override fun noFacesDetected() {
        previewManager.clearFaceOverlays()
    }

    fun startCamera() {
        createUseCaseManagers()
        bindUseCasesToLifecycle()
    }

    fun captureImage() {
        imageCaptureManager.captureImage()
    }

    private fun createUseCaseManagers() {
        createPreviewManager()
        createImageCaptureManager()
        createAnalysisManager()
    }

    private fun createPreviewManager() {
        previewManager = PreviewManager(previewViewsSupplier)
    }

    private fun createImageCaptureManager() {
        imageCaptureManager = ImageCaptureManager(directoryToSaveImage)
    }

    private fun createAnalysisManager() {
        analysisManager = AnalysisManager(imageAnalysisCallback = this)
    }

    private fun bindUseCasesToLifecycle() {
        CameraX.bindToLifecycle(
            lifecycleOwner,
            previewManager.useCase(),
            analysisManager.useCase()
        )
    }
}