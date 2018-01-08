/*
 *  Copyright 2017 Administrator of development departament newcontrol.ru .
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package ru.newcontrol.ncfv;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Администратор
 */
public class NcIdxWordManager {
/**
 * 
 * @param StructureWord
 * @return 
 */    
    public static long putWord(TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureWord){
        long countWritedIDs = 0;
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureWord.entrySet()){
            String hexWord = item.getValue().hexSubString;
            //detect last recorded ID for word
            //get file name for now record
            long recID = 0;
            
            //ArrayList<String> strCfgPath = getDirForWordFileNames(hexWord);
            //recID = getLastIDForWord(strCfgPath);
            String oldName = "";
            String forRecName = "";
            TreeMap<Long, NcDcIdxWordToFile> inDiskWordRecord = new TreeMap<Long, NcDcIdxWordToFile>();
            boolean isEqualNames = false;
            do{
                inDiskWordRecord = NcIdxWordFileReader.ncReadFromWord(hexWord,recID);
                oldName = NcIdxFileManager.getFileNameToRecord(
                        NcManageCfg.getDirWords().getAbsolutePath() + "/w-" + hexWord,recID);
                if(inDiskWordRecord.isEmpty()){
                    break;
                }
                recID = inDiskWordRecord.lastEntry().getValue().recordID;
                recID++;
                forRecName = NcIdxFileManager.getFileNameToRecord(
                        NcManageCfg.getDirWords().getAbsolutePath() + "/w-" + hexWord,recID);
                isEqualNames = ! oldName.equalsIgnoreCase(forRecName);
                if(isEqualNames){
                    inDiskWordRecord.clear();
                }
            }
            while(isEqualNames);

            NcDcIdxWordToFile forRec = 
                        new NcDcIdxWordToFile(
                                recID,
                                item.getValue().toFileId,
                                item.getValue().strSubString,
                                item.getValue().positionSubString,
                                item.getValue().lengthSubString);
            inDiskWordRecord.put(recID, forRec);
            countWritedIDs = countWritedIDs + NcIdxWordFileWriter.ncWriteForWord(inDiskWordRecord, hexWord, recID);
        }
        
        return countWritedIDs;
    }
