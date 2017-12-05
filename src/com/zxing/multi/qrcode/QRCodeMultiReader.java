package com.zxing.multi.qrcode;

import com.zxing.BarcodeFormat;
import com.zxing.BinaryBitmap;
import com.zxing.DecodeHintType;
import com.zxing.NotFoundException;
import com.zxing.ReaderException;
import com.zxing.Result;
import com.zxing.ResultMetadataType;
import com.zxing.ResultPoint;
import com.zxing.common.DecoderResult;
import com.zxing.common.DetectorResult;
import com.zxing.multi.MultipleBarcodeReader;
import com.zxing.multi.qrcode.detector.MultiDetector;
import com.zxing.qrcode.QRCodeReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This implementation can detect and decode multiple QR Codes in an image.
 *
 * @author Sean Owen
 * @author Hannes Erven
 */
public final class QRCodeMultiReader extends QRCodeReader implements MultipleBarcodeReader {

  private static final Result[] EMPTY_RESULT_ARRAY = new Result[0];

 // @Override
  public Result[] decodeMultiple(BinaryBitmap image) throws NotFoundException {
    return decodeMultiple(image, null);
  }

//  @Override
  public Result[] decodeMultiple(BinaryBitmap image, Map<DecodeHintType,?> hints) throws NotFoundException {
    List<Result> results = new ArrayList<Result>();
    DetectorResult[] detectorResults = new MultiDetector(image.getBlackMatrix()).detectMulti(hints);
    for (DetectorResult detectorResult : detectorResults) {
      try {
        DecoderResult decoderResult = getDecoder().decode(detectorResult.getBits(), hints);
        ResultPoint[] points = detectorResult.getPoints();
        Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), points,
                                   BarcodeFormat.QR_CODE);
        List<byte[]> byteSegments = decoderResult.getByteSegments();
        if (byteSegments != null) {
          result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments);
        }
        String ecLevel = decoderResult.getECLevel();
        if (ecLevel != null) {
          result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, ecLevel);
        }
        results.add(result);
      } catch (ReaderException re) {
        // ignore and continue 
      }
    }
    if (results.isEmpty()) {
      return EMPTY_RESULT_ARRAY;
    } else {
      return results.toArray(new Result[results.size()]);
    }
  }

}
