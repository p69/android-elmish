package com.example.pavelshilyagov.tryelmish.elmish


import android.support.v4.app.SupportActivity
import com.facebook.litho.*
import com.facebook.litho.Component
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.Prop


sealed class ElmishLithoView {
    data class ComponentView(val component: Component<*>) : ElmishLithoView()
    data class ComponentLayoutView(val componentLayout: ComponentLayout) : ElmishLithoView()
}

sealed class RootModel {
    class None : RootModel()
    data class Some(val appModel: Any, val dispatcher: Dispatch<*>, val view: (Any, Dispatch<*>) -> ElmishLithoView) : RootModel()
}

@LayoutSpec
class LithoRootComponentSpec {

    companion object {
        @JvmStatic
        @OnCreateLayout
        fun onCreateLayot(context: ComponentContext, @Prop model: RootModel): ComponentLayout = when(model) {
            is RootModel.None -> Column.create(context).build()
            is RootModel.Some -> {
                val child = model.view(model.appModel, model.dispatcher)
                when (child) {
                    is ElmishLithoView.ComponentView -> Column.create(context).child(child.component).build()
                    is ElmishLithoView.ComponentLayoutView -> Column.create(context).child(child.componentLayout).build()
                }
            }
        }
    }

}

fun <TArg, TModel, TMsg> (Program<TArg, TModel, TMsg, ElmishLithoView>).withLitho(activity: SupportActivity, context: ComponentContext)
        : Program<TArg, TModel, TMsg, ElmishLithoView> {

    val rootComponent = LithoRootComponent.create(context).model(RootModel.None()).build()
    val rootView = LithoView.create(context, rootComponent)
    activity.setContentView(rootView)

    return this.copy(
            setState = { m, d ->
                val newState = RootModel.Some(m as Any, d as Dispatch<*>, { model, dispatcher -> this.view(model as TModel, dispatcher as Dispatch<TMsg>) })
                val root = LithoRootComponent.create(context).model(newState).build()
                rootView.setComponent(root)
            })
}