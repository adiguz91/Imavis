package com.drone.imavis.mvp.data.model;

import android.graphics.Canvas;
import android.graphics.Path;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.Nodes;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoint;
import com.drone.imavis.mvp.util.StringUtil;
import com.drone.imavis.mvp.util.constants.classes.CFlyPlan;
import com.drone.imavis.mvp.util.constants.classes.CFlyPlan.UnitOfLength;
import com.google.gson.Gson;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Date;
import java.util.ListIterator;

/**
 * Created by Adrian on 26.11.2016.
 */

@Entity(indexes = {
        @Index(value = "name", unique = true)
})
public class FlyPlan implements Parcelable {

    // Method to recreate a Question from a Parcel
    public static Parcelable.Creator<FlyPlan> CREATOR = new Parcelable.Creator<FlyPlan>() {

        @Override
        public FlyPlan createFromParcel(Parcel source) {
            return new FlyPlan(source);
        }

        @Override
        public FlyPlan[] newArray(int size) {
            return new FlyPlan[size];
        }

    };
    @Transient
    private static Gson gson = new Gson();
    @Id(autoincrement = true)
    private Long id;
    @Convert(converter = UnitOfLengthConverter.class, columnType = String.class)
    private UnitOfLength unitOfLength = CFlyPlan.UNIT_OF_LENGTH;
    @NotNull
    private Long projectId;
    @NotNull
    @ToOne(joinProperty = "projectId")
    private Project project;
    private Long mapDataId;
    @ToOne(joinProperty = "mapDataId")
    private MapData mapData;
    @Transient
    private GoogleMapExtension map;
    private Date createdAt;
    @Transient
    private Uri imageFolderUrl;
    @Transient
    private Nodes nodes;
    private String nodesJson;
    private String taskId;
    @Transient
    private Task task;
    @Transient
    private String taskSerialized;
    @NotNull
    private String name; // task name
    @NotNull
    private int minFlyHeight = CFlyPlan.MIN_FLY_HEIGHT;
    @NotNull
    private int minSpeed = CFlyPlan.MIN_SPEED;

    private boolean isClosed = false;

