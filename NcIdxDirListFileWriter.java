/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Администратор
 */
public class NcIdxDirListFileWriter {
/**
 * Directory List
     * @param ncDataToDirListFile
     * @param recID
 * @return 
 */    
    public static int ncWriteToDirListFile(TreeMap<Long, NcDcIdxDirListToFileAttr> ncDataToDirListFile, long recID){
        if( ncDataToDirListFile == null ){
            return -1;
        }
        try(ObjectOutputStream oos = 
                new ObjectOutputStream(
                new FileOutputStream(NcIdxFileManager.getFileNameToRecord(NcManageCfg.getDirList().getAbsolutePath()+"/dl",recID))))
        {
            oos.writeObject(ncDataToDirListFile);
        }
        catch(Exception ex){
            Logger.getLogger(NcIdxDirListFileWriter.class.getName()).log(Level.SEVERE, null, ex); 
            return -1;
        } 
        return ncDataToDirListFile.size();
    }

    /**
     *
     * @param ncDataToDirListFile
     * @param dirListID
     * @return
     */
    public static int ncWriteToDirListExist(TreeMap<Long, NcDcIdxDirListToFileExist> ncDataToDirListFile, long dirListID){
        String strCfgPath = NcIdxFileManager.getFileNameToRecord(NcManageCfg.getDirListExist().getAbsolutePath() + "/e", dirListID);
        if( ncDataToDirListFile == null ){
            return -1;
        }
        try(ObjectOutputStream oos = 
                new ObjectOutputStream(
                new FileOutputStream(strCfgPath)))
        {
            oos.writeObject(ncDataToDirListFile);
        }
        catch(Exception ex){
            Logger.getLogger(NcIdxDirListFileWriter.class.getName()).log(Level.SEVERE, null, ex); 
            return -1;
        } 
        return ncDataToDirListFile.size();
    }
}