/**
 * 
 * @param StructureWord
 * @return 
 */    
    public static TreeMap<Long, NcDcIdxWordToFile> getWord(TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureWord){
        long countWritedIDs = 0;
        TreeMap<Long, NcDcIdxWordToFile> retInDiskWordRecord = new TreeMap<Long, NcDcIdxWordToFile>();
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureWord.entrySet()){
            String hexWord = item.getValue().hexSubString;
            long recID = 0;
            String oldName = "";
            String forRecName = "";
            TreeMap<Long, NcDcIdxWordToFile> inDiskWordRecord = new TreeMap<Long, NcDcIdxWordToFile>();
            boolean isEqualNames = false;
            do{
                inDiskWordRecord.clear();
                inDiskWordRecord = NcIdxWordFileReader.ncReadFromWord(hexWord,recID);
                oldName = NcIdxFileManager.getFileNameToRecord(NcManageCfg.getDirWords().getAbsolutePath() + "/w-" + hexWord,recID);
                if(inDiskWordRecord.isEmpty()){
                    break;
                }
                retInDiskWordRecord.putAll(inDiskWordRecord);
                recID = inDiskWordRecord.lastEntry().getValue().recordID;
                recID++;
                forRecName = NcIdxFileManager.getFileNameToRecord(NcManageCfg.getDirWords().getAbsolutePath() + "/w-" + hexWord,recID);
                isEqualNames = ! oldName.equalsIgnoreCase(forRecName);
                if(isEqualNames){
                    if( !NcIdxFileManager.fileExistRWAccessChecker(new File(forRecName))){
                        break;
                    }
                }
            }
            while(isEqualNames);
        }
        return retInDiskWordRecord;
    }

    /**
     *
     * @param inFuncData
     * @return
     */
    public static boolean isWordWrong(NcDcIdxWordToFile inFuncData){
        if( inFuncData == null ){
            return true;
        }
        if( !isWordDataHashTrue(inFuncData) ){
            return true;
        }
        return false;
    }

    /**
     *
     * @param inFuncData
     * @return
     */
    public static boolean isWordHasEmptyFiled(NcDcIdxWordToFile inFuncData){
        if( inFuncData == null ){
            return true;
        }
        boolean recordIDIsEmpty = inFuncData.recordID == -777;
        boolean dirListIDIsEmpty = inFuncData.dirListID == -777;
        boolean wordIsEmpty = inFuncData.word == "";
        boolean wordHashIsEmpty = inFuncData.wordHash == -777;
        boolean pathPositionIsEmpty = inFuncData.pathPosition == -777;
        boolean wordLengthIsEmpty = inFuncData.wordLength == -777;
        return recordIDIsEmpty
                || dirListIDIsEmpty
                || wordIsEmpty
                || wordHashIsEmpty
                || pathPositionIsEmpty
                || wordLengthIsEmpty;
    }

    /**
     *
     * @param inFuncData
     * @return
     */
    public static boolean isWordDataEmpty(NcDcIdxWordToFile inFuncData){
        if( inFuncData == null ){
            return true;
        }
        boolean recordIDIsEmpty = inFuncData.recordID == -777;
        boolean dirListIDIsEmpty = inFuncData.dirListID == -777;
        boolean wordIsEmpty = inFuncData.word == "";
        boolean wordHashIsEmpty = inFuncData.wordHash == -777;
        boolean pathPositionIsEmpty = inFuncData.pathPosition == -777;
        boolean wordLengthIsEmpty = inFuncData.wordLength == -777;
        boolean hashIsTrue =  isWordDataHashTrue(inFuncData);
        return recordIDIsEmpty
                && dirListIDIsEmpty
                && wordIsEmpty
                && wordHashIsEmpty
                && pathPositionIsEmpty
                && wordLengthIsEmpty
                && hashIsTrue;
    }

    /**
     *
     * @param inFuncData
     * @return
     */
    public static boolean isWordDataHashTrue(NcDcIdxWordToFile inFuncData){
        return inFuncData.recordHash == (
                ""
                + inFuncData.recordID
                + inFuncData.dirListID
                + inFuncData.word
                + inFuncData.wordHash
                + inFuncData.pathPosition
                + inFuncData.wordLength
                + inFuncData.recordTime).hashCode();
    }
    public static TreeMap<Long, NcDcIdxWordToFile> getSearchedWordData(TreeMap<Long, String> inFuncListKeyWords){
        return new TreeMap<Long, NcDcIdxWordToFile>();
    }
    /**
     * Return TreeMap<Long, File> structure for words and heximal view of words, existing in index /w sub dirictory
     * @param wordInHex
     * @param word
     * @return 
     */
    public static TreeMap<Long, File> getWordExistFile(String wordInHex){
        
        TreeMap<Integer, File> listDirs = NcIdxFileManager.getIndexWorkSubDirFilesList();
        
        File filePathDir = listDirs.get("/w".hashCode());
        boolean boolCheck = NcIdxFileManager.dirExistRWAccessChecker(filePathDir);
        if( !boolCheck ){
            if( !filePathDir.mkdirs() ){
                return new TreeMap<Long, File>();
            }
        }
        long recordID = 0;
        File fileWithRecords;
        TreeMap<Long, File> listFiles = new TreeMap<Long, File>();
        do{
            String fileName = NcIdxFileManager.getFileNameToRecord("/w-" + wordInHex, recordID);
            
            String strPathFile = NcIdxFileManager.strPathCombiner(filePathDir.getAbsolutePath(), fileName);
            
            fileWithRecords = new File(strPathFile);
            if( fileWithRecords.exists() ){
                listFiles.put(recordID, fileWithRecords);
            }
            recordID = recordID + 100;
        }
        while( fileWithRecords.exists() );
        return listFiles;
    }
    
    public static TreeMap<Long, NcDcIdxWordToFile> getAllDataForWord(String wordInHex){
        TreeMap<Long, NcDcIdxWordToFile> toReturnData = new TreeMap<Long, NcDcIdxWordToFile>();
        TreeMap<Long, File> existFileList = new TreeMap<Long, File>();
        
        existFileList = getWordExistFile(wordInHex);
        
        if( existFileList.isEmpty() ){
            return toReturnData;
        }
        for( Map.Entry<Long, File> itemFile : existFileList.entrySet() ){
            toReturnData.putAll(NcIdxWordFileReader.ncReadFromWordFile(itemFile.getValue()));
        }
        return toReturnData;
    }
    
}
