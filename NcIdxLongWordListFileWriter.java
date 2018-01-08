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
public class NcIdxLongWordListFileWriter {
/**
 * Directory List Word Long
     * @param ncDataToDirListFile
     * @param dataForWrite
     * @param recID
 * @return 
 */    
    public static int ncWriteData(TreeMap<Long, NcDcIdxLongWordListToFile> ncDataToDirListFile, NcDcIdxLongWordListToFile dataForWrite, long recID){
        if( ncDataToDirListFile == null ){
            return -1;
        }
        try(ObjectOutputStream oos = 
                new ObjectOutputStream(
                new FileOutputStream(NcIdxFileManager.getFileNameToRecord(
                        NcManageCfg.getDirLongWordList().getAbsolutePath() + "/wl-"
                        + dataForWrite.name.substring(0, 4),recID))))
        {
            oos.writeObject(ncDataToDirListFile);
        }
        catch(Exception ex){
            Logger.getLogger(NcIdxLongWordListFileWriter.class.getName()).log(Level.SEVERE, null, ex); 
            return -1;
        } 
        return ncDataToDirListFile.size();
    }
}
