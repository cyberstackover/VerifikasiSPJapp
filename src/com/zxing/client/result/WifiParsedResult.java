package com.zxing.client.result;

public final class WifiParsedResult extends ParsedResult {

  private final String ssid;
  private final String networkEncryption;
  private final String password;
  private final boolean hidden;

  public WifiParsedResult(String networkEncryption, String ssid, String password) {
    this(networkEncryption, ssid, password, false);
  }

  public WifiParsedResult(String networkEncryption, String ssid, String password, boolean hidden) {
    super(ParsedResultType.WIFI);
    this.ssid = ssid;
    this.networkEncryption = networkEncryption;
    this.password = password;
    this.hidden = hidden;
  }

  public String getSsid() {
    return ssid;
  }

  public String getNetworkEncryption() {
    return networkEncryption;
  }

  public String getPassword() {
    return password;
  }

  public boolean isHidden() {
    return hidden;
  }

  @Override
  public String getDisplayResult() {
    StringBuilder result = new StringBuilder(80);
    maybeAppend(ssid, result);
    maybeAppend(networkEncryption, result);
    maybeAppend(password, result);
    maybeAppend(Boolean.toString(hidden), result);
    return result.toString();
  }

}