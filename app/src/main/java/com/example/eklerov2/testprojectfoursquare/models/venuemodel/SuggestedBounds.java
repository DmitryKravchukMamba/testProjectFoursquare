
package com.example.eklerov2.testprojectfoursquare.models.venuemodel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SuggestedBounds {

    @SerializedName("ne")
    @Expose
    private Ne ne;
    @SerializedName("sw")
    @Expose
    private Sw sw;

    /**
     * 
     * @return
     *     The ne
     */
    public Ne getNe() {
        return ne;
    }

    /**
     * 
     * @param ne
     *     The ne
     */
    public void setNe(Ne ne) {
        this.ne = ne;
    }

    /**
     * 
     * @return
     *     The sw
     */
    public Sw getSw() {
        return sw;
    }

    /**
     * 
     * @param sw
     *     The sw
     */
    public void setSw(Sw sw) {
        this.sw = sw;
    }

}
