package org.notes.multi.localdata.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "notes")
data class NotesEntity(
    @PrimaryKey(autoGenerate = true) val uId : Int = 0,
    val title : String,
    val text : String,
    val image : String,
    val document : String,
)
