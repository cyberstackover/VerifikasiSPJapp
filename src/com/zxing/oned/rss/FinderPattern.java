package com.zxing.oned.rss;

import com.zxing.ResultPoint;

public final class FinderPattern {

  private final int value;
  private final int[] startEnd;
  private final ResultPoint[] resultPoints;

  public FinderPattern(int value, int[] startEnd, int start, int end, int rowNumber) {
    this.value = value;
    this.startEnd = startEnd;
    this.resultPoints = new ResultPoint[] {
        new ResultPoint((float) start, (float) rowNumber),
        new ResultPoint((float) end, (float) rowNumber),
    };
  }

  public int getValue() {
    return value;
  }

  public int[] getStartEnd() {
    return startEnd;
  }

  public ResultPoint[] getResultPoints() {
    return resultPoints;
  }

  @Override
  public boolean equals(Object o) {
    if(!(o instanceof FinderPattern)) {
      return false;
    }
    FinderPattern that = (FinderPattern) o;
    return value == that.value;
  }

  @Override
  public int hashCode() {
    return value;
  }

}
