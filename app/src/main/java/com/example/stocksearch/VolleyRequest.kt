import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.stocksearch.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.coroutines.resume

class VolleyRequest(private val context: Context) {

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    suspend fun fetchPortfolioDataFromAPI(): JSONObject? {

        Log.d("Volley","Portfolio Req Sent")
        return withContext(Dispatchers.IO) {
            val url = "https://cs571a3-418806.uc.r.appspot.com/api/portfolio?userid="+UserService.getUserId()
            Log.d("Send to",url)
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
                Log.d("Portfolio API Response","NULL")
                null
            }
        }
    }


}