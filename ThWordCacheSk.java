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
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;


public class ThWordCacheSk {
    /**
     * {@code <Integer,  <String,                          <Integer,           
     *             <String,     <UUID,                 TdataWord>>>>>}
     * {@code <typeWord, <tagHexNameLetter.substring(0,3), <subStrName.length, 
     *             <tagHexName, <TdataWord.randomUUID, TdataWord>>>>>}
     */
    private ConcurrentSkipListMap<Integer, 
            ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>>>> cachedData;
    
    public ThWordCacheSk() {
        this.cachedData = createNewListStoragesMapEmpty();
    }
    private ConcurrentSkipListMap<Integer, 
        ConcurrentSkipListMap<String, 
            ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>>>> createNewListStoragesMapEmpty(){
        return new ConcurrentSkipListMap<Integer, 
                        ConcurrentSkipListMap<String, 
                            ConcurrentSkipListMap<Integer, 
                                ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>>>>();
    }
    /**
     * @todo remove keys for empty lists
     */
    /**
     * remove and return list of data from cache, remove empty keys
     * {@code <typeWord, hexTagName>}
     * code func for poll by typeWord and hexTagName
     */
    /**
     * @todo after rerelease that, rebuild in Storage Word System
     * When all sources data has bin sended into index system Word, and processed, need
     * save to storage all cached data, 
     * this function return all cahed data for save to storage
     * @return {@code ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<String, String>>}
     *                         <p>{@code <TypeWord, <TagName, SubString>>}
     */
    protected ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<UUID, TdataWord>>>>> pollAllData(){
        
        Integer keyTypeWordList;
        ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<UUID, TdataWord>>>>> createNewListStoragesMapCleaned;
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<UUID, TdataWord>>>> removeIfNullCachedData;
        ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<Integer, 
                        ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<UUID, TdataWord>>>> valueTypeWordList;
        ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<Integer, 
                        ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<UUID, TdataWord>>>> valueTypeWordListCleaned;
        ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<UUID, TdataWord>>> removeIfNullTypeWordList;
        String keyHexTagNameLetter;
        ConcurrentSkipListMap<Integer,
                        ConcurrentSkipListMap<String,
                        ConcurrentSkipListMap<UUID, TdataWord>>> valueHexTagNameLetter;
        ConcurrentSkipListMap<Integer,
                        ConcurrentSkipListMap<String,
                        ConcurrentSkipListMap<UUID, TdataWord>>> valueHexTagNameLetterCleaned;
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<UUID, TdataWord>> removeIfNullHexTagNameLetter;
        Integer keySubStringLength;
        ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<UUID, TdataWord>> valueSubStringLength;
        ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<UUID, TdataWord>> valueSubStringLengthCleaned;
        ConcurrentSkipListMap<UUID, TdataWord> removeSubStringLength;
        String keyHexTagName;
        ConcurrentSkipListMap<UUID, TdataWord> valueHexTagName;
        ConcurrentSkipListMap<UUID, TdataWord> dataByHexTagName;
        UUID keyData;
        TdataWord removedData;
        TdataWord removeIfNullData;
        try {
            createNewListStoragesMapCleaned = createNewListStoragesMapEmpty();
            for( Map.Entry<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<UUID, TdataWord>>>>> entrySetTypeWord : this.cachedData.entrySet() ){
                
                keyTypeWordList = (Integer) entrySetTypeWord.getKey();
                
                valueTypeWordList = (ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<Integer,
                        ConcurrentSkipListMap<String,
                        ConcurrentSkipListMap<UUID, TdataWord>>>>) entrySetTypeWord.getValue();
                if( keyTypeWordList == null ){
                    continue;
                }
                if( valueTypeWordList == null ){
                    
                    removeIfNullCachedData = this.cachedData.remove(keyTypeWordList);
                    removeIfNullCachedData = null;
                    keyTypeWordList = null;
                    continue;
                }
                if( valueTypeWordList.isEmpty() ){
                    continue;
                }
                valueTypeWordListCleaned = new ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<UUID, TdataWord>>>>();
                for( Map.Entry<String, 
                    ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<UUID, TdataWord>>>> entrySetHexTagNameLetter : valueTypeWordList.entrySet() ){
                    
                    keyHexTagNameLetter = entrySetHexTagNameLetter.getKey();
                    valueHexTagNameLetter = (ConcurrentSkipListMap<Integer,
                        ConcurrentSkipListMap<String,
                        ConcurrentSkipListMap<UUID, TdataWord>>>) entrySetHexTagNameLetter.getValue();
                    if( keyHexTagNameLetter == null ){
                        continue;
                    }
                    if( valueHexTagNameLetter == null ){
                        
                        removeIfNullTypeWordList = valueTypeWordList.remove(keyHexTagNameLetter);
                        removeIfNullTypeWordList = null;
                        keyHexTagNameLetter = null;
                        continue;
                    }
                    if( valueHexTagNameLetter.isEmpty() ){
                        continue;
                    }
                    valueHexTagNameLetterCleaned = new ConcurrentSkipListMap<Integer, 
                        ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<UUID, TdataWord>>>();
                    for( Map.Entry<Integer, 
                        ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<UUID, TdataWord>>> entrySetSubStringLength : valueHexTagNameLetter.entrySet() ){
                        
                        keySubStringLength = (Integer) entrySetSubStringLength.getKey();
                        valueSubStringLength = (ConcurrentSkipListMap<String, 
                                                    ConcurrentSkipListMap<UUID, TdataWord>>) entrySetSubStringLength.getValue();
                        if( keySubStringLength == null ){
                            continue;
                        }
                        if( valueSubStringLength == null ){
                            
                            removeIfNullHexTagNameLetter = valueHexTagNameLetter.remove(keySubStringLength);
                            removeIfNullHexTagNameLetter = null;
                            keySubStringLength = null;
                            continue;
                        }
                        if( valueSubStringLength.isEmpty() ){
                            continue;
                        }
                        valueSubStringLengthCleaned = new ConcurrentSkipListMap<String, 
                            ConcurrentSkipListMap<UUID, TdataWord>>();
                        for( Map.Entry<String, 
                            ConcurrentSkipListMap<UUID, TdataWord>> entrySetHexTagName : valueSubStringLength.entrySet() ){
                            keyHexTagName = (String) entrySetHexTagName.getKey();
                            valueHexTagName = (ConcurrentSkipListMap<UUID, TdataWord>) entrySetHexTagName.getValue();
                            if( keyHexTagName == null ){
                                continue;
                            }
                            if( valueHexTagName == null ){
                                
                                removeSubStringLength = valueSubStringLength.remove(keyHexTagName);
                                removeSubStringLength = null;
                                keyHexTagName = null;
                                continue;
                            }
                            if( valueHexTagName.isEmpty() ){
                                continue;
                            }
                            dataByHexTagName = new ConcurrentSkipListMap<UUID, TdataWord>();
                            for( Map.Entry<UUID, TdataWord> itemFromHexTagNameList : valueHexTagName.entrySet() ){
                                keyData = (UUID) itemFromHexTagNameList.getKey();
                                removedData = (TdataWord) valueHexTagName.remove(keyData);
                                if( keyData == null ){
                                    continue;
                                }
                                if( removedData == null ){
                                    
                                    removeIfNullData = (TdataWord) valueHexTagName.remove(keyData);
                                    removeIfNullData = null;
                                    keyData = null;
                                    continue;
                                }
                                dataByHexTagName.put(keyData, removedData);
                            }
                            valueSubStringLengthCleaned.put(keyHexTagName, dataByHexTagName);
                        }
                        valueHexTagNameLetterCleaned.put(keySubStringLength, valueSubStringLengthCleaned);
                    }
                    valueTypeWordListCleaned.put(keyHexTagNameLetter, valueHexTagNameLetterCleaned);
                }
                createNewListStoragesMapCleaned.put(keyTypeWordList, valueTypeWordListCleaned);
            }
            return createNewListStoragesMapCleaned;
        }
        finally {
            keyTypeWordList = null;
            createNewListStoragesMapCleaned = null;
            removeIfNullCachedData = null;
            valueTypeWordList = null;
            valueTypeWordListCleaned = null;
            removeIfNullTypeWordList = null;
            keyHexTagNameLetter = null;
            valueHexTagNameLetter = null;
            valueHexTagNameLetterCleaned = null;
            removeIfNullHexTagNameLetter = null;
            keySubStringLength = null;
            valueSubStringLength = null;
            valueSubStringLengthCleaned = null;
            removeSubStringLength = null;
            keyHexTagName = null;
            valueHexTagName = null;
            dataByHexTagName = null;
            keyData = null;
            removedData = null;
            removeIfNullData = null;
        }
    }
    protected void cleanKeyForEmptyLists(){
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<UUID, TdataWord>>>> valueTypeWordList;
        Integer keyRemovedTypeWordList;
        ConcurrentSkipListMap<String, ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>>> valueRemovedTypeWordList;
        ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<UUID, TdataWord>>> valueHexTagNameLetter;
        String keyRemovedHexTagNameLetter;
        ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>> valueRemovedHexTagNameLetter;
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<UUID, TdataWord>> valueSubStringLength;
        Integer keyRemovedSubStringLength;
        ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>> valueRemovedSubStringLength;
        ConcurrentSkipListMap<UUID, TdataWord> dataByHexTagName;
        String keyRemovedHexTagName;
        ConcurrentSkipListMap<UUID, TdataWord> valueRemovedHexTagName;
        try{
            for( Map.Entry<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<UUID, TdataWord>>>>> entrySetTypeWord : this.cachedData.entrySet() ){
                
                valueTypeWordList = entrySetTypeWord.getValue();
                for( Map.Entry<String, 
                    ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<UUID, TdataWord>>>> entrySetHexTagNameLetter : valueTypeWordList.entrySet() ){
                    
                    valueHexTagNameLetter = entrySetHexTagNameLetter.getValue();
                    for( Map.Entry<Integer, 
                        ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<UUID, TdataWord>>> entrySetSubStringLength : valueHexTagNameLetter.entrySet() ){
                        
                        valueSubStringLength = entrySetSubStringLength.getValue();
                        for( Map.Entry<String, 
                            ConcurrentSkipListMap<UUID, TdataWord>> entrySetHexTagName : valueSubStringLength.entrySet() ){
                            
                            dataByHexTagName = entrySetHexTagName.getValue();
                            if( dataByHexTagName.isEmpty() ){
                                
                                keyRemovedHexTagName = entrySetHexTagName.getKey();
                                valueRemovedHexTagName = valueSubStringLength.remove(keyRemovedHexTagName);
                                valueRemovedHexTagName = null;
                                keyRemovedHexTagName = null;
                            }
                        }
                        if( valueSubStringLength.isEmpty() ){
                                
                                keyRemovedSubStringLength = entrySetSubStringLength.getKey();
                                valueRemovedSubStringLength = valueHexTagNameLetter.remove(keyRemovedSubStringLength);
                                valueRemovedSubStringLength = null;
                                keyRemovedSubStringLength = null;
                        }
                    }
                    if( valueHexTagNameLetter.isEmpty() ){
                                
                        keyRemovedHexTagNameLetter = entrySetHexTagNameLetter.getKey();
                        valueRemovedHexTagNameLetter = valueTypeWordList.remove(keyRemovedHexTagNameLetter);
                        valueRemovedHexTagNameLetter = null;
                        keyRemovedHexTagNameLetter = null;
                    }
                }
                if( valueTypeWordList.isEmpty() ){
                                
                    keyRemovedTypeWordList = entrySetTypeWord.getKey();
                    valueRemovedTypeWordList = this.cachedData.remove(keyRemovedTypeWordList);
                    valueRemovedTypeWordList = null;
                    keyRemovedTypeWordList = null;
                }
            }
        }
        finally {
            valueTypeWordList = null;
            valueHexTagNameLetter = null;
            valueSubStringLength = null;
            dataByHexTagName = null;
            valueRemovedTypeWordList = null;
            keyRemovedTypeWordList = null;
            valueRemovedHexTagNameLetter = null;
            keyRemovedHexTagNameLetter = null;
            valueRemovedSubStringLength = null;
            keyRemovedSubStringLength = null;
            valueRemovedHexTagName = null;
            keyRemovedHexTagName = null;
        }
    }
    /**
     * 
     * @return 
     */
    protected Boolean isCacheEmpty(){
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<UUID, TdataWord>>>> valueTypeWordList;
        ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<UUID, TdataWord>>> valueHexTagNameLetter;
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<UUID, TdataWord>> valueSubStringLength;
        ConcurrentSkipListMap<UUID, TdataWord> dataByHexTagName;
        try{
            for( Map.Entry<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<UUID, TdataWord>>>>> entrySetTypeWord : this.cachedData.entrySet() ){
                
                valueTypeWordList = entrySetTypeWord.getValue();
                for( Map.Entry<String, 
                    ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<UUID, TdataWord>>>> entrySetHexTagNameLetter : valueTypeWordList.entrySet() ){
                    
                    valueHexTagNameLetter = entrySetHexTagNameLetter.getValue();
                    for( Map.Entry<Integer, 
                        ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<UUID, TdataWord>>> entrySetSubStringLength : valueHexTagNameLetter.entrySet() ){
                        
                        valueSubStringLength = entrySetSubStringLength.getValue();
                        for( Map.Entry<String, 
                            ConcurrentSkipListMap<UUID, TdataWord>> entrySetHexTagName : valueSubStringLength.entrySet() ){
                            
                            dataByHexTagName = entrySetHexTagName.getValue();
                            if( !dataByHexTagName.isEmpty() ){
                                return Boolean.FALSE;
                            }
                        }
                    }
                }
            }
            return Boolean.TRUE;
        }
        finally {
            valueTypeWordList = null;
            valueHexTagNameLetter = null;
            valueSubStringLength = null;
            dataByHexTagName = null;
        }
    }
    /**
     * 
     * @param dataInputed
     * @return 
     * @throws IllegalArgumentException when inputed data not valid
     */
    private ConcurrentSkipListMap<UUID, TdataWord> getDataByDataWord(final TdataWord dataInputed){
        ConcurrentSkipListMap<UUID, TdataWord> dataTypeWordTagNameSubStr;
        TdataWord dataFunc;
        String tagNameFunc;
        String strSubStringFunc;
        Integer typeWordFunc;
        Boolean tdataWordValid;
        try {
            dataFunc = (TdataWord) dataInputed;
            
            tdataWordValid = ThWordHelper.isTdataWordValid(dataFunc);
            if( !tdataWordValid ){
                throw new IllegalArgumentException(ThWordCacheHa.class.getCanonicalName() 
                        + " not valid data for get from cache object class " + TdataWord.class.getCanonicalName() 
                        + " object data " + dataFunc.toString());
            }
            tagNameFunc = dataFunc.hexSubString;
            strSubStringFunc = dataFunc.strSubString;
            typeWordFunc = dataFunc.typeWord;
            
            dataTypeWordTagNameSubStr = getDataTypeWordTagNameSubStr(typeWordFunc, strSubStringFunc, tagNameFunc);
            return dataTypeWordTagNameSubStr;
        }
        finally {
            dataFunc = null;
            tagNameFunc = null;
            strSubStringFunc = null;
            typeWordFunc = null;
            dataTypeWordTagNameSubStr = null;
            tdataWordValid = null;
        }
    }
    /**
     * 
     * @param typeWordInput
     * @param strSubStringInput
     * @param tagHexNameInput
     * @return 
     * ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>> (3) - 
     * <hexWord (tagFileName), <UUID, TdataWord>>
     */
    private ConcurrentSkipListMap<UUID, TdataWord> getDataTypeWordTagNameSubStr(
            final Integer typeWordInput,
            final String strSubStringInput,
            final String tagHexNameInput){
        //(1)
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>>> valListByTypeWord;
        //(2a)
        ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>> valListByTagNameCode;
        //(2b)
        ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>> valListBySubStrLength;
        ConcurrentSkipListMap<UUID, TdataWord> valTagNameListData;
        
        String tagNameFunction;
        String strSubStringFunction;
        Integer typeWordFunction;
        try{
            tagNameFunction = (String) tagHexNameInput;
            strSubStringFunction = (String) strSubStringInput;
            typeWordFunction = (Integer) typeWordInput;
            int strSubStringlength = strSubStringFunction.length();
            int tagNamelength = tagNameFunction.length();
            if( (strSubStringlength * 4) != tagNamelength ){
                throw new IllegalArgumentException(ThWordCacheHa.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunction + " lengthHex: " + tagNameFunction.length()
                        + " strSubString: " + strSubStringFunction + " lengthStr: " + strSubStringFunction.length()
                        + " lengthHex == lengthStr * 4 ");
            }
            if( tagNamelength < 4 ){
                throw new IllegalArgumentException(ThWordCacheHa.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunction + " length: " + tagNameFunction.length()
                        + " < 4 ");
            }
            valListByTypeWord = getListByType(typeWordFunction);
            String substringTagName = tagNameFunction.substring(0, 3);
            valListByTagNameCode = valListByTypeWord.get(substringTagName);
            if( valListByTagNameCode == null ){
                valListByTagNameCode = new ConcurrentSkipListMap<Integer, 
                                                ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>>();
                valListByTypeWord.put(substringTagName, valListByTagNameCode);
            }
            valListBySubStrLength = valListByTagNameCode.get(strSubStringlength);
            if( valListBySubStrLength == null ){
                valListBySubStrLength = new ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>();
                valListByTagNameCode.put(strSubStringlength, valListBySubStrLength);
            }
            valTagNameListData = valListBySubStrLength.get(tagNameFunction);
            if( valTagNameListData == null ){
                valTagNameListData = new ConcurrentSkipListMap<UUID, TdataWord>();
                valListBySubStrLength.put(tagNameFunction, valTagNameListData);
            }
            return valTagNameListData;
        } finally {
            valListByTypeWord = null;
            valListByTagNameCode = null;
            valListBySubStrLength = null;
            valTagNameListData = null;
            tagNameFunction = null;
            strSubStringFunction = null;
            typeWordFunction = null;
        }
    }
    /**
     * 
     * @param dataInputed
     * @return 
     * @throws IllegalArgumentException when inputed data not valid
     */
    protected ConcurrentSkipListMap<UUID, TdataWord> pollDataByDataWord(
            final TdataWord dataInputed){
        ConcurrentSkipListMap<UUID, TdataWord> dataTypeWordTagNameSubStr;
        TdataWord dataFunc;
        String tagNameFunc;
        String strSubStringFunc;
        Integer typeWordFunc;
        Boolean tdataWordValid;
        try {
            dataFunc = (TdataWord) dataInputed;
            tdataWordValid = ThWordHelper.isTdataWordValid(dataFunc);
            if( !tdataWordValid ){
                throw new IllegalArgumentException(ThWordCacheHa.class.getCanonicalName() 
                        + " inputed not valid data for poll from cache object class " 
                        + TdataWord.class.getCanonicalName() 
                        + " object data " + dataFunc.toString());
            }
            tagNameFunc = dataFunc.hexSubString;
            strSubStringFunc = dataFunc.strSubString;
            typeWordFunc = dataFunc.typeWord;
            dataTypeWordTagNameSubStr = null;
            try {
                dataTypeWordTagNameSubStr = pollTypeWordTagFileNameData(typeWordFunc, strSubStringFunc, tagNameFunc);
            } catch (IllegalArgumentException exIll) {
                throw new IllegalArgumentException(exIll.getMessage());
            } catch (NullPointerException exNull) {
                throw new NullPointerException(exNull.getMessage());
            }
            if( dataTypeWordTagNameSubStr == null ){
                throw new NullPointerException(ThWordCacheHa.class.getCanonicalName() 
                        + " for word by type " + String.valueOf(typeWordFunc)
                        + " tagName " + tagNameFunc
                        + " subString " + strSubStringFunc
                        + " data in cache is null");
            }
            return dataTypeWordTagNameSubStr;
        }
        finally {
            dataFunc = null;
            tagNameFunc = null;
            strSubStringFunc = null;
            typeWordFunc = null;
            dataTypeWordTagNameSubStr = null;
            tdataWordValid = null;
        }
    }
    /**
     * poll by typeWord, hexTagName data
     */
    protected ConcurrentSkipListMap<UUID, TdataWord> pollDataByTypeWordHexTagName(
        final Integer typeWordInputed,
        final String hexTagNameInputed){
        
        ConcurrentSkipListMap<UUID, TdataWord> flowReturnedValueFunc;
        ConcurrentSkipListMap<UUID, TdataWord> flowRecivedValueFunc;
        ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<UUID, TdataWord>>>> listTypeWordData;
        ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<UUID, TdataWord>>> listTagNameLetter;
        ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<UUID, TdataWord>> listTagName;
        Map.Entry<UUID, TdataWord> pollFirstEntry;
        Integer typeWordFunc;
        String tagNameFunc;
        String tagNameLetter;
        Integer tagNamelength;
        Integer calculatedSubString;
        try {
            typeWordFunc = (Integer) typeWordInputed;
            tagNameFunc = (String) hexTagNameInputed;
            if( typeWordFunc == null || tagNameFunc == null ){
                throw new NullPointerException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " Main Flow UUID is null");
            }
            tagNamelength = (Integer) tagNameFunc.length();
            if( tagNamelength < 4 ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " length: " + tagNameFunc.length()
                        + " < 4 ");
            }
            calculatedSubString = tagNamelength / 4;
            tagNameLetter = tagNameFunc.substring(0, 3);
            
