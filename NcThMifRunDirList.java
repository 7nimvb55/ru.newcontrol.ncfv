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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcThMifRunDirList implements Runnable {
    private NcFsIdxFileVisitor fv;
    private Path ps;
    private BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> pd;
    
    
    public NcThMifRunDirList(
            BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> pipeDirListOuter,
            Path pathToStartOuter) {

        System.out.println("NcThMifRunDirList.constructor init ThreadLocal");
        

        pd = pipeDirListOuter;

        NcFsIdxFileVisitor ncFsIdxFileVisitor = new NcFsIdxFileVisitor(pd);
        fv = ncFsIdxFileVisitor;

        Path realPath = Paths.get("/usr/src");
        ps = realPath;

    }

    
    @Override
    public void run() {
        try {
            Files.walkFileTree(ps, fv);
        } catch (IOException ex) {
            NcAppHelper.logException(NcThMifRunDirList.class.getCanonicalName(), ex);
        } catch (IllegalStateException ex) {
            NcAppHelper.logException(NcThMifRunDirList.class.getCanonicalName(), ex);
        }
    }
    
}
