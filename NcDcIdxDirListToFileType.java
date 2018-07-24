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
public class NcDcIdxDirListToFileType implements Serializable {
    /**
     * dirListID - identification descriptor (ID) of records in file
     */
    public long dirListID;
    /**
     * fileContentType - String Files.probeContentType()
     */
    public String fileContentType;

    /**
     *
     */
    public int fileContentTypeHash;

    /**
     *
     */
    public String fileExtention;

    /**
     *
     */
    public int fileExtentionHash;

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
     * @param fileContentType 
     */
    public NcDcIdxDirListToFileType() {
        this.dirListID = -777;
        this.fileContentType = "";
        this.fileContentTypeHash = -777;
        this.fileExtention = "";
        this.fileExtentionHash = -777;
        long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                ""
                + this.dirListID
                + this.fileContentType
                + this.fileContentTypeHash
                + this.fileExtention
                + this.fileExtentionHash
                + nowSysTime).hashCode();
    }

    /**
     *
     * @param dirListID
     * @param fileExtention
     * @param fileContentType
     */
    public NcDcIdxDirListToFileType(long dirListID, String fileExtention, String fileContentType) {
        this.dirListID = dirListID;
        this.fileContentType = fileContentType;
        this.fileContentTypeHash = fileContentType.hashCode();
        this.fileExtention = fileExtention;
        this.fileExtentionHash = fileExtention.hashCode();
        long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                ""
                + this.dirListID
                + this.fileContentType
                + this.fileContentTypeHash
                + this.fileExtention
                + this.fileExtentionHash
                + nowSysTime).hashCode();
    }
}
