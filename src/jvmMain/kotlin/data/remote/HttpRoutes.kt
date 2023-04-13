package data.remote

object HttpRoutes {

    private const val BASE_URL = "http://127.0.0.1:8080/api"
    const val SIGNUP = "$BASE_URL/signup"
    const val SIGNIN = "$BASE_URL/signin"
    const val REPORT = "$BASE_URL/request/insert"
    const val GET_CONTRACT = "$BASE_URL/report/dogovor"
}