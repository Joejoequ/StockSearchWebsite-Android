package com.example.stocksearch


data class Stock(


    val ticker: String,
    val name: String,
    val currentValue: Double,
    val valueChange: Double,
    val percentValueChange: Double
)
