package com.group.libraryapp.domain.book;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\bJ\u0006\u0010\u0010\u001a\u00020\u0011R\u001a\u0010\u0006\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0013"}, d2 = {"Lcom/group/libraryapp/domain/book/Book;", "", "name", "", "type", "Lcom/group/libraryapp/domain/book/BookType;", "id", "", "(Ljava/lang/String;Lcom/group/libraryapp/domain/book/BookType;Ljava/lang/Long;)V", "getId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getName", "()Ljava/lang/String;", "getType", "()Lcom/group/libraryapp/domain/book/BookType;", "getEventScore", "", "Companion", "library-app"})
@javax.persistence.Entity()
public final class Book {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String name = null;
    @org.jetbrains.annotations.NotNull()
    @javax.persistence.Enumerated(value = javax.persistence.EnumType.STRING)
    private final com.group.libraryapp.domain.book.BookType type = null;
    @org.jetbrains.annotations.Nullable()
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Id()
    private final java.lang.Long id = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.group.libraryapp.domain.book.Book.Companion Companion = null;
    
    public Book(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    com.group.libraryapp.domain.book.BookType type, @org.jetbrains.annotations.Nullable()
    java.lang.Long id) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.group.libraryapp.domain.book.BookType getType() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getId() {
        return null;
    }
    
    public final int getEventScore() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J+\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\f"}, d2 = {"Lcom/group/libraryapp/domain/book/Book$Companion;", "", "()V", "fixture", "Lcom/group/libraryapp/domain/book/Book;", "name", "", "type", "Lcom/group/libraryapp/domain/book/BookType;", "id", "", "(Ljava/lang/String;Lcom/group/libraryapp/domain/book/BookType;Ljava/lang/Long;)Lcom/group/libraryapp/domain/book/Book;", "library-app"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.group.libraryapp.domain.book.Book fixture(@org.jetbrains.annotations.NotNull()
        java.lang.String name, @org.jetbrains.annotations.NotNull()
        com.group.libraryapp.domain.book.BookType type, @org.jetbrains.annotations.Nullable()
        java.lang.Long id) {
            return null;
        }
    }
}