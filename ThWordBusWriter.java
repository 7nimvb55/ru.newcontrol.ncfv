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

import java.util.NavigableSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Transfered data structure:
 * ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<String, String>>
 * <mainFlowContentFunc, <String.hashCode(), String.Value>>
 * @author wladimirowichbiaran
 */
public class ThWordBusWriter {
    /**
     * ConcurrentSkipListMap<Integer, Long>
     * <typeWordBus, System.nanoTime>
     * for automatedDeleteNotUsedBus;
     */
    private ConcurrentSkipListMap<Integer, Long> lastLastAccessUsedBusNanoTime;
    /**
     * ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<String, String>>
     * <mainFlowContentFunc, <String.hashCode(), String.Value>>
     */
    private ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<String, String>>> poolBusWordData;
    
    ThWordBusWriter(){
        this.poolBusWordData = new ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<String, String>>>();
        this.lastLastAccessUsedBusNanoTime = new ConcurrentSkipListMap<Integer, Long>();
    }
    /**
     * get or create new bus for type and set last access time for use
     * @param typeWordByDetectedCodePoint
     * @return 
     */
    protected ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<String, String>> getBusForTypeWord(final int typeWordByDetectedCodePoint){
        int inputedVal;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<String, String>> getBusFormPool;
        try{
            inputedVal = (int) typeWordByDetectedCodePoint;
            getBusFormPool = this.poolBusWordData.get(inputedVal);
            if( getBusFormPool == null ){
                getBusFormPool = new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<String, String>>();
                this.poolBusWordData.put(inputedVal, getBusFormPool);
            }
            setLastAccessForUseTime(inputedVal);
            return getBusFormPool;
        } finally {
            getBusFormPool = null;
        }
    }
    /**
     * 
     * @param typeWordByDetectedCodePoint 
     */
    private void setLastAccessForUseTime(final int typeWordByDetectedCodePoint){
        int inputedTypeVal;
        Long outerTimesPool;
        try{
            inputedTypeVal = (int) typeWordByDetectedCodePoint;
            outerTimesPool = this.lastLastAccessUsedBusNanoTime.get(inputedTypeVal);
            if( outerTimesPool == null ){
                outerTimesPool = Long.valueOf(System.nanoTime());
            }
            this.lastLastAccessUsedBusNanoTime.put(inputedTypeVal, outerTimesPool);
        } finally {
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
        NavigableSet<Integer> keySet;
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
        int inputedTypBusValue;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<String, String>> testedBusForTypeWord;
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
            testedBusForTypeWord = null;
        }
    }
    /**
     * not exist bus
     * @param typeWordByDetectedCodePoint
     * @return 
     */
    protected Boolean isBusNotExist(final int typeWordByDetectedCodePoint){
        int inputedTypBusValue;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<String, String>> testedBusForTypeWord;
        try{
            inputedTypBusValue = typeWordByDetectedCodePoint;
            
            testedBusForTypeWord = this.poolBusWordData.get(inputedTypBusValue);
            if( testedBusForTypeWord == null ){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } finally {
            testedBusForTypeWord = null;
        }
    }
    protected ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<String, String>>> getMaxUsedBusesSet(){
        Long nowNanoTime;
        NavigableSet<Integer> keySet;
        ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<String, String>>> keySetReturnedNotEmpty;
        Long usedSize;
        try{
            keySetReturnedNotEmpty = new ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<String, String>>>();
            keySet = this.poolBusWordData.keySet();
            for(Integer itemKey : keySet){
                ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<String, String>> getItemBus = this.poolBusWordData.get(itemKey);
                if( !getItemBus.isEmpty() ){
                    keySetReturnedNotEmpty.put(itemKey, getItemBus);
                }
            }
            return keySetReturnedNotEmpty;
        } finally {
            nowNanoTime = null;
            keySet = null;
            keySetReturnedNotEmpty = null;
        }
    }
}
