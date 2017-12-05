package com.zxing.oned;

import com.zxing.BarcodeFormat;
import com.zxing.EncodeHintType;
import com.zxing.Writer;
import com.zxing.WriterException;
import com.zxing.common.BitMatrix;

import java.util.Map;

public abstract class OneDimensionalCodeWriter implements Writer {

//  @Override
  public final BitMatrix encode(String contents, BarcodeFormat format, int width, int height)
      throws WriterException {
    return encode(contents, format, width, height, null);
  }

  /**
   * Encode the contents following specified format.
   * {@code width} and {@code height} are required size. This method may return bigger size
   * {@code BitMatrix} when specified size is too small. The user can set both {@code width} and
   * {@code height} to zero to get minimum size barcode. If negative value is set to {@code width}
   * or {@code height}, {@code IllegalArgumentException} is thrown.
   */
 // @Override
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          int width,
                          int height,
                          Map<EncodeHintType,?> hints) throws WriterException {
    if (contents.length() == 0) {
      throw new IllegalArgumentException("Found empty contents");
    }

    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("Negative size is not allowed. Input: "
                                             + width + 'x' + height);
    }

    int sidesMargin = getDefaultMargin();
    if (hints != null) {
      Integer sidesMarginInt = (Integer) hints.get(EncodeHintType.MARGIN);
      if (sidesMarginInt != null) {
        sidesMargin = sidesMarginInt;
      }
    }

    boolean[] code = encode(contents);
    return renderResult(code, width, height, sidesMargin);
  }

  /**
   * @return a byte array of horizontal pixels (0 = white, 1 = black)
   */
  private static BitMatrix renderResult(boolean[] code, int width, int height, int sidesMargin) {
    int inputWidth = code.length;
    // Add quiet zone on both sides.
    int fullWidth = inputWidth + sidesMargin;
    int outputWidth = Math.max(width, fullWidth);
    int outputHeight = Math.max(1, height);

    int multiple = outputWidth / fullWidth;
    int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;

    BitMatrix output = new BitMatrix(outputWidth, outputHeight);
    for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
      if (code[inputX]) {
        output.setRegion(outputX, 0, multiple, outputHeight);
      }
    }
    return output;
  }


  /**
   * Appends the given pattern to the target array starting at pos.
   *
   * @param startColor starting color - false for white, true for black
   * @return the number of elements added to target.
   */
  protected static int appendPattern(boolean[] target, int pos, int[] pattern, boolean startColor) {
    boolean color = startColor;
    int numAdded = 0;
    for (int len : pattern) {
      for (int j = 0; j < len; j++) {
        target[pos++] = color;
      }
      numAdded += len;
      color = !color; // flip color after each segment
    }
    return numAdded;
  }

  public int getDefaultMargin() {
    // CodaBar spec requires a side margin to be more than ten times wider than narrow space.
    // This seems like a decent idea for a default for all formats.
    return 10;
  }

  /**
   * Encode the contents to boolean array expression of one-dimensional barcode.
   * Start code and end code should be included in result, and side margins should not be included.
   *
   * @return a {@code boolean[]} of horizontal pixels (false = white, true = black)
   */
  public abstract boolean[] encode(String contents);
}

