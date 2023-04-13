package data.remote.services.contract

import data.remote.dto.ReportResponse
import data.remote.models.RequestModel
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface ReportService {

    suspend fun postReport(report: RequestModel): ReportResponse

    companion object {
        fun create(): ReportService {
            return ReportServiceImp(
                client = HttpClient(Android) {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer()
                    }
                }
            )
        }
    }
}