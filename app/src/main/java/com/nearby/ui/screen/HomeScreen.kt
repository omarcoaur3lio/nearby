package com.nearby.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.GoogleMap
import com.nearby.data.model.Market
import com.nearby.data.model.mock.mockCategories
import com.nearby.data.model.mock.mockMarkets
import com.nearby.ui.comonents.category.NearbyCategoryFilterChipList
import com.nearby.ui.comonents.market.NearbyMarketCardList
import com.nearby.ui.theme.Gray100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiSate: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    onNavigateToMarketDetails: (Market) -> Unit
) {
    val bottomSheetState = rememberBottomSheetScaffoldState()
    var isBottomSheetStateOpened by remember { mutableStateOf(true) }

    val configuration = LocalConfiguration.current

    LaunchedEffect(true) {
        onEvent(HomeUiEvent.OnFetchCategories)
    }

    if (isBottomSheetStateOpened) {
        BottomSheetScaffold(
            modifier = modifier,
            scaffoldState = bottomSheetState,
            sheetContainerColor = Gray100,
            sheetPeekHeight = configuration.screenHeightDp.dp * 0.5f,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetContent = {

                if (!uiSate.markets.isNullOrEmpty()) {
                    NearbyMarketCardList(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        markets = uiSate.markets,
                        onMarketClick = { selectedMarket ->
                            onNavigateToMarketDetails(selectedMarket)
                        }
                    )
                }
            },
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            bottom = it.calculateBottomPadding().minus(32.dp)
                        )
                ) {
                    GoogleMap(modifier = Modifier.fillMaxSize())

                    if (!uiSate.categories.isNullOrEmpty()) {
                        NearbyCategoryFilterChipList(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp)
                                .align(Alignment.TopStart),
                            categories = uiSate.categories,
                            onSelectedCategoryChanged = { selectedCategory ->
                                onEvent(HomeUiEvent.OnFetchMarkets(categoryId = selectedCategory.id))

                            }
                        )
                    }
                }

            }
        )
    }

}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(onNavigateToMarketDetails = {}, uiSate = HomeUiState(), onEvent = {})
}