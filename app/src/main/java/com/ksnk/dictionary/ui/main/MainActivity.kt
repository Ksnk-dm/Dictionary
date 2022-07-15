package com.ksnk.dictionary.ui.main

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.ksnk.dictionary.R
import com.ksnk.dictionary.data.entity.Word
import kotlinx.android.synthetic.main.addword.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MainActivity : AppCompatActivity() {
    var translator: Translator? = null
    private val mainViewModel: MainViewModel by viewModel()
    private var imageButtonAdd: ImageButton? = null
    var textToSpeech: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageButtonAdd = findViewById(R.id.imageButtonAdd)
        imageButtonAdd?.setOnClickListener { showDialogBox() }
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

    private fun showDialogBox() {
        val viewLayout = LayoutInflater.from(this).inflate(R.layout.addword, null)
        val addDialog: AlertDialog = AlertDialog.Builder(this).create()
        addDialog.setView(viewLayout)
        addDialog.show()
        addDialog.addWordBtnID.setOnClickListener {
            val engWord = addDialog.enterEngID.text.toString().trim()
            val wordTranslate = addDialog.enterTranslateID.text.toString().trim()
            if (engWord.isNotEmpty() && wordTranslate.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    mainViewModel.addWord(
                        Word(
                            0,
                            wordEng = engWord,
                            wordTranslate = wordTranslate
                        )
                    )
                }
                addDialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter both field", Toast.LENGTH_SHORT).show()
            }
        }
        addDialog.cancelBtnID.setOnClickListener {
            addDialog.dismiss()
        }

        addDialog.radioGroupTranslate.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.radioButtonAll -> {
                    addDialog.enterTranslateID.isEnabled = true
                    addDialog.enterEngID.isEnabled = true
                    addDialog.enterTranslateID.text.clear()
                    addDialog.enterEngID.text.clear()
                }
                R.id.radioButtonTranslate -> {
                    addDialog.enterEngID.isEnabled = false
                    addDialog.enterTranslateID.isEnabled = true
                    addDialog.enterTranslateID.text.clear()
                    addDialog.enterEngID.text.clear()
                    val translationConfigs = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.RUSSIAN)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build()
                    translator = Translation.getClient(translationConfigs)
                    addDialog.enterTranslateID.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            translator?.translate(addDialog.enterTranslateID.text.toString())
                                ?.addOnSuccessListener {
                                    addDialog.enterEngID.setText(it)
                                }
                                ?.addOnFailureListener {
                                    it.printStackTrace()
                                }
                        }
                    })


                }
                R.id.radioButtonEng -> {
                    addDialog.enterTranslateID.isEnabled = false
                    addDialog.enterEngID.isEnabled = true
                    addDialog.enterTranslateID.text.clear()
                    addDialog.enterEngID.text.clear()
                    val translationConfigs = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.RUSSIAN)
                        .build()
                    translator = Translation.getClient(translationConfigs)
                    addDialog.enterEngID.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            translator?.translate(addDialog.enterEngID.text.toString())
                                ?.addOnSuccessListener {
                                    addDialog.enterTranslateID.setText(it)
                                }
                                ?.addOnFailureListener {
                                    it.printStackTrace()
                                }
                        }
                    })

                }
            }
        }



        addDialog.button.setOnClickListener {
            if (addDialog.enterEngID.text.isNotEmpty()) {
                translator?.downloadModelIfNeeded()
                    ?.addOnSuccessListener {
                        Toast.makeText(this, "Download Successful", Toast.LENGTH_SHORT).show()
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
                translator?.translate(addDialog.enterEngID.text.toString())
                    ?.addOnSuccessListener {
                        addDialog.enterTranslateID.setText(it)
                    }
                    ?.addOnFailureListener {
                        it.printStackTrace()
                    }
            }


        }
    }

    val textWatcher = object :TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            //Do stuff
        }
    }
    }