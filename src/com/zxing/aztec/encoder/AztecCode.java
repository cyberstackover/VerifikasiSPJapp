package com.zxing.aztec.encoder;

import com.zxing.common.BitMatrix;

public final class AztecCode {

  private boolean compact;
  private int size;
  private int layers;
  private int codeWords;
  private BitMatrix matrix;

  /**
   * Compact or full symbol indicator
   */
  public boolean isCompact() {
    return compact;
  }

  public void setCompact(boolean compact) {
    this.compact = compact;
  }

  /**
   * Size in pixels (width and height)
   */
  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }
  
  /**
   * Number of levels
   */
  public int getLayers() {
    return layers;
  }
  
  public void setLayers(int layers) {
    this.layers = layers;
  }

  /**
   * Number of data codewords
   */
  public int getCodeWords() {
    return codeWords;
  }

  public void setCodeWords(int codeWords) {
    this.codeWords = codeWords;
  }

  /**
   * The symbol image
   */
  public BitMatrix getMatrix() {
    return matrix;
  }

  public void setMatrix(BitMatrix matrix) {
    this.matrix = matrix;
  }

}
