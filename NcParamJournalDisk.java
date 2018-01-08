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
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Администратор
 */
public class NcParamJournalDisk {

    /**
     *
     * @return
     */
    public static TreeMap<Long, NcDiskInfo> getFromJournalDiskOrCreateIt(){
        TreeMap<Long, NcDiskInfo> readedFromFileDiskInfo;
        if( !fileJournalDiskExist() ){
            TreeMap<Long, NcDiskInfo> sysDisk = NcDiskUtils.getDiskInfo();
            int count = fileJournalDiskWrite(sysDisk);
            return sysDisk;
        }
        readedFromFileDiskInfo = fileJournalDiskRead();
        if( needToUpdateJournalDisk(readedFromFileDiskInfo) ){
            if ( updateRecordInJournalDisk() ){
                readedFromFileDiskInfo = fileJournalDiskRead();
            }
        }
        return readedFromFileDiskInfo;
    }

    /**
     *
     * @param inFuncDiskInfo
     * @return
     */
    public static boolean needToUpdateJournalDisk(TreeMap<Long, NcDiskInfo> inFuncDiskInfo){
        FileSystem fs = FileSystems.getDefault();
        long tmpTotalSpace = 0;
        boolean totalSpaceDisk = false;
        boolean totalName = false;
        String fsName = "";
        for( FileStore itemFS : fs.getFileStores() ){
            try {
                tmpTotalSpace = itemFS.getTotalSpace();
            } catch (IOException ex) {
                Logger.getLogger(NcParamJournalDisk.class.getName()).log(Level.SEVERE, null, ex);
            }
            fsName = itemFS.name();
            for( Map.Entry<Long, NcDiskInfo> itemInFuncDisk : inFuncDiskInfo.entrySet() ){
                totalSpaceDisk = totalSpaceDisk || (tmpTotalSpace == itemInFuncDisk.getValue().totalSpace);
                totalName = totalName || ( fsName.equalsIgnoreCase(itemInFuncDisk.getValue().strFileStoreName) );
            }
            if( !totalSpaceDisk || !totalName ){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    public static boolean updateRecordInJournalDisk(){
        TreeMap<Long, NcDiskInfo> readedFromFileDiskInfo;
        TreeMap<Long, NcDiskInfo> writeToFileDiskInfo;
        readedFromFileDiskInfo = fileJournalDiskRead();
        writeToFileDiskInfo = appendRecordInJournalDisk(readedFromFileDiskInfo);
        if( readedFromFileDiskInfo.size() == writeToFileDiskInfo.size() ){
            int count = fileJournalDiskWrite(writeToFileDiskInfo);
            return count > 0;
        }
        return false;
    }

    /**
     *
     * @param addRecordToFileDiskInfo
     * @return
     */
    public static boolean addRecordInJournalDisk(NcDiskInfo addRecordToFileDiskInfo){
        TreeMap<Long, NcDiskInfo> readedFromFileDiskInfo;
        TreeMap<Long, NcDiskInfo> writeToFileDiskInfo;
        writeToFileDiskInfo = new TreeMap<Long, NcDiskInfo>();
        readedFromFileDiskInfo = fileJournalDiskRead();
        writeToFileDiskInfo.putAll(readedFromFileDiskInfo);
        writeToFileDiskInfo.put(readedFromFileDiskInfo.lastKey() + 1, addRecordToFileDiskInfo);
        if( readedFromFileDiskInfo.size() == writeToFileDiskInfo.size() ){
            int count = fileJournalDiskWrite(writeToFileDiskInfo);
            return count > 0;
        }
        return false;
    }

    /**
     *
     * @param diskUserAlias
     * @return
     */
    public static boolean updateUserAliasInJournalDisk(TreeMap<Integer, String> diskUserAlias){
        TreeMap<Long, NcDiskInfo> readedFromFileDiskInfo;
        TreeMap<Long, NcDiskInfo> writeToFileDiskInfo;
        readedFromFileDiskInfo = fileJournalDiskRead();
        writeToFileDiskInfo = new TreeMap<Long, NcDiskInfo>();
        writeToFileDiskInfo.putAll(readedFromFileDiskInfo);
        boolean boolAliasChanged = false;
        for( Map.Entry<Integer, String> itemAlias : diskUserAlias.entrySet() ){
            NcDiskInfo toChangeDiskInfo = writeToFileDiskInfo.get((long) itemAlias.getKey());
            if( toChangeDiskInfo != null ){
                toChangeDiskInfo.humanAlias = itemAlias.getValue();
                writeToFileDiskInfo.put((long) itemAlias.getKey(), toChangeDiskInfo);
                boolAliasChanged = true;
            }
            toChangeDiskInfo = null;
        }
        
        
        if( boolAliasChanged ){
            int count = fileJournalDiskWrite(writeToFileDiskInfo);
            return count > 0;
        }
        return false;
    }

    /**
     *
     * @param inFuncDiskInfo
     * @return
     */
    public static TreeMap<Long, NcDiskInfo> appendRecordInJournalDisk(TreeMap<Long, NcDiskInfo> inFuncDiskInfo){
        TreeMap<Long, NcDiskInfo> sysDisk = NcDiskUtils.getDiskInfo();
        TreeMap<Long, NcDiskInfo> listToWriteDiskInfo = new TreeMap<Long, NcDiskInfo>();
        TreeMap<Long, NcDiskInfo> forAppend = new TreeMap<Long, NcDiskInfo>();
        boolean checkOne = false;
        boolean checkTwo = false;
        boolean checkTree = false;
        boolean needCheckTree = false;
        boolean checkFour = false;
        listToWriteDiskInfo.putAll(inFuncDiskInfo);
        for( Map.Entry<Long, NcDiskInfo> itemInFuncDisk : inFuncDiskInfo.entrySet() ){
            for( Map.Entry<Long, NcDiskInfo> itemSysDisk : sysDisk.entrySet() ){
                if( itemSysDisk.getValue().strFileStoreName.equalsIgnoreCase(itemInFuncDisk.getValue().strFileStoreName) ){
                    checkOne = true;
                }
                if( itemInFuncDisk.getValue().diskFStype == itemSysDisk.getValue().diskFStype ){
                    checkTwo = true;
                }
                if( itemInFuncDisk.getValue().totalSpace == itemSysDisk.getValue().totalSpace ){
                    checkTree = true;
                }
                if( itemInFuncDisk.getValue().longSerialNumber != 0 ){
                    needCheckTree = true;
                    if ( itemInFuncDisk.getValue().longSerialNumber == itemSysDisk.getValue().longSerialNumber ){
                        checkFour = true;
                    }
                }
                if( (!checkOne) && (!checkTwo) && (!checkTree) ){
                    itemSysDisk.getValue().diskID = listToWriteDiskInfo.lastKey() + 1;
                    forAppend.put(itemSysDisk.getValue().diskID, itemSysDisk.getValue());
                }
            }
            if( (!checkOne) && (!checkTwo) && (!checkTree) ){
                    listToWriteDiskInfo.putAll(forAppend);
            }
            forAppend.clear();
            checkOne = false;
            checkTwo = false;
            checkTree = false;
            needCheckTree = false;
            checkFour = false;
        }
        return listToWriteDiskInfo;
    }

    /**
     *
     * @return
     */
    public static boolean fileJournalDiskExist(){
        String strDataInAppDir = NcIdxFileManager.getJournalDiskPath();
        File fileJornalDisk = new File(strDataInAppDir);
        if( !NcIdxFileManager.fileExistRWAccessChecker(fileJornalDisk) ){
            return false;
        }
        return true;
    }

    /**
     *
     * @param inFuncSysDisk
     * @return
     */
    public static int fileJournalDiskWrite(TreeMap<Long, NcDiskInfo> inFuncSysDisk){
        String strDataInAppDir = NcIdxFileManager.getJournalDiskPath();
        
        if( !NcDiskUtils.isDiskInfoRecordsHashTure(inFuncSysDisk) ){
            NcAppHelper.appExitWithMessage("Can't write disk info to journal, error in records hash");
            return -1;
        }
        try(ObjectOutputStream oos = 
                new ObjectOutputStream(
                new FileOutputStream(strDataInAppDir)))
        {
            oos.writeObject(inFuncSysDisk);
        }
        catch(Exception ex){
            Logger.getLogger(NcParamJournalDisk.class.getName()).log(Level.SEVERE, null, ex); 
            return -1;
        } 
        return inFuncSysDisk.size();
    }

    /**
     *
     * @return
     */
    public static TreeMap<Long, NcDiskInfo> fileJournalDiskRead(){
        TreeMap<Long, NcDiskInfo> readedDiskInfo;
        String strDataInAppDir = NcIdxFileManager.getJournalDiskPath();
        File fileJornalDisk = new File(strDataInAppDir);
        if( !NcIdxFileManager.fileExistRWAccessChecker(fileJornalDisk) ){
            return new TreeMap<Long, NcDiskInfo>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(strDataInAppDir)))
        {
            readedDiskInfo = (TreeMap<Long, NcDiskInfo>)ois.readObject();
            if( !NcDiskUtils.isDiskInfoRecordsHashTure(readedDiskInfo) ){
                NcAppHelper.appExitWithMessage("Can't read disk info from journal, error in records hash");
            }
        }
        catch(Exception ex){
            Logger.getLogger(NcParamJournalDisk.class.getName()).log(Level.SEVERE, null, ex); 
            return new TreeMap<Long, NcDiskInfo>();
        } 
        return readedDiskInfo;
    }
}
