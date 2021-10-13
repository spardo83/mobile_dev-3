package ar.edu.unlam.appsdeterceros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.edu.unlam.appsdeterceros.databinding.ActivitySendBinding

class SendActivity : AppCompatActivity() {
    lateinit var binding: ActivitySendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            binding.textView.text = intent.getStringExtra(Intent.EXTRA_TEXT)
        }
    }
}