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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.spi.FileSystemProvider;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcThMifWriterDirList implements Runnable {
    
    private long sleepTimeDownRecordSpeed;
    private BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> listPackInner;

    public NcThMifWriterDirList(
            BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> listPackOuter) {
        this.sleepTimeDownRecordSpeed = 100L;
        this.listPackInner = listPackOuter;
    }
    
    
    @Override
    public void run() {
        
        Path pathIndexFile = NcFsIdxStorageInit.buildPathToFileOfIdxStorage();
        Map<String, String> fsProperties = NcFsIdxStorageInit.getFsPropExist();
        System.out.println("\n\n\n file storage path: " + pathIndexFile.toString());
        Boolean existFSfile = NcFsIdxOperationFiles.existAndHasAccessRWNotLink(pathIndexFile);
        System.out.println("NcFsIdxOperationFiles.existAndHasAccessRWNotLink(): " + existFSfile.toString());
        if( !existFSfile ){
            fsProperties = NcFsIdxStorageInit.getFsPropCreate();
        }
        for (Map.Entry<String, String> entry : fsProperties.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Val: " + entry.getValue());
        }
        URI uriZipIndexStorage = URI.create("jar:file:" + pathIndexFile.toUri().getPath());
        try(FileSystem fsZipIndexStorage = 
            FileSystems.newFileSystem(uriZipIndexStorage, fsProperties)){
            
            NcParamFs dataStorage = NcFsIdxStorageInit.initStorageStructure(fsZipIndexStorage);
            FileSystemProvider provider = fsZipIndexStorage.provider();
            int dataWaitCount = 0;
            do{
                Boolean ifDataBegin = Boolean.FALSE;
                do {                
                    if( !this.listPackInner.isEmpty() ){
                        ifDataBegin = Boolean.TRUE;
                        dataWaitCount = 0;
                    }
                } while ( !ifDataBegin );
                do {  
                    ConcurrentSkipListMap<UUID, NcDataListAttr> nowPack = new ConcurrentSkipListMap<>();
                    Path dirDirList = dataStorage.getDirDirList();
                    long coutFiles = 0;
                    Path lastInList = dirDirList;
                    for (Iterator<Path> iteratorDir = dirDirList.iterator(); iteratorDir.hasNext();) {
                        lastInList = iteratorDir.next();
                        coutFiles++;
                    }
                    String strIndex = "";
                    if(coutFiles > 1){
                        strIndex = NcStrFileDir.PRE_DIR_LIST.getStr() + Long.toString(coutFiles * 100L + 100L);
                    }
                    else{
                        strIndex = NcStrFileDir.PRE_DIR_LIST.getStr() + Long.toString(100L);
                    }

                    Path getNew = fsZipIndexStorage.getPath(dirDirList.toString(), strIndex).normalize();
                    try{ 
                        nowPack = this.listPackInner.take();
                    } catch (InterruptedException ex) {
                        NcAppHelper.logException(NcThMifWriterDirList.class.getCanonicalName(), ex);
                    }
                        if(nowPack.size() == 100){
                            try(ObjectOutputStream oos = 
                            new ObjectOutputStream(
                                    Files.newOutputStream(getNew, 
                                            StandardOpenOption.CREATE, 
                                            StandardOpenOption.WRITE)
                            ))
                            {
                                oos.writeObject(nowPack);
                                System.out.println(getNew.toString() + " -|-|- " + nowPack.size() + " elements writed");
                            }
                            catch(Exception ex){
                                NcAppHelper.logException(
                                        NcThMifWriterDirList.class.getCanonicalName(), ex);
                            }
                        }
                        nowPack = new ConcurrentSkipListMap<>();
                } while ( this.listPackInner.size() != 0 );
                dataWaitCount++;
            } while ( dataWaitCount < 50 );
        } catch (IOException ex) {
            NcAppHelper.logException(NcThMifWriterDirList.class.getCanonicalName(), ex);
            String strMsg = "Imposible to create file for index Storage, see log";
            NcAppHelper.outMessage(
                NcStrLogMsgField.ERROR_CRITICAL.getStr()
                + strMsg
            );
        }
    }
    
    protected static int writeDirListData(ConcurrentSkipListMap<UUID, NcDataListAttr> dataToFile, Path dirDirList){
        if( dataToFile == null ){
            return -1;
        }
        try(ObjectOutputStream oos = 
                new ObjectOutputStream(
                new FileOutputStream(dirDirList.toFile())))
        {
            oos.writeObject(dataToFile);
        }
        catch(Exception ex){
            NcAppHelper.logException(
                    NcThMifWriterDirList.class.getCanonicalName(), ex);
            return -1;
        } 
        return dataToFile.size();
    }
    
}
