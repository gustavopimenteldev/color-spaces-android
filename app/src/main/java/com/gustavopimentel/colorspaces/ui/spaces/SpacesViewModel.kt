package com.gustavopimentel.colorspaces.ui.spaces

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gustavopimentel.colorspaces.util.StateStorage

class SpacesViewModel(application: Application) : AndroidViewModel(application) {

    private val _savedStates = MutableLiveData<MutableList<SavedState>>()
    val savedStates: MutableLiveData<MutableList<SavedState>> = _savedStates

    private val _activeStateIndex = MutableLiveData(0)
    val activeStateIndex: LiveData<Int> = _activeStateIndex

    init {
        loadStates()
    }

    data class SavedState(
        val cornerColors: MutableList<Int>,
        var isSubtractiveMixing: Boolean
    )

    private fun loadStates() {
        val loadedStates = StateStorage.loadStates(getApplication())
        if (loadedStates != null) {
            _savedStates.value = loadedStates.toMutableList()
        } else {
            _savedStates.value = mutableListOf(
                SavedState(mutableListOf(Color.RED, Color.GREEN, Color.BLUE), false),
                SavedState(mutableListOf(Color.CYAN, Color.MAGENTA, Color.YELLOW), true),
                *Array(8) { SavedState(mutableListOf(Color.RED, Color.GREEN, Color.BLUE), false) }
            )
        }
    }

    private fun saveStates() {
        _savedStates.value?.let { StateStorage.saveStates(getApplication(), it) }
    }

    fun switchToState(index: Int) {
        val states = _savedStates.value ?: return
        if (index in states.indices) {
            _activeStateIndex.value = index
            saveStates()
        }
    }

    fun updateActiveStateColors(newColors: List<Int>, isSubtractive: Boolean) {
        _savedStates.value?.let { states ->
            val currentState = states[_activeStateIndex.value ?: 0]
            currentState.cornerColors.clear()
            currentState.cornerColors.addAll(newColors)
            currentState.isSubtractiveMixing = isSubtractive
            _savedStates.value = states
            saveStates()
        }
    }

    fun updateActiveStateMixing(isSubtractive: Boolean) {
        _savedStates.value?.let { states ->
            val currentState = states[_activeStateIndex.value ?: 0]
            currentState.isSubtractiveMixing = isSubtractive
            _savedStates.value = states
            saveStates()
        }
    }
}
