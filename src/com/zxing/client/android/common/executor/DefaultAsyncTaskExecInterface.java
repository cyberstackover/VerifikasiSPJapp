package com.zxing.client.android.common.executor;

import android.os.AsyncTask;

public final class DefaultAsyncTaskExecInterface implements AsyncTaskExecInterface {

//  @Override
  public <T> void execute(AsyncTask<T,?,?> task, T... args) {
    task.execute(args);
  }

}
