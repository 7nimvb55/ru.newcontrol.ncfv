/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.io.File;
import java.util.TreeMap;


/**
 * 
 * @author Администратор
 */
public enum NcTypeOfWord {
    
    /**
     *
     */
    NCLVLABC("/a"),

    /**
     *
     */
    NCLVLRABC("/r"),

    /**
     *
     */
    NCLVLNUM("/n"),

    /**
     *
     */
    NCLVLSYM("/c"),

    /**
     *
     */
    NCLVLSPACE("/p");

    
    private String filtername;
    
    NcTypeOfWord(String filtername){
        this.filtername = filtername;
    }
    
    /**
     *
     * @return
     */
    public String getName(){
        return filtername;
    }
    /**
     * Return TreeMap<Long, File> structure for words and heximal view of words, existing in index sub dirictories
     * @param wordInHex
     * @param word
     * @return 
     */
    public TreeMap<Long, File> getStorageWordExistFileName(String wordInHex, String word){
        
        TreeMap<Integer, File> listDirs = NcIdxFileManager.getIndexWorkSubDirFilesList();
        String strPathSubDir = NcIdxFileManager.strPathCombiner(listDirs.get("/sw".hashCode()).getAbsolutePath(), getName());
        File filePathSubDir = new File(strPathSubDir);
        boolean boolCheck = NcIdxFileManager.dirExistRWAccessChecker(filePathSubDir);
        if( !boolCheck ){
            if( !filePathSubDir.mkdirs() ){
                return new TreeMap<Long, File>();
            }
        }
        long recordID = 0;
        File fileWithRecords;
        TreeMap<Long, File> listFiles = new TreeMap<Long, File>();
        do{
            String fileName = NcIdxFileManager.getFileNameToRecord("/d-" + word.length() + "-" + wordInHex.substring(0, 4), recordID);
            String strPathFile = NcIdxFileManager.strPathCombiner(strPathSubDir, fileName);
            fileWithRecords = new File(strPathFile);
            if( fileWithRecords.exists() ){
                listFiles.put(recordID, fileWithRecords);
            }
            recordID = recordID + 100;
        }
        while( fileWithRecords.exists() );
        return listFiles;
    }
    /**
     * Return file name contained record id in Storage word for word, heximal view for word, and id of record
     * @param wordInHex
     * @param word
     * @param recordId
     * @return 
     */
    public String getStorageWordByIdFileName(String wordInHex, String word, long recordId){
        TreeMap<Integer, File> listDirs = NcIdxFileManager.getIndexWorkSubDirFilesList();
        String strPathSubDir = NcIdxFileManager.strPathCombiner(listDirs.get("/sw".hashCode()).getAbsolutePath(), getName());
        File filePathSubDir = new File(strPathSubDir);
        boolean boolCheck = NcIdxFileManager.dirExistRWAccessChecker(filePathSubDir);
        if( !boolCheck ){
            if( !filePathSubDir.mkdirs() ){
                return "";
            }
        }
        String fileName = NcIdxFileManager.getFileNameToRecord("/d-" + word.length() + "-" + wordInHex.substring(0, 4), recordId);
        String strPathFile = NcIdxFileManager.strPathCombiner(strPathSubDir, fileName);
        return strPathFile;
    }

    
}
