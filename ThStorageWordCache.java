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
 *
 * @author wladimirowichbiaran
 */
public class ThStorageWordCache {
    private ConcurrentHashMap<Integer, 
                ConcurrentHashMap<String, 
                    ConcurrentHashMap<Integer, 
                        ConcurrentHashMap<String, 
                            ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>>>>> cachedData;
    
    public ThStorageWordCache() {
        this.cachedData = createNewListStoragesMapEmpty();
    }
    protected ConcurrentHashMap<Integer, ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<String, 
                            ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>>>>> createNewListStoragesMapEmpty(){
        return new ConcurrentHashMap<Integer, 
                        ConcurrentHashMap<String, 
                            ConcurrentHashMap<Integer, 
                                ConcurrentHashMap<String, 
                                    ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>>>>>();
    }
    protected ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>> getTypeWordTagFileNameFlowUuids(
            final Integer typeWord, 
            final String tagName, 
            final String strSubString){
        
        //(3)
        ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>> tagFileNameParams;
        //(1)
        ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, 
                        ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>>>> getListByTypeWord;
        //(2a)
        ConcurrentHashMap<Integer, 
                ConcurrentHashMap<String, 
                        ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>>> getListByTagNameCode;
        //(2b)
        ConcurrentHashMap<String, 
                        ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>> getListBySubStrLength;
        
        try{
            int strSubStringlength = strSubString.length();
            int tagNamelength = tagName.length();
            if( (strSubStringlength * 4) != tagNamelength ){
                throw new IllegalArgumentException(ThStorageWordStatistic.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagName + " lengthHex: " + tagName.length()
                        + " strSubString: " + strSubString + " lengthStr: " + strSubString.length()
                        + " lengthHex == lengthStr * 4 ");
            }
            if( tagNamelength < 4 ){
                throw new IllegalArgumentException(ThStorageWordStatistic.class.getCanonicalName() 
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
                                                        ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>>>();
                getListByTypeWord.put(substringTagName, getListByTagNameCode);
            }
            getListBySubStrLength = getListByTagNameCode.get(strSubStringlength);
            if( getListBySubStrLength == null ){
                getListBySubStrLength = new ConcurrentHashMap<String, 
                                                ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>>();
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
    protected ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>> getTagFileNameParams(
            final ConcurrentHashMap<String, ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>> getListByTypeWord,
            final String tagName){
        ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>> getListByTagFileName;
        try{
            getListByTagFileName = getListByTypeWord.get(tagName);
            if( getListByTagFileName == null ){
                getListByTagFileName = new ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>();
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
     * return list of not limited files from structure
     * @param typeWordOuter
     * @return 
     */
    protected ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, 
                        ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>>>> getListByType(final int typeWordOuter){
        ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, 
                        ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>>>> forListReturn;
        try{
            forListReturn = this.cachedData.get(typeWordOuter);
            if( forListReturn == null ){
                forListReturn = new ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, 
                        ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>>>>();
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
     * @param keysPointsFlow ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>
     *          <ThStorageWordStatusDataFs.hashCode(), recordUUID>
     *          <ThStorageWordStatusName.hashCode(), recordUUID>
     *          <ThStorageWordStatusActivity.hashCode(), recordUUID>
     *          <ThStorageWordStatusDataCache.hashCode(), recordUUID>
     *          <ThStorageWordStatusWorkers.hashCode(), recordUUID>
     */
    protected void setDataIntoCacheFlow(
            final UUID keyFlowStatusDataCacheInputed,
            final Integer typeWord, 
            final String tagName, 
            final String strSubString){
        UUID keyFlowStatusDataCacheFunc;
        Integer funcTypeWord;
        String funcSubString;
        String funcHexTagName;
        ConcurrentHashMap<String, String> inputedData;
        ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>> typeWordTagFileNameFlowUuids;
        try {
            funcTypeWord = typeWord;
            funcSubString = strSubString;
            funcHexTagName = tagName;
            keyFlowStatusDataCacheFunc = keyFlowStatusDataCacheInputed;
            typeWordTagFileNameFlowUuids = getTypeWordTagFileNameFlowUuids(
                    funcTypeWord,
                    funcHexTagName,
                    funcSubString);
            inputedData = new ConcurrentHashMap<String, String>();
            inputedData.put(funcHexTagName, funcSubString);
            typeWordTagFileNameFlowUuids.put(keyFlowStatusDataCacheFunc, inputedData);
        } finally {
            typeWordTagFileNameFlowUuids = null;
            inputedData = null;
            funcTypeWord = null;
            funcSubString = null;
            funcHexTagName = null;
            keyFlowStatusDataCacheFunc = null;
        }
    }
    protected void printCacheData(){
        for( Map.Entry<Integer,ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConcurrentHashMap<String, ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>>>>> cachedTypes : this.cachedData.entrySet()){
            for(Map.Entry<String, ConcurrentHashMap<Integer, ConcurrentHashMap<String, ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>>>> hexSubByte : cachedTypes.getValue().entrySet()){
                for(Map.Entry<Integer, ConcurrentHashMap<String, ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>>> itemLength : hexSubByte.getValue().entrySet()){
                    for(Map.Entry<String, ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>> itemTagName : itemLength.getValue().entrySet()){
                        for(Map.Entry<UUID, ConcurrentHashMap<String, String>> itemCaheKey : itemTagName.getValue().entrySet()){
                            for(Map.Entry<String, String> itemData : itemCaheKey.getValue().entrySet()){
                                System.out.println(
                                        " -  -  -  -  -   -      -     -     -       -    -   -  keysCacheData " 
                                                + itemCaheKey.getKey().toString() + " hexName " + itemData.getKey() + " subStr " + itemData.getValue());
                            }
                        }
                    }
                }
            }
        
        }
    }
}
