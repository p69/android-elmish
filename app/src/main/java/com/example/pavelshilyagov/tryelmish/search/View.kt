package com.example.pavelshilyagov.tryelmish.search

import android.widget.LinearLayout
import com.example.pavelshilyagov.tryelmish.elmish.Dispatch
import kotlinx.coroutines.experimental.async
import trikita.anvil.BaseDSL
import trikita.anvil.DSL.*

object SearchUI {
    fun view(model: SearchModel, dispatcher: Dispatch<SearchMsg>) {
        linearLayout {
            orientation(LinearLayout.VERTICAL)
            editText {
                text(model.searchValue)
                BaseDSL.onTextChanged({ w -> async { dispatcher(SearchMsg.OnTextChanged(w.toString())) } })
            }
            button {
                text("search")
                onClick { async { dispatcher(SearchMsg.SearchByCity()) } }
            }
        }
    }
}
