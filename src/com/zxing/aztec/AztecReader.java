package com.zxing.aztec;

import com.zxing.BarcodeFormat;
import com.zxing.BinaryBitmap;
import com.zxing.DecodeHintType;
import com.zxing.FormatException;
import com.zxing.NotFoundException;
import com.zxing.Reader;
import com.zxing.Result;
import com.zxing.ResultMetadataType;
import com.zxing.ResultPoint;
import com.zxing.ResultPointCallback;
import com.zxing.common.DecoderResult;
import com.zxing.aztec.decoder.Decoder;
import com.zxing.aztec.detector.Detector;

import java.util.List;
import java.util.Map;

public final class AztecReader implements Reader {

  /**
   * Locates and decodes a Data Matrix code in an image.
   *
   * @return a String representing the content encoded by the Data Matrix code
   * @throws NotFoundException if a Data Matrix code cannot be found
   * @throws FormatException if a Data Matrix code cannot be decoded
   * @throws com.google.zxing.ChecksumException if error correction fails
   */
//  @Override
  public Result decode(BinaryBitmap image) throws NotFoundException, FormatException {
    return decode(image, null);
  }

//  @Override
  public Result decode(BinaryBitmap image, Map<DecodeHintType,?> hints)
      throws NotFoundException, FormatException {

    AztecDetectorResult detectorResult = new Detector(image.getBlackMatrix()).detect();
    ResultPoint[] points = detectorResult.getPoints();

    if (hints != null) {
      ResultPointCallback rpcb = (ResultPointCallback) hints.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
      if (rpcb != null) {
        for (ResultPoint point : points) {
          rpcb.foundPossibleResultPoint(point);
        }
      }
    }

    DecoderResult decoderResult = new Decoder().decode(detectorResult);

    Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), points, BarcodeFormat.AZTEC);
    
    List<byte[]> byteSegments = decoderResult.getByteSegments();
    if (byteSegments != null) {
      result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments);
    }
    String ecLevel = decoderResult.getECLevel();
    if (ecLevel != null) {
      result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, ecLevel);
    }
    
    return result;
  }

 // @Override
  public void reset() {
    // do nothing
  }

}