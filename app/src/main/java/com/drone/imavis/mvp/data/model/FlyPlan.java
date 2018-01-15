package com.drone.imavis.mvp.data.model;

import android.graphics.Canvas;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.drone.imavis.mvp.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.Nodes;
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
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Date;
import java.util.List;

/**
 * Created by Adrian on 26.11.2016.
 */

@Entity(indexes = {
        @Index(value = "name", unique = true)
})
public class FlyPlan implements Parcelable {

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
    private Nodes nodes = new Nodes();

    private String nodesJson;

    private Long taskId;

    @Transient
    private Task task;

    @NotNull private String name; // task name
    @NotNull private int minFlyHeight = CFlyPlan.MIN_FLY_HEIGHT;
    @NotNull private int minSpeed = CFlyPlan.MIN_SPEED;

    @ToMany(referencedJoinProperty = "id")
    private List<com.drone.imavis.mvp.data.model.Waypoint> waypoints;

    @ToMany(referencedJoinProperty = "id")
    private List<com.drone.imavis.mvp.data.model.PointOfInterest> pointOfInterests;

    public Uri getImageFolderUrl() {
        return imageFolderUrl;
    }

    public void setImageFolderUrl(Uri imageFolderUrl) {
        this.imageFolderUrl = imageFolderUrl;
    }

    public void draw(Canvas canvas) {
        int selectedWaypointIndex = this.getPoints().getWaypoints().draw(canvas);
        int selectedPoiIndex = this.getPoints().getPointOfInterests().draw(canvas);
        int selectedWaypointId = selectedWaypointIndex + 1;
        int selectedPoiId = selectedPoiIndex + 1;
        com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoint selectedWaypoint = FlyPlanController.getSelectedWaypoint();
        com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterest selectedPOI = FlyPlanController.getSelectedPOI();

        // draw selectedWaypoint
        if(selectedWaypoint != null) {
            selectedWaypoint.setShapeSelectedPaint();
            selectedWaypoint.draw(canvas, String.valueOf(selectedWaypointId), selectedWaypointId);
        }
        // draw selectedPOI
        if(selectedPOI != null) {
            selectedPOI.setShapeSelectedPaint();
            selectedPOI.getShape().setBackgroundColor(this.getPoints().getPointOfInterests().getPoiColorById(selectedPoiIndex));
            selectedPOI.draw(canvas, String.valueOf(selectedPoiId));
        }
    }

    public void setNodesJson(String nodesJson) {
        this.nodesJson = nodesJson;
    }

    public static FlyPlan loadFromJsonFile(String jsonFileContent) {
        return new Gson().fromJson(jsonFileContent, FlyPlan.class);
    }
    public String saveToJsonFile() {
        return new Gson().toJson(this);
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
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
        nodes.setMap(map);
    }

    public Nodes getPoints() {
        if(nodes != null && (nodes.getWaypoints().size() == 0 && nodes.getPointOfInterests().size() == 0) && (nodesJson != null || nodesJson != "")) {
            nodes.loadNodes(nodesJson);
        }
        else if (nodes == null)
            nodes = new Nodes();

        return nodes;
    }
    private void setPoints(Nodes points) {
        this.nodes = nodes;
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
    public void setUnitOfLength(UnitOfLength unitOfLength) {
        this.unitOfLength = unitOfLength;
        // todo convert all unitOfLength values and reload
    }

    public String getName() {
        if(task != null)
            return task.getName();
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Task getTask() {
        return task;
    }
    public void setTask(Task task) {
        this.task = task;
    }

    /* PARCELABLE PART */

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

    /** To-one relationship, resolved on first access. */
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

    /** called by internal mechanisms, do not call yourself. */
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

    /** To-one relationship, resolved on first access. */
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 125145966)
    public void setMapData(MapData mapData) {
        synchronized (this) {
            this.mapData = mapData;
            mapDataId = mapData == null ? null : mapData.getId();
            mapData__resolvedKey = mapDataId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1597059416)
    public List<Waypoint> getWaypoints() {
        if (waypoints == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WaypointDao targetDao = daoSession.getWaypointDao();
            List<Waypoint> waypointsNew = targetDao._queryFlyPlan_Waypoints(id);
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
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 887659655)
    public List<PointOfInterest> getPointOfInterests() {
        if (pointOfInterests == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PointOfInterestDao targetDao = daoSession.getPointOfInterestDao();
            List<PointOfInterest> pointOfInterestsNew = targetDao._queryFlyPlan_PointOfInterests(id);
            synchronized (this) {
                if (pointOfInterests == null) {
                    pointOfInterests = pointOfInterestsNew;
                }
            }
        }
        return pointOfInterests;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 503561242)
    public synchronized void resetPointOfInterests() {
        pointOfInterests = null;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 608845545)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFlyPlanDao() : null;
    }

    /**
     * Constructs a Project from a Parcel
     * @param parcelIn Source Parcel
     */
    public FlyPlan (Parcel parcelIn) {
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
    }

    @Generated(hash = 111111680)
    public FlyPlan(Long id, UnitOfLength unitOfLength, @NotNull Long projectId, Long mapDataId, Date createdAt, String nodesJson, Long taskId,
            @NotNull String name, int minFlyHeight, int minSpeed) {
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
    }

    @Generated(hash = 1603132182)
    public FlyPlan() {
    }

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

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 500138044)
    private transient FlyPlanDao myDao;

    @Generated(hash = 1005767482)
    private transient Long project__resolvedKey;

    @Generated(hash = 1887247556)
    private transient Long mapData__resolvedKey;

    /* GreenDAO CONVERTER */

    static class UnitOfLengthConverter implements PropertyConverter<UnitOfLength, String> {
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
