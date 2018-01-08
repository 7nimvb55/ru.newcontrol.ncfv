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
public class NcIdxLongWordListFileReader {
 /**
 * Directory List Word Long
     * @param dataForRead
     * @param rID
 * @return 
 */      
    public static TreeMap<Long, NcDcIdxLongWordListToFile> ncReadFileContainedId(NcDcIdxLongWordListToFile dataForRead, long rID){
        TreeMap<Long, NcDcIdxLongWordListToFile> ncDataFromDirList;
        String strCfgPath = NcIdxFileManager.getFileNameToRecord(
                NcManageCfg.getDirLongWordList().getAbsolutePath() + "/wl-"
                + dataForRead.name.substring(0, 4),rID);
        if ( !NcIdxFileManager.fileExistRWAccessChecker(new File(strCfgPath))){
            return new TreeMap<Long, NcDcIdxLongWordListToFile>();
        };
        //mcGetWorkCfgDirName() + workFileNames[0];
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(strCfgPath)))
        {
            ncDataFromDirList = (TreeMap<Long, NcDcIdxLongWordListToFile>)ois.readObject();
        }
        catch(Exception ex){
            Logger.getLogger(NcIdxLongWordListFileReader.class.getName()).log(Level.SEVERE, null, ex); 
            return new TreeMap<Long, NcDcIdxLongWordListToFile>();
        } 
        return ncDataFromDirList;
    }
    
}
