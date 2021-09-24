package ar.edu.unlam.location.repositories

import ar.edu.unlam.location.retrofit.responses.DirectionsDto
import com.google.android.gms.maps.model.LatLng


interface DirectionsRepository {
    suspend fun retrieveDirections(origin: LatLng, destination: LatLng): DirectionsDto
}