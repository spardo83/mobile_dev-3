package ar.edu.unlam.appsdeterceros

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ar.edu.unlam.appsdeterceros.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.maps.setOnClickListener {
            val intent = Uri.parse("geo:0,0?q=${binding.direccion.text}").let { number ->
                Intent(Intent.ACTION_VIEW, number)
            }

            startActivity(intent)
        }

        binding.llamar.setOnClickListener {
            val intent = Uri.parse("geo:0,0?q=${binding.numero.text}").let { number ->
                Intent(Intent.ACTION_VIEW, number)
            }

            startActivity(intent)
        }

        binding.enviar.setOnClickListener {
            try {
                Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, binding.textPlano.text)
                    type = "text/plain"
                    // Para utilizar solo whatsapp
                    //setPackage("com.whatsapp")
                    startActivity(this)
                }
            } catch (ignored: ActivityNotFoundException) {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("market://details?id=com.whatsapp")
                    startActivity(this)
                }

                Toast.makeText(this, "no encontre ninguna app", Toast.LENGTH_LONG).show()
            }

        }

        binding.servicio.setOnClickListener {
            Intent(this, MyService::class.java).apply {
                putExtra("key", "esto es un extra")
                startService(this)
            }
        }
    }
}