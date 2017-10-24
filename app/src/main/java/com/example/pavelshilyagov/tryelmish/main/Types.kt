package com.example.pavelshilyagov.tryelmish.main

import com.example.pavelshilyagov.tryelmish.search.SearchModel
import com.example.pavelshilyagov.tryelmish.search.SearchMsg

sealed class Screen {
    class Search(val model:SearchModel) : Screen()
}

data class MainModel (val screen:Screen)

sealed class Msg {
    class Search(val msg:SearchMsg) : Msg()
}

