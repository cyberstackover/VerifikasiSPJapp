package com.zxing;

import com.zxing.common.BitMatrix;

import java.util.Map;
public interface Writer {

  /**
   * Encode a barcode using the default settings.
   *
   * @param contents The contents to encode in the barcode
   * @param format The barcode format to generate
   * @param width The preferred width in pixels
   * @param height The preferred height in pixels
   */
  BitMatrix encode(String contents, BarcodeFormat format, int width, int height)
      throws WriterException;

  /**
   *
   * @param contents The contents to encode in the barcode
   * @param format The barcode format to generate
   * @param width The preferred width in pixels
   * @param height The preferred height in pixels
   * @param hints Additional parameters to supply to the encoder
   */
  BitMatrix encode(String contents,
                   BarcodeFormat format,
                   int width,
                   int height,
                   Map<EncodeHintType,?> hints)
      throws WriterException;

}
