package com.example.stocksearch

import VolleyRequest
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject

class WatchlistViewModel(private val volleyRequest: VolleyRequest): ViewModel() {

    private val _stocksState = MutableStateFlow(emptyList<Stock>())
    val stocksState: StateFlow<List<Stock>> = _stocksState.asStateFlow()



    init {

_stocksState.update { test() }
    }


    fun removeItem(currentItem: Stock) {
        _stocksState.update {
            val mutableList = it.toMutableList()
            mutableList.remove(currentItem)
            mutableList
        }
    }


    private fun test() = listOf(
        Stock("Jod", "Hello",1.0,1.0,1.0),
        Stock("Jdd", "xllo",2.0,0.0,2.3),
        Stock("3", "3",2.0,-2.3,2.3),

    )

    suspend fun fetchData() {
        val response :JSONObject?= volleyRequest.fetchPortfolioDataFromAPI()


        //_stocksState.update { }
    }

    suspend fun getPortfolioStockList() {
        val stocks = volleyRequest.fetchPortfolioDataFromAPI()
        //_stocksState.update { stocks }
    }



}
