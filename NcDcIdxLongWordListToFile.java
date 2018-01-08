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
public class NcDcIdxLongWordListToFile implements Serializable {
    /**
     * nameID - index of files contained data for long world 
     */
    public long nameID;
    /**
     * name - name for this files, heximal string of long word 
     */
    public String name;
    /**
     * nameHash - hash code for name heximal string 
     */
    public int nameHash;
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
    public long recordTime;

    /**
     *
     */
    public int recordHash;
    /**
     * 
     * @param name
     * @param word
     * @param wordHash 
     */
    public NcDcIdxLongWordListToFile() {
        this.nameID = -777;
        this.name = "";
        this.nameHash = -777;
        this.word = "";
        this.wordHash = -777;
        long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                ""
                + this.nameID
                + this.name
                + this.nameHash
                + this.word
                + this.wordHash
                + nowSysTime).hashCode();
    }

    /**
     *
     * @param nameID
     * @param name
     * @param word
     */
    public NcDcIdxLongWordListToFile(long nameID, String name, String word) {
        this.nameID = nameID;
        this.name = name;
        this.nameHash = name.hashCode();
        this.word = word;
        this.wordHash = word.hashCode();
        long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                ""
                + this.nameID
                + this.name
                + this.nameHash
                + this.word
                + this.wordHash
                + nowSysTime).hashCode();
    }
    
}
