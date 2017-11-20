package io.mobitech.content.model.mobitech;

/**
 * Created on 11/16/17.
 *
 * @author Sergey Pogoryelov
 */

public enum ContentType {
    ORGANIC("organic"),
    PROMOTED("promoted"),
    VIEDO("video"),
    MIX("mix"),
    MIX_VIDEO("mix_video");

    String value;

    ContentType(java.lang.String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
