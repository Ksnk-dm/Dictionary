package com.ksnk.dictionary.ui.settingsFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ksnk.dictionary.R
import com.ksnk.dictionary.enums.ThemeValues
import kotlinx.android.synthetic.main.name_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private val settingsViewModel: SettingsViewModel by viewModel()
    private var setThemeRadioGroup: RadioGroup? = null
    private var imageButtonClearAll: ImageButton? = null
    private var nameTextView: TextView? = null
    private var countWordTextView: TextView? = null
    private var editNameImageButton: ImageButton? = null
    private var nameEditText: EditText? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        setCountTextView()
        setListeners()
        setRadioButtonCheck()
    }

    private fun init(view: View) {
        imageButtonClearAll = view.findViewById(R.id.imageButtonClearAll)
        nameTextView = view.findViewById(R.id.nameTextView)
        countWordTextView = view.findViewById(R.id.countWortTextView)
        editNameImageButton = view.findViewById(R.id.imageButtonEditName)
        nameEditText = view.findViewById(R.id.nameEditText)
        nameEditText?.setText(settingsViewModel.getNameProfile())
        setThemeRadioGroup = view.findViewById(R.id.setThemeRadioGroup)
    }

    private fun setCountTextView() {
        settingsViewModel.getAll().observe(viewLifecycleOwner, Observer {
            countWordTextView?.text = it.size.toString()
        })
    }

    private fun setListeners() {
        editNameImageButton?.setOnClickListener {
            if (nameEditText?.isEnabled == false) {
                nameEditText?.isEnabled = true
                editNameImageButton?.setImageResource(R.drawable.ic_baseline_done_24)
            } else {
                nameEditText?.isEnabled = false
                editNameImageButton?.setImageResource(R.drawable.ic_baseline_edit_24)
                settingsViewModel.setNameProfile(nameEditText?.text.toString())
            }
        }
        imageButtonClearAll?.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                settingsViewModel.deleteAllWords()
            }

        }

        setThemeRadioGroup?.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rbDark -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    settingsViewModel.setSetTheme(ThemeValues.THEME_DARK.value)
                }
                R.id.rbAuto -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    settingsViewModel.setSetTheme(ThemeValues.THEME_AUTO.value)
                }
                R.id.rbLight -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    settingsViewModel.setSetTheme(ThemeValues.THEME_LIGHT.value)
                }
            }
        }
    }

    private fun setRadioButtonCheck() {
        when (settingsViewModel.getTheme()) {
            0 -> {
                setThemeRadioGroup?.check(R.id.rbAuto)
            }
            1 -> {
                setThemeRadioGroup?.check(R.id.rbDark)
            }
            2 -> {
                setThemeRadioGroup?.check(R.id.rbLight)
            }
        }
    }


}