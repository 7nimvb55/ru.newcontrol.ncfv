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
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcThExRouter <V>
        implements Callable<V> {

    
    private NcThExRouter router;
    private NcThExDirTreeWalk dirWalker;
    private NcThExListAttrScanDir dirListScanner;
    private NcThExListPack resultPacker;
    
    public NcThExRouter() {
        this.router = null;
        this.dirWalker = null;
        this.dirListScanner = null;
        this.resultPacker = null;
    }
    
    @Override
    public V call() throws Exception {
        return null;
    }
    protected boolean startDirWalk(Path pathForScan){
        try {
            this.dirWalker = new NcThExDirTreeWalk(pathForScan);
        } catch (IOException ex) {
            NcAppHelper.logException(NcThExListAttrScanDir.class.getCanonicalName(), ex);
            return false;
        }
        return true;
    }
    protected boolean startScanDirectory(Path pathForScan){
        try {
            this.dirListScanner = new NcThExListAttrScanDir(pathForScan);
        } catch (IOException ex) {
            NcAppHelper.logException(NcThExListAttrScanDir.class.getCanonicalName(), ex);
            return false;
        }
        return true;
    }
    protected boolean startListPack(
            ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>> scanResult,
            NcSwGUIComponentStatus lComp){
        if( scanResult.size() < 101 ){
            return false;
        }
        this.resultPacker = new NcThExListPack(scanResult, lComp);
        return true;
    }
}
