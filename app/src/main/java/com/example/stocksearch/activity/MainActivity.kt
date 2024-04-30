package com.example.stocksearch.activity


import DataService
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stocksearch.viewmodel.AutocompleteViewModel
import com.example.stocksearch.viewmodel.PortfolioViewModel
import com.example.stocksearch.R
import com.example.stocksearch.Stock
import com.example.stocksearch.viewmodel.WatchlistViewModel
import com.example.stocksearch.ui.theme.StockSearchTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyColumnState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import kotlin.reflect.KSuspendFunction1

object GlobalValues {
    var indentationValue = 16.dp
}

class MainActivity : ComponentActivity() {
    val portfolioViewModel =
        PortfolioViewModel()
    val watchlistViewModel =
        WatchlistViewModel()
    val autocompleteViewModel =
        AutocompleteViewModel()


    private var timer: Timer? = null


    private fun startTimer() {
        if (timer == null) {

            timer = Timer()

            portfolioViewModel.fetchData()
            watchlistViewModel.fetchData()
            timer?.schedule(object : TimerTask() {
                override fun run() {

                    portfolioViewModel.fetchData()
                    watchlistViewModel.fetchData()
                }
            }, 15000, 15000)
        }
    }

    private fun stopTimer() {

        timer?.cancel()
        timer = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DataService.init(this)
        setTheme(R.style.Theme_StockSearch)
        super.onCreate(savedInstanceState)
        setContent {
            StockSearchTheme {

                MainContent()
            }

        }
    }

    override fun onResume() {
        super.onResume()

        startTimer()
    }

    override fun onPause() {
        super.onPause()

        stopTimer()
    }


    @Composable
    fun MainContent() {


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


                val watchlistLoaded by remember { watchlistViewModel.watchlistFirstLoaded }
                val portofolioLoaded by remember { portfolioViewModel.portofolioFirstLoaded }
                if (watchlistLoaded && portofolioLoaded) {


                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState())
                    ) {
                        TimeSection()
                        MyAppLabel(labelText = "PORTFOLIO")
                        PortfolioSection()
                        MyAppLabel(labelText = "FAVORITES")
                        WatchlistSection()
                        ReferenceSection()
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


            var showClearBtn by remember { mutableStateOf(false) }
            var showSearchBtn by remember { mutableStateOf(true) }
            var showBackBtn by remember { mutableStateOf(false) }
            var showTitle by remember { mutableStateOf(true) }
            var showInput by remember { mutableStateOf(false) }


            val inputFocusRequester = remember { FocusRequester() }

            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                ),
                title = {
                    if (showTitle) {
                        Text(
                            "Stocks",
                            modifier = Modifier.padding(horizontal = GlobalValues.indentationValue)
                        )
                    }
                },

                navigationIcon = {
                    if (showBackBtn) {
                        IconButton(onClick = {
                            showInput = false
                            showClearBtn = false
                            showTitle = true
                            showSearchBtn = true
                            showBackBtn = false

                        }) {
                            Icon(painter = painterResource(id = R.drawable.ic_action_back), null)
                        }
                    }


                },

