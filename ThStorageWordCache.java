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

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap<Integer,   - (2) - Type of word index hash value
 *   ConcurrentHashMap<String,    - (2a) - tagFileName.substring(0,3)
 *     ConcurrentHashMap<Integer, - (2b) - subString.length                            
 *     ConcurrentHashMap<String, String> (3) - <hexWord (tagFileName), subString>
 * @author wladimirowichbiaran
 */
public class ThStorageWordCache {
    
    private ConcurrentHashMap<Integer, 
            ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, String>>>> cachedData;
    
    public ThStorageWordCache() {
        this.cachedData = createNewListStoragesMapEmpty();
    }
    protected ConcurrentHashMap<Integer, 
        ConcurrentHashMap<String, 
            ConcurrentHashMap<Integer, 
                ConcurrentHashMap<String, String>>>> createNewListStoragesMapEmpty(){
        return new ConcurrentHashMap<Integer, 
                        ConcurrentHashMap<String, 
                            ConcurrentHashMap<Integer, 
                                ConcurrentHashMap<String, String>>>>();
    }
    /**
     * When router read all data from filter, need save all cached data into
     * storage, ths function rebuild all cache data in bus format, for save
     * it into storages in next step
     * 
     * @todo need delete data froom cache in this function, and iterate this, ahile
     *       all data no saved
     * 
     * @return ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>>
     *                          <TypeWord, <TagName, SubString>>
     */
    protected ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> pollListTypeTagSubStr(){
        ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> forReturnList;
        Boolean hasDataForReturn;
        ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>>> valueItemTypeWord;
        ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> valueTagLetter;
        ConcurrentHashMap<String, String> getListForKeyWord;
        ConcurrentHashMap<String, String> valueSubStrLength;
        ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>>> getForFinishCheck;
        ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>>> removedFromCache;
        ConcurrentHashMap<String, String> removeSubStrList;
        
        ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> valueCheckSubStrLength;
        Integer keyList;
        ConcurrentHashMap<String, String> valueList;
        ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> removeCacheFinishCheck;
        
        ConcurrentHashMap<String, String> removeFromForReturnList;
        
        Integer keySubStrLength;
        Integer keyTypeWord;
        String keyTagLetter;
        try {
            
            forReturnList = new ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>>();
            // remove data from cache, for empty tree, remove his key
            for( Map.Entry<Integer, ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>>>> itemTypeWord : this.cachedData.entrySet() ){
                keyTypeWord = (Integer) itemTypeWord.getKey();
                valueItemTypeWord = itemTypeWord.getValue();
                for( Map.Entry<String, ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>>> itemTagLetter : valueItemTypeWord.entrySet() ){
                    keyTagLetter = (String) itemTagLetter.getKey();
                    valueTagLetter = itemTagLetter.getValue();
                    for( Map.Entry<Integer, ConcurrentHashMap<String, String>> itemSubStrLength : valueTagLetter.entrySet() ){
                        keySubStrLength = (Integer) itemSubStrLength.getKey();
                        valueSubStrLength = (ConcurrentHashMap<String, String>) valueTagLetter.remove(keySubStrLength);
                        getListForKeyWord = (ConcurrentHashMap<String, String>) forReturnList.get(keyTypeWord);
                        if( getListForKeyWord == null){
                            getListForKeyWord = new ConcurrentHashMap<String, String>();
                        }
                        getListForKeyWord.putAll(valueSubStrLength);
                        forReturnList.put(keyTypeWord, getListForKeyWord);
                    }
                }
            }
            hasDataForReturn = Boolean.FALSE;
            for(Map.Entry<Integer, ConcurrentHashMap<String, String>> itemReturnedList : forReturnList.entrySet()){
                if( !itemReturnedList.getValue().isEmpty() ){
                    hasDataForReturn = Boolean.TRUE;
                } else {
                    getForFinishCheck = this.cachedData.get(itemReturnedList.getKey());
                    for( Map.Entry<String, ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>>> entrySetCheckTagName : getForFinishCheck.entrySet() ){
                        valueCheckSubStrLength = entrySetCheckTagName.getValue();
                        for( Map.Entry<Integer, ConcurrentHashMap<String, String>> entrySetCheckSubStrLength : valueCheckSubStrLength.entrySet() ){
                            keyList = entrySetCheckSubStrLength.getKey();
                            valueList = entrySetCheckSubStrLength.getValue();
                            if( valueList.isEmpty() ){
                                removeSubStrList = valueCheckSubStrLength.remove(keyList);
                                removeSubStrList = null;
                            }
                        }
                        valueCheckSubStrLength = entrySetCheckTagName.getValue();
                        if( valueCheckSubStrLength.isEmpty() ){
                            removeCacheFinishCheck = getForFinishCheck.remove(entrySetCheckTagName.getKey());
                            removeCacheFinishCheck = null;
                        }
                        
                    }
                    getForFinishCheck = this.cachedData.get(itemReturnedList.getKey());
                    if( getForFinishCheck.isEmpty() ){
                        removedFromCache = this.cachedData.remove(itemReturnedList.getKey());
                        removedFromCache = null;
                    }
                    
                    removeFromForReturnList = forReturnList.remove(itemReturnedList.getKey());
                    removeFromForReturnList = null;
                }
            }
            if( !hasDataForReturn ){
                return null;
            }
            return forReturnList;
        } finally {
            valueItemTypeWord = null;
            valueSubStrLength = null;
            valueTagLetter = null;
            getListForKeyWord = null;
            forReturnList = null;
            hasDataForReturn = null;
            getForFinishCheck = null;
            removedFromCache = null;
            removeSubStrList = null;
            valueCheckSubStrLength = null;
            keyList = null;
            valueList = null;
            removeCacheFinishCheck = null;
            removeFromForReturnList = null;
            keySubStrLength = null;
            keyTypeWord = null;
            keyTagLetter = null;
        }
    }
    /**
     * 
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @return 
     * ConcurrentHashMap{@code <String, String> (3) - <hexWord (tagFileName), subString>}
     */
    protected ConcurrentHashMap<String, String> getTypeWordTagFileNameData(
            final Integer typeWordInputed, 
            final String tagNameInputed, 
            final String strSubStringInputed){
        

        //(1)
        ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, String>>> getListByTypeWord;
        //(2a)
        ConcurrentHashMap<Integer, 
                ConcurrentHashMap<String, String>> getListByTagNameCode;
        //(2b)
        ConcurrentHashMap<String, String> getListBySubStrLength;
        String substringTagName;
        Integer strSubStringlength;
        Integer tagNamelength;
        Integer typeWordFunc;
        String tagNameFunc;
        String strSubStringFunc;
        try{
            typeWordFunc = (Integer) typeWordInputed;
            tagNameFunc = (String) tagNameInputed;
            strSubStringFunc = (String) strSubStringInputed;
            strSubStringlength = strSubStringFunc.length();
            tagNamelength = tagNameFunc.length();
            if( (strSubStringlength * 4) != tagNamelength ){
                throw new IllegalArgumentException(ThStorageWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " lengthHex: " + tagNameFunc.length()
                        + " strSubString: " + strSubStringFunc + " lengthStr: " + strSubStringFunc.length()
                        + " lengthHex == lengthStr * 4 ");
            }
            if( tagNamelength < 4 ){
                throw new IllegalArgumentException(ThStorageWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameInputed + " length: " + tagNameInputed.length()
                        + " < 4 ");
            }
            
            getListByTypeWord = getListByType(typeWordFunc);
            
            substringTagName = new String(tagNameFunc.substring(0, 3));
            getListByTagNameCode = getListByTypeWord.get(substringTagName);
            if( getListByTagNameCode == null ){
                getListByTagNameCode = new ConcurrentHashMap<Integer, 
                                                ConcurrentHashMap<String, String>> ();
                getListByTypeWord.put(substringTagName, getListByTagNameCode);
            }
            getListBySubStrLength = getListByTagNameCode.get(strSubStringlength);
            if( getListBySubStrLength == null ){
                getListBySubStrLength = new ConcurrentHashMap<String, String>();
                getListByTagNameCode.put(strSubStringlength, getListBySubStrLength);
            }
            
            return getListBySubStrLength;
        } finally {
            typeWordFunc = null;
            tagNameFunc = null;
            strSubStringFunc = null;
            strSubStringlength = null;
            tagNamelength = null;
            getListByTypeWord = null;
            substringTagName = null;
            getListByTagNameCode = null;
            getListBySubStrLength = null;
        }
    }
    /**
     * Remove and return data from cache
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @return 
     * ConcurrentHashMap<String, String> (3) - <hexWord (tagFileName), subString>
     */
    protected ConcurrentHashMap<String, String> pollTypeWordTagFileNameData(
            final Integer typeWord, 
            final String tagName, 
            final String strSubString){
        

        //(1)
        ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, String>>> getListByTypeWord;
        //(2a)
        ConcurrentHashMap<Integer, 
                ConcurrentHashMap<String, String>> getListByTagNameCode;
        //(2b)
        ConcurrentHashMap<String, String> getListBySubStrLength;
        
        Integer typeWordFunc;
        String tagNameFunc;
        String strSubStringFunc;
        try{
            typeWordFunc = (Integer) typeWord;
            tagNameFunc = (String) tagName;
            strSubStringFunc = (String) strSubString;
            
            
            int strSubStringlength = strSubStringFunc.length();
            int tagNamelength = tagNameFunc.length();
            if( (strSubStringlength * 4) != tagNamelength ){
                throw new IllegalArgumentException(ThStorageWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " lengthHex: " + tagNameFunc.length()
                        + " strSubString: " + strSubStringFunc + " lengthStr: " + strSubStringFunc.length()
                        + " lengthHex == lengthStr * 4 ");
            }
            if( tagNamelength < 4 ){
                throw new IllegalArgumentException(ThStorageWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " length: " + tagNameFunc.length()
                        + " < 4 ");
            }
            /**
             * @todo fix for ... not new create, throw NullPointer...
             */
            getListByTypeWord = getListByType(typeWordFunc);
            String substringTagName = tagNameFunc.substring(0, 3);
            getListByTagNameCode = getListByTypeWord.get(substringTagName);
            if( getListByTagNameCode == null ){
                getListByTagNameCode = new ConcurrentHashMap<Integer, 
                                                ConcurrentHashMap<String, String>> ();
                getListByTypeWord.put(substringTagName, getListByTagNameCode);
            }
            Boolean returnFormCacheNull = Boolean.FALSE;
            
            getListBySubStrLength = null;
            try{
                getListBySubStrLength = getListByTagNameCode.remove(strSubStringlength);
            } catch (NullPointerException exCache) {
                String message = exCache.getMessage();
                returnFormCacheNull = Boolean.TRUE;
            }
            if( getListBySubStrLength == null ){
                throw new NullPointerException(ThStorageWordCache.class.getCanonicalName() 
                        + " for word by type " + String.valueOf(typeWordFunc)
                        + " tagName " + tagNameFunc
                        + " subString " + strSubStringFunc
                        + " data in cache is null");
            }
            if( returnFormCacheNull ){
                throw new NullPointerException(ThStorageWordCache.class.getCanonicalName() 
                        + " for word by type " + String.valueOf(typeWordFunc)
                        + " tagName " + tagNameFunc
                        + " subString " + strSubStringFunc
                        + " data in cache is null");
            }
            
            return getListBySubStrLength;
        } finally {
            typeWordFunc = null;
            tagNameFunc = null;
            strSubStringFunc = null;
            
            getListByTypeWord = null;
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
    protected ConcurrentHashMap<String, String> getTagFileNameParams(
            final ConcurrentHashMap<String, ConcurrentHashMap<String, String>> inputedListByTypeWord,
            final String tagName){
        ConcurrentHashMap<String, String> funcListByTagFileName;
        ConcurrentHashMap<String, ConcurrentHashMap<String, String>> funcListByTypeWord;
        try{
            funcListByTypeWord = (ConcurrentHashMap<String, ConcurrentHashMap<String, String>>) inputedListByTypeWord;
            funcListByTagFileName = inputedListByTypeWord.get(tagName);
            if( funcListByTagFileName == null ){
                funcListByTagFileName = new ConcurrentHashMap<String, String>();
                funcListByTypeWord.put(tagName, funcListByTagFileName);
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
            return funcListByTagFileName;
        } finally {
            funcListByTagFileName = null;
            funcListByTypeWord = null;
        }
    }
    /**
     * return list of not limited files from structure
     * @param typeWordOuter
     * @return 
     *   ConcurrentHashMap<String,    - (2a) - tagFileName.substring(0,3)
     *     ConcurrentHashMap<Integer, - (2b) - subString.length                            
     *     ConcurrentHashMap<String, String> - <hexWord (tagFileName), subString>
     */
    protected ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, String>>> getListByType(final int typeWordOuter){
        ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, String>>> forListReturn;
        try{
            forListReturn = this.cachedData.get(typeWordOuter);
            if( forListReturn == null ){
                forListReturn = new ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, String>>>();
                this.cachedData.put(typeWordOuter, forListReturn);
            }
            return forListReturn;
        } finally {
            forListReturn = null;
        }
    }
    /**
     * 
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @param keysPointsFlow ConcurrentHashMap<String, String>
     *          <ThStorageWordStatusDataFs.hashCode(), recordUUID>
     *          <ThStorageWordStatusName.hashCode(), recordUUID>
     *          <ThStorageWordStatusActivity.hashCode(), recordUUID>
     *          <ThStorageWordStatusDataCache.hashCode(), recordUUID>
     *          <ThStorageWordStatusWorkers.hashCode(), recordUUID>
     */
    protected Boolean setDataIntoCacheFlow(
            final Integer typeWord, 
            final String tagName, 
            final String strSubString){
        Integer funcTypeWord;
        String funcSubString;
        String funcHexTagName;
        ConcurrentHashMap<String, String> inputedData;
        ConcurrentHashMap<String, String> typeWordTagFileNameFlowUuids;
        try {
            funcTypeWord = (Integer) typeWord;
            funcSubString = (String) strSubString;
            funcHexTagName = (String) tagName;
            try{
                typeWordTagFileNameFlowUuids = getTypeWordTagFileNameData(
                        funcTypeWord,
                        funcHexTagName,
                        funcSubString);
            } catch(IllegalArgumentException exSetInCahe) {
                System.err.println(exSetInCahe.getMessage());
                return Boolean.FALSE;
            }
            inputedData = new ConcurrentHashMap<String, String>();
            inputedData.put(funcHexTagName, funcSubString);
            typeWordTagFileNameFlowUuids.putAll(inputedData);
            return Boolean.TRUE;
        } finally {
            typeWordTagFileNameFlowUuids = null;
            inputedData = null;
            funcTypeWord = null;
            funcSubString = null;
            funcHexTagName = null;
        }
    }
    
    /**
     * 
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @param outerInputedData - readed list data for insert into cacheReaded
     * @param keysPointsFlow ConcurrentHashMap<String, String>
     *          <ThStorageWordStatusDataFs.hashCode(), recordUUID>
     *          <ThStorageWordStatusName.hashCode(), recordUUID>
     *          <ThStorageWordStatusActivity.hashCode(), recordUUID>
     *          <ThStorageWordStatusDataCache.hashCode(), recordUUID>
     *          <ThStorageWordStatusWorkers.hashCode(), recordUUID>
     */
    protected Boolean addAllDataIntoCache(
            final Integer typeWord, 
            final String tagName, 
            final String strSubString,
            final ConcurrentHashMap<String, String> outerInputedData){
        Integer funcTypeWord;
        String funcSubString;
        String funcHexTagName;
        ConcurrentHashMap<String, String> inputedData;
        ConcurrentHashMap<String, String> typeWordTagFileNameFlowUuids;
        try {
            
            inputedData = (ConcurrentHashMap<String, String>) outerInputedData;
            
            if( inputedData == null ) {
                return Boolean.FALSE;
            }
            
            if( inputedData.isEmpty() ){
                return Boolean.FALSE;
            }
            
            funcTypeWord = (Integer) typeWord;
            funcSubString = (String) strSubString;
            funcHexTagName = (String) tagName;
            try{
                typeWordTagFileNameFlowUuids = getTypeWordTagFileNameData(
                        funcTypeWord,
                        funcHexTagName,
                        funcSubString);
            } catch(IllegalArgumentException exSetInCahe) {
                System.err.println(exSetInCahe.getMessage());
                return Boolean.FALSE;
            }
            
            
            typeWordTagFileNameFlowUuids.putAll(inputedData);
            return Boolean.TRUE;
        } finally {
            typeWordTagFileNameFlowUuids = null;
            inputedData = null;
            funcTypeWord = null;
            funcSubString = null;
            funcHexTagName = null;
        }
    }
    /**
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @param keysPointsFlow ConcurrentHashMap<String, String>
     *          <ThStorageWordStatusDataFs.hashCode(), recordUUID>
     *          <ThStorageWordStatusName.hashCode(), recordUUID>
     *          <ThStorageWordStatusActivity.hashCode(), recordUUID>
     *          <ThStorageWordStatusDataCache.hashCode(), recordUUID>
     *          <ThStorageWordStatusWorkers.hashCode(), recordUUID>
     */
    protected Boolean isCacheHasData(
            final Integer typeWord, 
            final String tagName, 
            final String strSubString){
        Integer funcTypeWord;
        String funcSubString;
        String funcHexTagName;
        ConcurrentHashMap<String, String> typeWordTagFileNameFlowUuids;
        try {
            funcTypeWord = (Integer) typeWord;
            funcSubString = (String) strSubString;
            funcHexTagName = (String) tagName;
            try{
                typeWordTagFileNameFlowUuids = (ConcurrentHashMap<String, String>) getTypeWordTagFileNameData(
                        funcTypeWord,
                        funcHexTagName,
                        funcSubString);
            } catch(IllegalArgumentException exSetInCahe) {
                System.err.println(exSetInCahe.getMessage());
                return Boolean.FALSE;
            }
            if( typeWordTagFileNameFlowUuids == null ) {
                return Boolean.FALSE;
            }
            
            if( typeWordTagFileNameFlowUuids.isEmpty() ){
                return Boolean.FALSE;
            }
            
            return Boolean.TRUE;
        } finally {
            typeWordTagFileNameFlowUuids = null;
            funcTypeWord = null;
            funcSubString = null;
            funcHexTagName = null;
        }
    }
    /**
     * 
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @param keysPointsFlow ConcurrentHashMap<String, String>
     *          <ThStorageWordStatusDataFs.hashCode(), recordUUID>
     *          <ThStorageWordStatusName.hashCode(), recordUUID>
     *          <ThStorageWordStatusActivity.hashCode(), recordUUID>
     *          <ThStorageWordStatusDataCache.hashCode(), recordUUID>
     *          <ThStorageWordStatusWorkers.hashCode(), recordUUID>
     */
    protected Integer sizeDataInCache(
            final Integer typeWord, 
            final String tagName, 
            final String strSubString){
        Integer funcTypeWord;
        String funcSubString;
        String funcHexTagName;
        ConcurrentHashMap<String, String> inputedData;
        ConcurrentHashMap<String, String> typeWordTagFileNameFlowUuids;
        Integer returnedValue;
        try {
            funcTypeWord = (Integer) typeWord;
            funcSubString = (String) strSubString;
            funcHexTagName = (String) tagName;
            returnedValue = 0;
            try{
                typeWordTagFileNameFlowUuids = getTypeWordTagFileNameData(
                        funcTypeWord,
                        funcHexTagName,
                        funcSubString);
            } catch(IllegalArgumentException exSetInCahe) {
                System.err.println(exSetInCahe.getMessage());
                exSetInCahe.printStackTrace();
                return returnedValue;
            }
            returnedValue = (int) typeWordTagFileNameFlowUuids.size();
            return returnedValue;
        } finally {
            typeWordTagFileNameFlowUuids = null;
            inputedData = null;
            funcTypeWord = null;
            funcSubString = null;
            funcHexTagName = null;
            returnedValue = null;
        }
    }
    protected void printCacheData(){
        for( Map.Entry<Integer,ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>>>> cachedTypes : this.cachedData.entrySet()){
            for(Map.Entry<String, ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>>> hexSubByte : cachedTypes.getValue().entrySet()){
                for(Map.Entry<Integer, ConcurrentHashMap<String, String>> itemLength : hexSubByte.getValue().entrySet()){
                    for(Map.Entry<String, String> itemData : itemLength.getValue().entrySet()){
                        System.out.println(" -  -  -  -  -   -      -     -     -       -    -   -  hexName " 
                                + itemData.getKey() 
                                + " subStr " 
                                + itemData.getValue());
                    }
                }
            }
        
        }
    }
    /**
     * isCacheEmpty
     */
    /**
     * return CacheDataForFinishedWrite by bus type
     */
}
