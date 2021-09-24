package ar.edu.unlam.location.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.location.repositories.DirectionsRepository
import ar.edu.unlam.location.retrofit.responses.DirectionsDto
import com.google.android.gms.maps.model.LatLng


import kotlinx.coroutines.launch

class MapViewModel(val repository: DirectionsRepository) : ViewModel() {

    val currentDirections = MutableLiveData<DirectionsDto>(null)

    fun retrieveDirections(origin: LatLng, destination: LatLng) {
        viewModelScope.launch {
            val directions = repository.retrieveDirections(origin, destination)
            currentDirections.value = directions
        }

    }

}