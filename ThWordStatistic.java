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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * What kind of work and how much
 * @author wladimirowichbiaran
 */
public class ThWordStatistic {
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
     * * - (1) In release only for storage of Word index not apply
     *   ConcurrentHashMap<Integer,   - (2) - Type of word index hash value
     *     ConcurrentHashMap<String,  - (3) - tagFileName with hex view
     *       ConcurrentHashMap<Integer, Integer>>>> - (4) - <hashFieldName, Value>
     * (4) hashFieldName:
     *     - CountRecords - updated onWrite, before write (Read, Write into old
     *             file name, after write Files.move to newFileName
     *     - VolumeNumber - update onWrite, before write = ifLimit ? update : none
     *     - CurrentFileName - full file name where read from data
     *     - NewFileName - full file name for Files.move operation after write
     *             created when readJobDataSize
     *     - LastAccNanotime - update onWrite, before write
     *             
     *     - countDataAccum - update onWrite, before write, count++
     */
    private ConcurrentHashMap<Integer, ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>>> fileStoragesMap;

    public ThWordStatistic() {
        this.fileStoragesMap = new ConcurrentHashMap<Integer, ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>>>();
    }
    
    protected static Map<String, String> getOperationsFileNames(final int typeWordOuter, final String tagFileNameOuter){
        Map<String, String> returnedNames;
        try{
            returnedNames = new HashMap<String, String>();
            
            
            return returnedNames;
        } finally {
            returnedNames = null;
        }
    }
    /**
     * return list of not limited files from structure
     * @param typeWordOuter
     * @return 
     */
    protected ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>> getListByType(final int typeWordOuter){
        ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>> forListReturn;
        try{
            forListReturn = this.fileStoragesMap.get(typeWordOuter);
            if( forListReturn == null ){
                //init for list
            }
            return forListReturn;
        } finally {
            forListReturn = null;
        }
    }
    /**
     * 
     * ThWordStateStorage - Bus:
     *      From file system storages directory by type bus ArrayBlockingQueue<Path>
     *      if new type, create bus
     * Read from storage file system list of files,
     * filter in readed list limited files
     * if type directory not exist, create empty list
     * @return 
     */
    protected ConcurrentHashMap<Integer, Integer> getRecordForList(final int typeWordStorageOuter){
        ConcurrentHashMap<Integer, Integer> forListTypeRecordsReturn;
        try{
            forListTypeRecordsReturn = new ConcurrentHashMap<Integer, Integer>();
            
            return forListTypeRecordsReturn;
        } finally {
            forListTypeRecordsReturn = null;
        }
    }
    
}
