package com.zxing.datamatrix.encoder;

interface Encoder {

  int getEncodingMode();

  void encode(EncoderContext context);

}
