package com.vikram.xlauncher.models;

import android.graphics.drawable.Drawable;

public class AppModel {
    private String appName;
    private Drawable appImage;
    private double version;
    private String packageName;

    // Constructor
    public AppModel(String appName, Drawable appImage, double version, String packageName) {
        this.appName = appName;
        this.appImage = appImage;
        this.version = version;
        this.packageName = packageName;
    }

    // Getters and setters
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppImage() {
        return appImage;
    }

    public void setAppImage(Drawable appImage) {
        this.appImage = appImage;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    // toString method to represent object as string
    @Override
    public String toString() {
        return "AppModel{" +
                "appName='" + appName + '\'' +
                ", appImage='" + appImage + '\'' +
                ", version=" + version +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}