package es.xyz.pokedexgps.service

import retrofit2.Call
import es.xyz.pokedexgps.model.Pokemon
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeapiService {
    @GET ("pokemon/{id}")
    fun getPokemonInfo(@Path("id") id: Int): Call<Pokemon>
}