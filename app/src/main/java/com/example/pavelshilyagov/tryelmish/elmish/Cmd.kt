package com.example.pavelshilyagov.tryelmish.elmish

import com.example.pavelshilyagov.tryelmish.then
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking

typealias Dispatch<TMsg> = suspend (TMsg) -> Unit

typealias Sub<TMsg> = (Dispatch<TMsg>) -> Unit

typealias Cmd<TMsg> = List<Sub<TMsg>>


object CmdF {
    fun <TMsg> none(): Cmd<TMsg> = emptyList()

    fun <TMsg> ofMsg (msg:TMsg) : Cmd<TMsg> = listOf({ dispatch-> async { dispatch(msg)}})

    fun <TMsg> ofSub (sub:Sub<TMsg>) : Cmd<TMsg> = listOf(sub)

    fun <T, TMsg> map(f: (T) -> TMsg, cmd: Cmd<T>): Cmd<TMsg> {
        return cmd.map { dispatcher:(Dispatch<T>) -> Unit ->
            val dispatcherMapper:(Dispatch<TMsg>)->Dispatch<T> = { dispatch: Dispatch<TMsg> -> { x:T -> async { dispatch(f(x)) } } }
            dispatcherMapper then dispatcher
        }
    }

    infix fun <T, TMsg> Cmd<T>.map(f: (T) -> TMsg): Cmd<TMsg> = map(f, this)

    fun <TMsg> batch(cmds:Collection<Cmd<TMsg>>) : Cmd<TMsg> = cmds.flatten()

    fun <TArg, TResult, TMsg> ofAsyncFunc(
            task: suspend (TArg) -> Deferred<TResult>,
            arg: TArg,
            ofSuccess: (TResult) -> TMsg,
            ofError: (Exception) -> TMsg): Cmd<TMsg> {
        fun bind(dispatch: Dispatch<TMsg>) = runBlocking(Unconfined) {
            try {
                val res = task(arg).await()
                dispatch(ofSuccess(res))
            } catch (ex: Exception) {
                dispatch(ofError(ex))
            }
        }
        return listOf(::bind)
    }

    fun <TArg, TResult, TMsg> ofFunc(
            task: (TArg) -> TResult,
            arg: TArg,
            ofSuccess: (TResult) -> TMsg,
            ofError: (Exception) -> TMsg): Cmd<TMsg> {
        fun bind(dispatch: Dispatch<TMsg>) {
            try {
                val res = task(arg)
                async{dispatch(ofSuccess(res))}
            } catch (ex: Exception) {
                async{dispatch(ofError(ex))}
            }
        }
        return listOf(::bind)
    }

    fun <TArg, TResult, TMsg> performFunc(
            task: (TArg) -> TResult,
            arg: TArg,
            ofSuccess: (TResult) -> TMsg
            ): Cmd<TMsg> {
        fun bind(dispatch: Dispatch<TMsg>) {
            try {
                val res = task(arg)
                async{dispatch(ofSuccess(res))}
            } catch (_:Exception){}
        }
        return listOf(::bind)
    }

    fun <TArg, TMsg> attemptFunc(
            task: (TArg) -> Unit,
            arg: TArg,
            ofError: (Exception) -> TMsg
    ): Cmd<TMsg> {
        fun bind(dispatch: Dispatch<TMsg>) {
            try {
                task(arg)
            } catch (ex: Exception) {
                async{dispatch(ofError(ex))}
            }
        }
        return listOf(::bind)
    }
}
