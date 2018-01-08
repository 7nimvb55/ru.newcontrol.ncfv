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
public class NcIdxStorageWordFileReader {

    /**
     *
     * @param inFuncFile
     * @return
     */
    public static TreeMap<Long, NcDcIdxStorageWordToFile> ncReadFileContainedId(File inFuncFile){
        TreeMap<Long, NcDcIdxStorageWordToFile> ncReadedData;
        
        if ( !NcIdxFileManager.fileExistRWAccessChecker(inFuncFile)){
            return new TreeMap<Long, NcDcIdxStorageWordToFile>();
        };
        //mcGetWorkCfgDirName() + workFileNames[0];
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inFuncFile)))
        {
            ncReadedData = (TreeMap<Long, NcDcIdxStorageWordToFile>)ois.readObject();
        }
        catch(Exception ex){
            Logger.getLogger(NcIdxStorageWordFileReader.class.getName()).log(Level.SEVERE, null, ex); 
            return new TreeMap<Long, NcDcIdxStorageWordToFile>();
        } 
        return ncReadedData;
    }
}
