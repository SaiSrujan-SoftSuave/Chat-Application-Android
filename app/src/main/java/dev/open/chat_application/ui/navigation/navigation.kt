package dev.open.chat_application.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.open.chat_application.core.SessionManager
import dev.open.chat_application.ui.navigation.navGraph.authNavGraph
import dev.open.chat_application.ui.navigation.navGraph.chatHomeNavGraph
import org.koin.compose.koinInject


@Composable
fun RootNavGraph(modifier: Modifier = Modifier, navController: NavHostController) {
    val sessionManager: SessionManager = koinInject()
    val isLoggedIn by sessionManager.isLoggedIn.collectAsState(initial = false)
    NavHost(
        navController = navController,
        startDestination = if(isLoggedIn) Graph.CHAT_HOME else Graph.AUTH,
        modifier = modifier
    ) {
        authNavGraph(navController)
        chatHomeNavGraph(navController)
    }
}


object Graph {
    const val ROOT = "root"
    const val AUTH = "auth"
    const val CHAT_HOME = "chat_home"
}