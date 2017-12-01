package com.example.pavelshilyagov.tryelmish.main

import com.example.pavelshilyagov.tryelmish.details.Details
import com.example.pavelshilyagov.tryelmish.elmish.Cmd
import com.example.pavelshilyagov.tryelmish.elmish.CmdF
import com.example.pavelshilyagov.tryelmish.elmish.CmdF.map
import com.example.pavelshilyagov.tryelmish.search.Search

object Main {
    fun update(msg: Msg, model: MainModel): Pair<MainModel, Cmd<Msg>> {
        when {
            model.screen is Screen.Search && msg is Msg.Search -> {
                val (updatedSearchModel, searchCmd, parentCmd) = Search.update(msg.msg, model.screen.model)
                return Pair(model.copy(screen = Screen.Search(updatedSearchModel)), CmdF.batch(searchCmd map Msg::Search, parentCmd))
            }
            model.screen is Screen.Search && msg is Msg.GoToDetails -> {
                model.backStack.push(model.screen)
                val detailsModel = Details.init(msg.details)
                val detailsScreen = Screen.Details(detailsModel)
                return Pair(model.copy(screen = detailsScreen), CmdF.none)
            }
            msg is Msg.GoBack -> {
                val fromStack = if (model.backStack.empty()) null else model.backStack.pop()
                return if (fromStack == null) {
                    Pair(model, CmdF.ofMsg(Msg.Exit))
                } else {
                    return Pair(model.copy(screen = fromStack), CmdF.none)
                }
            }
        }
        return Pair(model, CmdF.none)
    }
}