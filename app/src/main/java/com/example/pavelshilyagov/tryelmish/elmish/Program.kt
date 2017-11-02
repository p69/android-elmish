package com.example.pavelshilyagov.tryelmish.elmish

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.actor
import trikita.anvil.Anvil
import trikita.anvil.RenderableView

data class Program<in TArg, TModel, TMsg, out TView>(
        val init: (TArg) -> Pair<TModel, Cmd<TMsg>>,
        val update: (TMsg, TModel) -> Pair<TModel, Cmd<TMsg>>,
        val subscribe: (TModel) -> Cmd<TMsg>,
        val view: (TModel, Dispatch<TMsg>) -> TView,
        val setState: (TModel, Dispatch<TMsg>) -> Unit,
        val onError: (Pair<String, Exception>) -> Unit
)

@SuppressLint("ViewConstructor")
class AnvilRenderer<in TModel, out TMsg> @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, private val view: (TModel, Dispatch<TMsg>) -> Unit
) : RenderableView(context, attrs, defStyleAttr) {
    override fun view() {
        if (model != null) {
            view(model!!, dispatcher)
        }
    }

    private var model: TModel? = null
    private var dispatcher: Dispatch<TMsg> = { _ -> }

    fun setState(newState: TModel, dispatch: Dispatch<TMsg>) {
        model = newState
        dispatcher = dispatch
    }
}

fun <TArg, TModel, TMsg, TView> mkProgram(
        init: (TArg) -> Pair<TModel, Cmd<TMsg>>,
        update: (TMsg, TModel) -> Pair<TModel, Cmd<TMsg>>,
        view: (TModel, Dispatch<TMsg>) -> TView): Program<TArg, TModel, TMsg, TView> {
    return Program(
            init = init,
            update = update,
            view = view,
            subscribe = { _ -> emptyList() },
            setState = { model, dispatcher -> view(model, dispatcher) },
            onError = { err -> Log.e("Program loop", err.first, err.second) }
    )
}

fun <TArg, TModel, TMsg> withAnvil(program: Program<TArg, TModel, TMsg, Unit>, container: View, ctx: Context)
        : Program<TArg, TModel, TMsg, Unit> {
    val renderer = AnvilRenderer(ctx, view = program.view)
    Anvil.mount(container, renderer)
    return program.copy(setState = { m, d ->
        renderer.setState(m, d)
        Anvil.render()
    })
}

fun <TArg, TModel, TMsg, TView> runWith(arg: TArg, program: Program<TArg, TModel, TMsg, TView>) {
    val (initialModel, initialCmds) = program.init(arg)
    var currentModel = initialModel
    val loop = actor<TMsg>(context = UI) {
        for (msg in channel) {
            try {
                val (updatedModel, cmds) = program.update(msg, currentModel)
                currentModel = updatedModel
                async {
                    program.setState(currentModel, { m ->  channel.send(m) })
                    for (cmd in cmds) {
                        cmd({ m ->channel.send(m)})
                    }
                }
            } catch (e: Exception) {
                program.onError(Pair("Failed while processing message.", e))
            }
        }
    }
    async {
        program.setState(initialModel, { m ->  loop.send(m) })
        Anvil.render() // render action is invoked automatically after any UI events
        program.subscribe(initialModel)
        initialCmds.forEach { it({ m -> loop.send(m) }) }
    }
}

fun <TModel, TMsg, TView> run(program: Program<Unit, TModel, TMsg, TView>) {
    return runWith(Unit, program)
}