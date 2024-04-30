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


class PortfolioViewModel() : ViewModel() {

    private val _stocksState = MutableStateFlow(emptyList<Stock>())
    val stocksState: StateFlow<List<Stock>> = _stocksState.asStateFlow()
    var portfolioBalanceState = mutableStateOf(0.0)
    var netWorthState = mutableStateOf(0.0)

    var portofolioFirstLoaded = mutableStateOf(false)

    var portfolioBalance: Double
        get() = portfolioBalanceState.value
        set(value) {
            portfolioBalanceState.value = value
        }

    var netWorth: Double
        get() = netWorthState.value
        set(value) {
            netWorthState.value = value
        }


    init {
        portfolioBalanceState = mutableStateOf(portfolioBalance)
        netWorthState = mutableStateOf(netWorth)


    }


    fun fetchData() {
        try {
            DataService.fetchPortfolioDataFromAPI(
                callback = { response ->
                    val stockList = mutableListOf<Stock>()
                    var totalValue = 0.0
                    netWorth = 0.0
                    val stocksArray = response.getJSONArray("stocks")

                    for (i in 0 until stocksArray.length()) {
                        val stockObject = stocksArray.getJSONObject(i)


                        val ticker = stockObject.getString("ticker")

                        val quantity = stockObject.getInt("quantity")
                        val cost = stockObject.getDouble("cost")
                        val price = stockObject.getDouble("price")


                        val avgCost = cost / quantity
                        val currentValue = quantity * price

                        val totalChange = (price - avgCost) * quantity

                        val percentChange = (totalChange / cost) * 100

                        val stock = Stock(
                            ticker,
                            "$quantity shares",
                            currentValue,
                            totalChange,
                            percentChange
                        )
                        stockList.add(stock)


                        totalValue += currentValue
                    }


                    _stocksState.update { stockList }
                    portfolioBalance = response.getDouble("balance")
                    netWorth = totalValue + portfolioBalance
                    portofolioFirstLoaded.value = true

                },
                errorCallback = { error ->

                }
            )
        } catch (e: Exception) {
            e.message?.let { Log.d("Error", it) }
        }

    }


}
