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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcThMifTakeDirList implements Runnable {
    BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> fromPipeDirWalker;
    BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> toPackDirList;

    public NcThMifTakeDirList(
            BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> fromPipeDirWalkerOuter,
            BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> toPackDirListOuter) {
        this.fromPipeDirWalker = fromPipeDirWalkerOuter;
        this.toPackDirList = toPackDirListOuter;
        System.out.println("NcThMifTakeDirList.constructor");
    }
    
    
    
    @Override
    public void run() {
        int emptyCount = 0;
        int size = 0;
        boolean hasData = Boolean.FALSE;
        try {
            do {
                boolean notExitFromReadData = Boolean.TRUE;
                do {
                    size = this.fromPipeDirWalker.size();
                    if( (size > 0) ){
                        hasData = Boolean.TRUE;
                        emptyCount = 0;
                        ConcurrentSkipListMap<UUID, NcDataListAttr> take = this.fromPipeDirWalker.take();
                        this.toPackDirList.put(take.clone());
                        take = new ConcurrentSkipListMap<UUID, NcDataListAttr>();
                        System.out.println("NcThMifTakeDirList.run() toPackDirList.size() = " + this.toPackDirList.size());
                    }
                    if( hasData ){
                       if( size == 0 ){
                            notExitFromReadData = Boolean.FALSE;
                        } 
                    }
                } while ( notExitFromReadData );
                emptyCount++;
            } while ( emptyCount < 50 );
        } catch (InterruptedException ex) {
            NcAppHelper.logException(NcThMifTakeDirList.class.getCanonicalName(), ex);
        }
    }
    
}
