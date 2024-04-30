import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.stocksearch.UserService
import org.json.JSONArray
import org.json.JSONObject

object DataService {

    private lateinit var requestQueue: RequestQueue
    val timeout = 10000

    fun init(context: Context) {
        requestQueue = Volley.newRequestQueue(context.applicationContext)
    }


    fun fetchPortfolioDataFromAPI(callback: (JSONObject) -> Unit, errorCallback: (String) -> Unit) {
        val url =
            "https://cs571a3-418806.uc.r.appspot.com/api/portfolio?userid=" + UserService.getUserId()
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                Log.d("Portfolio API Response", response.toString())

                callback(response)
            },
            { error ->
                Log.d("Portfolio API Response", "Error ${error.message}")
                errorCallback("Error: ${error.message}")
            })

        request.retryPolicy = DefaultRetryPolicy(
            timeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        requestQueue.add(request)

    }




    fun fetchWatchlistDataFromAPI(callback: (JSONArray) -> Unit, errorCallback: (String) -> Unit) {
        val url =
            "https://cs571a3-418806.uc.r.appspot.com/api/watchlist?userid=" + UserService.getUserId()
        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->

                Log.d("Watchlist API Response", response.toString())

                callback(response)
            },
            { error ->
                Log.d("Watchlist API Response", "Error $error")

                errorCallback("Error: ${error.message}")
            })

        request.retryPolicy = DefaultRetryPolicy(
            timeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(request)

    }


    fun sendWatchlistRemoveRequest(
        stockSymbol: String,
        callback: (JSONObject) -> Unit,
        errorCallback: (String) -> Unit
    ) {
        val url =
            "https://cs571a3-418806.uc.r.appspot.com/api/watchlist/${UserService.getUserId()}/$stockSymbol"
        val request = JsonObjectRequest(Request.Method.DELETE, url, null,
            { response ->

                Log.d("Watchlist Remove API Response", response.toString())
                callback(response)
            },
            { error ->
                Log.d("Watchlist Remove API Response", "Error")
                errorCallback("Error: ${error.message}")
            })

        request.retryPolicy = DefaultRetryPolicy(
            timeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        requestQueue.add(request)

    }


    fun fetchAutocompleteDataFromAPI(
        stockSymbol: String,
        callback: (JSONArray) -> Unit,
        errorCallback: (String) -> Unit
    ) {
        val url = "https://cs571a3-418806.uc.r.appspot.com/api/autocomplete?symbol=$stockSymbol"
        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->

                Log.d("Autocomplete API Response", response.toString())
                callback(response)
            },
            { error ->
                Log.d("Autocomplete API Response", "Error")
                errorCallback("Error: ${error.message}")
            })

        request.retryPolicy = DefaultRetryPolicy(
            4000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        requestQueue.add(request)

    }


    fun fetchProfileDataFromAPI(
        stockSymbol: String,
        callback: (JSONObject) -> Unit,
        errorCallback: (String) -> Unit
    ) {
        val url = "https://cs571a3-418806.uc.r.appspot.com/api/profile?symbol=$stockSymbol"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->

                Log.d("Profile API Response", response.toString())
                callback(response)
            },
            { error ->
                Log.d("Profile API Response", "Error")
                errorCallback("Error: ${error.message}")
            })

        request.retryPolicy = DefaultRetryPolicy(
            timeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        requestQueue.add(request)

    }


    fun fetchQuoteDataFromAPI(
        stockSymbol: String,
        callback: (JSONObject) -> Unit,
        errorCallback: (String) -> Unit
    ) {
        val url = "https://cs571a3-418806.uc.r.appspot.com/api/quote?symbol=$stockSymbol"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->

                Log.d("Quote API Response", response.toString())
                callback(response)
            },
            { error ->
                Log.d("Quote API Response", "Error")
                errorCallback("Error: ${error.message}")
            })

        request.retryPolicy = DefaultRetryPolicy(
            timeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        requestQueue.add(request)

    }



    fun fetchPeerDataFromAPI(
        stockSymbol: String,
        callback: (JSONArray) -> Unit,
        errorCallback: (String) -> Unit
    ) {
        val url = "https://cs571a3-418806.uc.r.appspot.com/api/peers?symbol=$stockSymbol"
        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->

                Log.d("Peer API Response", response.toString())
                callback(response)
            },
            { error ->
                Log.d("Peer API Response", "Error")
                errorCallback("Error: ${error.message}")
            })

        request.retryPolicy = DefaultRetryPolicy(
            timeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        requestQueue.add(request)

    }



    fun fetchInsightDataFromAPI(
        stockSymbol: String,
        callback: (JSONArray) -> Unit,
        errorCallback: (String) -> Unit
    ) {
        val url = "https://cs571a3-418806.uc.r.appspot.com/api/insider?symbol=$stockSymbol"
        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->

                Log.d("Insider API Response", response.toString())
                callback(response)
            },
            { error ->
                Log.d("Insider API Response", "Error")
                errorCallback("Error: ${error.message}")
            })

        request.retryPolicy = DefaultRetryPolicy(
            timeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        requestQueue.add(request)

    }




    fun fetchNewsDataFromAPI(
        stockSymbol: String,
        callback: (JSONArray) -> Unit,
        errorCallback: (String) -> Unit
    ) {
        val url = "https://cs571a3-418806.uc.r.appspot.com/api/news?symbol=$stockSymbol"
        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->

                Log.d("News API Response", response.toString())
                callback(response)
            },
            { error ->
                Log.d("News API Response", "Error")
                errorCallback("Error: ${error.message}")
            })

        request.retryPolicy = DefaultRetryPolicy(
            timeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        requestQueue.add(request)

    }



    fun checkIfInWatchlistFromAPI(
        stockSymbol: String,
        callback: (String) -> Unit,
        errorCallback: (String) -> Unit
    ) {
        val url = "https://cs571a3-418806.uc.r.appspot.com/api/watchlist/ifStockInWatchlist?symbol=$stockSymbol&userid=${UserService.getUserId()}"
        val request =  StringRequest(Request.Method.GET, url,
            { response ->

                Log.d("IfInWatchlist API Response", response.toString())
                callback(response)
            },
            { error ->
                Log.d("IfInWatchlist API Response", "Error")
                errorCallback("Error: ${error.message}")
            })

        request.retryPolicy = DefaultRetryPolicy(
            timeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        requestQueue.add(request)

    }




    fun addStockToWatchlistAPI(
        stockSymbol: String,
        callback: (JSONObject) -> Unit,
        errorCallback: (String) -> Unit
    ) {
        val url = "https://cs571a3-418806.uc.r.appspot.com/api/watchlist"

        val jsonObject = JSONObject().apply {
            put("userid", UserService.getUserId())
            put("stockSymbol", stockSymbol)
        }

        val request =  JsonObjectRequest(Request.Method.POST, url,jsonObject,
            { response ->

                Log.d("addStockToWatchlist API Response", response.toString())
                callback(response)
            },
            { error ->
                Log.d("addStockToWatchlist API Response", "Error")
                errorCallback("Error: ${error.message}")
            })



        requestQueue.add(request)

    }




    fun removeStockFromWatchlistAPI(
        stockSymbol: String,
        callback: (JSONObject) -> Unit,
        errorCallback: (String) -> Unit
    ) {
        val url = "https://cs571a3-418806.uc.r.appspot.com/api/watchlist/${UserService.getUserId()}/${stockSymbol}"



        val request =  JsonObjectRequest(Request.Method.DELETE, url,null,
            { response ->

                Log.d("removeStockFromWatchlist API Response", response.toString())
                callback(response)
            },
            { error ->
                Log.d("removeStockFromWatchlist API Response", "Error")
                errorCallback("Error: ${error.message}")
            })



        requestQueue.add(request)

    }




    fun sendBuyStockAPI(
        stockSymbol: String,quantity:Int,
        callback: (JSONObject) -> Unit,
        errorCallback: (String) -> Unit
    ) {
        val url = "https://cs571a3-418806.uc.r.appspot.com/api/portfolio/buy"

        val jsonObject = JSONObject().apply {
            put("userid", UserService.getUserId())
            put("ticker", stockSymbol)
            put("quantity", quantity)
        }

        val request =  JsonObjectRequest(Request.Method.POST, url,jsonObject,
            { response ->

                Log.d("sendBuyStock API Response", response.toString())
                callback(response)
            },
            { error ->
                Log.d("sendBuyStock API Response", "Error")
                errorCallback("Error: ${error.message}")
            })



        requestQueue.add(request)

    }




    fun sendSellStockAPI(
        stockSymbol: String,quantity: Int,
        callback: (JSONObject) -> Unit,
        errorCallback: (String) -> Unit
    ) {
        val url = "https://cs571a3-418806.uc.r.appspot.com/api/portfolio/sell"
        val jsonObject = JSONObject().apply {
            put("userid", UserService.getUserId())
            put("ticker", stockSymbol)
            put("quantity", quantity)
        }


        val request =  JsonObjectRequest(Request.Method.POST, url,jsonObject,
            { response ->

                Log.d("sendSellStock API Response", response.toString())
                callback(response)
            },
            { error ->
                Log.d("sendSellStock API Response", "Error")
                errorCallback("Error: ${error.message}")
            })



        requestQueue.add(request)

    }







}