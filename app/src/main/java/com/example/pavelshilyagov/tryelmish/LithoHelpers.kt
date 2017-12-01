package com.example.pavelshilyagov.tryelmish

import com.example.pavelshilyagov.tryelmish.elmish.ElmishLithoView
import com.facebook.litho.*
import com.facebook.litho.animation.AnimatedProperties
import com.facebook.litho.animation.DimensionValue
import com.facebook.litho.annotations.*
import com.facebook.litho.widget.EditText
import com.facebook.litho.widget.TextChangedEvent

fun (Component.Builder<*, *>).clickHandler(handler: () -> Unit): Component.Builder<*, *> {
    val dispatcher = createEventDispatcher(handler)
    this.clickHandler(EventHandler(dispatcher, "elmishClickHandler", 1, null))
    return this
}

fun (EditText.Builder).textChangedEventHandler(handler: (String) -> Unit): Component.Builder<*, *> {
    val dispatcher = createEventDispatcher(handler)
    this.textChangedEventHandler(EventHandler<TextChangedEvent>(dispatcher, "elmishTextChangedHandler", 1, null))
    return this
}

fun createEventDispatcher(handler: () -> Unit): HasEventDispatcher {
    val clickDispatcher = EventDispatcher { _, _ ->
        handler()
        Any()
    }

    return HasEventDispatcher { clickDispatcher }
}

fun createEventDispatcher(handler: (String) -> Unit): HasEventDispatcher {
    val clickDispatcher = EventDispatcher { _, eventState ->
        if (eventState is TextChangedEvent) {
            handler(eventState.text)
        }

        Any()
    }

    return HasEventDispatcher { clickDispatcher }
}

@LayoutSpec
class ScreenWithTransitionSpec {
    companion object {
        @JvmStatic
        @OnCreateLayout
        fun onCreateLayout(ctx: ComponentContext, @Prop screen: ElmishLithoView, @Prop screenKey: String): ComponentLayout {
            val layout = when (screen) {
                is ElmishLithoView.ComponentView -> Column.create(ctx).child(screen.component)
                is ElmishLithoView.ComponentLayoutView -> Column.create(ctx).child(screen.componentLayout)
            }
            return layout.transitionKey(screenKey).build()
        }

        @JvmStatic
        @OnCreateTransition
        fun onCreateTransition(c: ComponentContext): Transition {
            return Transition
                    .create(Transition.allKeys())
                    .animate(AnimatedProperties.ALPHA)
                    .appearFrom(0f)
                    .disappearTo(0f)
                    .animate(AnimatedProperties.X)
                    .appearFrom(DimensionValue.offsetDip(c, -100))
                    .disappearTo(DimensionValue.offsetDip(c,100))
        }
    }
}