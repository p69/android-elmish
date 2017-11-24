package com.example.pavelshilyagov.tryelmish.main

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import com.example.pavelshilyagov.tryelmish.MainActivity
import com.example.pavelshilyagov.tryelmish.R
import com.example.pavelshilyagov.tryelmish.details.Details
import com.example.pavelshilyagov.tryelmish.details.DetailsUI
import com.example.pavelshilyagov.tryelmish.elmish.Cmd
import com.example.pavelshilyagov.tryelmish.elmish.CmdF
import com.example.pavelshilyagov.tryelmish.elmish.CmdF.map
import com.example.pavelshilyagov.tryelmish.elmish.Component
import com.example.pavelshilyagov.tryelmish.elmish.Dispatch
import com.example.pavelshilyagov.tryelmish.search.Search
import com.example.pavelshilyagov.tryelmish.search.SearchUI
import kotlinx.coroutines.experimental.async
import trikita.anvil.Anvil
import trikita.anvil.DSL.linearLayout
import trikita.anvil.DSL.orientation
import java.util.*


class MainComponent(private val activity:MainActivity): Component<Unit, MainModel, Msg, Unit> {

    override fun init(args: Unit): Pair<MainModel, Cmd<Msg>> {
        val initialScreen = Screen.Search(Search.init())
        val backStack = Stack<Screen>()
        val model = MainModel(initialScreen, backStack)
        return Pair(model, CmdF.none())
    }

    override fun update(msg: Msg, model: MainModel): Pair<MainModel, Cmd<Msg>> {
        when {
            model.screen is Screen.Search && msg is Msg.Search -> {
                val (updatedSearchModel, searchCmd, parentCmd) = Search.update(msg.msg, model.screen.model)
                return Pair(model.copy(screen = Screen.Search(updatedSearchModel)), CmdF.batch(listOf(searchCmd map Msg::Search, parentCmd)))
            }
            model.screen is Screen.Search && msg is Msg.GoToDetails -> {
                model.backStack.push(model.screen)
                val detailsModel = Details.init(msg.details)
                val detailsScreen = Screen.Details(detailsModel)
                return Pair(model.copy(screen = Screen.ScreenTransition(model.screen, detailsScreen, false)), CmdF.none())
            }
            msg is Msg.GoBack -> {
                val fromStack = model.backStack.pop()
                if (fromStack == null) {
                    activity.finish()
                } else {
                    return Pair(model.copy(screen = Screen.ScreenTransition(model.screen, fromStack, true)), CmdF.none())
                }
            }
            msg is Msg.CompleteTransition -> {
                return Pair(model.copy(screen = msg.to), CmdF.none())
            }
        }
        return Pair(model, CmdF.none())
    }

    override fun view(model: MainModel, dispatch: Dispatch<Msg>) {
        linearLayout {
            orientation(LinearLayout.VERTICAL)

            if (model.screen is Screen.ScreenTransition) {
                renderScreen(model.screen.from, dispatch)
                val slideOutAnimation = createSlideOutAnimation(model.screen.isBack){
                    async {dispatch(Msg.CompleteTransition(model.screen.from, model.screen.to))}
                }
                Anvil.currentView<View>().startAnimation(slideOutAnimation)
            } else {
                renderScreen(model.screen, dispatch)
            }
        }
    }

    private fun renderScreen(screen:Screen, dispatch: Dispatch<Msg>) {
        when (screen) {
            is Screen.Search -> SearchUI.view(screen.model, { msg -> dispatch(Msg.Search(msg)) })
            is Screen.Details -> DetailsUI.view(screen.model, { msg -> dispatch(Msg.Details(msg)) })
        }
    }



    private fun createSlideOutAnimation(isBack: Boolean, onEndedAction: () -> Unit): Animation {
        val animation =
                AnimationUtils
                        .loadAnimation(activity, if (isBack) R.anim.slide_out_to_left else R.anim.slide_out_to_right)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
                //no-op
            }

            override fun onAnimationStart(p0: Animation?) {
                //no-op
            }

            override fun onAnimationEnd(p0: Animation?) {
                onEndedAction()
            }

        })
        return animation
    }

}