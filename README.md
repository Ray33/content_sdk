# content_sdk
Content SDK to be convenience  layer either for API or for UI Rendering.

The project is divided into 2 moduels:

1. __content_api_sdk__ - a code that queries Mobitech content API SDK and provide a callback to process the returned java object.
 Use this module if you want to cusomize the UI layout.
 To use this project, add to your gradle.build the dependency:
```java
     compile('io.mobitech.content:content_api_sdk:4.1.21@aar') {
        transitive = true
    }
```
Then initiate the module once:
```java
  recommendationService = RecommendationService.build(getApplicationContext(), getBaseContext().getString(R.string.MOBITECH_CONTENT_PUBLISHER_API_KEY), advertId);
```

Then use it:
```java
     recommendationService
                    .getOrganicContent(null, 5, new ContentCallback<List<Document>>() {
                @Override
                public void processResult(List<Document> contentResult, Context context) {
                    //Do something with the documents
                }
            });
```
A sample usage can be found here: https://github.com/Ray33/mobitech_content_sdk_demo
2. __content_ui_sdk__ - a code that queries Mobitech content UI SDK and provide a ready content UI .
Use this module if you want the SDK to render the UI as well and you are satisfied with the layour.
The usage of the content UI SDK is demonstrated here:
https://github.com/Ray33/mobitech_content_ui_sdk_demo
