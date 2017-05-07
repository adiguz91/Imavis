package com.drone.imavis.data.local;

import com.drone.imavis.util.helper.DateUtil;

import java.util.Date;

/**
 * Created by adigu on 06.05.2017.
 */

public class BaseEntity {

    private long id;
    private Date creationDate;
    private Date modificationDate;

    public BaseEntity() {
        setCreationDate(DateUtil.getDateNow());
        setModificationDate(DateUtil.getDateNow());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }
}
