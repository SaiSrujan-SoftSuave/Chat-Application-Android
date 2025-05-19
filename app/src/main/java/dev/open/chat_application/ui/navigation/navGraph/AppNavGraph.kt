package dev.open.chat_application.ui.navigation.navGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import dev.open.chat_application.ui.navigation.Graph
import dev.open.chat_application.ui.screens.auth.AuthScreen
import dev.open.chat_application.ui.screens.users.UserChatScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        route = Graph.AUTH,
        startDestination = AuthRoute.AUTH
    ) {
        composable(route = AuthRoute.AUTH) {
            AuthScreen(navController = navController)
        }
    }
}

fun NavGraphBuilder.chatHomeNavGraph(navController: NavController) {
    navigation(
        route = Graph.CHAT_HOME,
        startDestination = ChatRoute.USERCHATS
    ) {
        composable(route = ChatRoute.USERCHATS) {
            UserChatScreen()
        }
        composable(route = ChatRoute.CHATSCREEN) {

        }
    }
}

object AuthRoute {
    const val AUTH = "auth_route"
}

object ChatRoute {
    const val USERCHATS = "chat_route"
    const val CHATSCREEN = "chat_screen"
}