    private float scaleFactor = 1.0f;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 500138044)
    private transient FlyPlanDao myDao;
    @Generated(hash = 1005767482)
    private transient Long project__resolvedKey;
    @Generated(hash = 1887247556)
    private transient Long mapData__resolvedKey;

    /**
     * Constructs a Project from a Parcel
     *
     * @param parcelIn Source Parcel
     */
    public FlyPlan(Parcel parcelIn) {
        this.id = parcelIn.readLong();
        this.mapData = (MapData) parcelIn.readSerializable();
        this.minFlyHeight = parcelIn.readInt();
        this.minSpeed = parcelIn.readInt();
        this.unitOfLength = UnitOfLength.valueOf(parcelIn.readString());
        this.name = parcelIn.readString();
        //this.nodes = (Nodes) parcelIn.readSerializable();
        this.nodesJson = parcelIn.readString();
        this.task = parcelIn.readParcelable(Task.class.getClassLoader());
        this.projectId = parcelIn.readLong();
        this.taskSerialized = parcelIn.readString();
        this.isClosed = parcelIn.readByte() != 0;
        this.scaleFactor = parcelIn.readFloat();
    }

    @Generated(hash = 15195015)
    public FlyPlan(Long id, UnitOfLength unitOfLength, @NotNull Long projectId, Long mapDataId, Date createdAt, String nodesJson, String taskId,
                   @NotNull String name, int minFlyHeight, int minSpeed, boolean isClosed, float scaleFactor) {
        this.id = id;
        this.unitOfLength = unitOfLength;
        this.projectId = projectId;
        this.mapDataId = mapDataId;
        this.createdAt = createdAt;
        this.nodesJson = nodesJson;
        this.taskId = taskId;
        this.name = name;
        this.minFlyHeight = minFlyHeight;
        this.minSpeed = minSpeed;
        this.isClosed = isClosed;
        this.scaleFactor = scaleFactor;
    }

    @Generated(hash = 1603132182)
    public FlyPlan() {
    }

    public FlyPlan(Task task) {
        this.task = task;
        this.taskId = task.getId();
        this.projectId = Long.valueOf(task.getProject());
    }

    public static FlyPlan loadFromJsonFile(String jsonFileContent) {
        return new Gson().fromJson(jsonFileContent, FlyPlan.class);
    }

    public Uri getImageFolderUrl() {
        return imageFolderUrl;
    }

    public void setImageFolderUrl(Uri imageFolderUrl) {
        this.imageFolderUrl = imageFolderUrl;
    }

    public Path getPathRoute(float xCorrection, float yCorrection) {
        Path path = new Path();
        int count = 1;
        for (ListIterator<Waypoint> it = getPoints().getWaypoints().listIterator(); it.hasNext(); ) {
            Waypoint waypoint = it.next();
            Coordinate coordinate = waypoint.getShape().getCoordinate();
            if (count == 1) {
                path.moveTo(coordinate.getX() - xCorrection, coordinate.getY() - yCorrection);
            } else {
                path.lineTo(coordinate.getX() - xCorrection, coordinate.getY() - yCorrection);
            }
            count++;
        }
        return path;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void toggleClosedOrOpen() {
        isClosed = !isClosed;
    }

    public void draw(Canvas canvas) {
        getPoints().getWaypoints().setClosed(isClosed);
        getPoints().draw(canvas);
    }

    public String saveToJsonFile() {
        return new Gson().toJson(this);
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public GoogleMapExtension getMap() {
        return map;
    }

    public void setMap(GoogleMapExtension map) {
        this.map = map;
        getPoints().setMap(map);
    }

    public Nodes getPoints() {
        if (nodes == null && !StringUtil.isNullOrEmpty(nodesJson))
            nodes.loadNodes(nodesJson);
        else if (nodes == null)
            nodes = new Nodes();
        return nodes;
    }

    public int getMinFlyHeight() {
        return minFlyHeight;
    }

    public void setMinFlyHeight(int minFlyHeight) {
        this.minFlyHeight = minFlyHeight;
    }

    public int getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(int minSpeed) {
        this.minSpeed = minSpeed;
    }

    public UnitOfLength getUnitOfLength() {
        return unitOfLength;
    }

    /* PARCELABLE PART */

    public void setUnitOfLength(UnitOfLength unitOfLength) {
        this.unitOfLength = unitOfLength;
        // todo convert all unitOfLength values and reload
    }

    public String getName() {
        if (task != null)
            return task.getName();
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Task getTask() {
        if (task != null)
            return task;
        else if (!StringUtil.isNullOrEmpty(taskSerialized))
            return gson.fromJson(taskSerialized, Task.class); // deserialized Task
        return new Task(); // dummy empty task
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getTaskSerialized() {
        return taskSerialized;
    }

    public void setTaskSerialized(String taskSerialized) {
        this.taskSerialized = taskSerialized;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id == null ? 0 : id);
        dest.writeSerializable(mapData);
        //dest.writeSerializable(nodes);
        dest.writeInt(minFlyHeight);
        dest.writeInt(minSpeed);
        dest.writeString(unitOfLength.name());
        dest.writeString(name);
        dest.writeString(nodesJson);
        dest.writeParcelable(task, flags);
        dest.writeLong(projectId == null ? 0 : projectId);
        dest.writeString(taskSerialized);
        dest.writeByte((byte) (isClosed ? 1 : 0));
        dest.writeFloat(scaleFactor);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getMapDataId() {
        return this.mapDataId;
    }

    public void setMapDataId(Long mapDataId) {
        this.mapDataId = mapDataId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1654636707)
    public Project getProject() {
        Long __key = this.projectId;
        if (project__resolvedKey == null || !project__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProjectDao targetDao = daoSession.getProjectDao();
            Project projectNew = targetDao.load(__key);
            synchronized (this) {
                project = projectNew;
                project__resolvedKey = __key;
            }
        }
        return project;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1207172963)
    public void setProject(@NotNull Project project) {
        if (project == null) {
            throw new DaoException("To-one property 'projectId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.project = project;
            projectId = project.getId();
            project__resolvedKey = projectId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1443398968)
    public MapData getMapData() {
        Long __key = this.mapDataId;
        if (mapData__resolvedKey == null || !mapData__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapDataDao targetDao = daoSession.getMapDataDao();
            MapData mapDataNew = targetDao.load(__key);
            synchronized (this) {
                mapData = mapDataNew;
                mapData__resolvedKey = __key;
            }
        }
        return mapData;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 125145966)
    public void setMapData(MapData mapData) {
        synchronized (this) {
            this.mapData = mapData;
            mapDataId = mapData == null ? null : mapData.getId();
            mapData__resolvedKey = mapDataId;
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

    public String getNodesJson() {
        return this.nodesJson;
    }

    public void setNodesJson(String nodesJson) {
        this.nodesJson = nodesJson;
    }

    public boolean getIsClosed() {
        return this.isClosed;
    }

    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 608845545)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFlyPlanDao() : null;
    }

    public static class UnitOfLengthConverter implements PropertyConverter<UnitOfLength, String> {
        @Override
        public UnitOfLength convertToEntityProperty(String databaseValue) {
            return UnitOfLength.valueOf(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(UnitOfLength entityProperty) {
            return entityProperty.name();
        }
    }
}
