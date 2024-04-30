package com.example.stocksearch.viewmodel

import DataService
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.stocksearch.Stock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class WatchlistViewModel() : ViewModel() {

    private val _stocksState = MutableStateFlow(emptyList<Stock>())
    val stocksState: StateFlow<List<Stock>> = _stocksState.asStateFlow()

    var watchlistFirstLoaded= mutableStateOf(false)
    init {


    }


    suspend fun removeItem(currentItem: Stock): Boolean =


        suspendCoroutine<Boolean> { continuation ->
            _stocksState.update {
                val mutableList = it.toMutableList()
                mutableList.remove(currentItem)
                mutableList
            }

            DataService.sendWatchlistRemoveRequest(currentItem.ticker, callback = { response ->
                val isSuccess = response.getString("message") == "SUCCESS"
                if (!isSuccess){
                    fetchData()
                }
                continuation.resume(isSuccess)
            }, errorCallback = {

                fetchData()
                continuation.resume(false)
            })
        }







    fun fetchData() {
try {


        DataService.fetchWatchlistDataFromAPI(
            callback = { response ->





                val stockList = mutableListOf<Stock>()

                for (i in 0 until response.length()) {
                    val stockObject = response.getJSONObject(i)
                    val ticker = stockObject.getString("ticker")
                    val name = stockObject.getString("name")
                    val price = stockObject.getDouble("currentPrice")


                       val priceChange = stockObject.getDouble("priceChange")




                       val percentChange = stockObject.getDouble("percentChange")



                    val stock = Stock(ticker, name, price, priceChange, percentChange)
                    stockList.add(stock)
                }
                _stocksState.update { stockList }

                watchlistFirstLoaded.value=true

            },
            errorCallback = { error ->

            }
        )}
catch (e:Exception){
    e.message?.let { Log.d("Error", it) }
}
    }
}











