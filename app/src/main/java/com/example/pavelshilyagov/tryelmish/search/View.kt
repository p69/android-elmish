package com.example.pavelshilyagov.tryelmish.search

import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.pavelshilyagov.tryelmish.elmish.Dispatch
import io.michaelrocks.optional.Optional
import kotlinx.coroutines.experimental.async
import trikita.anvil.Anvil
import trikita.anvil.DSL.*

object SearchUI {
    fun view(model: SearchModel, dispatcher: Dispatch<SearchMsg>) {
        linearLayout {
            orientation(LinearLayout.VERTICAL)
            editText {
                init {
                    Anvil.currentView<EditText>().setText(model.searchValue, TextView.BufferType.EDITABLE)
                }
                onTextChanged({ w -> async { dispatcher(SearchMsg.OnTextChanged(w.toString())) } })
            }
            button {
                text("search")
                onClick { async { dispatcher(SearchMsg.SearchByCity()) } }
                enabled(!model.isLoading && model.searchValue.isNotEmpty())
            }
            textView {
                when(model.current) {
                    is Optional.Some -> {
                        text("Current temperature in ${model.searchValue} is ${model.current.value.temperature} ℃")
                    }
                    is Optional.None -> {
                        if (model.error.isNotEmpty()) {
                            text("Error: ${model.error}")
                        }
                    }
                }
            }
            button {
                text("Show details")
                when(model.current) {
                    is Optional.Some -> {
                        visibility(true)
                        onClick { async { dispatcher(SearchMsg.ShowDetails(model.current.value)) } }
                    }
                    is Optional.None -> {
                        visibility(false)
                    }
                }
            }
        }
    }
}