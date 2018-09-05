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

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcThMifTakeDirList extends Thread {
    private String typeObject;
    private ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> fromPipeDirWalker;
    private ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> toPackDirList;
    private NcThExStatus jobStatus;

    public NcThMifTakeDirList(
            ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> fromPipeDirWalkerOuter,
            ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> toPackDirListOuter,
            NcThExStatus outerJobStatus) {
        this.fromPipeDirWalker = fromPipeDirWalkerOuter;
        this.toPackDirList = toPackDirListOuter;
        this.typeObject = "[MIFTAKEDIRLIST]" + this.toString();
        this.jobStatus = outerJobStatus;
        NcAppHelper.outCreateObjectMessage(this.typeObject, this.getClass());
    }
    
    
    
    @Override
    public void run() {
        
        int size = 0;
        boolean hasData = Boolean.FALSE;
        try {
            do{
                int emptyCount = 0;
            do {
                do {
                    try{
                        if(this.toPackDirList.size() != 0){
                            emptyCount = 0;
                        }
                        ConcurrentSkipListMap<UUID, NcDataListAttr> take;
                        take = null;
                        int emptyCountWaiter = 0;
                        do{
                            take = this.fromPipeDirWalker.poll(15, TimeUnit.NANOSECONDS);
                            if ( take == null ){
                                emptyCountWaiter++;
                            }
                            if ( emptyCountWaiter > 7 ){
                                String strMsgError = 
                                    "[Taker] Time out for wait data from [Runner] is over";
                                throw new IllegalArgumentException(strMsgError);
                            }
                        }while( take == null );
                        this.toPackDirList.put(take);
                        /*System.out.println("[Take]fromPipeDirWalker-"
                                + take.size()
                                + "-toPackDirList-"
                                + this.toPackDirList.size());*/
                    } catch (IllegalArgumentException ex) {
                        NcAppHelper.logException(NcThMifTakeDirList.class.getCanonicalName(), ex);
                    } catch (NullPointerException ex) {
                        NcAppHelper.logException(NcThMifTakeDirList.class.getCanonicalName(), ex);
                    } 
                } while ( this.toPackDirList.size() != 0 );
                emptyCount++;
            } while ( emptyCount < 50 );
        }while ( this.jobStatus.getRunnerStatus() == Thread.State.RUNNABLE );
        } catch (InterruptedException ex) {
            NcAppHelper.logException(NcThMifTakeDirList.class.getCanonicalName(), ex);
        }
        System.out.println("[TACKER][FINISH][EXIT]");
    }
    
}
