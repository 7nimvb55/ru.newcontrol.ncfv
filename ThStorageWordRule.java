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

/**
 *
 * @author wladimirowichbiaran
 */
public class ThStorageWordRule {
    private final ThreadGroup workerThStorageWord;
    private final ThIndexRule indexRule;
    /**
     * ThStorageWordWorkFilter
     */
    private ThStorageWordWorkFilter runnableWorkerStorageWordFilter;
    private Boolean isSetStorageWordWorkFilter;
    private Boolean isRunStorageWordWorkFilter;
    /**
     * ThStorageWordWorkRouter
     */
    private ThStorageWordWorkRouter runnableWorkerStorageWordRouter;
    private Boolean isSetStorageWordWorkRouter;
    private Boolean isRunStorageWordWorkRouter;
    /**
     * ThStorageWordWorkWrite
     */
    private ThStorageWordWorkWrite runnableWorkerStorageWordWrite;
    private Boolean isSetStorageWordWorkWrite;
    private Boolean isRunStorageWordWorkWrite;
    /**
     * ThStorageWordWorkRead
     */
    private ThStorageWordWorkRead runnableWorkerStorageWordRead;
    private Boolean isSetStorageWordWorkRead;
    private Boolean isRunStorageWordWorkRead;
    /**
     * ThStorageWordState
     */
    private ThStorageWordState currentStorageWordState;
    private Boolean isSetStorageWordState;
    /**
     * ThStorageWordStatusMainFlow
     */
    private ThStorageWordStatusMainFlow currentStorageWordStatistic;
    private Boolean isSetStorageWordStatistic;
    /**
     * ThStorageWordBusReadedFlow thStorageWordFlowRead
     */
    private ThStorageWordBusReadedFlow thStorageWordFlowRead;
    private Boolean isSetStorageWordFlowReaded;
    
