package com.example.stocksearch.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.IntrinsicSize


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard

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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.stocksearch.ui.theme.StockSearchTheme


import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.stocksearch.R
import com.example.stocksearch.viewmodel.PortfolioViewModel
import com.example.stocksearch.viewmodel.StockDetailViewModel
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.Date
import java.util.Locale

class StockDetailActivity : ComponentActivity() {
    var searchedStock: String = ""
    lateinit var stockDetailViewModel: StockDetailViewModel

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
        stockDetailViewModel = StockDetailViewModel(searchedStock)
        stockDetailViewModel.fetchData()

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

                        StockProfileCard()
                        PriceChartTab()

                        SectionLabel(title = "Portfolio")
                        PortfolioSection()
                        SectionLabel(title = "Stats")
                        StatsSection()
                        SectionLabel(title = "About")
                        AboutSection()
                        SectionLabel(title = "Insights")
                        InsightSection()

                        SectionLabel(title = "News")
                        NewsSection()


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

                    val context = LocalContext.current
                    val inWatchlist by remember {
                        stockDetailViewModel.ifInWatchlist
                    }
                    if (!inWatchlist)
                        IconButton(onClick = {
                            stockDetailViewModel.addToWatchlist(searchedStock, context)
                        }) {
                            Icon(painter = painterResource(id = R.drawable.star_border), null)
                        }
                    else {
                        IconButton(onClick = {
                            stockDetailViewModel.removeFromWatchlist(searchedStock, context)
                        }) {
                            Icon(painter = painterResource(id = R.drawable.full_star), null)
                        }
                    }
                }
            )


        }

    }

    @Composable
    fun PriceChartTab() {


        var tabIndex by remember { mutableStateOf(0) }

        val tabs = listOf("hour", "year")

        Column(modifier = Modifier.fillMaxWidth()) {


            val hourChartVisibility = remember { mutableStateOf(true) }
            val yearChartVisibility = remember { mutableStateOf(true) }

            LaunchedEffect(tabIndex) {
                hourChartVisibility.value = tabIndex == 0
                yearChartVisibility.value = tabIndex == 1
            }


            ChartFromHTML(
                path = "file:///android_asset/hourChart.html",
                visibility = hourChartVisibility
            )




            ChartFromHTML(
                path = "file:///android_asset/yearChart.html",
                visibility = yearChartVisibility
            )










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
        val portfolioData by stockDetailViewModel.portfolioData.collectAsState()
        var showDialog = remember { mutableStateOf(false) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {


            Row(
                modifier = Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {

                    Text(text = "Shares Owned:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Avg. Cost/Share:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Total Cost:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Change:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Market Value:")
                }
                Column(modifier = Modifier.padding(start = 15.dp)) {

                    Text(text = portfolioData.getInt("quantity").toString())
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = String.format("%.2f", portfolioData.getDouble("avgCost")))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = String.format("%.2f", portfolioData.getDouble("totalCost")))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = String.format("%.2f", portfolioData.getDouble("change")))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = String.format("%.2f", portfolioData.getDouble("marketValue")))
                }
            }

            Button(
                onClick = { showDialog.value = true },
                modifier = Modifier.padding(end = 20.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0FA128),
                    contentColor = Color(0xFFFFFFFF)
                )
            ) {
                Text("Trade")
            }
        }


        if (showDialog.value) {
            val portfolioViewModel = PortfolioViewModel()
            portfolioViewModel.fetchData()
            val quoteData by stockDetailViewModel.quoteData.collectAsState()
            val profileData by stockDetailViewModel.profileData.collectAsState()
            var textInput by remember { mutableStateOf("") }
            val showResult = remember { mutableStateOf(false) }

            var resultMsg = remember { mutableStateOf("") }
            if (!showResult.value) {
                AlertDialog(

                    containerColor = Color.White,
                    onDismissRequest = { showDialog.value = false },
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Trade ${profileData.getString("name")} shares",
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontSize = 17.sp,

                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    },
                    text = {
                        Column {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(

                                    value = textInput,
                                    modifier = Modifier
                                        .width(200.dp)
                                        .height(50.dp),

                                    placeholder = {
                                        Text("0", color = Color.Gray)
                                    },

                                    onValueChange = {
                                        textInput = it
                                    },

                                    singleLine = true,

                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Cyan,
                                        unfocusedIndicatorColor = Color.Cyan,
                                    ),
                                    keyboardOptions = KeyboardOptions(

                                        keyboardType = KeyboardType.Number
                                    ),


                                    )

                                Text(text = "shares", color = Color.Black)

                            }

                            val price: Double = quoteData.getDouble("c")

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 20.dp)
                                    .background(Color.White),
                                horizontalArrangement = Arrangement.End
                            ) {


                                if (!validateInput(textInput)) {
                                    Text(
                                        text = "0*" + "%.2f".format(price) + "/share = 0.0",
                                        color = Color.Black
                                    )
                                } else {
                                    Text(
                                        text = textInput.toDouble()
                                            .toString() + "*" + "%.2f".format(
                                            price
                                        ) + "/share = " + "%.2f".format(textInput.toDouble() * price),
                                        color = Color.Black
                                    )
                                }
                            }


                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 15.dp),
                                textAlign = TextAlign.Center,
                                text = "$${portfolioViewModel.portfolioBalance} to buy " + searchedStock
                            )

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {

                                val mContext = LocalContext.current
                                Button(
                                    onClick = {
                                        if (!validateInput(textInput)) {
                                            Toast.makeText(
                                                mContext,
                                                "Please enter a valid amount",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {

                                            portfolioViewModel.buyStock(searchedStock,
                                                textInput.toInt(),
                                                onSuccess =
                                                { response ->

                                                    if (response.getBoolean("success")) {

                                                        stockDetailViewModel.updatePortfolioData()
                                                        resultMsg.value =
                                                            "You have successfully bought ${textInput.toInt()} shares of ${searchedStock}"
                                                        showResult.value = true
                                                    } else {
                                                        Toast.makeText(
                                                            mContext,
                                                            "Not enough money to buy",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }


                                                })

                                        }
                                    },
                                    modifier = Modifier.width(100.dp),
                                    shape = RoundedCornerShape(3.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF0FA128),
                                        contentColor = Color(0xFFFFFFFF)
                                    )
                                ) {
                                    Text("Buy")
                                }
                                Spacer(modifier = Modifier.width(40.dp))


                                Button(
                                    onClick = {
                                        if (!validateInput(textInput)) {
                                            Toast.makeText(
                                                mContext,
                                                "Please enter a valid amount",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {

                                            portfolioViewModel.sellStock(
                                                searchedStock,
                                                textInput.toInt(),
                                                onSuccess = { response ->
                                                    if (response.getBoolean("success")) {
                                                        stockDetailViewModel.updatePortfolioData()
                                                        resultMsg.value =
                                                            "You have successfully sold ${textInput.toInt()} shares of ${searchedStock}"
                                                        showResult.value = true
                                                    } else {
                                                        Toast.makeText(
                                                            mContext,
                                                            "Not enough shares to sell",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }

                                                })


                                        }


                                    },
                                    modifier = Modifier.width(100.dp),
                                    shape = RoundedCornerShape(3.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF0FA128),
                                        contentColor = Color(0xFFFFFFFF)
                                    )
                                ) {
                                    Text("Sell")
                                }
                            }

                        }
                    },
                    confirmButton = {

                    }
                )
            } else {
                AlertDialog(

                    containerColor = Color(0xFF0FA128),
                    onDismissRequest = { showDialog.value = false },
                    title = {

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp), horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Congratulations!",
                                color = Color.White,
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 26.sp)
                            )
                        }

                    },
                    text = {
                        val msg by remember {
                            resultMsg
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(0.6f),
                                text = msg,
                                color = Color.White,
                                style = TextStyle(
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 14.sp
                                ),
                                textAlign = TextAlign.Center
                            )
                        }


                    }, confirmButton = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = { showDialog.value = false },
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .padding(bottom = 20.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFFFFFF),
                                    contentColor = Color(0xFF0FA128)
                                )
                            ) {
                                Text("Done")
                            }


                        }

                    })
            }
        }

    }


    @Composable
    fun StatsSection() {
        val quoteData by stockDetailViewModel.quoteData.collectAsState()

        Row(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.weight(1f)) {
                Column {
                    Text("Open Price:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Low Price:")
                }

                Column {
                    Text(text = String.format("%.2f", quoteData.getDouble("o")))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = String.format("%.2f", quoteData.getDouble("l")))

                }
            }
            Row(modifier = Modifier.weight(1f)) {
                Column {
                    Text("High Price:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Prev. Close:")
                }
                Column {
                    Text(text = String.format("%.2f", quoteData.getDouble("h")))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = String.format("%.2f", quoteData.getDouble("pc")))
                }

            }
        }
    }

    @Composable
    fun AboutSection() {

        val profileData by stockDetailViewModel.profileData.collectAsState()

        val peerData by stockDetailViewModel.peerData.collectAsState()
        val peerList = mutableListOf<String>()
        for (i in 0 until peerData.length()) {
            peerList.add(peerData.getString(i))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column {
                Text(text = "IPO Start Date")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Industry")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Webpage")


            }
            Column(modifier = Modifier.padding(start = 20.dp)) {
                Text(text = profileData.getString("ipo"))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = profileData.getString("finnhubIndustry"))
                Spacer(modifier = Modifier.height(10.dp))

                HyperLink(
                    linkText = profileData.getString("weburl"),
                    link = profileData.getString("weburl"),
                    true
                )


            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(text = "Company Peers")

            LazyRow(modifier = Modifier.padding(start = 8.dp, top = 3.dp)) {
                items(count = peerList.size) { index ->

                    HyperLink(linkText = peerList[index], link = peerList[index], false)
                    Spacer(modifier = Modifier.width(5.dp))

                }
            }

        }
    }

    @Composable
    fun InsightSection() {

        val profileData by stockDetailViewModel.profileData.collectAsState()
        val insiderData by stockDetailViewModel.insiderData.collectAsState()
        Surface(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Social Sentiments",
                fontSize = 19.sp,
                textAlign = TextAlign.Center
            )
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//1st row
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
            ) {
                Text(
                    profileData.getString("name"), modifier = Modifier
                        .weight(5f)
                        .background(Color(0xFFE1E3E5)), color = Color(0xFF808281)
                )
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .background(Color.White)
                )
                Text(
                    "MSRP", modifier = Modifier
                        .weight(4f)
                        .fillMaxHeight()
                        .background(Color(0xFFE1E3E5)), color = Color(0xFF808281)
                )
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .background(Color.White)
                )
                Text(
                    "Change", modifier = Modifier
                        .weight(6f)
                        .fillMaxHeight()
                        .background(Color(0xFFE1E3E5)), color = Color(0xFF808281)
                )
            }
            Spacer(modifier = Modifier.height(1.dp))

            // 2nd row
            Row(Modifier.fillMaxWidth()) {
                Text(
                    "Total", modifier = Modifier
                        .weight(5f)
                        .background(Color(0xFFE1E3E5)), color = Color(0xFF808281)
                )
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .background(Color.White)
                )
                Text(
                    String.format("%.2f", insiderData.getDouble("totalMsprSum")),
                    modifier = Modifier
                        .weight(4f)
                        .background(Color(0xFFF2F2F2)),
                    color = Color(0xFFB7B7B7)
                )
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .background(Color.White)
                )
                Text(
                    String.format("%.1f", insiderData.getDouble("totalChangeSum")),
                    modifier = Modifier
                        .weight(6f)
                        .background(Color(0xFFF2F2F2)),
                    color = Color(0xFFB7B7B7)
                )
            }
            Spacer(modifier = Modifier.height(1.dp))

            // 3rd row
            Row(Modifier.fillMaxWidth()) {
                Text(
                    "Positive", modifier = Modifier
                        .weight(5f)
                        .background(Color(0xFFE1E3E5)), color = Color(0xFF808281)
                )
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .background(Color.White)
                )
                Text(
                    String.format("%.2f", insiderData.getDouble("positiveMsprSum")),
                    modifier = Modifier
                        .weight(4f)
                        .background(Color(0xFFF2F2F2)),
                    color = Color(0xFFB7B7B7)
                )
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .background(Color.White)
                )
                Text(
                    String.format("%.1f", insiderData.getDouble("positiveChangeSum")),
                    modifier = Modifier
                        .weight(6f)
                        .background(Color(0xFFF2F2F2)),
                    color = Color(0xFFB7B7B7)
                )
            }
            Spacer(modifier = Modifier.height(1.dp))

            // 4th row
            Row(Modifier.fillMaxWidth()) {
                Text(
                    "Negative", modifier = Modifier
                        .weight(5f)
                        .background(Color(0xFFE1E3E5)), color = Color(0xFF808281)
                )
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .background(Color.White)
                )
                Text(
                    String.format("%.2f", insiderData.getDouble("negativeMsprSum")),
                    modifier = Modifier
                        .weight(4f)
                        .background(Color(0xFFF2F2F2)),
                    color = Color(0xFFB7B7B7)
                )
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .background(Color.White)
                )
                Text(
                    String.format("%.1f", insiderData.getDouble("negativeChangeSum")),
                    modifier = Modifier
                        .weight(6f)
                        .background(Color(0xFFF2F2F2)),
                    color = Color(0xFFB7B7B7)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        ChartFromHTML(path = "file:///android_asset/recommendationChart.html")

        ChartFromHTML(path = "file:///android_asset/epsChart.html")

    }


    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun NewsSection() {

        val newsData by stockDetailViewModel.newsData.collectAsState()
        var showDialog by remember { mutableStateOf(false) }
        var clickedIndex by remember { mutableStateOf(0) }


        if (newsData.length() > 0) {
            val firstNews = newsData.getJSONObject(0)

            Column (Modifier.padding(top=20.dp)){
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ), colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 320.dp)
                        .padding(3.dp)
                        .clickable { showDialog = true;clickedIndex = 0 }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        GlideImage(
                            model = firstNews.getString("image"),
                            contentDescription = "news-logo",
                            contentScale = ContentScale.FillBounds,

                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .fillMaxHeight(0.8f)

                                .clip(shape = RoundedCornerShape(8.dp))

                        )

                        Text(
                            text = firstNews.getString("source") + " " + getDuration(
                                firstNews.getLong(
                                    "datetime"
                                )
                            ),
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(vertical = 4.dp),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = firstNews.getString("headline"),
                            modifier = Modifier.fillMaxWidth(0.9f),
                            fontSize = 14.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }




                for (index in 1 until newsData.length()) {
                    val news = newsData.getJSONObject(index)
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        ), colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(3.dp)
                            .clickable { showDialog = true;clickedIndex = index }
                    ) {

                        Row(modifier = Modifier.padding(start = 20.dp)) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(top = 25.dp)
                            ) {

                                Text(
                                    text = news.getString("source") + " " + getDuration(
                                        news.getLong(
                                            "datetime"
                                        )
                                    ),
                                    modifier = Modifier.padding(bottom = 5.dp),
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = news.getString("headline"),

                                    fontSize = 14.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 6.dp)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                GlideImage(
                                    model = news.getString("image"),
                                    contentDescription = "news-logo",
                                    contentScale = ContentScale.FillBounds,

                                    modifier = Modifier
                                        .size(130.dp, 130.dp)

                                        .clip(shape = RoundedCornerShape(8.dp))

                                )
                            }


                        }

                    }
                }


            }


        } else {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No News Found")
            }
        }


        if (showDialog) {
            val uriHandler = LocalUriHandler.current
            val news = newsData.getJSONObject(clickedIndex)
            AlertDialog(

                containerColor = Color.White,
                onDismissRequest = { showDialog = false },
                title = {
                    Column {
                        Text(
                            news.getString("source"), style = TextStyle(
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Text(
                            formatDate(news.getLong("datetime")),
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = Color.Gray,
                            style = TextStyle(
                                fontSize = 16.sp,

                                )
                        )
                        Divider()
                    }
                },
                text = {
                    Column {
                        Text(
                            news.getString("headline"), style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ), color = Color.Black
                        )
                        Text(news.getString("summary"), color = Color.Black)
                        Row(Modifier.padding(top = 5.dp)) {
                            IconButton(
                                onClick = {


                                    uriHandler.openUri(news.getString("url"))
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.chrome),
                                    contentDescription = "chrome",
                                    tint = Color.Unspecified
                                )
                            }

                            IconButton(
                                onClick = {
                                    uriHandler.openUri(
                                        "https://twitter.com/intent/tweet?text=" + news.getString(
                                            "headline"
                                        ) + "&url=" + news.getString("url")
                                    )


                                },

                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.twitter_x_icon),
                                    contentDescription = "twitter",
                                    tint = Color.Unspecified
                                )
                            }


                            IconButton(
                                onClick = {

                                    uriHandler.openUri(
                                        "https://www.facebook.com/sharer/sharer.php?u=" + news.getString(
                                            "url"
                                        )
                                    )


                                },

                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.facebook),
                                    contentDescription = "facebook",
                                    tint = Color.Unspecified
                                )
                            }
                        }
                    }
                },
                confirmButton = {

                }
            )
        }
    }


    @Composable
    fun SectionLabel(title: String) {
        Text(modifier = Modifier.padding(top = 20.dp, start = 10.dp), text = title, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun StockProfileCard() {

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
                    modifier = Modifier.size(50.dp, 50.dp),
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
    fun ChartFromHTML(
        path: String, visibility: MutableState<Boolean> = remember {
            mutableStateOf(true)
        }
    ) {
        val context = LocalContext.current
        var webView = WebView(context)

        var visible by remember {
            visibility
        }

        var loaded by remember { mutableStateOf(false) }

        webView.apply {
            settings.javaScriptEnabled = true
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    if (!loaded) {
                        view?.evaluateJavascript("run('$searchedStock');", null)

                        loaded = true
                    }
                }
            }
            webView = this
        }

        webView.loadUrl(path)

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {

                webView
            }
        ) {
            if (visible) {
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.GONE
            }
        }


    }

    fun formatDate(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        val format = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
        return format.format(date)
    }

    private fun getDuration(timestamp: Long): String {


        val instant = Instant.ofEpochSecond(timestamp)
        val now = Instant.now()

        val duration = Duration.between(instant, now)
        val days = duration.toDays()
        val hours = duration.toHours() % 24

        return when {
            days > 0 -> {
                val dayString = if (days == 1L) "day" else "days"
                "$days $dayString ago"
            }

            else -> {
                val hourString = if (hours == 1L) "hour" else "hours"
                "$hours $hourString ago"
            }
        }
    }


    fun validateInput(input: String): Boolean {

        val intValue = input.toIntOrNull()


        return intValue != null && intValue > 0
    }


    @Composable
    fun HyperLink(linkText: String, link: String, url: Boolean) {
        val mContext = LocalContext.current
        val text = AnnotatedString.Builder().apply {
            withStyle(
                style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)
            ) {
                append(linkText)
                addStringAnnotation(
                    tag = "URL",
                    annotation = link,
                    start = 0,
                    end = length
                )
            }
        }.toAnnotatedString()
        val uriHandler = LocalUriHandler.current
        ClickableText(text = text, style = TextStyle(

            fontSize = 16.sp
        ), onClick = { offset ->


            if (url) {
                val url = text.getStringAnnotations("URL", offset, offset)
                    .firstOrNull()?.item


                if (url != null) {
                    uriHandler.openUri(url)
                }
            } else {

                val intent = Intent(mContext, StockDetailActivity::class.java)
                intent.putExtra("SearchTicker", link)

                mContext.startActivity(intent)

            }
        }


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