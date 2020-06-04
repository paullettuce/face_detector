package pl.paullettuce.face_detector.image_capture

import android.annotation.SuppressLint
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import pl.paullettuce.face_detector.camera.UseCaseManager
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ImageCaptureManager(
    private val directoryToSaveImage: Array<File>,
    private val executor: Executor = Executors.newSingleThreadExecutor()
) : UseCaseManager {
    private val captureConfig: ImageCaptureConfig
    private val captureUseCase: ImageCapture

    init {
        captureConfig = buildCaptureConfig()
        captureUseCase = buildCaptureUseCase()
    }

    override fun useCase() = captureUseCase

    fun captureImage() {
        captureAndSaveImage()
    }

    private fun captureAndSaveImage() {
        val file = createFileForImage()

        captureUseCase.takePicture(file, executor,
            object : ImageCapture.OnImageSavedListener {
                override fun onError(
                    imageCaptureError: ImageCapture.ImageCaptureError,
                    message: String,
                    exc: Throwable?
                ) {
                    logFailure(imageCaptureError, message, exc)
                }

                override fun onImageSaved(file: File) {
                    logSuccess(file)
                }
            })
    }

    private fun createFileForImage(): File {
        return File(
            directoryToSaveImage.first(),
            newImageFileName()
        )
    }

    private fun newImageFileName() = "${System.currentTimeMillis()}.jpg"

    private fun logSuccess(file: File) {
        Timber.d("File '${file.absolutePath}' saved successfully!")
    }

    private fun logFailure(imageCaptureError: ImageCapture.ImageCaptureError, message: String, exc: Throwable?) {
        Timber.e("Failed to save image. Error name = ${imageCaptureError.name}.\nMessage = $message.\nException = ${exc?.message}")
    }

    @SuppressLint("RestrictedApi")
    @Override
    private fun buildCaptureConfig() = ImageCaptureConfig.Builder()
        .apply {
            // We don't set a resolution for image capture; instead, we
            // select a capture mode which will infer the appropriate
            // resolution based on aspect ration and requested mode
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
        }.build()


    private fun buildCaptureUseCase() = ImageCapture(captureConfig)
}