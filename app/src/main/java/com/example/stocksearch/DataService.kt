import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.stocksearch.UserService
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume

object DataService {

    private lateinit var requestQueue: RequestQueue
    fun init(context: Context) {
        requestQueue = Volley.newRequestQueue(context.applicationContext)
    }


    fun fetchPortfolioDataFromAPI( callback: (JSONObject) -> Unit,errorCallback: (String) -> Unit) {
        val url ="https://cs571a3-418806.uc.r.appspot.com/api/portfolio?userid=" + UserService.getUserId()
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                Log.d("Portfolio API Response", response.toString())
                callback(response)
            },
            { error ->
                Log.d("Portfolio API Response","Error")
                errorCallback("Error: ${error.message}")
            })

        requestQueue.add(request)

    }



    fun fetchWatchlistDataFromAPI( callback: (JSONArray) -> Unit,errorCallback: (String) -> Unit) {
        val url ="https://cs571a3-418806.uc.r.appspot.com/api/watchlist?userid=" + UserService.getUserId()
        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->

                Log.d("Watchlist API Response", response.toString())
                callback(response)
            },
            { error ->
                Log.d("Watchlist API Response","Error")
                errorCallback("Error: ${error.message}")
            })

        requestQueue.add(request)

    }


    fun sendWatchlistRemoveRequest(stockSymbol: String, callback: (JSONObject) -> Unit,errorCallback: (String) -> Unit) {
        val url =
            "https://cs571a3-418806.uc.r.appspot.com/api/watchlist/${UserService.getUserId()}/$stockSymbol"
        val request = JsonObjectRequest(Request.Method.DELETE, url, null,
            { response ->

                Log.d("Watchlist Remove API Response", response.toString())
                callback(response)
            },
            { error ->
                Log.d("Watchlist Remove API Response","Error")
                errorCallback("Error: ${error.message}")
            })

        requestQueue.add(request)

    }
/*
    suspend fun o_fetchPortfolioDataFromAPI(): JSONObject? {



            val url ="https://cs571a3-418806.uc.r.appspot.com/api/portfolio?userid=" + UserService.getUserId()

            val response = kotlin.coroutines.suspendCoroutine<JSONObject?> { continuation ->
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                    { response ->

                        continuation.resume(response)
                    },
                    { error ->

                        Log.e("Volley Error", error.toString())
                        continuation.resume(null)
                    })

                this.requestQueue.add(jsonObjectRequest)
            }




            if (response != null) {
                Log.d("Portfolio API Response", response.toString())
                return  response
            } else {
                Log.d("Portfolio API Response", "NULL")
                return null
            }
        }
    }


    suspend fun o_fetchWatchlistDataFromAPI(): JSONArray? {

        Log.d("volley","watchlist attempt to fetch")

            val url =
                "https://cs571a3-418806.uc.r.appspot.com/api/watchlist?userid=" + UserService.getUserId()

            val response = kotlin.coroutines.suspendCoroutine<JSONArray?> { continuation ->
                val jsonRequest = JsonArrayRequest(Request.Method.GET, url, null,
                    { response ->

                        continuation.resume(response)
                    },
                    { error ->

                        Log.e("Volley Error", error.toString())
                        continuation.resume(null)
                    })

                //requestQueue.add(jsonRequest)
            }




            if (response != null) {
                Log.d("Watchlist API Response", response.toString())
                return response
            } else {
                Log.d("Watchlist API Response", "NULL")
                return null
            }

    }


    suspend fun o_sendWatchlistRemoveRequest(stockSymbol: String): JSONObject? {



            val url =
                "https://cs571a3-418806.uc.r.appspot.com/api/watchlis/${UserService.getUserId()}/$stockSymbol"

            val response = kotlin.coroutines.suspendCoroutine<JSONObject?> { continuation ->
                val jsonRequest = JsonObjectRequest(Request.Method.DELETE, url, null,
                    { response ->

                        continuation.resume(response)
                    },
                    { error ->

                        Log.e("Volley Error", error.toString())
                        continuation.resume(null)
                    })

                //requestQueue.add(jsonRequest)
            }

            if (response != null) {
                Log.d("Watchlist Remove API Response", response.toString())
                 return response
            } else {
                Log.d("Watchlist Remove API Response", "NULL")
                return  null
            }

    }
*/

}