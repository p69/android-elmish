package com.example.pavelshilyagov.tryelmish.main

import com.example.pavelshilyagov.tryelmish.ScreenWithTransition
import com.example.pavelshilyagov.tryelmish.details.DetailsUI
import com.example.pavelshilyagov.tryelmish.elmish.Dispatch
import com.example.pavelshilyagov.tryelmish.elmish.ElmishLithoView
import com.example.pavelshilyagov.tryelmish.search.SearchUI
import com.facebook.litho.ComponentContext
import com.facebook.litho.widget.Text


object MainLithoUI {
    fun view(model: MainModel, ctx: ComponentContext, dispatch: Dispatch<Msg>): ElmishLithoView {
        val (screen, key) = createView(model.screen, ctx, dispatch)
        return ElmishLithoView.ComponentView(ScreenWithTransition.create(ctx).screen(screen).screenKey(key).build())
    }

    private fun createView(screen: Screen, ctx: ComponentContext, dispatch: Dispatch<Msg>): Pair<ElmishLithoView, String> =
            when (screen) {
                is Screen.Search -> Pair(SearchUI.view(screen.model, ctx, { msg -> dispatch(Msg.Search(msg)) }), "search-view")
                is Screen.Details -> Pair(DetailsUI.view(screen.model, ctx, { msg -> dispatch(Msg.Details(msg)) }), "details-view")
                else -> Pair(ElmishLithoView.ComponentView(Text.create(ctx).text("Unknown screen").build()), "unknown-view")
            }
}