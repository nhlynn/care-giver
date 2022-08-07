package com.nhlynn.care_giver.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhlynn.care_giver.network.response.HospitalResponse
import com.nhlynn.care_giver.repository.CareRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NearestHospitalViewModel
@Inject constructor(private val repository: CareRepository) : ViewModel() {
    private var _response = MutableLiveData<HospitalResponse>()

    val myResponse: LiveData<HospitalResponse> get() = _response

    fun getNearestHospital(lat: Double, lng: Double) {
        viewModelScope.launch {
            try {
                repository.nearestHospital(lat, lng).let {
                    if (it.isSuccessful) {
                        _response.postValue(it.body())
                    } else {
                        val mResponse = HospitalResponse()
                        mResponse.status = it.code()
                        mResponse.message = "${it.code()} ${it.message()}"
                        _response.postValue(mResponse)
                    }
                }
            } catch (e: Exception) {
                val mResponse = HospitalResponse()
                mResponse.status = 400
                if (e.message?.lowercase(Locale.ENGLISH) != "job was cancelled") {
                    if (e.message.toString().lowercase(Locale.ENGLISH)
                            .contains("unable to resolve host") ||
                        e.message.toString().lowercase(Locale.ENGLISH)
                            .contains("failed to connect to")
                    ) {
                        mResponse.message = null
                    } else {
                        mResponse.message = e.toString()
                    }
                }
                _response.postValue(mResponse)
            }
        }
    }
}