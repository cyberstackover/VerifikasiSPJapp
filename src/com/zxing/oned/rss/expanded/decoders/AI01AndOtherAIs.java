package com.zxing.oned.rss.expanded.decoders;

import com.zxing.NotFoundException;
import com.zxing.common.BitArray;

final class AI01AndOtherAIs extends AI01decoder {

  private static final int HEADER_SIZE = 1 + 1 + 2; //first bit encodes the linkage flag,
                          //the second one is the encodation method, and the other two are for the variable length
  AI01AndOtherAIs(BitArray information) {
    super(information);
  }

  @Override
  public String parseInformation() throws NotFoundException {
    StringBuilder buff = new StringBuilder();

    buff.append("(01)");
    int initialGtinPosition = buff.length();
    int firstGtinDigit = this.getGeneralDecoder().extractNumericValueFromBitArray(HEADER_SIZE, 4);
    buff.append(firstGtinDigit);

    this.encodeCompressedGtinWithoutAI(buff, HEADER_SIZE + 4, initialGtinPosition);

    return this.getGeneralDecoder().decodeAllCodes(buff, HEADER_SIZE + 44);
  }
}
