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
public class ThStorageWordBusReadedFlow {
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
     *     ConcurrentHashMap<String, UUID> - (3) - tagFileName subString with hex view
     * list of uuids
     * > >   ConcurrentHashMap<tagFileName, 
     *          ConcurrentHashMap<recordUUIDNanoTime, UUIDMainFlow>>
     *          UUIDMainFlow - contains UUID for ready jobs from WorkRead flow
     */
    private ConcurrentHashMap<Integer, 
                ConcurrentHashMap<String, 
                    ConcurrentHashMap<Integer, 
                        ConcurrentHashMap<String, ConcurrentHashMap<Long, UUID>>>>> uuidReadedFlowMap;

    private ThStorageWordStatusMainFlow mainStorageWordFlow;
    
    public ThStorageWordBusReadedFlow(ThStorageWordStatusMainFlow storageWordStatisticOuter) {
        this.mainStorageWordFlow = storageWordStatisticOuter;
        this.uuidReadedFlowMap = createNewListStoragesMapEmpty();

    }

    /**
     * > > > > > > > > > this use in router
     * lvl (2a) init params for new itemIndex
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @return lvl (2b)
     * ConcurrentHashMap<tagFileName, UUIDMainFlow>
     *          UUIDMainFlow - contains UUID for ready jobs from WorkRead flow
     * @throws IllegalArgumentException
     */
    protected ConcurrentHashMap<String, ConcurrentHashMap<Long, UUID>> getTypeWordTagFileNameReadedFlowUuids(
            final Integer typeWord, 
            final String tagName, 
            final String strSubString){
        
        //(1)
        ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, ConcurrentHashMap<Long, UUID>>>> getListByTypeWord;
        //(2a)
        ConcurrentHashMap<Integer, 
                ConcurrentHashMap<String, ConcurrentHashMap<Long, UUID>>> getListByTagNameCode;
        //(2b)
        ConcurrentHashMap<String, ConcurrentHashMap<Long, UUID>> getListBySubStrLength;
        
        try{
            int strSubStringlength = strSubString.length();
            int tagNamelength = tagName.length();
            if( (strSubStringlength * 4) != tagNamelength ){
                throw new IllegalArgumentException(ThStorageWordBusReadedFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagName + " lengthHex: " + tagName.length()
                        + " strSubString: " + strSubString + " lengthStr: " + strSubString.length()
                        + " lengthHex == lengthStr * 4 ");
            }
            if( tagNamelength < 4 ){
                throw new IllegalArgumentException(ThStorageWordBusReadedFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagName + " length: " + tagName.length()
                        + " < 4 ");
            }
            
            getListByTypeWord = getListByType(typeWord);
            String substringTagName = tagName.substring(0, 3);
            getListByTagNameCode = getListByTypeWord.get(substringTagName);
            if( getListByTagNameCode == null ){
                getListByTagNameCode = new ConcurrentHashMap<Integer, 
                                ConcurrentHashMap<String, ConcurrentHashMap<Long, UUID>>>();
                getListByTypeWord.put(substringTagName, getListByTagNameCode);
                
            }
            getListBySubStrLength = getListByTagNameCode.get(strSubStringlength);
            if( getListBySubStrLength == null ){
                getListBySubStrLength = new ConcurrentHashMap<String, ConcurrentHashMap<Long, UUID>>();
                getListBySubStrLength.put(tagName, new ConcurrentHashMap<Long, UUID>());
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
     * create default or get from lists
     * @param getListByTypeWord
     * @param tagName
     * @return lvl (3)
     */
    /*protected ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> getTagFileNameParams(
            final ConcurrentHashMap<String, ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>> getListByTypeWord,
            final String tagName){
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> getListByTagFileName;
        try{
            getListByTagFileName = getListByTypeWord.get(tagName);
            if( getListByTagFileName == null ){
                getListByTagFileName = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>();
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
        ConcurrentHashMap<Long, UUID> mainFlowContentFunc;
        ConcurrentHashMap<String, ConcurrentHashMap<Long, UUID>> typeWordTagFileNameFlowUuids;
        try {

            ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> typeWordTagFileNameFlowUuids1 = this.mainStorageWordFlow.getTypeWordTagFileNameFlowUuids(typeWord, tagName, strSubString);
            if( !typeWordTagFileNameFlowUuids1.containsKey(mainFlowContentInputed) ){
                throw new IllegalArgumentException(ThStorageWordBusReadedFlow.class.getCanonicalName() 
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
                mainFlowContentFunc = new ConcurrentHashMap<Long, UUID>();
            }
            Long nowTime = System.nanoTime();
            mainFlowContentFunc.put(nowTime, mainFlowContentInputed);
            typeWordTagFileNameFlowUuids.put(tagName, mainFlowContentFunc);
        } finally {
            typeWordTagFileNameFlowUuids = null;
        }
    }
    
    
    
    protected ConcurrentHashMap<Integer, 
                ConcurrentHashMap<String, 
                    ConcurrentHashMap<Integer, 
                        ConcurrentHashMap<String, ConcurrentHashMap<Long, UUID>>>>> createNewListStoragesMapEmpty(){
        return new ConcurrentHashMap<Integer, 
                ConcurrentHashMap<String, 
                    ConcurrentHashMap<Integer, 
                        ConcurrentHashMap<String, ConcurrentHashMap<Long, UUID>>>>>();
    }
    /**
     * return list of not limited files from structure
     * @param typeWordOuter
     * @return 
     */
    protected ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, ConcurrentHashMap<Long, UUID>>>> getListByType(final int typeWordOuter){
        ConcurrentHashMap<String, 
                ConcurrentHashMap<Integer, 
                    ConcurrentHashMap<String, ConcurrentHashMap<Long, UUID>>>> forListReturn;
        try{
            forListReturn = this.uuidReadedFlowMap.get((int) typeWordOuter);
            if( forListReturn == null ){
                forListReturn = new ConcurrentHashMap<String, 
                        ConcurrentHashMap<Integer, 
                            ConcurrentHashMap<String, ConcurrentHashMap<Long, UUID>>>>();
                this.uuidReadedFlowMap.put((int) typeWordOuter, forListReturn);
            }
            return forListReturn;
        } finally {
            forListReturn = null;
        }
    }

}
