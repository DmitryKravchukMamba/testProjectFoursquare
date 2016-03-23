
package com.example.eklerov2.testprojectfoursquare.models.imageModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Photos {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("items")
    @Expose
    private List<Item_> items = new ArrayList<Item_>();
    @SerializedName("dupesRemoved")
    @Expose
    private Integer dupesRemoved;

    /**
     * 
     * @return
     *     The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 
     * @param count
     *     The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 
     * @return
     *     The items
     */
    public List<Item_> getItems() {
        return items;
    }

    /**
     * 
     * @param items
     *     The items
     */
    public void setItems(List<Item_> items) {
        this.items = items;
    }

    /**
     * 
     * @return
     *     The dupesRemoved
     */
    public Integer getDupesRemoved() {
        return dupesRemoved;
    }

    /**
     * 
     * @param dupesRemoved
     *     The dupesRemoved
     */
    public void setDupesRemoved(Integer dupesRemoved) {
        this.dupesRemoved = dupesRemoved;
    }

}
