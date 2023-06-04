package com.group.libraryapp.repository.book;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/group/libraryapp/repository/book/BookQuerydslRepository;", "", "queryFactory", "Lcom/querydsl/jpa/impl/JPAQueryFactory;", "(Lcom/querydsl/jpa/impl/JPAQueryFactory;)V", "getStats", "", "Lcom/group/libraryapp/dto/book/response/BookStatResponse;", "library-app"})
@org.springframework.stereotype.Component()
public class BookQuerydslRepository {
    private final com.querydsl.jpa.impl.JPAQueryFactory queryFactory = null;
    
    public BookQuerydslRepository(@org.jetbrains.annotations.NotNull()
    com.querydsl.jpa.impl.JPAQueryFactory queryFactory) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public java.util.List<com.group.libraryapp.dto.book.response.BookStatResponse> getStats() {
        return null;
    }
}