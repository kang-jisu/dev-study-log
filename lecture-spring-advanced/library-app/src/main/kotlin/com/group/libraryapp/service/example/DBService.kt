package com.group.libraryapp.service.example

class DBService {
    fun get(id: String): Data? {
        return null
    }
    fun save(data: Data) {

    }
    fun modify(data: Data) {

    }
}

data class Data(
        val id: String,
        val content: String
)