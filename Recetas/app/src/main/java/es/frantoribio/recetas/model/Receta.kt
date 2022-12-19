package es.frantoribio.recetas.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Receta")
data class Receta(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String,
    var producto: String,
    var categorias: String,
    var webUrl: String,
    var date: String,
    var iscompleated: Boolean = false
): Parcelable
