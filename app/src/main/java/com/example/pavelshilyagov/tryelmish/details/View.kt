package com.example.pavelshilyagov.tryelmish.details

import com.example.pavelshilyagov.tryelmish.elmish.Dispatch
import trikita.anvil.DSL.*
import android.widget.LinearLayout

object DetailsUI {
    fun view(model: DetailsModel, dispatcher: Dispatch<DetailsMsg>) {
        linearLayout {
            toolbar {
                text(model.weather.location.name)
            }
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