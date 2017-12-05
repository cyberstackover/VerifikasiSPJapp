package com.zxing;

public final class NotFoundException extends ReaderException {

  private static final NotFoundException instance = new NotFoundException();

  private NotFoundException() {
    // do nothing
  }

  public static NotFoundException getNotFoundInstance() {
    return instance;
  }

}