package com.example.stocksearch

import DataService

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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler


import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle

import androidx.compose.ui.text.withStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KSuspendFunction1




import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.AddCircle

import androidx.compose.material3.Card

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp

import androidx.compose.ui.unit.dp
import sh.calvin.reorderable.ReorderableColumn
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyColumnState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        DataService.init(this)
        setTheme(R.style.Theme_StockSearch)
        super.onCreate(savedInstanceState)
        setContent {
            StockSearchTheme {
                // A surface container using the 'background' color from the theme
                MainContent()
            }

        }
    }
}

object GlobalValues {
    var indentationValue = 16.dp
}


@Composable
fun MainContent() {

    val portfolioViewModel: PortfolioViewModel =
        PortfolioViewModel()
    LaunchedEffect(key1 = Unit) {
        while (true) {


            withContext(Dispatchers.IO) {
                portfolioViewModel.fetchData()

            }
            delay(15000)
        }
    }


    val watchlistViewModel: WatchlistViewModel =
        WatchlistViewModel()
    LaunchedEffect(key1 = Unit) {
        while (true) {


            withContext(Dispatchers.IO) {
                watchlistViewModel.fetchData()
            }
            delay(15000)
        }

    }


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


            val watchlistLoaded by remember { DataService.watchlistLoaded }

            val portofolioLoaded by remember { DataService.portofolioLoaded }
if (watchlistLoaded&&portofolioLoaded){
/*
    LazyColumn(modifier = Modifier.fillMaxSize()) {

        item {
            TimeSection()
        }


        item {
            MyAppLabel(labelText = "PORTFOLIO")
        }


        item {
            PortfolioSection(portfolioViewModel)
        }


        item {
            MyAppLabel(labelText = "FAVORITES")
        }


        item {
            WatchlistSection(watchlistViewModel)
        }


        item {
            ReferenceSection()
        }
    }*/

    Column (modifier = Modifier
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())){
        TimeSection()
        MyAppLabel(labelText = "PORTFOLIO")
        PortfolioSection(portfolioViewModel)
        MyAppLabel(labelText = "FAVORITES")
        WatchlistSection(watchlistViewModel)
        ReferenceSection()
    }





}else{


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
    showCloseBtn: Boolean = true,
    showSearchBtn: Boolean = true,
    showBackBtn: Boolean = true,
    title: String = "Stocks"
) {
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

                if (showSearchBtn) {
                    IconButton(onClick = {/* Do Something*/ }) {
                        Icon(Icons.Filled.Search, null)
                    }
                }

                if (showCloseBtn) {
                    IconButton(onClick = {/* Do Something*/ }) {
                        Icon(Icons.Filled.Close, null)
                    }
                }

            }
        )
    }

}

@Composable
fun TimeSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = GlobalValues.indentationValue, vertical = 14.dp)
    ) {
        Text(
            "21 March 2024", color = Color.Gray,
            fontSize = 18.sp
        )
    }
}


@Composable
fun MyAppLabel(labelText: String) {

    Surface(
        color = Color(0xFFE1E1E1),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            labelText,
            color = Color.Black,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = GlobalValues.indentationValue, vertical = 6.dp)
        )

    }
}


@Composable
fun PortfolioSection(portfolioViewModel: PortfolioViewModel) {



    PortfolioBalance(portfolioViewModel = portfolioViewModel)

    val stocksListState = portfolioViewModel.stocksState


    //StockCardList(stocksListState, withDismiss = false)


    //LongPressHandleReorderableColumnScreen(stocksListState)
    SimpleLongPressHandleReorderableLazyColumnScreen(stocksListState,withDismiss = false)
}


