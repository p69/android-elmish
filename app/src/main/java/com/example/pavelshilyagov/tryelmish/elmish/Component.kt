package com.example.pavelshilyagov.tryelmish.elmish

interface Component<in TArg, TModel, TMsg, out TView> {
    fun init(args: TArg): Pair<TModel, Cmd<TMsg>>
    fun update(msg: TMsg, model: TModel): Pair<TModel, Cmd<TMsg>>
    fun view(model: TModel, dispatch: Dispatch<TMsg>): TView
}