package com.example.stocksearch

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject

class StockDetailViewModel (): ViewModel() {

    val profileData: MutableStateFlow<JSONObject> = MutableStateFlow(JSONObject())
    val quoteData: MutableStateFlow<JSONObject> = MutableStateFlow(JSONObject())
    var profileDataLoaded = mutableStateOf(false)
    var quoteDataLoaded = mutableStateOf(false)

    fun fetchData(symbolInput:String) {
        try {
            profileDataLoaded.value=false
            quoteDataLoaded.value=false

            DataService.fetchProfileDataFromAPI(stockSymbol = symbolInput,
                callback = { response ->
                    profileData.update{response}
                    profileDataLoaded.value=true
                },
                errorCallback = { error ->
                }
            )


            DataService.fetchQuoteDataFromAPI(stockSymbol = symbolInput,
                callback = { response ->
                    quoteData.update{response}
                    quoteDataLoaded.value=true
                },
                errorCallback = { error ->
                }
            )










        } catch (e: Exception) {
            e.message?.let { Log.d("Error", it) }
        }





    }


}