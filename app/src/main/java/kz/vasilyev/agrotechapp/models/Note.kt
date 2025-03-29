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
    val height: String,
    val light: String,
    val waterTemp: String,
    val humidity: String,
    val airTemp: String,
    val comment: String,
    val photo: String,
    val gardenId: Long
) {
    companion object {
        const val DEFAULT_ID = 0L
    }
}