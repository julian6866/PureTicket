package com.pureiconsulting.tt.service;

import java.util.Date;

/**
 * Created by julianzhu on 11/16/15.
 */
public abstract class CacheableObject implements Cacheable {

    private Date timestamp = new Date();

    private String id = "";
    //abstract public void setId(String id);

    //abstract public String getId();

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
