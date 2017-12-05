package com.zxing.pdf417.encoder;

public final class BarcodeMatrix {

  private final BarcodeRow[] matrix;
  private int currentRow;
  private final int height;
  private final int width;

  BarcodeMatrix(int height, int width) {
    matrix = new BarcodeRow[height + 2];
    //Initializes the array to the correct width
    for (int i = 0, matrixLength = matrix.length; i < matrixLength; i++) {
      matrix[i] = new BarcodeRow((width + 4) * 17 + 1);
    }
    this.width = width * 17;
    this.height = height + 2;
    this.currentRow = 0;
  }

  void set(int x, int y, byte value) {
    matrix[y].set(x, value);
  }

  void setMatrix(int x, int y, boolean black) {
    set(x, y, (byte) (black ? 1 : 0));
  }

  void startRow() {
    ++currentRow;
  }

  BarcodeRow getCurrentRow() {
    return matrix[currentRow];
  }

  public byte[][] getMatrix() {
    return getScaledMatrix(1, 1);
  }

  public byte[][] getScaledMatrix(int scale) {
    return getScaledMatrix(scale, scale);
  }

  public byte[][] getScaledMatrix(int xScale, int yScale) {
    byte[][] matrixOut = new byte[height * yScale][width * xScale];
    int yMax = height * yScale;
    for (int i = 0; i < yMax; i++) {
      matrixOut[yMax - i - 1] = matrix[i / yScale].getScaledRow(xScale);
    }
    return matrixOut;
  }
}
