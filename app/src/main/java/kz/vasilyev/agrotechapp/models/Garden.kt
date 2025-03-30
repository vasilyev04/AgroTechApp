package kz.vasilyev.agrotechapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gardens")
data class Garden(
    @PrimaryKey(autoGenerate = true)
    val id: Long = DEFAULT_ID,
    val title: String,
    val substrate: String,
    val plantType: String,
    val plantDate: Long, // timestamp in milliseconds
    val harvestDate: Long, // timestamp in milliseconds
    val wateringTime: Long, // timestamp in milliseconds, only time part is used
    val photo: String
) {
    companion object {
        const val DEFAULT_ID = 0L
    }
}