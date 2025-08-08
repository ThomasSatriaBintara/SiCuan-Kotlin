package com.example.sicuan.viewmodel

import androidx.lifecycle.*
import com.example.sicuan.model.response.Penjualan
import com.example.sicuan.model.response.Profile
import com.example.sicuan.model.response.SalesSummaryData
import com.example.sicuan.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> get() = _profile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _salesSummary = MutableLiveData<SalesSummaryData>()
    val salesSummary: LiveData<SalesSummaryData> get() = _salesSummary

    private val _sales = MutableLiveData<List<Penjualan>>()
    val sales: LiveData<List<Penjualan>> get() = _sales

    private val _chartPenjualan = MutableLiveData<List<Penjualan>>()
    val chartPenjualan: LiveData<List<Penjualan>> get() = _chartPenjualan

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

    fun fetchSalesSummary() {
        viewModelScope.launch {
            try {
                val response = repository.getSalesSummary()
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        _salesSummary.value = it
                    }
                } else {
                    _error.value = response.message()
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun fetchSalesToday() {
        viewModelScope.launch {
            try {
                val response = repository.getSalesToday()
                if (response.isSuccessful) {
                    val data = response.body()?.data?.sales ?: emptyList()
                    _sales.value = data
                } else {
                    _error.value = response.message()
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

}