                actions = {

                    val mContext = LocalContext.current
                    var textInput by remember { mutableStateOf("") }

                    var menuShouldExpand by remember { mutableStateOf(false) }
                    if (showInput) {

                        LaunchedEffect(!menuShouldExpand) {

                            inputFocusRequester.requestFocus()
                        }



                        TextField(

                            value = textInput,
                            modifier = Modifier
                                .width(290.dp)
                                .focusRequester(inputFocusRequester),

                            onValueChange = { newValue ->


                                if (newValue != textInput) {

                                    textInput = newValue.uppercase()

                                    autocompleteViewModel.fetchData(textInput)


                                }

                            },

                            singleLine = true,

                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(onSearch = {


                                val intent = Intent(mContext, StockDetailActivity::class.java)
                                intent.putExtra("SearchTicker", textInput)

                                mContext.startActivity(intent)


                            }

                            ),


                            )


                        LaunchedEffect(textInput) {
                            snapshotFlow { textInput }
                                .distinctUntilChanged()
                                .collectLatest { newString ->
                                    delay(500)
                                    //if user not changing
                                    if (newString == textInput) {

                                        menuShouldExpand = true
                                    }

                                }
                        }

                        val autocompleteResultMap by autocompleteViewModel.autocompleteResultMap.collectAsState()



                        DropdownMenu(
                            expanded = menuShouldExpand && textInput != "" && autocompleteResultMap.containsKey(
                                textInput
                            ) && autocompleteResultMap[textInput]!!.isNotEmpty(),
                            onDismissRequest = { menuShouldExpand = false },
                            modifier = Modifier
                                .width(290.dp)
                                .heightIn(max = 350.dp)

                        ) {


                            autocompleteResultMap[textInput]?.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item.ticker + " | " + item.name) },
                                    onClick = {
                                        textInput = item.ticker

                                        menuShouldExpand = false

                                    })
                            }
                        }


                    }


                    if (showSearchBtn) {
                        IconButton(onClick = {
                            showInput = true
                            showClearBtn = true
                            showTitle = false
                            showBackBtn = true

                            showSearchBtn = false

                        }) {
                            Icon(painterResource(id = R.drawable.search), null)
                        }
                    }

                    if (showClearBtn) {
                        IconButton(onClick = { textInput = "" }) {
                            Icon(painterResource(id = R.drawable.ic_action_close), null)
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

            val currentTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            Text(
                dateFormat.format(currentTime), color = Color.Gray,
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
                modifier = Modifier.padding(
                    horizontal = GlobalValues.indentationValue,
                    vertical = 6.dp
                )
            )

        }
    }


    @Composable
    fun PortfolioSection() {


        PortfolioBalance()

        val stocksListState = portfolioViewModel.stocksState



        ReorderableStockCardList(stocksListState, withDismiss = false)
    }


    @Composable
    fun PortfolioBalance() {


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

                val result = onRemove(currentItem)

                if (result) {
                    Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show()
                }


            }
        }


    }

    @Composable
    fun WatchlistSection() {


        val stocksListState = watchlistViewModel.stocksState


        ReorderableStockCardList(
            stocksListState,
            withDismiss = true,
            watchlistViewModel::removeItem
        )


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
    fun ReorderableStockCardList(
        stocksListState: StateFlow<List<Stock>>, withDismiss: Boolean,
        removeMethod: KSuspendFunction1<Stock, Boolean>? = null
    ) {


        var list by remember { mutableStateOf(emptyList<Stock>()) }

        val lazyListState = rememberLazyListState()
        val reorderableLazyColumnState =
            rememberReorderableLazyColumnState(lazyListState) { from, to ->
                list = list.toMutableList().apply {
                    add(to.index, removeAt(from.index))
                }
            }

        LaunchedEffect(stocksListState) {
            stocksListState.collect { newList ->
                list = newList
            }
        }
        val mContext = LocalContext.current
        LazyColumn(

            userScrollEnabled = false,
            state = lazyListState,

            modifier = Modifier.height((list.size * 65).dp),

            ) {


            items(list, key = { it.ticker }) {
                ReorderableItem(reorderableLazyColumnState, it.ticker)

                { isDragging ->
                    val interactionSource = remember { MutableInteractionSource() }
                    //val density = androidx.compose.ui.platform.LocalDensity.current
                    Card(
                        onClick = {

                            val intent = Intent(mContext, StockDetailActivity::class.java)
                            intent.putExtra("SearchTicker", it.ticker)

                            mContext.startActivity(intent)
                        },
                        shape = RectangleShape,
                        /* calculate per card height to avoid nesting lazy column exception

                                            .onGloballyPositioned { coordinates ->
                                                val heightInPixels = coordinates.size.height
                                                val heightInDp: Dp = with(density) { heightInPixels.toDp() }
                                                println("Component height: $heightInDp")
                                            }*/
                        interactionSource = interactionSource,
                    ) {
                        Surface(
                            modifier = Modifier.longPressDraggableHandle(
                                onDragStarted = {

                                },
                                onDragStopped = {

                                },
                                interactionSource = interactionSource,
                            )
                        ) {

                            if (withDismiss && removeMethod != null) {
                                StockCardWithDismiss(it, onRemove = removeMethod)
                            } else {

                                StockCard(it)
                            }

                        }

                    }
                    Divider()
                }
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        StockSearchTheme {
            MainContent()
        }
    }


}