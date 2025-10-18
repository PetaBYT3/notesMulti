package org.notes.multi.localdata.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "images",
    foreignKeys = [
        ForeignKey(
            entity = NotesEntity::class,
            parentColumns = ["uId"],
            childColumns = ["ownerUid"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["ownerUid"])]
)
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val uId: Long = 0,
    val imageName: String,
    val imagePath: String,

    val ownerUid: Long,
)
