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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcThExListPack<V>
        implements Callable<ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>>> {
    private final Semaphore avalableThToScan = new Semaphore(1);
    private final transient ReentrantLock lock = new ReentrantLock();
    private final NcSwGUIComponentStatus lComp;
    private final ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>> listPack;
    private final ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>> pipeDirList;
    
    public NcThExListPack(
            ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>> pipeDirList,
            NcSwGUIComponentStatus lComp) {
        this.lComp = lComp;
        this.listPack = new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>>();
        this.pipeDirList = pipeDirList;
    }
    
    @Override
    public ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>> call() throws Exception {
        try {
                    
            avalableThToScan.acquire();
            ArrayList<String> listStrArr = new ArrayList<String>();
                    //final ReentrantLock lock = this.lock;
            do{
                listStrArr.clear();
                ConcurrentSkipListMap<UUID, NcDataListAttr> dataPack =
                            new ConcurrentSkipListMap<UUID, NcDataListAttr>();


                NavigableSet<UUID> keySet = listPack.keySet();
                for (Iterator<UUID> iterator = keySet.iterator(); iterator.hasNext();) {

                    final ReentrantLock lock = this.lock;
                    lock.lock();
                    try {

                        UUID nextKey = iterator.next();
                        ConcurrentSkipListMap<UUID, NcDataListAttr> getPacket = listPack.get(nextKey);
                        if( getPacket == null ){
                            continue;
                        } else {
                            int packSize = getPacket.size();
                            listStrArr.add("[packetCreator][run][listPack.("
                                    + nextKey + ").size]"
                                    + packSize);
                            if( packSize != 100 ){
                                dataPack = (ConcurrentSkipListMap<UUID, NcDataListAttr>) listPack.remove(nextKey);

                                if( dataPack == null ){
                                    continue;
                                }
                            }
                        }
                    } finally {
                            lock.unlock();
                    }
                }
                if( dataPack == null ){
                    continue;
                }        

            listStrArr.add("[packetCreator][run][initPacket][dataPack.size]"
                + dataPack.size());

            listStrArr.add("[packetCreator][run][pipeDirList.size]"
                + pipeDirList.size()
                + "[packetCreator][run][startIteration]"
                + "[dataPack.size]" + dataPack.size()
                + "[listPack.size]" + listPack.size());
            NavigableSet<UUID> pipekeySet = pipeDirList.keySet();

            for (Iterator<UUID> iterator = pipekeySet.iterator(); iterator.hasNext();) {
                UUID next = iterator.next();

                //for publish and save to index code here
                /*lock.lock();
                try {*/
                ConcurrentSkipListMap<UUID, NcDataListAttr> nowPack = pipeDirList.remove(next);
                int nowSize = nowPack.size();

                int currentPack = dataPack.size();
                listStrArr.add("[packetCreator][run][pipeDirList.remove][nowPack][size]"
                + nowSize
                + "[dataPack.size]" + currentPack
                + "[listPack.size]" + listPack.size());
                if( currentPack == 100 ){
                    listPack.put(UUID.randomUUID(), dataPack);
                    dataPack = new ConcurrentSkipListMap<UUID, NcDataListAttr>();
                    listStrArr.add("[packetCreator][run][initPacket][dataPack.size]"
                        + dataPack.size());
                }
                /*} finally {
                    lock.unlock();
                }*/
                currentPack = dataPack.size();
                if( (nowSize + currentPack)  < 101 ){
                    dataPack.putAll(nowPack);
                    currentPack = dataPack.size();
                    listStrArr.add("[packetCreator][run][dataPack.putAll][nowPack][size]"
                        + nowSize
                        + "[dataPack.size]" + currentPack
                        + "[listPack.size]" + listPack.size());
                    continue;
                }
                if( (nowSize + currentPack) > 100){
                    for (Map.Entry<UUID, NcDataListAttr> entry : nowPack.entrySet()) {
                        UUID key = entry.getKey();
                        NcDataListAttr value = entry.getValue();
                        currentPack = dataPack.size();
                        if( currentPack == 100 ){
                            listPack.put(UUID.randomUUID(), dataPack);
                            dataPack = new ConcurrentSkipListMap<UUID, NcDataListAttr>();
                            listStrArr.add("[packetCreator][run][initPacket][dataPack.size]"
                                + dataPack.size());
                        }
                        dataPack.put(key, value);
                    }
                }

            }
            listPack.put(UUID.randomUUID(), dataPack);
            dataPack = new ConcurrentSkipListMap<UUID, NcDataListAttr>();
            listStrArr.add("[packetCreator][run][pipeDirList.size]"
                + pipeDirList.size()
                + "[packetCreator][run][endIteration]"
                + "[dataPack.size]" + dataPack.size()
                + "[listPack.size]" + listPack.size());

            }while( pipeDirList.size() != 0 );
            listStrArr.add("[packetCreator][run][finishStady][listPack.size]"
                + listPack.size());
            for (Map.Entry<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>> entryItem : listPack.entrySet()) {
                UUID key = entryItem.getKey();
                ConcurrentSkipListMap<UUID, NcDataListAttr> value = entryItem.getValue();
                listStrArr.add("[packetCreator][run][report][listPack(" + key + ").size]"
                + value.size());
            }
            NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, listStrArr);
            avalableThToScan.release();
        } catch (InterruptedException ex) {
            NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
        }
        return listPack;
    }
    
}
