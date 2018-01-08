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
