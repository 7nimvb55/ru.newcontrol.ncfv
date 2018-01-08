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
 * DiskFStype - FileStore.type()
 * @author Администратор
 */
public class NcDiskInfo implements Serializable{
/**
 * diskID - Identification descriptor (ID) for records about disk in Index
 */    
    public long diskID;

    /**
     *
     */
    public long longSerialNumber;

    /**
     *
     */
    public String strHexSerialNumber;

    /**
     *
     */
    public String strFileStore;

    /**
     *
     */
    public String strFileStoreName;

    /**
     *
     */
    public char diskLetter;

    /**
     *
     */
    public String diskFStype;

    /**
     *
     */
    public String programAlias;

    /**
     *
     */
    public String humanAlias;

    /**
     *
     */
    public long totalSpace;

    /**
     *
     */
    public long usedSpace;

    /**
     *
     */
    public long availSpace;

    /**
     *
     */
    public long unAllocatedSpace;

    /**
     *
     */
    public boolean isReadonly;

    /**
     *
     */
    public long recordCreationTime;

    /**
     *
     */
    public int reordHash;
    /**
     * 
     * @param diskID
     * @param longSerialNumber
     * @param strHexSerialNumber
     * @param strFileStore
     * @param strFileStoreName
     * @param diskLetter
     * @param diskFStype
     * @param programAlias
     * @param humanAlias
     * @param totalSpace
     * @param usedSpace
     * @param availSpace
     * @param unAllocatedSpace
     * @param isReadonly 
     */

    public NcDiskInfo(long diskID,
            long longSerialNumber,
            String strHexSerialNumber,
            String strFileStore,
            String strFileStoreName,
            char diskLetter,
            String diskFStype,
            String programAlias,
            String humanAlias,
            long totalSpace,
            long usedSpace,
            long availSpace,
            long unAllocatedSpace,
            boolean isReadonly) {
        long longCreationTime = System.nanoTime();
        this.diskID = diskID;
        this.longSerialNumber = longSerialNumber;
        this.strHexSerialNumber = strHexSerialNumber;
        this.strFileStore = strFileStore;
        this.strFileStoreName = strFileStoreName;
        this.diskLetter = diskLetter;
        this.diskFStype = diskFStype;
        this.programAlias = programAlias;
        this.humanAlias = humanAlias;
        this.totalSpace = totalSpace;
        this.usedSpace = usedSpace;
        this.availSpace = availSpace;
        this.unAllocatedSpace = unAllocatedSpace;
        this.isReadonly = isReadonly;
        this.recordCreationTime = longCreationTime;
        this.reordHash = (""
            + diskID
            + longSerialNumber
            + strHexSerialNumber
            + strFileStore
            + strFileStoreName
            + diskLetter
            + diskFStype
            + programAlias
            + humanAlias
            + totalSpace
            + usedSpace
            + availSpace
            + unAllocatedSpace
            + isReadonly
            + longCreationTime).hashCode();
    }
}
