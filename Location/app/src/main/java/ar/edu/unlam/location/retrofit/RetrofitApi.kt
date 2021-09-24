package ar.edu.unlam.location.retrofit

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApi {
    fun getDirectionsApi(): DirectionsApi =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .build()
            .create(DirectionsApi::class.java)
}

