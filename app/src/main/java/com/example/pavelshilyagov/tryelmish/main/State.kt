package com.example.pavelshilyagov.tryelmish.main

import com.example.pavelshilyagov.tryelmish.details.Details
import com.example.pavelshilyagov.tryelmish.elmish.Cmd
import com.example.pavelshilyagov.tryelmish.elmish.CmdF
import com.example.pavelshilyagov.tryelmish.elmish.CmdF.map
import com.example.pavelshilyagov.tryelmish.search.Search

fun init(): MainModel {
    return MainModel(screen = Screen.Search(Search.init()))
}

fun update(msg: Msg, model: MainModel): Pair<MainModel, Cmd<Msg>> {
    when {
        model.screen is Screen.Search && msg is Msg.Search -> {
            val (updatedSearchModel, searchCmd, parentCmd) = Search.update(msg.msg, model.screen.model)
            return Pair(model.copy(screen = Screen.Search(updatedSearchModel)), CmdF.batch(listOf(searchCmd map Msg::Search, parentCmd)))
        }
        model.screen is Screen.Search && msg is Msg.GoToDetails -> {
            val detailsModel = Details.init(msg.details)
            return Pair(MainModel(Screen.Details(detailsModel)), CmdF.none())
        }
    }
    return Pair(model, CmdF.none())
}