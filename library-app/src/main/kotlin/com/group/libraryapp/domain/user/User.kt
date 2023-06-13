package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import javax.persistence.*

@Entity
class User (

        var name: String,

        val age: Int?,

        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
        val userLoanHistories: MutableList<UserLoanHistory> = mutableListOf(),

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
){

        init {
                if (name.isBlank()) {
                        throw IllegalArgumentException("이름은 비어 있을 수 없습니다.")
                }
        }

        fun updateName(name: String) {
                this.name = name
        }

        fun loanBook(book: Book) {
                this.userLoanHistories.add(UserLoanHistory(this, book.name, false))
        }

        fun returnBook(bookName: String) {
                this.userLoanHistories.first { history -> history.bookName == bookName}.doReturn()
        }
}