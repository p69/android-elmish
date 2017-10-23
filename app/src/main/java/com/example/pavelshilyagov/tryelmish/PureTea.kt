package com.example.pavelshilyagov.tryelmish.pure

import android.widget.LinearLayout
import com.example.pavelshilyagov.tryelmish.Elmish.Cmd
import com.example.pavelshilyagov.tryelmish.Elmish.Dispatch
import com.example.pavelshilyagov.tryelmish.Elmish.cmdOfMsg
import kotlinx.coroutines.experimental.async
import trikita.anvil.DSL.*

sealed class Msg {
    object INCREMENT : Msg()
    object DECREMENT : Msg()
}

data class Model(val counter: Int = 0)

fun init(): Pair<Model, Cmd<Msg>> {
    return Pair(Model(), cmdOfMsg(Msg.INCREMENT))
}

fun update(msg:Msg,model:Model):Model {
    return when (msg) {
        is Msg.INCREMENT -> Model(model.counter + 1)
        is Msg.DECREMENT -> Model(model.counter - 1)
    }
}

fun view(model: Model, dispatch: Dispatch<Msg>){
    linearLayout {
        orientation(LinearLayout.VERTICAL)
        button {
            text("increment")
            onClick { async { dispatch(Msg.INCREMENT) }}
        }

        button {
            text("decrement")
            onClick { async { dispatch(Msg.DECREMENT) }}
        }
        textView {
            text(model.counter.toString())
        }
    }
}