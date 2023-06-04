package com.group.libraryapp.domain.book;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\t\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b\u00a8\u0006\f"}, d2 = {"Lcom/group/libraryapp/domain/book/BookType;", "", "score", "", "(Ljava/lang/String;II)V", "getScore", "()I", "COMPUTER", "ECONOMY", "SOCIETY", "LANGUAGE", "SCIENCE", "library-app"})
public enum BookType {
    /*public static final*/ COMPUTER /* = new COMPUTER(0) */,
    /*public static final*/ ECONOMY /* = new ECONOMY(0) */,
    /*public static final*/ SOCIETY /* = new SOCIETY(0) */,
    /*public static final*/ LANGUAGE /* = new LANGUAGE(0) */,
    /*public static final*/ SCIENCE /* = new SCIENCE(0) */;
    private final int score = 0;
    
    BookType(int score) {
    }
    
    public final int getScore() {
        return 0;
    }
}