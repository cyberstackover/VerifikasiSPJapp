package com.zxing.client.result;

import com.zxing.Result;
public final class SMTPResultParser extends ResultParser {

  @Override
  public EmailAddressParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    if (!(rawText.startsWith("smtp:") || rawText.startsWith("SMTP:"))) {
      return null;
    }
    String emailAddress = rawText.substring(5);
    String subject = null;
    String body = null;
    int colon = emailAddress.indexOf(':');
    if (colon >= 0) {
      subject = emailAddress.substring(colon + 1);
      emailAddress = emailAddress.substring(0, colon);
      colon = subject.indexOf(':');
      if (colon >= 0) {
        body = subject.substring(colon + 1);
        subject = subject.substring(0, colon);
      }
    }
    String mailtoURI = "mailto:" + emailAddress;
    return new EmailAddressParsedResult(emailAddress, subject, body, mailtoURI);
  }
}
