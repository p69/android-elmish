package com.example.pavelshilyagov.tryelmish.search


object Search {
    fun init(): SearchModel {
        return SearchModel("", false)
    }

    fun update(msg:SearchMsg, model:SearchModel): SearchModel {
        return when(msg) {
            is SearchMsg.OnTextChanged -> model.copy(searchValue = msg.text)
            is SearchMsg.SearchByCity ->
                //TODO: start searching
                model.copy(isLoading = true)
            is SearchMsg.OnSearhcResults ->
                //TODO: show results
                model.copy(isLoading = false)
        }
    }
}
