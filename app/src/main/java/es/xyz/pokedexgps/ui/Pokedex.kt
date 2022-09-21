package es.xyz.pokedexgps.ui

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import es.xyz.pokedexgps.R
import es.xyz.pokedexgps.ui.pokedexGPS.PokedexNavegationActivity
import es.xyz.pokedexgps.ui.pokedexRandom.PokedexProfileActivity

class Pokedex : AppCompatActivity() {

    var media: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokedex)

        media = MediaPlayer.create(this, R.raw.title)
        media?.start()
    }

    //manda a activity Mostrar Pokemon
    fun onClickIniciar(view: View){
        if(media!=null){
            media?.stop()
            media?.release()
            media = null
        }
        val intent = Intent(this, PokedexProfileActivity::class.java)
        finish()
        startActivity(intent)
    }

    //Cierra app
    fun onClickClose(view: View){
        finish()
        System.exit(0)
    }

    //Mnada a activity Buscar Pokemon
    fun onClickGPS(view: View){
        if(media!=null){
            media?.stop()
            media?.release()
            media = null
        }
        val intent = Intent(this, PokedexNavegationActivity::class.java)
        finish()
        startActivity(intent)
    }
}