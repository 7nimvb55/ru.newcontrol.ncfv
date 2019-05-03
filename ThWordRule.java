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

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.UUID;

/**
 * 
 * @author wladimirowichbiaran
 */
public class ThWordRule {
    private final ThreadGroup workerThWord;
    private final ThIndexRule indexRule;

    /**
     * ThWordWorkRouter
     */
    private ThWordWorkRouter runnableWorkerWordRouter;
    private Boolean isSetWordWorkRouter;
    private Boolean isRunWordWorkRouter;
    /**
     * ThWordWorkWrite
     */
    private ThWordWorkWrite runnableWorkerWordWrite;
    private Boolean isSetWordWorkWrite;
    private Boolean isRunWordWorkWrite;
    /**
     * ThWordWorkRead
     */
    private ThWordWorkRead runnableWorkerWordRead;
    private Boolean isSetWordWorkRead;
    private Boolean isRunWordWorkRead;
    /**
     * ThWordState
     */
    private ThWordState currentWordState;
    private Boolean isSetWordState;
    /**
     * ThWordStatusMainFlow
     */
    private ThWordStatusMainFlow currentWordStatusMainFlow;
    private Boolean isSetWordStatusMainFlow;
    
    
    public ThWordRule(final ThIndexRule outerRuleIndex) {
        this.indexRule = (ThIndexRule) outerRuleIndex;
        this.workerThWord = new ThreadGroup(UUID.randomUUID().toString());
        /**
         * ThWordState
         */
        setFalseWordState();
        /**
         * ThWordStatistic
         */
        setFalseWordStatusMainFlow();
        /**
         * ThWordWorkRouter
         */
        setFalseWordWorkRouter();
        setFalseRunnedWordWorkRouter();
        /**
         * ThWordWorkWrite
         */
        setFalseWordWorkWrite();
        setFalseRunnedWordWorkWrite();
        /**
         * ThWordWorkRead
         */
        setFalseWordWorkRead();
        setFalseRunnedWordWorkRead();
    }
    /**
     * ThIndexRule
     * @return 
     */
    protected ThIndexRule getIndexRule(){
        return (ThIndexRule) this.indexRule;
    }
    /**
     * ThWordWorkRouter
     * @return 
     */
    protected ThWordWorkRouter getWordWorkRouter(){
        if( !this.isWordWorkRouter() ){
            throw new IllegalArgumentException(ThWordWorkRouter.class.getCanonicalName() 
                    + " object not set in " 
                    + ThWordRule.class.getCanonicalName()
            );
        }
        return this.runnableWorkerWordRouter;
    }
    protected void setWordWorkRouter(final ThWordWorkRouter runnableWorkerWordRouterOuter){
        this.runnableWorkerWordRouter = (ThWordWorkRouter) runnableWorkerWordRouterOuter;
        setTrueWordWorkRouter();
    }
    protected void setTrueWordWorkRouter(){
        this.isSetWordWorkRouter = Boolean.TRUE;
    }
    protected void setFalseWordWorkRouter(){
        this.isSetWordWorkRouter = Boolean.FALSE;
    }
    protected Boolean isWordWorkRouter(){
        if( this.isSetWordWorkRouter ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueRunnedWordWorkRouter(){
        this.isRunWordWorkRouter = Boolean.TRUE;
    }
    protected void setFalseRunnedWordWorkRouter(){
        this.isRunWordWorkRouter = Boolean.FALSE;
    }
    protected Boolean isRunnedWordWorkRouter(){
        if( this.isRunWordWorkRouter ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void runRouterWordWork(){
        if( isWordWorkRouter() && !isRunnedWordWorkRouter() ){
            /**
             * @todo release workWriter Bus names for runned threads names threads
             * for release wait him finish functions
             */
            String toStringWordWorkRouter = UUID.randomUUID().toString();
            this.indexRule.addThredNameInQueue(toStringWordWorkRouter);
            Thread thForWorkRouter = new Thread(this.workerThWord, this.runnableWorkerWordRouter, toStringWordWorkRouter);
            thForWorkRouter.setPriority(5);
            thForWorkRouter.start();
        }
    }
    /**
     * ThWordWorkWrite
     * @return 
     */
    protected ThWordWorkWrite getWordWorkWrite(){
        if( !this.isWordWorkWrite() ){
            throw new IllegalArgumentException(ThWordWorkWrite.class.getCanonicalName() 
                    + " object not set in " 
                    + ThWordRule.class.getCanonicalName()
            );
        }
        return this.runnableWorkerWordWrite;
    }
    protected void setWordWorkWrite(final ThWordWorkWrite runnableWorkerWordWriteOuter){
        this.runnableWorkerWordWrite = (ThWordWorkWrite) runnableWorkerWordWriteOuter;
        setTrueWordWorkWrite();
    }
    protected void setTrueWordWorkWrite(){
        this.isSetWordWorkWrite = Boolean.TRUE;
    }
    protected void setFalseWordWorkWrite(){
        this.isSetWordWorkWrite = Boolean.FALSE;
    }
    protected Boolean isWordWorkWrite(){
        if( this.isSetWordWorkWrite ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueRunnedWordWorkWrite(){
        this.isRunWordWorkWrite = Boolean.TRUE;
    }
    protected void setFalseRunnedWordWorkWrite(){
        this.isRunWordWorkWrite = Boolean.FALSE;
    }
    protected Boolean isRunnedWordWorkWrite(){
        if( this.isRunWordWorkWrite ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void runWriteWordWork(){
        if( isWordWorkWrite() && !isRunnedWordWorkWrite() ){
            /**
             * @todo release workWriter Bus names for runned threads names threads
             * for release wait him finish functions
             */
            String toStringWordWorkWrite = UUID.randomUUID().toString();
            this.indexRule.addThredNameInQueue(toStringWordWorkWrite);
            Thread thForWorkWrite = new Thread(this.workerThWord, this.runnableWorkerWordWrite, toStringWordWorkWrite);
            thForWorkWrite.setPriority(3);
            thForWorkWrite.start();
        }
    }
    /**
     * ThWordWorkRead
     * @return 
     */
    protected ThWordWorkRead getWordWorkRead(){
        if( !this.isWordWorkRead() ){
            throw new IllegalArgumentException(ThWordWorkRead.class.getCanonicalName() 
                    + " object not set in " 
                    + ThWordRule.class.getCanonicalName()
            );
        }
        return this.runnableWorkerWordRead;
    }
    protected void setWordWorkRead(final ThWordWorkRead runnableWorkerWordReadOuter){
        this.runnableWorkerWordRead = (ThWordWorkRead) runnableWorkerWordReadOuter;
        setTrueWordWorkRead();
    }
    protected void setTrueWordWorkRead(){
        this.isSetWordWorkRead = Boolean.TRUE;
    }
    protected void setFalseWordWorkRead(){
        this.isSetWordWorkRead = Boolean.FALSE;
    }
    protected Boolean isWordWorkRead(){
        if( this.isSetWordWorkRead ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueRunnedWordWorkRead(){
        this.isRunWordWorkRead = Boolean.TRUE;
    }
    protected void setFalseRunnedWordWorkRead(){
        this.isRunWordWorkRead = Boolean.FALSE;
    }
    protected Boolean isRunnedWordWorkRead(){
        if( this.isRunWordWorkRead ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void runReadWordWork(){
        if( isWordWorkRead() && !isRunnedWordWorkRead() ){
            /**
             * @todo release workReadr Bus names for runned threads names threads
             * for release wait him finish functions
             */
            String toStringWordWorkRead = UUID.randomUUID().toString();
            this.indexRule.addThredNameInQueue(toStringWordWorkRead);
            Thread thForWorkRead = new Thread(this.workerThWord, this.runnableWorkerWordRead, toStringWordWorkRead);
            thForWorkRead.setPriority(4);
            thForWorkRead.start();
        }
    }
    /**
     * ThWordState
     * @return 
     */
    protected ThWordState getWordState(){
        if( !this.isWordState() ){
            throw new IllegalArgumentException(ThWordState.class.getCanonicalName() + " object not set in " + ThWordRule.class.getCanonicalName());
        }
        return this.currentWordState;
    }
    protected void setWordState(final ThWordState stateWordOuter){
        this.currentWordState = (ThWordState) stateWordOuter;
        setTrueWordState();
    }
    protected void setTrueWordState(){
        this.isSetWordState = Boolean.TRUE;
    }
    protected void setFalseWordState(){
        this.isSetWordState = Boolean.FALSE;
    }
    protected Boolean isWordState(){
        if( this.isSetWordState ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * ThWordStatusMainFlow
     * @return 
     */
    protected ThWordStatusMainFlow getWordStatusMainFlow(){
        if( !this.isWordStatusMainFlow() ){
            throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() + " object not set in " + ThWordRule.class.getCanonicalName());
        }
        return this.currentWordStatusMainFlow;
    }
    protected void setWordStatusMainFlow(final ThWordStatusMainFlow stateWordOuter){
        this.currentWordStatusMainFlow = stateWordOuter;
        setTrueWordStatusMainFlow();
    }
    protected void setTrueWordStatusMainFlow(){
        this.isSetWordStatusMainFlow = Boolean.TRUE;
    }
    protected void setFalseWordStatusMainFlow(){
        this.isSetWordStatusMainFlow = Boolean.FALSE;
    }
    protected Boolean isWordStatusMainFlow(){
        if( this.isSetWordStatusMainFlow ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
