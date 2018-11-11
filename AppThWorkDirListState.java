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

import java.lang.Thread.State;
import java.nio.file.FileSystem;
import java.nio.file.Path;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppThWorkDirListState {
    private AppThWorkDirListRule ruleForDirListWorkers;
    
    private AppThManagerIndexStorage indexStorageManager;
    
    private AppThWorkDirListRun runDirlistReader;
    private AppThWorkDirListTake runDirlistTacker;
    private AppThWorkDirListPack runDirListPacker;
    private AppThWorkDirListWrite runDirListWriter;
    private FileSystem currentFsZipIndexStorage;
    

    public AppThWorkDirListState(Path makeIndex) {
        
        this.ruleForDirListWorkers = new AppThWorkDirListRule(makeIndex);
        this.indexStorageManager = new AppThManagerIndexStorage(this.ruleForDirListWorkers);
        this.runDirlistReader = new AppThWorkDirListRun(this.ruleForDirListWorkers);
        this.runDirlistTacker = new AppThWorkDirListTake(this.ruleForDirListWorkers);
        this.runDirListPacker = new AppThWorkDirListPack(this.ruleForDirListWorkers);
        this.runDirListWriter = new AppThWorkDirListWrite(this.ruleForDirListWorkers);
    }
    protected void initWorkerGroup(){
        //@todo ThreadGroup, set names, build fields for timestamp class creation
        //@todo make validator from timestamp fields, and threads hashes of toStrings
        
        
    }
    protected void makeDirList(){
        this.ruleForDirListWorkers.setWorkDirListState(this);
        
        //this.currentFsZipIndexStorage = fsZipIndexStorage;
        this.indexStorageManager.start();
        int countWaitIteration = 0;
        while( !this.ruleForDirListWorkers.isStorageSetted() ){
            countWaitIteration++;
        }
        this.currentFsZipIndexStorage = this.ruleForDirListWorkers.getFsZipIndexStorage();
        
        this.ruleForDirListWorkers.setDirlistReader(this.runDirlistReader);
        this.ruleForDirListWorkers.setDirlistTacker(this.runDirlistTacker);
        this.ruleForDirListWorkers.setDirListPacker(runDirListPacker);
        this.ruleForDirListWorkers.setDirListWriter(this.runDirListWriter);
        
        this.startDirlistReader();
    }
    protected State getStateDirlistReader(){
        return this.runDirlistReader.getState();
    }
    protected State getStateDirlistTacker(){
        return this.runDirlistTacker.getState();
    }
    protected State getStateDirlistPacker(){
        return this.runDirListPacker.getState();
    }
    protected State getStateDirlistWriter(){
        return this.runDirListWriter.getState();
    }
    
    protected void startDirlistReader(){
        this.runDirlistReader.start();
    }
    protected void startDirlistTacker(){
        this.runDirlistTacker.start();
    }
    protected void startDirlistPacker(){
        this.runDirListPacker.start();
    }
    protected void startDirlistWriter(){
        this.runDirListWriter.start();
    }
    
    protected void stopDirlistReader(){
        this.ruleForDirListWorkers.sayNeedFinishDirlistReader();
    }
    protected void stopDirlistTacker(){
        this.ruleForDirListWorkers.sayNeedFinishDirlistTacker();
    }
    protected void stopDirlistPacker(){
        this.ruleForDirListWorkers.sayNeedFinishDirListPacker();
    }
    protected void stopDirlistWriter(){
        this.ruleForDirListWorkers.sayNeedFinishDirListWriter();
    }
    
}
