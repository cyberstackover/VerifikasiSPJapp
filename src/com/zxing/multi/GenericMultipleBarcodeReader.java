package com.zxing.multi;

import com.zxing.BinaryBitmap;
import com.zxing.DecodeHintType;
import com.zxing.NotFoundException;
import com.zxing.Reader;
import com.zxing.ReaderException;
import com.zxing.Result;
import com.zxing.ResultPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class GenericMultipleBarcodeReader implements MultipleBarcodeReader {

  private static final int MIN_DIMENSION_TO_RECUR = 100;
  private static final int MAX_DEPTH = 4;

  private final Reader delegate;

  public GenericMultipleBarcodeReader(Reader delegate) {
    this.delegate = delegate;
  }

//  @Override
  public Result[] decodeMultiple(BinaryBitmap image) throws NotFoundException {
    return decodeMultiple(image, null);
  }

//  @Override
  public Result[] decodeMultiple(BinaryBitmap image, Map<DecodeHintType,?> hints)
      throws NotFoundException {
    List<Result> results = new ArrayList<Result>();
    doDecodeMultiple(image, hints, results, 0, 0, 0);
    if (results.isEmpty()) {
      throw NotFoundException.getNotFoundInstance();
    }
    return results.toArray(new Result[results.size()]);
  }

  private void doDecodeMultiple(BinaryBitmap image,
                                Map<DecodeHintType,?> hints,
                                List<Result> results,
                                int xOffset,
                                int yOffset,
                                int currentDepth) {
    if (currentDepth > MAX_DEPTH) {
      return;
    }
    
    Result result;
    try {
      result = delegate.decode(image, hints);
    } catch (ReaderException ignored) {
      return;
    }
    boolean alreadyFound = false;
    for (Result existingResult : results) {
      if (existingResult.getText().equals(result.getText())) {
        alreadyFound = true;
        break;
      }
    }
    if (!alreadyFound) {
      results.add(translateResultPoints(result, xOffset, yOffset));
    }
    ResultPoint[] resultPoints = result.getResultPoints();
    if (resultPoints == null || resultPoints.length == 0) {
      return;
    }
    int width = image.getWidth();
    int height = image.getHeight();
    float minX = width;
    float minY = height;
    float maxX = 0.0f;
    float maxY = 0.0f;
    for (ResultPoint point : resultPoints) {
      float x = point.getX();
      float y = point.getY();
      if (x < minX) {
        minX = x;
      }
      if (y < minY) {
        minY = y;
      }
      if (x > maxX) {
        maxX = x;
      }
      if (y > maxY) {
        maxY = y;
      }
    }

    // Decode left of barcode
    if (minX > MIN_DIMENSION_TO_RECUR) {
      doDecodeMultiple(image.crop(0, 0, (int) minX, height),
                       hints, results, 
                       xOffset, yOffset, 
                       currentDepth + 1);
    }
    // Decode above barcode
    if (minY > MIN_DIMENSION_TO_RECUR) {
      doDecodeMultiple(image.crop(0, 0, width, (int) minY),
                       hints, results, 
                       xOffset, yOffset, 
                       currentDepth + 1);
    }
    // Decode right of barcode
    if (maxX < width - MIN_DIMENSION_TO_RECUR) {
      doDecodeMultiple(image.crop((int) maxX, 0, width - (int) maxX, height),
                       hints, results, 
                       xOffset + (int) maxX, yOffset, 
                       currentDepth + 1);
    }
    // Decode below barcode
    if (maxY < height - MIN_DIMENSION_TO_RECUR) {
      doDecodeMultiple(image.crop(0, (int) maxY, width, height - (int) maxY),
                       hints, results, 
                       xOffset, yOffset + (int) maxY, 
                       currentDepth + 1);
    }
  }

  private static Result translateResultPoints(Result result, int xOffset, int yOffset) {
    ResultPoint[] oldResultPoints = result.getResultPoints();
    if (oldResultPoints == null) {
      return result;
    }
    ResultPoint[] newResultPoints = new ResultPoint[oldResultPoints.length];
    for (int i = 0; i < oldResultPoints.length; i++) {
      ResultPoint oldPoint = oldResultPoints[i];
      newResultPoints[i] = new ResultPoint(oldPoint.getX() + xOffset, oldPoint.getY() + yOffset);
    }
    Result newResult = new Result(result.getText(), result.getRawBytes(), newResultPoints, result.getBarcodeFormat());
    newResult.putAllMetadata(result.getResultMetadata());
    return newResult;
  }

}
