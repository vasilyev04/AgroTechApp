package kz.vasilyev.agrotechapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kz.vasilyev.agrotechapp.models.Garden
import kz.vasilyev.agrotechapp.models.Note

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addGarden(garden: Garden): Long

    @Query("SELECT * FROM gardens")
    fun getGardens(): Flow<List<Garden>>

    @Query("SELECT * FROM gardens WHERE id = :id")
    fun getGarden(id: Long): Garden

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote(note: Note): Long

    @Query("SELECT * FROM notes WHERE gardenId = :gardenId")
    fun getNotesForGarden(gardenId: Long): List<Note>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNote(id: Long): Note

    @Query("DELETE FROM notes WHERE id = :id")
    fun deleteNote(id: Long)
}