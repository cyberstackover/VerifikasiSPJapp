package com.zxing.client.result;

public final class EmailAddressParsedResult extends ParsedResult {

  private final String emailAddress;
  private final String subject;
  private final String body;
  private final String mailtoURI;

  EmailAddressParsedResult(String emailAddress,
                           String subject,
                           String body,
                           String mailtoURI) {
    super(ParsedResultType.EMAIL_ADDRESS);
    this.emailAddress = emailAddress;
    this.subject = subject;
    this.body = body;
    this.mailtoURI = mailtoURI;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public String getSubject() {
    return subject;
  }

  public String getBody() {
    return body;
  }

  public String getMailtoURI() {
    return mailtoURI;
  }

  @Override
  public String getDisplayResult() {
    StringBuilder result = new StringBuilder(30);
    maybeAppend(emailAddress, result);
    maybeAppend(subject, result);
    maybeAppend(body, result);
    return result.toString();
  }

}