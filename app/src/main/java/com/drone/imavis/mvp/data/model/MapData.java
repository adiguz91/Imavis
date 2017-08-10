package com.drone.imavis.mvp.data.model;

import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;
import org.greenrobot.greendao.DaoException;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinateDao;

/**
 * Created by adigu on 10.08.2017.
 */

@Entity
public class MapData implements Serializable {

    public MapData(GPSCoordinate centerCoordinate, float zoomFactor) {
        this.centerCoordinate = centerCoordinate;
        this.zoomFactor = zoomFactor;
    }

    @Generated(hash = 847372236)
    public MapData(Long id, @NotNull Long centerCoordinateId, float zoomFactor,
            MapMode mapMode) {
        this.id = id;
        this.centerCoordinateId = centerCoordinateId;
        this.zoomFactor = zoomFactor;
        this.mapMode = mapMode;
    }

    @Generated(hash = 896623006)
    public MapData() {
    }

    // ANY-ACCESS-MODIFIER
    static final long serialVersionUID = 42L;

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private Long centerCoordinateId;
    @ToOne(joinProperty = "centerCoordinateId")
    private GPSCoordinate centerCoordinate;

    private float zoomFactor;

    @Convert(converter = MapModeConverter.class, columnType = String.class)
    private MapMode mapMode;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 590566735)
    private transient MapDataDao myDao;

    @Generated(hash = 86340270)
    private transient Long centerCoordinate__resolvedKey;

    /* GreenDAO CONVERTER */

    static class MapModeConverter implements PropertyConverter<MapMode, String> {
        @Override
        public MapMode convertToEntityProperty(String databaseValue) {
            return MapMode.valueOf(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(MapMode entityProperty) {
            return entityProperty.name();
        }
    }

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCenterCoordinateId() {
        return this.centerCoordinateId;
    }

    public void setCenterCoordinateId(Long centerCoordinateId) {
        this.centerCoordinateId = centerCoordinateId;
    }

    public float getZoomFactor() {
        return this.zoomFactor;
    }

    public void setZoomFactor(float zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public MapMode getMapMode() {
        return this.mapMode;
    }

    public void setMapMode(MapMode mapMode) {
        this.mapMode = mapMode;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 678387712)
    public GPSCoordinate getCenterCoordinate() {
        Long __key = this.centerCoordinateId;
        if (centerCoordinate__resolvedKey == null
                || !centerCoordinate__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GPSCoordinateDao targetDao = daoSession.getGPSCoordinateDao();
            GPSCoordinate centerCoordinateNew = targetDao.load(__key);
            synchronized (this) {
                centerCoordinate = centerCoordinateNew;
                centerCoordinate__resolvedKey = __key;
            }
        }
        return centerCoordinate;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 108410224)
    public void setCenterCoordinate(@NotNull GPSCoordinate centerCoordinate) {
        if (centerCoordinate == null) {
            throw new DaoException(
                    "To-one property 'centerCoordinateId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.centerCoordinate = centerCoordinate;
            centerCoordinateId = centerCoordinate.getId();
            centerCoordinate__resolvedKey = centerCoordinateId;
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
    @Generated(hash = 89250062)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMapDataDao() : null;
    }
}