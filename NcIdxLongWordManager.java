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
public class NcIdxLongWordManager {

    /**
     * 
     * @param StructureLongWord
     * @param dataFuncForWrite
     * @return 
     */    
    public static long putLongWordInFile(TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureLongWord, NcDcIdxLongWordListToFile dataFuncForWrite){
        long countWritedIDs = 0;
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureLongWord.entrySet()){
            String hexWord = item.getValue().hexSubString;
            long recID = 0;
            String oldName = "";
            String forRecName = "";
            TreeMap<Long, NcDcIdxWordToFile> inDiskWordRecord = new TreeMap<Long, NcDcIdxWordToFile>();
            boolean isEqualNames = false;
            do{
                oldName = NcIdxFileManager.getFileNameToRecordLongWord(
                        NcIdxFileManager.getStrCanPathFromFile(NcManageCfg.getDirLongWord()) + "/n-"
                        + dataFuncForWrite.name.substring(0, 4), dataFuncForWrite.nameID, recID);
                inDiskWordRecord = NcIdxLongWordFileReader.ncReadFromLongWordFile(oldName,recID);
                
                if(inDiskWordRecord.isEmpty()){
                    break;
                }
                recID = inDiskWordRecord.lastEntry().getValue().recordID;
                recID++;
                forRecName = NcIdxFileManager.getFileNameToRecordLongWord(
                        NcIdxFileManager.getStrCanPathFromFile(NcManageCfg.getDirLongWord()) + "/n-"
                        + dataFuncForWrite.name.substring(0, 4), dataFuncForWrite.nameID, recID);
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
            forRecName = NcIdxFileManager.getFileNameToRecordLongWord(
                NcIdxFileManager.getStrCanPathFromFile(NcManageCfg.getDirLongWord()) + "/n-"
                + dataFuncForWrite.name.substring(0, 4), dataFuncForWrite.nameID, recID);
            countWritedIDs = countWritedIDs + NcIdxLongWordFileWriter.ncWriteForLongWord(
                inDiskWordRecord, forRecName, recID);
        }
        
        return countWritedIDs;
    }
    /**
     * 
     * @param StructureLongWord
     * @return 
     */    
    public static TreeMap<Long, NcDcIdxWordToFile> getLongWord(TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureLongWord){
        TreeMap<Long, NcDcIdxWordToFile> retReadedData = new TreeMap<Long, NcDcIdxWordToFile>();
        long countWritedIDs = 0;
        String nameLongWordList = "";
        NcDcIdxLongWordListToFile dataForRead;
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureLongWord.entrySet()){
            long recLongWordListID = 0;
            String hexWord = item.getValue().hexSubString;
            dataForRead = new NcDcIdxLongWordListToFile(
                    recLongWordListID,
                    hexWord, 
                    item.getValue().strSubString);
            
            dataForRead = getForSearchLongWordID(dataForRead);
            // toChangeCheckRecord
            if((dataForRead.nameID == -777)
                && (dataForRead.name.length() == 0)
                && (dataForRead.nameHash == -777)
                && (dataForRead.word.length() == 0)
                && (dataForRead.wordHash == -777)){
                break;
            }
            
            recLongWordListID = dataForRead.nameID;
            TreeMap<Long, NcDcIdxSubStringToOperationUse> subStructureLongWord = new TreeMap<Long, NcDcIdxSubStringToOperationUse>();
            subStructureLongWord.put(recLongWordListID,item.getValue());
            retReadedData.putAll(getLongWordFromFile(subStructureLongWord,dataForRead));
        }
        return retReadedData;
    }
    /**
     * 
     * @param dataForRead
     * @return 
     */    
    private static NcDcIdxLongWordListToFile getForSearchLongWordID(NcDcIdxLongWordListToFile dataForRead){

        String nameLongWordList = "";
        String nameNextLongWordList = "";
        long recID = dataForRead.nameID;
        nameLongWordList = NcIdxFileManager.getFileNameToRecord(
                NcIdxFileManager.getStrCanPathFromFile(NcManageCfg.getDirLongWordList()) + "/wl-"
                + dataForRead.name.substring(0, 4),recID);
        boolean isNamesEquals = false;
        boolean isNameEquals = false;
        boolean isNameHashEquals = false;
        boolean isSubStrEquals = false;
        boolean isSubStrHashEquals = false;
        
        TreeMap<Long, NcDcIdxLongWordListToFile> readFromDiskData = new TreeMap<Long, NcDcIdxLongWordListToFile>();
        do{
            readFromDiskData.clear();
            readFromDiskData = NcIdxLongWordListFileReader.ncReadFileContainedId(dataForRead, recID);
            for(Map.Entry<Long, NcDcIdxLongWordListToFile> item  : readFromDiskData.entrySet()){
                isNameEquals = item.getValue().name.equalsIgnoreCase(dataForRead.name);
                isNameHashEquals = item.getValue().nameHash == dataForRead.nameHash;
                isSubStrEquals = item.getValue().word.equalsIgnoreCase(dataForRead.word);
                isSubStrHashEquals = item.getValue().wordHash == dataForRead.wordHash;
                if(isNameEquals
                        && isNameHashEquals
                        && isSubStrEquals
                        && isSubStrHashEquals){
                    return item.getValue();
                }
            }
          
            recID = readFromDiskData.lastEntry().getValue().nameID + 1; 
            nameNextLongWordList = NcIdxFileManager.getFileNameToRecord(
                    NcIdxFileManager.getStrCanPathFromFile(NcManageCfg.getDirLongWordList()) + "/wl-"
                    + dataForRead.name.substring(0, 4),recID);
            isNamesEquals = ! nameLongWordList.equalsIgnoreCase(nameNextLongWordList);
            if(isNamesEquals){
                dataForRead.nameID = recID;
                if( !NcIdxFileManager.fileExistRWAccessChecker(new File(nameNextLongWordList))){
                    break;
                }
            }
        }
        while(isNamesEquals);
        
        dataForRead = new NcDcIdxLongWordListToFile();
        return dataForRead;
    }
    /**
     * 
     * @param StructureLongWord
     * @param dataFuncForWrite
     * @return 
     */
    private static TreeMap<Long, NcDcIdxWordToFile> getLongWordFromFile(TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureLongWord, NcDcIdxLongWordListToFile dataFuncForWrite){
        TreeMap<Long, NcDcIdxWordToFile> retReadedData = new TreeMap<Long, NcDcIdxWordToFile>();

        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureLongWord.entrySet()){
            long recID = 0;
            String oldName = "";
            String forRecName = "";
            TreeMap<Long, NcDcIdxWordToFile> inDiskWordRecord = new TreeMap<Long, NcDcIdxWordToFile>();
            boolean isEqualNames = false;
            do{
                oldName = NcIdxFileManager.getFileNameToRecordLongWord(
                        NcIdxFileManager.getStrCanPathFromFile(NcManageCfg.getDirLongWord()) + "/n-"
                        + dataFuncForWrite.name.substring(0, 4), dataFuncForWrite.nameID, recID);
                inDiskWordRecord = NcIdxLongWordFileReader.ncReadFromLongWordFile(oldName,recID);
                if(inDiskWordRecord.isEmpty()){
                    break;
                }
                retReadedData.putAll(inDiskWordRecord);
                recID = inDiskWordRecord.lastEntry().getValue().recordID;
                recID++;
                forRecName = NcIdxFileManager.getFileNameToRecordLongWord(
                        NcIdxFileManager.getStrCanPathFromFile(NcManageCfg.getDirLongWord()) + "/n-"
                        + dataFuncForWrite.name.substring(0, 4), dataFuncForWrite.nameID, recID);
                isEqualNames = ! oldName.equalsIgnoreCase(forRecName);
                if(isEqualNames){
                    if( !NcIdxFileManager.fileExistRWAccessChecker(new File(forRecName))){
                        break;
                    }
                }
            }
            while(isEqualNames);
        }
        return retReadedData;
    }

    /**
     * Used in
     * <ul>
     * <li>
     * </ul>
     * @param inFuncWord
     * @return
     */
    protected static boolean isLongWord(String inFuncWord){
        if (inFuncWord.length() > 25){
            return true;
        }
        return false;
    }
}
