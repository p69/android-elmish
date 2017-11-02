package com.example.pavelshilyagov.tryelmish.main

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
            val (updatedSearchModel, searchCmd) = Search.update(msg.msg, model.screen.model)
            return Pair(model.copy(screen = Screen.Search(updatedSearchModel)), searchCmd map Msg::Search)
        }
    }
    return Pair(model, CmdF.none())
}