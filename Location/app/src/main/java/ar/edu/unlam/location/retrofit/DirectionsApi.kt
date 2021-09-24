package ar.edu.unlam.location.retrofit

import ar.edu.unlam.location.BuildConfig
import ar.edu.unlam.location.retrofit.responses.DirectionsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsApi {

    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String = BuildConfig.MAPS_API_KEY
    ) : DirectionsDto
}