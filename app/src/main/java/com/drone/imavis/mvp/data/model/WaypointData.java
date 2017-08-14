package com.drone.imavis.mvp.data.model;

import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.WaypointMode;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by adigu on 09.08.2017.
 */

@Entity
public class WaypointData {

    public WaypointData() {}
    @Generated(hash = 1994139258)
    public WaypointData(Long id, Long poiID, int distanceToNextNode, int speedToNextNode, int flyHeight,
            @NotNull WaypointMode mode) {
        this.id = id;
        this.poiID = poiID;
        this.distanceToNextNode = distanceToNextNode;
        this.speedToNextNode = speedToNextNode;
        this.flyHeight = flyHeight;
        this.mode = mode;
    }

    @Id(autoincrement = true)
    private Long id;

    private Long poiID;
    @ToOne(joinProperty = "poiID")
    private PointOfInterest poi;

    //private int direction;

    @NotNull
    private int distanceToNextNode;

    @NotNull
    private int speedToNextNode;

    @NotNull
    private int flyHeight;

    @NotNull
    @Convert(converter = WaypointModeConverter.class, columnType = String.class)
    private WaypointMode mode;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1891596530)
    private transient WaypointDataDao myDao;

    @Generated(hash = 1422013611)
    private transient Long poi__resolvedKey;

    public int getDistanceToNextNode() {
        return distanceToNextNode;
    }
    public void setDistanceToNextNode(int distanceToNextNode) {
        this.distanceToNextNode = distanceToNextNode;
    }

    public int getSpeedToNextNode() {
        return speedToNextNode;
    }
    public void setSpeedToNextNode(int speedToNextNode) {
        this.speedToNextNode = speedToNextNode;
    }

    public int getFlyHeight() {
        return flyHeight;
    }
    public void setFlyHeight(int flyHeight) {
        this.flyHeight = flyHeight;
    }

    public WaypointMode getMode() {
        return mode;
    }
    public void setMode(WaypointMode mode) {
        this.mode = mode;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getPoiID() {
        return this.poiID;
    }
    public void setPoiID(Long poiID) {
        this.poiID = poiID;
    }
	
	/* GreenDAO CONVERTER */

    static class WaypointModeConverter implements PropertyConverter<WaypointMode, String> {
        @Override
        public WaypointMode convertToEntityProperty(String databaseValue) {
            return WaypointMode.valueOf(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(WaypointMode entityProperty) {
            return entityProperty.name();
        }
    }
	
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 785828351)
    public PointOfInterest getPoi() {
        Long __key = this.poiID;
        if (poi__resolvedKey == null || !poi__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PointOfInterestDao targetDao = daoSession.getPointOfInterestDao();
            PointOfInterest poiNew = targetDao.load(__key);
            synchronized (this) {
                poi = poiNew;
                poi__resolvedKey = __key;
            }
        }
        return poi;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1946840573)
    public void setPoi(PointOfInterest poi) {
        synchronized (this) {
            this.poi = poi;
            poiID = poi == null ? null : poi.getId();
            poi__resolvedKey = poiID;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 364396101)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getWaypointDataDao() : null;
    }   

    
}
