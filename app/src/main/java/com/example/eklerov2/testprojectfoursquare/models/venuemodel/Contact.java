
package com.example.eklerov2.testprojectfoursquare.models.venuemodel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Contact {

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("formattedPhone")
    @Expose
    private String formattedPhone;

    /**
     * 
     * @return
     *     The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 
     * @param phone
     *     The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 
     * @return
     *     The formattedPhone
     */
    public String getFormattedPhone() {
        return formattedPhone;
    }

    /**
     * 
     * @param formattedPhone
     *     The formattedPhone
     */
    public void setFormattedPhone(String formattedPhone) {
        this.formattedPhone = formattedPhone;
    }

}
