package com.drone.imavis.mvp.data.model;

import android.graphics.Point;

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
public class PointOfInterest {

    public PointOfInterest(GPSCoordinate gpsCoordinate, PointOfInterestData pointOfInterestData) {
        this.gpsCoordinate = gpsCoordinate;
        this.pointOfInterestData = pointOfInterestData;
    }

    @Generated(hash = 1734459232)
    public PointOfInterest(Long id, @NotNull Long gpsCoordinateId,
            @NotNull Long pointOfInterestDataId) {
        this.id = id;
        this.gpsCoordinateId = gpsCoordinateId;
        this.pointOfInterestDataId = pointOfInterestDataId;
    }

    @Generated(hash = 2132244466)
    public PointOfInterest() {
    }

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private Long gpsCoordinateId;
    @ToOne(joinProperty = "gpsCoordinateId")
    private GPSCoordinate gpsCoordinate;

    @NotNull
    private Long pointOfInterestDataId;
    @ToOne(joinProperty = "pointOfInterestDataId")
    private PointOfInterestData pointOfInterestData;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 193699901)
    private transient PointOfInterestDao myDao;

    @Generated(hash = 31235149)
    private transient Long gpsCoordinate__resolvedKey;

    @Generated(hash = 226575151)
    private transient Long pointOfInterestData__resolvedKey;

    public Long getId() {
        return id;
    }

    public Long getGpsCoordinateId() {
        return gpsCoordinateId;
    }
    public void setGpsCoordinateId(Long gpsCoordinateId) {
        this.gpsCoordinateId = gpsCoordinateId;
    }
    
    public Long getPointOfInterestDataId() {
        return pointOfInterestDataId;
    }
    public void setPointOfInterestDataId(Long pointOfInterestDataId) {
        this.pointOfInterestDataId = pointOfInterestDataId;
    }

    public void setId(Long id) {
        this.id = id;
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
    @Generated(hash = 904156294)
    public PointOfInterestData getPointOfInterestData() {
        Long __key = this.pointOfInterestDataId;
        if (pointOfInterestData__resolvedKey == null
                || !pointOfInterestData__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PointOfInterestDataDao targetDao = daoSession.getPointOfInterestDataDao();
            PointOfInterestData pointOfInterestDataNew = targetDao.load(__key);
            synchronized (this) {
                pointOfInterestData = pointOfInterestDataNew;
                pointOfInterestData__resolvedKey = __key;
            }
        }
        return pointOfInterestData;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 587454760)
    public void setPointOfInterestData(@NotNull PointOfInterestData pointOfInterestData) {
        if (pointOfInterestData == null) {
            throw new DaoException(
                    "To-one property 'pointOfInterestDataId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.pointOfInterestData = pointOfInterestData;
            pointOfInterestDataId = pointOfInterestData.getId();
            pointOfInterestData__resolvedKey = pointOfInterestDataId;
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
    @Generated(hash = 1937936028)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPointOfInterestDao() : null;
    }
}
