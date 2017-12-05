package com.zxing.oned.rss.expanded.decoders;

import com.zxing.NotFoundException;
import com.zxing.common.BitArray;

abstract class AI013x0xDecoder extends AI01weightDecoder {

  private static final int HEADER_SIZE = 4 + 1;
  private static final int WEIGHT_SIZE = 15;

  AI013x0xDecoder(BitArray information) {
    super(information);
  }

  @Override
  public String parseInformation() throws NotFoundException {
    if (this.getInformation().getSize() != HEADER_SIZE + GTIN_SIZE + WEIGHT_SIZE) {
      throw NotFoundException.getNotFoundInstance();
    }

    StringBuilder buf = new StringBuilder();

    encodeCompressedGtin(buf, HEADER_SIZE);
    encodeCompressedWeight(buf, HEADER_SIZE + GTIN_SIZE, WEIGHT_SIZE);

    return buf.toString();
  }
}
