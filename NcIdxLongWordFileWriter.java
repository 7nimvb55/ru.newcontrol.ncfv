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
public class NcIdxLongWordFileWriter {
    /**
     * 
     * @param ncWordToRec
     * @param recHexWord
     * @param recID
     * @return 
     */    
    public static long ncWriteForLongWord(TreeMap<Long, NcDcIdxWordToFile> ncWordToRec, String recHexWord, long recID){
        if( ncWordToRec == null ){
            return -1;
        }
        try(ObjectOutputStream oos = 
                new ObjectOutputStream(
                new FileOutputStream(recHexWord)))
        {
            oos.writeObject(ncWordToRec);
        }
        catch(Exception ex){
            Logger.getLogger(NcIdxLongWordFileWriter.class.getName()).log(Level.SEVERE, null, ex); 
            return -1;
        } 
        return ncWordToRec.size();
    }
    
}
