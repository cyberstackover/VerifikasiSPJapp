package com.zxing.aztec;

import java.nio.charset.Charset;
import java.util.Map;

import android.annotation.SuppressLint;
import com.zxing.BarcodeFormat;
import com.zxing.EncodeHintType;
import com.zxing.Writer;
import com.zxing.aztec.encoder.AztecCode;
import com.zxing.aztec.encoder.Encoder;
import com.zxing.common.BitMatrix;

@SuppressLint("NewApi")
public final class AztecWriter implements Writer {
  
  private static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");

 // @Override
  public BitMatrix encode(String contents, BarcodeFormat format, int width, int height) {
    return encode(contents, format, DEFAULT_CHARSET, Encoder.DEFAULT_EC_PERCENT);
  }

 // @Override
  public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map<EncodeHintType,?> hints) {
    String charset = (String) hints.get(EncodeHintType.CHARACTER_SET);
    Number eccPercent = (Number) hints.get(EncodeHintType.ERROR_CORRECTION);
    return encode(contents, 
                  format, 
                  charset == null ? DEFAULT_CHARSET : Charset.forName(charset),
                  eccPercent == null ? Encoder.DEFAULT_EC_PERCENT : eccPercent.intValue());
  }

  private static BitMatrix encode(String contents, BarcodeFormat format, Charset charset, int eccPercent) {
    if (format != BarcodeFormat.AZTEC) {
      throw new IllegalArgumentException("Can only encode AZTEC, but got " + format);
    }
    AztecCode aztec = Encoder.encode(contents.getBytes(charset), eccPercent);
    return aztec.getMatrix();
  }

}
