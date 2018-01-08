/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
