package ar.edu.unlam.camerax.usecases

data class FaceResult(
    val smileProbability: Float?,
    val leftEyeOpenProbability: Float?,
    val rightEyeOpenProbability: Float?,
    val faceQuantity: Int
)