package org.notes.multi.localdata.database

import androidx.room.Embedded
import androidx.room.Relation

data class NotesRelation (

    @Embedded
    val noteEntity: NotesEntity,

    @Relation(
        parentColumn = "uId",
        entityColumn = "ownerUid"
    )
    val documentsList: List<DocumentsEntity>,

    @Relation(
        parentColumn = "uId",
        entityColumn = "ownerUid"
    )
    val image: ImageEntity?

)