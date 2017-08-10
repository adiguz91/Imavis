package com.drone.imavis.mvp.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by adigu on 09.08.2017.
 */

@Entity
public class PointOfInterestData {

    public PointOfInterestData() {}

    @Generated(hash = 1598951641)
    public PointOfInterestData(Long id, int flyHeight) {
        this.id = id;
        this.flyHeight = flyHeight;
    }

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private int flyHeight;

    @ToMany(referencedJoinProperty = "id")
    private List<Waypoint> waypoints;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1773127643)
    private transient PointOfInterestDataDao myDao;

    public Long getId() {
        return id;
    }

    public int getFlyHeight() {
        return flyHeight;
    }
    public void setFlyHeight(int flyHeight) {
        this.flyHeight = flyHeight;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1210881413)
    public List<Waypoint> getWaypoints() {
        if (waypoints == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WaypointDao targetDao = daoSession.getWaypointDao();
            List<Waypoint> waypointsNew = targetDao
                    ._queryPointOfInterestData_Waypoints(id);
            synchronized (this) {
                if (waypoints == null) {
                    waypoints = waypointsNew;
                }
            }
        }
        return waypoints;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1743736783)
    public synchronized void resetWaypoints() {
        waypoints = null;
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
    @Generated(hash = 335587453)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPointOfInterestDataDao() : null;
    }
}
