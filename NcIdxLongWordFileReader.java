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
public class NcIdxLongWordFileReader {
    /**
 * 
 * @param readedWord
 * @param rID
 * @return 
 */    
    public static TreeMap<Long, NcDcIdxWordToFile> ncReadFromLongWordFile(String readedWord, long rID){
        TreeMap<Long, NcDcIdxWordToFile> ncDataFromWordFile;
        String strCfgPath =  readedWord;
        if ( !NcIdxFileManager.fileExistRWAccessChecker(new File(strCfgPath))){
            return new TreeMap<Long, NcDcIdxWordToFile>();
        };
        
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(strCfgPath)))
        {
            ncDataFromWordFile = (TreeMap<Long, NcDcIdxWordToFile>)ois.readObject();
        }
        catch(Exception ex){
            Logger.getLogger(NcIdxLongWordFileReader.class.getName()).log(Level.SEVERE, null, ex); 
            return new TreeMap<Long, NcDcIdxWordToFile>();
        } 
        return ncDataFromWordFile;
    }
}
