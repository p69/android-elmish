package com.example.pavelshilyagov.tryelmish.search

import com.example.pavelshilyagov.tryelmish.services.Json
import java.util.*

data class SearchModel (val searchValue:String, val isLoading:Boolean, val current:Optional<CurrentWeatherModel>, val error:String)

data class WindCondition(val speed:Double, val direction:String)
data class Location(val name:String, val country:String)

data class CurrentWeatherModel(val location: Location, val temperature: Double, val feelsLike: Double, val wind: WindCondition, val pressure: Double, val humidity: Int)

sealed class SearchMsg {
    class OnTextChanged(val text: String) : SearchMsg()
    class SearchByCity : SearchMsg()
    class OnSearchSuccess(val result: Json.Response) : SearchMsg()
    class OnSearchError(val exc: Exception) : SearchMsg()
}
