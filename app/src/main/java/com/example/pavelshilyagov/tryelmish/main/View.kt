package com.example.pavelshilyagov.tryelmish.main

import android.widget.LinearLayout
import com.example.pavelshilyagov.tryelmish.elmish.Dispatch
import com.example.pavelshilyagov.tryelmish.search.SearchUI
import trikita.anvil.DSL.*

fun view(model:MainModel, dispatcher:Dispatch<Msg>) {
    linearLayout {
        orientation(LinearLayout.VERTICAL)
        when(model.screen) {
            is Screen.Search -> SearchUI.view(model.screen.model, { msg -> dispatcher(Msg.Search(msg)) })
        }
    }
}