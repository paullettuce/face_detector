package pl.paullettuce.face_detector.image_analysis

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysisConfig
import pl.paullettuce.face_detector.camera.UseCaseManager
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AnalysisManager(
    private val executor: Executor = Executors.newSingleThreadExecutor(),
    private val imageAnalysisCallback: ImageAnalysisCallback
) : UseCaseManager {

    private val analysisConfig: ImageAnalysisConfig
    private val analysisUseCase: ImageAnalysis

    init {
        analysisConfig = buildConfig()
        analysisUseCase = buildUseCase()
    }

    override fun useCase() = analysisUseCase

    @SuppressLint("RestrictedApi")
    @Override
    private fun buildConfig() = ImageAnalysisConfig.Builder().apply {
        // In our analysis, we care more about the latest image than analyzing *every* image
        setImageReaderMode(
            ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE
        )
    }.build()

    private fun buildUseCase() = ImageAnalysis(analysisConfig).apply {
        setAnalyzer(executor, FaceDetector(imageAnalysisCallback))
    }
}