/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Администратор
 */
public class NcIdxDirListFileReader {
    /**
     * Directory List
     * @param dirListID
     * @return 
     */ 
    public static TreeMap<Long, NcDcIdxDirListToFileAttr> ncReadFromDirListFile(long dirListID){
        TreeMap<Long, NcDcIdxDirListToFileAttr> ncDataFromDirList;
        String strCfgPath = NcIdxFileManager.getFileNameToRecord(NcManageCfg.getDirList().getAbsolutePath()+"/dl", dirListID);

        if ( !NcIdxFileManager.fileExistRWAccessChecker(new File(strCfgPath))){
            return new TreeMap<>();
        }
        
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(strCfgPath)))
        {
            ncDataFromDirList = (TreeMap<Long, NcDcIdxDirListToFileAttr>)ois.readObject();
        }
        catch(Exception ex){
            Logger.getLogger(NcIdxDirListFileReader.class.getName()).log(Level.SEVERE, null, ex); 
            return new TreeMap<>();
        } 
        return ncDataFromDirList;
    }

    /**
     *
     * @param dirListID
     * @return
     */
    public static TreeMap<Long, NcDcIdxDirListToFileExist> ncReadFromDirListExist(long dirListID){
        TreeMap<Long, NcDcIdxDirListToFileExist> ncDataFromDirList;
        String strCfgPath = NcIdxFileManager.getFileNameToRecord(NcManageCfg.getDirListExist().getAbsolutePath() + "/e", dirListID);

        if ( !NcIdxFileManager.fileExistRWAccessChecker(new File(strCfgPath))){
            return new TreeMap<>();
        }
        
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(strCfgPath)))
        {
            ncDataFromDirList = (TreeMap<Long, NcDcIdxDirListToFileExist>)ois.readObject();
        }
        catch(Exception ex){
            Logger.getLogger(NcIdxDirListFileReader.class.getName()).log(Level.SEVERE, null, ex); 
            return new TreeMap<>();
        } 
        return ncDataFromDirList;
    }
    public static TreeMap<Long, NcDcIdxDirListToFileAttr> ncReadFromDirListFileByName(String strCfgPath){
        TreeMap<Long, NcDcIdxDirListToFileAttr> ncDataFromDirList;
        

        if ( !NcIdxFileManager.fileExistRWAccessChecker(new File(strCfgPath))){
            return new TreeMap<>();
        }
        
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(strCfgPath)))
        {
            ncDataFromDirList = (TreeMap<Long, NcDcIdxDirListToFileAttr>)ois.readObject();
        }
        catch(Exception ex){
            Logger.getLogger(NcIdxDirListFileReader.class.getName()).log(Level.SEVERE, null, ex); 
            return new TreeMap<>();
        } 
        return ncDataFromDirList;
    }
}
