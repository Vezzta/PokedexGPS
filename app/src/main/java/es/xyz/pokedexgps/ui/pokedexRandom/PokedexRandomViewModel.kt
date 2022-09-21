package es.xyz.pokedexgps.ui.pokedexRandom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.xyz.pokedexgps.model.Pokemon
import es.xyz.pokedexgps.service.PokeapiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class PokedexRandomViewModel() : ViewModel() {

    //Retrofit
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: PokeapiService = retrofit.create(PokeapiService::class.java)

    val pokemonData = MutableLiveData<Pokemon>()

    fun getPokemonData(id:Int){
        val call = service.getPokemonInfo(id)

        call.enqueue(object : Callback<Pokemon>{
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                response.body()?.let { pokemon ->
                    pokemonData.postValue(pokemon)
                }
            }

            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                call.cancel()
            }
        })
    }
}