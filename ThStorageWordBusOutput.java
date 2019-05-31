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

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThStorageWordBusOutput {
    /**
     * ConcurrentHashMap<Integer, Long>
     * <typeWordBus, System.nanoTime>
     * for automatedDeleteNotUsedBus;
     */
    private ConcurrentHashMap<Integer, Long> lastLastAccessUsedBusNanoTime;
    /**
     * ConcurrentHashMap<Integer, LinkedTransferQueue<TdataStorageWord>>
     * <typeWordBus, <TdataStorageWord>>
     * ThWordLogicFilter.getWordCode(inputedPath.codePointAt(int idexCharCodePoint))
     */
    private ConcurrentHashMap<Integer, LinkedTransferQueue<TdataWord>> poolBusWordData;
    
    ThStorageWordBusOutput(){
        this.poolBusWordData = new ConcurrentHashMap<Integer, LinkedTransferQueue<TdataWord>>();
        this.lastLastAccessUsedBusNanoTime = new ConcurrentHashMap<Integer, Long>();
    }
    /**
     * get or create new bus for type and set last access time for use
     * @param typeWordByDetectedCodePoint
     * @return 
     */
    protected LinkedTransferQueue<TdataWord> getBusForTypeWord(final int typeWordByDetectedCodePoint){
        Integer inputedVal;
        LinkedTransferQueue<TdataWord> getBusFormPool;
        try{
            inputedVal = (int) typeWordByDetectedCodePoint;
            getBusFormPool = this.poolBusWordData.get(inputedVal);
            if( getBusFormPool == null ){
                getBusFormPool = new LinkedTransferQueue<TdataWord>();
                this.poolBusWordData.put(inputedVal, getBusFormPool);
            }
            setLastAccessForUseTime(inputedVal);
            return getBusFormPool;
        } finally {
            inputedVal = null;
            getBusFormPool = null;
        }
    }
    protected Set<Entry<Integer, LinkedTransferQueue<TdataWord>>> getExistBusEntrySetForTypeWord(){
        Set<Entry<Integer, LinkedTransferQueue<TdataWord>>> entrySet;
        try{
            entrySet = this.poolBusWordData.entrySet();
        return entrySet;
        } finally {
            entrySet = null;
        }
    }
    /**
     * 
     * @param typeWordByDetectedCodePoint 
     */
    private void setLastAccessForUseTime(final int typeWordByDetectedCodePoint){
        Integer inputedTypeVal;
        Long outerTimesPool;
        try{
            inputedTypeVal = (int) typeWordByDetectedCodePoint;
            outerTimesPool = this.lastLastAccessUsedBusNanoTime.get(inputedTypeVal);
            if( outerTimesPool == null ){
                outerTimesPool = Long.valueOf(System.nanoTime());
            }
            this.lastLastAccessUsedBusNanoTime.put(inputedTypeVal, outerTimesPool);
        } finally {
            inputedTypeVal = null;
            outerTimesPool = null;
        }
    }
    /**
     * See in list of bus last access time for bus used and delete not used
     * above time limit
     * time limit need choice recomendation:
     * 1. - algoritm get bus for transfer data
     * 2. - and data transfer by bus
     * 3. - and last access time not updated
     * 4. - if you use this method shrink, bus is destroy...
     * see bus not empty before shrink
     * use periodical get for bus method
     */
    protected void shrinkNotUsedAboveLimitBus(){
        Long nowNanoTime;
        ConcurrentHashMap.KeySetView<Integer, Long> keySet;
        Long getAccTime;
        try{
            //AppConstants.NOT_USED_ABOVE_TIME_LIMIT;
            keySet = this.lastLastAccessUsedBusNanoTime.keySet();
            for(Integer itemKey : keySet){
                getAccTime = this.lastLastAccessUsedBusNanoTime.get(itemKey);
                if( isBusNotEmpty(itemKey) ){
                    setLastAccessForUseTime(itemKey);
                    continue;
                }
                nowNanoTime = System.nanoTime();
                if( nowNanoTime > getAccTime ){
                    if( (nowNanoTime - getAccTime) > AppConstants.NOT_USED_ABOVE_TIME_LIMIT){
                        this.lastLastAccessUsedBusNanoTime.remove(itemKey);
                    }
                }
            }
        } finally {
            nowNanoTime = null;
            keySet = null;
            getAccTime = null;
        }
    }
    /**
     * not empty bus
     * @param typeWordByDetectedCodePoint
     * @return 
     */
    protected Boolean isBusNotEmpty(final int typeWordByDetectedCodePoint){
        Integer inputedTypBusValue;
        LinkedTransferQueue<TdataWord> testedBusForTypeWord;
        try{
            inputedTypBusValue = typeWordByDetectedCodePoint;
            
            testedBusForTypeWord = this.poolBusWordData.get(inputedTypBusValue);
            if( testedBusForTypeWord != null ){
                if( !testedBusForTypeWord.isEmpty() ){
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        } finally {
            inputedTypBusValue = null;
            testedBusForTypeWord = null;
        }
    }
    /**
     * not exist bus
     * @param typeWordByDetectedCodePoint
     * @return 
     */
    protected Boolean isBusNotExist(final int typeWordByDetectedCodePoint){
        Integer inputedTypBusValue;
        LinkedTransferQueue<TdataWord> testedBusForTypeWord;
        try{
            inputedTypBusValue = typeWordByDetectedCodePoint;
            
            testedBusForTypeWord = this.poolBusWordData.get(inputedTypBusValue);
            if( testedBusForTypeWord == null ){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } finally {
            inputedTypBusValue = null;
            testedBusForTypeWord = null;
        }
    }
}
