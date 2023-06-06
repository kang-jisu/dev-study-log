package com.group.libraryapp.dto.user.request;

public class UserUpdateRequest {

  private long id;
  private String name;

  public UserUpdateRequest(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

}
