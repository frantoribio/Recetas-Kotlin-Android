package es.frantoribio.recetas

import android.app.Application
import androidx.room.Room
import es.frantoribio.recetas.database.RecetaDatabase


class RecetasApplication : Application() {

    companion object{
        lateinit var database: RecetaDatabase
    }

    override fun onCreate() {
        super.onCreate()

        //contruimos la database
        database = Room.databaseBuilder(this,
            RecetaDatabase::class.java,
            "RecetaDatabase")
            .build()
    }
}