package org.notes.multi.utilities

//File Path
const val imagePath = "images"
const val videoPath = "videos"
const val documentPath = "documents"

fun normalizePath(path: String) : String {
    return  path.replace("\\", "/")
}