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

import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;

/**
 *
 * @author Администратор
 */
public class NcParamFv implements Serializable{

    /**
     *
     */
    public String indexPath;

    /**
     *
     */
    public String keywordsOutOfSearch;

    /**
     *
     */
    public String keywordsInSearch;

    /**
     *
     */
    public String dirOutOfIndex;

    /**
     *
     */
    public String dirInIndex;

    /**
     *
     */
    public TreeMap<Integer, String> diskUserAlias;

    /**
     *
     */
    public String strHexMD5;

    /**
     *
     */
    public String strHexSHA1;

    /**
     *
     */
    public String strHexSHA256;

    /**
     *
     */
    public String strHexSHA512;

    /**
     *
     */
    public TreeMap<Integer, File> tmIndexSubDirs;
    public long recordTime;
    public long recordHash;
    /**
     * 
     * @param indexPath
     * @param keywordsOutOfSearch
     * @param keywordsInSearch
     * @param dirOutOfIndex
     * @param dirInIndex
     * @param diskUserAlias 
     * @param strHexMD5 
     * @param strHexSHA1 
     * @param strHexSHA256 
     * @param strHexSHA512 
     * @param tmIndexSubDirs 
     */
    public NcParamFv(){
        this.indexPath = "";
        this.keywordsOutOfSearch = "";
        this.keywordsInSearch = "";
        this.dirOutOfIndex = "";
        this.dirInIndex = "";
        this.diskUserAlias = new TreeMap<Integer, String>();
        this.strHexMD5 = "";
        this.strHexSHA1 = "";
        this.strHexSHA256 = "";
        this.strHexSHA512 = "";
        this.tmIndexSubDirs = new TreeMap<Integer, File>();
        long nowTime = System.nanoTime();
        this.recordTime = nowTime;
        this.recordHash = (""
            + this.indexPath
            + this.keywordsOutOfSearch
            + this.keywordsInSearch
            + this.dirOutOfIndex
            + this.diskUserAlias.hashCode()
            + this.strHexMD5
            + this.strHexSHA1
            + this.strHexSHA256
            + this.strHexSHA512
            + this.tmIndexSubDirs.hashCode()
            + this.recordTime).hashCode();
    }

    public NcParamFv(String indexPath,
            String keywordsOutOfSearch,
            String keywordsInSearch,
            String dirOutOfIndex,
            String dirInIndex,
            TreeMap<Integer, String> diskUserAlias,
            String strHexMD5,
            String strHexSHA1,
            String strHexSHA256,
            String strHexSHA512,
            TreeMap<Integer, File> tmIndexSubDirs) {
        this.indexPath = indexPath;
        this.keywordsOutOfSearch = keywordsOutOfSearch;
        this.keywordsInSearch = keywordsInSearch;
        this.dirOutOfIndex = dirOutOfIndex;
        this.dirInIndex = dirInIndex;
        this.diskUserAlias = diskUserAlias;
        this.strHexMD5 = strHexMD5;
        this.strHexSHA1 = strHexSHA1;
        this.strHexSHA256 = strHexSHA256;
        this.strHexSHA512 = strHexSHA512;
        this.tmIndexSubDirs = tmIndexSubDirs;
        long nowTime = System.nanoTime();
        this.recordTime = nowTime;
        this.recordHash = (""
            + this.indexPath
            + this.keywordsOutOfSearch
            + this.keywordsInSearch
            + this.dirOutOfIndex
            + this.diskUserAlias.hashCode()
            + this.strHexMD5
            + this.strHexSHA1
            + this.strHexSHA256
            + this.strHexSHA512
            + this.tmIndexSubDirs.hashCode()
            + this.recordTime).hashCode();
    }
    
    
}
