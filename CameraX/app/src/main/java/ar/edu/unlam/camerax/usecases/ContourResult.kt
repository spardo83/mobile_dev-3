package ar.edu.unlam.camerax.usecases

import com.google.mlkit.vision.face.FaceContour

data class ContourResult(
    val faceContour: FaceContour?,
    val leftEyeContour: FaceContour?,
    val rightEyeContour: FaceContour?
)