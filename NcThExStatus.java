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
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcThExStatus {
    private ReentrantLock lockFromPipeDirWalker;
    private ReentrantLock lockPackPipeDirWalker;
    private boolean fairQueue;
    private int lengthQueue;
    private Path dirForScan;
    private BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> pipeDirWalker;
    private BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> fromPipeDirWalker;
    //private ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>> packPipeDirWalker;
    private BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> packDirList;
    
    
    

    public NcThExStatus(Path inputDirForScan) throws IOException {
        this.lockFromPipeDirWalker = new ReentrantLock(Boolean.TRUE);
        this.lockPackPipeDirWalker = new ReentrantLock(Boolean.TRUE);
        this.fairQueue = Boolean.TRUE;
        this.lengthQueue = 10000;
        //dirForScan = new ThreadLocal<>();
        //pipeDirWalker = new ThreadLocal<>();
        //fromPipeDirWalker = new ThreadLocal<>();
        
        BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> tArr;
        tArr = new ArrayBlockingQueue<>(lengthQueue, fairQueue);
        
        this.pipeDirWalker = tArr;
        
        BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> fromPipeList;
        fromPipeList = new ArrayBlockingQueue<>(lengthQueue, fairQueue);
        this.fromPipeDirWalker = fromPipeList;
        
        //ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>> forPackPipeList;
        //forPackPipeList = new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>>();
        //this.packPipeDirWalker = forPackPipeList;
        
        BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> pArr;
        pArr = new ArrayBlockingQueue<>(lengthQueue, fairQueue);
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
    
    
    protected BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> getPipeDirList(){
        return this.pipeDirWalker;
    }
    
    protected BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> getFromPipeDirWalker(){
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
    protected BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> getPackDirList(){
        return this.packDirList;
    }
    
}
