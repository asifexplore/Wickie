package com.example.Wickie.data.source.data


data class LocationClass (
    var longitude: Double,
    var latitude: Double
)
{
    var results: FloatArray? = FloatArray(1)

    // 1.3774 | 103.8488 | 1.4443 / 103.7838
    // 1.4507° N, 103.8232° E
    fun distance(lat1: Double, lon1: Double, lat2: Double = 1.4507, lon2: Double = 103.8232): Boolean {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return convertToKm(dist) < 2
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
    private fun convertToKm(dist : Double) : Double
    {
        return (dist * 1.60934)
    }
}