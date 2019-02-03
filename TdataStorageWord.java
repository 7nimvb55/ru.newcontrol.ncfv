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

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author wladimirowichbiaran
 */
public class TdataStorageWord implements Serializable {
    /**
     * file path where record saved
     */
    public String dirListFile;
    /**
     * UUID record in list of data in file
     */
    public UUID recordDirListUUID;
    /**
     * 
     */
    public UUID thisRecordUUID;
    /** 
     * strSubString - returned by one of NcPathToArrListStr.(*NCLVLABC*).retStr
     * or NcPathToArrListStr.NCLVLNUM.retArrListStr method from class NcPathToArrListStr
     */
    public String strSubString;
    /**
     * 
     */
    public int strSubStringHash;
    /**
     * 
     */
    public int typeWord;
    /** 
     * hexSubString - returned by toStrUTFinHEX() 
     */
    public String hexSubString;
    /** 
     * h - strSubString.hashCode() 
     */
    public int hexSubStringHash;
    /**
     *
     */
    public long recordTime;
    /**
     *
     */
    public long countWords;
    /**
     *
     */
    public int recordHash;
    /**
     * Class for serialize into StorageWord index system
     * this is blank data constructor
     */
    public TdataStorageWord() {
        this.thisRecordUUID = UUID.randomUUID();
        this.recordDirListUUID = this.thisRecordUUID;
        this.dirListFile = "";
        this.strSubString = "";
        strSubStringHash = strSubString.hashCode();
        typeWord = -777;
        hexSubString = "";
        hexSubStringHash = hexSubString.hashCode();
        long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.countWords = -777L;
        this.recordHash = (
                new String("")
                .concat(dirListFile)
                .concat(strSubString)
                .concat(String
                        .valueOf(strSubStringHash))
                .concat(hexSubString)
                .concat(String
                        .valueOf(hexSubStringHash))
                .concat(String
                        .valueOf(recordTime))
                .concat(String.valueOf(countWords))
                .hashCode()
                );
    }
    /**
     * Class for serialize into StorageWord index system
     * 
     * @param recordDirListUUIDOuter
     * @param dirListFileOuter
     * @param strSubStringOuter
     * @param typeWordOuter
     * @param hexSubStringOuter
     * @param countWordsOuter 
     */
    public TdataStorageWord(
        final UUID recordDirListUUIDOuter,
        final String dirListFileOuter,
        final String strSubStringOuter,
        final int typeWordOuter,
        final String hexSubStringOuter,
        final long countWordsOuter) {
        
        this.thisRecordUUID = UUID.randomUUID();
        this.recordDirListUUID = recordDirListUUIDOuter;
        this.dirListFile = dirListFileOuter;
        this.strSubString = strSubStringOuter;
        this.strSubStringHash = strSubString.hashCode();
        this.typeWord = typeWordOuter;
        this.hexSubString = hexSubStringOuter;
        this.hexSubStringHash = hexSubString.hashCode();
        long nowSysTime = System.nanoTime();
        this.recordTime = (long) nowSysTime;
        this.countWords = countWordsOuter;
        this.recordHash = (
                new String("")
                .concat(dirListFile)
                .concat(strSubString)
                .concat(String
                        .valueOf(strSubStringHash))
                .concat(hexSubString)
                .concat(String
                        .valueOf(hexSubStringHash))
                .concat(String
                        .valueOf(recordTime))
                .concat(String.valueOf(countWords))
                .hashCode()
                );
    }
}