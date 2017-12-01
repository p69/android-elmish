package com.example.pavelshilyagov.tryelmish.details

import com.example.pavelshilyagov.tryelmish.elmish.Dispatch
import com.example.pavelshilyagov.tryelmish.elmish.ElmishLithoView
import com.example.pavelshilyagov.tryelmish.search.CurrentWeatherModel
import com.facebook.litho.Column
import com.facebook.litho.ComponentContext
import com.facebook.litho.widget.Text

object DetailsUI {
    fun view(model: DetailsModel, ctx: ComponentContext, dispatcher: Dispatch<DetailsMsg>): ElmishLithoView = ElmishLithoView.ComponentLayoutView(
        Column.create(ctx)
                .child(Text.create(ctx)
                        .isSingleLine(false)
                        .maxLines(10)
                        .textSizeDip(16f)
                        .text(createDescriptionText(model.weather)))
                .build()
    )

    private fun createDescriptionText(weather: CurrentWeatherModel): String =
            """Temperature: ${weather.temperature} ℃ / feels like ${weather.feelsLike} ℃
Wind: ${weather.wind.speed} km/h ${weather.wind.direction}
Pressure: ${weather.pressure} mb
Humidity: ${weather.humidity}%"""

}