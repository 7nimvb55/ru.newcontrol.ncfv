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

/**
 * Adih
 * <ul>
 * <li>Automated
 * <li>data
 * <li>indexing
 * <li>helper Utilization generated variables
 * </ul>
 * @author wladimirowichbiaran
 */
public class AdihUtilization {
    /**
     * 
     * @param prevData
     * @return 
     */
    protected static ConcurrentSkipListMap<UUID, TdataWord> doUtilizationDataInitNew(ConcurrentSkipListMap<UUID, TdataWord> prevData){
        utilizeTdataWord(prevData);
        return new ConcurrentSkipListMap<UUID, TdataWord>();
    }
    /**
     * 
     * @param forUtilizationData 
     */
    protected static void utilizeTdataWord(ConcurrentSkipListMap<UUID, TdataWord> forUtilizationData){
        UUID keyForDelete;
        TdataWord removedData;
        try {
            for( Map.Entry<UUID, TdataWord> deletingItem : forUtilizationData.entrySet() ){
                keyForDelete = deletingItem.getKey();
                removedData = forUtilizationData.remove(keyForDelete);
                removedData.dirListFile = null;
                removedData.hexSubString = null;
                removedData.hexSubStringHash = null;
                removedData.lengthSubString = null;
                removedData.positionSubString = null;
                removedData.randomUUID = null;
                removedData.recordHash = null;
                removedData.recordTime = null;
                removedData.recordUUID = null;
                removedData.strSubString = null;
                removedData.strSubStringHash = null;
                removedData.typeWord = null;
                removedData = null;
                keyForDelete = null;
            }
            forUtilizationData = null;
        } finally {
            keyForDelete = null;
            removedData = null;
        }
    }
    /**
     * 
     * @param valuesForDelete 
     */
    protected static void utilizeStringValues(String[] valuesForDelete){
        for( String deletedItem : valuesForDelete ){
            deletedItem = null;
        }
        valuesForDelete = null;
    }
}
