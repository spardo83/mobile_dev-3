package ar.edu.unlam.camerax

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import ar.edu.unlam.camerax.databinding.ActivityMainBinding


private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createPermissionsLauncher()

        binding.launchCamera.setOnClickListener { launchCameraClicked() }
    }

    private fun createPermissionsLauncher() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.CAMERA] == true) {
                    launchCamera()
                } else {
                    Toast.makeText(
                        this,
                        "Se necesitan los permisos para lanzar la cámara",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun launchCameraClicked() {
        if (arePermissionsGranted()) {
            launchCamera()
        } else {
            askForPermissions()
        }
    }

    private fun arePermissionsGranted(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun askForPermissions() {
        permissionLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun launchCamera() {
        startActivity(Intent(this, CameraActivity::class.java))
    }
}