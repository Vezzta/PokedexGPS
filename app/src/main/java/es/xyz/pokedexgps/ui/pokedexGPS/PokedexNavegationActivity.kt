package es.xyz.pokedexgps.ui.pokedexGPS

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import es.xyz.pokedexgps.R
import es.xyz.pokedexgps.databinding.ActivityPokedexNavegationBinding
import es.xyz.pokedexgps.ui.Pokedex
import es.xyz.pokedexgps.ui.pokedexRandom.PokedexProfileActivity

class PokedexNavegationActivity : AppCompatActivity() {

    //Se declaran los permisos de localización para posteriormente confirmar que estén aceptados
    private val permissionFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permissionCoarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION

    //Variable de localización inicial para medir movimiento
    var locationA:  Location? = null

    private val CODE = 100

    var fusedLocationClient: FusedLocationProviderClient? = null

    var locationReuest: LocationRequest? = null

    var callBack: LocationCallback? = null

    private lateinit var binding: ActivityPokedexNavegationBinding

    //Url de gif
    val url = "https://pa1.narvii.com/6582/359cd31650aefae5604e9d14d624a48059f93d03_hq.gif"

    var media: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokedexNavegationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        media = MediaPlayer.create(this, R.raw.road)
        media?.start()


        Glide.with(this).load(url).into(binding.imageView)

        fusedLocationClient = FusedLocationProviderClient(this)
        locationReuest = null

        initLocationRequest()


    }


    //onClick para el botón Back
    fun onClickBack(view: View){
        if(media!=null){
            media?.stop()
            media?.release()
            media = null
        }
        stopLocation()
        finish()
        val intent = Intent(this, Pokedex::class.java)
        startActivity(intent)
    }

    //Inicialización de valores para locationRequest
    private fun initLocationRequest(){
        locationReuest = LocationRequest()
        locationReuest?.interval = 1000
        locationReuest?.fastestInterval = 5000
        locationReuest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    //funcion que retorna si hay permisos en FineLocation y CoarseLocation
    private fun validPermissionLocation(): Boolean{
        val preciseLocation = ActivityCompat.checkSelfPermission(this, permissionFineLocation) == PackageManager.PERMISSION_GRANTED
        val ordinaryLocation = ActivityCompat.checkSelfPermission(this, permissionCoarseLocation) == PackageManager.PERMISSION_GRANTED

        return preciseLocation && ordinaryLocation
    }

    //Se obtiene location y se ejecuta instrucción para cada caso
    @SuppressLint("MissingPermission")
    private fun getLocation(){


        //Se obtiene location inicial
       fusedLocationClient?.lastLocation?.addOnSuccessListener(this, object: OnSuccessListener<Location>{
           override fun onSuccess(location: Location?) {
               if(location != null){

                   locationA = location

               }
           }

       })

        //Se obtiene location cada segundo
        callBack = object: LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)


                //Comparando la ubicación del primer location con las actualizaciones (m) se da instrucción para cada caso
                if (locationA?.distanceTo(locationResult?.lastLocation)!! < 40.0){
                    binding.textStatus.setTextColor(Color.DKGRAY)
                    binding.textStatus.text = "No hay Pokémon cerca"

                }
                if (locationA?.distanceTo(locationResult?.lastLocation)!! >= 40.0 && locationA?.distanceTo(locationResult?.lastLocation)!! < 75.0){
                    binding.textStatus.setTextColor(Color.GREEN)
                    binding.textStatus.text = "Se escucha un Pokémon cerca"
                }
                if (locationA?.distanceTo(locationResult?.lastLocation)!! >= 75.0 && locationA?.distanceTo(locationResult?.lastLocation)!! < 100.0){
                    binding.textStatus.setTextColor(Color.BLUE)
                    binding.textStatus.text = "Hay un Pokémon muy cerca!!"
                }
                if (locationA?.distanceTo(locationResult?.lastLocation)!! >= 100.0){
                    val intent = Intent(applicationContext, PokedexProfileActivity::class.java)
                    intent.putExtra("from", "Navegation")

                    //Código que permite vibrar
                    val vibrator = applicationContext?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        vibrator.vibrate(200)
                    }
                    //----------

                    //detiene audio
                    if(media!=null){
                        media?.stop()
                        media?.release()
                        media = null
                    }

                    locationA = locationResult?.lastLocation //location inicial se vuelve la última tomada
                    finish()
                    startActivity(intent)

                }else{

                    //Toast.makeText(applicationContext, locationA?.distanceTo(locationResult?.lastLocation)!!.toString() + " m", Toast.LENGTH_LONG).show()
                }

            }
        }

        fusedLocationClient?.requestLocationUpdates(locationReuest, callBack, null)
    }

    //funciones que solicitan el permiso
    private fun requestPerm(){
        val request = ActivityCompat.shouldShowRequestPermissionRationale(this, permissionFineLocation)

        if (request){
            solicitPermission()
        }else{
            solicitPermission()
        }
    }

    private fun solicitPermission(){
        requestPermissions(arrayOf(permissionFineLocation, permissionCoarseLocation), CODE)
    }

    //Si se otorgan los permisos, esta función manda a llamar el getLocation
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            CODE ->{
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //obtener Ubicacion
                    getLocation()
                }else{
                    Toast.makeText(this, "Permisos no aceptados", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //Función que detiene la obtención de location
    private fun stopLocation(){
        fusedLocationClient?.removeLocationUpdates(callBack)
    }

    //Inicia el proceso de obtención de location
    override fun onStart() {
        super.onStart()

        if(validPermissionLocation()){
            getLocation()
        }else{
            requestPerm()
        }

    }

    //detiene la obtención de location
    override fun onPause() {
        super.onPause()
        stopLocation()
    }
}


