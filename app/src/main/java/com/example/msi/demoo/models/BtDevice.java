package com.example.msi.demoo.models;

import android.bluetooth.BluetoothDevice;

public class BtDevice {
    private BluetoothDevice bluetoothDevice;
    private boolean connectStatu;
    private String connectInfo;


    public BtDevice(BluetoothDevice bluetoothDevice, boolean connectStatu, String connectInfo) {
        this.bluetoothDevice = bluetoothDevice;
        this.connectStatu = connectStatu;
        this.connectInfo = connectInfo;
    }


    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }
    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public boolean isConnectStatu() {
        return connectStatu;
    }
    public void setConnectStatu(boolean connectStatu) {
        this.connectStatu = connectStatu;
    }

    public String getConnectInfo() {
        return connectInfo;
    }
    public void setConnectInfo(String connectInfo) {
        this.connectInfo = connectInfo;
    }
}
