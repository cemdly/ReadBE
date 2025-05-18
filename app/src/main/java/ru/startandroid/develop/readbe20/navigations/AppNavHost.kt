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
    object Page : NavRoute("page_screen/{bookId}") {
        fun createRoute(bookId: String): String = "page_screen/$bookId"
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoute.Home.route
    ) {
        homeGraph(navController)
        libraryGraph(navController)
        pageGraph(navController)
    }
}
fun NavGraphBuilder.homeGraph(navController: NavController) {
    composable(NavRoute.Home.route) {
        HomeScreen(
            onNavigateToLibrary = { navController.navigate(NavRoute.Library.route) },
            onOpenBook = { bookId -> navController.navigate(NavRoute.Page.createRoute(bookId)) }
        )
    }
}

fun NavGraphBuilder.libraryGraph(navController: NavController) {
    composable(NavRoute.Library.route) {
        LibraryScreen(
            onOpenBook = { bookId ->
                navController.navigate(NavRoute.Page.createRoute(bookId))
            }
        )
    }
}

fun NavGraphBuilder.pageGraph(navController: NavController) {
    composable(
        route = NavRoute.Page.route,
        arguments = listOf(
            navArgument("bookId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val bookId = backStackEntry.arguments?.getString("bookId") ?: "Unknown"
        PageScreen(
            bookId = bookId,
            onBack = {
                navController.popBackStack()
            }
        )
    }
}
