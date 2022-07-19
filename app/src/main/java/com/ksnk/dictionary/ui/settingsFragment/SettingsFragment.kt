package com.ksnk.dictionary.ui.settingsFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageButtonClearAll = view.findViewById(R.id.imageButtonClearAll)
        nameTextView = view.findViewById(R.id.nameTextView)
        countWordTextView = view.findViewById(R.id.countWortTextView)
        editNameImageButton = view.findViewById(R.id.imageButtonEditName)
        val profileDialog: AlertDialog =  AlertDialog.Builder(requireContext()).create()
        val viewLayout = LayoutInflater.from(requireContext()).inflate(R.layout.name_profile, null)
        profileDialog?.setView(viewLayout)
        editNameImageButton?.setOnClickListener {
            profileDialog?.show()
        }
        profileDialog?.editTextTextPersonName?.setText(settingsViewModel.getNameProfile())
        profileDialog?.saveNameBtnID?.setOnClickListener {
            settingsViewModel.setNameProfile(profileDialog.editTextTextPersonName?.text.toString())
            profileDialog?.dismiss()
        }
        imageButtonClearAll?.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                settingsViewModel.deleteAllWords()
            }

        }
        setThemeRadioGroup = view.findViewById(R.id.setThemeRadioGroup)
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
}