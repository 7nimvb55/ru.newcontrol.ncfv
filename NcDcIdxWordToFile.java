/*
 *  Copyright 2017 Administrator of development departament newcontrol.ru .
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package ru.newcontrol.ncfv;

import java.io.Serializable;

/**
 *
 * @author Администратор
 */
public class NcDcIdxWordToFile implements Serializable {
    /**
     * recordID - (record id) number of record in file
     */
    public long recordID;
    /**
     * dirListID - (directory id) number of record in diretcory list files
     */
    public long dirListID;

    /**
     *
     */
    public String word;
    /**
     * wordHash - (hash) subStringHashCode (String.hashCode())
     */
    public int wordHash;
    /**
     * pathPosition - (position) subString position in source str
     */
    public int pathPosition;
    /**
     * wordLength - (length) subString length
     */
    public int wordLength;

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
     * @param recordID
     * @param dirListID
     * @param pathPosition
     * @param wordLength 
     */

    public NcDcIdxWordToFile() {
        this.recordID = -777;
        this.dirListID = -777;
        this.word = "";
        this.wordHash = -777;
        this.pathPosition = -777;
        this.wordLength = -777;
        long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                ""
                + this.recordID
                + this.dirListID
                + this.word
                + this.wordHash
                + this.pathPosition
                + this.wordLength
                + nowSysTime).hashCode();
    }

    /**
     *
     * @param recordID
     * @param dirListID
     * @param word
     * @param pathPosition
     * @param wordLength
     */
    public NcDcIdxWordToFile(long recordID, long dirListID, String word, int pathPosition, int wordLength) {
        this.recordID = recordID;
        this.dirListID = dirListID;
        this.word = word;
        this.wordHash = word.hashCode();
        this.pathPosition = pathPosition;
        this.wordLength = wordLength;
        long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                ""
                + this.recordID
                + this.dirListID
                + this.word
                + this.wordHash
                + this.pathPosition
                + this.wordLength
                + nowSysTime).hashCode();
    }
    
}
