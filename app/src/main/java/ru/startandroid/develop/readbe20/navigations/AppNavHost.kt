package navigation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.startandroid.develop.readbe20.screens.HomeScreen
import ru.startandroid.develop.readbe20.screens.LibraryScreen
import ru.startandroid.develop.readbe20.screens.PageScreen

sealed class NavRoute(val route: String) {
    object Home : NavRoute("home_screen")
    object Library : NavRoute("library_screen")
    data class Page(val encodedBookUri: String) : NavRoute("page_screen/$encodedBookUri")
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoute.Home.route
    ) {
        mainGraph(navController)
    }
}

fun NavGraphBuilder.mainGraph(navController: NavController) {
    composable(NavRoute.Home.route) {
        HomeScreen(
            onNavigateToLibrary = { navController.navigate(NavRoute.Library.route) },
            onNavigateToPage = { navController.navigate(NavRoute.Page("default_uri").route) }
        )
    }

    composable(NavRoute.Library.route) {
        LibraryScreen(
            onOpenBook = { safeUri ->
                navController.navigate(NavRoute.Page(safeUri).route)
            },
            onNavigateToHome = {
                navController.navigate(NavRoute.Home.route)
            }
        )
    }

    composable(
        route = "page_screen/{bookUri}",
        arguments = listOf(navArgument("bookUri") { type = NavType.StringType })
    ) { backStackEntry ->
        val encodedUri = backStackEntry.arguments?.getString("bookUri")
        val bookUri = encodedUri?.let { Uri.decode(it) }

        if (bookUri != null) {
            PageScreen(bookUri = bookUri)
        } else {

        }
    }

}