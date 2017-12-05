package com.zxing.multi;

import com.zxing.BinaryBitmap;
import com.zxing.DecodeHintType;
import com.zxing.NotFoundException;
import com.zxing.Result;

import java.util.Map;

public interface MultipleBarcodeReader {

  Result[] decodeMultiple(BinaryBitmap image) throws NotFoundException;

  Result[] decodeMultiple(BinaryBitmap image,
                          Map<DecodeHintType,?> hints) throws NotFoundException;

}
