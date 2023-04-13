package data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val login: String,
    val password: String,
    val firstName: String,
    val secondName: String,
    val lastName: String
)
