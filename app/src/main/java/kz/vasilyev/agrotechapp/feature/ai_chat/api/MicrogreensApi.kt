package kz.vasilyev.agrotechapp.feature.ai_chat.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MicrogreensApi {
    @Multipart
    @POST("predict")
    suspend fun analyzeImage(
        @Part image: MultipartBody.Part
    ): Response<AnalysisResponse>
}

data class AnalysisResponse(
    val healthy: Float,
    val unhealthy: Float,
    val image_base64: String
) 