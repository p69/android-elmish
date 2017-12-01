package com.example.pavelshilyagov.tryelmish.main

import android.support.v4.app.SupportActivity
import com.example.pavelshilyagov.tryelmish.elmish.*
import com.example.pavelshilyagov.tryelmish.hideVirtualKeyboard
import com.example.pavelshilyagov.tryelmish.search.Search
import com.facebook.litho.ComponentContext
import java.util.*


class MainLithoComponent(private val context:ComponentContext, private val activity: SupportActivity) : Component<Unit, MainModel, Msg, ElmishLithoView> {

    override fun init(args: Unit): Pair<MainModel, Cmd<Msg>> {
        val initialScreen = Screen.Search(Search.init())
        val backStack = Stack<Screen>()
        val model = MainModel(initialScreen, backStack)
        return Pair(model, CmdF.none)
    }

    override fun update(msg: Msg, model: MainModel): Pair<MainModel, Cmd<Msg>> {
        return when(msg) {
            is Msg.Exit -> {
                activity.finish()
                Pair(model, CmdF.none)
            }
            is Msg.HideVirtualKeyboard -> {
                activity.hideVirtualKeyboard()
                Pair(model, CmdF.none)
            }
            else -> Main.update(msg, model)
        }
    }

    override fun view(model: MainModel, dispatch: Dispatch<Msg>): ElmishLithoView = MainLithoUI.view(model, context, dispatch)
}