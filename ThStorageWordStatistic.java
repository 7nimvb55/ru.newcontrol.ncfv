/*
 * Copyright 2019 wladimirowichbiaran.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.newcontrol.ncfv;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThStorageWordStatistic {
    /**
     * ConcurrentHashMap<Integer, Integer> (<hashFieldCode, Value>)
     * hashFieldCode:
     * - Size
     * - VolumeNum
     * Search by tagFileName current VolumeNum and get his size
     * data for record size summ with writed size and compared with limit for
     * index type if need accumulate data to limit size, send it into cache
     * data structure, while volume not have limited size or time limit in nanos
     * 
     * and control to sizes for cache lists
     * 
     * This structure also for distinct word index need...
     * 
     * ConcurrentHashMap<Integer,     - (1) - Strorage hash value 
     * * * * * - (1) In release only for storage of StorageWord index not apply
     * > ConcurrentHashMap<Integer,   - (2) - Type of word index hash value
     *   
     *     ConcurrentHashMap<String,  - (3) - tagFileName with hex view
     * > >   ConcurrentHashMap<Integer,  - (3a) - Fields - <hashDestinationName, Field>
     * (3a) <hashDestinationName, <Integer, byTypeValue(3a.[1-5], 4)>:
     *      1 - InDirNamesRecordsVolumeNumber
     *      2 - SourcesNowMoveIntoNew
     *      3 - LastAccessCountAccess
     *      4 - CacheToLimitFileSystemLimit
     *      5 - FlagsProcess
     * > > >   ConcurrentHashMap<Integer, Integer>>>>> - (4) - <hashFieldName, Value>
     * 
     * (4) <hashFieldName, Value (Class of 3a.[1-5])>:
     * countFS    - (3a.1) - Integer countRecordsOnFileSystem - updated onWrite, 
     *                before write (Read, Write into old file name, 
     *                after write Files.move to newFileName
     *     - (3a.1) - Integer volumeNumber - update onWrite, before
     *                write = ifLimit ? update : none
     * -------------------------------------------------------------------------
     * namesFS    - (3a.2) - String currentFileName - full file name where read 
     *                from data
     *     - (3a.2) - String newFileName - full file name for Files.move 
     *                operation after write created when readJobDataSize
     * -------------------------------------------------------------------------
     * timeUSE    - (3a.3) - Long lastAccessNanotime - update onWrite, before 
     *                write
     *     - (3a.3) - Long countDataUseIterationsSummary - update onWrite, 
     *                before write, count++ sended jobWrite
     * -------------------------------------------------------------------------
     * countTMP   - (3a.4) - Integer currentInCache - records count, need when 
     *                get job for write for example:
     *                fromJobToWriteDataSize + countRecordsOnFileSystem + 
     *                currentInCache = resultNowData < indexSystemLimitOnStorage
     *                => readFormFileSystem -> summaryReadedCacheFromJob ->
     *                setNew VolumeNumber -> setNew in countRecordsOnFileSystem
     *                -> sendJobForWriter about writeMove -> setNew NewFileName
     *                -> writeToFileSystem in CurrentFileName -> moveTo 
     *                NewFileName (data from sendedJobForWriter)
     *                = resultNowData > indexSystemLimitOnStorage => 
     *                I. - readFromFileSystem, summ...
     *                II. - resultNowData - indexSystemLimitOnStorage -> toCache
     *                III. - sendJobForWriter, update HashMap data ...
     *                = resultNowData == indexSystemLimitOnStorage =>
     *                I. - readFromFileSystem...
     *                II. - resultNowData
     *                III. - sendJobForWriter, update HashMap data ...
     *     - (3a.4) - Integer addNeedToFileSystemLimit - 
     *     - (3a.4) - Integer indexSystemLimitOnStorage -
     * -------------------------------------------------------------------------
     * flagsProc  - (3a.5) - Boolean isWriteProcess - when this param init do it
     *     - (3a.5) - Boolean isReadProcess - when this param init do it
     *     - (3a.5) - Boolean isCachedData - when this param init do it
     *     - (3a.5) - Boolean isCalculatedData
     *     - (3a.5) - Boolean isUdatedDataInHashMap
     */
    private ConcurrentHashMap<Integer, ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ?>>>> fileStoragesMap;
    public ThStorageWordStatistic() {
        this.fileStoragesMap = createNewListStoragesMapEmpty();
    }
    //label
    
    
    protected Integer getGroupIdByNumber(int groupNumber){
        switch (groupNumber) {
            case 1: //InDirNamesRecordsVolumeNumber.hashCode()
                return -1160070363;
            case 2: //SourcesNowMoveIntoNew.hashCode()
                return 1247026961;
            case 3: //LastAccessCountAccess.hashCode()
                return -628632775;
            case 4: //CacheToLimitFileSystemLimit.hashCode()
                return 346081170;
            case 5: //FlagsProcess.hashCode()
                return 492307976;
        }
        return "defaultNotDetectedGroupNumber".hashCode();
    }
    protected Integer getGroupIdByStringName(String inputedName){
        return inputedName.hashCode();
    }
    /**
     * lvl (3a) init params for new itemIndex
     * @return 
     */
    protected ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ?>> getTypeWordTagFileNameParams(final Integer typeWord, final String tagName){
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ?>> tagFileNameParams;
        ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ?>>> getListByTypeWord;
        try{
            getListByTypeWord = getListByType(typeWord);
            tagFileNameParams = getTagFileNameParams(getListByTypeWord, tagName);
            return tagFileNameParams;
        } finally {
            getListByTypeWord = null;
        }
    }
    /**
     * create default or get from lists
     * @param getListByTypeWord
     * @param tagName
     * @return 
     */
    protected ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ?>> getTagFileNameParams(
            final ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ?>>> getListByTypeWord,
            final String tagName){
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ?>> getListByTagFileName;
        try{
            getListByTagFileName = getListByTypeWord.get(tagName);
            if( getListByTagFileName == null ){
                getListByTagFileName = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ?>>();
                //createStructureParams...(...) params from FileSystem or new
                /*getListByTagFileName.put(getGroupIdByNumber(1), );
                getListByTagFileName.put(getGroupIdByNumber(2), );
                getListByTagFileName.put(getGroupIdByNumber(3), );
                getListByTagFileName.put(getGroupIdByNumber(4), );
                getListByTagFileName.put(getGroupIdByNumber(5), );*/
            }
            return getListByTagFileName;
        } finally {
            getListByTagFileName = null;
        }
    }
    //@todo need init new
    /**
     * 
     * @param countRecords
     * @param volumeNumber
     * @return 
     */
    protected ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> createStructureParamsCountFS(
                        final Integer countRecords,
                        final Integer volumeNumber){
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> returnedParams;
        ConcurrentHashMap<Integer, Integer> countFS;
        try{
            countFS = setInParamCountFS(countRecords,
                        volumeNumber);
            Integer groupIdByNumberCountFS = getGroupIdByNumber(1);
            returnedParams = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>>();
            returnedParams.put(groupIdByNumberCountFS , countFS);
            return returnedParams;
        } finally {
            returnedParams = null;
            countFS = null;
        }
    }
    /**
     * 
     * @param srcFileName
     * @param destFileName
     * @return 
     */
    protected ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, String>> createStructureParamsNamesFS(
                        final String srcFileName,
                        final String destFileName){
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, String>> returnedParams;
        ConcurrentHashMap<Integer, String> namesFS;
        try{
            namesFS = setInParamNamesFS(
                        srcFileName,
                        destFileName);
            Integer groupIdByNumberNamesFS = getGroupIdByNumber(2);
            returnedParams = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, String>>();
            returnedParams.put(groupIdByNumberNamesFS , namesFS);
            return returnedParams;
        } finally {
            returnedParams = null;
            namesFS = null;
        }
    }
    /**
     * 
     * @param countDataUseIterationsSummary
     * @return 
     */
    protected ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Long>> createStructureParamsTimeUSE(final long countDataUseIterationsSummary){
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Long>> returnedParams;
        ConcurrentHashMap<Integer, Long> timeUSE;
        try{
            timeUSE = setInParamTimeUSE(countDataUseIterationsSummary);
            Integer groupIdByNumberTimeUSE = getGroupIdByNumber(3);
            returnedParams = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Long>>();
            returnedParams.put(groupIdByNumberTimeUSE , timeUSE);
            return returnedParams;
        } finally {
            returnedParams = null;
            timeUSE = null;
        }
    }
    /**
     * 
     * @param currentInCache
     * @param addNeedToFileSystemLimit
     * @param indexSystemLimitOnStorage
     * @return 
     */
    protected ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> createStructureParamsCountTMP(
                        final Integer currentInCache,
                        final Integer addNeedToFileSystemLimit,
                        final Integer indexSystemLimitOnStorage){
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> returnedParams;
        ConcurrentHashMap<Integer, Integer> countTMP;
        try{
            countTMP = setInParamCountTMP(
                        currentInCache,
                        addNeedToFileSystemLimit,
                        indexSystemLimitOnStorage);
            Integer groupIdByNumberCountTMP = getGroupIdByNumber(4);
            returnedParams = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>>();
            returnedParams.put(groupIdByNumberCountTMP , countTMP);
            return returnedParams;
        } finally {
            returnedParams = null;
            countTMP = null;
        }
    }
    /**
     * 
     * @param isWriteProcess
     * @param isReadProcess
     * @param isCachedData
     * @param isCalculatedData
     * @param isUdatedDataInHashMap
     * @param isMoveFileReady
     * @return 
     */
    protected ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Boolean>> createStructureParamsFlagsProc(
                        final Boolean isWriteProcess,
                        final Boolean isReadProcess,
                        final Boolean isCachedData,
                        final Boolean isCalculatedData,
                        final Boolean isUdatedDataInHashMap,
                        final Boolean isMoveFileReady){
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Boolean>> returnedParams;
        ConcurrentHashMap<Integer, Boolean> flagsProc;
        try{
            flagsProc = setInParamFlagsProc(
                        isWriteProcess,
                        isReadProcess,
                        isCachedData,
                        isCalculatedData,
                        isUdatedDataInHashMap,
                        isMoveFileReady);
            Integer groupIdByNumberFlagsProc = getGroupIdByNumber(5);
            returnedParams = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Boolean>>();
            returnedParams.put(groupIdByNumberFlagsProc , flagsProc);
            return returnedParams;
        } finally {
            returnedParams = null;
            flagsProc = null;
        }
    }
    /**
     * 
     * @param countRecords
     * @param volumeNumber
     * @return 
     */
    protected ConcurrentHashMap<Integer, Integer> setInParamCountFS(
                        final Integer countRecords,
                        final Integer volumeNumber){
        ConcurrentHashMap<Integer, Integer> returnedHashMap;
        Integer funcCountRecords;
        Integer funcVolumeNumber;
        try {
            funcCountRecords = countRecords;
            if( funcCountRecords < 0 ){
                funcCountRecords = 0;
            }
            funcVolumeNumber = volumeNumber;
            if( funcVolumeNumber < 0 ){
                funcVolumeNumber = 0;
            }
            returnedHashMap = new ConcurrentHashMap<Integer, Integer>();
            //countRecordsOnFileSystem.hashCode() - -2011092003
            returnedHashMap.put(-2011092003, funcCountRecords);
            //volumeNumber.hashCode() - -1832815869
            returnedHashMap.put(-1832815869, funcVolumeNumber);
            return returnedHashMap;
        } finally {
            returnedHashMap = null;
            funcCountRecords = null;
            funcVolumeNumber = null;
        }
    }
    /**
     * 
     * @param srcFileName
     * @param destFileName
     * @return 
     */
    protected ConcurrentHashMap<Integer, String> setInParamNamesFS(
                        final String srcFileName,
                        final String destFileName){
        ConcurrentHashMap<Integer, String> returnedHashMap;
        String srcFuncFileName;
        String destFuncFileName;
        try {
            srcFuncFileName = srcFileName;
            destFuncFileName = destFileName;
            if( srcFuncFileName.isEmpty() ){
                srcFuncFileName = "undefinedSrcName-0-0"; // getDafaultNames with current Size and Volume Number
            }
            if( destFuncFileName.isEmpty() ){
                destFuncFileName = "undefinedDestName-0-0";
            }
            returnedHashMap = new ConcurrentHashMap<Integer, String>();
            //currentFileName - 1517772480
            returnedHashMap.put(1517772480, srcFuncFileName);
            //newFileName - 521024487
            returnedHashMap.put(521024487, destFuncFileName);
            return returnedHashMap;
        } finally {
            returnedHashMap = null;
            srcFuncFileName = null;
            destFuncFileName = null;
        }
    }
    /**
     * 
     * @param countDataUseIterationsSummary
     * @return 
     */
    protected ConcurrentHashMap<Integer, Long> setInParamTimeUSE(final long countDataUseIterationsSummary){
        ConcurrentHashMap<Integer, Long> returnedHashMap;
        Long countIterations;
        try {
            returnedHashMap = new ConcurrentHashMap<Integer, Long>();
            countIterations = countDataUseIterationsSummary;
            if( countIterations < 0L ){
                countIterations = 0L;
            }
            //lastAccessNanotime - -1553995461
            long nanoTime = System.nanoTime();
            returnedHashMap.put(-1553995461, nanoTime);
            //countDataUseIterationsSummary - 1445275074
            returnedHashMap.put(1445275074, countIterations);
            return returnedHashMap;
        } finally {
            returnedHashMap = null;
            countIterations = null;
        }
    }
    /**
     * 
     * @param currentInCache
     * @param addNeedToFileSystemLimit
     * @param indexSystemLimitOnStorage
     * @return 
     */
    protected ConcurrentHashMap<Integer, Integer> setInParamCountTMP(
                        final Integer currentInCache,
                        final Integer addNeedToFileSystemLimit,
                        final Integer indexSystemLimitOnStorage){
        ConcurrentHashMap<Integer, Integer> returnedHashMap;
        Integer defaultInCache;
        Integer defaultNeedToFileSystemLimit;
        Integer defaultIndexSystemLimitOnStorage;
        try {
            defaultInCache = currentInCache;
            if( defaultInCache < 0 ){
                defaultInCache = 0;
            }
            defaultNeedToFileSystemLimit = addNeedToFileSystemLimit;
            if( defaultNeedToFileSystemLimit < 0 ){
                defaultNeedToFileSystemLimit = 0;
            }
            defaultIndexSystemLimitOnStorage = indexSystemLimitOnStorage;
            if( defaultIndexSystemLimitOnStorage < 0 ){
                defaultIndexSystemLimitOnStorage = 0;
            }
            returnedHashMap = new ConcurrentHashMap<Integer, Integer>();
            //currentInCache - 322802084
            returnedHashMap.put(322802084, defaultInCache);
            //addNeedToFileSystemLimit - 1443203998
            returnedHashMap.put(1443203998, defaultNeedToFileSystemLimit);
            //indexSystemLimitOnStorage - 585177634
            returnedHashMap.put(585177634, defaultIndexSystemLimitOnStorage);
            return returnedHashMap;
        } finally {
            returnedHashMap = null;
            defaultInCache = null;
            defaultNeedToFileSystemLimit = null;
            defaultIndexSystemLimitOnStorage = null;
        }
    }
    /**
     * 
     * @param isWriteProcess
     * @param isReadProcess
     * @param isCachedData
     * @param isCalculatedData
     * @param isUdatedDataInHashMap
     * @param isMoveFileReady
     * @return 
     */
    protected ConcurrentHashMap<Integer, Boolean> setInParamFlagsProc(
                        final Boolean isWriteProcess,
                        final Boolean isReadProcess,
                        final Boolean isCachedData,
                        final Boolean isCalculatedData,
                        final Boolean isUdatedDataInHashMap,
                        final Boolean isMoveFileReady){
        ConcurrentHashMap<Integer, Boolean> returnedHashMap;
        Boolean funcWriteProcess;
        Boolean funcReadProcess;
        Boolean funcCachedData;
        Boolean funcCalculatedData;
        Boolean funcUdatedDataInHashMap;
        Boolean funcMoveFileReady;
        try {
            funcWriteProcess = isWriteProcess;
            funcReadProcess = isReadProcess;
            funcCachedData = isCachedData;
            funcCalculatedData = isCalculatedData;
            funcUdatedDataInHashMap = isUdatedDataInHashMap;
            funcMoveFileReady = isMoveFileReady;
            returnedHashMap = new ConcurrentHashMap<Integer, Boolean>();
            //isWriteProcess - 1640531930
            returnedHashMap.put(1640531930, funcWriteProcess);
            //isReadProcess - 1836000367
            returnedHashMap.put(1836000367, funcReadProcess);
            //isCachedData - -2091433802
            returnedHashMap.put(-2091433802, funcCachedData);
            //isCalculatedData - 1804093010
            returnedHashMap.put(1804093010, funcCalculatedData);
            //isUdatedDataInHashMap - -2092233516
            returnedHashMap.put(-2092233516, funcUdatedDataInHashMap);
            //isMoveFileReady -
            returnedHashMap.put("isMoveFileReady".hashCode(), funcMoveFileReady);
            return returnedHashMap;
        } finally {
            returnedHashMap = null;
            funcWriteProcess = null;
            funcReadProcess = null;
            funcCachedData = null;
            funcCalculatedData = null;
            funcUdatedDataInHashMap = null;
            funcMoveFileReady = null;
        }
    }
    
    protected ConcurrentHashMap<Integer, ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ?>>>> createNewListStoragesMapEmpty(){
        return new ConcurrentHashMap<Integer, ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ?>>>>();
    }
    protected Map<String, String> getOperationsFileNames(final int typeWordOuter, final String tagFileNameOuter){
        Map<String, String> returnedNames;
        try{
            returnedNames = new HashMap<String, String>();
            
            //file names src dest
            getGroupIdByNumber(2);
            
            return returnedNames;
        } finally {
            returnedNames = null;
        }
    }
    /**
     * return list of not limited files from structure
     * @param typeWordOuter
     * @return 
     */
    protected ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ?>>> getListByType(final int typeWordOuter){
        ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ?>>> forListReturn;
        try{
            forListReturn = this.fileStoragesMap.get(typeWordOuter);
            if( forListReturn == null ){
                forListReturn = new ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ?>>>();
                
            }
            return forListReturn;
        } finally {
            forListReturn = null;
        }
    }
    /**
     * 
     * ThWordStateStorage - Bus:
     *      From file system storages directory by type bus ArrayBlockingQueue<Path>
     *      if new type, create bus
     * Read from storage file system list of files,
     * filter in readed list limited files
     * if type directory not exist, create empty list
     * @return 
     */
    protected ConcurrentHashMap<Integer, Integer> getRecordForList(final int typeWordStorageOuter){
        ConcurrentHashMap<Integer, Integer> forListTypeRecordsReturn;
        try{
            forListTypeRecordsReturn = new ConcurrentHashMap<Integer, Integer>();
            
            return forListTypeRecordsReturn;
        } finally {
            forListTypeRecordsReturn = null;
        }
    }
    /*protected void updateParamCountFS(
                        final Integer typeWord,
                        final String tagFileName,
                        final String paramName,
                        final Integer valueForUpdate){
        try{
            
        } finally {
            
        }
    }
    protected void updateParamNamesFS(
                        final Integer typeWord,
                        final String tagFileName,
                        final String paramName,
                        final Integer valueForUpdate){
        try{
            
        } finally {
            
        }
    }
    protected void updateParamTimeUSE(
                        final Integer typeWord,
                        final String tagFileName,
                        final String paramName,
                        final Long valueForUpdate){
        try{
            
        } finally {
            
        }
    }
    protected void updateParamCountTMP(
                        final Integer typeWord,
                        final String tagFileName,
                        final String paramName,
                        final Integer valueForUpdate){
        try{
            
        } finally {
            
        }
    }
    protected void updateParamFlagsProc(
                        final Integer typeWord,
                        final String tagFileName,
                        final String paramName,
                        final Boolean valueForUpdate){
        try{
            
        } finally {
            
        }
    }*/
}
