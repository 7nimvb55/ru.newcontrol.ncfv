/*
 * Copyright 2019 wladimirowichbiaran.
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

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThDirListStateJobReader {
    private final UUID randomUUID;
    private final long creationNanoTime;
    
    private Boolean readerBlankJob;
    private Boolean jobReaderIsDone;
    private Boolean jobReaderDataSend;
    private Boolean jobReaderDataIndexedByWord;
    private URI readFromFs;
    
    private Path forReadPath;
    private int readedSize;
    private Boolean isReadedDataEmpty;
    
    private ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> readedData;
    
    protected ThDirListStateJobReader(){
        this.randomUUID = UUID.randomUUID();
        this.creationNanoTime = System.nanoTime();
        setTrueBlankObject();
        setTrueReaderJobDone();
        
        setFalseDataSend();
        setFalseDataIndexedByWord();
        
        this.readedData = new ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>();
    }
    protected ThDirListStateJobReader(Path pathForJob, URI fromFs){
        this.forReadPath = pathForJob;
        this.readFromFs = fromFs;
        this.randomUUID = UUID.randomUUID();
        this.creationNanoTime = System.nanoTime();
        setFalseBlankObject();
        setFalseReaderJobDone();
        
        setFalseDataSend();
        setFalseDataIndexedByWord();
        
        this.readedData = new ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>();
    }
    /**
     * 
     */
    protected void setTrueBlankObject(){
        this.readerBlankJob = Boolean.TRUE;
    }
    protected void setFalseBlankObject(){
        this.readerBlankJob = Boolean.FALSE;
    }
    protected Boolean isBlankObject(){
        if( this.readerBlankJob ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * 
     */
    protected void setTrueDataSend(){
        this.jobReaderDataSend = Boolean.TRUE;
    }
    protected void setFalseDataSend(){
        this.jobReaderDataSend = Boolean.FALSE;
    }
    protected Boolean isDataSend(){
        if( this.jobReaderDataSend ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * 
     */
    protected void setTrueDataIndexedByWord(){
        this.jobReaderDataIndexedByWord = Boolean.TRUE;
    }
    protected void setFalseDataIndexedByWord(){
        this.jobReaderDataIndexedByWord = Boolean.FALSE;
    }
    protected Boolean isDataIndexedByWord(){
        if( this.jobReaderDataIndexedByWord ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * 
     * @return 
     */
    protected UUID getID(){
        return this.randomUUID;
    }
    protected long getCreationTime(){
        return this.creationNanoTime;
    }
    protected void setTrueReaderJobDone(){
        this.jobReaderIsDone = Boolean.TRUE;
    }
    protected void setFalseReaderJobDone(){
        this.jobReaderIsDone = Boolean.FALSE;
    }
    protected Boolean isReaderJobDone(){
        if( this.jobReaderIsDone ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void putReadedData(final ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> inputedData){
        this.readedSize = (int) inputedData.size();
        this.readedData.putAll(inputedData);
    }
    protected ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> getReadedData(){
        return this.readedData;
    }
    protected Boolean isReadedDataEmpty(){
        if( this.readedData.isEmpty() ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void cleanReadedData(){
        this.readedData = new ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>();
    }
    protected Integer getReadedDataSize(){
        return (int) this.readedSize;
    }
    protected URI getReadedFileSystem(){
        return this.readFromFs;
    }
    protected Path getReadedPath(){
        return this.forReadPath;
    }
}
