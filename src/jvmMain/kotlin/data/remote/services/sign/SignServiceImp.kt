package data.remote.services.sign


/*
*   Класс авторизации с
*   приостанавливаемыми функциями авторизации и регистрации (сервер)
* */


import data.remote.HttpRoutes
import data.remote.dto.SignInRequest
import data.remote.dto.SignUpRequest
import data.remote.dto.SignResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

class SignServiceImp(
    private val client: HttpClient
) : SignService {

    override suspend fun getSignIn(signInRequest: SignInRequest): SignResponse? {
        return try {
            client.post<SignResponse> {
                url(HttpRoutes.SIGNIN)
                contentType(ContentType.Application.Json)
                body = signInRequest
            }
        } catch (e: RedirectResponseException) {
            println("Error: {${e.response.status.description}}")
            null
        } catch (e: ClientRequestException) {
            println("Error: {${e.response.status.description}}")
            null
        } catch (e: Exception) {
            println("Error: {${e.message}}")
            null
        }
    }

    override suspend fun getSignUp(signUpRequest: SignUpRequest): SignResponse? {
        return try {
            client.post<SignResponse> {
                url(HttpRoutes.SIGNUP)
                contentType(ContentType.Application.Json)
                body = signUpRequest
            }
        } catch (e: RedirectResponseException) {
            println("Error: {${e.response.status.description}}")
            null
        } catch (e: ClientRequestException) {
            println("Error: {${e.response.status.description}}")
            null
        } catch (e: Exception) {
            println("Error: {${e.message}}")
            null
        }
    }
}