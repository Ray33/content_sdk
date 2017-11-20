
package io.mobitech.content.model.mobitech;

import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class IpResponse
{
    String ip = "";

    public String getIp() {
        return ip;
    }
}
