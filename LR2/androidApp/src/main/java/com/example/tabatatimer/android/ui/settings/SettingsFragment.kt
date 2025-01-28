package com.example.tabatatimer.android.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.tabatatimer.android.MainActivity
import com.example.tabatatimer.android.R
import com.example.tabatatimer.android.utils.clearAll
import com.example.tabatatimer.android.utils.setFont
import com.example.tabatatimer.android.utils.setLocale
import java.util.Locale

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        findPreference<Preference>("clearData")?.setOnPreferenceClickListener {
            clearAppData()
            true
        }
        updateThemeSummary()
        updateFontSizeSummary()
        updateLanguageSummary()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            "theme" -> applyTheme(sharedPreferences)
            "fontSize" -> applyFontSize(sharedPreferences)
            "language" -> applyLanguage(sharedPreferences)
            "clearData" -> clearAppData()
        }
    }

    private fun applyTheme(sharedPreferences: SharedPreferences?) {
        val themeValue = sharedPreferences?.getString("theme", "light")
        val nightMode = when (themeValue) {
            "dark" -> AppCompatDelegate.MODE_NIGHT_YES
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            "system" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else -> AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().startActivity(Intent(requireContext(), MainActivity::class.java))
    }

    private fun updateThemeSummary() {
        val themePreference: ListPreference? = findPreference("theme")
        themePreference?.summary = themePreference?.entry
    }

    private fun applyFontSize(sharedPreferences: SharedPreferences?) {
        val fontSizeValue = sharedPreferences?.getString("fontSize", "medium")
        val size = when (fontSizeValue) {
            "small" -> 10f
            "medium" -> 20f
            "large" -> 30f
            else -> 20f
        }
        sharedPreferences?.edit()?.putFloat("font_size_pref", size)?.apply()
        setFont(size)
        val config = Configuration(resources.configuration)
        config.fontScale = size / 16f
        requireActivity().apply {
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
            recreate()
        }
    }

    private fun updateFontSizeSummary() {
        val fontSizePreference: ListPreference? = findPreference("fontSize")
        fontSizePreference?.summary = fontSizePreference?.entry
    }

    private fun applyLanguage(sharedPreferences: SharedPreferences?) {
        val language = sharedPreferences?.getString("language", "en") ?: "en"
        setLocale(language)
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        requireActivity().baseContext.resources.updateConfiguration(config, requireActivity().resources.displayMetrics)
        requireActivity().title = getString(R.string.title_settings)
        requireActivity().recreate()
    }

    private fun updateLanguageSummary() {
        findPreference<ListPreference>("language")?.summary = findPreference<ListPreference>("language")?.entry
    }

    private fun clearAppData() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.clear_data_title)
            .setMessage(R.string.clear_data_message)
            .setPositiveButton(R.string.clear) { _, _ ->
                val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
                sharedPrefs.edit().clear().apply()

                clearAll()

                requireActivity().startActivity(Intent(requireContext(), MainActivity::class.java))
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}