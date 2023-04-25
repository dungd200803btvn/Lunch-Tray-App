
package com.example.lunchtray
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.ui.*
enum class LunchTrayScreen(@StringRes val title:Int ){
    Start(title = R.string.app_name),
    entreeMenu(title = R.string.choose_entree),
    sideDishMenu(title = R.string.choose_side_dish),
    accompanimentMenu(title = R.string.choose_accompaniment),
    Checkout(title = R.string.order_checkout)
}
@Composable
fun LunchTrayAppBar(@StringRes currentScreenTitle:Int,
canNavigateBack:Boolean,
navigateup: ()->Unit,
modifier: Modifier = Modifier){
TopAppBar(title = { Text(stringResource(currentScreenTitle)) },
    modifier = modifier,
    navigationIcon = {
        if(canNavigateBack){
            IconButton(onClick = navigateup) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back_button))
            }
        }
    }
)
}
@Composable
fun LunchTrayApp(modifier: Modifier = Modifier) {
   // tao ra nav controller
     val navController = rememberNavController( )
    // tao back stack bang cach dung navcontrol
    val backStackEntry by navController.currentBackStackEntryAsState()
    // tao bien luu giu man hinh hien tai
    val currentScreen = LunchTrayScreen.valueOf(
        backStackEntry?.destination?.route?: LunchTrayScreen.Start.name
    )
    val viewModel: OrderViewModel = viewModel()

    Scaffold(
        topBar = {
           LunchTrayAppBar(currentScreenTitle = currentScreen.title,
               canNavigateBack = navController.previousBackStackEntry!=null,
               navigateup = { navController.navigateUp() })
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        // create navHost funtion
        NavHost(navController = navController, startDestination = LunchTrayScreen.Start.name,
       modifier = modifier.padding(innerPadding)){
            // trong navHost xay dung cac composable cua cac route 
       composable(route =LunchTrayScreen.Start.name ){
           StartOrderScreen(onStartOrderButtonClicked = { navController.navigate(LunchTrayScreen.entreeMenu.name) })
       }
            composable(route = LunchTrayScreen.entreeMenu.name){
                EntreeMenuScreen(
                    options = DataSource.entreeMenuItems,
                    onCancelButtonClicked = { viewModel.resetOrder()
                      navController.popBackStack(LunchTrayScreen.Start.name,false)                      },
                    onNextButtonClicked = { navController.navigate(LunchTrayScreen.sideDishMenu.name) },
                    onSelectionChanged = {item-> viewModel.updateEntree(item)}
                )
            }

            composable(route = LunchTrayScreen.sideDishMenu.name){
               SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onCancelButtonClicked = { viewModel.resetOrder()
                        navController.popBackStack(LunchTrayScreen.Start.name,false)                      },
                    onNextButtonClicked = { navController.navigate(LunchTrayScreen.accompanimentMenu.name) },
                    onSelectionChanged = {item-> viewModel.updateSideDish(item)}
                )
            }

            composable(route = LunchTrayScreen.accompanimentMenu.name){
              AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onCancelButtonClicked = { viewModel.resetOrder()
                        navController.popBackStack(LunchTrayScreen.Start.name,false)                      },
                    onNextButtonClicked = { navController.navigate(LunchTrayScreen.Checkout.name) },
                    onSelectionChanged = {item-> viewModel.updateAccompaniment(item)}
                )
            }
            composable(route = LunchTrayScreen.Checkout.name){
                CheckoutScreen(
                    orderUiState = uiState,
                    onNextButtonClicked = { viewModel.resetOrder()
                        navController.popBackStack(LunchTrayScreen.Start.name,false)        },
                    onCancelButtonClicked = { viewModel.resetOrder()
                        navController.popBackStack(LunchTrayScreen.Start.name,false) })
            }


        }

    }
}
