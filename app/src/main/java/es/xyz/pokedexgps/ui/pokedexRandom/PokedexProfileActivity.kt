package es.xyz.pokedexgps.ui.pokedexRandom

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import es.xyz.pokedexgps.R
import es.xyz.pokedexgps.databinding.ActivityPokedexProfileBinding
import es.xyz.pokedexgps.ui.Pokedex
import es.xyz.pokedexgps.ui.pokedexGPS.PokedexNavegationActivity
import java.util.Random

class PokedexProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: PokedexRandomViewModel
    private lateinit var binding: ActivityPokedexProfileBinding

    var media: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        media = MediaPlayer.create(this, R.raw.wild_pokemon)
        media?.start()

        binding = ActivityPokedexProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(PokedexRandomViewModel::class.java)

        initUI()
    }

    private fun initUI(){

        //Obtención de número aleatorio para ID
        var aleatorio = Random()
        var id = aleatorio.nextInt(898) + 1

        //obtiene los datos del modelView y le pasa argumento id aleatorio
        viewModel.getPokemonData(id)

        //Observer, manda el texto e imagen obtenidos del getPokemonData
        viewModel.pokemonData.observe(this, Observer { pokemon ->
            binding.nameText.text = "#${id} "+pokemon.name
            Glide.with(this).load(pokemon.sprites.frontDefault).into(binding.pokemonImage)
        })
    }

    //método del botón back
    fun onClickBack(view: View){
        if(media!=null){
            media?.stop()
            media?.release()
            media = null
        }

        var dato = intent.getStringExtra("from")
        if (dato == "Navegation"){
            intent = Intent(this, PokedexNavegationActivity::class.java)
        }else{
            intent = Intent(this, Pokedex::class.java)
        }
        finish()
        startActivity(intent)
    }
}