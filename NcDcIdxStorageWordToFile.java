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
public class NcDcIdxStorageWordToFile implements Serializable {
    /**
     * nameID - index of files contained data for long world 
     */
    public long wordId;

    /**
     *
     */
    public boolean isLongWord;
    /**
     * name - name for this files, heximal string of long word 
     */
    public String wordInHex;
    /**
     * nameHash - hash code for name heximal string 
     */
    public int wordInHexHash;
    /**
     * word - word in string 
     */
    public String word;
    /**
     * wordHash - hash code for word in string 
     */
    public int wordHash;

    /**
     *
     */
    public long wordCount;

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
     * @param nameID
     * @param name
     * @param nameHash
     * @param word
     * @param wordHash 
     */

    public NcDcIdxStorageWordToFile() {
        this.wordId = -777;
        this.isLongWord = false;
        this.wordInHex = "";
        this.wordInHexHash = -777;
        this.word = "";
        this.wordHash = -777;
        this.wordCount = -777;
        long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                ""
                + this.wordId
                + this.isLongWord
                + this.wordInHex
                + this.wordInHexHash
                + this.word
                + this.wordHash
                + this.wordCount
                + nowSysTime).hashCode();
    }

    /**
     *
     * @param wordId
     * @param isLongWord
     * @param wordInHex
     * @param word
     * @param wordCount
     */
    public NcDcIdxStorageWordToFile(long wordId, boolean isLongWord, String wordInHex, String word, long wordCount) {
        this.wordId = wordId;
        this.isLongWord = isLongWord;
        this.wordInHex = wordInHex;
        this.wordInHexHash = wordInHex.hashCode();
        this.word = word;
        this.wordHash = word.hashCode();
        this.wordCount = wordCount;
        long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                ""
                + this.wordId
                + this.isLongWord
                + this.wordInHex
                + this.wordInHexHash
                + this.word
                + this.wordHash
                + this.wordCount
                + nowSysTime).hashCode();
    }
    
}