            listTypeWordData = this.cachedData.get(typeWordFunc);
            if( listTypeWordData == null ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal key value for typeWord: "
                        + typeWordFunc);
            }
            listTagNameLetter = listTypeWordData.get(tagNameLetter);
            if( listTagNameLetter == null ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal key value for tagNameLetter: "
                        + tagNameLetter);
            }
            listTagName = listTagNameLetter.get(calculatedSubString);
            if( listTagName == null ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal key value for subStringLength: "
                        + calculatedSubString);
            }
            flowRecivedValueFunc = listTagName.get(tagNameFunc);
            if(flowRecivedValueFunc == null ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal key value for hexTagName: "
                        + tagNameFunc);
            }
            flowReturnedValueFunc = new ConcurrentSkipListMap<UUID, TdataWord>();
            do {
                pollFirstEntry = flowRecivedValueFunc.pollFirstEntry();
                if( pollFirstEntry != null ){
                    flowReturnedValueFunc.put((UUID) pollFirstEntry.getKey(), (TdataWord) pollFirstEntry.getValue());
                }
            } while( !flowRecivedValueFunc.isEmpty() );
            return flowReturnedValueFunc;
        } finally {
            pollFirstEntry = null;
            flowRecivedValueFunc = null;
            flowReturnedValueFunc = null;
            listTypeWordData = null;
            listTagNameLetter = null;
            listTagName = null;

            typeWordFunc = null;
            tagNameFunc = null;
            tagNamelength = null;
            calculatedSubString = null;
            tagNameLetter = null;
        }
    }
    /**
     * Remove and return data from cache
     * {@code ConcurrentSkipListMap<Integer, 
     *      ConcurrentSkipListMap<String, 
     *           ConcurrentSkipListMap<Integer, 
     *               ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>>>>}
     * {@code <TypeWord,
     *      <hexTagName.substring(0, 3),
     *           <subString.length(),
     *               <hexTagName, --- removed key for list return
     *                   <data.randomUUID, (TdataWord) data>>>>>}
     * @param typeWord
     * @param strSubString
     * @param tagName
     * 
     * @return 
     * ConcurrentSkipListMap<String, String> (3) - <hexWord (tagFileName), subString>
     * @throws IllegalArgumentException when data not valid
     * @throws NullPointerException when data in cache for keys not exist
     */
    protected ConcurrentSkipListMap<UUID, TdataWord> pollTypeWordTagFileNameData(
            final Integer typeWordInput,
            final String strSubStringInput,
            final String tagHexNameInput){
        //(1)
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>>> valListByTypeWord;
        //(2a)
        ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>> valListByTagNameCode;
        //(2b)
        ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>> valListBySubStrLength;
        ConcurrentSkipListMap<UUID, TdataWord> valTagNameListData;
        
        String tagNameFunc;
        String strSubStringFunc;
        Integer typeWordFunc;
        try{
            tagNameFunc = (String) tagHexNameInput;
            strSubStringFunc = (String) strSubStringInput;
            typeWordFunc = (Integer) typeWordInput;
            int strSubStringlength = strSubStringFunc.length();
            int tagNamelength = tagNameFunc.length();
            if( (strSubStringlength * 4) != tagNamelength ){
                throw new IllegalArgumentException(ThWordCacheHa.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " lengthHex: " + tagNameFunc.length()
                        + " strSubString: " + strSubStringFunc + " lengthStr: " + strSubStringFunc.length()
                        + " lengthHex == lengthStr * 4 ");
            }
            if( tagNamelength < 4 ){
                throw new IllegalArgumentException(ThWordCacheHa.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " length: " + tagNameFunc.length()
                        + " < 4 ");
            }
            valListByTypeWord = getListByType(typeWordFunc);
            String substringTagName = tagNameFunc.substring(0, 3);
            valListByTagNameCode = valListByTypeWord.get(substringTagName);
            if( valListByTagNameCode == null ){
                throw new NullPointerException(ThWordCacheHa.class.getCanonicalName() 
                        + " for word by type " + String.valueOf(typeWordFunc)
                        + " tagName " + tagNameFunc
                        + " subString " + strSubStringFunc
                        + " data in cache is null");
            }
            valListBySubStrLength = valListByTagNameCode.get(strSubStringlength);
            if( valListBySubStrLength == null ){
                throw new NullPointerException(ThWordCacheHa.class.getCanonicalName() 
                        + " for word by type " + String.valueOf(typeWordFunc)
                        + " tagName " + tagNameFunc
                        + " subString " + strSubStringFunc
                        + " data in cache is null");
            }
            //do while and poll elements from list
            valTagNameListData = valListBySubStrLength.remove(tagNameFunc);
            if( valTagNameListData == null ){
                throw new NullPointerException(ThWordCacheHa.class.getCanonicalName() 
                        + " for word by type " + String.valueOf(typeWordFunc)
                        + " tagName " + tagNameFunc
                        + " subString " + strSubStringFunc
                        + " data in cache is null");
            }
            return valTagNameListData;
        } finally {
            valListByTypeWord = null;
            valListByTagNameCode = null;
            valListBySubStrLength = null;
            valTagNameListData = null;
            tagNameFunc = null;
            strSubStringFunc = null;
            typeWordFunc = null;
        }
    }
    /**
     * create default or get from lists
     * @param getListByTypeWord
     * @param tagName
     * @return lvl (3)
     */
    /*protected ConcurrentSkipListMap<String, String> getTagFileNameParams(
            final ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, String>> inputedListByTypeWord,
            final String tagName){
        ConcurrentSkipListMap<String, String> funcListByTagFileName;
        ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, String>> funcListByTypeWord;
        try{
            funcListByTypeWord = (ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, String>>) inputedListByTypeWord;
            funcListByTagFileName = inputedListByTypeWord.get(tagName);
            if( funcListByTagFileName == null ){
                funcListByTagFileName = new ConcurrentSkipListMap<String, String>();
                funcListByTypeWord.put(tagName, funcListByTagFileName);

            }
            return funcListByTagFileName;
        } finally {
            funcListByTagFileName = null;
            funcListByTypeWord = null;
        }
    }*/
    /**
     * return list of not limited files from structure
     * @param typeWordOuter
     * @return 
     *   ConcurrentSkipListMap<String,    - (2a) - tagFileName.substring(0,3)
     *     ConcurrentSkipListMap<Integer, - (2b) - subString.length                            
     *     ConcurrentSkipListMap<String, String> - <hexWord (tagFileName), subString>
     */
    private ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>>> getListByType(final int typeWordOuter){
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>>> forListReturn;
        try{
            forListReturn = this.cachedData.get(typeWordOuter);
            if( forListReturn == null ){
                forListReturn = new ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>>>();
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
     * @param keysPointsFlow ConcurrentSkipListMap<String, String>
     *          <ThWordStatusDataFs.hashCode(), recordUUID>
     *          <ThWordStatusName.hashCode(), recordUUID>
     *          <ThWordStatusActivity.hashCode(), recordUUID>
     *          <ThWordStatusDataCache.hashCode(), recordUUID>
     *          <ThWordStatusWorkers.hashCode(), recordUUID>
     * @throws IllegalArgumentException when inputed data not valid
     */
    protected Boolean setDataIntoCacheFlow(
            final TdataWord dataForSet){
        Integer funcTypeWord;
        String funcSubString;
        String funcHexTagName;
        ConcurrentSkipListMap<UUID, TdataWord> inputedData;
        ConcurrentSkipListMap<UUID, TdataWord> typeWordTagFileNameFlowUuids;
        UUID recordUUID;
        TdataWord funcData;
        Boolean tdataWordValid;
        try {
            funcData = (TdataWord) dataForSet;
            tdataWordValid = ThWordHelper.isTdataWordValid(funcData);
            if( !tdataWordValid ){
                throw new IllegalArgumentException(ThWordCacheHa.class.getCanonicalName() 
                        + " inputed not valid data for set into cache object class " 
                        + TdataWord.class.getCanonicalName() 
                        + " object data " + funcData.toString());
            }
            funcTypeWord = (Integer) funcData.typeWord;
            funcSubString = (String) funcData.strSubString;
            funcHexTagName = (String) funcData.hexSubString;
            recordUUID = (UUID) funcData.recordUUID;
            try{
                typeWordTagFileNameFlowUuids = getDataTypeWordTagNameSubStr(
                        funcTypeWord,
                        funcSubString, 
                        funcHexTagName);
            } catch(IllegalArgumentException exSetInCahe) {
                System.err.println(exSetInCahe.getMessage());
                return Boolean.FALSE;
            }
            inputedData = new ConcurrentSkipListMap<UUID, TdataWord>();
            inputedData.put(recordUUID, funcData);
            
            typeWordTagFileNameFlowUuids.putAll(inputedData);
            return Boolean.TRUE;
        } finally {
            recordUUID = null;
            typeWordTagFileNameFlowUuids = null;
            inputedData = null;
            funcTypeWord = null;
            funcSubString = null;
            funcHexTagName = null;
            funcData = null;
            tdataWordValid = null;
        }
    }
    
    /**
     * @todo recode for check inputed data, call
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @param outerInputedData - readed list data for insert into cacheReaded
     * @param keysPointsFlow ConcurrentSkipListMap<String, String>
     *          <ThWordStatusDataFs.hashCode(), recordUUID>
     *          <ThWordStatusName.hashCode(), recordUUID>
     *          <ThWordStatusActivity.hashCode(), recordUUID>
     *          <ThWordStatusDataCache.hashCode(), recordUUID>
     *          <ThWordStatusWorkers.hashCode(), recordUUID>
     */
    protected Boolean addAllDataIntoCache(
            final ConcurrentSkipListMap<UUID, TdataWord> outerInputedData){
        ConcurrentSkipListMap<UUID, TdataWord> inputedData;
        TdataWord removeElementForAdd;
        try {
            inputedData = (ConcurrentSkipListMap<UUID, TdataWord>) outerInputedData;
            if( inputedData == null ) {
                return Boolean.FALSE;
            }
            if( inputedData.isEmpty() ){
                return Boolean.FALSE;
            }
            for( Map.Entry<UUID, TdataWord> itemsForAdd : inputedData.entrySet() ){
                removeElementForAdd = inputedData.remove(itemsForAdd.getKey());
                if( removeElementForAdd != null ){
                    setDataIntoCacheFlow((TdataWord) itemsForAdd.getValue());
                }
            }
            return Boolean.TRUE;
        } finally {
            inputedData = null;
        }
    }
    /**
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @param keysPointsFlow ConcurrentSkipListMap<String, String>
     *          <ThWordStatusDataFs.hashCode(), recordUUID>
     *          <ThWordStatusName.hashCode(), recordUUID>
     *          <ThWordStatusActivity.hashCode(), recordUUID>
     *          <ThWordStatusDataCache.hashCode(), recordUUID>
     *          <ThWordStatusWorkers.hashCode(), recordUUID>
     */
    protected Boolean isCacheHasData(
            final Integer typeWord, 
            final String tagName, 
            final String strSubString){
        Integer funcTypeWord;
        String funcSubString;
        String funcHexTagName;
        ConcurrentSkipListMap<UUID, TdataWord> typeWordTagFileNameFlowUuids;
        try {
            funcTypeWord = (Integer) typeWord;
            funcSubString = (String) strSubString;
            funcHexTagName = (String) tagName;
            try{
                typeWordTagFileNameFlowUuids = (ConcurrentSkipListMap<UUID, TdataWord>) getDataTypeWordTagNameSubStr(
                        funcTypeWord,
                        funcSubString,
                        funcHexTagName);
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
     * @param keysPointsFlow ConcurrentSkipListMap<String, String>
     *          <ThWordStatusDataFs.hashCode(), recordUUID>
     *          <ThWordStatusName.hashCode(), recordUUID>
     *          <ThWordStatusActivity.hashCode(), recordUUID>
     *          <ThWordStatusDataCache.hashCode(), recordUUID>
     *          <ThWordStatusWorkers.hashCode(), recordUUID>
     */
    protected Integer sizeDataInCache(
            final Integer typeWord, 
            final String tagName, 
            final String strSubString){
        Integer funcTypeWord;
        String funcSubString;
        String funcHexTagName;
        ConcurrentSkipListMap<UUID, TdataWord> typeWordTagFileNameFlowUuids;
        Integer returnedValue;
        try {
            funcTypeWord = (Integer) typeWord;
            funcSubString = (String) strSubString;
            funcHexTagName = (String) tagName;
            returnedValue = 0;
            try{
                typeWordTagFileNameFlowUuids = getDataTypeWordTagNameSubStr(
                        funcTypeWord,
                        funcSubString,
                        funcHexTagName);
            } catch(IllegalArgumentException exSetInCahe) {
                System.err.println(exSetInCahe.getMessage());
                exSetInCahe.printStackTrace();
                return returnedValue;
            }
            returnedValue = (int) typeWordTagFileNameFlowUuids.size();
            return returnedValue;
        } finally {
            typeWordTagFileNameFlowUuids = null;
            funcTypeWord = null;
            funcSubString = null;
            funcHexTagName = null;
            returnedValue = null;
        }
    }
    /*protected void printCacheData(){
        for( Map.Entry<Integer,ConcurrentSkipListMap<String, ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<String, String>>>> cachedTypes : this.cachedData.entrySet()){
            for(Map.Entry<String, ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<String, String>>> hexSubByte : cachedTypes.getValue().entrySet()){
                for(Map.Entry<Integer, ConcurrentSkipListMap<String, String>> itemLength : hexSubByte.getValue().entrySet()){
                    for(Map.Entry<String, String> itemData : itemLength.getValue().entrySet()){
                        System.out.println(" -  -  -  -  -   -      -     -     -       -    -   -  hexName " 
                                + itemData.getKey() 
                                + " subStr " 
                                + itemData.getValue());
                    }
                }
            }
        
        }
    }*/
    /**
     * isCacheEmpty
     */
    /**
     * return CacheDataForFinishedWrite by bus type
     */
}
