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

import java.util.UUID;
import java.util.concurrent.LinkedTransferQueue;

/**
 *
 * @author wladimirowichbiaran
 */
public class AdilRule {
    private ThIndexRule indexRule;
    
    private AdilWorkerWrite runnableAdilWorkWrite;
    private Boolean isRunAdilWorkWrite;
    private Boolean isSetAdilWorkWrite;
    
    private AdilState currentAdilState;
    private Boolean isSetAdilState;
    
    private LinkedTransferQueue<UUID> queueForRunLogger;
    
    AdilRule(ThIndexRule indexRuleOuter){
        this.indexRule = (ThIndexRule) indexRuleOuter;
        this.queueForRunLogger = new LinkedTransferQueue<UUID>();
        setFalseRunnerAdilWorkWrite();
        setFalseAdilWorkWrite();
        setFalseAdilState();
    }
    /**
     * ThIndexRule
     * @return 
     */
    protected ThIndexRule getIndexRule(){
        return (ThIndexRule) this.indexRule;
    }
    /**
     * AdilWorkerWrite
     * @return 
     */
    protected AdilWorkerWrite getAdilWorkWrite(){
        if( !this.isAdilWorkWrite() ){
            throw new IllegalArgumentException(AdilWorkerWrite.class.getCanonicalName() 
                    + " object not set in " 
                    + AdilRule.class.getCanonicalName()
            );
        }
        return this.runnableAdilWorkWrite;
    }
    protected void setAdilWorkWrite(final AdilWorkerWrite runnableAdilWorkWriteOuter){
        this.runnableAdilWorkWrite = (AdilWorkerWrite) runnableAdilWorkWriteOuter;
        setTrueAdilWorkWrite();
    }
    protected void setTrueAdilWorkWrite(){
        this.isSetAdilWorkWrite = Boolean.TRUE;
    }
    protected void setFalseAdilWorkWrite(){
        this.isSetAdilWorkWrite = Boolean.FALSE;
    }
    protected Boolean isAdilWorkWrite(){
        if( this.isSetAdilWorkWrite ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueRunnerAdilWorkWrite(){
        this.isRunAdilWorkWrite = Boolean.TRUE;
    }
    protected void setFalseRunnerAdilWorkWrite(){
        this.isRunAdilWorkWrite = Boolean.FALSE;
    }
    protected Boolean isRunnedAdilWorkWrite(){
        if( this.isRunAdilWorkWrite ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void runAdilWorkWrite(){
        if( isAdilWorkWrite() && !isRunnedAdilWorkWrite() ){
            /**
             * @todo release workWriter Bus names for runned threads names threads
             * for release wait him finish functions
             */
            String toStringAdilWorkWrite = UUID.randomUUID().toString();
            //this.indexRule.addThreadNameInQueue(toStringAdilWorkWrite);
            Thread thForWorkRouter = new Thread(this.runnableAdilWorkWrite, toStringAdilWorkWrite);
            thForWorkRouter.setPriority(7);
            thForWorkRouter.start();
        } else {
            if( isAdilWorkWrite() ){
                this.queueForRunLogger.add(UUID.randomUUID());
            }
        }
    }
    protected void needNextRunLogger(){
        UUID poll;
        try {
            poll = this.queueForRunLogger.poll();
            if( poll != null ){
                runAdilWorkWrite();
            }
        } finally {
            poll = null;
        }
    }
    /**
     * AdilState
     * @return 
     */
    protected AdilState getAdilState(){
        if( !this.isAdilState() ){
            throw new IllegalArgumentException(AdilState.class.getCanonicalName() + " object not set in " + AdilRule.class.getCanonicalName());
        }
        return this.currentAdilState;
    }
    protected void setAdilState(final AdilState stateAdilOuter){
        this.currentAdilState = (AdilState) stateAdilOuter;
        setTrueAdilState();
    }
    protected void setTrueAdilState(){
        this.isSetAdilState = Boolean.TRUE;
    }
    protected void setFalseAdilState(){
        this.isSetAdilState = Boolean.FALSE;
    }
    protected Boolean isAdilState(){
        if( this.isSetAdilState ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
