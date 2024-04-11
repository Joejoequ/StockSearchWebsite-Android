package com.example.stocksearch

import VolleyRequest
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class WatchlistViewModel(private val volleyRequest: VolleyRequest): ViewModel() {

    private val _stocksState = MutableStateFlow(emptyList<Stock>())
    val stocksState: StateFlow<List<Stock>> = _stocksState.asStateFlow()



    init {


    }


    suspend fun removeItem(currentItem: Stock) :Boolean{

        _stocksState.update {
            val mutableList = it.toMutableList()
            mutableList.remove(currentItem)
            mutableList
        }



            val response = volleyRequest.sendWatchlistRemoveRequest(currentItem.ticker)

            if (response != null && response.getString("message") == "SUCCESS") {
                return true
            } else {
                fetchData()
                return false
            }


    }




    suspend fun fetchData() {


        val response :JSONArray?= volleyRequest.fetchWatchlistDataFromAPI()

        if (response!=null) {
            val stockList = mutableListOf<Stock>()



            for (i in 0 until response.length()) {
                val stockObject =response.getJSONObject(i)


                val ticker = stockObject.getString("ticker")

                val name=stockObject.getString("name")

                val price = stockObject.getDouble("currentPrice")
                val priceChange = stockObject.getDouble("priceChange")
                val percentChange = stockObject.getDouble("percentChange")






                val stock = Stock(ticker, name, price,priceChange,percentChange)
                stockList.add(stock)



            }


            _stocksState.update { stockList }



        }
    }



}
