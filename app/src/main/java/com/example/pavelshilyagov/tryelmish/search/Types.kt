package com.example.pavelshilyagov.tryelmish.search

import com.example.pavelshilyagov.tryelmish.services.Json

data class SearchModel (val searchValue:String, val isLoading:Boolean, val currentTemp:String)

sealed class SearchMsg {
    class OnTextChanged(val text: String) : SearchMsg()
    class SearchByCity : SearchMsg()
    class OnSearchSuccess(val result: Json.Current) : SearchMsg()
    class OnSearchError(val exc: Exception) : SearchMsg()
}
