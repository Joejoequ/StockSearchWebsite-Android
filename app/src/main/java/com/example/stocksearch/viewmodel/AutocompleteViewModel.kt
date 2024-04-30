package com.example.stocksearch.viewmodel

import DataService
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.stocksearch.Stock
import kotlinx.coroutines.flow.MutableStateFlow

class AutocompleteViewModel() : ViewModel() {


    var autocompleteResultMap: MutableStateFlow<HashMap<String, List<Stock>>> =
        MutableStateFlow(HashMap())


    fun fetchData(symbolInput: String) {
        try {

            if (autocompleteResultMap.value.containsKey(symbolInput)) {
                Log.d("Autocomplete", "already have result, request not sent")
            } else {
                DataService.fetchAutocompleteDataFromAPI(stockSymbol = symbolInput,
                    callback = { response ->


                        val stockList = mutableListOf<Stock>()

                        for (i in 0 until response.length()) {
                            val stockObject = response.getJSONObject(i)
                            val ticker = stockObject.getString("symbol")
                            val name = stockObject.getString("description")


                            val stock = Stock(ticker, name, 0.0, 0.0, 0.0)
                            stockList.add(stock)
                        }
                        if (autocompleteResultMap.value.size >= 50) {
                            val newMap = HashMap<String, List<Stock>>()
                            newMap[symbolInput] = stockList

                            autocompleteResultMap.value = newMap
                        } else {
                            val updatedStocksMap = HashMap(autocompleteResultMap.value)
                            updatedStocksMap[symbolInput] = stockList
                            autocompleteResultMap.value = updatedStocksMap
                        }

                    },
                    errorCallback = { error ->

                    }
                )
            }
        } catch (e: Exception) {
            e.message?.let { Log.d("Error", it) }
        }
    }


}




