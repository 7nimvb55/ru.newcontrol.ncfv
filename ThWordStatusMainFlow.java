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
public class ThWordStatusMainFlow {
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
     * * * * * - (1) In release only for storage of Word index not apply
     * > ConcurrentHashMap<Integer,   - (2) - Type of word index hash value
     *   ConcurrentHashMap<String,    - (2a) - tagFileName.substring(0,3)
     *     ConcurrentHashMap<Integer, - (2b) - subString.length                            
     *     ConcurrentHashMap<String,  - (3) - tagFileName with hex view
     * > >   ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>
     *          <UUID MainFlow, Field.hashCode(), valueUUID>
     *          <ThWordStatusDataFs.hashCode(), recordUUID>
     *          <ThWordStatusName.hashCode(), recordUUID>
     *          <ThWordStatusActivity.hashCode(), recordUUID>
     *          <ThWordStatusDataCache.hashCode(), recordUUID>
     *          <ThWordStatusWorkers.hashCode(), recordUUID>>
     * -------------------------------------------------------------------------
     * ThWordStatusDataFs
     * countFS    - (3a.1) - Integer countRecordsOnFileSystem - updated onWrite, 
     *                before write (Read, Write into old file name, 
     *                after write Files.move to newFileName
     *     - (3a.1) - Integer volumeNumber - update onWrite, before
     *                write = ifLimit ? update : none
     * -------------------------------------------------------------------------
     * ThWordStatusName
     * namesFS    - (3a.2) - String currentFileName - full file name where read 
     *                from data
     *     - (3a.2) - String newFileName - full file name for Files.move 
     *                operation after write created when readJobDataSize
     * -------------------------------------------------------------------------
     * ThWordStatusActivity
     * timeUSE    - (3a.3) - Long lastAccessNanotime - update onWrite, before 
     *                write
     *     - (3a.3) - Long countDataUseIterationsSummary - update onWrite, 
     *                before write, count++ sended jobWrite
     * -------------------------------------------------------------------------
     * ThWordStatusDataCache
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
     * ThWordStatusWorkers
     * flagsProc  - (3a.5) - Boolean isWriteProcess - when this param init do it
     *     - (3a.5) - Boolean isReadProcess - when this param init do it
     *     - (3a.5) - Boolean isCachedData - when this param init do it
     *     - (3a.5) - Boolean isCalculatedData
     *     - (3a.5) - Boolean isUdatedDataInHashMap
     */
    private final Long timeCreation;
    private final UUID objectLabel;
    
    private ConcurrentHashMap<Integer, 
                ConcurrentHashMap<String, 
                    ConcurrentHashMap<Integer, 
                        ConcurrentHashMap<String, 
                            ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>>>>> fileStoragesMap;
    
    private ThWordCache thWordCache;
    private ThWordCacheReaded thWordCacheReaded;
    private ThWordStatusDataFs thWordStatusDataFs;
    private ThWordStatusName thWordStatusName;
    private ThWordStatusActivity thWordStatusActivity;
    private ThWordStatusDataCache thWordStatusDataCache;
    private ThWordStatusWorkers thWordStatusWorkers;
    /**
     * @todo StatusError
     */
    
    public ThWordStatusMainFlow() {
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        
        this.fileStoragesMap = createNewListStoragesMapEmpty();
        
        this.thWordCache = new ThWordCache();
        this.thWordCacheReaded = new ThWordCacheReaded();
        
        this.thWordStatusDataFs = new ThWordStatusDataFs();
        this.thWordStatusName = new ThWordStatusName();
        this.thWordStatusActivity = new ThWordStatusActivity();
        this.thWordStatusDataCache = new ThWordStatusDataCache();
        this.thWordStatusWorkers = new ThWordStatusWorkers();
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
                                    UUID keyDataFs = itemMainFlowUUID.getValue().get("ThWordStatusDataFs".hashCode());
                                    Boolean removeStatusDataFsForKeyPointFlow = (Boolean) this.thWordStatusDataFs.removeStatusDataFsForKeyPointFlow(keyDataFs);
                                    UUID keyName = itemMainFlowUUID.getValue().get("ThWordStatusName".hashCode());
                                    Boolean removeStatusNameForKeyPointFlow = (Boolean) this.thWordStatusName.removeStatusNameForKeyPointFlow(keyName);
                                    UUID keyActivity = itemMainFlowUUID.getValue().get("ThWordStatusActivity".hashCode());
                                    Boolean removeStatusActivityForKeyPointFlow = (Boolean) this.thWordStatusActivity.removeStatusActivityForKeyPointFlow(keyActivity);
                                    UUID keyDataCache = itemMainFlowUUID.getValue().get("ThWordStatusDataCache".hashCode());
                                    Boolean removeStatusDataCacheForKeyPointFlow = (Boolean) this.thWordStatusDataCache.removeStatusDataCacheForKeyPointFlow(keyDataCache);
                                    UUID keyWorkers = itemMainFlowUUID.getValue().get("ThWordStatusWorkers".hashCode());
                                    Boolean removeStatusWorkersForKeyPointFlow = (Boolean) this.thWordStatusWorkers.removeStatusWorkersForKeyPointFlow(keyWorkers);
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
    protected ThWordCache getWordCache(){
        return this.thWordCache;
    }
    protected ThWordCacheReaded getWordCacheReaded(){
        return this.thWordCacheReaded;
    }
    protected ThWordStatusDataFs getWordStatusDataFs(){
        return this.thWordStatusDataFs;
    }
    protected ThWordStatusName getWordStatusName(){
        return this.thWordStatusName;
    }
    protected ThWordStatusActivity getWordStatusActivity(){
        return this.thWordStatusActivity;
    }
    protected ThWordStatusDataCache getWordStatusDataCache(){
        return this.thWordStatusDataCache;
    }
    protected ThWordStatusWorkers getWordStatusWorkers(){
        return this.thWordStatusWorkers;
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
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagName + " lengthHex: " + tagName.length()
                        + " strSubString: " + strSubString + " lengthStr: " + strSubString.length()
                        + " lengthHex == lengthStr * 4 ");
            }
            if( tagNamelength < 4 ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
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
     *          <ThWordStatusDataFs.hashCode(), recordUUID>
     *          <ThWordStatusName.hashCode(), recordUUID>
     *          <ThWordStatusActivity.hashCode(), recordUUID>
     *          <ThWordStatusDataCache.hashCode(), recordUUID>
     *          <ThWordStatusWorkers.hashCode(), recordUUID>
     * @throws IllegalArgumentException when count params or name not valid
     */
    protected void setParamsPointsFlow(
            final Integer typeWordOuter, 
            final String tagNameOuter, 
            final String strSubStringOuter,
            final ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> mainFlowContentInputed){
        Integer typeWordInner;
        String tagNameInner;
        String strSubStringInner;
        
        Boolean existInListOfParams;
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> mainFlowContentFunc;
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> typeWordTagFileNameFlowUuids;
        try {
            typeWordInner = (Integer) typeWordOuter;
            tagNameInner = (String) tagNameOuter;
            strSubStringInner = (String) strSubStringOuter;
            mainFlowContentFunc = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>();
            for(Map.Entry<UUID, ConcurrentHashMap<Integer, UUID>> itemsContent : mainFlowContentInputed.entrySet()){
                validateCountParams(itemsContent.getValue());
                mainFlowContentFunc.put(itemsContent.getKey(), itemsContent.getValue());
            }
            
            typeWordTagFileNameFlowUuids = (ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>) getTypeWordTagFileNameFlowUuids(
                    typeWordInner,
                    tagNameInner,
                    strSubStringInner);
            
            typeWordTagFileNameFlowUuids.putAll((ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>) mainFlowContentFunc);
        } finally {
            typeWordTagFileNameFlowUuids = null;
            existInListOfParams = null;
            //delete it null if in creation of structure has problem...need test
            mainFlowContentFunc = null;
            typeWordInner = null;
            tagNameInner = null;
            strSubStringInner = null;
        }
    }
    /**
     * 
     * @param checkedMainFlowContentInputed 
     * @throws IllegalArgumentException when count params or name not valid
     */
    protected void validateCountParams(final ConcurrentHashMap<Integer, UUID> checkedMainFlowContentInputed){
        
        ConcurrentHashMap<Integer, UUID> checkedMainFlowContentFunc;
        try {
            checkedMainFlowContentFunc = (ConcurrentHashMap<Integer, UUID>) checkedMainFlowContentInputed;
            if( checkedMainFlowContentFunc.isEmpty() ){
                    throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() + " parameters of data for set into cache is empty");
                }
                int countParam = getParamCount();
                
                
                int countForSet = checkedMainFlowContentFunc.size();
                if( countForSet != countParam ){
                    throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() + " parameters of data for set into cache not valid, "
                            + " base count: " + countParam
                            + ", send for set count: " + countParam);
                }
                
                for( int idx = 0 ; idx < countParam ; idx++ ){
                    if ( !checkedMainFlowContentFunc.containsKey(getParamCodeByNumber(idx)) ){
                        throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() + " parameter "
                                + " for name: " + getParamNameByNumber(idx)
                                + " in inputed data for set into cache not exist");
                    }
                }
        } finally {
            checkedMainFlowContentFunc = null;
        }
    }
    /**
     * <ul>
     * <li>0 - ThWordStatusDataFs
     * <li>1 - ThWordStatusName
     * <li>2 - ThWordStatusActivity
     * <li>3 - ThWordStatusDataCache
     * <li>4 - ThWordStatusWorkers
     * </ul>
     * @return 
     */
    private String[] getParamNames(){
        String[] namesForReturn;
        try {
            namesForReturn = new String[] {
                "ThWordStatusDataFs", 
                "ThWordStatusName", 
                "ThWordStatusActivity",
                "ThWordStatusDataCache",
                "ThWordStatusWorkers"
            };
            return namesForReturn;
        } finally {
            namesForReturn = null;
        }
    }
    /**
     * Return code of parameter by his number, calculeted from some fileds
     * @param numParam
     * @return hashCode for Parameter by his number
     * @see getParamNames()
     * @throws IllegalArgumentException when inputed number of parameter
     * out of bounds
     */
    protected Integer getParamCodeByNumber(int numParam){
        String[] paramNames;
        try {
            paramNames = getParamNames();
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusError.class.getCanonicalName() 
                                + " parameters of flow statusError in StorageWord is not valid, "
                                + "count parameters: " 
                                + paramNames.length 
                                + ", need for return " + numParam);
            } 
            int codeForParameter = paramNames[numParam]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).hashCode();
            return codeForParameter;
        } finally {
            paramNames = null;
        }
    }
    /**
     * Count records (array.length) returned from {@link #getParamNames }
     * @return 
     */
    protected int getParamCount(){
        String[] paramNames;
        try {
            paramNames = getParamNames();
            return paramNames.length;
        } finally {
            paramNames = null;
        }
    }
    /**
     * 
     * @param numParam
     * @return name of param by his number
     * @throws IllegalArgumentException when inputed number of parameter
     * out of bounds
     */
    private String getParamNameByNumber(int numParam){
        String[] paramNames;
        String paramName;
        try {
            paramNames = getParamNames();
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusError.class.getCanonicalName() 
                                + " parameters of flow statusError in StorageWord is not valid, "
                                + "count parameters: " 
                                + paramNames.length 
                                + ", need for return " + numParam);
            } 
            paramName = new String(paramNames[numParam]);
            return paramName;
        } finally {
            paramNames = null;
            paramName = null;
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
    

}
