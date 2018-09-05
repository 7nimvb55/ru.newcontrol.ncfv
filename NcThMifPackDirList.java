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

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcThMifPackDirList extends Thread {
    private String typeObject;
    private long sleepTimeDownRecordSpeed;
    private ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> pipeDirListInner;
    private ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> readyPack;
    private NcThExStatus jobStatus;
    
    public NcThMifPackDirList(
            ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> pipeDirListOuter,
            ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> listPackOuter,
            NcThExStatus outerJobStatus
            ) {
        this.pipeDirListInner = pipeDirListOuter;
        this.readyPack = listPackOuter;
        this.sleepTimeDownRecordSpeed = 100L;
        this.typeObject = "[MIFPACKDIRLIST]" + this.toString();
        this.jobStatus = outerJobStatus;
        NcAppHelper.outCreateObjectMessage(this.typeObject, this.getClass());
    }
    
    @Override
    public void run() {
        try {
            
            ConcurrentSkipListMap<UUID, NcDataListAttr> dataPack =
                                    new ConcurrentSkipListMap<UUID, NcDataListAttr>();
            do{
                int dataWaitCount = 0;
            do{
                try{
                    
                    do{

                        ConcurrentSkipListMap<UUID, NcDataListAttr> take;
                        take = null;
                        int emptyCountWaiter = 0;
                        do{
                            take = this.pipeDirListInner.poll(3, TimeUnit.NANOSECONDS);
                            if ( take == null ){
                                emptyCountWaiter++;
                            }
                            if ( emptyCountWaiter > 21 ){
                                String strMsgError = 
                                    "[Packer] Time out for wait data from [Taker] is over";
                                throw new IllegalArgumentException(strMsgError);
                            }
                        }while( take == null );


                        dataWaitCount = 0;
                        for (Map.Entry<UUID, NcDataListAttr> entry : take.entrySet()) {
                            UUID key = entry.getKey();
                            NcDataListAttr value = entry.getValue();
                            int nowSize = 1;
                            int currentPackSize = dataPack.size();
                            if( currentPackSize == 100 ){
                                this.readyPack.put(dataPack);
                                dataPack = new ConcurrentSkipListMap<UUID, NcDataListAttr>();
                                currentPackSize = dataPack.size();
                                continue;
                            }
                            if( (nowSize + currentPackSize)  < 101 ){
                                dataPack.put(key, value);
                                currentPackSize = dataPack.size();
                                continue;
                            }
                        }
                        /*System.out.println("[Pack]readyPack-" + this.readyPack.size()
                        + "-pipeDirList-" + this.pipeDirListInner.size());*/
                    }while( this.pipeDirListInner.size() != 0 );
                } catch (IllegalArgumentException ex) {
                        NcAppHelper.logException(NcThMifPackDirList.class.getCanonicalName(), ex);
                }
                dataWaitCount++;
            }while( dataWaitCount < 50);
            }while( this.jobStatus.getTackerStatus() == Thread.State.RUNNABLE );
            
        
        } catch (Exception ex) {
            NcAppHelper.logException(NcThMifPackDirList.class.getCanonicalName(), ex);
        }
        System.out.println("[PACKER][FINISH][EXIT]");
    }
    
}
