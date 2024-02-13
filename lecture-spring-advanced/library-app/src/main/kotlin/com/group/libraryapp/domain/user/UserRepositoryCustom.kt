package com.group.libraryapp.domain.user

interface UserRepositoryCustom {

    fun findAllWithHistories(): List<User>
}