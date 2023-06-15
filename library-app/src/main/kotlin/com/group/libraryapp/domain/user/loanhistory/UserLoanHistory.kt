package com.group.libraryapp.domain.user.loanhistory

import com.group.libraryapp.domain.user.User
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class UserLoanHistory(
        @ManyToOne
        val user: User,

        val bookName: String,

        var status: UserLoanStatus = UserLoanStatus.LOANED,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
) {
    fun doReturn() {
        this.status = UserLoanStatus.RETURNED
    }

    companion object {
        fun fixture(
                user: User,
                bookName: String = "이상한 나라의 엘리스",
                status: UserLoanStatus = UserLoanStatus.LOANED,
                id: Long? = null,
        ): UserLoanHistory {
            return UserLoanHistory(
                    user = user,
                    bookName = bookName,
                    status = status,
                    id = id,
            )
        }
    }
}