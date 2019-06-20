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
public class TdataWord implements Serializable {
    /**
     * 
     * 
     * file path where record saved
     */
    public String dirListFile;
    /**
     * UUID record in list of data in file
     */
    public UUID recordUUID;
    /**
     * UUID for this record
     */
    public UUID randomUUID;
    /** 
     * strSubString - returned by one of NcPathToArrListStr.(*NCLVLABC*).retStr
     * or NcPathToArrListStr.NCLVLNUM.retArrListStr method from class NcPathToArrListStr
     */
    public String strSubString;

    /**
     *
     */
    public Integer strSubStringHash;
    /**
     * 
     */
    public Integer typeWord;
    /** hexSubString - returned by toStrUTFinHEX() */
    public String hexSubString;
    /** h - strSubString.hashCode() */
    public Integer hexSubStringHash;
    /** p - (position) subString position in source str */
    public Integer positionSubString;
    /** l - (length) subString length */
    public Integer lengthSubString;

    /**
     *
     */
    public Long recordTime;

    /**
     *
     */
    public Integer recordHash;
    
    
    /**
     *
     */
    public TdataWord() {
        this.randomUUID = UUID.randomUUID();
        this.recordUUID = this.randomUUID;
        this.dirListFile = "";
        this.strSubString = "";
        this.strSubStringHash = -777;
        this.hexSubString = "";
        this.hexSubStringHash = -777;
        this.typeWord = -777;
        this.positionSubString = -777;
        this.lengthSubString = -777;
        long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                new String("")
                .concat(this.randomUUID.toString())
                .concat(this.recordUUID.toString())
                .concat(this.dirListFile)
                .concat(this.strSubString)
                .concat(String.valueOf(this.strSubStringHash))
                .concat(this.hexSubString)
                .concat(String.valueOf(this.typeWord))
                .concat(String.valueOf(this.hexSubStringHash))
                .concat(String.valueOf(this.positionSubString))
                .concat(String.valueOf(this.lengthSubString))
                .concat(String.valueOf(nowSysTime))).hashCode();
    }
    /**
     * 
     * @param recordId
     * @param dirListStorageName
     * @param strSubString
     * @param calcTypeWord
     * @param hexSubString
     * @param positionSubString
     * @param lengthSubString 
     */
    public TdataWord(final UUID recordId,
            final String dirListStorageName,
            final String strSubString,
            final int calcTypeWord,
            final String hexSubString,
            final int positionSubString,
            final int lengthSubString) {

        this.randomUUID = UUID.randomUUID();
        this.recordUUID = recordId;
        this.dirListFile = dirListStorageName;
        this.strSubString = strSubString;
        this.strSubStringHash = strSubString.hashCode();
        this.typeWord = calcTypeWord;
        this.hexSubString = hexSubString;
        this.hexSubStringHash = hexSubString.hashCode();
        this.positionSubString = positionSubString;
        this.lengthSubString = lengthSubString;
        final Long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                new String("")
                .concat(this.randomUUID.toString())
                .concat(this.recordUUID.toString())
                .concat(this.dirListFile)
                .concat(this.strSubString)
                .concat(String.valueOf(this.strSubStringHash))
                .concat(this.hexSubString)
                .concat(String.valueOf(this.typeWord))
                .concat(String.valueOf(this.hexSubStringHash))
                .concat(String.valueOf(this.positionSubString))
                .concat(String.valueOf(this.lengthSubString))
                .concat(String.valueOf(nowSysTime))).hashCode();
    }
}
