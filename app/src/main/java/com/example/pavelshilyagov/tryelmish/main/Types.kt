package com.example.pavelshilyagov.tryelmish.main

import com.example.pavelshilyagov.tryelmish.details.DetailsModel
import com.example.pavelshilyagov.tryelmish.details.DetailsMsg
import com.example.pavelshilyagov.tryelmish.search.CurrentWeatherModel
import com.example.pavelshilyagov.tryelmish.search.SearchModel
import com.example.pavelshilyagov.tryelmish.search.SearchMsg
import java.util.*

sealed class Screen {
    class Search(val model:SearchModel) : Screen()
    class Details(val model: DetailsModel) : Screen()
    class ScreenTransition(val from:Screen, val to:Screen, val isBack:Boolean) : Screen()
}

data class MainModel (val screen:Screen, val backStack: Stack<Screen>)

sealed class Msg {
    class Search(val msg:SearchMsg) : Msg()
    class Details(val msg:DetailsMsg) : Msg()
    class GoToDetails(val details:CurrentWeatherModel) : Msg()
    class CompleteTransition(val from:Screen, val to:Screen) : Msg()
    class GoBack : Msg()
}

