package com.example.data.model
import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("aired")
    val aired: Aired? = Aired(),
    @SerializedName("airing")
    val airing: Boolean? = false,
    @SerializedName("approved")
    val approved: Boolean? = false,
    @SerializedName("background")
    val background: String? = "",
    @SerializedName("broadcast")
    val broadcast: Broadcast? = Broadcast(),
    @SerializedName("demographics")
    val demographics: List<Demographic>? = listOf(),
    @SerializedName("duration")
    val duration: String? = "",
    @SerializedName("episodes")
    val episodes: Int? = 0,
    @SerializedName("explicit_genres")
    val explicitGenres: List<Any>? = listOf(),
    @SerializedName("favorites")
    val favorites: Int? = 0,
    @SerializedName("genres")
    val genres: List<Genre>? = listOf(),
    @SerializedName("images")
    val images: Images? = Images(),
    @SerializedName("licensors")
    val licensors: List<Licensor>? = listOf(),
    @SerializedName("mal_id")
    val malId: Int? = 0,
    @SerializedName("members")
    val members: Int? = 0,
    @SerializedName("popularity")
    val popularity: Int? = 0,
    @SerializedName("producers")
    val producers: List<Producer>? = listOf(),
    @SerializedName("rank")
    val rank: Int? = 0,
    @SerializedName("rating")
    val rating: String? = "",
    @SerializedName("score")
    val score: Double? = 0.0,
    @SerializedName("scored_by")
    val scoredBy: Int? = 0,
    @SerializedName("season")
    val season: String? = "",
    @SerializedName("source")
    val source: String? = "",
    @SerializedName("status")
    val status: String? = "",
    @SerializedName("studios")
    val studios: List<Studio>? = listOf(),
    @SerializedName("synopsis")
    val synopsis: String? = "",
    @SerializedName("themes")
    val themes: List<Theme>? = listOf(),
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("title_english")
    val titleEnglish: String? = "",
    @SerializedName("title_japanese")
    val titleJapanese: String? = "",
    @SerializedName("title_synonyms")
    val titleSynonyms: List<String>? = listOf(),
    @SerializedName("titles")
    val titles: List<Title>? = listOf(),
    @SerializedName("trailer")
    val trailer: Trailer? = Trailer(),
    @SerializedName("type")
    val type: String? = "",
    @SerializedName("url")
    val url: String? = "",
    @SerializedName("year")
    val year: Int? = 0,
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("character")
    val character: Character? = Character(),
    @SerializedName("role")
    val role: String? = "",
    @SerializedName("voice_actors")
    val voiceActors: List<VoiceActor>? = listOf(),
    @SerializedName("episode")
    val episode: String? = "",
    @SerializedName("about")
    val about: String? = "",
    @SerializedName("anime")
    val anime: List<Anime>? = listOf(),
    @SerializedName("manga")
    val manga: List<Manga>? = listOf(),
    @SerializedName("name_kanji")
    val nameKanji: String? = "",
    @SerializedName("nicknames")
    val nicknames: List<Any>? = listOf(),
    @SerializedName("voices")
    val voices: List<Voice>? = listOf()
)