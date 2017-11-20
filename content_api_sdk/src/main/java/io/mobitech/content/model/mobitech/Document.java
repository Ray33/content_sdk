
package io.mobitech.content.model.mobitech;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class Document implements Serializable, Parcelable
{

    String id = "";

//    @JsonField(typeConverter = Thumbnail.class)
    List<Thumbnail> thumbnails = new ArrayList<Thumbnail>();

    String title = "";

    String description = "";

    String clickUrl = "";

    String visibleUrl = "";

    String originalUrl = "";

    String publishedTime = "";

    String language = null;

    boolean promoted = false;

    String promotedText = "";

    String type = "";

    String author = "";

    List<String> categories = new ArrayList<String>();

    List<Object> categoriesEn = new ArrayList<Object>();

    String country = "";

    public final static Parcelable.Creator<Document> CREATOR = new Creator<Document>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Document createFromParcel(Parcel in) {
            return new Document(in);
        }

        public Document[] newArray(int size) {
            return (new Document[size]);
        }

    }
    ;
    private final static long serialVersionUID = 5974178878098079415L;

    protected Document(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.thumbnails, (io.mobitech.content.model.mobitech.Thumbnail.class.getClassLoader()));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.clickUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.visibleUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.originalUrl = ((String) in.readValue((Object.class.getClassLoader())));
        this.publishedTime = ((String) in.readValue((String.class.getClassLoader())));
        this.language = ((String) in.readValue((Object.class.getClassLoader())));
        this.promoted = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.promotedText = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.author = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.categories, (java.lang.String.class.getClassLoader()));
        in.readList(this.categoriesEn, (java.lang.Object.class.getClassLoader()));
        this.country = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Document() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Thumbnail> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<Thumbnail> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getVisibleUrl() {
        return visibleUrl;
    }

    public void setVisibleUrl(String visibleUrl) {
        this.visibleUrl = visibleUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public String getPromotedText() {
        return promotedText;
    }

    public void setPromotedText(String promotedText) {
        this.promotedText = promotedText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Object> getCategoriesEn() {
        return categoriesEn;
    }

    public void setCategoriesEn(List<Object> categoriesEn) {
        this.categoriesEn = categoriesEn;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(thumbnails);
        dest.writeValue(title);
        dest.writeValue(description);
        dest.writeValue(clickUrl);
        dest.writeValue(visibleUrl);
        dest.writeValue(originalUrl);
        dest.writeValue(publishedTime);
        dest.writeValue(language);
        dest.writeValue(promoted);
        dest.writeValue(promotedText);
        dest.writeValue(type);
        dest.writeValue(author);
        dest.writeList(categories);
        dest.writeList(categoriesEn);
        dest.writeValue(country);
    }

    public int describeContents() {
        return  0;
    }

}
