package com.ksnk.dictionary.ui.translateFragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.ksnk.dictionary.R
import com.ksnk.dictionary.data.entity.Word
import com.ksnk.dictionary.ui.settingsFragment.SettingsViewModel
import com.ksnk.dictionary.utils.showToast
import kotlinx.android.synthetic.main.addword.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TranslateFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private val translateViewModel: TranslateViewModel by viewModel()
    private var spinnerFirst: Spinner? = null
    private var spinnerSecond: Spinner? = null
    private var languages = arrayOf("English")
    private var languagesTranslate = arrayOf("Українська")
    private var imageButtonSwitch: ImageButton? = null
    private var editTextFirst: EditText? = null
    private var editTextSecond: EditText? = null
    private var imageButtonClear: ImageButton? = null
    private var imageButtonCopy: ImageButton? = null
    private var saveImageButton: ImageButton? = null
    private var translator: Translator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.translate_fragment, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerFirst = view.findViewById(R.id.spinnerFirst)
        saveImageButton = view.findViewById(R.id.addTranslateImageButton)
        saveImageButton?.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                translateViewModel.addWord(
                    Word(
                        0,
                        wordEng = editTextFirst?.text.toString(),
                        wordTranslate = editTextSecond?.text.toString()
                    )
                )
            }
            showToast("Додано до словника")
        }
        spinnerFirst?.onItemSelectedListener = this
        spinnerSecond = view.findViewById(R.id.spinnerSecond)
        editTextFirst = view.findViewById(R.id.editTextFirst)
        imageButtonClear = view.findViewById(R.id.imageButtonClear)

        imageButtonCopy = view.findViewById(R.id.imageButtonCopy)
        editTextFirst?.addTextChangedListener(textWatcher)
        editTextSecond = view.findViewById(R.id.editTextSecond)


        imageButtonClear?.setOnClickListener {
            editTextFirst?.text?.clear()
            editTextSecond?.text?.clear()
        }

        imageButtonCopy?.setOnClickListener {
            val myClipboard =
                context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val myClip: ClipData = ClipData.newPlainText("Text", editTextSecond?.text.toString())
            myClipboard.setPrimaryClip(myClip)
            showToast("Текст скопійовано")
        }
        imageButtonSwitch = view.findViewById(R.id.imageButtonSwitch)
        val translationConfigs = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.UKRAINIAN)
            .build()
        translator = Translation.getClient(translationConfigs)

        val aa =
            ArrayAdapter(requireActivity().applicationContext, R.layout.spinner_item, languages)
        val aa2 = ArrayAdapter(
            requireActivity().applicationContext,
            R.layout.spinner_item,
            languagesTranslate
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFirst!!.adapter = aa
        spinnerSecond!!.adapter = aa2

        imageButtonSwitch?.setOnClickListener {
            if (spinnerFirst?.adapter == aa) {

                val translationConfigs = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.UKRAINIAN)
                    .setTargetLanguage(TranslateLanguage.ENGLISH)
                    .build()
                translator = Translation.getClient(translationConfigs)
                translator!!.downloadModelIfNeeded()
                translator?.downloadModelIfNeeded()
                    ?.addOnSuccessListener {
                    }
                    ?.addOnFailureListener {
                    }
                spinnerFirst!!.adapter = aa2
                spinnerSecond!!.adapter = aa
                saveImageButton?.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        translateViewModel.addWord(
                            Word(
                                0,
                                wordEng = editTextSecond?.text.toString(),
                                wordTranslate = editTextFirst?.text.toString()
                            )
                        )
                    }
                    showToast("Додано до словника")
                }
            } else {
                spinnerFirst!!.adapter = aa
                spinnerSecond!!.adapter = aa2
                saveImageButton?.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        translateViewModel.addWord(
                            Word(
                                0,
                                wordEng = editTextFirst?.text.toString(),
                                wordTranslate = editTextSecond?.text.toString()
                            )
                        )
                    }
                    showToast("Додано до словника")
                }

                val translationConfigs = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.RUSSIAN)
                    .build()
                translator = Translation.getClient(translationConfigs)
            }
        }


    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private val textWatcher = object : TextWatcher {
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
            translator?.translate(editTextFirst?.text.toString())
                ?.addOnSuccessListener {
                    editTextSecond?.setText(it)
                }
                ?.addOnFailureListener {
                    it.printStackTrace()
                }
        }
    }
}