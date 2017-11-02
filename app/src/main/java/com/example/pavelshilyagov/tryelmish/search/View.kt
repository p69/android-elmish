package com.example.pavelshilyagov.tryelmish.search

import android.widget.LinearLayout
import com.example.pavelshilyagov.tryelmish.elmish.Dispatch
import kotlinx.coroutines.experimental.async
import trikita.anvil.DSL.*

object SearchUI {
    fun view(model: SearchModel, dispatcher: Dispatch<SearchMsg>) {
        linearLayout {
            orientation(LinearLayout.VERTICAL)
            editText {
                onTextChanged({ w -> async { dispatcher(SearchMsg.OnTextChanged(w.toString())) } })
            }
            button {
                text("search")
                onClick { async { dispatcher(SearchMsg.SearchByCity()) } }
                enabled(!model.isLoading && model.searchValue.isNotEmpty())
            }
            textView {
                model.current.ifPresent {
                    text("Current temperature in ${model.searchValue} is ${it.temperature}℃")
                }.also {
                    if (model.error.isNotEmpty()) {
                        text("Error: ${model.error}")
                    }
                }
            }
            button {
                text("Show details")
                visibility(model.current.isPresent)
            }
        }
    }
}
