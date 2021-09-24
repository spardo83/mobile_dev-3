package ar.edu.unlam.camerax

import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import ar.edu.unlam.camerax.databinding.ActivityCameraBinding
import ar.edu.unlam.camerax.usecases.BarCodeAnalyzer
import ar.edu.unlam.camerax.usecases.ContourAnalyzer
import ar.edu.unlam.camerax.usecases.FaceAnalyzer
import ar.edu.unlam.camerax.usecases.FaceResult
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()
        outputDirectory = getOutputDirectory()

        startCamera()

        binding.takePicture.setOnClickListener { takePhoto() }
        binding.flipCamera.setOnClickListener { flipCamera() }
    }

    private fun flipCamera() {

        cameraSelector =
            if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                CameraSelector.DEFAULT_FRONT_CAMERA
            else
                CameraSelector.DEFAULT_BACK_CAMERA

        startCamera()
    }

    private fun takePhoto() {
        // Obtenemos la referencia a la captura de la imagen
        val imageCapture = imageCapture ?: return


        // Creamos un archivo para almacenar la imagen utilizando un timestamp
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        // Creamos un OutPutFileOptions con la metadata para almacenar la imagen
        // utilizando el metodo .setMetadata() del builder podemos agregar mas metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
            .build()

        // Creamos un listener asociado a imageCapture que escuche cuando se tomo una fotografia
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Error al capturar imagen: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    output.savedUri?.run {
                        val msg = "Imagen captura con éxito: $this"
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, msg)
                    }
                }
            })
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Enlazamos el ciclo de vida de las camaras al de la activity
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Generamos el preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()


            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { imageAnalysis ->
                    imageAnalysis.setAnalyzer(cameraExecutor, FaceAnalyzer {

                        binding.smileProgressbar.progress =
                            (it.smileProbability?.times(100F))?.toInt() ?: 0

                        binding.rightEyeOpenProgressBar.progress =
                            (it.rightEyeOpenProbability?.times(100F))?.toInt() ?: 0

                        binding.leftEyeOpenProgressBar.progress =
                            (it.leftEyeOpenProbability?.times(100F))?.toInt() ?: 0
                    })
                }

            var barcodeAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { imageAnalysis ->
                    imageAnalysis.setAnalyzer(cameraExecutor, BarCodeAnalyzer())
                }

            try {
                // Si existia algun provider enlazado al ciclo de vida lo desenlazamos
                cameraProvider.unbindAll()

                // Enlazamos los casos de uso a la camara
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture,  barcodeAnalyzer
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Falló al enlazar la camara", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    //Esta función obtiene un directorio para almacenar las imagenes
    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    // Es muy importante liberar la camara cuando finalizamos el uso
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val TAG = "CameraActivity"
    }


}