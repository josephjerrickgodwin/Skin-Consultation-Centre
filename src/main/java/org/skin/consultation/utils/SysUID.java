package org.skin.consultation.utils;

import java.util.UUID;

public final class SysUID {
    private final String UID;

    // Constructor of the class
    public SysUID() {
        this.UID = getUniqueId();
    }

    // Generate a unique ID
    private static String getUniqueId() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "").substring(0, uuid.length() - 10);
    }

    // Implement a getter method
    public String getUID() {return UID;}
}
