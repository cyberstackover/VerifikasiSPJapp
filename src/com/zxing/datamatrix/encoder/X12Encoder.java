package com.zxing.datamatrix.encoder;
final class X12Encoder extends C40Encoder {

  @Override
  public int getEncodingMode() {
    return HighLevelEncoder.X12_ENCODATION;
  }

  @Override
  public void encode(EncoderContext context) {
    //step C
    StringBuilder buffer = new StringBuilder();
    while (context.hasMoreCharacters()) {
      char c = context.getCurrentChar();
      context.pos++;

      encodeChar(c, buffer);

      int count = buffer.length();
      if ((count % 3) == 0) {
        writeNextTriplet(context, buffer);

        int newMode = HighLevelEncoder.lookAheadTest(context.msg, context.pos, getEncodingMode());
        if (newMode != getEncodingMode()) {
          context.signalEncoderChange(newMode);
          break;
        }
      }
    }
    handleEOD(context, buffer);
  }

  @Override
  int encodeChar(char c, StringBuilder sb) {
    if (c == '\r') {
      sb.append('\0');
    } else if (c == '*') {
      sb.append('\1');
    } else if (c == '>') {
      sb.append('\2');
    } else if (c == ' ') {
      sb.append('\3');
    } else if (c >= '0' && c <= '9') {
      sb.append((char) (c - 48 + 4));
    } else if (c >= 'A' && c <= 'Z') {
      sb.append((char) (c - 65 + 14));
    } else {
      HighLevelEncoder.illegalCharacter(c);
    }
    return 1;
  }

  @Override
  void handleEOD(EncoderContext context, StringBuilder buffer) {
    context.updateSymbolInfo();
    int available = context.symbolInfo.dataCapacity - context.getCodewordCount();
    int count = buffer.length();
    if (count == 2) {
      context.writeCodeword(HighLevelEncoder.X12_UNLATCH);
      context.pos -= 2;
      context.signalEncoderChange(HighLevelEncoder.ASCII_ENCODATION);
    } else if (count == 1) {
      context.pos--;
      if (available > 1) {
        context.writeCodeword(HighLevelEncoder.X12_UNLATCH);
      }
      //NOP - No unlatch necessary
      context.signalEncoderChange(HighLevelEncoder.ASCII_ENCODATION);
    }
  }
}
