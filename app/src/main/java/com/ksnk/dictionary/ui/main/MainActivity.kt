package com.ksnk.dictionary.ui.main

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.ksnk.dictionary.FragmentSettingListener
import com.ksnk.dictionary.R
import com.ksnk.dictionary.data.entity.Word
import com.ksnk.dictionary.ui.listFragment.ListWordFragment
import com.ksnk.dictionary.ui.translateFragment.TranslateFragment
import kotlinx.android.synthetic.main.addword.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    var translator: Translator? = null
    private val mainViewModel: MainViewModel by viewModel()
    private var imageButtonAdd: ImageButton? = null
    var textToSpeech: TextToSpeech? = null
    var searchView: SearchView? = null
    private var fragmentSettingListener: FragmentSettingListener? = null
    private var bottomNavView: BottomNavigationView? = null

    private var bottomNavViewOnItemSelectListener = NavigationBarView.OnItemSelectedListener {
        when (it.itemId) {
            R.id.translate_item -> {
                val fragment: Fragment = TranslateFragment()
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerViewMain, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
            R.id.list_item -> {
                val fragment: Fragment = ListWordFragment().newInstance()
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerViewMain, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
            R.id.setting_item -> {
//                createSettingFragment()
//                loadPageAds()
//                searchImageButton.visibility = View.INVISIBLE
            }
        }
        return@OnItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchView = findViewById(R.id.word_search)
        bottomNavView = findViewById(R.id.bottomNavigationViewMain)

        bottomNavView?.setOnItemSelectedListener(bottomNavViewOnItemSelectListener)

        searchView?.setOnQueryTextListener(this)
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


    }

    var addDialog: AlertDialog? = null
    private fun showDialogBox() {
        translator?.downloadModelIfNeeded()
            ?.addOnSuccessListener {
            }
            ?.addOnFailureListener {
            }
        val viewLayout = LayoutInflater.from(this).inflate(R.layout.addword, null)
        addDialog = AlertDialog.Builder(this).create()
        addDialog?.setView(viewLayout)
        addDialog?.show()
        addDialog?.addWordBtnID?.setOnClickListener {
            val engWord = addDialog?.enterEngID?.text.toString().trim()
            val wordTranslate = addDialog?.enterTranslateID?.text.toString().trim()
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
                addDialog?.dismiss()
            } else {
                Toast.makeText(this, "Please enter both field", Toast.LENGTH_SHORT).show()
            }
        }
        addDialog?.cancelBtnID?.setOnClickListener {
            addDialog?.dismiss()
        }

        addDialog?.radioGroupTranslate?.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.radioButtonAll -> {
                    addDialog?.enterTranslateID?.isEnabled = true
                    addDialog?.enterEngID?.isEnabled = true
                    addDialog?.enterTranslateID?.text?.clear()
                    addDialog?.enterEngID?.text?.clear()
                    addDialog?.enterEngID?.removeTextChangedListener(textWatcher)
                    addDialog?.enterTranslateID?.removeTextChangedListener(textWatcher2)
                }
                R.id.radioButtonTranslate -> {
                    addDialog?.enterEngID?.isEnabled = false
                    addDialog?.enterTranslateID?.isEnabled = true
                    addDialog?.enterTranslateID?.text?.clear()
                    addDialog?.enterEngID?.text?.clear()
                    val translationConfigs = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.UKRAINIAN)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build()
                    translator = Translation.getClient(translationConfigs)
                    addDialog?.enterTranslateID?.addTextChangedListener(textWatcher2)
                    addDialog?.enterEngID?.removeTextChangedListener(textWatcher)

                }
                R.id.radioButtonEng -> {
                    addDialog?.enterTranslateID?.isEnabled = false
                    addDialog?.enterEngID?.isEnabled = true
                    addDialog?.enterTranslateID?.text?.clear()
                    addDialog?.enterEngID?.text?.clear()
                    val translationConfigs = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.UKRAINIAN)
                        .build()
                    translator = Translation.getClient(translationConfigs)
                    addDialog?.enterEngID?.addTextChangedListener(textWatcher)
                    addDialog?.enterTranslateID?.removeTextChangedListener(textWatcher2)
                }
            }
        }
    }

    private val textWatcher2 = object : TextWatcher {
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
            translator?.translate(addDialog?.enterTranslateID?.text.toString())
                ?.addOnSuccessListener {
                    addDialog?.enterEngID?.setText(it)
                }
                ?.addOnFailureListener {
                    it.printStackTrace()
                }
        }
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
            translator?.translate(addDialog?.enterEngID?.text.toString())
                ?.addOnSuccessListener {
                    addDialog?.enterTranslateID?.setText(it)
                }
                ?.addOnFailureListener {
                    it.printStackTrace()
                }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            fragmentSettingListener?.search(query)
        }
        return true
    }

    fun setSettingListener(fragmentSettingListener: FragmentSettingListener) {
        this.fragmentSettingListener = fragmentSettingListener
    }
}