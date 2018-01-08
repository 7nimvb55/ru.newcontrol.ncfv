/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.io.Serializable;

/**
 * DiskFStype - FileStore.type()
 * @author Администратор
 */
public class NcDiskInfo implements Serializable{
/**
 * diskID - Identification descriptor (ID) for records about disk in Index
 */    
    public long diskID;

    /**
     *
     */
    public long longSerialNumber;

    /**
     *
     */
    public String strHexSerialNumber;

    /**
     *
     */
    public String strFileStore;

    /**
     *
     */
    public String strFileStoreName;

    /**
     *
     */
    public char diskLetter;

    /**
     *
     */
    public String diskFStype;

    /**
     *
     */
    public String programAlias;

    /**
     *
     */
    public String humanAlias;

    /**
     *
     */
    public long totalSpace;

    /**
     *
     */
    public long usedSpace;

    /**
     *
     */
    public long availSpace;

    /**
     *
     */
    public long unAllocatedSpace;

    /**
     *
     */
    public boolean isReadonly;

    /**
     *
     */
    public long recordCreationTime;

    /**
     *
     */
    public int reordHash;
    /**
     * 
     * @param diskID
     * @param longSerialNumber
     * @param strHexSerialNumber
     * @param strFileStore
     * @param strFileStoreName
     * @param diskLetter
     * @param diskFStype
     * @param programAlias
     * @param humanAlias
     * @param totalSpace
     * @param usedSpace
     * @param availSpace
     * @param unAllocatedSpace
     * @param isReadonly 
     */

    public NcDiskInfo(long diskID,
            long longSerialNumber,
            String strHexSerialNumber,
            String strFileStore,
            String strFileStoreName,
            char diskLetter,
            String diskFStype,
            String programAlias,
            String humanAlias,
            long totalSpace,
            long usedSpace,
            long availSpace,
            long unAllocatedSpace,
            boolean isReadonly) {
        long longCreationTime = System.nanoTime();
        this.diskID = diskID;
        this.longSerialNumber = longSerialNumber;
        this.strHexSerialNumber = strHexSerialNumber;
        this.strFileStore = strFileStore;
        this.strFileStoreName = strFileStoreName;
        this.diskLetter = diskLetter;
        this.diskFStype = diskFStype;
        this.programAlias = programAlias;
        this.humanAlias = humanAlias;
        this.totalSpace = totalSpace;
        this.usedSpace = usedSpace;
        this.availSpace = availSpace;
        this.unAllocatedSpace = unAllocatedSpace;
        this.isReadonly = isReadonly;
        this.recordCreationTime = longCreationTime;
        this.reordHash = (""
            + diskID
            + longSerialNumber
            + strHexSerialNumber
            + strFileStore
            + strFileStoreName
            + diskLetter
            + diskFStype
            + programAlias
            + humanAlias
            + totalSpace
            + usedSpace
            + availSpace
            + unAllocatedSpace
            + isReadonly
            + longCreationTime).hashCode();
    }
}
