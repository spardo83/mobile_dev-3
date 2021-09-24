package ar.edu.unlam.location.repositories.impl

import ar.edu.unlam.location.repositories.DirectionsRepository
import ar.edu.unlam.location.repositories.utils.toUrlParam
import ar.edu.unlam.location.retrofit.RetrofitApi
import ar.edu.unlam.location.retrofit.responses.DirectionsDto
import com.google.android.gms.maps.model.LatLng

class DirectionsRepositoryImpl(private val retrofitApi: RetrofitApi) : DirectionsRepository {

    override suspend fun retrieveDirections(origin: LatLng, destination: LatLng): DirectionsDto {
        return retrofitApi.getDirectionsApi()
            .getDirections(origin.toUrlParam(), destination.toUrlParam())
    }
}