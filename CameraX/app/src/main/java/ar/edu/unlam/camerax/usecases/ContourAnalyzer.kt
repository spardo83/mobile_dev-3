package ar.edu.unlam.camerax.usecases

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class ContourAnalyzer(val callback: (List<ContourResult>) -> Unit) : ImageAnalysis.Analyzer {

    // Estas opciones permiten detectar con mejor precision pero no se recomiendan para tiempo real
    val highAccuracyOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    // Opciones para captura de rostros en tiempo real
    val realTimeOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .enableTracking()
        .build()


    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy?.image
        if (mediaImage != null) {
            val image =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val detector = FaceDetection.getClient(realTimeOpts)

            detector.process(image).addOnSuccessListener { faces ->
                val contours = ArrayList<ContourResult>()

                for (face in faces) {
                    contours.add(
                        ContourResult(
                            face.getContour(FaceContour.FACE),
                            face.getContour(FaceContour.LEFT_EYE),
                            face.getContour(FaceContour.RIGHT_EYE)
                        )
                    )
                }

                callback(contours)
            }
        }
    }

    private fun onFaceDetected(faces: List<Face>) {
        for (face in faces) {

        }
    }
}