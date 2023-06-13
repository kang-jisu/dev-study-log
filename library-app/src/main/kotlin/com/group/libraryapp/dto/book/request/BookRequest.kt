package com.group.libraryapp.dto.book.request;

public class BookRequest {

  private String name;

  public BookRequest(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}
