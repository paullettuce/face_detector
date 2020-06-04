package pl.paullettuce.face_detector.camera

import androidx.camera.core.UseCase

interface UseCaseManager {
    fun useCase(): UseCase
}