package com.zxing.client.result;

import com.zxing.Result;

public final class SMSTOMMSTOResultParser extends ResultParser {

  @Override
  public SMSParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    if (!(rawText.startsWith("smsto:") || rawText.startsWith("SMSTO:") ||
          rawText.startsWith("mmsto:") || rawText.startsWith("MMSTO:"))) {
      return null;
    }
    // Thanks to dominik.wild for suggesting this enhancement to support
    // smsto:number:body URIs
    String number = rawText.substring(6);
    String body = null;
    int bodyStart = number.indexOf(':');
    if (bodyStart >= 0) {
      body = number.substring(bodyStart + 1);
      number = number.substring(0, bodyStart);
    }
    return new SMSParsedResult(number, null, null, body);
  }

}