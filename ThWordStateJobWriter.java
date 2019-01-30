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
public class ThWordStateJobWriter {
    private final UUID randomUUID;
    private final long creationNanoTime;
    
    private Boolean writerBlankJob;
    private Boolean jobWriterIsDone;
    private Boolean isLongWordJob;
    //private URI writeToFsUri;
    private String forWriteFileName;
    private int writerSize;
    private Boolean isWriterDataEmpty;
    
    private ConcurrentSkipListMap<UUID, TdataWord> writerData;
    
    protected ThWordStateJobWriter(){
        this.randomUUID = UUID.randomUUID();
        this.creationNanoTime = System.nanoTime();
        setTrueBlankObject();
        setTrueWriterJobDone();
        setFalseLongWord();
        
        this.writerData = new ConcurrentSkipListMap<UUID, TdataWord>();
    }
    protected ThWordStateJobWriter(String pathForJob){
        this.forWriteFileName = pathForJob;
        //this.writeToFsUri = fromFsUri;
        this.randomUUID = UUID.randomUUID();
        this.creationNanoTime = System.nanoTime();
        setFalseBlankObject();
        setFalseWriterJobDone();
        setFalseLongWord();
        
        this.writerData = new ConcurrentSkipListMap<UUID, TdataWord>();
    }
    protected ThWordStateJobWriter(String pathForJob, Boolean longWord){
        this.forWriteFileName = pathForJob;
        //this.writeToFsUri = fromFsUri;
        this.randomUUID = UUID.randomUUID();
        this.creationNanoTime = System.nanoTime();
        setFalseBlankObject();
        setFalseWriterJobDone();
        setTrueLongWord();
        
        this.writerData = new ConcurrentSkipListMap<UUID, TdataWord>();
    }

    protected void setTrueBlankObject(){
        this.writerBlankJob = Boolean.TRUE;
    }
    protected void setFalseBlankObject(){
        this.writerBlankJob = Boolean.FALSE;
    }
    protected Boolean isBlankObject(){
        if( this.writerBlankJob ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueLongWord(){
        this.isLongWordJob = Boolean.TRUE;
    }
    protected void setFalseLongWord(){
        this.isLongWordJob = Boolean.FALSE;
    }
    protected Boolean isLongWord(){
        if( this.isLongWordJob ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected UUID getID(){
        return this.randomUUID;
    }
    protected long getCreationTime(){
        return this.creationNanoTime;
    }
    protected void setTrueWriterJobDone(){
        this.jobWriterIsDone = Boolean.TRUE;
    }
    protected void setFalseWriterJobDone(){
        this.jobWriterIsDone = Boolean.FALSE;
    }
    protected Boolean isWriterJobDone(){
        if( this.jobWriterIsDone ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void putWritedData(final ConcurrentSkipListMap<UUID, TdataWord> inputedData){
        this.writerSize = (int) inputedData.size();
        this.writerData.putAll(inputedData);
    }
    protected ConcurrentSkipListMap<UUID, TdataWord> getWriterData(){
        return this.writerData;
    }
    protected Boolean isWritedDataEmpty(){
        if( this.writerData.isEmpty() ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void cleanWriterData(){
        this.writerData = new ConcurrentSkipListMap<UUID, TdataWord>();
    }
    protected Integer getWriterDataSize(){
        return (int) this.writerSize;
    }
    /*protected URI getWritedStorageUri(){
        return this.writeToFsUri;
    }*/
    protected String getFileNameForWrite(){
        return this.forWriteFileName;
    }
}
