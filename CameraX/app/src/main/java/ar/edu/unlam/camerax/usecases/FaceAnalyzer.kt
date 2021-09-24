package ar.edu.unlam.camerax.usecases

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*

class FaceAnalyzer(val callback: (FaceResult) -> Unit) : ImageAnalysis.Analyzer {


    // Estas opciones permiten detectar con mejor precision pero no se recomiendan para tiempo real
    val highAccuracyOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    // Opciones para captura de rostros en tiempo real
    val realTimeOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .enableTracking()
        .build()

    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image

        if (mediaImage != null) {
            val image =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val detector = FaceDetection.getClient(realTimeOpts)

            detector.process(image)
                .addOnSuccessListener { faces ->
                    onFaceDetected(faces) { callback(it) }
                    imageProxy.close()
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                    imageProxy.close()
                }
        }

    }

    private fun onFaceDetected(faces: MutableList<Face>, callback: (FaceResult) -> Unit) {
        Log.i(TAG, "Caras detectadas ${faces.size}")
        for (face in faces) {

            if (face.smilingProbability != null) {
                val smileProb = face.smilingProbability
                Log.i(TAG, "Probabilidad de sonrisa $smileProb")
            }
            if (face.rightEyeOpenProbability != null) {
                val rightEyeOpenProb = face.rightEyeOpenProbability
                Log.i(TAG, "Probabilidad de ojo derecho abierto $rightEyeOpenProb")
            }
            if (face.leftEyeOpenProbability != null) {
                val leftEyeOpenProb = face.leftEyeOpenProbability
                Log.i(
                    TAG,
                    "Probabilidad de ojo izquierdo abierto $leftEyeOpenProb"
                )
            }
            callback(
                FaceResult(
                    face.smilingProbability,
                    face.leftEyeOpenProbability,
                    face.rightEyeOpenProbability,
                    faces.size
                )
            )
        }
    }

    companion object {
        const val TAG = "FaceAnalyzer"
    }
}