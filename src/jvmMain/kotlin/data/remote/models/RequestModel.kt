package data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestModel(
    val firstName: String,
    val secondName: String,
    val lastName: String,
    val right: String,
    val expDrive: Int,
    val brand: String,
    val model: String,
    val dateStart: String,
    val dateEnd: String
)
