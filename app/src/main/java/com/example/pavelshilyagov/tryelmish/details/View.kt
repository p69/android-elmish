package com.example.pavelshilyagov.tryelmish.details

import com.example.pavelshilyagov.tryelmish.elmish.Dispatch
import trikita.anvil.DSL.*
import android.widget.LinearLayout
import trikita.anvil.appcompat.v7.AppCompatv7DSL.actionBarContainer
import trikita.anvil.appcompat.v7.AppCompatv7DSL.toolbar

object DetailsUI {
    fun view(model: DetailsModel, dispatcher: Dispatch<DetailsMsg>) {
        actionBarContainer {
            toolbar {
                title(model.weather.location.name)
            }
        }
        linearLayout {
            orientation(LinearLayout.VERTICAL)
            with(model.weather) {
                textView {
                    text("Temperature: ${this.temperature} ℃ / feels like ${this.feelsLike} ℃")
                }
                textView {
                    text("Wind: ${this.wind.speed} km/h ${this.wind.direction}")
                }
                textView {
                    text("Pressure: ${this.pressure} mb")
                }
                textView {
                    text("Humidity: ${this.humidity}%")
                }
            }

        }
    }
}