package com.zxing.oned.rss.expanded.decoders;

import com.zxing.NotFoundException;
import com.zxing.common.BitArray;

final class AnyAIDecoder extends AbstractExpandedDecoder {

  private static final int HEADER_SIZE = 2 + 1 + 2;

  AnyAIDecoder(BitArray information) {
    super(information);
  }

  @Override
  public String parseInformation() throws NotFoundException {
    StringBuilder buf = new StringBuilder();
    return this.getGeneralDecoder().decodeAllCodes(buf, HEADER_SIZE);
  }
}
