package com.example.pavelshilyagov.tryelmish.main

import com.example.pavelshilyagov.tryelmish.MainActivity
import com.example.pavelshilyagov.tryelmish.details.DetailsUI
import com.example.pavelshilyagov.tryelmish.elmish.Dispatch
import com.example.pavelshilyagov.tryelmish.search.SearchUI
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.verticalLayout

class MainActivityComponent : AnkoComponent<MainActivity> {

    private lateinit var model: MainModel
    private lateinit var dispatcher: Dispatch<Msg>

    fun setState(model: MainModel, dispatcher: Dispatch<Msg>) {
        this.model = model
        this.dispatcher = dispatcher
    }

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        verticalLayout  {
            val currentModel = model
            when (currentModel.screen) {
                is Screen.Search -> SearchUI.view(currentModel.screen.model, { msg -> dispatcher(Msg.Search(msg)) })
                is Screen.Details -> DetailsUI.view(currentModel.screen.model, { msg -> dispatcher(Msg.Details(msg)) })
            }
        }

    }

}

