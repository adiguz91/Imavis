package com.drone.imavis.mvp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by adigu on 06.05.2017.
 */

@Entity(indexes = {
        @Index(value = "name", unique = true)
})
public class Project implements Parcelable {

    @Id(autoincrement = true)
    @SerializedName("databaseId")
    @Expose(serialize = false, deserialize = false)
    private Long id;

    @SerializedName("id")
    private int onlineId;

    @Transient
    @SerializedName("tasks")
    private List<String> tasks; // UUIDs

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("name")
    @NotNull
    private String name;

    @SerializedName("description")
    private String description;

    @ToMany(referencedJoinProperty = "projectId")
    @OrderBy("name ASC")
    private List<FlyPlan> flyPlans;

    public Project() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public int getOnlineId() {
        return onlineId;
    }
    public void setOnlineId(int onlineId) {
        this.onlineId = onlineId;
    }

    public List<String> getTasks() {
        return tasks;
    }
    private void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    /* PARCELABLE PART */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id == null ? 0 : id);
        dest.writeInt(onlineId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeList(tasks);
        dest.writeLong((createdAt == null) ? 0 : createdAt.getTime());
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1691566062)
    public List<FlyPlan> getFlyPlans() {
        if (flyPlans == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FlyPlanDao targetDao = daoSession.getFlyPlanDao();
            List<FlyPlan> flyPlansNew = targetDao._queryProject_FlyPlans(id);
            synchronized (this) {
                if (flyPlans == null) {
                    flyPlans = flyPlansNew;
                }
            }
        }
        return flyPlans;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 786999239)
    public synchronized void resetFlyPlans() {
        flyPlans = null;
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
    @Generated(hash = 2081800561)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getProjectDao() : null;
    }

    /**
     * Constructs a Project from a Parcel
     * @param parcelIn Source Parcel
     */
    public Project (Parcel parcelIn) {
        this.id = parcelIn.readLong();
        this.onlineId = parcelIn.readInt();
        this.name = parcelIn.readString();
        this.description = parcelIn.readString();
        this.tasks = new ArrayList<String>();
        parcelIn.readList(tasks, null);
        long temp = parcelIn.readLong();
        this.createdAt = (temp == 0) ? null : new Date(temp);
    }

    @Generated(hash = 100493354)
    public Project(Long id, int onlineId, Date createdAt, @NotNull String name, String description) {
        this.id = id;
        this.onlineId = onlineId;
        this.createdAt = createdAt;
        this.name = name;
        this.description = description;
    }

    // Method to recreate a Question from a Parcel
    public static Creator<Project> CREATOR = new Creator<Project>() {

        @Override
        public Project createFromParcel(Parcel source) {
            return new Project(source);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }

    };

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1378029107)
    private transient ProjectDao myDao;

}
