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
     * ThStorageWordBusOutput
     */
    private ThStorageWordBusOutput busWordWritedJob;
    private Boolean isSetWordWritedJob;
    
    public ThStorageWordState() {
        setFalseWordWritedJob();
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
}
