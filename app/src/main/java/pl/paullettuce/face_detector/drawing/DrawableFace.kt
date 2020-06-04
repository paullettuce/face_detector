package pl.paullettuce.face_detector.drawing

import android.graphics.Rect

interface DrawableFace {
    fun boundingRect(): Rect
    fun faceContour(): FloatArray
    fun eyesContour(): FloatArray
}