package com.zxing.client.android.common.executor;

import android.os.AsyncTask;

public interface AsyncTaskExecInterface {

  <T> void execute(AsyncTask<T,?,?> task, T... args);

}
