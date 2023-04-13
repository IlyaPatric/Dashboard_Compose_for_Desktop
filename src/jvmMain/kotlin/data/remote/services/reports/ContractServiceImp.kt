package data.remote.services.reports

import data.remote.HttpRoutes
import data.remote.models.ContractModel
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class ContractServiceImp(
    private val client: HttpClient
) : ContractService {

    override suspend fun getContract(): ContractModel {
        return client.get<ContractModel>{
            url(HttpRoutes.GET_CONTRACT)
            contentType(ContentType.Application.Json)
        }
    }
}