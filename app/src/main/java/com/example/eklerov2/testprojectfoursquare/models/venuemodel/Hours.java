
package com.example.eklerov2.testprojectfoursquare.models.venuemodel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Hours {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("isOpen")
    @Expose
    private Boolean isOpen;
    @SerializedName("isLocalHoliday")
    @Expose
    private Boolean isLocalHoliday;

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The isOpen
     */
    public Boolean getIsOpen() {
        return isOpen;
    }

    /**
     * 
     * @param isOpen
     *     The isOpen
     */
    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    /**
     * 
     * @return
     *     The isLocalHoliday
     */
    public Boolean getIsLocalHoliday() {
        return isLocalHoliday;
    }

    /**
     * 
     * @param isLocalHoliday
     *     The isLocalHoliday
     */
    public void setIsLocalHoliday(Boolean isLocalHoliday) {
        this.isLocalHoliday = isLocalHoliday;
    }

}
