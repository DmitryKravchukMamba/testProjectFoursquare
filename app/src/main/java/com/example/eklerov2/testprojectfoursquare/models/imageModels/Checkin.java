
package com.example.eklerov2.testprojectfoursquare.models.imageModels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Checkin {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("createdAt")
    @Expose
    private Integer createdAt;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("timeZoneOffset")
    @Expose
    private Integer timeZoneOffset;

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The createdAt
     */
    public Integer getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     *     The createdAt
     */
    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The timeZoneOffset
     */
    public Integer getTimeZoneOffset() {
        return timeZoneOffset;
    }

    /**
     * 
     * @param timeZoneOffset
     *     The timeZoneOffset
     */
    public void setTimeZoneOffset(Integer timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

}
