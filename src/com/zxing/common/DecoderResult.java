package com.zxing.common;

import java.util.List;

public final class DecoderResult {

  private final byte[] rawBytes;
  private final String text;
  private final List<byte[]> byteSegments;
  private final String ecLevel;
  private Integer errorsCorrected;
  private Integer erasures;
  private Object other;

  public DecoderResult(byte[] rawBytes,
                       String text,
                       List<byte[]> byteSegments,
                       String ecLevel) {
    this.rawBytes = rawBytes;
    this.text = text;
    this.byteSegments = byteSegments;
    this.ecLevel = ecLevel;
  }

  public byte[] getRawBytes() {
    return rawBytes;
  }

  public String getText() {
    return text;
  }

  public List<byte[]> getByteSegments() {
    return byteSegments;
  }

  public String getECLevel() {
    return ecLevel;
  }

  public Integer getErrorsCorrected() {
    return errorsCorrected;
  }

  public void setErrorsCorrected(Integer errorsCorrected) {
    this.errorsCorrected = errorsCorrected;
  }

  public Integer getErasures() {
    return erasures;
  }

  public void setErasures(Integer erasures) {
    this.erasures = erasures;
  }
  
  public Object getOther() {
    return other;
  }

  public void setOther(Object other) {
    this.other = other;
  }
  
}