package com.SearingMedia.featurepager.permissions;

public class PermissionObject {
    // Variables
    String[] permission;
    int position;

    // **********************************
    // Constructor
    // **********************************
    public PermissionObject(String[] permission, int position){
        this.permission = permission;
        this.position = position;
    }

    // **********************************
    // Getters
    // **********************************
    public String[] getPermission(){
        return permission;
    }

    public int getPosition(){
        return position;
    }
}
