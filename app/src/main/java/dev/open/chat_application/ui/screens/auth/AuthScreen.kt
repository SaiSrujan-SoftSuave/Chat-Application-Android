package dev.open.chat_application.ui.screens.auth

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.open.chat_application.core.UiEvent
import dev.open.chat_application.data.validateEmail
import dev.open.chat_application.data.validatePassword
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        // Pager with indicator
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = false,
            pageSize = PageSize.Fill,
            key = { it }
        ) { page ->
            when (page) {
                0 -> SignInContent(){
//                navController.navigateUp()
                }
                1 -> SignUpContent(){
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            page = 0,
                            animationSpec = tween(
                                durationMillis = 250,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                }
            }
        }

//        HorizontalPagerIndicator(
//            pagerState = pagerState,
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .padding(vertical = 12.dp)
//        )

        // Bottom Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val isSignInSelected = pagerState.currentPage == 0
            ElevatedButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            page = 0,
                            animationSpec = tween(
                                durationMillis = 250,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (isSignInSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (isSignInSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Sign In")
            }

            ElevatedButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            page = 1,
                            animationSpec = tween(
                                durationMillis = 250,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (!isSignInSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (!isSignInSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Sign Up")
            }
        }
    }
}

@Composable
private fun SignInContent(onSuccess: () -> Unit = {}) {
    val viewModel = koinViewModel<AuthViewModel>()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val event by viewModel.signInUiEvent.collectAsState()
    val emailErrorText by remember {
        derivedStateOf {
            validateEmail(email)
        }
    }
    var passwordVisibility by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val passwordErrorText by remember {
        derivedStateOf {
            validatePassword(password)
        }
    }
    val isEnabled by remember { derivedStateOf {
        emailErrorText.isNullOrEmpty() && passwordErrorText.isNullOrEmpty()
    } }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            supportingText = {
                Text(
                    text = emailErrorText ?: "",
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                Text(
                    text = passwordErrorText ?: "",
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    color = MaterialTheme.colorScheme.error
                )
            },
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisibility = !passwordVisibility }
                ) {
                    Icon(
                        if (passwordVisibility) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Visibility"
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            buttonText = "Sign In",
            onClick = { viewModel.signIn(email, password) },
            enabled = isEnabled,
            isLoading = isLoading
        )
    }

    when(event){
        is UiEvent.Loading -> {
            isLoading = true
        }
        is UiEvent.Success -> {
            isLoading = false
            onSuccess()
        }
        is UiEvent.Failure -> {
            isLoading = false
            Toast.makeText(context,(event as UiEvent.Failure).error, Toast.LENGTH_SHORT).show()
        }
        is UiEvent.Empty -> {}
    }
}

@Composable
private fun SignUpContent(
    onSuccess:() -> Unit = {}
) {
    val viewModel: AuthViewModel = koinViewModel<AuthViewModel>()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val emailErrorText by remember {
        derivedStateOf {
            validateEmail(email)
        }
    }
    var passwordVisibility by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val passwordErrorText by remember {
        derivedStateOf {
            validatePassword(password)
        }
    }
    val isEnabled by remember { derivedStateOf {
        emailErrorText.isNullOrEmpty() && passwordErrorText.isNullOrEmpty()
    } }
    val event by viewModel.signUpUiEvent.collectAsState()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            supportingText = {
                Text(
                    text = emailErrorText ?: "",
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    color = MaterialTheme.colorScheme.error
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                Text(
                    text = passwordErrorText ?: "",
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    color = MaterialTheme.colorScheme.error
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisibility = !passwordVisibility }
                ) {
                    Icon(
                        if (passwordVisibility) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Visibility"
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            buttonText = "Sign Up",
            onClick = { viewModel.signUp(email, password, name) },
            enabled = isEnabled,
            isLoading = isLoading
        )
    }
    when(event){
        is UiEvent.Loading -> {
            isLoading = true
        }
        is UiEvent.Success -> {
            isLoading = false
            Toast.makeText(context,"Sign Up Successful,Please Sign In", Toast.LENGTH_SHORT).show()
            onSuccess()
        }
        is UiEvent.Failure -> {
            isLoading = false
            Toast.makeText(context,(event as UiEvent.Failure).error, Toast.LENGTH_SHORT).show()
        }
        is UiEvent.Empty -> {}
    }
}

@Composable
fun PrimaryButton(
    buttonText: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val containerColors = if (isLoading) {
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
        )
    } else {
        ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        )
    }
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .focusable(enabled = true)
            .then(
                if (enabled) {
                    Modifier.pointerHoverIcon(PointerIcon.Hand)
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(20.dp),
        colors = containerColors,
        enabled = enabled,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(35.dp)
            )
        } else {
            Text(
                text = buttonText,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