    public ThStorageWordRule(final ThIndexRule outerRuleIndex) {
        this.indexRule = (ThIndexRule) outerRuleIndex;
        this.workerThStorageWord = new ThreadGroup(UUID.randomUUID().toString());
        /**
         * ThStorageWordState
         */
        setFalseStorageWordState();
        /**
         * ThStorageWordStatistic
         */
        setFalseStorageWordStatistic();
        /**
         * ThStorageWordFlowReaded
         */
        setFalseStorageWordFlowReaded();
        /**
         * ThStorageWordWorkFilter
         */
        setFalseStorageWordWorkFilter();
        setFalseRunnedStorageWordWorkFilter();
        /**
         * ThStorageWordWorkRouter
         */
        setFalseStorageWordWorkRouter();
        setFalseRunnedStorageWordWorkRouter();
        /**
         * ThStorageWordWorkWrite
         */
        setFalseStorageWordWorkWrite();
        setFalseRunnedStorageWordWorkWrite();
        /**
         * ThStorageWordWorkRead
         */
        setFalseStorageWordWorkRead();
        setFalseRunnedStorageWordWorkRead();
    }
    /**
     * ThIndexRule
     * @return 
     */
    protected ThIndexRule getIndexRule(){
        return (ThIndexRule) this.indexRule;
    }
    /**
     * ThStorageWordWorkFilter
     * @return 
     */
    protected ThStorageWordWorkFilter getStorageWordWorkFilter(){
        if( !this.isStorageWordWorkFilter() ){
            throw new IllegalArgumentException(ThStorageWordWorkFilter.class.getCanonicalName() 
                    + " object not set in " 
                    + ThWordRule.class.getCanonicalName()
            );
        }
        return this.runnableWorkerStorageWordFilter;
    }
    protected void setStorageWordWorkFilter(final ThStorageWordWorkFilter runnableWorkerStorageWordFilterOuter){
        this.runnableWorkerStorageWordFilter = (ThStorageWordWorkFilter) runnableWorkerStorageWordFilterOuter;
        setTrueStorageWordWorkFilter();
    }
    protected void setTrueStorageWordWorkFilter(){
        this.isSetStorageWordWorkFilter = Boolean.TRUE;
    }
    protected void setFalseStorageWordWorkFilter(){
        this.isSetStorageWordWorkFilter = Boolean.FALSE;
    }
    protected Boolean isStorageWordWorkFilter(){
        if( this.isSetStorageWordWorkFilter ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueRunnedStorageWordWorkFilter(){
        this.isRunStorageWordWorkFilter = Boolean.TRUE;
    }
    protected void setFalseRunnedStorageWordWorkFilter(){
        this.isRunStorageWordWorkFilter = Boolean.FALSE;
    }
    protected Boolean isRunnedStorageWordWorkFilter(){
        if( this.isRunStorageWordWorkFilter ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void runFilterStorageWordWork(){
        if( isStorageWordWorkFilter() && !isRunnedStorageWordWorkFilter() ){
            /**
             * @todo release workWriter Bus names for runned threads names threads
             * for release wait him finish functions
             */
            String toStringStorageWordWorkFilter = UUID.randomUUID().toString();
            this.indexRule.addThredNameInQueue(toStringStorageWordWorkFilter);
            Thread thForWorkFilter = new Thread(this.workerThStorageWord, this.runnableWorkerStorageWordFilter, toStringStorageWordWorkFilter);
            thForWorkFilter.setPriority(2);
            thForWorkFilter.start();
        }
    }
    /**
     * ThStorageWordWorkRouter
     * @return 
     */
    protected ThStorageWordWorkRouter getStorageWordWorkRouter(){
        if( !this.isStorageWordWorkRouter() ){
            throw new IllegalArgumentException(ThStorageWordWorkRouter.class.getCanonicalName() 
                    + " object not set in " 
                    + ThWordRule.class.getCanonicalName()
            );
        }
        return this.runnableWorkerStorageWordRouter;
    }
    protected void setStorageWordWorkRouter(final ThStorageWordWorkRouter runnableWorkerStorageWordRouterOuter){
        this.runnableWorkerStorageWordRouter = (ThStorageWordWorkRouter) runnableWorkerStorageWordRouterOuter;
        setTrueStorageWordWorkRouter();
    }
    protected void setTrueStorageWordWorkRouter(){
        this.isSetStorageWordWorkRouter = Boolean.TRUE;
    }
    protected void setFalseStorageWordWorkRouter(){
        this.isSetStorageWordWorkRouter = Boolean.FALSE;
    }
    protected Boolean isStorageWordWorkRouter(){
        if( this.isSetStorageWordWorkRouter ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueRunnedStorageWordWorkRouter(){
        this.isRunStorageWordWorkRouter = Boolean.TRUE;
    }
    protected void setFalseRunnedStorageWordWorkRouter(){
        this.isRunStorageWordWorkRouter = Boolean.FALSE;
    }
    protected Boolean isRunnedStorageWordWorkRouter(){
        if( this.isRunStorageWordWorkRouter ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void runRouterStorageWordWork(){
        if( isStorageWordWorkRouter() && !isRunnedStorageWordWorkRouter() ){
            /**
             * @todo release workWriter Bus names for runned threads names threads
             * for release wait him finish functions
             */
            String toStringStorageWordWorkRouter = UUID.randomUUID().toString();
            this.indexRule.addThredNameInQueue(toStringStorageWordWorkRouter);
            Thread thForWorkRouter = new Thread(this.workerThStorageWord, this.runnableWorkerStorageWordRouter, toStringStorageWordWorkRouter);
            thForWorkRouter.setPriority(5);
            thForWorkRouter.start();
        }
    }
    /**
     * ThStorageWordWorkWrite
     * @return 
     */
    protected ThStorageWordWorkWrite getStorageWordWorkWrite(){
        if( !this.isStorageWordWorkWrite() ){
            throw new IllegalArgumentException(ThStorageWordWorkWrite.class.getCanonicalName() 
                    + " object not set in " 
                    + ThWordRule.class.getCanonicalName()
            );
        }
        return this.runnableWorkerStorageWordWrite;
    }
    protected void setStorageWordWorkWrite(final ThStorageWordWorkWrite runnableWorkerStorageWordWriteOuter){
        this.runnableWorkerStorageWordWrite = (ThStorageWordWorkWrite) runnableWorkerStorageWordWriteOuter;
        setTrueStorageWordWorkWrite();
    }
    protected void setTrueStorageWordWorkWrite(){
        this.isSetStorageWordWorkWrite = Boolean.TRUE;
    }
    protected void setFalseStorageWordWorkWrite(){
        this.isSetStorageWordWorkWrite = Boolean.FALSE;
    }
    protected Boolean isStorageWordWorkWrite(){
        if( this.isSetStorageWordWorkWrite ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueRunnedStorageWordWorkWrite(){
        this.isRunStorageWordWorkWrite = Boolean.TRUE;
    }
    protected void setFalseRunnedStorageWordWorkWrite(){
        this.isRunStorageWordWorkWrite = Boolean.FALSE;
    }
    protected Boolean isRunnedStorageWordWorkWrite(){
        if( this.isRunStorageWordWorkWrite ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void runWriteStorageWordWork(){
        if( isStorageWordWorkWrite() && !isRunnedStorageWordWorkWrite() ){
            /**
             * @todo release workWriter Bus names for runned threads names threads
             * for release wait him finish functions
             */
            String toStringStorageWordWorkWrite = UUID.randomUUID().toString();
            this.indexRule.addThredNameInQueue(toStringStorageWordWorkWrite);
            Thread thForWorkWrite = new Thread(this.workerThStorageWord, this.runnableWorkerStorageWordWrite, toStringStorageWordWorkWrite);
            thForWorkWrite.setPriority(3);
            thForWorkWrite.start();
        }
    }
    /**
     * ThStorageWordWorkRead
     * @return 
     */
    protected ThStorageWordWorkRead getStorageWordWorkRead(){
        if( !this.isStorageWordWorkRead() ){
            throw new IllegalArgumentException(ThStorageWordWorkRead.class.getCanonicalName() 
                    + " object not set in " 
                    + ThWordRule.class.getCanonicalName()
            );
        }
        return this.runnableWorkerStorageWordRead;
    }
    protected void setStorageWordWorkRead(final ThStorageWordWorkRead runnableWorkerStorageWordReadOuter){
        this.runnableWorkerStorageWordRead = (ThStorageWordWorkRead) runnableWorkerStorageWordReadOuter;
        setTrueStorageWordWorkRead();
    }
    protected void setTrueStorageWordWorkRead(){
        this.isSetStorageWordWorkRead = Boolean.TRUE;
    }
    protected void setFalseStorageWordWorkRead(){
        this.isSetStorageWordWorkRead = Boolean.FALSE;
    }
    protected Boolean isStorageWordWorkRead(){
        if( this.isSetStorageWordWorkRead ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueRunnedStorageWordWorkRead(){
        this.isRunStorageWordWorkRead = Boolean.TRUE;
    }
    protected void setFalseRunnedStorageWordWorkRead(){
        this.isRunStorageWordWorkRead = Boolean.FALSE;
    }
    protected Boolean isRunnedStorageWordWorkRead(){
        if( this.isRunStorageWordWorkRead ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void runReadStorageWordWork(){
        if( isStorageWordWorkRead() && !isRunnedStorageWordWorkRead() ){
            /**
             * @todo release workReadr Bus names for runned threads names threads
             * for release wait him finish functions
             */
            String toStringStorageWordWorkRead = UUID.randomUUID().toString();
            this.indexRule.addThredNameInQueue(toStringStorageWordWorkRead);
            Thread thForWorkRead = new Thread(this.workerThStorageWord, this.runnableWorkerStorageWordRead, toStringStorageWordWorkRead);
            thForWorkRead.setPriority(4);
            thForWorkRead.start();
        }
    }
    /**
     * ThStorageWordState
     * @return 
     */
    protected ThStorageWordState getStorageWordState(){
        if( !this.isStorageWordState() ){
            throw new IllegalArgumentException(ThStorageWordState.class.getCanonicalName() + " object not set in " + ThWordRule.class.getCanonicalName());
        }
        return this.currentStorageWordState;
    }
    protected void setStorageWordState(final ThStorageWordState stateWordOuter){
        this.currentStorageWordState = (ThStorageWordState) stateWordOuter;
        setTrueStorageWordState();
    }
    protected void setTrueStorageWordState(){
        this.isSetStorageWordState = Boolean.TRUE;
    }
    protected void setFalseStorageWordState(){
        this.isSetStorageWordState = Boolean.FALSE;
    }
    protected Boolean isStorageWordState(){
        if( this.isSetStorageWordState ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * ThStorageWordStatusMainFlow
     * @return 
     */
    protected ThStorageWordStatusMainFlow getStorageWordStatistic(){
        if( !this.isStorageWordStatistic() ){
            throw new IllegalArgumentException(ThStorageWordStatusMainFlow.class.getCanonicalName() + " object not set in " + ThWordRule.class.getCanonicalName());
        }
        return this.currentStorageWordStatistic;
    }
    protected void setStorageWordStatistic(final ThStorageWordStatusMainFlow stateWordOuter){
        this.currentStorageWordStatistic = stateWordOuter;
        setTrueStorageWordStatistic();
    }
    protected void setTrueStorageWordStatistic(){
        this.isSetStorageWordStatistic = Boolean.TRUE;
    }
    protected void setFalseStorageWordStatistic(){
        this.isSetStorageWordStatistic = Boolean.FALSE;
    }
    protected Boolean isStorageWordStatistic(){
        if( this.isSetStorageWordStatistic ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * ThStorageWordBusReadedFlow
     * @return 
     */
    protected ThStorageWordBusReadedFlow getStorageWordFlowReaded(){
        if( !this.isStorageWordFlowReaded() ){
            throw new IllegalArgumentException(ThStorageWordBusReadedFlow.class.getCanonicalName() + " object not set in " + ThWordRule.class.getCanonicalName());
        }
        return this.thStorageWordFlowRead;
    }
    protected void setStorageWordFlowReaded(final ThStorageWordBusReadedFlow stateWordOuter){
        this.thStorageWordFlowRead = stateWordOuter;
        setTrueStorageWordFlowReaded();
    }
    protected void setTrueStorageWordFlowReaded(){
        this.isSetStorageWordFlowReaded = Boolean.TRUE;
    }
    protected void setFalseStorageWordFlowReaded(){
        this.isSetStorageWordFlowReaded = Boolean.FALSE;
    }
    protected Boolean isStorageWordFlowReaded(){
        if( this.isSetStorageWordFlowReaded ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
