package com.example.stocksearch.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONArray
import org.json.JSONObject

class StockDetailViewModel(symbol:String) : ViewModel() {

    val profileData: MutableStateFlow<JSONObject> = MutableStateFlow(JSONObject())
    val quoteData: MutableStateFlow<JSONObject> = MutableStateFlow(JSONObject())
    var profileDataLoaded = mutableStateOf(false)
    var quoteDataLoaded = mutableStateOf(false)

    val peerData: MutableStateFlow<JSONArray> = MutableStateFlow(JSONArray())

    val newsData: MutableStateFlow<JSONArray> = MutableStateFlow(JSONArray())
    val insiderData: MutableStateFlow<JSONObject> = MutableStateFlow(JSONObject())
    val portfolioData: MutableStateFlow<JSONObject> = MutableStateFlow(JSONObject())

    var ifInWatchlist = mutableStateOf(false)

    var symbolInput=symbol

    init {
        val newStock = JSONObject()

        newStock.put("quantity", 0)
        newStock.put("totalCost", 0.0)
        newStock.put("avgCost", 0.0)
        newStock.put("change", 0.0)
        newStock.put("marketValue", 0.0)
        portfolioData.update { newStock }


        val insider = JSONObject()


        insider.put("positiveMsprSum", 0.0)
        insider.put("negativeMsprSum", 0.0)
        insider.put("totalMsprSum", 0.0)
        insider.put("positiveChangeSum", 0.0)
        insider.put("negativeChangeSum", 0.0)
        insider.put("totalChangeSum", 0.0)
        insiderData.update { insider }
    }

    fun fetchData() {
        try {
            profileDataLoaded.value = false
            quoteDataLoaded.value = false

            DataService.fetchProfileDataFromAPI(stockSymbol = symbolInput,
                callback = { response ->
                    profileData.update { response }
                    profileDataLoaded.value = true
                },
                errorCallback = { error ->
                }
            )


            DataService.fetchQuoteDataFromAPI(stockSymbol = symbolInput,
                callback = { response ->
                    quoteData.update { response }
                    quoteDataLoaded.value = true
                },
                errorCallback = { error ->
                }
            )


            DataService.checkIfInWatchlistFromAPI(stockSymbol = symbolInput,
                callback = { response ->

                    ifInWatchlist.value =  response.equals("true", ignoreCase = true)
                },
                errorCallback = { error ->
                }
            )


            DataService.fetchNewsDataFromAPI(stockSymbol = symbolInput,
                callback = { response ->
                    newsData.update { response }
                },
                errorCallback = { error ->
                }
            )

            DataService.fetchPeerDataFromAPI(stockSymbol = symbolInput,
                callback = { response ->
                    peerData.update { response }

                },
                errorCallback = { error ->
                }
            )

            DataService.fetchInsightDataFromAPI(stockSymbol = symbolInput,
                callback = { response ->


                    var positiveMsprSum:Double= 0.0
                    var negativeMsprSum:Double= 0.0
                    var totalMsprSum:Double= 0.0
                    var positiveChangeSum:Double=0.0
                    var negativeChangeSum:Double=0.0
                    var totalChangeSum:Double=0.0


                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val mspr = jsonObject.getDouble("mspr")
                        val change = jsonObject.getDouble("change")

                        positiveMsprSum = if (mspr > 0) mspr+positiveMsprSum else positiveMsprSum
                        negativeMsprSum = if (mspr < 0) mspr+negativeMsprSum else negativeMsprSum
                        totalMsprSum += mspr

                        positiveChangeSum = if (change > 0) change+positiveChangeSum else positiveChangeSum
                        negativeChangeSum = if (change < 0) change+negativeChangeSum else negativeChangeSum
                        totalChangeSum += change

                    }

                    val insider = JSONObject()
                    insider.put("positiveMsprSum", positiveMsprSum)
                    insider.put("negativeMsprSum", negativeMsprSum)
                    insider.put("totalMsprSum", totalMsprSum)
                    insider.put("positiveChangeSum", positiveChangeSum)
                    insider.put("negativeChangeSum", negativeChangeSum)
                    insider.put("totalChangeSum", totalChangeSum)

                        insiderData.update { insider }


                },
                errorCallback = { error ->
                }
            )




            updatePortfolioData()


        } catch (e: Exception) {
            e.message?.let { Log.d("Error", it) }
        }


    }

    fun updatePortfolioData(){
        DataService.fetchPortfolioDataFromAPI(
            callback = { response ->
                var found = false
                val newStock = JSONObject()

                (0 until response.getJSONArray("stocks").length()).forEach {
                    val stock = response.getJSONArray("stocks").getJSONObject(it)
                    if (stock.getString("ticker") == symbolInput) {

                        var avgCost = stock.getDouble("cost") / stock.getInt("quantity")


                        newStock.put("ticker", stock.getString("ticker"))
                        newStock.put("quantity", stock.getInt("quantity"))
                        newStock.put("avgCost", avgCost)
                        newStock.put("totalCost", stock.getDouble("cost"))
                        newStock.put("change", stock.getDouble("price") - avgCost)
                        newStock.put(
                            "marketValue",
                            stock.getDouble("price") * stock.getInt("quantity")
                        )
                        found = true
                    }
                }


                if (!found) {

                    newStock.put("ticker", symbolInput)
                    newStock.put("quantity", 0)
                    newStock.put("totalCost", 0.0)
                    newStock.put("avgCost", 0.0)
                    newStock.put("change", 0.0)
                    newStock.put("marketValue", 0.0)


                }

                portfolioData.update { newStock }

            },
            errorCallback = { error ->
            }
        )
    }

    fun addToWatchlist(symbol:String,context:Context){
        DataService.addStockToWatchlistAPI(stockSymbol = symbol,
            callback = { response ->
               if (response.getString("message")=="SUCCESS"){
                   ifInWatchlist.value=true
                   Toast.makeText(context, symbol+" is added to favorites", Toast.LENGTH_SHORT).show()
               }
            },
            errorCallback = { error ->
            }
        )
    }

    fun removeFromWatchlist(symbol:String,context:Context){
        DataService.removeStockFromWatchlistAPI(stockSymbol = symbol,
            callback = { response ->
                if (response.getString("message")=="SUCCESS"){
                    ifInWatchlist.value=false
                    Toast.makeText(context, symbol+" is removed from favorites", Toast.LENGTH_SHORT).show()
                }
            },
            errorCallback = { error ->
            }
        )
    }




}