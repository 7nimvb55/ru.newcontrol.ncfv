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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThStorageWordStatusMainFlow {
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
     *   ConcurrentHashMap<String,    - (2a) - tagFileName.substring(0,3)
     *     ConcurrentHashMap<Integer, - (2b) - subString.length                            
     *     ConcurrentHashMap<String,  - (3) - tagFileName with hex view
     * > >   ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>
     *          <UUID MainFlow, Field.hashCode(), valueUUID>
     *          <ThStorageWordStatusDataFs.hashCode(), recordUUID>
     *          <ThStorageWordStatusName.hashCode(), recordUUID>
     *          <ThStorageWordStatusActivity.hashCode(), recordUUID>
     *          <ThStorageWordStatusDataCache.hashCode(), recordUUID>
     *          <ThStorageWordStatusWorkers.hashCode(), recordUUID>>
     * -------------------------------------------------------------------------
     * ThStorageWordStatusDataFs
     * countFS    - (3a.1) - Integer countRecordsOnFileSystem - updated onWrite, 
     *                before write (Read, Write into old file name, 
     *                after write Files.move to newFileName
     *     - (3a.1) - Integer volumeNumber - update onWrite, before
     *                write = ifLimit ? update : none
     * -------------------------------------------------------------------------
     * ThStorageWordStatusName
     * namesFS    - (3a.2) - String currentFileName - full file name where read 
     *                from data
     *     - (3a.2) - String newFileName - full file name for Files.move 
     *                operation after write created when readJobDataSize
     * -------------------------------------------------------------------------
     * ThStorageWordStatusActivity
     * timeUSE    - (3a.3) - Long lastAccessNanotime - update onWrite, before 
     *                write
     *     - (3a.3) - Long countDataUseIterationsSummary - update onWrite, 
     *                before write, count++ sended jobWrite
     * -------------------------------------------------------------------------
     * ThStorageWordStatusDataCache
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
     *     - (3a.4) - Integer addNeedToFileSystemLimit - exist in data file
     *                records size => indexSystemLimitOnStorage - sizeFormFileName
     *                @todo when data read need calculate name and readed data size
     *     - (3a.4) - Integer indexSystemLimitOnStorage - limit from constants
     * -------------------------------------------------------------------------
     * ThStorageWordStatusWorkers
     * flagsProc  - (3a.5) - Boolean isWriteProcess - when this param init do it
     *     - (3a.5) - Boolean isReadProcess - when this param init do it
     *     - (3a.5) - Boolean isCachedData - when this param init do it
     *     - (3a.5) - Boolean isCalculatedData
     *     - (3a.5) - Boolean isUdatedDataInHashMap
     */
    private ConcurrentHashMap<Integer, 
                ConcurrentHashMap<String, 
                    ConcurrentHashMap<Integer, 
                        ConcurrentHashMap<String, 
                            ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>>>>> fileStoragesMap;
    
    private ThStorageWordCache thStorageWordCache;
    private ThStorageWordCacheReaded thStorageWordCacheReaded;
    private ThStorageWordStatusDataFs thStorageWordStatusDataFs;
    private ThStorageWordStatusName thStorageWordStatusName;
    private ThStorageWordStatusActivity thStorageWordStatusActivity;
    private ThStorageWordStatusDataCache thStorageWordStatusDataCache;
    private ThStorageWordStatusWorkers thStorageWordStatusWorkers;
    /**
     * @todo StatusError
     */
    
    public ThStorageWordStatusMainFlow() {
        this.fileStoragesMap = createNewListStoragesMapEmpty();
        
        this.thStorageWordCache = new ThStorageWordCache();
        this.thStorageWordCacheReaded = new ThStorageWordCacheReaded();
        
        this.thStorageWordStatusDataFs = new ThStorageWordStatusDataFs();
        this.thStorageWordStatusName = new ThStorageWordStatusName();
        this.thStorageWordStatusActivity = new ThStorageWordStatusActivity();
        this.thStorageWordStatusDataCache = new ThStorageWordStatusDataCache();
        this.thStorageWordStatusWorkers = new ThStorageWordStatusWorkers();
    }
    /**
     * 
     * @param inputMainFlowUUID
     * @return true if found and delete data
     */
    protected Boolean removeAllFlowStatusByUUID(UUID inputMainFlowUUID){
        
        try {
            if( this.fileStoragesMap == null ){
                return Boolean.FALSE;
            }
            for( Map.Entry<Integer, ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<String, ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>>>>> itemLvlTypeWord : this.fileStoragesMap.entrySet() ){
                ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<String, ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>>>> valueItemLvlTypeWord = itemLvlTypeWord.getValue();
                for( Map.Entry<String, ConcurrentHashMap<Integer, ConcurrentHashMap<String, ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>>>> itemLvlTagFileNameLetter : valueItemLvlTypeWord.entrySet() ){
                    ConcurrentHashMap<Integer, ConcurrentHashMap<String, ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>>> valueItemLvlTagFileNameLetter = itemLvlTagFileNameLetter.getValue();
                    for( Map.Entry<Integer, ConcurrentHashMap<String, ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>>> itemLvlSubStrLength : valueItemLvlTagFileNameLetter.entrySet() ){
                        ConcurrentHashMap<String, ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>> valueItemLvlSubStrLength = itemLvlSubStrLength.getValue();
                        for( Map.Entry<String, ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>> itemLvlTagFileName : valueItemLvlSubStrLength.entrySet() ){
                            ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> valueItemLvlTagFileName = itemLvlTagFileName.getValue();
                            for( Map.Entry<UUID, ConcurrentHashMap<Integer, UUID>> itemMainFlowUUID : valueItemLvlTagFileName.entrySet() ){
                                UUID keyMainFlow = (UUID) itemMainFlowUUID.getKey();
                                if( itemMainFlowUUID.getValue().size() == 5 ){
                                    UUID keyDataFs = itemMainFlowUUID.getValue().get("ThStorageWordStatusDataFs".hashCode());
                                    Boolean removeStatusDataFsForKeyPointFlow = (Boolean) this.thStorageWordStatusDataFs.removeStatusDataFsForKeyPointFlow(keyDataFs);
                                    UUID keyName = itemMainFlowUUID.getValue().get("ThStorageWordStatusName".hashCode());
                                    Boolean removeStatusNameForKeyPointFlow = (Boolean) this.thStorageWordStatusName.removeStatusNameForKeyPointFlow(keyName);
                                    UUID keyActivity = itemMainFlowUUID.getValue().get("ThStorageWordStatusActivity".hashCode());
                                    Boolean removeStatusActivityForKeyPointFlow = (Boolean) this.thStorageWordStatusActivity.removeStatusActivityForKeyPointFlow(keyActivity);
                                    UUID keyDataCache = itemMainFlowUUID.getValue().get("ThStorageWordStatusDataCache".hashCode());
                                    Boolean removeStatusDataCacheForKeyPointFlow = (Boolean) this.thStorageWordStatusDataCache.removeStatusDataCacheForKeyPointFlow(keyDataCache);
                                    UUID keyWorkers = itemMainFlowUUID.getValue().get("ThStorageWordStatusWorkers".hashCode());
                                    Boolean removeStatusWorkersForKeyPointFlow = (Boolean) this.thStorageWordStatusWorkers.removeStatusWorkersForKeyPointFlow(keyWorkers);
                                    ConcurrentHashMap<Integer, UUID> removeMainFlowRec = valueItemLvlTagFileName.remove(keyMainFlow);
                                    removeMainFlowRec = null;
                                    keyMainFlow = null;
                                } else {
                                    return Boolean.FALSE;
                                }
                            }
                        }
                    }
                }
            }
            return Boolean.TRUE;
        } finally {
        
        }
    }
    protected ThStorageWordCache getStorageWordCache(){
        return this.thStorageWordCache;
    }
    protected ThStorageWordCacheReaded getStorageWordCacheReaded(){
        return this.thStorageWordCacheReaded;
    }
    protected ThStorageWordStatusDataFs getStorageWordStatusDataFs(){
        return this.thStorageWordStatusDataFs;
    }
    protected ThStorageWordStatusName getStorageWordStatusName(){
        return this.thStorageWordStatusName;
    }
    protected ThStorageWordStatusActivity getStorageWordStatusActivity(){
        return this.thStorageWordStatusActivity;
    }
    protected ThStorageWordStatusDataCache getStorageWordStatusDataCache(){
        return this.thStorageWordStatusDataCache;
    }
    protected ThStorageWordStatusWorkers getStorageWordStatusWorkers(){
        return this.thStorageWordStatusWorkers;
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
     * > > > > > > > > > this use in router
     * lvl (2a) init params for new itemIndex
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @return lvl (3a)
     * @throws IllegalArgumentException
     */
    protected ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> getTypeWordTagFileNameFlowUuids(
            final Integer typeWord, 
            final String tagName, 
            final String strSubString){
        
        //(3)
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> tagFileNameParams;
        //(1)
        ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, 
                        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>>>> getListByTypeWord;
        //(2a)
        ConcurrentHashMap<Integer, 
                ConcurrentHashMap<String, 
                        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>>> getListByTagNameCode;
        //(2b)
        ConcurrentHashMap<String, 
                        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>> getListBySubStrLength;
        
        try{
            int strSubStringlength = strSubString.length();
            int tagNamelength = tagName.length();
            if( (strSubStringlength * 4) != tagNamelength ){
                throw new IllegalArgumentException(ThStorageWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagName + " lengthHex: " + tagName.length()
                        + " strSubString: " + strSubString + " lengthStr: " + strSubString.length()
                        + " lengthHex == lengthStr * 4 ");
            }
            if( tagNamelength < 4 ){
                throw new IllegalArgumentException(ThStorageWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagName + " length: " + tagName.length()
                        + " < 4 ");
            }
            
            getListByTypeWord = getListByType(typeWord);
            String substringTagName = tagName.substring(0, 3);
            getListByTagNameCode = getListByTypeWord.get(substringTagName);
            if( getListByTagNameCode == null ){
                getListByTagNameCode = new ConcurrentHashMap<Integer, 
                                                ConcurrentHashMap<String, 
                                                        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>>>();
                getListByTypeWord.put(substringTagName, getListByTagNameCode);
                
            }
            getListBySubStrLength = getListByTagNameCode.get(strSubStringlength);
            if( getListBySubStrLength == null ){
                getListBySubStrLength = new ConcurrentHashMap<String, 
                                                ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>>();
                getListByTagNameCode.put(strSubStringlength, getListBySubStrLength);
            }
            tagFileNameParams = getTagFileNameParams(getListBySubStrLength, tagName);
            return tagFileNameParams;
        } finally {
            getListByTypeWord = null;
            tagFileNameParams = null;
            getListByTagNameCode = null;
            getListBySubStrLength = null;
        }
    }
    /**
     * create default or get from lists
     * @param getListByTypeWord
     * @param tagName
     * @return lvl (3)
     */
    protected ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> getTagFileNameParams(
            final ConcurrentHashMap<String, ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>> getListByTypeWord,
            final String tagName){
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> getListByTagFileName;
        try{
            getListByTagFileName = getListByTypeWord.get(tagName);
            if( getListByTagFileName == null ){
                getListByTagFileName = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>();
                getListByTypeWord.put(tagName, getListByTagFileName);
                /**
                 * -> get results from: 
                 * createStructureParamsCountFS
                 * createStructureParamsNamesFS
                 * createStructureParamsTimeUSE
                 * createStructureParamsCountTMP
                 * createStructureParamsFlagsProc
                 * -> add to getListByTagFileName
                 * if null
                 *  - create defaults from job data - first iteration
                 *  - need update data from fs - if read old index storage
                 */
            }
            return getListByTagFileName;
        } finally {
            getListByTagFileName = null;
        }
    }
    /**
     * @todo check for inputed params
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @param keysPointsFlow ConcurrentHashMap<Integer, UUID>
     *          <ThStorageWordStatusDataFs.hashCode(), recordUUID>
     *          <ThStorageWordStatusName.hashCode(), recordUUID>
     *          <ThStorageWordStatusActivity.hashCode(), recordUUID>
     *          <ThStorageWordStatusDataCache.hashCode(), recordUUID>
     *          <ThStorageWordStatusWorkers.hashCode(), recordUUID>
     */
    protected void setParamsPointsFlow(
            final Integer typeWord, 
            final String tagName, 
            final String strSubString,
            final ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> mainFlowContentInputed){
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> mainFlowContentFunc;
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> typeWordTagFileNameFlowUuids;
        try {
            mainFlowContentFunc = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>();
            for(Map.Entry<UUID, ConcurrentHashMap<Integer, UUID>> itemsContent : mainFlowContentInputed.entrySet()){
                if( itemsContent.getValue().isEmpty() ){
                    new IllegalArgumentException(ThStorageWordStatusMainFlow.class.getCanonicalName() + " parameters of data for set into cache is empty");
                }
                
                int countThStorageWordStatusDataFs = 0;
                int countThStorageWordStatusName = 0;
                int countThStorageWordStatusActivity = 0;
                int countThStorageWordStatusDataCache = 0;
                int countThStorageWordStatusWorkers = 0;
                
                for(Map.Entry<Integer, UUID> itemsElements : itemsContent.getValue().entrySet()){
                    switch ( itemsElements.getKey() ){
                        case 24537146: 
                            countThStorageWordStatusDataFs++;
                            continue; //ThStorageWordStatusDataFs
                        case -835430034: 
                            countThStorageWordStatusName++;
                            continue; //ThStorageWordStatusName
                        case -1339250574: 
                            countThStorageWordStatusActivity++;
                            continue; //ThStorageWordStatusActivity
                        case 838467829: 
                            countThStorageWordStatusDataCache++;
                            continue; //ThStorageWordStatusDataCache
                        case 842641138: 
                            countThStorageWordStatusWorkers++;
                            continue; //ThStorageWordStatusWorkers
                    }
                    
                    new IllegalArgumentException(ThStorageWordStatusMainFlow.class.getCanonicalName() + " parameters of data for set into cache not valid");
                }
                
                if( countThStorageWordStatusDataFs != 2 ){
                                new IllegalArgumentException(ThStorageWordStatusMainFlow.class.getCanonicalName() 
                                        + " parameters flowDataFs not valid");
                }
                if( countThStorageWordStatusName != 3 ){
                    new IllegalArgumentException(ThStorageWordStatusMainFlow.class.getCanonicalName() 
                            + " parameters flowName not valid");
                    }
                if( countThStorageWordStatusActivity != 2 ){
                    new IllegalArgumentException(ThStorageWordStatusMainFlow.class.getCanonicalName() 
                            + " parameters flowActivity not valid");  
                }     
                if( countThStorageWordStatusDataCache != 2 ){
                    new IllegalArgumentException(ThStorageWordStatusMainFlow.class.getCanonicalName() 
                            + " parameters flowDataCache not valid");
                }
                if( countThStorageWordStatusWorkers != 5 ){
                    new IllegalArgumentException(ThStorageWordStatusMainFlow.class.getCanonicalName() 
                            + " parameters flowWorkers not valid");
                }
                
                
                mainFlowContentFunc.put(itemsContent.getKey(), itemsContent.getValue());
            }
            
            typeWordTagFileNameFlowUuids = getTypeWordTagFileNameFlowUuids(
                    typeWord,
                    tagName,
                    strSubString);
            
            typeWordTagFileNameFlowUuids.putAll(mainFlowContentFunc);
        } finally {
            typeWordTagFileNameFlowUuids = null;
        }
    }
    
    
    
    protected ConcurrentHashMap<Integer, ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<String, 
                            ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>>>>> createNewListStoragesMapEmpty(){
        return new ConcurrentHashMap<Integer, 
                        ConcurrentHashMap<String, 
                            ConcurrentHashMap<Integer, 
                                ConcurrentHashMap<String, 
                                    ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>>>>>();
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
    protected ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, 
                        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>>>> getListByType(final int typeWordOuter){
        ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, 
                        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>>>> forListReturn;
        try{
            forListReturn = this.fileStoragesMap.get(typeWordOuter);
            if( forListReturn == null ){
                forListReturn = new ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, 
                        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>>>>();
                this.fileStoragesMap.put(typeWordOuter, forListReturn);
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
