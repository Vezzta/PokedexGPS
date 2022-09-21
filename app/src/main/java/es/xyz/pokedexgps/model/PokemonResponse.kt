package es.xyz.pokedexgps.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @Expose @SerializedName("results") val results: List<PokemonResponse>
)

data class PokeResult(
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("url") val url: String
)
