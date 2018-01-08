/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcParamFvReader {
    /**
     * Read data from serializable 
     * {@link ru.newcontrol.ncfv.NcParamFv}
     * saved on the disk in *.dat file
     * and return
     * @return empty object if it not read or not set before
     */
    public static NcParamFv readDataFromWorkCfg(){
        NcParamFv readedDiskInfo;
        String strDataInAppDir = NcIdxFileManager.getWorkCfgPath();
        if( strDataInAppDir.length() < 1 ){
            return new NcParamFv();
        }
        File fileJornalDisk = new File(strDataInAppDir);
        if( !NcIdxFileManager.fileExistRWAccessChecker(fileJornalDisk) ){
            return new NcParamFv();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(strDataInAppDir)))
        {
            readedDiskInfo = (NcParamFv)ois.readObject();
            NcParamFvManager.checkFromRead(readedDiskInfo);
        }
        catch(Exception ex){
            Logger.getLogger(NcPreRunFileViewer.class.getName()).log(Level.SEVERE, null, ex); 
            return new NcParamFv();
        } 
        return readedDiskInfo;
    }
}
