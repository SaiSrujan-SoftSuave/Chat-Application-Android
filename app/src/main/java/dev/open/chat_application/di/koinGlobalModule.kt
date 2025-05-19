package dev.open.chat_application.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dev.open.chat_application.core.Constants.DATASTORE_NAME
import dev.open.chat_application.core.SessionManager
import dev.open.chat_application.data.repository.AuthRepository
import dev.open.chat_application.data.repository.ChatRepository
import dev.open.chat_application.data.repositoryImpl.AuthRepositoryImpl
import dev.open.chat_application.data.repositoryImpl.ChatRepositoryImpl
import dev.open.chat_application.ui.screens.auth.AuthViewModel
import dev.open.chat_application.ui.screens.users.UserChatViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val koinGlobalModule = module {
    single { provideDataStore(androidContext()) }
    single { provideEngine() }

    single(named("unauthClient")) { provideUnauthenticatedHttpClient(get()) }
    single(named("authClient")) { provideAuthenticatedHttpClient(get(), get()) }

    single { SessionManager(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(named("unauthClient"))) }
    single<ChatRepository> { ChatRepositoryImpl(get(named("authClient"))) }

    viewModelOf(::AuthViewModel)
    viewModel { UserChatViewModel(get(),get()) }
}

fun provideDataStore(context: Context): DataStore<Preferences> =
    androidx.datastore.preferences.core.PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile(DATASTORE_NAME) }
    )

fun provideEngine(): HttpClientEngine = OkHttp.create()

fun provideUnauthenticatedHttpClient(engine: HttpClientEngine): HttpClient = HttpClient(engine) {
    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.ALL
    }
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            prettyPrint = true
        })
    }
}

fun provideAuthenticatedHttpClient(
    engine: HttpClientEngine,
    sessionManager: SessionManager
): HttpClient = HttpClient(engine) {
    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.ALL
    }

    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }

    // Automatically add Bearer token to requests
    install(Auth) {
        bearer {
            loadTokens {
                val token = runBlocking { sessionManager.accessToken.first() }
                BearerTokens(token, "")
            }
        }
    }

    install(HttpCallValidator) {
        validateResponse { response ->
            if (response.status.value == 401) {
                // Force logout on unauthorized
                runBlocking { sessionManager.clearSession() }
            }
        }
    }
}
