package data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val login: String,
    val password: String
)
