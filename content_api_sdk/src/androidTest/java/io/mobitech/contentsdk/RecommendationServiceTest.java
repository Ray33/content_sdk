package io.mobitech.contentsdk;

import android.content.Context;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.mobitech.content.model.mobitech.Document;
import io.mobitech.content.services.RecommendationService;
import io.mobitech.content.services.api.callbacks.ContentCallback;

/**
 * Created on 11/16/17.
 *
 * @author Sergey Pogoryelov
 */

public class RecommendationServiceTest extends ApplicationTest {

    public void testRecommendationService() {
        try {
            RecommendationService recommendationService = RecommendationService
                    .build(getContext(), "TESTC36B5A", "testRecommendationService-user-id");

            final CountDownLatch signal = new CountDownLatch(1);

            recommendationService.getOrganicContent(null, null, null,
                    null, null, null, new ContentCallback<List<Document>>() {
                        @Override
                        public void processResult(List<Document> contentResult, Context context) {
                            assertTrue(contentResult.size() > 0);
                            signal.countDown();
                        }
                    });


            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
