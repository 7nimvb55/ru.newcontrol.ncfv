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

import java.util.concurrent.ConcurrentHashMap;

/**
 * How work
 * @author wladimirowichbiaran
 */
public class ThWordState {
    /**
     * create, get, set bus from 
     *  - ThWordLogicFilter to 
     *  - - - busJobStorageWalker ConcurrentHashMap<Integer, Integer>(jobTypes, typeWordStorage)
     *  - - - 
     *  - - - 
     * ThWordLogicStorageRouter
     * ThWordLogicStorageWalker
     * ThWordLogicStorageReader
     * ThWordLogicStorageWriter
     * 
     * private ThWordBusReaded busReadedJob;
     * private Boolean isSetReadedJob;
     */
    /**
     * @todo add bus for long word
     * add new jobwrited class for long word and some index
     */
    private ThWordBusWrited busWordWritedJob;
    private Boolean isSetWordWritedJob;
    
    private ThWordBusWrited busLongWordWritedJob;
    private Boolean isSetLongWordWritedJob;
    
    /*private ThWordBusReaded busSendToIndexWord;
    private Boolean isSetSendToIndexWord;*/
    
    ThWordState(){
        //setFalseReadedJob();
        setFalseWordWritedJob();
        setFalseLongWordWritedJob();
    }
    /**
     * 
     * @return
     * @throws #java.lang.IllegalArgumentException
     */
    /*protected ThWordBusReaded getBusJobForRead(){
        if( !this.isReadedJob() ){
            throw new IllegalArgumentException("Bus jobs for read not set in " + ThWordState.class.getCanonicalName());
        }
        return this.busReadedJob;
    }
    protected void setBusJobForRead(final ThWordBusReaded busReadOuter){
        this.busReadedJob = busReadOuter;
        setTrueReadedJob();
    }
    protected void setTrueReadedJob(){
        this.isSetReadedJob = Boolean.TRUE;
    }
    protected void setFalseReadedJob(){
        this.isSetReadedJob = Boolean.FALSE;
    }
    protected Boolean isReadedJob(){
        if( this.isSetReadedJob ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }*/
    /**
     * 
     * @return 
     * @throws #java.lang.IllegalArgumentException
     */
    protected ThWordBusWrited getBusJobForWordWrite(){
        if( !this.isWordWritedJob() ){
            throw new IllegalArgumentException("Bus jobs for write not set in " + ThWordState.class.getCanonicalName());
        }
        return this.busWordWritedJob;
    }
    protected void setBusJobForWordWrite(final ThWordBusWrited busWordWriteOuter){
        this.busWordWritedJob = busWordWriteOuter;
        setTrueWordWritedJob();
    }
    protected void setTrueWordWritedJob(){
        this.isSetWordWritedJob = Boolean.TRUE;
    }
    protected void setFalseWordWritedJob(){
        this.isSetWordWritedJob = Boolean.FALSE;
    }
    protected Boolean isWordWritedJob(){
        if( this.isSetWordWritedJob ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * 
     * @return 
     * @throws #java.lang.IllegalArgumentException
     */
    protected ThWordBusWrited getBusJobForLongWordWrite(){
        if( !this.isLongWordWritedJob() ){
            throw new IllegalArgumentException("Bus jobs for write not set in " + ThWordState.class.getCanonicalName());
        }
        return this.busLongWordWritedJob;
    }
    protected void setBusJobForLongWordWrite(final ThWordBusWrited busLongWordWriteOuter){
        this.busLongWordWritedJob = busLongWordWriteOuter;
        setTrueLongWordWritedJob();
    }
    protected void setTrueLongWordWritedJob(){
        this.isSetLongWordWritedJob = Boolean.TRUE;
    }
    protected void setFalseLongWordWritedJob(){
        this.isSetLongWordWritedJob = Boolean.FALSE;
    }
    protected Boolean isLongWordWritedJob(){
        if( this.isSetLongWordWritedJob ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * 
     * @return
     * @throws #java.lang.IllegalArgumentException
     */
    /*protected ThWordBusReaded getBusJobForSendToIndexWord(){
        if( !this.isSendToIndexWord() ){
            throw new IllegalArgumentException("Bus jobs for read not set in " + ThWordState.class.getCanonicalName());
        }
        return this.busSendToIndexWord;
    }
    protected void setBusJobForSendToIndexWord(final ThWordBusReaded busReadOuter){
        this.busSendToIndexWord = busReadOuter;
        setTrueReadedJob();
    }
    protected void setTrueSendToIndexWord(){
        this.isSetSendToIndexWord = Boolean.TRUE;
    }
    protected void setFalseSendToIndexWord(){
        this.isSetSendToIndexWord = Boolean.FALSE;
    }
    protected Boolean isSendToIndexWord(){
        if( this.isSetSendToIndexWord ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }*/
    /**
     * Read from JobBusForStorageWalker data, writed by
     * ThWordLogicFilter
     * ThWordLogicStorageRouter
     * Bus commands system:
     *  - jobWalkerStorageType - job types:
     *  - - - scanNotLimited, typeWordStorage
     *  - - - scanLimited, typeWordStorage
     *  - - - scanAllFiles, typeWordStorage
     *  + + + not released in this bus version moveFilesDirectories, typeWordStorage
     *  - - - createDirectoryTypeWord, typeWordStorage
     * @return 
     */
    protected ConcurrentHashMap<Integer, Integer> getJobStorageWalker(){
        ConcurrentHashMap<Integer, Integer> createdJobForReturn;
        try{
            createdJobForReturn = new ConcurrentHashMap<Integer, Integer>();
            return createdJobForReturn;
        }finally{
            createdJobForReturn = null;
        }
        
    }
        
    
}
