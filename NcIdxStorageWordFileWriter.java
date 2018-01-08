/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Администратор
 */
public class NcIdxStorageWordFileWriter {

    /**
     *
     * @param inFuncWrite
     * @param updatedRecords
     * @return
     */
    public static int ncUpdateData(File inFuncWrite, TreeMap<Long, NcDcIdxStorageWordToFile> updatedRecords){
        try(ObjectOutputStream oos = 
                new ObjectOutputStream(
                new FileOutputStream(inFuncWrite)))
        {
            oos.writeObject(updatedRecords);
        }
        catch(Exception ex){
            Logger.getLogger(NcIdxStorageWordFileWriter.class.getName()).log(Level.SEVERE, null, ex); 
            return -1;
        } 
        return updatedRecords.size();
    }
}
