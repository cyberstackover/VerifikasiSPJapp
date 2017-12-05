package com.zxing.pdf417.detector;

import com.zxing.ResultPoint;
import com.zxing.common.BitMatrix;

import java.util.List;

public final class PDF417DetectorResult {

  private final BitMatrix bits;
  private final List<ResultPoint[]> points;

  public PDF417DetectorResult(BitMatrix bits, List<ResultPoint[]> points) {
    this.bits = bits;
    this.points = points;
  }

  public BitMatrix getBits() {
    return bits;
  }

  public List<ResultPoint[]> getPoints() {
    return points;
  }

}
