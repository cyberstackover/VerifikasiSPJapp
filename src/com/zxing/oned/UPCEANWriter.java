package com.zxing.oned;

public abstract class UPCEANWriter extends OneDimensionalCodeWriter {

  @Override
  public int getDefaultMargin() {
    // Use a different default more appropriate for UPC/EAN
    return UPCEANReader.START_END_PATTERN.length;
  }

}
