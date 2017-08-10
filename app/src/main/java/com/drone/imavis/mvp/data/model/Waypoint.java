package com.drone.imavis.mvp.data.model;

import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinateDao;

/**
 * Created by adigu on 09.08.2017.
 */

@Entity
public class Waypoint {

    public Waypoint(GPSCoordinate gpsCoordinate, WaypointData waypointData) {
        this.gpsCoordinate = gpsCoordinate;
        this.waypointData = waypointData;
    }

    @Generated(hash = 1965225866)
    public Waypoint(Long id, @NotNull Long gpsCoordinateId, @NotNull Long waypointDataId) {
        this.id = id;
        this.gpsCoordinateId = gpsCoordinateId;
        this.waypointDataId = waypointDataId;
    }

    @Generated(hash = 1578831160)
    public Waypoint() {
    }

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private Long gpsCoordinateId;
    @ToOne(joinProperty = "gpsCoordinateId")
    private GPSCoordinate gpsCoordinate;

    @NotNull
    private Long waypointDataId;
    @ToOne(joinProperty = "waypointDataId")
    private WaypointData waypointData;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 148709341)
    private transient WaypointDao myDao;

    @Generated(hash = 31235149)
    private transient Long gpsCoordinate__resolvedKey;

    @Generated(hash = 293187056)
    private transient Long waypointData__resolvedKey;

    public Long getId() {
        return id;
    }

    /*
    public GPSCoordinate getGpsCoordinate() {
        return gpsCoordinate;
    }
    public void setGpsCoordinate(GPSCoordinate gpsCoordinate) {
        this.gpsCoordinate = gpsCoordinate;
    }
    */

    public Long getWaypointDataId() {
        return waypointDataId;
    }
    public void setWaypointDataId(Long waypointDataId) {
        this.waypointDataId = waypointDataId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGpsCoordinateId() {
        return this.gpsCoordinateId;
    }

    public void setGpsCoordinateId(Long gpsCoordinateId) {
        this.gpsCoordinateId = gpsCoordinateId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 776504788)
    public GPSCoordinate getGpsCoordinate() {
        Long __key = this.gpsCoordinateId;
        if (gpsCoordinate__resolvedKey == null || !gpsCoordinate__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GPSCoordinateDao targetDao = daoSession.getGPSCoordinateDao();
            GPSCoordinate gpsCoordinateNew = targetDao.load(__key);
            synchronized (this) {
                gpsCoordinate = gpsCoordinateNew;
                gpsCoordinate__resolvedKey = __key;
            }
        }
        return gpsCoordinate;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 344223125)
    public void setGpsCoordinate(@NotNull GPSCoordinate gpsCoordinate) {
        if (gpsCoordinate == null) {
            throw new DaoException(
                    "To-one property 'gpsCoordinateId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.gpsCoordinate = gpsCoordinate;
            gpsCoordinateId = gpsCoordinate.getId();
            gpsCoordinate__resolvedKey = gpsCoordinateId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 430949116)
    public WaypointData getWaypointData() {
        Long __key = this.waypointDataId;
        if (waypointData__resolvedKey == null || !waypointData__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WaypointDataDao targetDao = daoSession.getWaypointDataDao();
            WaypointData waypointDataNew = targetDao.load(__key);
            synchronized (this) {
                waypointData = waypointDataNew;
                waypointData__resolvedKey = __key;
            }
        }
        return waypointData;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1967994165)
    public void setWaypointData(@NotNull WaypointData waypointData) {
        if (waypointData == null) {
            throw new DaoException(
                    "To-one property 'waypointDataId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.waypointData = waypointData;
            waypointDataId = waypointData.getId();
            waypointData__resolvedKey = waypointDataId;
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
    @Generated(hash = 1888057484)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getWaypointDao() : null;
    }

}
