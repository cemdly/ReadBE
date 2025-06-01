package navigation

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

    data class Page(val bookUri: String) : NavRoute("page_screen/$bookUri")
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
        LibraryScreen(onOpenBook = { uri ->
            navController.navigate(NavRoute.Page(uri.toString()).route)
        })
    }

    composable(
        route = "page_screen/{bookUri}",
        arguments = listOf(navArgument("bookUri") { type = NavType.StringType })
    ) { backStackEntry ->
        val bookUri = backStackEntry.arguments?.getString("bookUri")
        if (bookUri != null) {
            PageScreen(bookUri = bookUri)
        } else {

        }
    }
}
