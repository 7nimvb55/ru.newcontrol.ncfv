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
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;
import java.util.UUID;

/**
 * <p> TdataDirListFsObjAttr class provided by {@link java.io.Serializable}
 * 
 * {@code Td}ata - Threads Data
 * {@code DirList} - for Threads released part logic app creation list of
 * {@code F}ile {@code s}ystem {@code Obj}ects - and his list used in maked index from
 * {@code Attr}ibutes - of objects on file systems from some disks
 * @author wladimirowichbiaran
 */
public class TdataDirListFsObjAttr implements Serializable  {
    public final long pcNumber;
    public final long hightNumber;
    public final long lowNumber;
    
    public final UUID randomUUID;
        
    public final String file;
    public final String fileName;
    public final String fileRealPath;
    public final int hashCode;
        
    //private final Set<PosixFilePermission> posixFilePermissions;
        
    public final long creationTime;
    public final long lastAccessTime;
    public final long lastModifiedTime;
    public final long sizeFromAttr;
    public final long sizeFromFiles;
        
    public final boolean directory;
    public final boolean readable;
    public final boolean writable;
    public final boolean executable;
    public final boolean other;
    public final boolean symbolicLink;
    public final boolean absolute;
    public final boolean notExists;
    public final boolean exists;

    public final boolean regularFile;
    public final boolean hidden;

    public final boolean exHidden;
    public final boolean exPosix;
    
    public final boolean exReal;
    public final boolean exSize;
    public final boolean notEqualSize;
    /**
     * 
     * @param file
     * @param fileName
     * @param fileRealPath
     * @param hashCode
     * @param posixFilePermissions
     * @param creationTime
     * @param lastAccessTime
     * @param lastModifiedTime
     * @param sizeFromAttr
     * @param sizeFromFiles
     * @param directory
     * @param readable
     * @param writable
     * @param executable
     * @param other
     * @param symbolicLink
     * @param absolute
     * @param notExists
     * @param exists
     * @param regularFile
     * @param hidden
     * @param exHidden
     * @param exPosix
     * @param exReal
     * @param exSize
     * @param notEqualSize 
     */

    public TdataDirListFsObjAttr(final long lowNumber, final Path file, final Path fileName, final Path fileRealPath,
            final int hashCode, final Set<PosixFilePermission> posixFilePermissions,
            final FileTime creationTime, final FileTime lastAccessTime,
            final FileTime lastModifiedTime, final long sizeFromAttr,
            final long sizeFromFiles, final boolean directory, final boolean readable,
            final boolean writable, final boolean executable, final boolean other,
            final boolean symbolicLink, final boolean absolute, final boolean notExists,
            final boolean exists, final boolean regularFile, final boolean hidden,
            final boolean exHidden, final boolean exPosix, final boolean exReal,
            final boolean exSize, final boolean notEqualSize) {
        // for presonal computer id when released network functional
        // in one of main app class for provided this functional
        this.pcNumber = 0;
        // if numbers of journl records above long in lowNumber, need release function
        // in one of main app class for provided this functional
        this.hightNumber = 0;
        this.lowNumber = lowNumber;
        this.randomUUID = UUID.randomUUID();
        this.file = file.toString();
        this.fileName = fileName.toString();
        this.fileRealPath = fileRealPath.toString();
        this.hashCode = hashCode;
        //this.posixFilePermissions = posixFilePermissions;
        
        this.creationTime = creationTime.toMillis();
        this.lastAccessTime = lastAccessTime.toMillis();
        this.lastModifiedTime = lastModifiedTime.toMillis();
        this.sizeFromAttr = sizeFromAttr;
        this.sizeFromFiles = sizeFromFiles;
        this.directory = directory;
        this.readable = readable;
        this.writable = writable;
        this.executable = executable;
        this.other = other;
        this.symbolicLink = symbolicLink;
        this.absolute = absolute;
        this.notExists = notExists;
        this.exists = exists;
        this.regularFile = regularFile;
        this.hidden = hidden;
        this.exHidden = exHidden;
        this.exPosix = exPosix;
        this.exReal = exReal;
        this.exSize = exSize;
        this.notEqualSize = notEqualSize;
    }
    
    protected String getShortDataToString(){
        return "[Path]" + this.file.toString()
                + "[lastModifiedTime]" + this.lastModifiedTime
                + ( this.directory ? "" : "[sizeFromAttr]" + this.sizeFromAttr);
    }
    
    
}
