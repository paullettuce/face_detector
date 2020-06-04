package pl.paullettuce.face_detector

import android.graphics.Rect
import com.google.common.truth.Truth.assertThat
import com.google.firebase.ml.vision.common.FirebaseVisionPoint
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner
import pl.paullettuce.face_detector.models.DetectedFace

private val FAKE_RECT = Rect(1,2,3,4)
private val FAKE_CONTOUR = listOf<FirebaseVisionPoint>(
    FirebaseVisionPoint(0.1f, 0.2f, 0.3f),
    FirebaseVisionPoint(0.4f, 0.5f, 0.6f),
    FirebaseVisionPoint(0.7f, 0.8f, 0.9f)
)

@RunWith(MockitoJUnitRunner::class)
class DetectedFaceTest {

    @Mock
    private lateinit var mockFirebaseVisionFace: FirebaseVisionFace

    @Test
    fun givenMockedFirebaseVisionFaceBoundingBox_CheckCoordinatesWithTestedObject() {
        `when`(mockFirebaseVisionFace.boundingBox)
            .thenReturn(FAKE_RECT)

        val detectedFaceUnderTest = DetectedFace(mockFirebaseVisionFace)
        val resultLeft: Rect = detectedFaceUnderTest.boundingRect()

        assertThat(resultLeft).isEqualTo(FAKE_RECT)
    }

    @Test
    fun givenMockedFirebaseVisionPointList_CheckIfFaceContourFloatArrayContainsXandYCoordinatesInCorrectOrder() {
        `when`(mockFirebaseVisionFace.getContour(FirebaseVisionFaceContour.FACE))
            .thenReturn(FirebaseVisionFaceContour(FirebaseVisionFaceContour.FACE, FAKE_CONTOUR))

        val detectedFaceUnderTest = DetectedFace(mockFirebaseVisionFace)
        val result: FloatArray = detectedFaceUnderTest.faceContour()

        assertThat(result).isNotEmpty()
        assertThat(result).isEqualTo(floatArrayOf(
            0.1f, 0.2f,
            0.4f, 0.5f,
            0.7f, 0.8f
        ))
    }
}