package com.example.pavelshilyagov.tryelmish.search

import com.example.pavelshilyagov.tryelmish.elmish.Cmd
import com.example.pavelshilyagov.tryelmish.elmish.CmdF
import com.example.pavelshilyagov.tryelmish.main.Msg
import com.example.pavelshilyagov.tryelmish.services.Json
import com.example.pavelshilyagov.tryelmish.services.WeatherApi
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import io.michaelrocks.optional.Optional


object Search {
    fun init(): SearchModel {
        return SearchModel("", false, Optional.None, error = "")
    }

    fun update(msg: SearchMsg, model: SearchModel): Triple<SearchModel, Cmd<SearchMsg>, Cmd<Msg>> {
        return when (msg) {
            is SearchMsg.OnTextChanged -> Triple(model.copy(searchValue = msg.text), CmdF.none(), CmdF.none())
            is SearchMsg.SearchByCity -> {
                val requestCmd = CmdF.ofAsyncFunc(
                        {q->requestByQuery(q)},
                        model.searchValue,
                        SearchMsg::OnSearchSuccess,
                        SearchMsg::OnSearchError)
                Triple(model.copy(isLoading = true), requestCmd, CmdF.none())
            }
            is SearchMsg.OnSearchSuccess ->
                Triple(model.copy(isLoading = false, current = Optional.Some(createCurrentWeatherModel(msg.result))), CmdF.none(), CmdF.none())
            is SearchMsg.OnSearchError ->
                Triple(model.copy(isLoading = false, current = Optional.None, error = msg.exc.toString()), CmdF.none(), CmdF.none())
            is SearchMsg.ShowDetails -> Triple(model, CmdF.none(), CmdF.ofMsg(Msg.GoToDetails(msg.details)))
        }
    }
}

private suspend fun requestByQuery(query:String): Deferred<Json.Response> = async {
    val (_,_,res) = Fuel.request(WeatherApi.currentFor(query)).responseObject(deserializer = Json.CurrentDeserializer())
    return@async when (res) {
        is Result.Success -> res.value
        is Result.Failure -> throw res.error
    }
}

private fun createCurrentWeatherModel(response: Json.Response): CurrentWeatherModel {
    return CurrentWeatherModel(
            Location(response.location.name, response.location.country),
            response.current.temperature,
            response.current.feelsLikeTemp,
            WindCondition(response.current.windsSpeed, response.current.windsDirection),
            response.current.pressure,
            response.current.humidity)
}