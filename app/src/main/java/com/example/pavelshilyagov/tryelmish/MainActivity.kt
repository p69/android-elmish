package com.example.pavelshilyagov.tryelmish

import com.example.pavelshilyagov.tryelmish.elmish.*
import com.example.pavelshilyagov.tryelmish.main.*
import com.trello.navi2.Event
import com.trello.navi2.rx.RxNavi
import kotlinx.coroutines.experimental.async

class MainActivity : NaviRxActivity() {
    init {
        RxNavi.observe(this, Event.CREATE).subscribe({ _ ->
            setContentView(R.layout.activity_main)
            startWithComponent(MainComponent(this))
        })
    }

    private fun startWithComponent(mainComponent: MainComponent) {
        mkProgramFromComponent(mainComponent)
                .withSubscription(this::subscription)
                .withAnvil(findViewById(R.id.content), this)
                .run()
    }

    private fun subscription(model: MainModel): Cmd<Msg> {
        val sub: Sub<Msg> = { dispatcher ->
            RxNavi.observe(this, Event.BACK_PRESSED)
                    .subscribe({ _ -> async { dispatcher(Msg.GoBack()) } })
        }

        return CmdF.ofSub(sub)
    }
}


