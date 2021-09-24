package ar.edu.unlam.location.retrofit.responses

import com.google.gson.annotations.SerializedName

data class DirectionsDto(
    @SerializedName("geocoded_waypoints") val geocodedWaypoints : List<GeocodedWaypoints>,
    @SerializedName("routes") val routes : List<Routes>,
    @SerializedName("status") val status : String
)

data class Routes (

    @SerializedName("copyrights") val copyrights : String,
    @SerializedName("legs") val legs : List<Legs>,
    @SerializedName("overview_polyline") val overviewPolyline : Polyline,
    @SerializedName("summary") val summary : String,
    @SerializedName("warnings") val warnings : List<String>,
    @SerializedName("waypoint_order") val waypointOrder : List<String>
)

data class Legs (

    @SerializedName("distance") val distance : Distance,
    @SerializedName("duration") val duration : Duration,
    @SerializedName("end_address") val endAddress : String,
    @SerializedName("end_location") val endLocation : Location,
    @SerializedName("start_address") val startAddress : String,
    @SerializedName("start_location") val startLocation : Location,
    @SerializedName("steps") val steps : List<Steps>,
    @SerializedName("traffic_speed_entry") val trafficSpeedEntry : List<String>,
    @SerializedName("via_waypoint") val viaWaypoint : List<String>
)


data class Steps (

    @SerializedName("distance") val distance : Distance,
    @SerializedName("duration") val duration : Duration,
    @SerializedName("end_location") val endLocation : Location,
    @SerializedName("html_instructions") val htmlInstructions : String,
    @SerializedName("polyline") val polyline : Polyline,
    @SerializedName("start_location") val startLocation : Location,
    @SerializedName("travel_mode") val travelMode : String
)

data class Polyline (

    @SerializedName("points") val points : String
)


data class Distance (

    @SerializedName("text") val text : String,
    @SerializedName("value") val value : Int
)

data class Duration (

    @SerializedName("text") val text : String,
    @SerializedName("value") val value : Int
)

data class GeocodedWaypoints (

    @SerializedName("geocoder_status") val geocoderStatus : String,
    @SerializedName("place_id") val placeId : String,
    @SerializedName("types") val types : List<String>
)

data class Location (

    @SerializedName("lat") val lat : Double,
    @SerializedName("lng") val lng : Double
)