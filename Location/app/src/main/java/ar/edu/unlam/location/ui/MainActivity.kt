package ar.edu.unlam.location.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import ar.edu.unlam.location.databinding.ActivityMainBinding
import ar.edu.unlam.location.ui.MapsActivity

private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createPermissionsLauncher()

        binding.launchMap.setOnClickListener { launchMapsClick() }
    }

    private fun createPermissionsLauncher() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                    launchMaps()
                } else {
                    Toast.makeText(
                        this,
                        "Se necesitan los permisos para la ubicación",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun launchMapsClick() {
        if (arePermissionsGranted()) {
            launchMaps()
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

    private fun launchMaps() {
        startActivity(Intent(this, MapsActivity::class.java))
    }
}