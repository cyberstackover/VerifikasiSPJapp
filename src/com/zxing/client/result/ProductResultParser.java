package com.zxing.client.result;

import com.zxing.BarcodeFormat;
import com.zxing.Result;
import com.zxing.oned.UPCEReader;

public final class ProductResultParser extends ResultParser {

  // Treat all UPC and EAN variants as UPCs, in the sense that they are all product barcodes.
  @Override
  public ProductParsedResult parse(Result result) {
    BarcodeFormat format = result.getBarcodeFormat();
    if (!(format == BarcodeFormat.UPC_A || format == BarcodeFormat.UPC_E ||
          format == BarcodeFormat.EAN_8 || format == BarcodeFormat.EAN_13)) {
      return null;
    }
    String rawText = getMassagedText(result);
    int length = rawText.length();
    for (int x = 0; x < length; x++) {
      char c = rawText.charAt(x);
      if (c < '0' || c > '9') {
        return null;
      }
    }
    // Not actually checking the checksum again here    

    String normalizedProductID;
    // Expand UPC-E for purposes of searching
    if (format == BarcodeFormat.UPC_E) {
      normalizedProductID = UPCEReader.convertUPCEtoUPCA(rawText);
    } else {
      normalizedProductID = rawText;
    }

    return new ProductParsedResult(rawText, normalizedProductID);
  }

}