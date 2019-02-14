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

/**
 *
 * @author wladimirowichbiaran
 */
public class ThStorageWordState {
    /**
     * ThStorageWordBusInput
     */
    private ThStorageWordBusInput busStorageWordRouterJob;
    private Boolean isSetStorageWordRouterJob;
    /**
     * ThStorageWordBusOutput
     */
    private ThStorageWordBusOutput busWordWritedJob;
    private Boolean isSetWordWritedJob;
    /**
     * ThStorageWordBusOutput
     */
    private ThStorageWordBusOutput busLongWordWritedJob;
    private Boolean isSetLongWordWritedJob;
    
    public ThStorageWordState() {
        setFalseStorageWordRouterJob();
        setFalseWordWritedJob();
        setFalseLongWordWritedJob();
    }
    /**
     * 
     * @return 
     * @throws #java.lang.IllegalArgumentException
     */
    protected ThStorageWordBusInput getBusJobForStorageWordRouterJob(){
        if( !this.isStorageWordRouterJob() ){
            throw new IllegalArgumentException("Bus jobs for output not set in " + ThStorageWordState.class.getCanonicalName());
        }
        return this.busStorageWordRouterJob;
    }
    protected void setBusJobForStorageWordRouterJob(final ThStorageWordBusInput busStorageWordRouterJobOuter){
        this.busStorageWordRouterJob = busStorageWordRouterJobOuter;
        setTrueStorageWordRouterJob();
    }
    protected void setTrueStorageWordRouterJob(){
        this.isSetStorageWordRouterJob = Boolean.TRUE;
    }
    protected void setFalseStorageWordRouterJob(){
        this.isSetStorageWordRouterJob = Boolean.FALSE;
    }
    protected Boolean isStorageWordRouterJob(){
        if( this.isSetStorageWordRouterJob ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * 
     * @return 
     * @throws #java.lang.IllegalArgumentException
     */
    protected ThStorageWordBusOutput getBusJobForWordWrite(){
        if( !this.isWordWritedJob() ){
            throw new IllegalArgumentException("Bus jobs for output not set in " + ThWordState.class.getCanonicalName());
        }
        return this.busWordWritedJob;
    }
    protected void setBusJobForWordWrite(final ThStorageWordBusOutput busWordWriteOuter){
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
    protected ThStorageWordBusOutput getBusJobForLongWordWrite(){
        if( !this.isLongWordWritedJob() ){
            throw new IllegalArgumentException("Bus jobs for output not set in " + ThWordState.class.getCanonicalName());
        }
        return this.busLongWordWritedJob;
    }
    protected void setBusJobForLongWordWrite(final ThStorageWordBusOutput busLongWordWriteOuter){
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
}