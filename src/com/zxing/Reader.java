package com.zxing;

import java.util.Map;

public interface Reader {

  /**
   * Locates and decodes a barcode in some format within an image.
   *
   * @param image image of barcode to decode
   * @return String which the barcode encodes
   * @throws NotFoundException if the barcode cannot be located or decoded for any reason
   */
  Result decode(BinaryBitmap image) throws NotFoundException, ChecksumException, FormatException;

  /**
   * Locates and decodes a barcode in some format within an image. This method also accepts
   * hints, each possibly associated to some data, which may help the implementation decode.
   *
   * @param image image of barcode to decode
   * @param hints passed as a {@link java.util.Map} from {@link com.google.zxing.DecodeHintType}
   * to arbitrary data. The
   * meaning of the data depends upon the hint type. The implementation may or may not do
   * anything with these hints.
   * @return String which the barcode encodes
   * @throws NotFoundException if the barcode cannot be located or decoded for any reason
   */
  Result decode(BinaryBitmap image, Map<DecodeHintType,?> hints)
      throws NotFoundException, ChecksumException, FormatException;

  /**
   * Resets any internal state the implementation has after a decode, to prepare it
   * for reuse.
   */
  void reset();

}