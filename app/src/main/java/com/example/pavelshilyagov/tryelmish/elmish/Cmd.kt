package com.example.pavelshilyagov.tryelmish.elmish

import kotlinx.coroutines.experimental.async

typealias Dispatch<TMsg> = suspend (TMsg) -> Unit

typealias Sub<TMsg> = (Dispatch<TMsg>) -> Unit

typealias Cmd<TMsg> = List<Sub<TMsg>>

fun <TMsg> cmdOfMsg (msg:TMsg) : Cmd<TMsg> {
    return listOf({dispatch-> async { dispatch(msg)}})
}