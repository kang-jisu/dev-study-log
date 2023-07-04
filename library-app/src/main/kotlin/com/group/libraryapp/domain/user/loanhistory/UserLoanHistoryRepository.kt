package com.group.libraryapp.domain.user.loanhistory

import org.springframework.data.jpa.repository.JpaRepository

interface UserLoanHistoryRepository : JpaRepository<UserLoanHistory, Long>{
//    fun findByBookName(bookName: String): UserLoanHistory?
//    fun findByBookNameAndStatus(bookName: String, userLoanStatus: UserLoanStatus): UserLoanHistory?
//    fun findAllByStatus(status: UserLoanStatus): List<UserLoanHistory>
//    fun countByStatus(status: UserLoanStatus): Long
}