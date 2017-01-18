package com.drone.imavis.uav.control.activity;

import com.parrot.arsdk.ardiscovery.UsbAccessoryActivity;

public class UsbAccessoryActivityImpl extends UsbAccessoryActivity
{
    @Override
    protected Class getBaseActivity() {
        return DeviceListActivity.class;
    }
}
