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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcThExStatus {
    private String typeObject;
    private ReentrantLock lockFromPipeDirWalker;
    private ReentrantLock lockPackPipeDirWalker;
    private boolean fairQueue;
    private int lengthQueue;
    private Path dirForScan;
    private ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> pipeDirWalker;
    private ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> fromPipeDirWalker;
    //private ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>> packPipeDirWalker;
    private ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> packDirList;
    private Map<String,String> threadStatus;
    
    private NcThMifRunDirList runDirList;
    private NcThMifTakeDirList takeDirList;
    private NcThMifPackDirList packerDirList;
    
    

    public NcThExStatus(Path inputDirForScan) throws IOException {
        this.typeObject = "[THREADSTATUS]" + this.toString();
        NcAppHelper.outCreateObjectMessage(this.typeObject, this.getClass());
        this.lockFromPipeDirWalker = new ReentrantLock(Boolean.TRUE);
        this.lockPackPipeDirWalker = new ReentrantLock(Boolean.TRUE);
        this.fairQueue = Boolean.TRUE;
        this.lengthQueue = 1000;
        
        //dirForScan = new ThreadLocal<>();
        //pipeDirWalker = new ThreadLocal<>();
        //fromPipeDirWalker = new ThreadLocal<>();
        
        ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> tArr;
        tArr = new ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>>(this.lengthQueue, this.fairQueue);
        
        this.pipeDirWalker = tArr;
        
        ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> fromPipeList;
        fromPipeList = new ArrayBlockingQueue<>(this.lengthQueue, this.fairQueue);
        this.fromPipeDirWalker = fromPipeList;
        
        //ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>> forPackPipeList;
        //forPackPipeList = new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>>();
        //this.packPipeDirWalker = forPackPipeList;
        
        ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> pArr;
        pArr = new ArrayBlockingQueue<>(this.lengthQueue, this.fairQueue);
        this.packDirList = pArr;
        
        if( inputDirForScan != null){
            this.dirForScan = inputDirForScan;
        }
        else{
            this.dirForScan = NcFsDefaults.getHomeOrAppOrRootStorage();
        }
        checkScanPath();
        verifyScanPath();
        
    }
    protected NcThMifExecPool initJobParam(){
        String typeThread = "[EXECPOOL]";
        NcAppHelper.outCreateObjectMessage(typeThread, this.getClass());
        return new NcThMifExecPool();
    }
    private void checkScanPath() throws IOException{
        Path inputedPath = dirForScan;
        if ( inputedPath == null ){
            String strAddMsg = NcStrLogMsgField.MSG_ERROR.getStr()
                    + " wrong path for scan "
                    + NcStrLogMsgField.VALUE.getStr()
                    + inputedPath.toString();
            NcAppHelper.outMessage(strAddMsg);
            throw new IOException(strAddMsg);
        }
    }
    private void verifyScanPath() throws IOException{
        Path pathToStart = Paths.get(dirForScan.toString());
        try{
            pathToStart = NcFsIdxOperationDirs.checkScanPath(pathToStart);
        } catch (IOException ex) {
            NcAppHelper.logException(NcThExStatus.class.getCanonicalName(), ex);
            throw new IOException(ex);
        }
    }
    protected Path getScanPath(){
        return this.dirForScan;
    }
    
    
    protected ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> getPipeDirList(){
        return this.pipeDirWalker;
    }
    
    protected ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> getFromPipeDirWalker(){
        return this.fromPipeDirWalker;
    }
    
    protected ReentrantLock getLockFromPipeDirWalker(){
        return this.lockFromPipeDirWalker;
    }
    
    /*protected ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>> getPackPipeDirWalker(){
        return this.packPipeDirWalker;
    }*/
    protected ReentrantLock getLockPackPipeDirWalker(){
        return this.lockPackPipeDirWalker;
    }
    protected ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> getPackDirList(){
        return this.packDirList;
    }
    
    protected Thread.State getRunnerStatus(){
        Thread.State staterunDirList = this.runDirList.getState();
        if( staterunDirList == null ){
            staterunDirList = this.runDirList.getState();
        }
        return staterunDirList;
    }
    protected void setRunner(NcThMifRunDirList outerRunDirList){
        this.runDirList =  outerRunDirList;
    }
    
    protected Thread.State getTackerStatus(){

        Thread.State statetakeDirList = this.takeDirList.getState();
        if( statetakeDirList == null ){
            statetakeDirList = this.takeDirList.getState();
        }

        return statetakeDirList;
        
    }
    protected void setTacker(NcThMifTakeDirList outerTakeDirList){
        this.takeDirList =  outerTakeDirList;
    }
    
    protected Thread.State getPackerStatus(){
        Thread.State statepackerDirList = this.packerDirList.getState();
        if( statepackerDirList == null ){
            statepackerDirList = this.packerDirList.getState();
        }

        return statepackerDirList;
    }
    protected void setPacker(NcThMifPackDirList outerPackerDirList){
        this.packerDirList =  outerPackerDirList;
    }
}
