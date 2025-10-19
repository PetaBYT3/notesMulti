package org.notes.multi.utilities

//File Path
const val imagePath = "images"
const val videoPath = "videos"
const val documentPath = "documents"
const val audioPath = "audios"
const val temp = "temp"

fun normalizePath(path: String) : String {
    return  path.replace("\\", "/")
}