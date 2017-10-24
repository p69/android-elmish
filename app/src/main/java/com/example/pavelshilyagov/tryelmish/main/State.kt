package com.example.pavelshilyagov.tryelmish.main

import com.example.pavelshilyagov.tryelmish.search.Search

fun init(): MainModel {
    return MainModel(screen = Screen.Search(Search.init()))
}

fun update(msg: Msg, model: MainModel): MainModel {
    when {
        model.screen is Screen.Search && msg is Msg.Search -> {
            val updatedSearchModel = Search.update(msg.msg, model.screen.model)
            return model.copy(screen = Screen.Search(updatedSearchModel))
        }
    }
    return model
}
