package com.gustavopimentel.colorspaces.util

import android.content.Context
import android.content.SharedPreferences
import com.gustavopimentel.colorspaces.ui.spaces.SpacesViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object StateStorage {
    private const val PREFS_NAME = "saved_states_prefs"
    private const val KEY_STATES = "saved_states"

    private val gson = Gson()

    fun saveStates(context: Context, states: List<SpacesViewModel.SavedState>) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val json = gson.toJson(states)
        editor.putString(KEY_STATES, json)
        editor.apply()
    }

    fun loadStates(context: Context): List<SpacesViewModel.SavedState>? {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_STATES, null) ?: return null
        val type: Type = object : TypeToken<List<SpacesViewModel.SavedState>>() {}.type
        return gson.fromJson(json, type)
    }
}
