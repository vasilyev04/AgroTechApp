package kz.vasilyev.agrotechapp.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kz.vasilyev.agrotechapp.models.Garden

@Database(entities = [Garden::class], version = 1, exportSchema = false)
abstract class RoomInstance : RoomDatabase() {

    abstract fun roomDao(): RoomDao

    companion object{
        private var INSTANCE: RoomInstance? = null
        private val LOCK = Any()
        private const val DB_NAME = "agro_tech.db"

        fun getInstance(application: Application): RoomInstance {
            INSTANCE?.let { return it }

            synchronized(LOCK) {
                INSTANCE?.let { return it }

                val db = Room.databaseBuilder(
                    application,
                    RoomInstance::class.java,
                    DB_NAME
                ).build()

                INSTANCE = db
                return db
            }
        }
    }
}