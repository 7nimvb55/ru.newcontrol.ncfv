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
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThWordBusReadedFlow {
    /**
     * ConcurrentSkipListMap<Integer, Integer> (<hashFieldCode, Value>)
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
     * ConcurrentSkipListMap<Integer,     - (1) - Strorage hash value 
     * * * * * - (1) In release only for storage of StorageWord index not apply
     * > ConcurrentSkipListMap<Integer,   - (2) - Type of word index hash value
     *   ConcurrentSkipListMap<String,    - (2a) - tagFileName.substring(0,3)
     *     ConcurrentSkipListMap<Integer, - (2b) - subString.length                            
     *     ConcurrentSkipListMap<String, UUID> - (3) - tagFileName subString with hex view
     * list of uuids
     * > >   ConcurrentSkipListMap<tagFileName, 
     *          ConcurrentSkipListMap<recordUUIDNanoTime, UUIDMainFlow>>
     *          UUIDMainFlow - contains UUID for ready jobs from WorkRead flow
     */
    private ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<Integer, 
                        ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, UUID>>>>> uuidReadedFlowMap;

    private ThWordStatusMainFlow mainWordFlow;
    
    public ThWordBusReadedFlow(ThWordStatusMainFlow storageWordStatisticOuter) {
        this.mainWordFlow = storageWordStatisticOuter;
        this.uuidReadedFlowMap = createNewListStoragesMapEmpty();

    }
    /**
     * 
     * @param dataInputed
     * @return 
     * @throws IllegalArgumentException when inputed data not valid
     */
    protected ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, UUID>> getDataReadedFlowUuidsByDataWord(final TdataWord dataInputed){
        ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, UUID>> dataTypeWordTagNameSubStr;
        TdataWord dataFunc;
        String tagNameFunc;
        String strSubStringFunc;
        Integer typeWordFunc;
        Boolean tdataWordValid;
        try {
            dataFunc = (TdataWord) dataInputed;
            
            tdataWordValid = ThWordHelper.isTdataWordValid(dataFunc);
            if( !tdataWordValid ){
                throw new IllegalArgumentException(ThWordBusReadedFlow.class.getCanonicalName() 
                        + " not valid data for get from cache object class " + TdataWord.class.getCanonicalName() 
                        + " object data " + dataFunc.toString());
            }
            tagNameFunc = dataFunc.hexSubString;
            strSubStringFunc = dataFunc.strSubString;
            typeWordFunc = dataFunc.typeWord;
            
            dataTypeWordTagNameSubStr = getTypeWordTagFileNameReadedFlowUuids(typeWordFunc, strSubStringFunc, tagNameFunc);
            if( dataTypeWordTagNameSubStr == null ){
                throw new NullPointerException(ThWordBusReadedFlow.class.getCanonicalName() 
                        + " not have UUIDs in ReadedFlow for key type " + TdataWord.class.getCanonicalName() 
                        + " object data " + dataFunc.toString());
            }
            if( dataTypeWordTagNameSubStr.isEmpty() ){
                throw new NullPointerException(ThWordBusReadedFlow.class.getCanonicalName() 
                        + " not have UUIDs in ReadedFlow for key type " + TdataWord.class.getCanonicalName() 
                        + " object data " + dataFunc.toString());
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
     * > > > > > > > > > this use in router
     * lvl (2a) init params for new itemIndex
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @return lvl (2b)
     * ConcurrentSkipListMap<tagFileName, UUIDMainFlow>
     *          UUIDMainFlow - contains UUID for ready jobs from WorkRead flow
     * @throws IllegalArgumentException
     */
    protected ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, UUID>> getTypeWordTagFileNameReadedFlowUuids(
            final Integer typeWord, 
            final String strSubString,
            final String tagName ){
        
        //(1)
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, UUID>>>> getListByTypeWord;
        //(2a)
        ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, UUID>>> getListByTagNameCode;
        //(2b)
        ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, UUID>> getListBySubStrLength;
        
        Integer typeWordFunc;
        String strSubStringFunc;
        String tagNameFunc;
        
        String substringTagName;
        Integer strSubStringlength;
        Integer tagNamelength;
        try{
            typeWordFunc = (Integer) typeWord;
            strSubStringFunc = (String) strSubString;
            tagNameFunc = (String) tagName;
            strSubStringlength = strSubString.length();
            tagNamelength = tagName.length();
            substringTagName = tagName.substring(0, 3);
            
            if( (strSubStringlength * 4) != tagNamelength ){
                throw new IllegalArgumentException(ThWordBusReadedFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " lengthHex: " + tagNamelength
                        + " strSubString: " + strSubStringFunc + " lengthStr: " + strSubStringlength
                        + " lengthHex == lengthStr * 4 ");
            }
            if( tagNamelength < 4 ){
                throw new IllegalArgumentException(ThWordBusReadedFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " length: " + tagNamelength
                        + " < 4 ");
            }
            
            getListByTypeWord = getListByType(typeWordFunc);
            getListByTagNameCode = getListByTypeWord.get(substringTagName);
            if( getListByTagNameCode == null ){
                getListByTagNameCode = new ConcurrentSkipListMap<Integer, 
                                ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, UUID>>>();
                getListByTypeWord.put(substringTagName, getListByTagNameCode);
                
            }
            getListBySubStrLength = getListByTagNameCode.get(strSubStringlength);
            if( getListBySubStrLength == null ){
                getListBySubStrLength = new ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, UUID>>();
                getListBySubStrLength.put(tagNameFunc, new ConcurrentSkipListMap<Long, UUID>());
                getListByTagNameCode.put(strSubStringlength, getListBySubStrLength);
            }
            return getListBySubStrLength;
        } finally {
            getListByTypeWord = null;
            getListByTagNameCode = null;
            getListBySubStrLength = null;
            substringTagName = null;
            strSubStringlength = null;
            tagNamelength = null;
            typeWordFunc = null;
            strSubStringFunc = null;
            tagNameFunc = null;
        }
    }
    /**
     * create default or get from lists
     * @param getListByTypeWord
     * @param tagName
     * @return lvl (3)
     */
    /*protected ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> getTagFileNameParams(
            final ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>> getListByTypeWord,
            final String tagName){
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> getListByTagFileName;
        try{
            getListByTagFileName = getListByTypeWord.get(tagName);
            if( getListByTagFileName == null ){
                getListByTagFileName = new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>();
                getListByTypeWord.put(tagName, getListByTagFileName);

            }
            return getListByTagFileName;
        } finally {
            getListByTagFileName = null;
        }
    }*/
    /**
     * @todo check for inputed params
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @param UUID mainFlowContentInputed
     * @throws IllegalArgumentException
     */
    protected void addToListOfReadedFlowUuids(
            final Integer typeWord, 
            final String tagName, 
            final String strSubString,
            final UUID mainFlowContentInputed){
        ConcurrentSkipListMap<Long, UUID> mainFlowContentFunc;
        ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, UUID>> typeWordTagFileNameFlowUuids;
        ConcurrentSkipListMap<UUID, 
                    ConcurrentSkipListMap<Integer, UUID>> typeWordTagFileNameFlowUuidsFromMain;
        try {
            if( !this.mainWordFlow.isUuidExistInFlow(typeWord, tagName, strSubString, mainFlowContentInputed) ){
                throw new IllegalArgumentException(ThWordBusReadedFlow.class.getCanonicalName() 
                        + " UUID: "
                        + mainFlowContentInputed.toString() 
                        + " in mainFlow not exist, hexTagName: "
                        + tagName + " lengthHex: " + tagName.length()
                        + " strSubString: " + strSubString + " lengthStr: " + strSubString.length()
                        + " lengthHex == lengthStr * 4 ");
            }
            
            typeWordTagFileNameFlowUuids = getTypeWordTagFileNameReadedFlowUuids(
                    typeWord,
                    tagName,
                    strSubString);
            mainFlowContentFunc = typeWordTagFileNameFlowUuids.get(tagName);
            if( mainFlowContentFunc == null ){
                mainFlowContentFunc = new ConcurrentSkipListMap<Long, UUID>();
            }
            Long nowTime = System.nanoTime();
            mainFlowContentFunc.put(nowTime, mainFlowContentInputed);
            typeWordTagFileNameFlowUuids.put(tagName, mainFlowContentFunc);
        } finally {
            typeWordTagFileNameFlowUuids = null;
        }
    }
    
    
    
    protected ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<Integer, 
                        ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, UUID>>>>> createNewListStoragesMapEmpty(){
        return new ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<Integer, 
                        ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, UUID>>>>>();
    }
    /**
     * return list of not limited files from structure
     * @param typeWordOuter
     * @return 
     */
    protected ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, UUID>>>> getListByType(final int typeWordOuter){
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, UUID>>>> forListReturn;
        try{
            forListReturn = this.uuidReadedFlowMap.get((int) typeWordOuter);
            if( forListReturn == null ){
                forListReturn = new ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<Integer, 
                            ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, UUID>>>>();
                this.uuidReadedFlowMap.put((int) typeWordOuter, forListReturn);
            }
            return forListReturn;
        } finally {
            forListReturn = null;
        }
    }

}
