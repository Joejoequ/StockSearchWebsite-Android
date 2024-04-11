import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.stocksearch.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.resume

class VolleyRequest(private val context: Context) {

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    suspend fun fetchPortfolioDataFromAPI(): JSONObject? {


        return withContext(Dispatchers.IO) {
            val url =
                "https://cs571a3-418806.uc.r.appspot.com/api/portfolio?userid=" + UserService.getUserId()

            val response = kotlin.coroutines.suspendCoroutine<JSONObject?> { continuation ->
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                    { response ->

                        continuation.resume(response)
                    },
                    { error ->

                        Log.e("Volley Error", error.toString())
                        continuation.resume(null)
                    })

                requestQueue.add(jsonObjectRequest)
            }




            if (response != null) {
                Log.d("Portfolio API Response", response.toString())
                response
            } else {
                Log.d("Portfolio API Response", "NULL")
                null
            }
        }
    }


    suspend fun fetchWatchlistDataFromAPI(): JSONArray? {


        return withContext(Dispatchers.IO) {
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

                requestQueue.add(jsonRequest)
            }




            if (response != null) {
                Log.d("Watchlist API Response", response.toString())
                response
            } else {
                Log.d("Watchlist API Response", "NULL")
                null
            }
        }
    }


    suspend fun sendWatchlistRemoveRequest(stockSymbol: String): JSONObject? {



            val url =
                "https://cs571a3-418806.uc.r.appspot.com/api/watchlist/${UserService.getUserId()}/$stockSymbol"

            val response = kotlin.coroutines.suspendCoroutine<JSONObject?> { continuation ->
                val jsonRequest = JsonObjectRequest(Request.Method.DELETE, url, null,
                    { response ->

                        continuation.resume(response)
                    },
                    { error ->

                        Log.e("Volley Error", error.toString())
                        continuation.resume(null)
                    })

                requestQueue.add(jsonRequest)
            }

            if (response != null) {
                Log.d("Watchlist Remove API Response", response.toString())
                 return response
            } else {
                Log.d("Watchlist Remove API Response", "NULL")
                return  null
            }

    }


}