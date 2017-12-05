package com.zxing.multi.qrcode.detector;

import com.zxing.DecodeHintType;
import com.zxing.NotFoundException;
import com.zxing.ReaderException;
import com.zxing.ResultPointCallback;
import com.zxing.common.BitMatrix;
import com.zxing.common.DetectorResult;
import com.zxing.qrcode.detector.Detector;
import com.zxing.qrcode.detector.FinderPatternInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class MultiDetector extends Detector {

  private static final DetectorResult[] EMPTY_DETECTOR_RESULTS = new DetectorResult[0];

  public MultiDetector(BitMatrix image) {
    super(image);
  }

  public DetectorResult[] detectMulti(Map<DecodeHintType,?> hints) throws NotFoundException {
    BitMatrix image = getImage();
    ResultPointCallback resultPointCallback =
        hints == null ? null : (ResultPointCallback) hints.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
    MultiFinderPatternFinder finder = new MultiFinderPatternFinder(image, resultPointCallback);
    FinderPatternInfo[] infos = finder.findMulti(hints);

    if (infos.length == 0) {
      throw NotFoundException.getNotFoundInstance();
    }

    List<DetectorResult> result = new ArrayList<DetectorResult>();
    for (FinderPatternInfo info : infos) {
      try {
        result.add(processFinderPatternInfo(info));
      } catch (ReaderException e) {
        // ignore
      }
    }
    if (result.isEmpty()) {
      return EMPTY_DETECTOR_RESULTS;
    } else {
      return result.toArray(new DetectorResult[result.size()]);
    }
  }

}
