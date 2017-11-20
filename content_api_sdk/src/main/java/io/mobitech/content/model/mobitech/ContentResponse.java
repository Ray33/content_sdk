
package io.mobitech.content.model.mobitech;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class ContentResponse implements Serializable, Parcelable
{

    String sessionId = "";

//    @JsonField(typeConverter = Document.class)
    List<Document> documents = new ArrayList<Document>();

    public final static Parcelable.Creator<ContentResponse> CREATOR = new Creator<ContentResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ContentResponse createFromParcel(Parcel in) {
            return new ContentResponse(in);
        }

        public ContentResponse[] newArray(int size) {
            return (new ContentResponse[size]);
        }

    }
    ;
    private final static long serialVersionUID = 3897937653990126749L;

    protected ContentResponse(Parcel in) {
        this.sessionId = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.documents, (io.mobitech.content.model.mobitech.Document.class.getClassLoader()));
    }

    public ContentResponse() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(sessionId);
        dest.writeList(documents);
    }

    public int describeContents() {
        return  0;
    }

}
