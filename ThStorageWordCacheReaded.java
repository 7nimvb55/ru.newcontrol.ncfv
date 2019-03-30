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
public class ThStorageWordCacheReaded {
    
    private ConcurrentHashMap<Integer, 
            ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, String>>>> cachedDataReaded;
    
    public ThStorageWordCacheReaded() {
        this.cachedDataReaded = createNewListStoragesMapEmpty();
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
     * 
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @return 
     * ConcurrentHashMap<String, String> (3) - <hexWord (tagFileName), subString>
     */
    protected ConcurrentHashMap<String, String> getTypeWordTagFileNameData(
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
            getListByTypeWord = null;
            
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
                throw new NullPointerException(ThStorageWordCacheReaded.class.getCanonicalName() 
                        + " for word by type " + String.valueOf(typeWordFunc)
                        + " tagName " + tagNameFunc
                        + " subString " + strSubStringFunc
                        + " data in cache is null");
            }
            if( returnFormCacheNull ){
                throw new NullPointerException(ThStorageWordCacheReaded.class.getCanonicalName() 
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
            forListReturn = this.cachedDataReaded.get(typeWordOuter);
            if( forListReturn == null ){
                forListReturn = new ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, String>>>();
                this.cachedDataReaded.put(typeWordOuter, forListReturn);
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
     * @param outerInputedData - readed list data for insert into cacheReaded
     * @param keysPointsFlow ConcurrentHashMap<String, String>
     *          <ThStorageWordStatusDataFs.hashCode(), recordUUID>
     *          <ThStorageWordStatusName.hashCode(), recordUUID>
     *          <ThStorageWordStatusActivity.hashCode(), recordUUID>
     *          <ThStorageWordStatusDataCache.hashCode(), recordUUID>
     *          <ThStorageWordStatusWorkers.hashCode(), recordUUID>
     */
    protected Boolean addAllDataIntoCacheReaded(
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
    protected Boolean isCacheReadedHasData(
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
    protected Integer sizeDataInCacheReaded(
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
        for( Map.Entry<Integer,ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>>>> cachedTypes : this.cachedDataReaded.entrySet()){
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
}
