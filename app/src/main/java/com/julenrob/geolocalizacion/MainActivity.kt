package com.julenrob.geolocalizacion

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions


class MainActivity : AppCompatActivity() , OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private lateinit var map: GoogleMap

    companion object {
        const val REQUEST_CODE_LOCALIZATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createFragment()
    }

    private fun createFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // createMarker()
        createPolylines()
        map.setOnMyLocationButtonClickListener (this)
        map.setOnMyLocationClickListener (this)
        enableLocation()
    }

    private fun createMarker(){
        val coordinates = LatLng(39.954518, -1.228578)
        val marker = MarkerOptions().position(coordinates).title("La casa del pueblo")
        map.addMarker(marker)
    }

    private fun isLocationPermissionsGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED


    private fun enableLocation(){
        if(!::map.isInitialized) return
        if (isLocationPermissionsGranted()){
            map.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCALIZATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            REQUEST_CODE_LOCALIZATION -> if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
            } else {
                Toast.makeText(this, "Para activar la localización acepta los permisos en ajustes", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if(!::map.isInitialized) return
        if (!isLocationPermissionsGranted()){
            map.isMyLocationEnabled = false
            Toast.makeText(this, "Para activar la localización acepta los permisos en ajustes", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Botón pulsado", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Estás en ${location.latitude} ${location.longitude}", Toast.LENGTH_SHORT).show()

    }

    private fun createPolylines() {
        val polylineOptions = PolylineOptions()
            .add(LatLng(40.419173113350965, -3.705976009368897))
            .add(LatLng(40.4150807746539, -3.706072568893432))
            .add(LatLng(40.41517062907432, -3.7012016773223873))
            .add(LatLng(40.41713105928677, -3.7037122249603267))
            .add(LatLng(40.41926296230622, -3.701287508010864))
            .add(LatLng(40.419173113350965, -3.7048280239105225))
            .add(LatLng(40.419173113350965, -3.705976009368897))
            .width(20f)
        val polyline = map.addPolyline(polylineOptions)
    }
}