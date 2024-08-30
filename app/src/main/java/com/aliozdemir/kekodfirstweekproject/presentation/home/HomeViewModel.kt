package com.aliozdemir.kekodfirstweekproject.presentation.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _isEgoChecked = MutableLiveData(true)
    val isEgoChecked: LiveData<Boolean> = _isEgoChecked

    private val _bottomNavVisibility = MutableLiveData(View.GONE)
    val bottomNavVisibility: LiveData<Int> = _bottomNavVisibility

    fun onEgoSwitchChanged(isChecked: Boolean) {
        _isEgoChecked.value = isChecked
        updateBottomNavVisibility(isChecked)
    }

    fun handleOtherSwitchChange(isChecked: Boolean) {
        if (isChecked && _isEgoChecked.value == true) {
            _isEgoChecked.value = true
        }
    }

    fun getInitialEgoState(): Boolean = _isEgoChecked.value ?: true

    private fun updateBottomNavVisibility(isEgoChecked: Boolean) {
        _bottomNavVisibility.value = if (isEgoChecked) View.GONE else View.VISIBLE
    }
}
