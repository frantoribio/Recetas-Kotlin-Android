package es.frantoribio.recetas.database

import androidx.room.Database
import androidx.room.RoomDatabase
import es.frantoribio.recetas.dao.RecetaDao
import es.frantoribio.recetas.model.Receta

@Database(entities = [Receta::class], version = 1)
abstract class RecetaDatabase : RoomDatabase(){

    abstract  fun recetaDao() : RecetaDao
}