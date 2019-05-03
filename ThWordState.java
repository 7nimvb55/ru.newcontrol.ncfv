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
     * ThWordBusWriter
     */
    private ThWordBusWriter busWordRouterJobToWriter;
    private Boolean isSetWordRouterJobToWriter;
    /**
     * ThWordBusReader
     */
    private ThWordBusReader busWordRouterJobToReader;
    private Boolean isSetWordRouterJobToReader;
    /**
     * ThWordBusReadedFlow thWordFlowRead
     * @todo Bus into State
     */
    private ThWordBusReadedFlow thWordFlowRead;
    private Boolean isSetWordFlowReaded;
    
    public ThWordState() {
        setFalseWordRouterJobToWriter();
        setFalseWordRouterJobToReader();
        /**
         * ThWordFlowReaded
         */
        setFalseWordFlowReaded();
    }
    /**
     * 
     * @return 
     * @throws #java.lang.IllegalArgumentException
     */
    protected ThWordBusWriter getBusJobForWordRouterJobToWriter(){
        if( !this.isWordRouterJobToWriter() ){
            throw new IllegalArgumentException("Bus jobs for output not set in " + ThWordState.class.getCanonicalName());
        }
        return (ThWordBusWriter) this.busWordRouterJobToWriter;
    }
    protected void setBusJobForWordRouterJobToWriter(final ThWordBusWriter busWordRouterJobToWriterOuter){
        this.busWordRouterJobToWriter = (ThWordBusWriter) busWordRouterJobToWriterOuter;
        setTrueWordRouterJobToWriter();
    }
    protected void setTrueWordRouterJobToWriter(){
        this.isSetWordRouterJobToWriter = Boolean.TRUE;
    }
    protected void setFalseWordRouterJobToWriter(){
        this.isSetWordRouterJobToWriter = Boolean.FALSE;
    }
    protected Boolean isWordRouterJobToWriter(){
        if( this.isSetWordRouterJobToWriter ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * 
     * @return 
     * @throws #java.lang.IllegalArgumentException
     */
    protected ThWordBusReader getBusJobForWordRouterJobToReader(){
        if( !this.isWordRouterJobToReader() ){
            throw new IllegalArgumentException("Bus jobs for output not set in " + ThWordState.class.getCanonicalName());
        }
        return (ThWordBusReader) this.busWordRouterJobToReader;
    }
    protected void setBusJobForWordRouterJobToReader(final ThWordBusReader busWordRouterJobToReaderOuter){
        this.busWordRouterJobToReader = (ThWordBusReader) busWordRouterJobToReaderOuter;
        setTrueWordRouterJobToReader();
    }
    protected void setTrueWordRouterJobToReader(){
        this.isSetWordRouterJobToReader = Boolean.TRUE;
    }
    protected void setFalseWordRouterJobToReader(){
        this.isSetWordRouterJobToReader = Boolean.FALSE;
    }
    protected Boolean isWordRouterJobToReader(){
        if( this.isSetWordRouterJobToReader ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
     /**
     * ThWordBusReadedFlow
     * @return 
     */
    protected ThWordBusReadedFlow getWordFlowReaded(){
        if( !this.isWordFlowReaded() ){
            throw new IllegalArgumentException(ThWordBusReadedFlow.class.getCanonicalName() + " object not set in " + ThWordRule.class.getCanonicalName());
        }
        return this.thWordFlowRead;
    }
    protected void setWordFlowReaded(final ThWordBusReadedFlow stateWordOuter){
        this.thWordFlowRead = stateWordOuter;
        setTrueWordFlowReaded();
    }
    protected void setTrueWordFlowReaded(){
        this.isSetWordFlowReaded = Boolean.TRUE;
    }
    protected void setFalseWordFlowReaded(){
        this.isSetWordFlowReaded = Boolean.FALSE;
    }
    protected Boolean isWordFlowReaded(){
        if( this.isSetWordFlowReaded ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
