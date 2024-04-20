package com.example.stocksearch


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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text

import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.stocksearch.ui.theme.StockSearchTheme


import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
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
                        PriceChartTab()


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
    fun PriceChartTab() {

        var context = LocalContext.current
        var webView1 = WebView(context)


        var hourChartLoaded by remember { mutableStateOf(false) }
        var yearChartLoaded by remember { mutableStateOf(false) }
        webView1.apply {
            settings.javaScriptEnabled = true
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    if (!hourChartLoaded) {
                        view?.evaluateJavascript("loadHourChart('$searchedStock');", null)

                        hourChartLoaded = true
                    }
                }
            }
            webView1 = this
        }

        webView1.loadUrl("file:///android_asset/hourChart.html")


        var webView2 = WebView(context)

        webView2.apply {
            settings.javaScriptEnabled = true
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    if (!yearChartLoaded) {
                        view?.evaluateJavascript("loadYearChart('$searchedStock');", null)
                        yearChartLoaded = true
                    }

                }
            }
            webView2 = this
        }


        webView2.loadUrl("file:///android_asset/yearChart.html")


        var tabIndex by remember { mutableStateOf(0) }

        val tabs = listOf("hour", "year")

        Column(modifier = Modifier.fillMaxWidth()) {


            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {

                    webView1
                }
            ) {
                if (tabIndex == 0) {
                    it.visibility = android.view.View.VISIBLE
                } else {
                    it.visibility = android.view.View.GONE
                }
            }


            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { webView2 }
            ) {
                if (tabIndex == 1) {
                    it.visibility = android.view.View.VISIBLE
                } else {
                    it.visibility = android.view.View.GONE
                }
            }





            TabRow(selectedTabIndex = tabIndex, indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                    color = colorResource(id = R.color.purple_700)
                )
            }, contentColor = colorResource(id = R.color.purple_700)) {
                tabs.forEachIndexed { index, title ->
                    Tab(

                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        icon = {
                            when (index) {
                                0 -> Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.chart_hour),
                                    contentDescription = null
                                )

                                1 -> Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.chart_historical),
                                    contentDescription = null
                                )

                            }
                        }
                    )
                }
            }

        }
    }

    @Composable
    fun PortfolioSection() {

    }


    @Preview(showBackground = true)
    @Composable
    fun StockDetailContentPreview() {
        StockSearchTheme {
            StockDetailContent()
        }
    }

}