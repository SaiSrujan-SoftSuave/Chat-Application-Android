package dev.open.chat_application.ui.screens.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.open.chat_application.core.UiEvent
import dev.open.chat_application.data.LoadingScreen
import dev.open.chat_application.data.network.model.UserModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserChatScreen(
    modifier: Modifier = Modifier, viewModel: UserChatViewModel = koinViewModel()
) {
    val uiState = viewModel.userState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier, topBar = {
            ChatTopBar()
        }) { paddingValues ->
        when (uiState.value) {
            is UiEvent.Empty -> {

            }

            is UiEvent.Failure -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = (uiState.value as UiEvent.Failure).error.toString(),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is UiEvent.Loading -> {
                LoadingScreen()
            }

            is UiEvent.Success<*> -> {
                val data = (uiState.value as UiEvent.Success<List<UserModel>>).data
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxHeight()
                ) {
                    items(data, key = { user -> user.id }) { user ->
                        UserChatTile(
                            userName = user.name,
                            isOnline = user.isOnline
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun UserChatScreenPreview() {
    UserChatScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    modifier: Modifier = Modifier,
    title: String = "Chat Screen",
    currentUser: String = "user name",
    isActive: Boolean = false
) {
    TopAppBar(
        title = {
            Column {
                Text(title)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        currentUser,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .background(
                                if (isActive) Color.Green else Color.Red,
                                shape = RoundedCornerShape(5.dp)
                            )
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = if (isActive) "Active" else "InActive",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }, modifier = modifier, actions = {
            IconButton(
                onClick = {  }) {
                Icon(
                    imageVector = Icons.Default.MoreVert, contentDescription = "Back"
                )
            }
        }, navigationIcon = {

        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
@Preview(showBackground = true)
fun ChatTopBarPreview() {
    ChatTopBar()
}

@Composable
fun UserChatTile(
    modifier: Modifier = Modifier, userName: String = "Sai srujan", isOnline: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                userName, style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = if (isOnline) "Online" else "Offline",
                color = if (isOnline) Color.Green else Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                "12:43", style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
fun UserChatTilePreview() {
    UserChatTile()
}