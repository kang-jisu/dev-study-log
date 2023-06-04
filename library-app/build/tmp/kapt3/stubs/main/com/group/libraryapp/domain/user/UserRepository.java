package com.group.libraryapp.domain.user;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u0004J\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0006\u001a\u00020\u0007H&\u00a8\u0006\b"}, d2 = {"Lcom/group/libraryapp/domain/user/UserRepository;", "Lorg/springframework/data/jpa/repository/JpaRepository;", "Lcom/group/libraryapp/domain/user/User;", "", "Lcom/group/libraryapp/domain/user/UserRepositoryCustom;", "findByName", "userName", "", "library-app"})
public abstract interface UserRepository extends org.springframework.data.jpa.repository.JpaRepository<com.group.libraryapp.domain.user.User, java.lang.Long>, com.group.libraryapp.domain.user.UserRepositoryCustom {
    
    @org.jetbrains.annotations.Nullable()
    public abstract com.group.libraryapp.domain.user.User findByName(@org.jetbrains.annotations.NotNull()
    java.lang.String userName);
}