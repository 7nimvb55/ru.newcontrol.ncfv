/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.io.Serializable;

/**
 *
 * @author Администратор
 */
public class NcDcIdxDirListToFileExist implements Serializable {
    /**
     * 
     */    
    public long dirListID;
    /**
     * For example:
     * <p>method of class 
     * {@link ru.newcontrol.ncfv.NcDiskUtils#getDiskIDbyLetterTotalSpace NcDiskUtils.getDiskIDbyLetterTotalSpace()}
     * - returned ID for Disks in system now (Not from temporary saved information)
     * class {@link ru.newcontrol.ncfv.NcDiskInfo#diskID NcDiskInfo}</p>
     * <p>property of class
     * {@link ru.newcontrol.ncfv.NcManageCfg#arrDiskInfo NcManageCfg.arrDiskInfo}
     * - contained information about disks on the system
     * </p>
     */    
    public long diskID;
    /**
     * 
     */    
    public String pathWithOutDiskLetter;
    /**
     * 
     */    
    public int pathHash;
    /**
     * 
     */ 
    public long nanoTimeStartAddToIndex;
    /**
     * 
     */ 
    public long nanoTimeEndAddToIndex;

    /**
     *
     */
    public long recordTime;

    /**
     *
     */
    public long recordHash;
    /**
     * 
     * @param dirListID
     * @param diskID
     * @param nanoTimeStartAddToIndex 
     */
    public NcDcIdxDirListToFileExist() {
        this.dirListID = -777;
        this.diskID = -777;
        this.pathWithOutDiskLetter = "";
        this.pathHash = -777;
        this.nanoTimeStartAddToIndex = -777;
        this.nanoTimeEndAddToIndex = -777;
        long nowTime = System.nanoTime();
        this.recordTime = nowTime;
        this.recordHash = (""
            + this.dirListID
            + this.diskID
            + this.pathWithOutDiskLetter
            + this.pathHash
            + this.nanoTimeStartAddToIndex
            + this.nanoTimeEndAddToIndex
            + nowTime).hashCode();
    }

    /**
     *
     * @param dirListID
     * @param diskID
     * @param pathWithOutDiskLetter
     * @param nanoTimeStartAddToIndex
     * @param nanoTimeEndAddToIndex
     */
    public NcDcIdxDirListToFileExist(
            long dirListID,
            long diskID,
            String pathWithOutDiskLetter,
            long nanoTimeStartAddToIndex,
            long nanoTimeEndAddToIndex) {
        this.dirListID = dirListID;
        this.diskID = diskID;
        this.pathWithOutDiskLetter = pathWithOutDiskLetter;
        this.pathHash = pathWithOutDiskLetter.hashCode();
        this.nanoTimeStartAddToIndex = nanoTimeStartAddToIndex;
        this.nanoTimeEndAddToIndex = nanoTimeEndAddToIndex;
        long nowTime = System.nanoTime();
        this.recordTime = nowTime;
        this.recordHash = (""
            + this.dirListID
            + this.diskID
            + this.pathWithOutDiskLetter
            + this.pathHash
            + this.nanoTimeStartAddToIndex
            + this.nanoTimeEndAddToIndex
            + nowTime).hashCode();
    }
    
}
