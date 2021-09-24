package ar.edu.unlam.location.repositories.utils

import com.google.android.gms.maps.model.LatLng

fun LatLng.toUrlParam(): String {
    return "${this.latitude},${this.longitude}"
}