package com.group.libraryapp.dto.user.response;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\b\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\n\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000b\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\r\u001a\u00020\u00052\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0004\u001a\u00020\u00058\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\u0013"}, d2 = {"Lcom/group/libraryapp/dto/user/response/BookHistoryResponse;", "", "name", "", "isReturn", "", "(Ljava/lang/String;Z)V", "()Z", "getName", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "other", "hashCode", "", "toString", "Companion", "library-app"})
public final class BookHistoryResponse {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String name = null;
    private final boolean isReturn = false;
    @org.jetbrains.annotations.NotNull()
    public static final com.group.libraryapp.dto.user.response.BookHistoryResponse.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    public final com.group.libraryapp.dto.user.response.BookHistoryResponse copy(@org.jetbrains.annotations.NotNull()
    java.lang.String name, boolean isReturn) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String toString() {
        return null;
    }
    
    public BookHistoryResponse(@org.jetbrains.annotations.NotNull()
    java.lang.String name, boolean isReturn) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getName() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    @com.fasterxml.jackson.annotation.JsonProperty(value = "isReturn")
    public final boolean isReturn() {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/group/libraryapp/dto/user/response/BookHistoryResponse$Companion;", "", "()V", "of", "Lcom/group/libraryapp/dto/user/response/BookHistoryResponse;", "userLoanHistory", "Lcom/group/libraryapp/domain/user/loanhistory/UserLoanHistory;", "library-app"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.group.libraryapp.dto.user.response.BookHistoryResponse of(@org.jetbrains.annotations.NotNull()
        com.group.libraryapp.domain.user.loanhistory.UserLoanHistory userLoanHistory) {
            return null;
        }
    }
}