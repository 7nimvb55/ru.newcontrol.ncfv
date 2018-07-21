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

import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcThExInfo {
    private ThreadLocal<Long> sleepTime;
    private ThreadLocal<Path> dirForScan;
    private ThreadLocal<BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>>> pipeDirWalker;
    private boolean fairQueue;
    private int lengthQueue;

    public NcThExInfo() {
        this.sleepTime.set(0L);
        this.dirForScan.set(null);
        this.fairQueue = Boolean.TRUE;
        this.lengthQueue = 1000;
        this.pipeDirWalker.set(new ArrayBlockingQueue(this.lengthQueue, this.fairQueue));
    }
    
    protected void setDirForScan(Path inputDirForScan){
        if( inputDirForScan != null){
            this.dirForScan.set(inputDirForScan);
        }
    }
    
    protected ConcurrentSkipListMap<UUID, NcDataListAttr> takeFromPipe() throws InterruptedException{
        try {
            return pipeDirWalker.get().take();
        } catch (InterruptedException ex) {
            NcAppHelper.logException(NcThExInfo.class.getCanonicalName(), ex);

            String strThreadInfo = NcAppHelper.getThreadInfoToString(Thread.currentThread());
            throw new  InterruptedException(
                strThreadInfo
                + NcStrLogMsgField.MSG_INFO.getStr()
                + "Thread interrupted, reason "
                + NcStrLogMsgField.EXCEPTION_MSG.getStr()
                + ex.getMessage());
        }
    }
    
    protected int getCountElement(){
        return pipeDirWalker.get().size();
    }
    
    protected void setPipeDirWalker(BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> inPipe){
        pipeDirWalker.set(inPipe);
    }
    
    protected void putToPipeDirWalker(ConcurrentSkipListMap<UUID, NcDataListAttr> puInPipe){
        pipeDirWalker.get().add(puInPipe);
    }
    
    protected BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> getPipeDirWalker(){
        return pipeDirWalker.get();
    }
    
    protected Path getDirForScan(){
        return this.dirForScan.get();
    }
    
    protected void setSleepTime(Long sleepTimeIn){
        if( sleepTimeIn >= 0 ){
            this.sleepTime.set(sleepTimeIn);
        }
    }
    
    protected long getSleepTime(){
        return this.sleepTime.get();
    }
}
