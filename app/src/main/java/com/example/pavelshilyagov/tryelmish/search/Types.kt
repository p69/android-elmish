package com.example.pavelshilyagov.tryelmish.search

data class SearchModel (val searchValue:String, val isLoading:Boolean)

sealed class SearchMsg {
    class OnTextChanged(val text: String) : SearchMsg()
    class SearchByCity : SearchMsg()
    class OnSearhcResults(val isOk: Boolean) : SearchMsg()
}
