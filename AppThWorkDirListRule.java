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

import java.nio.file.FileSystem;
import java.nio.file.Path;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppThWorkDirListRule {
    private ThreadGroup workerDirList;
    
    private String nameIndexStorage;
    private String nameDirlistReader;
    private String nameDirlistTacker;
    private String nameDirListPacker;
    private String nameDirListWriter;
    
    private Path currentPathForMakeIndex;
    private Boolean needFinishDirlistReader;
    private Boolean needFinishDirlistTacker;
    private Boolean needFinishDirListPacker;
    private Boolean needFinishDirListWriter;
    
    private FileSystem currentFsZipIndexStorage;
    private Boolean storageSetted;
    
    private AppThWorkDirListRun runDirlistReader;
    private AppThWorkDirListTake runDirlistTacker;
    private AppThWorkDirListPack runDirListPacker;
    private AppThWorkDirListWrite runDirListWriter;
    
    private AppThWorkDirListState workDirListState;

    public AppThWorkDirListRule(Path pathForMakeIndex) {
        this.storageSetted = Boolean.FALSE;
        this.nameIndexStorage = "IndexStorage";
        this.nameDirlistReader = "DirlistReader";
        this.nameDirlistTacker = "DirlistTacker";
        this.nameDirListPacker = "DirListPacker";
        this.nameDirListWriter = "DirListWriter";
        
        this.workerDirList = new ThreadGroup("workerDirListGroup");
        
        this.currentPathForMakeIndex = pathForMakeIndex;
        this.needFinishDirlistReader = Boolean.FALSE;
        this.needFinishDirlistTacker = Boolean.FALSE;
        this.needFinishDirListPacker = Boolean.FALSE;
        this.needFinishDirListWriter = Boolean.FALSE;
    }
    protected void setWorkDirListState(AppThWorkDirListState outerWorkDirListState){
        this.storageSetted = Boolean.TRUE;
        this.workDirListState = outerWorkDirListState;
    }
    protected Boolean isStorageSetted(){
        return this.storageSetted;
    }
    protected AppThWorkDirListState getWorkDirListState(){
        return this.workDirListState;
    }
    protected String getNameIndexStorage(){
        return nameIndexStorage;
    }
    protected String getNameDirlistReader(){
        return nameDirlistReader;
    }
    protected String getNameDirlistTacker(){
        return nameDirlistTacker;
    }
    protected String getNameDirListPacker(){
        return nameDirListPacker;
    }
    protected String getNameDirListWriter(){
        return nameDirListWriter;
    }
    protected ThreadGroup getThreadGroupWorkerDirList(){
        return this.workerDirList;
    }
    protected FileSystem setFsZipIndexStorage(FileSystem outerFsZipIndexStorage){
        this.currentFsZipIndexStorage = outerFsZipIndexStorage;
        return this.currentFsZipIndexStorage;
    }
    protected FileSystem getFsZipIndexStorage(){
        return this.currentFsZipIndexStorage;
    }
    
    protected void setDirlistReader(AppThWorkDirListRun outerDirlistReader){
        this.runDirlistReader = outerDirlistReader;
    }
    protected void setDirlistTacker(AppThWorkDirListTake outerDirlistTacker){
        this.runDirlistTacker = outerDirlistTacker;
    }
    protected void setDirListPacker(AppThWorkDirListPack outerDirListPacker){
        this.runDirListPacker = outerDirListPacker;
    }
    protected void setDirListWriter(AppThWorkDirListWrite outerDirListWriter){
        this.runDirListWriter = outerDirListWriter;
    }
    protected void startDirlistReader(){
        this.runDirlistReader.start();
    }
    protected void startDirlistTacker(){
        this.runDirlistTacker.start();
    }
    protected void startDirListPacker(){
        this.runDirListPacker.start();
    }
    protected void startDirListWriter(){
        this.runDirListWriter.start();
    }
    
    protected Path getCurrentPathForMakeIndex(){
        return this.currentPathForMakeIndex;
    }
    protected Boolean getNeedFinishStateDirlistReader(){
        return this.needFinishDirlistReader;
    }
    protected Boolean getNeedFinishStateDirlistTacker(){
        return this.needFinishDirlistTacker;
    }
    protected Boolean getNeedFinishStateDirListPacker(){
        return this.needFinishDirListPacker;
    }
    protected Boolean getNeedFinishStateDirListWriter(){
        return this.needFinishDirListWriter;
    }
    protected void sayNeedFinishDirlistReader(){
        this.needFinishDirlistReader = Boolean.TRUE;
    }
    protected void sayNeedFinishDirlistTacker(){
        this.needFinishDirlistTacker = Boolean.TRUE;
    }
    protected void sayNeedFinishDirListPacker(){
        this.needFinishDirListPacker = Boolean.TRUE;
    }
    protected void sayNeedFinishDirListWriter(){
        this.needFinishDirListWriter = Boolean.TRUE;
    }
}
