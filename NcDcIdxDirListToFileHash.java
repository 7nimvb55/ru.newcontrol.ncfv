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
public class NcDcIdxDirListToFileHash implements Serializable {
    /**
     * dirListID - identification descriptor (ID) of records in file
     */
    public long dirListID;
    /**
     * fileLength - File.length()
     */
    public long fileLength;
    /**
     * fileLastModified - File.lastModified()
     */
    public long fileLastModified;
    /**
     * fileChecksumMd5 - toHex(getFileHash.MD5.checksum(file))
     */
    public String fileChecksumMd5;

    /**
     *
     */
    public int fileChecksumMd5Hash;
    /**
     * fileChecksumSha1 - toHex(getFileHash.SHA1.checksum(file))
     */
    public String fileChecksumSha1;

    /**
     *
     */
    public int fileChecksumSha1Hash;
    /**
     * fileChecksumSha256 - toHex(getFileHash.SHA256.checksum(file))
     */
    public String fileChecksumSha256;

    /**
     *
     */
    public int fileChecksumSha256Hash;
    /**
     * fileChecksumSha512 - toHex(getFileHash.SHA512.checksum(file))
     */
    public String fileChecksumSha512;

    /**
     *
     */
    public int fileChecksumSha512Hash;

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
     * @param dirListID
     * @param fileLastModified
     * @param fileChecksumSha1
     * @param fileChecksumSha512 
     */
    public NcDcIdxDirListToFileHash() {
        this.dirListID = -777;
        this.fileLength = -777;
        this.fileLastModified = -777;
        this.fileChecksumMd5 = "";
        this.fileChecksumMd5Hash = -777;
        this.fileChecksumSha1 = "";
        this.fileChecksumSha1Hash = -777;
        this.fileChecksumSha256 = "";
        this.fileChecksumSha256Hash = -777;
        this.fileChecksumSha512 = "";
        this.fileChecksumSha512Hash = -777;
        long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                ""
                + this.dirListID
                + this.fileLength
                + this.fileLastModified
                + this.fileChecksumMd5
                + this.fileChecksumMd5Hash
                + this.fileChecksumSha1
                + this.fileChecksumSha1Hash
                + this.fileChecksumSha256
                + this.fileChecksumSha256Hash
                + this.fileChecksumSha512
                + this.fileChecksumSha512Hash
                + nowSysTime).hashCode();
    }

    /**
     *
     * @param dirListID
     * @param fileLength
     * @param fileLastModified
     * @param fileChecksumMd5
     * @param fileChecksumSha1
     * @param fileChecksumSha256
     * @param fileChecksumSha512
     */
    public NcDcIdxDirListToFileHash(long dirListID,
            long fileLength,
            long fileLastModified,
            String fileChecksumMd5,
            String fileChecksumSha1,
            String fileChecksumSha256,
            String fileChecksumSha512) {
        this.dirListID = dirListID;
        this.fileLength = fileLength;
        this.fileLastModified = fileLastModified;
        this.fileChecksumMd5 = fileChecksumMd5;
        this.fileChecksumMd5Hash = fileChecksumMd5.hashCode();
        this.fileChecksumSha1 = fileChecksumSha1;
        this.fileChecksumSha1Hash = fileChecksumSha1.hashCode();
        this.fileChecksumSha256 = fileChecksumSha256;
        this.fileChecksumSha256Hash = fileChecksumSha256.hashCode();
        this.fileChecksumSha512 = fileChecksumSha512;
        this.fileChecksumSha512Hash = fileChecksumSha512.hashCode();
        long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                ""
                + this.dirListID
                + this.fileLength
                + this.fileLastModified
                + this.fileChecksumMd5
                + this.fileChecksumMd5Hash
                + this.fileChecksumSha1
                + this.fileChecksumSha1Hash
                + this.fileChecksumSha256
                + this.fileChecksumSha256Hash
                + this.fileChecksumSha512
                + this.fileChecksumSha512Hash
                + nowSysTime).hashCode();
    }
    
}
