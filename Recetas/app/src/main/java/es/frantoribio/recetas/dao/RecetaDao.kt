package es.frantoribio.recetas.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import es.frantoribio.recetas.model.Receta


@Dao
interface RecetaDao  {

    @Query("SELECT * FROM Receta")
    suspend fun getAllRecetas(): MutableList<Receta>

    @Insert
    suspend fun insertReceta(receta: Receta)

    @Query("DELETE FROM Receta WHERE id = :id")
    suspend fun deleteReceta(id: Int)

    @Update
    suspend fun updateReceta(receta: Receta)

    @Query("SELECT * FROM Receta WHERE iscompleated")
    suspend fun getCompleatedReceta(): MutableList<Receta>

    @Query("SELECT * FROM Receta WHERE NOT iscompleated")
    suspend fun getPendingReceta(): MutableList<Receta>

}