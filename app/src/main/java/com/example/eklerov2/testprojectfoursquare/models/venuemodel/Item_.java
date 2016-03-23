
package com.example.eklerov2.testprojectfoursquare.models.venuemodel;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Item_ {

    @SerializedName("reasons")
    @Expose
    private Reasons reasons;
    @SerializedName("venue")
    @Expose
    private Venue venue;
    @SerializedName("tips")
    @Expose
    private List<Tip> tips = new ArrayList<Tip>();
    @SerializedName("referralId")
    @Expose
    private String referralId;

    /**
     * 
     * @return
     *     The reasons
     */
    public Reasons getReasons() {
        return reasons;
    }

    /**
     * 
     * @param reasons
     *     The reasons
     */
    public void setReasons(Reasons reasons) {
        this.reasons = reasons;
    }

    /**
     * 
     * @return
     *     The venue
     */
    public Venue getVenue() {
        return venue;
    }

    /**
     * 
     * @param venue
     *     The venue
     */
    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    /**
     * 
     * @return
     *     The tips
     */
    public List<Tip> getTips() {
        return tips;
    }

    /**
     * 
     * @param tips
     *     The tips
     */
    public void setTips(List<Tip> tips) {
        this.tips = tips;
    }

    /**
     * 
     * @return
     *     The referralId
     */
    public String getReferralId() {
        return referralId;
    }

    /**
     * 
     * @param referralId
     *     The referralId
     */
    public void setReferralId(String referralId) {
        this.referralId = referralId;
    }

}
