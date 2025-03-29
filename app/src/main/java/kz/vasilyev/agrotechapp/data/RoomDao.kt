package kz.vasilyev.agrotechapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kz.vasilyev.agrotechapp.models.Garden

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addGarden(garden: Garden): Long

    @Query("SELECT * FROM gardens")
    fun getGardens(): Flow<List<Garden>>
}