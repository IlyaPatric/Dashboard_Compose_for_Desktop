package data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class ContractModel(
    val fName: String,
    val sName: String,
    val lName: String,
    val right: String,
    val expDriving: String,
    val brand: String,
    val model: String,
    val clas: String,
    val dateStart: String,
    val dateEnd: String,
    val rentalPrice: String,
    val allPrice: String
)
