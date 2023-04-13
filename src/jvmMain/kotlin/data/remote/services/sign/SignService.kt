package data.remote.services.sign

import data.remote.dto.SignInRequest
import data.remote.dto.SignUpRequest
import data.remote.dto.SignResponse
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface SignService {

    suspend fun getSignUp(signUpRequest: SignUpRequest): SignResponse?
    suspend fun getSignIn(signInRequest: SignInRequest): SignResponse?

    companion object {
        fun create(): SignService {
            return SignServiceImp(
                client = HttpClient(Android) {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer()
                    }
                }
            )
        }
    }
}