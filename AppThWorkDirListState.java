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
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppThWorkDirListState {
    private AppObjectsList currentListOfObject;
    private AppThWorkDirListRule ruleForDirListWorkers;
    
    private final ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>> pipeFromRunnerToTacker;
    private final ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>> pipeFromTackerToPacker;
    private final ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>> pipeFromPackerToWriter;
    
    private Thread indexStorageManager;
    
    private Thread runDirlistReader;
    private Thread runDirlistTacker;
    private Thread runDirListPacker;
    private Thread runDirListWriter;
    private FileSystem currentFsZipIndexStorage;
    

    public AppThWorkDirListState(AppObjectsList outerListOfObject, Path makeIndex) {
        this.currentListOfObject = outerListOfObject;
        this.ruleForDirListWorkers = new AppThWorkDirListRule(makeIndex);
        
        this.pipeFromRunnerToTacker = 
                new ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>>(AppConstants.PIPE_READ_FS_TO_TACKER_WORKER_QUEUE_SIZE);
        this.pipeFromTackerToPacker = 
                new ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>>(AppConstants.PIPE_READ_FS_TO_TACKER_WORKER_QUEUE_SIZE);
        this.pipeFromPackerToWriter = 
                new ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>>(AppConstants.PIPE_READ_FS_TO_TACKER_WORKER_QUEUE_SIZE);
//threads init
        
        this.indexStorageManager = new Thread(
                this.ruleForDirListWorkers.getThreadGroupWorkerDirList(),
                new AppThManagerIndexStorage(this.ruleForDirListWorkers),
                this.ruleForDirListWorkers.getNameIndexStorage());
        this.currentListOfObject.addAnyThread(this.indexStorageManager,
                this.ruleForDirListWorkers.getNameIndexStorage());
        
        this.runDirlistReader = new Thread(
                this.ruleForDirListWorkers.getThreadGroupWorkerDirList(),
                new AppThWorkDirListRun(this.ruleForDirListWorkers),
                this.ruleForDirListWorkers.getNameDirlistReader());
        this.currentListOfObject.addAnyThread(this.runDirlistReader,
                this.ruleForDirListWorkers.getNameDirlistReader());
        
        
        this.runDirlistTacker = new Thread(
                this.ruleForDirListWorkers.getThreadGroupWorkerDirList(),
                new AppThWorkDirListTake(this.ruleForDirListWorkers),
                this.ruleForDirListWorkers.getNameDirlistTacker());
        this.currentListOfObject.addAnyThread(this.runDirlistTacker,
                this.ruleForDirListWorkers.getNameDirlistTacker());
        
        
        this.runDirListPacker = new Thread(
                this.ruleForDirListWorkers.getThreadGroupWorkerDirList(),
                new AppThWorkDirListPack(this.ruleForDirListWorkers),
                this.ruleForDirListWorkers.getNameDirListPacker());
        this.currentListOfObject.addAnyThread(this.runDirListPacker,
                this.ruleForDirListWorkers.getNameDirListPacker());
        
        
        this.runDirListWriter = new Thread(
                this.ruleForDirListWorkers.getThreadGroupWorkerDirList(),
                new AppThWorkDirListWrite(this.ruleForDirListWorkers),
                this.ruleForDirListWorkers.getNameDirListWriter());
        this.currentListOfObject.addAnyThread(this.runDirListWriter,
                this.ruleForDirListWorkers.getNameDirListWriter());
        
        
        //this.currentListOfObject.putLogMessageInfo("Create objects for WorkDirList");
        //this.currentListOfObject.doLogger();
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
        this.ruleForDirListWorkers.setDirListPacker(this.runDirListPacker);
        this.ruleForDirListWorkers.setDirListWriter(this.runDirListWriter);
        
        //this.startDirlistReader();
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
    protected void joinDirlistReader(){
        try{
            this.runDirlistReader.join();
        } catch(InterruptedException ex){
            ex.printStackTrace();
        }
    }
    protected void joinDirlistTacker(){
        try{
            this.runDirlistTacker.join();
        } catch(InterruptedException ex){
            ex.printStackTrace();
        }
    }
    protected void joinDirlistPacker(){
        try{
            this.runDirListPacker.join();
        } catch(InterruptedException ex){
            ex.printStackTrace();
        }
    }
    protected void joinDirlistWriter(){
        try{
            this.runDirListWriter.join();
        } catch(InterruptedException ex){
            ex.printStackTrace();
        }
    }
    protected AppObjectsList getListOfObjectAndLogger(){
        return this.currentListOfObject;
    }
    
    protected ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>> getPipeReaderToTacker(){
        return this.pipeFromRunnerToTacker;
    }
    protected ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>> getPipeTackerToPacker(){
        return this.pipeFromTackerToPacker;
    }
    protected ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>> getPipePackerToWriter(){
        return this.pipeFromPackerToWriter;
    }
}
