package com.drone.imavis.mvp.services.dronecontrol;

public class MavlinkFileInfo {
    private String filePath;
    private int missionItems;

    public MavlinkFileInfo(String filePath, int missionItems) {
        this.filePath = filePath;
        this.missionItems = missionItems;
    }

    public String getFilePath() {
        return filePath;
    }

    /*public void setFilePath(String filePath) {
        this.filePath = filePath;
    }*/

    public int getMissionItems() {
        return missionItems;
    }

    /*public void setMissionItems(int missionItems) {
        this.missionItems = missionItems;
    }*/
}
