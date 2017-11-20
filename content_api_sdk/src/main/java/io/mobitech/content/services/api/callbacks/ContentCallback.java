package io.mobitech.content.services.api.callbacks;

import android.content.Context;

public interface ContentCallback<T> {

    void processResult(T contentResult, Context context);
}
