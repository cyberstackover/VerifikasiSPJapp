package com.zxing.client.result;

import java.util.HashMap;
import java.util.Map;

import com.zxing.BarcodeFormat;
import com.zxing.Result;

public final class ExpandedProductResultParser extends ResultParser {

  @Override
  public ExpandedProductParsedResult parse(Result result) {
    BarcodeFormat format = result.getBarcodeFormat();
    if (format != BarcodeFormat.RSS_EXPANDED) {
      // ExtendedProductParsedResult NOT created. Not a RSS Expanded barcode
      return null;
    }
    // Really neither of these should happen:
    String rawText = getMassagedText(result);
    if (rawText == null) {
      // ExtendedProductParsedResult NOT created. Input text is NULL
      return null;
    }

    String productID = null;
    String sscc = null;
    String lotNumber = null;
    String productionDate = null;
    String packagingDate = null;
    String bestBeforeDate = null;
    String expirationDate = null;
    String weight = null;
    String weightType = null;
    String weightIncrement = null;
    String price = null;
    String priceIncrement = null;
    String priceCurrency = null;
    Map<String,String> uncommonAIs = new HashMap<String,String>();

    int i = 0;

    while (i < rawText.length()) {
      String ai = findAIvalue(i, rawText);
      if (ai == null) {
        // Error. Code doesn't match with RSS expanded pattern
        // ExtendedProductParsedResult NOT created. Not match with RSS Expanded pattern
        return null;
      }
      i += ai.length() + 2;
      String value = findValue(i, rawText);
      i += value.length();

      if ("00".equals(ai)) {
        sscc = value;
      } else if ("01".equals(ai)) {
        productID = value;
      } else if ("10".equals(ai)) {
        lotNumber = value;
      } else if ("11".equals(ai)) {
        productionDate = value;
      } else if ("13".equals(ai)) {
        packagingDate = value;
      } else if ("15".equals(ai)) {
        bestBeforeDate = value;
      } else if ("17".equals(ai)) {
        expirationDate = value;
      } else if ("3100".equals(ai) || "3101".equals(ai)
          || "3102".equals(ai) || "3103".equals(ai)
          || "3104".equals(ai) || "3105".equals(ai)
          || "3106".equals(ai) || "3107".equals(ai)
          || "3108".equals(ai) || "3109".equals(ai)) {
        weight = value;
        weightType = ExpandedProductParsedResult.KILOGRAM;
        weightIncrement = ai.substring(3);
      } else if ("3200".equals(ai) || "3201".equals(ai)
          || "3202".equals(ai) || "3203".equals(ai)
          || "3204".equals(ai) || "3205".equals(ai)
          || "3206".equals(ai) || "3207".equals(ai)
          || "3208".equals(ai) || "3209".equals(ai)) {
        weight = value;
        weightType = ExpandedProductParsedResult.POUND;
        weightIncrement = ai.substring(3);
      } else if ("3920".equals(ai) || "3921".equals(ai)
          || "3922".equals(ai) || "3923".equals(ai)) {
        price = value;
        priceIncrement = ai.substring(3);
      } else if ("3930".equals(ai) || "3931".equals(ai)
          || "3932".equals(ai) || "3933".equals(ai)) {
        if (value.length() < 4) {
          // The value must have more of 3 symbols (3 for currency and
          // 1 at least for the price)
          // ExtendedProductParsedResult NOT created. Not match with RSS Expanded pattern
          return null;
        }
        price = value.substring(3);
        priceCurrency = value.substring(0, 3);
        priceIncrement = ai.substring(3);
      } else {
        // No match with common AIs
        uncommonAIs.put(ai, value);
      }
    }

    return new ExpandedProductParsedResult(rawText,
                                           productID,
                                           sscc,
                                           lotNumber,
                                           productionDate,
                                           packagingDate,
                                           bestBeforeDate,
                                           expirationDate,
                                           weight,
                                           weightType,
                                           weightIncrement,
                                           price,
                                           priceIncrement,
                                           priceCurrency,
                                           uncommonAIs);
  }

  private static String findAIvalue(int i, String rawText) {
    char c = rawText.charAt(i);
    // First character must be a open parenthesis.If not, ERROR
    if (c != '(') {
      return null;
    }

    String rawTextAux = rawText.substring(i + 1);

    StringBuilder buf = new StringBuilder();
    for (int index = 0; index < rawTextAux.length(); index++) {
      char currentChar = rawTextAux.charAt(index);
      if (currentChar == ')') {
        return buf.toString();
      } else if (currentChar >= '0' && currentChar <= '9') {
        buf.append(currentChar);
      } else {
        return null;
      }
    }
    return buf.toString();
  }

  private static String findValue(int i, String rawText) {
    StringBuilder buf = new StringBuilder();
    String rawTextAux = rawText.substring(i);

    for (int index = 0; index < rawTextAux.length(); index++) {
      char c = rawTextAux.charAt(index);
      if (c == '(') {
        // We look for a new AI. If it doesn't exist (ERROR), we coninue
        // with the iteration
        if (findAIvalue(index, rawTextAux) == null) {
          buf.append('(');
        } else {
          break;
        }
      } else {
        buf.append(c);
      }
    }
    return buf.toString();
  }
}
