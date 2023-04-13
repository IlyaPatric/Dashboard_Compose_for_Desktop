package data.remote.services.contract

import data.remote.HttpRoutes
import data.remote.dto.ReportResponse
import data.remote.models.RequestModel
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

class ReportServiceImp(
    private val client: HttpClient
): ReportService {

    override suspend fun postReport(report: RequestModel): ReportResponse {
        return try {
            client.post<ReportResponse> {
                url(HttpRoutes.REPORT)
                contentType(ContentType.Application.Json)
                body = report
            }
        } catch (e: ClientRequestException) {
            println("Error: {${e.response.status.description}}")
            ReportResponse(false)
        } catch (e: Exception) {
            println("Error-exception: {${e.message}}")
            ReportResponse(false)
        }
    }


}