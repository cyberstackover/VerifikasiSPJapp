package com.zxing.client.result;

import com.zxing.Result;

public final class WifiResultParser extends ResultParser {

  @Override
  public WifiParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    if (!rawText.startsWith("WIFI:")) {
      return null;
    }
    String ssid = matchSinglePrefixedField("S:", rawText, ';', false);
    if (ssid == null || ssid.length() == 0) {
      return null;
    }
    String pass = matchSinglePrefixedField("P:", rawText, ';', false);
    String type = matchSinglePrefixedField("T:", rawText, ';', false);
    if (type == null) {
      type = "nopass";
    }
    boolean hidden = Boolean.parseBoolean(matchSinglePrefixedField("H:", rawText, ';', false));
    return new WifiParsedResult(type, ssid, pass, hidden);
  }
}