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
     * file path where record saved
     */
    public String dirListFile;
    /**
     * UUID record in list of data in file
     */
    public UUID recordUUID;
    public UUID randomUUID;
    /** 
     * strSubString - returned by one of NcPathToArrListStr.(*NCLVLABC*).retStr
     * or NcPathToArrListStr.NCLVLNUM.retArrListStr method from class NcPathToArrListStr
     */
    public String strSubString;

    /**
     *
     */
    public int strSubStringHash;
    public int typeWord;
    /** hexSubString - returned by toStrUTFinHEX() */
    public String hexSubString;
    /** h - strSubString.hashCode() */
    public int hexSubStringHash;
    /** p - (position) subString position in source str */
    public int positionSubString;
    /** l - (length) subString length */
    public int lengthSubString;

    /**
     *
     */
    public long recordTime;

    /**
     *
     */
    public int recordHash;
    
    
    /**
     *
     */
    public TdataWord() {
        this.randomUUID = UUID.randomUUID();
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
                ""
                + this.strSubString
                + this.strSubStringHash
                + this.hexSubString
                + this.hexSubStringHash
                + this.positionSubString
                + this.lengthSubString
                + nowSysTime).hashCode();
    }

    /**
     *
     * @param inClassId
     * @param toFileId
     * @param strSubString
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
        final long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                this.randomUUID.toString()
                + String.valueOf(this.recordUUID.toString())
                + String.valueOf(this.dirListFile)
                + this.strSubString
                + String.valueOf(this.strSubStringHash)
                + this.hexSubString
                + String.valueOf(this.hexSubStringHash)
                + String.valueOf(this.positionSubString)
                + String.valueOf(this.lengthSubString)
                + String.valueOf(nowSysTime)).hashCode();
    }
}
