package org.notes.multi.localdata.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "documents",
    foreignKeys = [
        ForeignKey(
            entity = NotesEntity::class,
            parentColumns = ["uId"],
            childColumns = ["ownerUid"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ownerUid")],
)
data class DocumentsEntity(
    @PrimaryKey(autoGenerate = true)
    val uId: Long = 0,
    val documentName: String,
    val documentPath: String,
    val ownerUid: Long,
)
