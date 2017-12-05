package com.zxing.client.result;

import com.zxing.Result;

public final class AddressBookDoCoMoResultParser extends AbstractDoCoMoResultParser {

  @Override
  public AddressBookParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    if (!rawText.startsWith("MECARD:")) {
      return null;
    }
    String[] rawName = matchDoCoMoPrefixedField("N:", rawText, true);
    if (rawName == null) {
      return null;
    }
    String name = parseName(rawName[0]);
    String pronunciation = matchSingleDoCoMoPrefixedField("SOUND:", rawText, true);
    String[] phoneNumbers = matchDoCoMoPrefixedField("TEL:", rawText, true);
    String[] emails = matchDoCoMoPrefixedField("EMAIL:", rawText, true);
    String note = matchSingleDoCoMoPrefixedField("NOTE:", rawText, false);
    String[] addresses = matchDoCoMoPrefixedField("ADR:", rawText, true);
    String birthday = matchSingleDoCoMoPrefixedField("BDAY:", rawText, true);
    if (birthday != null && !isStringOfDigits(birthday, 8)) {
      // No reason to throw out the whole card because the birthday is formatted wrong.
      birthday = null;
    }
    String[] urls = matchDoCoMoPrefixedField("URL:", rawText, true);

    // Although ORG may not be strictly legal in MECARD, it does exist in VCARD and we might as well
    // honor it when found in the wild.
    String org = matchSingleDoCoMoPrefixedField("ORG:", rawText, true);

    return new AddressBookParsedResult(maybeWrap(name),
                                       null,
                                       pronunciation,
                                       phoneNumbers,
                                       null,
                                       emails,
                                       null,
                                       null,
                                       note,
                                       addresses,
                                       null,
                                       org,
                                       birthday,
                                       null,
                                       urls,
                                       null);
  }

  private static String parseName(String name) {
    int comma = name.indexOf((int) ',');
    if (comma >= 0) {
      // Format may be last,first; switch it around
      return name.substring(comma + 1) + ' ' + name.substring(0, comma);
    }
    return name;
  }

}
