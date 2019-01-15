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

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 *
 * @author wladimirowichbiaran
 */
/**
 * for managment in runned threads create class AppThWorkDirListRule
 * Run and check state of work process, for example:
 * need create file and directory list of storage, this work in iteration list:
 * 1. init and create index storage in zip (in AppThManagerIndexStorage class) 
 *              (by static functions AppFileManagerIndexStorageHelper)
 * 1.1. found for existing storage and open it, if not exist create new (in this class)
 *              (by static functions AppFileManagerIndexStorageHelper)
 * 1.2. get for start of process directory, if new storage: (in this class)
 * 1.2.1. get roots of storages, select first root  (in this class)
 * - 1.2.2. create new Objects for working process by list:  (in AppThWorkDirListState class)
 *              put to constructor of class, selected Path of root
 * - 1.2.2.1. AppThWorkDirListRun - Run NIO 2 File.walker, create and make Objects for Directory List
 * - 1.2.2.2. AppThWorkDirListTake - Get List elements in buffer and make sort for directory or file
 * - 1.2.2.3. AppThWorkDirListPack - Two instance, first for directories, second for files (for links -?)
 * - 1.2.2.3.1. instance for directories, make list for first char in UTF-8 and put to wirter
 * - 1.2.2.3.2. instance for files, make list for first char in UTF-8 and put to wirter
 * - 1.2.2.4. AppThWorkDirListWrite found for store file, read it, add record and write list, when
 *              count of records equal of records limit, create lock (*.lck) file for file storage
 * 1.3.1. if storage exist (in this class)
 * 1.3.1.1. read all indexes, and compare him with disk objects (in AppThWorkDirListCheck class)
 * 1.3.1.2. if in directory and record of list has changes: new objects (sub directories, files, links)
 * 1.3.1.2.1. create temporary list and put in it record for this element 
 *              (Path in List of Path) (in AppThWorkDirListCreateNewTmp class)
 * 1.3.1.2.2. do compare index with disk objects (in AppThWorkDirListCheck class)
 * 1.3.1.3. if in directory and record of list has changes: change date, size of disk object
 * 1.3.1.3.1. mark record "need change", and datetime stamp, create temp list 
 *              for need change records (Path, UUID, file contaned record)
 *              (in AppThWorkDirListCreateChangesTmp class)
 * 1.3.1.3.2. do compare index with disk objects (in AppThWorkDirListCheck class)
 * 1.3.2. get for temp list for need change records by 1.3.1.3.1. (in AppThWorkDirListMakeChanges class)
 * 1.3.2.1. do it 1.2.1 process analogy, and change status of record:
 * 1.3.2.2. create new record with UUID of old record, marked "need change"
 * 1.3.2.3. delete "need change", and datetime stamp
 * 1.3.2.4. mark "changed" and write to filed UUID of new record, and datetime stamp of changes
 * 1.3.3. if found for "need change" record and his changes process is not finished normal, fix it
 * 1.3.4. get for temp list for need make new records 1.3.1.2.1. (in AppThWorkDirListMakeAdditions class)
 * 1.4. after check existing index, build index for other object of storages by process in 1.2.1
 * @author wladimirowichbiaran
 */
public class AppObjectsManagerState {
    private Path currentSelectedPathForMakeIndex;
    private AppThWorkDirListState currentWorkState;
    private AppObjectsList currentListOfObjects;
    private AppThManager thManager;

    public AppObjectsManagerState(AppThManager appThManager) {
        this.thManager = appThManager;
        this.currentListOfObjects = appThManager.getListOfObjects();
        this.currentSelectedPathForMakeIndex = Paths.get("/");
        
        this.currentWorkState = new AppThWorkDirListState(this.currentListOfObjects, this.currentSelectedPathForMakeIndex);
        
    }
    protected void runWorkMakeDirList(){
        this.currentWorkState.makeDirList();
    }
}
