package com.group.libraryapp.repository.user.loanHistory

import com.group.libraryapp.domain.user.loanhistory.QUserLoanHistory.userLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class UserLoanHistoryQuerydslRepository(
        private val queryFactory: JPAQueryFactory
) {

    fun find(bookName: String, status: UserLoanStatus? = null): UserLoanHistory? {
        return queryFactory.select(userLoanHistory)
                .from(userLoanHistory)
                .where(
                        userLoanHistory.bookName.eq(bookName),
                        status?.let { userLoanHistory.status.eq(status) }
                )
                .limit(1) // 한개만 가져온다
                .fetchOne() // fetchOne을 써서 하나만 리턴하도록 함
    }

    fun count(status: UserLoanStatus): Long {
        return queryFactory.select(userLoanHistory.id.count())
                .from(userLoanHistory)
                .where(userLoanHistory.status.eq(status))
                .fetchOne() ?: 0L
    }
}