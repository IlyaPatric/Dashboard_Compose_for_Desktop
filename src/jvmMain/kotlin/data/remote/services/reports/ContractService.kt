package data.remote.services.reports

import data.remote.models.ContractModel
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface ContractService {

    suspend fun getContract(): ContractModel

    companion object {
        fun create(): ContractService {
            return ContractServiceImp(
                client = HttpClient(Android) {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer()
                    }
                }
            )
        }
    }
}