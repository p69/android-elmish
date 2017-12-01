package com.example.pavelshilyagov.tryelmish.elmish

import android.util.Log
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.actor

data class Program<in TArg, TModel, TMsg, out TView>(
        val init: (TArg) -> Pair<TModel, Cmd<TMsg>>,
        val update: (TMsg, TModel) -> Pair<TModel, Cmd<TMsg>>,
        val subscribe: (TModel) -> Cmd<TMsg>,
        val view: (TModel, Dispatch<TMsg>) -> TView,
        val setState: (TModel, Dispatch<TMsg>) -> Unit,
        val onError: (Pair<String, Exception>) -> Unit
)


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

fun <TArg, TModel, TMsg, TView> mkSimple(
        init: (TArg) -> TModel,
        update: (TMsg, TModel) -> TModel,
        view: (TModel, Dispatch<TMsg>) -> TView): Program<TArg, TModel, TMsg, TView> {
    return Program(
            init = { arg -> Pair(init(arg), CmdF.none) },
            update = { msg, model -> Pair(update(msg, model), CmdF.none) },
            view = view,
            subscribe = { _ -> emptyList() },
            setState = { model, dispatcher -> view(model, dispatcher) },
            onError = { err -> Log.e("Program loop", err.first, err.second) }
    )
}

fun <TArg, TModel, TMsg, TView> mkProgramFromComponent(component: Component<TArg, TModel, TMsg, TView>): Program<TArg, TModel, TMsg, TView> {
    return Program(
            init = component::init,
            update = component::update,
            view = component::view,
            subscribe = { _ -> emptyList() },
            setState = { model, dispatcher -> component.view(model, dispatcher) },
            onError = { err -> Log.e("Program loop", err.first, err.second) }
    )
}

fun <TArg, TModel, TMsg, TView> (Program<TArg, TModel, TMsg, TView>).withSubscription(subscribe: (TModel) -> Cmd<TMsg>): Program<TArg, TModel, TMsg, TView> {
    fun sub(model: TModel): Cmd<TMsg> = CmdF.batch(this.subscribe(model), subscribe(model))
    return this.copy(
            subscribe = ::sub
    )
}

fun <TArg, TModel, TMsg, TView> (Program<TArg, TModel, TMsg, TView>).runWith(arg: TArg) {
    val program = this
    val (initialModel, initialEffects) = program.init(arg)
    var currentModel = initialModel
    val loop = actor<TMsg>(context = UI) {
        for (msg in channel) {
            try {
                val (updatedModel, effects) = program.update(msg, currentModel)
                currentModel = updatedModel
                program.setState(currentModel, { m ->  channel.send(m) })

                for (cmd in effects) {
                    cmd({ m ->channel.send(m)})
                }
            } catch (e: Exception) {
                program.onError(Pair("Failed while processing message.", e))
            }
        }
    }
    program.setState(initialModel, { m ->  loop.send(m) })

    val cmds = initialEffects + program.subscribe(initialModel)
    cmds.forEach { it({ m -> loop.send(m) }) }
}

fun <TModel, TMsg, TView> (Program<Unit, TModel, TMsg, TView>).run() = runWith(Unit)