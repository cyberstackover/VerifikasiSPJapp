package com.zxing;

public enum EncodeHintType {
  ERROR_CORRECTION,

  /**
   * Specifies what character encoding to use where applicable (type {@link String})
   */
  CHARACTER_SET,

  /**
   * Specifies the matrix shape for Data Matrix (type {@link com.google.zxing.datamatrix.encoder.SymbolShapeHint})
   */
  DATA_MATRIX_SHAPE,

  /**
   * Specifies a minimum barcode size (type {@link Dimension}). Only applicable to Data Matrix now.
   */
  MIN_SIZE,

  /**
   * Specifies a maximum barcode size (type {@link Dimension}). Only applicable to Data Matrix now.
   */
  MAX_SIZE,

  /**
   * Specifies margin, in pixels, to use when generating the barcode. The meaning can vary
   * by format; for example it controls margin before and after the barcode horizontally for
   * most 1D formats. (Type {@link Integer}).
   */
  MARGIN,

  /**
   * Specifies whether to use compact mode for PDF417 (type {@link Boolean}).
   */
  PDF417_COMPACT,

  /**
   * Specifies what compaction mode to use for PDF417 (type
   * {@link com.google.zxing.pdf417.encoder.Compaction Compaction}).
   */
  PDF417_COMPACTION,

  /**
   * Specifies the minimum and maximum number of rows and columns for PDF417 (type
   * {@link com.google.zxing.pdf417.encoder.Dimensions Dimensions}).
   */
  PDF417_DIMENSIONS,

}
