package pl.paullettuce.face_detector.image_analysis

import pl.paullettuce.face_detector.models.DetectedFacesData

interface ImageAnalysisCallback {
    fun someFacesDetected(detectedFacesData: DetectedFacesData)
    fun noFacesDetected()
}