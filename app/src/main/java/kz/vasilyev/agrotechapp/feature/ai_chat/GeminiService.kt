package kz.vasilyev.agrotechapp.feature.ai_chat

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiService {
    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AIzaSyARJ94Gy2Ldqk8ErzdEaIFIEtpc4jM-PvE" // Замените на ваш API ключ
    )

    private val systemContext = """
        Ты - специализированный ИИ-ассистент по выращиванию микрозелени. 
        Твоя основная задача - помогать пользователям в:
        - Выборе видов микрозелени для выращивания
        - Подборе оптимальных условий (свет, температура, влажность)
        - Решении проблем с ростом и развитием
        - Сборе и хранении урожая
        - Выборе субстратов и оборудования
        - Планировании посевов
        
        Отвечай профессионально, но дружелюбно. Используй научно обоснованные рекомендации.
        Если не уверен в чем-то, лучше признай это и предложи обратиться к специалисту.
        
        Всегда учитывай, что речь идет именно о микрозелени - молодых ростках растений, 
        которые собирают на стадии первых настоящих листьев.
    """.trimIndent()

    suspend fun makeRequest(prompt: String): String {
        val fullPrompt = "$systemContext\n\nВопрос пользователя: $prompt"

        val inputContent = content {
            text(fullPrompt)
        }

        return try {
            val response = model.generateContent(inputContent)
            response.text ?: "Извините, не удалось сгенерировать ответ."
        } catch (e: Exception) {
            "Произошла ошибка при получении ответа. Пожалуйста, попробуйте позже."
        }
    }
}