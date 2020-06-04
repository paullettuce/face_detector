package pl.paullettuce.face_detector.drawing

import android.view.TextureView

interface PreviewViewsSupplier {
    fun cameraPreview(): TextureView
    fun graphicOverlay(): GraphicOverlayView
}