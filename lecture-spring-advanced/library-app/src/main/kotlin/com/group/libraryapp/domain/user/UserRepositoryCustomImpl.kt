package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.user.QUser.user
import com.group.libraryapp.domain.user.loanhistory.QUserLoanHistory.userLoanHistory
import com.querydsl.jpa.hibernate.HibernateQueryFactory
import com.querydsl.jpa.impl.JPAQueryFactory

class UserRepositoryCustomImpl(
        private val queryFactory: JPAQueryFactory
) : UserRepositoryCustom {
    override fun findAllWithHistories(): List<User> {
        return queryFactory.select(user).distinct()
                .from(user)
//                .leftJoin(user.userLoanHistories)
                .leftJoin(userLoanHistory).on(userLoanHistory.user.id.eq(user.id)).fetchJoin()
                .fetch()
    }
}