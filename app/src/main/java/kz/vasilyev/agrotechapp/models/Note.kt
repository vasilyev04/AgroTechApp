package kz.vasilyev.agrotechapp.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = Garden::class,
            parentColumns = ["id"],
            childColumns = ["gardenId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = DEFAULT_ID,
    val height: Float,
    val light: Float,
    val waterTemp: Float,
    val humidity: Float,
    val airTemp: Float,
    val comment: String,
    val photo: String,
    val gardenId: Long,
    val createdAt: Long = System.currentTimeMillis() // Timestamp в миллисекундах
) {
    fun getFormattedDate(): String {
        val date = java.util.Date(createdAt)
        val format = java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale("ru"))
        return format.format(date)
    }

    companion object {
        const val DEFAULT_ID = 0L
    }
}