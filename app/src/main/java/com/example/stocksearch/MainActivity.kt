package com.example.stocksearch

import VolleyRequest
import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue


import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


import androidx.compose.ui.res.painterResource

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stocksearch.ui.theme.StockSearchTheme


import androidx.compose.foundation.lazy.itemsIndexed

import androidx.compose.runtime.*


import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext


import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle

import androidx.compose.ui.text.withStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_StockSearch)
        super.onCreate(savedInstanceState)
        setContent {
            StockSearchTheme {
                // A surface container using the 'background' color from the theme
                MainContent()
            }

    }
}}

object GlobalValues {
    var indentationValue = 16.dp
}




@Composable
fun MainContent() {


    Scaffold(
        topBar = {
            MyAppTopBar()
        },
    ) {
            innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

            color = Color.White
        ) {

Column {
    TimeSection()
    MyAppLabel(labelText = "PORTFOLIO")


    PortfolioSection()
    MyAppLabel(labelText = "FAVORITES")
    WatchlistSection()

    ReferenceSection()


} } }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppTopBar( showCloseBtn: Boolean = true,showSearchBtn: Boolean = true,showBackBtn: Boolean = true,title:String="Stocks"){
    Surface(shadowElevation = 8.dp) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black,
            ),
            title = {
                Text(title, modifier = Modifier.padding(horizontal = GlobalValues.indentationValue))
            },

            navigationIcon = {
                if (showBackBtn) {
                    IconButton(onClick = {/* Do Something*/ }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                }


            },

            actions = {

                if (showSearchBtn){
                    IconButton(onClick = {/* Do Something*/ }) {
                        Icon(Icons.Filled.Search, null)
                    }}

                if (showCloseBtn) {
                    IconButton(onClick = {/* Do Something*/ }) {
                        Icon(Icons.Filled.Close, null)
                    }}

            }
        )
    }

}
@Composable
fun TimeSection(){
    Surface ( modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = GlobalValues.indentationValue, vertical = 14.dp)){
        Text("21 March 2024",color = Color.Gray,
            fontSize = 18.sp)
    }
}



@Composable
fun MyAppLabel(labelText:String){

    Surface(
        color =Color(0xFFE1E1E1),
        modifier = Modifier.fillMaxWidth()
    ){
    Text(labelText,color = Color.Black,
        fontSize = 13.sp, modifier = Modifier.padding(horizontal = GlobalValues.indentationValue, vertical = 6.dp))

    }
}


@Composable
fun PortfolioSection(){

    val portfolioStockViewModel: PortfolioViewModel = PortfolioViewModel(VolleyRequest(LocalContext.current))
    LaunchedEffect(key1 = Unit) {
        while (true) {


            withContext(Dispatchers.IO) {
                portfolioStockViewModel.fetchData()

            }
            delay(15000)
        }
    }

    PortfolioBalance(portfolioViewModel =portfolioStockViewModel)

    val stocksListState=portfolioStockViewModel.stocksState


    PortfolioStockCardList(stocksListState)



}







@Composable
fun PortfolioBalance(portfolioViewModel: PortfolioViewModel){




    Surface ( modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = GlobalValues.indentationValue, vertical = 3.dp)){

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column{
                Text(
                    text = "Net Worth",
                    color = Color.Gray,
                    fontSize = 18.sp,

                    )
                Text(
                    text = "$${"%.2f".format(portfolioViewModel.netWorth)}",
                    color = Color.Gray,
                    fontSize = 18.sp,

                    )
            }
            Column {
                Text(
                    text = "Cash Balance",
                    color = Color.Gray,
                    fontSize = 18.sp,

                    )
                Text(

                    text = "$${"%.2f".format(portfolioViewModel.portfolioBalance)}",
                    color = Color.Gray,
                    fontSize = 18.sp,

                    )
            }
        }




    }



Divider()
}



@Composable
fun PortfolioStockCardList(stocksListState: StateFlow<List<Stock>>) {

    val stocksList by stocksListState.collectAsState()
    LazyColumn {

        itemsIndexed(
            items = stocksList,
            // Provide a unique key based on the email content
            key = { _, item -> item.hashCode() }
        ) { _, stock ->


            StockCard(stock)
            Divider()
        }
    }



}



