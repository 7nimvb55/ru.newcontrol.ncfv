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
import java.nio.file.DirectoryStream;
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcThMifWriterDirList extends Thread {
    private String typeObject;
    private long sleepTimeDownRecordSpeed;
    private ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> listPackInner;
    private ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> toPackDirList;
    private NcThExStatus outerJobStatus;

    public NcThMifWriterDirList(
            ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> listPackOuter,
            ArrayBlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> toPackDirListOuter,
            NcThExStatus outerJobStatus) {
        this.sleepTimeDownRecordSpeed = 100L;
        this.listPackInner = listPackOuter;
        this.toPackDirList = toPackDirListOuter;
        this.typeObject = "[MIFWRITERDIRLIST]" + this.toString();
        NcAppHelper.outCreateObjectMessage(this.typeObject, this.getClass());
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
        /*for (Map.Entry<String, String> entry : fsProperties.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Val: " + entry.getValue());
        }*/
        
        Boolean ifException = Boolean.FALSE;
        
        URI uriZipIndexStorage = URI.create("jar:file:" + pathIndexFile.toUri().getPath());
        try(FileSystem fsZipIndexStorage = 
            FileSystems.newFileSystem(uriZipIndexStorage, fsProperties)){
            
            NcParamFs dataStorage = NcFsIdxStorageInit.initStorageStructure(fsZipIndexStorage);
            do{
            int dataWaitCount = 0;
            
            do{
                /*Boolean ifDataBegin = Boolean.FALSE;
                do {                
                    if( !this.listPackInner.isEmpty() ){
                        ifDataBegin = Boolean.TRUE;
                        dataWaitCount = 0;
                    }
                } while ( !ifDataBegin );*/
                try{
                    Boolean boolMustBegin = Boolean.FALSE;
                    do {  
                        ConcurrentSkipListMap<UUID, NcDataListAttr> nowPack = new ConcurrentSkipListMap<>();
                        Path dirDirList = dataStorage.getDirDirList();
                        Path pathForViewCount = fsZipIndexStorage.getPath(dirDirList.toString()).normalize();
                        Path getNew = pathForViewCount;
                        Boolean existFile = Boolean.FALSE;
                        long countFiles = 0;
                        String strIndex = "";
                        do{
                            
                            if(countFiles > 0){
                                strIndex = NcStrFileDir.PRE_DIR_LIST.getStr() + Long.toString(countFiles * 100L + 100L);
                            }
                            else{
                                strIndex = NcStrFileDir.PRE_DIR_LIST.getStr() + Long.toString(100L);
                            }
                            getNew = fsZipIndexStorage.getPath(dirDirList.toString(), strIndex).normalize();
                            existFile = Files.exists(getNew, LinkOption.NOFOLLOW_LINKS);
                            if(existFile){
                                countFiles++;
                            }
                           
                        }while( existFile );
                        
                        try{

                            nowPack = this.listPackInner.poll(13, TimeUnit.MICROSECONDS);

                            if ( nowPack == null ){
                                String strMsgError = 
                                    "[Writer] From [Taker] get null, need packet size of 100 element";
                                throw new IllegalArgumentException(strMsgError);
                            }

                            /*System.out.println("[Writer]From [Packer] get packet with hashCode "
                            + nowPack.hashCode()
                            + " toString val is " + nowPack.toString()
                            + " size of " + nowPack.size() 
                            + " in packet records, ready path for write is "
                            + getNew.toString());*/
                        } catch (InterruptedException ex) {
                            NcAppHelper.logException(NcThMifWriterDirList.class.getCanonicalName(), ex);
                        }
                            if(nowPack.size() == 100){
                                dataWaitCount = 0;
                                try(ObjectOutputStream oos = 
                                new ObjectOutputStream(
                                        Files.newOutputStream(getNew, 
                                                StandardOpenOption.CREATE, 
                                                StandardOpenOption.WRITE)
                                ))
                                {
                                    oos.writeObject(nowPack);
                                    //System.out.println(getNew.toString() + " -|-|- " + nowPack.size() + " elements writed");
                                }
                                catch(Exception ex){
                                    NcAppHelper.logException(
                                            NcThMifWriterDirList.class.getCanonicalName(), ex);
                                }
                            }
                            nowPack = new ConcurrentSkipListMap<>();
                            if( this.toPackDirList.size() != 0 ){
                                boolMustBegin = Boolean.TRUE;
                            }
                            if( this.listPackInner.size() != 0 ){
                                boolMustBegin = Boolean.TRUE;
                            }
                            
                            if( (this.toPackDirList.size() == 0)
                                && (this.listPackInner.size() == 0) ){
                                boolMustBegin = Boolean.FALSE;
                            }
                            
                    } while ( boolMustBegin );
                } catch (IllegalArgumentException ex){
                    NcAppHelper.logException(NcThMifWriterDirList.class.getCanonicalName(), ex);    
                }
                dataWaitCount++;
            } while ( dataWaitCount < 50 );
            System.out.println("[Writer]statusPacker-" + this.outerJobStatus.getPackerStatus().toString()
                        + "");
            } while ( (outerJobStatus.getPackerStatus() == Thread.State.RUNNABLE)
                    || (outerJobStatus.getPackerStatus() == Thread.State.TIMED_WAITING) );
        } catch (IOException ex) {
            ex.printStackTrace();
            NcAppHelper.logException(NcThMifWriterDirList.class.getCanonicalName(), ex);
            String strMsg = "Imposible to create file for index Storage, see log";
            NcAppHelper.outMessage(
                NcStrLogMsgField.ERROR_CRITICAL.getStr()
                + strMsg
            );
            ifException = Boolean.TRUE;
        } catch (Exception ex){
            ex.printStackTrace();
            NcAppHelper.logException(NcThMifWriterDirList.class.getCanonicalName(), ex);
            String strMsg = "Imposible for exec operation in the index Storage, see log"
                    + NcStrLogMsgField.EXCEPTION_MSG.getStr() + ex.getMessage();
            NcAppHelper.outMessage(
                NcStrLogMsgField.ERROR_CRITICAL.getStr()
                + strMsg
            );
            ifException = Boolean.TRUE;
        }
        if( ifException ){
            System.out.println("[WRITER][ERRROR][FINISH][EXIT]");
        }
        System.out.println("[WRITER][FINISH][EXIT]");
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
