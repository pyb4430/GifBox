package com.example.taylor.gifbox

import java.io.IOException

fun readJsonFile(testClass: Any, filename: String): String {
    val stream = testClass.javaClass.classLoader.getResource(filename).openStream()
    var jsonString: String? = null
    stream.bufferedReader().use {
        jsonString = it.readText()
    }
    if (jsonString == null) {
        throw IOException("Error reading")
    }
    return jsonString!!
}