@Composable
fun StockCard(singleStock: Stock) {


    Surface ( modifier = Modifier
        .fillMaxWidth()
        ){

        Row(
            modifier = Modifier.fillMaxWidth()


        ) {

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = GlobalValues.indentationValue, vertical = 5.dp),horizontalArrangement = Arrangement.SpaceBetween){


            Column {
                Text(
                    text = singleStock.ticker,
                    color = Color.Black,
                    fontSize = 22.sp,

                    )
                Text(
                    text = singleStock.name,
                    color = Color.Gray,
                    fontSize = 18.sp,

                    )
            }

            Row {


            Column( horizontalAlignment = Alignment.End) {



                Text(
                    text = "$${"%.2f".format(singleStock.currentValue)}",
                    color = Color.Black,
                    fontSize = 22.sp,

                    )


                Row {

                    val fontColor:Any
                    val imageResource:Int?
                    if (singleStock.valueChange >0.0) {
                         imageResource = R.drawable.trending_up
                        fontColor= Color(0xFF37AF4C)
                    } else if (singleStock.valueChange <0.0){
                         imageResource = R.drawable.trending_down
                        fontColor=Color(0xFFEB502E)
                    } else {
                        imageResource = null
                        fontColor= Color.Black
                    }


                    if (imageResource!=null){
                        Image(
                            painter = painterResource(imageResource),
                            contentDescription = "My Image"
                        )
                    }

                    Text(

                        text = "$${"%.2f".format(singleStock.valueChange)}",
                        color = fontColor,
                        fontSize = 18.sp,

                        )

                    Text(

                        text = "(${"%.2f".format(singleStock.percentValueChange)}%)",
                        color = fontColor,
                        fontSize = 18.sp,

                        )
                }

            }


Image(painter =  painterResource(R.drawable.right_arrow), contentDescription = "detail", modifier = Modifier.align(Alignment.CenterVertically))
            }
        }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: DismissState) {
    val color = when (dismissState.dismissDirection) {
        DismissDirection.StartToEnd -> Color(0xFFFF1744)
        DismissDirection.EndToStart -> Color(0xFF1DE9B6)
        null -> Color.Transparent
    }
    val direction = dismissState.dismissDirection

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (direction == DismissDirection.StartToEnd) Icon(
            Icons.Default.Delete,
            contentDescription = "delete"
        )
        Spacer(modifier = Modifier)
        if (direction == DismissDirection.EndToStart) Icon(

            painter = painterResource(R.drawable.full_star),
            contentDescription = "Archive"
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockCardWithDismiss(
    singleStock: Stock,
    onRemove: (Stock) -> Unit
) {
    val context = LocalContext.current
    var show by remember { mutableStateOf(true) }
    val currentItem by rememberUpdatedState(singleStock)
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart || it == DismissValue.DismissedToEnd) {
                show = false
                true
            } else false
        }, positionalThreshold = { 150.dp.toPx() }
    )
    AnimatedVisibility(
        show,exit = fadeOut(spring())
    ) {
        SwipeToDismiss(
            state = dismissState,
            modifier = Modifier,
            background = {
                DismissBackground(dismissState)
            },
            dismissContent = {
                StockCard(singleStock)
            }
        )
    }

    LaunchedEffect(show) {
        if (!show) {
            delay(800)
            onRemove(currentItem)
            Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
        }
    }
}
@Composable
fun WatchlistSection(){
    val watchlistStockViewModel: WatchlistViewModel = WatchlistViewModel(VolleyRequest(LocalContext.current))
    LaunchedEffect(key1 = Unit) {
        while (true) {


            withContext(Dispatchers.IO) {
                watchlistStockViewModel.fetchData()
            }
            delay(15000)
        }
    }
    WatchlistStockCardList(watchlistStockViewModel= watchlistStockViewModel)

}


@Composable
fun WatchlistStockCardList(watchlistStockViewModel: WatchlistViewModel) {

    val stocksList by watchlistStockViewModel.stocksState.collectAsState()
    LazyColumn {

        itemsIndexed(
            items = stocksList,
            // Provide a unique key based on the email content
            key = { _, item -> item.hashCode() }
        ) { _, stock ->

            Divider()
            StockCardWithDismiss(stock,onRemove = watchlistStockViewModel::removeItem)
        }
    }



}


@Composable
fun ReferenceSection(){
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {


        val text = AnnotatedString.Builder().apply {
            withStyle(
                style = SpanStyle(color = Color.Gray)
            ) {
                append("Powered by Finnhub")
                addStringAnnotation(
                    tag = "URL",
                    annotation = "https://www.finnhub.io/",
                    start = 0,
                    end = length
                )
            }
        }.toAnnotatedString()

        Text(text = text,style = TextStyle(

            fontStyle = FontStyle.Italic,
            fontSize = 16.sp
        ),
            modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StockSearchTheme {
        MainContent()
    }
}