/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * New control Index Managment in File System (NcIMinFS)
 * @author Администратор
 */
public class NcIMinFS {
    private NcManageCfg ncmcCfg;
    private NcIndexManageIDs ncmIDs;

    /** Work directory object*/
    private File workDir;
    /** Count Ready for record to Directory List File*/
    private int readyForRecord;
    /** Maximum count record for one file */
    private final int limitCountRecordInFiles;
    /** Current File name for write data of Directory List */
    public final String recDirListFileName;
    /** Data to record in Directory list files */
    private ArrayList<NcDcIdxDirListToFileAttr> readyForRecordData;
    static final long K = 1024;
    static final long M = 1048576;
    static final long G = 1073741824;

    /**
     *
     */
    public NcIMinFS() {
       this.limitCountRecordInFiles = 100;

       ncmcCfg = new NcManageCfg();
       workDir = ncmcCfg.ncfvdi;
       
       ncmIDs = new NcIndexManageIDs(ncmcCfg);

       long tmpLong = 0;
       String posfixName = "";
       if(tmpLong > 0){
            posfixName = Long.toString(tmpLong);
       }
       
       recDirListFileName="/dl" + posfixName + ".dat";
    }
    
    /**
     *
     * @return
     */
    public int getDiskCount(){
        
        return ncmcCfg.arrDiskInfo.size();
    }
    
    /**
     *
     * @return
     */
    public TreeMap<Long, NcDiskInfo> getDiskInfo(){
        return ncmcCfg.arrDiskInfo;
    }
    
    /**
     *
     * @return
     */
    public NcIndexManageIDs getNcIndexManageIDs(){
        return ncmIDs;
    }
    
    /**
     *
     * @return
     */
    public NcManageCfg getNcManageCfg(){
        return ncmcCfg;
    }

/**
 * LastModified Date from Directory List File, where contained record for File or Directory
 * @param indexedFile
 * @return 
 */    
    public long getIndexLastModifiedForDirectoryOrFile(File indexedFile){
        return -1;
    }
    private long writeDirListToFile(){
        readyForRecordData.clear();
        readyForRecord = readyForRecordData.size();
        return -1;
    }
    private long readDirListFormFile(){
        return -1;
    }
/**
 * Set ArrayList<NcDirListToFilesForIndex> for Ready to record in File
 * 
     * @param fReadyForRecordData
     * @return 
 */
    public int setReadyForRecordData(ArrayList<NcDcIdxDirListToFileAttr> fReadyForRecordData){
        fReadyForRecordData.trimToSize();
        readyForRecordData = fReadyForRecordData;
        readyForRecord = fReadyForRecordData.size();
        return -1;
    }
/**
 * Create TempFile with last ID record in Directory List File
 * and last ID record in Directory List File for Files Hashes
 */
/**
 * Create TempFile with list Directory List Files and his Hashes
 * provided by class NcDirListToFilesHashes
 * may be released in some perspective
 */
/**
 * Create TempFile with last ID record in Directory List File for long world
 */

}
