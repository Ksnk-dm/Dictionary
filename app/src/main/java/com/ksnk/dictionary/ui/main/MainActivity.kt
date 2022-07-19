package com.ksnk.dictionary.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.ksnk.dictionary.listeners.FragmentSettingListener
import com.ksnk.dictionary.R
import com.ksnk.dictionary.data.entity.Word
import com.ksnk.dictionary.ui.listFragment.ListWordFragment
import com.ksnk.dictionary.ui.settingsFragment.SettingsFragment
import com.ksnk.dictionary.ui.translateFragment.TranslateFragment
import com.ksnk.dictionary.utils.changeFragment
import com.ksnk.dictionary.utils.showToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.addword.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private var translator: Translator? = null
    private val mainViewModel: MainViewModel by viewModel()
    private var imageButtonAdd: ImageButton? = null
    private var textToSpeech: TextToSpeech? = null
    private var searchView: SearchView? = null
    private var fragmentSettingListener: FragmentSettingListener? = null
    private var bottomNavView: BottomNavigationView? = null
    private var addDialog: AlertDialog? = null
    private var floatingActionButtonMain: FloatingActionButton? = null
    private var toolBarMain: MaterialToolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkTheme()
        init()
        setListeners()
        checkTextSpeechSupportLang()
    }

    private fun checkTheme() {
        when (mainViewModel.getTheme()) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }


    private fun checkTextSpeechSupportLang() {
        textToSpeech = TextToSpeech(applicationContext) { i ->
            if (i == TextToSpeech.SUCCESS) {
                val lang = textToSpeech!!.setLanguage(Locale.US)
                if (lang == TextToSpeech.LANG_MISSING_DATA || lang == TextToSpeech.LANG_NOT_SUPPORTED) {
                    showToast("Language is not supported")
                }
            }
        }
    }

    private fun init() {
        searchView = findViewById(R.id.word_search)
        bottomNavView = findViewById(R.id.bottomNavigationViewMain)
        toolBarMain = findViewById(R.id.materialToolbarMain)
        //  imageButtonAdd = findViewById(R.id.imageButtonAdd)
        floatingActionButtonMain = findViewById(R.id.floatingActionButtonMain)
    }

    private fun setListeners() {
        bottomNavView?.setOnItemSelectedListener(bottomNavViewOnItemSelectListener)
        searchView?.setOnQueryTextListener(this)
        floatingActionButtonMain?.setOnClickListener { showDialogBox() }
        // imageButtonAdd?.setOnClickListener { showDialogBox() }
    }


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
                showToast("Please enter both field")
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

    override fun onBackPressed() {
        //  super.onBackPressed()
    }

//    private fun changeFragment(fragment: Fragment) {
//        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fragmentContainerViewMain, fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }

    private var bottomNavViewOnItemSelectListener = NavigationBarView.OnItemSelectedListener {
        when (it.itemId) {
            R.id.translate_item -> {
                val fragment: Fragment = TranslateFragment()
                changeFragment(fragment)
                floatingActionButtonMain?.hide()
                toolBarMain?.visibility = View.GONE
            }
            R.id.list_item -> {
                val fragment: Fragment = ListWordFragment().newInstance()
                changeFragment(fragment)
                floatingActionButtonMain?.show()
                toolBarMain?.visibility = View.VISIBLE
            }
            R.id.setting_item -> {
                val fragment: Fragment = SettingsFragment()
                changeFragment(fragment)
                floatingActionButtonMain?.hide()
                toolBarMain?.visibility = View.GONE
            }
        }
        return@OnItemSelectedListener true
    }
}