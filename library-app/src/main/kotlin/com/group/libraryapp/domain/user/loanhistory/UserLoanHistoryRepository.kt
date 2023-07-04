package com.group.libraryapp.domain.user.loanhistory

import org.springframework.data.jpa.repository.JpaRepository

interface UserLoanHistoryRepository : JpaRepository<UserLoanHistory, Long>{
    fun findByBookNameAndStatus(bookName: String, userLoanStatus: UserLoanStatus): UserLoanHistory?
    fun findAllByStatus(status: UserLoanStatus): List<UserLoanHistory>
    fun countByStatus(status: UserLoanStatus): Long
}