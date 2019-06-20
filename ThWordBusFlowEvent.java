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
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedTransferQueue;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThWordBusFlowEvent {
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
                        ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>>>>> uuidReadedFlowMap;

    private ThWordStatusMainFlow mainWordFlow;
    
    public ThWordBusFlowEvent(ThWordStatusMainFlow storageWordStatisticOuter) {
        this.mainWordFlow = (ThWordStatusMainFlow) storageWordStatisticOuter;
        this.uuidReadedFlowMap = createNewListStoragesMapEmpty();

    }
    
    /**
     * @param dataInputed
     * @return 
     * @throws IllegalArgumentException when inputed data not valid
     */
    protected LinkedTransferQueue<UUID> pollDataReadedFlowUuidsByDataWord(final TdataWord dataInputed){
        ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>> dataTypeWordTagNameSubStr;
        TdataWord dataFunc;
        String tagNameFunc;
        String strSubStringFunc;
        Integer typeWordFunc;
        Boolean tdataWordValid;
        LinkedTransferQueue<UUID> removedForReturn;
        UUID poll;
        try {
            dataFunc = (TdataWord) dataInputed;
            
            tdataWordValid = ThWordHelper.isTdataWordValid(dataFunc);
            if( !tdataWordValid ){
                throw new IllegalArgumentException(ThWordBusFlowEvent.class.getCanonicalName() 
                        + " not valid data for get from cache object class " + TdataWord.class.getCanonicalName() 
                        + " object data " + dataFunc.toString());
            }
            tagNameFunc = dataFunc.hexSubString;
            strSubStringFunc = dataFunc.strSubString;
            typeWordFunc = dataFunc.typeWord;
            
            dataTypeWordTagNameSubStr = getTypeWordTagFileNameReadedFlowUuids(typeWordFunc, strSubStringFunc, tagNameFunc);
            if( dataTypeWordTagNameSubStr == null ){
                throw new NullPointerException(ThWordBusFlowEvent.class.getCanonicalName() 
                        + " not have UUIDs in ReadedFlow for key type " + TdataWord.class.getCanonicalName() 
                        + " object data " + dataFunc.toString());
            }
            if( dataTypeWordTagNameSubStr.isEmpty() ){
                throw new NullPointerException(ThWordBusFlowEvent.class.getCanonicalName() 
                        + " not have UUIDs in ReadedFlow for key type " + TdataWord.class.getCanonicalName() 
                        + " object data " + dataFunc.toString());
            }
            LinkedTransferQueue<UUID> getCurrentList = dataTypeWordTagNameSubStr.get(tagNameFunc);
            removedForReturn = new LinkedTransferQueue<UUID>();
            do {
                poll = getCurrentList.poll();
                if( poll != null ){
                    removedForReturn.add(poll);
                }
            } while( !getCurrentList.isEmpty() );
            return removedForReturn;
        }
        finally {
            dataFunc = null;
            tagNameFunc = null;
            strSubStringFunc = null;
            typeWordFunc = null;
            dataTypeWordTagNameSubStr = null;
            tdataWordValid = null;
            removedForReturn = null;
            poll = null;
        }
    }
    /**
     * @param dataInputed
     * @return 
     * @throws IllegalArgumentException when inputed data not valid
     */
    protected LinkedTransferQueue<UUID> pollDataReadedFlowUuids(final Integer typeWord, 
            final String strSubString,
            final String tagName){
        ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>> dataTypeWordTagNameSubStr;

        LinkedTransferQueue<UUID> removedForReturn;
        Integer typeWordFunc;
        String strSubStringFunc;
        String tagNameFunc;
        
        Integer strSubStringlength;
        Integer tagNamelength;
        UUID poll;
        try{
            typeWordFunc = (Integer) typeWord;
            strSubStringFunc = (String) strSubString;
            tagNameFunc = (String) tagName;
            strSubStringlength = strSubString.length();
            tagNamelength = tagName.length();
            
            if( (strSubStringlength * 4) != tagNamelength ){
                throw new IllegalArgumentException(ThWordBusFlowEvent.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " lengthHex: " + tagNamelength
                        + " strSubString: " + strSubStringFunc + " lengthStr: " + strSubStringlength
                        + " lengthHex == lengthStr * 4 ");
            }
            if( tagNamelength < 4 ){
                throw new IllegalArgumentException(ThWordBusFlowEvent.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " length: " + tagNamelength
                        + " < 4 ");
            }
            
            dataTypeWordTagNameSubStr = getTypeWordTagFileNameReadedFlowUuids(typeWordFunc, strSubStringFunc, tagNameFunc);
            if( dataTypeWordTagNameSubStr == null ){
                throw new NullPointerException(ThWordBusFlowEvent.class.getCanonicalName() 
                        + " not have UUIDs in for key type, hexTagName: "
                        + tagNameFunc + " lengthHex: " + tagNamelength
                        + " strSubString: " + strSubStringFunc);
            }
            if( dataTypeWordTagNameSubStr.isEmpty() ){
                throw new NullPointerException(ThWordBusFlowEvent.class.getCanonicalName() 
                        + " not have UUIDs in for key type, hexTagName: "
                        + tagNameFunc + " lengthHex: " + tagNamelength
                        + " strSubString: " + strSubStringFunc);
            }
            
            LinkedTransferQueue<UUID> getCurrentList = dataTypeWordTagNameSubStr.get(tagNameFunc);
            removedForReturn = new LinkedTransferQueue<UUID>();
            do {
                poll = getCurrentList.poll();
                if( poll != null ){
                    removedForReturn.add(poll);
                }
            } while( !getCurrentList.isEmpty() );
            return removedForReturn;
        }
        finally {

            tagNameFunc = null;
            strSubStringFunc = null;
            typeWordFunc = null;
            dataTypeWordTagNameSubStr = null;
            removedForReturn = null;
            poll = null;
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
    private ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>> getTypeWordTagFileNameReadedFlowUuids(
            final Integer typeWord, 
            final String strSubString,
            final String tagName){
        
        //(1)
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>>>> getListByTypeWord;
        //(2a)
        ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>>> getListByTagNameCode;
        //(2b)
        ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>> getListBySubStrLength;
        
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
                throw new IllegalArgumentException(ThWordBusFlowEvent.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " lengthHex: " + tagNamelength
                        + " strSubString: " + strSubStringFunc + " lengthStr: " + strSubStringlength
                        + " lengthHex == lengthStr * 4 ");
            }
            if( tagNamelength < 4 ){
                throw new IllegalArgumentException(ThWordBusFlowEvent.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " length: " + tagNamelength
                        + " < 4 ");
            }
            
            getListByTypeWord = getListByType(typeWordFunc);
            getListByTagNameCode = getListByTypeWord.get(substringTagName);
            if( getListByTagNameCode == null ){
                getListByTagNameCode = new ConcurrentSkipListMap<Integer, 
                                ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>>>();
                getListByTypeWord.put(substringTagName, getListByTagNameCode);
                
            }
            getListBySubStrLength = getListByTagNameCode.get(strSubStringlength);
            if( getListBySubStrLength == null ){
                getListBySubStrLength = new ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>>();
                getListBySubStrLength.put(tagNameFunc, new LinkedTransferQueue<UUID>());
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
    private LinkedTransferQueue<UUID> getFlowUuidsByTypeWordHexTagName(
            final Integer typeWordInputed,
            final UUID mainFlowUuid, 
            final String tagNameInputed){
        
        LinkedTransferQueue<UUID> flowUuidsFunc;
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                LinkedTransferQueue<UUID>>>> listTypeWordData;
        ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                LinkedTransferQueue<UUID>>> listTagNameLetter;
        ConcurrentSkipListMap<String, 
                LinkedTransferQueue<UUID>> listTagName;
        
        Integer typeWordFunc;
        UUID mainFlowUuidFunc;
        String tagNameFunc;
        String tagNameLetter;
        Integer tagNamelength;
        Integer calculatedSubString;
        try {
            typeWordFunc = (Integer) typeWordInputed;
            mainFlowUuidFunc = (UUID) mainFlowUuid;
            tagNameFunc = (String) tagNameInputed;
            tagNamelength = (Integer) tagNameFunc.length();
            if( mainFlowUuidFunc == null ){
                throw new NullPointerException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " Main Flow UUID is null");
            }
            if( tagNamelength < 4 ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " length: " + tagNameFunc.length()
                        + " < 4 ");
            }
            calculatedSubString = tagNamelength / 4;
            tagNameLetter = tagNameFunc.substring(0, 3);
            
            listTypeWordData = this.uuidReadedFlowMap.get(typeWordFunc);
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
            flowUuidsFunc = listTagName.get(tagNameFunc);
            if(flowUuidsFunc == null ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal key value for tagName: "
                        + tagNameFunc);
            }
            return flowUuidsFunc;
        } finally {
            flowUuidsFunc = null;
            listTypeWordData = null;
            listTagNameLetter = null;
            listTagName = null;

            typeWordFunc = null;
            mainFlowUuidFunc = null;
            tagNameFunc = null;
            tagNamelength = null;
            calculatedSubString = null;
            tagNameLetter = null;
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
    protected void addToListOfFlowEventUuidByTypeWordHexTagName(
            final Integer typeWord, 
            final UUID mainFlowContentInputed,
            final String tagName
            ){
        LinkedTransferQueue<UUID> mainFlowContentFunc;
        Integer typeWordFunc;
        String tagNameFunc;
        UUID uuidFlowContentFunc;
        try {
            typeWordFunc = (Integer) typeWord;
            tagNameFunc = (String) tagName;
            uuidFlowContentFunc = (UUID) mainFlowContentInputed;
            mainFlowContentFunc = getFlowUuidsByTypeWordHexTagName(typeWordFunc, uuidFlowContentFunc, tagNameFunc);
            
            if( isExistInEventQueue(mainFlowContentFunc, uuidFlowContentFunc) ){
                throw new IllegalArgumentException(ThWordBusFlowEvent.class.getCanonicalName() 
                        + " UUID: "
                        + uuidFlowContentFunc.toString() 
                        + " exist in event flow, hexTagName: "
                        + tagNameFunc + " lengthHex: " + tagNameFunc.length());
            }
            
            this.mainWordFlow.valideInFlowAllPointsByTypeWordHexTagName(typeWordFunc, uuidFlowContentFunc, tagNameFunc);
            mainFlowContentFunc.add(uuidFlowContentFunc);
        } finally {
            typeWordFunc = null;
            tagNameFunc = null;
            uuidFlowContentFunc = null;
            mainFlowContentFunc = null;
        }
    }
    private Boolean isExistInEventQueue(LinkedTransferQueue<UUID> checkedQueue, final UUID checkedUuid){
        Iterator<UUID> iterator;
        UUID next;
        Boolean equals;
        try{
            for( iterator = checkedQueue.iterator(); iterator.hasNext(); ){
                next = iterator.next();
                equals = checkedUuid.equals(next);
                if( equals ){
                    return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
        } finally {
            iterator = null;
            next = null;
            equals = null;
        }
    }
    /**
     * @todo check for inputed params
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @param UUID mainFlowContentInputed
     * @throws IllegalArgumentException
     */
    protected void addToListOfFlowEventUuids(
            final Integer typeWord, 
            final String tagName, 
            final String strSubString,
            final UUID mainFlowContentInputed){
        LinkedTransferQueue<UUID> mainFlowContentFunc;
        ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>> typeWordTagFileNameFlowUuids;
        
        Integer typeWordFunc;
        String tagNameFunc;
        String strSubStringFunc;
        UUID uuidFlowContentFunc;
        try {
            typeWordFunc = (Integer) typeWord;
            tagNameFunc = (String) tagName;
            strSubStringFunc = (String) strSubString;
            uuidFlowContentFunc = (UUID) mainFlowContentInputed;
            if( !this.mainWordFlow.isUuidExistInFlow(typeWordFunc, tagNameFunc, strSubStringFunc, uuidFlowContentFunc) ){
                throw new IllegalArgumentException(ThWordBusFlowEvent.class.getCanonicalName() 
                        + " UUID: "
                        + uuidFlowContentFunc.toString() 
                        + " in mainFlow not exist, hexTagName: "
                        + tagNameFunc + " lengthHex: " + tagNameFunc.length()
                        + " strSubString: " + strSubStringFunc + " lengthStr: " + strSubStringFunc.length()
                        + " lengthHex == lengthStr * 4 ");
            }
            this.mainWordFlow.validateInFlowAllPoints(typeWordFunc, strSubStringFunc, tagNameFunc, uuidFlowContentFunc);
            typeWordTagFileNameFlowUuids = getTypeWordTagFileNameReadedFlowUuids(
                    typeWordFunc,
                    tagNameFunc,
                    strSubStringFunc);
            mainFlowContentFunc = typeWordTagFileNameFlowUuids.get(tagNameFunc);
            if( mainFlowContentFunc == null ){
                mainFlowContentFunc = new LinkedTransferQueue<UUID>();
            }
            mainFlowContentFunc.put(uuidFlowContentFunc);
            typeWordTagFileNameFlowUuids.put(tagNameFunc, mainFlowContentFunc);
        } finally {
            typeWordTagFileNameFlowUuids = null;
            typeWordFunc = null;
            tagNameFunc = null;
            strSubStringFunc = null;
            uuidFlowContentFunc = null;
        }
    }
    
    
    
    private ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<Integer, 
                        ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>>>>> createNewListStoragesMapEmpty(){
        return new ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<Integer, 
                        ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>>>>>();
    }
    /**
     * 
     * @param typeWordOuter
     * @return 
     */
    private ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>>>> getListByType(final int typeWordOuter){
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>>>> forListReturn;
        Integer typeWordFunc;
        try{
            typeWordFunc = (Integer) typeWordOuter;
            forListReturn = this.uuidReadedFlowMap.get(typeWordFunc);
            if( forListReturn == null ){
                forListReturn = new ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<Integer, 
                            ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>>>>();
                this.uuidReadedFlowMap.put(typeWordFunc, forListReturn);
            }
            return forListReturn;
        } finally {
            forListReturn = null;
            typeWordFunc = null;
        }
    }
    protected ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                LinkedTransferQueue<UUID>>>>> pollAllBusData(){
        ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                LinkedTransferQueue<UUID>>>>> forReturnData;
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                LinkedTransferQueue<UUID>>>> forReturnHexTagNameLetter;
        ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                LinkedTransferQueue<UUID>>> forReturnSubStrLength;
        ConcurrentSkipListMap<String, 
                LinkedTransferQueue<UUID>> forReturnHexTagName;
        Integer keyTypeWord;
        ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<Integer, 
                        ConcurrentSkipListMap<String, 
                        LinkedTransferQueue<UUID>>>> valueHexTagNameList;
        String keyHexTagNameLetter;
        ConcurrentSkipListMap<Integer, 
                            ConcurrentSkipListMap<String, 
                            LinkedTransferQueue<UUID>>> valueSubStrLength;
        Integer keySubStrLength;
        ConcurrentSkipListMap<String, 
                                LinkedTransferQueue<UUID>> valueHexTagName;
        String keyHexTagName;
        LinkedTransferQueue<UUID> valueListUuids;
        UUID poll;
        LinkedTransferQueue<UUID> removedForReturn;
        try{
            forReturnData = createNewListStoragesMapEmpty();

            forReturnHexTagNameLetter = new ConcurrentSkipListMap<String, 
            ConcurrentSkipListMap<Integer, 
            ConcurrentSkipListMap<String, 
            LinkedTransferQueue<UUID>>>>();

            forReturnSubStrLength = new ConcurrentSkipListMap<Integer, 
            ConcurrentSkipListMap<String, 
            LinkedTransferQueue<UUID>>>();

            forReturnHexTagName = new ConcurrentSkipListMap<String, 
            LinkedTransferQueue<UUID>>();
            for( Map.Entry<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    LinkedTransferQueue<UUID>>>>> itemOfListTypeWord : this.uuidReadedFlowMap.entrySet() ){
                keyTypeWord = itemOfListTypeWord.getKey();
                valueHexTagNameList = itemOfListTypeWord.getValue();
                for( Map.Entry<String, 
                    ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    LinkedTransferQueue<UUID>>>> itemOfListHexTagNameLetter : valueHexTagNameList.entrySet() ){
                    keyHexTagNameLetter = itemOfListHexTagNameLetter.getKey();
                    valueSubStrLength = itemOfListHexTagNameLetter.getValue();
                    for(Map.Entry<Integer, 
                            ConcurrentSkipListMap<String, 
                            LinkedTransferQueue<UUID>>> itemOfListSubStrLength  : valueSubStrLength.entrySet() ){
                        keySubStrLength = itemOfListSubStrLength.getKey();
                        valueHexTagName = itemOfListSubStrLength.getValue();
                        for( Map.Entry<String, LinkedTransferQueue<UUID>> itemOfListHexTagName : valueHexTagName.entrySet() ){
                            keyHexTagName = itemOfListHexTagName.getKey();
                            valueListUuids = itemOfListHexTagName.getValue();
                            if( !valueListUuids.isEmpty() ){
                                removedForReturn = new LinkedTransferQueue<UUID>();
                                do {
                                    poll = valueListUuids.poll();
                                    if( poll != null ){
                                        removedForReturn.add(poll);
                                    }
                                } while( !valueListUuids.isEmpty() );
                                
                                forReturnHexTagName.put(keyHexTagName, removedForReturn);
                                forReturnSubStrLength.put(keySubStrLength, forReturnHexTagName);
                                forReturnHexTagNameLetter.put(keyHexTagNameLetter, forReturnSubStrLength);
                                forReturnData.put(keyTypeWord, forReturnHexTagNameLetter);
                            }
                        }
                    }
                }
            }
            return forReturnData;
        } finally {
            forReturnData = null;
            forReturnHexTagNameLetter = null;
            forReturnSubStrLength = null;
            forReturnHexTagName = null;
            keyTypeWord = null;
            valueHexTagNameList = null;
            keyHexTagNameLetter = null;
            valueSubStrLength = null;
            keySubStrLength = null;
            valueHexTagName = null;
            keyHexTagName = null;
            valueListUuids = null;
            poll = null;
            removedForReturn = null;
        }
    
    }
    protected void deleteBusPacketData(ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                LinkedTransferQueue<UUID>>>>> dataForDelete){
        Integer keyTypeWord;
        ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<Integer, 
                        ConcurrentSkipListMap<String, 
                        LinkedTransferQueue<UUID>>>> valueHexTagNameList;
        String keyHexTagNameLetter;
        ConcurrentSkipListMap<Integer, 
                            ConcurrentSkipListMap<String, 
                            LinkedTransferQueue<UUID>>> valueSubStrLength;
        Integer keySubStrLength;
        ConcurrentSkipListMap<String, 
                                LinkedTransferQueue<UUID>> valueHexTagName;
        String keyHexTagName;
        LinkedTransferQueue<UUID> valueListUuids;
        UUID poll;
        LinkedTransferQueue<UUID> removeValueListUuids;
        ConcurrentSkipListMap<String, 
                                LinkedTransferQueue<UUID>> removeValueHexTagName;
        ConcurrentSkipListMap<Integer, 
                            ConcurrentSkipListMap<String, 
                            LinkedTransferQueue<UUID>>> removeValueSubStrLength;
        ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<Integer, 
                        ConcurrentSkipListMap<String, 
                        LinkedTransferQueue<UUID>>>> removeValueHexTagNameList;
        try{
            for( Map.Entry<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    LinkedTransferQueue<UUID>>>>> itemOfListTypeWord : dataForDelete.entrySet() ){
                keyTypeWord = itemOfListTypeWord.getKey();
                valueHexTagNameList = itemOfListTypeWord.getValue();
                for( Map.Entry<String, 
                    ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    LinkedTransferQueue<UUID>>>> itemOfListHexTagNameLetter : valueHexTagNameList.entrySet() ){
                    keyHexTagNameLetter = itemOfListHexTagNameLetter.getKey();
                    valueSubStrLength = itemOfListHexTagNameLetter.getValue();
                    for(Map.Entry<Integer, 
                            ConcurrentSkipListMap<String, 
                            LinkedTransferQueue<UUID>>> itemOfListSubStrLength  : valueSubStrLength.entrySet() ){
                        keySubStrLength = itemOfListSubStrLength.getKey();
                        valueHexTagName = itemOfListSubStrLength.getValue();
                        for( Map.Entry<String, LinkedTransferQueue<UUID>> itemOfListHexTagName : valueHexTagName.entrySet() ){
                            keyHexTagName = itemOfListHexTagName.getKey();
                            valueListUuids = itemOfListHexTagName.getValue();
                            if( !valueListUuids.isEmpty() ){

                                do {
                                    poll = valueListUuids.poll();
                                    poll = null;
                                } while( !valueListUuids.isEmpty() );

                            }
                            removeValueListUuids = valueHexTagName.remove(keyHexTagName);
                            removeValueListUuids = null;
                            valueListUuids = null;
                            keyHexTagName = null;
                        }
                        removeValueHexTagName = valueSubStrLength.remove(keySubStrLength);
                        removeValueHexTagName = null;
                        valueHexTagName = null;
                    }
                    removeValueSubStrLength = valueHexTagNameList.remove(keyHexTagNameLetter);
                    removeValueSubStrLength = null;
                    keyHexTagNameLetter = null;
                    valueSubStrLength = null;
                }
                removeValueHexTagNameList = dataForDelete.remove(keyTypeWord);
                removeValueHexTagNameList = null;
                keyTypeWord = null;
                valueHexTagNameList = null;
            }
        } finally {
            keyTypeWord = null;
            valueHexTagNameList = null;
            keyHexTagNameLetter = null;
            valueSubStrLength = null;
            keySubStrLength = null;
            valueHexTagName = null;
            keyHexTagName = null;
            valueListUuids = null;
            poll = null;
            removeValueListUuids = null;
            removeValueHexTagName = null;
            removeValueSubStrLength = null;
            removeValueHexTagNameList = null;
        }
    
    }

}