@Composable
fun PortfolioBalance(portfolioViewModel: PortfolioViewModel) {


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = GlobalValues.indentationValue, vertical = 3.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
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
fun StockCardList(
    stocksListState: StateFlow<List<Stock>>, withDismiss: Boolean,
    removeMethod: KSuspendFunction1<Stock, Boolean>? = null
) {

    val stocksList by stocksListState.collectAsState()
    LazyColumn {

        itemsIndexed(
            items = stocksList,
            // Provide a unique key based on the email content
            key = { _, item -> item.hashCode() }
        ) { _, stock ->

            if (withDismiss && removeMethod != null) {
                StockCardWithDismiss(stock, onRemove = removeMethod)
            } else {

                StockCard(stock)
            }
            Divider()
        }
    }


}


@Composable
fun StockCard(singleStock: Stock) {


    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()


        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = GlobalValues.indentationValue, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


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


                    Column(horizontalAlignment = Alignment.End) {


                        Text(
                            text = "$${"%.2f".format(singleStock.currentValue)}",
                            color = Color.Black,
                            fontSize = 22.sp,

                            )


                        Row {

                            val fontColor: Any
                            val imageResource: Int?
                            if (singleStock.valueChange > 0.0) {
                                imageResource = R.drawable.trending_up
                                fontColor = Color(0xFF37AF4C)
                            } else if (singleStock.valueChange < 0.0) {
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


                    Image(
                        painter = painterResource(R.drawable.right_arrow),
                        contentDescription = "detail",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: DismissState) {
    val color = Color(0xFFFF1744)
    val direction = dismissState.dismissDirection

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Spacer(modifier = Modifier)
        Icon(

            painter = painterResource(R.drawable.delete),
            contentDescription = "Delete",
            tint = Color.White
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockCardWithDismiss(
    singleStock: Stock,
    onRemove: KSuspendFunction1<Stock, Boolean>
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
        show, exit = fadeOut(spring())
    ) {
        SwipeToDismiss(
            state = dismissState,
            modifier = Modifier,
            directions = setOf(DismissDirection.EndToStart),
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

            val result =onRemove(currentItem)

            if (result) {
                Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show()
            }


        }
    }


}

@Composable
fun WatchlistSection(watchlistViewModel: WatchlistViewModel) {



    val stocksListState = watchlistViewModel.stocksState

/*
    StockCardList(
        stocksListState,
        withDismiss = true,
        removeMethod = watchlistViewModel::removeItem
    )*/

    //LongPressHandleReorderableColumnScreen(stocksListState)
    SimpleLongPressHandleReorderableLazyColumnScreen(stocksListState,withDismiss = true,watchlistViewModel::removeItem)





}


@Composable
fun ReferenceSection() {
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
        val uriHandler = LocalUriHandler.current
        ClickableText(text = text, style = TextStyle(

            fontStyle = FontStyle.Italic,
            fontSize = 16.sp
        ),
            modifier = Modifier.padding(16.dp), onClick = { offset ->

                val url = text.getStringAnnotations("URL", offset, offset)
                    .firstOrNull()?.item


                if (url != null) {
                    uriHandler.openUri(url)
                }
            }


        )


    }
}



@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SimpleLongPressHandleReorderableLazyColumnScreen(stocksListState: StateFlow<List<Stock>>,withDismiss: Boolean,
                                                     removeMethod: KSuspendFunction1<Stock, Boolean>? = null) {


    var list by remember { mutableStateOf(emptyList<Stock>()) }

    val lazyListState = rememberLazyListState()
    val reorderableLazyColumnState = rememberReorderableLazyColumnState(lazyListState) { from, to ->
        list = list.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    }

    LaunchedEffect(stocksListState) {
        stocksListState.collect { newList ->
            list = newList
        }
    }

    LazyColumn(

        userScrollEnabled = false,
        state = lazyListState,

        modifier = Modifier.height((list.size*65).dp),

    ) {
        items(list, key = { it.ticker}) {
            ReorderableItem(reorderableLazyColumnState, it.ticker) { isDragging ->
                val interactionSource = remember { MutableInteractionSource() }
                //val density = androidx.compose.ui.platform.LocalDensity.current
                Card(
                    onClick = {},
                    shape = RectangleShape,
/* calculate per card height to avoid nesting lazy column exception

                        .onGloballyPositioned { coordinates ->
                            val heightInPixels = coordinates.size.height
                            val heightInDp: Dp = with(density) { heightInPixels.toDp() }
                            println("Component height: $heightInDp")
                        }*/
                    interactionSource = interactionSource,
                ) {
                    Surface(modifier =  Modifier.longPressDraggableHandle(
                        onDragStarted = {

                        },
                        onDragStopped = {

                        },
                        interactionSource = interactionSource,
                    )){

                        if (withDismiss && removeMethod != null) {
                            StockCardWithDismiss(it, onRemove = removeMethod)
                        } else {

                            StockCard(it)
                        }

                    }

                }
                Divider()
            }}
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StockSearchTheme {
        MainContent()
    }
}


