package com.example.pavelshilyagov.tryelmish.elmish

interface Component<in TArg, TModel, TMsg, out TView> {
    fun init(args: TArg): UpdateResult<TModel, TMsg>
    fun update(msg: TMsg, model: TModel): UpdateResult<TModel, TMsg>
    fun view(model: TModel, dispatch: Dispatch<TMsg>): TView
}