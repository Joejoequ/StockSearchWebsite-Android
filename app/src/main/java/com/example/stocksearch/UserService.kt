package com.example.stocksearch

object UserService {
    private val userId: String

    init {
        //this.userId = uuidv4();
        this.userId = "test"
    }

    fun getUserId(): String {
        return userId
    }
}