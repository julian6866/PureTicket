package com.pureiconsulting.tt.service;


import java.util.Date;

/**
 * Created by julianzhu on 11/16/15.
 */
public interface Cacheable {


    /**
     * The unique identifier for this cacheable object.
     * @return
     */
    String getId();

    void setId(String id);

    /**
     * The time stamp when this object is put in cached.
     * @return
     */
    Date getTimestamp();

    void setTimestamp(Date timestamp);


}
