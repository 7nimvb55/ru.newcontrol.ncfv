/*
 * Copyright 2018 wladimirowichbiaran.
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
 *
 * @author wladimirowichbiaran
 */
public class NcDataListAttr implements Serializable  {
    private final UUID randomUUID;
        
    private final Path file;
    private final Path fileName;
    private final Path fileRealPath;
    private final int hashCode;
        
    private final Set<PosixFilePermission> posixFilePermissions;
        
    private final FileTime creationTime;
    private final FileTime lastAccessTime;
    private final FileTime lastModifiedTime;
    private final long sizeFromAttr;
    private final long sizeFromFiles;
        
    private final boolean directory;
    private final boolean readable;
    private final boolean writable;
    private final boolean executable;
    private final boolean other;
    private final boolean symbolicLink;
    private final boolean absolute;
    private final boolean notExists;
    private final boolean exists;

    private final boolean regularFile;
    private final boolean hidden;

    private final boolean exHidden;
    private final boolean exPosix;
    
    private final boolean exReal;
    private final boolean exSize;
    private final boolean notEqualSize;

    public NcDataListAttr(Path file, Path fileName, Path fileRealPath,
            int hashCode, Set<PosixFilePermission> posixFilePermissions,
            FileTime creationTime, FileTime lastAccessTime,
            FileTime lastModifiedTime, long sizeFromAttr,
            long sizeFromFiles, boolean directory, boolean readable,
            boolean writable, boolean executable, boolean other,
            boolean symbolicLink, boolean absolute, boolean notExists,
            boolean exists, boolean regularFile, boolean hidden,
            boolean exHidden, boolean exPosix, boolean exReal,
            boolean exSize, boolean notEqualSize) {
        randomUUID = UUID.randomUUID();
        this.file = file;
        this.fileName = fileName;
        this.fileRealPath = fileRealPath;
        this.hashCode = hashCode;
        this.posixFilePermissions = posixFilePermissions;
        this.creationTime = creationTime;
        this.lastAccessTime = lastAccessTime;
        this.lastModifiedTime = lastModifiedTime;
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
