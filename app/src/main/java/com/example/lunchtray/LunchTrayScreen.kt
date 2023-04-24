
package com.example.lunchtray

import androidx.annotation.StringRes
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.ui.OrderViewModel


enum class LunchTray(@StringRes val title:Int ){
    Start(title = R.string.app_name),
    entreeMenu(title = R.string.choose_entree),
    sideDishMenu(title = R.string.choose_side_dish),
    accompanimentMenu(title = R.string.choose_accompaniment),
    Checkout(title = R.string.order_checkout)
}
// TODO: AppBar

@Composable
fun LunchTrayApp(modifier: Modifier = Modifier) {
    // TODO: Create Controller and initialization
     val navController = rememberNavController( )
    // Create ViewModel
    val viewModel: OrderViewModel = viewModel()

    Scaffold(
        topBar = {
            // TODO: AppBar
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        // TODO: Navigation host
    }
}
