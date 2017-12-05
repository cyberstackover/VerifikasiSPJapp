package com.zxing;

import com.zxing.aztec.AztecWriter;
import com.zxing.common.BitMatrix;
import com.zxing.datamatrix.DataMatrixWriter;
import com.zxing.oned.CodaBarWriter;
import com.zxing.oned.Code128Writer;
import com.zxing.oned.Code39Writer;
import com.zxing.oned.EAN13Writer;
import com.zxing.oned.EAN8Writer;
import com.zxing.oned.ITFWriter;
import com.zxing.oned.UPCAWriter;
import com.zxing.pdf417.PDF417Writer;
import com.zxing.qrcode.QRCodeWriter;

import java.util.Map;

public final class MultiFormatWriter implements Writer {

 // @Override
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          int width,
                          int height) throws WriterException {
    return encode(contents, format, width, height, null);
  }

 // @Override
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          int width, int height,
                          Map<EncodeHintType,?> hints) throws WriterException {

    Writer writer;
    switch (format) {
      case EAN_8:
        writer = new EAN8Writer();
        break;
      case EAN_13:
        writer = new EAN13Writer();
        break;
      case UPC_A:
        writer = new UPCAWriter();
        break;
      case QR_CODE:
        writer = new QRCodeWriter();
        break;
      case CODE_39:
        writer = new Code39Writer();
        break;
      case CODE_128:
        writer = new Code128Writer();
        break;
      case ITF:
        writer = new ITFWriter();
        break;
      case PDF_417:
        writer = new PDF417Writer();
        break;
      case CODABAR:
        writer = new CodaBarWriter();
        break;
      case DATA_MATRIX:
        writer = new DataMatrixWriter();
        break;
      case AZTEC:
        writer = new AztecWriter();
        break;
      default:
        throw new IllegalArgumentException("No encoder available for format " + format);
    }
    return writer.encode(contents, format, width, height, hints);
  }

}
