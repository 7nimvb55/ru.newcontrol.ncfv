/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.io.Serializable;

/**
 * class not avalable now, this is for latest releases
 * journalname - name of current journal file
 * jid - last id for journal files
 * provided by class NcDirListToFilesForIndex
 * listname - name of current work file for Directory List File
 * lnid - last id for Directory List File
 * provided by class NcDirListToFilesHashes
 * hashlistname - name of current work file for Directory List Files Hashes
 * hlnid - last id for Directory List File Hashes
 * provided by class NcLongWord
 * longwordlistname - name of current work file for Directory List File for long world
 * lwlnid - last id for Directory List File for long world
 * @author Администратор
 */
public class NcTmpNowProcessInfo implements Serializable {
    /** Not used and released in this version */
    public String journalname;
    /** Not used and released in this version */
    public long journalid;
    /** hashlistname - last name for Directory List File 
     * class NcDirListToFilesForIndex
     */
    public String listname;
    /** hashlistname - last name for Directory List File 
     * class NcDirListToFilesForIndex
     */
    public long listnameid;
    /** hashlistname - last name for Directory List File Hashes 
     * class NcDirListToFilesHashes
     */
    public String hashlistname;
    /** hashlistnnameid - last id for Directory List File Hashes 
     * class NcDirListToFilesHashes
     */
    public long hashlistnameid;
    /** longwordlistname - last file name for index of files contained data for long world 
     * class NcLongWord
     */
    public String longwordlistname;
    /** longwordlistnameid - last id for index of files contained data for long world
     * class NcLongWord
     */
    public long longwordlistnameid;

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
     */
    public NcTmpNowProcessInfo() {
        this.journalname = "";
        this.journalid = -777;
        this.listname = "";
        this.listnameid = -777;
        this.hashlistname = "";
        this.hashlistnameid = -777;
        this.longwordlistname = "";
        this.longwordlistnameid = -777;
        long recordNowTime = System.nanoTime();
        this.recordTime = recordNowTime;
        this.recordHash = (""
                + this.journalname
                + this.journalid
                + this.listname
                + this.listnameid
                + this.hashlistname
                + this.hashlistnameid
                + this.longwordlistname
                + this.longwordlistnameid
                + this.recordTime).hashCode();
    }

    /**
     *
     * @param journalname
     * @param journalid
     * @param listname
     * @param listnameid
     * @param hashlistname
     * @param hashlistnameid
     * @param longwordlistname
     * @param longwordlistnameid
     */
    public NcTmpNowProcessInfo(String journalname,
            long journalid,
            String listname,
            long listnameid,
            String hashlistname,
            long hashlistnameid,
            String longwordlistname,
            long longwordlistnameid) {
        this.journalname = journalname;
        this.journalid = journalid;
        this.listname = listname;
        this.listnameid = listnameid;
        this.hashlistname = hashlistname;
        this.hashlistnameid = hashlistnameid;
        this.longwordlistname = longwordlistname;
        this.longwordlistnameid = longwordlistnameid;
        long recordNowTime = System.nanoTime();
        this.recordTime = recordNowTime;
        this.recordHash = (""
                + this.journalname
                + this.journalid
                + this.listname
                + this.listnameid
                + this.hashlistname
                + this.hashlistnameid
                + this.longwordlistname
                + this.longwordlistnameid
                + this.recordTime).hashCode();
    }
    
}
