package com.zxing.client.result;

import com.zxing.Result;

import java.util.Map;
public final class EmailAddressResultParser extends ResultParser {

  @Override
  public EmailAddressParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    String emailAddress;
    if (rawText.startsWith("mailto:") || rawText.startsWith("MAILTO:")) {
      // If it starts with mailto:, assume it is definitely trying to be an email address
      emailAddress = rawText.substring(7);
      int queryStart = emailAddress.indexOf('?');
      if (queryStart >= 0) {
        emailAddress = emailAddress.substring(0, queryStart);
      }
      emailAddress = urlDecode(emailAddress);
      Map<String,String> nameValues = parseNameValuePairs(rawText);
      String subject = null;
      String body = null;
      if (nameValues != null) {
        if (emailAddress.length() == 0) {
          emailAddress = nameValues.get("to");
        }
        subject = nameValues.get("subject");
        body = nameValues.get("body");
      }
      return new EmailAddressParsedResult(emailAddress, subject, body, rawText);
    } else {
      if (!EmailDoCoMoResultParser.isBasicallyValidEmailAddress(rawText)) {
        return null;
      }
      emailAddress = rawText;
      return new EmailAddressParsedResult(emailAddress, null, null, "mailto:" + emailAddress);
    }
  }

}