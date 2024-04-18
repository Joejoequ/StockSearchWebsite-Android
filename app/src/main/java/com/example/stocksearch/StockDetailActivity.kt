package com.example.stocksearch


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.painterResource

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.stocksearch.ui.theme.StockSearchTheme


import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.placeholder

class StockDetailActivity : ComponentActivity() {
    var searchedStock: String = ""
    val stockDetailViewModel = StockDetailViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_StockSearch)
        super.onCreate(savedInstanceState)
        val para = intent.getStringExtra("SearchTicker")
        if (para != null) {
            searchedStock = para
        }
        setContent {
            StockSearchTheme {
                StockDetailContent()
            }
        }

        stockDetailViewModel.fetchData(searchedStock)

    }

    @Composable
    fun StockDetailContent() {
        Scaffold(
            topBar = {
                MyAppTopBar()

            },
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),

                color = Color.White
            ) {


                val quoteDataLoaded by remember {
                    stockDetailViewModel.quoteDataLoaded
                }

                val profileDataLoaded by remember {
                    stockDetailViewModel.profileDataLoaded
                }

                if (quoteDataLoaded && profileDataLoaded) {


                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState())
                    ) {

                        StockProfileCard(stockDetailViewModel)


                        yearChartFromHTML()
                    }


                } else {


                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier,
                            color = Color(0xFF512DA8),
                        )
                    }
                }
            }
        }

    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MyAppTopBar(


    ) {


        Surface(shadowElevation = 8.dp) {


            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                ),
                title = {

                    Text(
                        searchedStock,
                        modifier = Modifier.padding(horizontal = GlobalValues.indentationValue)
                    )

                },

                navigationIcon = {

                    IconButton(onClick = {
                        finish()

                    }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }


                },

                actions = {
                    IconButton(onClick = { }) {
                        Icon(painter = painterResource(id = R.drawable.star_border), null)
                    }
                }
            )


        }

    }


    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun StockProfileCard(stockDetailViewModel: StockDetailViewModel) {

        val profileData by stockDetailViewModel.profileData.collectAsState()
        val quoteData by stockDetailViewModel.quoteData.collectAsState()





        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(GlobalValues.indentationValue),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                val circularProgressDrawable = CircularProgressDrawable(LocalContext.current)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()
                GlideImage(
                    model = profileData.getString("logo"),
                    contentDescription = "stock_detail_logo",
                    modifier = Modifier.size(50.dp),
                    loading = placeholder(circularProgressDrawable),
                    failure = placeholder(R.drawable.launcher)


                )


                Column(modifier = Modifier.padding(start = GlobalValues.indentationValue)) {
                    Text(
                        text = profileData.getString("ticker"),
                        color = Color.Black,
                        fontSize = 22.sp,

                        )
                    Text(
                        text = profileData.getString("name"),
                        color = Color.Gray,
                        fontSize = 17.sp,

                        )
                }
            }

            Row {


                Column(horizontalAlignment = Alignment.End) {


                    Text(
                        text = "$${"%.2f".format(quoteData.getDouble("c"))}",
                        color = Color.Black,
                        fontSize = 22.sp,

                        )


                    Row {

                        val priceChange = quoteData.getDouble("d")
                        val fontColor: Any
                        val imageResource: Int?
                        if (priceChange > 0.0) {
                            imageResource = R.drawable.trending_up
                            fontColor = Color(0xFF37AF4C)
                        } else if (priceChange < 0.0) {
                            imageResource = R.drawable.trending_down
                            fontColor = Color(0xFFEB502E)
                        } else {
                            imageResource = null
                            fontColor = Color.Black
                        }


                        if (imageResource != null) {
                            Image(
                                painter = painterResource(imageResource),
                                contentDescription = "My Image"
                            )
                        }

                        Text(

                            text = "$${"%.2f".format(priceChange)}",
                            color = fontColor,
                            fontSize = 17.sp,

                            )

                        Text(

                            text = "(${"%.2f".format(quoteData.getDouble("dp"))}%)",
                            color = fontColor,
                            fontSize = 17.sp,

                            )
                    }

                }


            }

        }


    }



    @Composable
    fun yearChartFromHTML() {
        var webView = WebView(LocalContext.current)

       webView.apply {
            settings.javaScriptEnabled = true
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    val javascriptCode = "loadChart('$searchedStock');"
                    webView.evaluateJavascript(javascriptCode, null)
                }
            }
            webView = this
        }


        webView.loadUrl("file:///android_asset/yearChart.html")




        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { webView }
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun StockDetailContentPreview() {
        StockSearchTheme {
            StockDetailContent()
        }
    }

}