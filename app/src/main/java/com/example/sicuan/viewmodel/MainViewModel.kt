package com.example.sicuan.viewmodel

import androidx.lifecycle.*
import com.example.sicuan.model.response.Profile
import com.example.sicuan.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> get() = _profile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchProfile() {
        viewModelScope.launch {
            try {
                val response = repository.getProfile()
                if (response.isSuccessful) {
                    response.body()?.data?.profile?.let {
                        _profile.value = it
                    }
                } else {
                    _error.value = response.message()
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
