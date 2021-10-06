package ar.edu.unlam.motionapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button2)
        val motion = findViewById<MotionLayout>(R.id.mainMotion)
        button.setOnClickListener {

                motion.progress = 0.5f

        }
    }
}