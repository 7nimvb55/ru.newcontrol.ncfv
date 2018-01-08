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
public class NcDcIdxDirListToFileType implements Serializable {
    /**
     * dirListID - identification descriptor (ID) of records in file
     */
    public long dirListID;
    /**
     * fileContentType - String Files.probeContentType()
     */
    public String fileContentType;

    /**
     *
     */
    public int fileContentTypeHash;

    /**
     *
     */
    public String fileExtention;

    /**
     *
     */
    public int fileExtentionHash;

    /**
     *
     */
    public long recordTime;

    /**
     *
     */
    public int recordHash;
    /**
     * 
     * @param fileContentType 
     */
    public NcDcIdxDirListToFileType() {
        this.dirListID = -777;
        this.fileContentType = "";
        this.fileContentTypeHash = -777;
        this.fileExtention = "";
        this.fileExtentionHash = -777;
        long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                ""
                + this.dirListID
                + this.fileContentType
                + this.fileContentTypeHash
                + this.fileExtention
                + this.fileExtentionHash
                + nowSysTime).hashCode();
    }

    /**
     *
     * @param dirListID
     * @param fileExtention
     * @param fileContentType
     */
    public NcDcIdxDirListToFileType(long dirListID, String fileExtention, String fileContentType) {
        this.dirListID = dirListID;
        this.fileContentType = fileContentType;
        this.fileContentTypeHash = fileContentType.hashCode();
        this.fileExtention = fileExtention;
        this.fileExtentionHash = fileExtention.hashCode();
        long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                ""
                + this.dirListID
                + this.fileContentType
                + this.fileContentTypeHash
                + this.fileExtention
                + this.fileExtentionHash
                + nowSysTime).hashCode();
    }
}
