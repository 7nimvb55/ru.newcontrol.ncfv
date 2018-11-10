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

/**
 *
 * @author wladimirowichbiaran
 */
/**
 * Run and check state of work process, for example:
 * need create file and directory list of storage, this work in iteration list:
 * 1. init and create index storage in zip archive
 * 1.1. found for existing storage and open it, if not exist create new
 * 1.2. get for start of process directory, if new storage: 
 * 1.2.1. get roots of storages, select first root
 * 1.2.2. create new Objects for working process by list:
 * 1.2.2.1. AppThDirListRun - Run NIO 2 File.walker, create and make Objects for Directory List
 * 1.2.2.2. AppThDirListTake - Get List elements in buffer and make sort for directory or file
 * 1.2.2.3. AppThDirListPack - Two instance, first for directories, second for files (for links -?)
 * 1.2.2.3.1. instance for directories, make list for first char in UTF-8 and put to wirter
 * 1.2.2.3.2. instance for files, make list for first char in UTF-8 and put to wirter
 * 1.2.2.4. AppThDirListWrite found for store file, read it, add record and write list, when
 *              count of records equal of records limit, create lock (*.lck) file for file storage
 * 1.3.1. if storage exist, 
 * 1.3.1.1. read all indexes, and compare him with disk objects
 * 1.3.1.2. if in directory and record of list has changes: new objects (sub directories, files, links)
 * 1.3.1.2.1. create temporary list and put in it record for this element (Path in List<Path>)
 * 1.3.1.2.2. do compare index with disk objects
 * 1.3.1.3. if in directory and record of list has changes: change date, size of disk object
 * 1.3.1.3.1. mark record "need change", and datetime stamp, create temp list for need change records (Path, UUID, file contaned record)
 * 1.3.1.3.2. do compare index with disk objects
 * 1.3.2. get for temp list for need change records by 1.3.1.3.1.
 * 1.3.2.1. do it 1.2.1 process analogy, and change status of record:
 * 1.3.2.2. create new record with UUID of old record, marked "need change"
 * 1.3.2.3. delete "need change", and datetime stamp
 * 1.3.2.4. mark "changed" and write to filed UUID of new record, and datetime stamp of changes
 * 1.3.3. if found for "need change" record and his changes process is not finished normal, fix it
 * 1.3.4. after check existing index, build index for other object of storges by process in 1.2.1
 * @author wladimirowichbiaran
 */
public class AppObjectsManagerState {

    public AppObjectsManagerState() {
    }
    
}
