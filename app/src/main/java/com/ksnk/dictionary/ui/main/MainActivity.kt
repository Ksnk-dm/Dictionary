package com.ksnk.dictionary.ui.main

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ksnk.dictionary.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    var textToSpeech: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textToSpeech = TextToSpeech(applicationContext) { i ->
            if (i == TextToSpeech.SUCCESS) {
                val lang = textToSpeech!!.setLanguage(Locale.US)
                if (lang == TextToSpeech.LANG_MISSING_DATA || lang == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(
                        this@MainActivity,
                        "Language is not supported",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    Toast.makeText(this@MainActivity, "Language Supported", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            textToSpeech?.speak("speed", TextToSpeech.QUEUE_FLUSH, null, null);
        }
//        CoroutineScope(Dispatchers.IO).launch {
//            mainViewModel.addWord(
//                Word(
//                    0,
//                    wordEng = "read",
//                    wordTranslate = "читать"
//                )
//            )
//        }

        mainViewModel.getAllWords().observe(this, Observer {
            Log.d("dfdff", it.toString())
        })
    }
}