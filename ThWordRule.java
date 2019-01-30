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
 * On/off set and nore functions
 * @author wladimirowichbiaran
 */
public class ThWordRule {
    //Released version
    private ThreadGroup workerThWord;
    private ThWordState currentWordState;
    private Boolean isSetWordState;
    
    /*private ThWordStatistic currentWordCounter;
    private Boolean isSetWordCounter;
    
    
    private ThWordWorkRead runnableWorkerWordRead;
    private Boolean isSetWordWorkReader;
    private Boolean isRunWordWorkReader;*/
    
    private ThWordWorkWrite runnableWorkerWordWrite;
    private Boolean isSetWordWorkWriter;
    private Boolean isRunWordWorkWriter;
    
    private ThWordWorkBuild runnableWorkerWordBuild;
    private Boolean isSetWordWorkBuild;
    private Boolean isRunWordWorkBuild;
    
    private ThIndexRule outerIndexRule;

    public ThWordRule (final ThIndexRule outerRule) {
        //Released version
        this.outerIndexRule = outerRule;
        this.workerThWord = new ThreadGroup(UUID.randomUUID().toString());
        setFalseWordState();
        //setFalseWordCounter();
        //setFalseWordWorkReader();
        setFalseWordWorkWriter();
        setFalseWordWorkBuild();
        //setFalseRunnedWordWorkReader();
        setFalseRunnedWordWorkWriter();
        setFalseRunnedWordWorkBuild();
    }
    //Released version
    protected ThIndexRule getIndexRule(){
        return this.outerIndexRule;
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
        this.currentWordState = stateWordOuter;
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
     * ThWordStatistic
     * @return 
     */
    /*protected ThWordStatistic getWordCounter(){
        if( !this.isWordCounter() ){
            throw new IllegalArgumentException(ThWordStatistic.class.getCanonicalName() + " object not set in " + ThWordRule.class.getCanonicalName());
        }
        return this.currentWordCounter;
    }
    protected void setWordCounter(final ThWordStatistic counterWordOuter){
        this.currentWordCounter = counterWordOuter;
        setTrueWordCounter();
    }
    protected void setTrueWordCounter(){
        this.isSetWordCounter = Boolean.TRUE;
    }
    protected void setFalseWordCounter(){
        this.isSetWordCounter = Boolean.FALSE;
    }
    protected Boolean isWordCounter(){
        if( this.isSetWordCounter ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }*/
    /**
     * ThWordWorkRead
     * @return 
     */
    /*protected ThWordWorkRead getWordWorkReader(){
        if( !this.isWordWorkReader() ){
            throw new IllegalArgumentException(ThWordWorkRead.class.getCanonicalName() + " object not set in " + ThWordRule.class.getCanonicalName());
        }
        return this.runnableWorkerWordRead;
    }
    protected void setWordWorkReader(final ThWordWorkRead runnableWorkerWordReadOuter){
        this.runnableWorkerWordRead = runnableWorkerWordReadOuter;
        setTrueWordWorkReader();
    }
    protected void setTrueWordWorkReader(){
        this.isSetWordWorkReader = Boolean.TRUE;
    }
    protected void setFalseWordWorkReader(){
        this.isSetWordWorkReader = Boolean.FALSE;
    }
    protected Boolean isWordWorkReader(){
        if( this.isSetWordWorkReader ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueRunnedWordWorkReader(){
        this.isRunWordWorkReader = Boolean.TRUE;
    }
    protected void setFalseRunnedWordWorkReader(){
        this.isRunWordWorkReader = Boolean.FALSE;
    }
    protected Boolean isRunnedWordWorkReader(){
        if( this.isRunWordWorkReader ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }*/
    /**
     * ThWordWorkWrite
     * @return 
     */
    protected ThWordWorkWrite getWordWorkWriter(){
        if( !this.isWordWorkWriter() ){
            throw new IllegalArgumentException(ThWordWorkWrite.class.getCanonicalName() + " object not set in " + ThWordRule.class.getCanonicalName());
        }
        return this.runnableWorkerWordWrite;
    }
    protected void setWordWorkWriter(final ThWordWorkWrite runnableWorkerWordWriteOuter){
        this.runnableWorkerWordWrite = runnableWorkerWordWriteOuter;
        setTrueWordWorkWriter();
    }
    protected void setTrueWordWorkWriter(){
        this.isSetWordWorkWriter = Boolean.TRUE;
    }
    protected void setFalseWordWorkWriter(){
        this.isSetWordWorkWriter = Boolean.FALSE;
    }
    protected Boolean isWordWorkWriter(){
        if( this.isSetWordWorkWriter ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueRunnedWordWorkWriter(){
        this.isRunWordWorkWriter = Boolean.TRUE;
    }
    protected void setFalseRunnedWordWorkWriter(){
        this.isRunWordWorkWriter = Boolean.FALSE;
    }
    protected Boolean isRunnedWordWorkWriter(){
        if( this.isRunWordWorkWriter ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * ThWordWorkBuild
     * @return 
     */
    protected ThWordWorkBuild getWordWorkBuild(){
        if( !this.isWordWorkBuild() ){
            throw new IllegalArgumentException(ThWordWorkBuild.class.getCanonicalName() + " object not set in " + ThWordRule.class.getCanonicalName());
        }
        return this.runnableWorkerWordBuild;
    }
    protected void setWordWorkBuild(final ThWordWorkBuild runnableWorkerWordBuildOuter){
        this.runnableWorkerWordBuild = runnableWorkerWordBuildOuter;
        setTrueWordWorkBuild();
    }
    protected void setTrueWordWorkBuild(){
        this.isSetWordWorkBuild = Boolean.TRUE;
    }
    protected void setFalseWordWorkBuild(){
        this.isSetWordWorkWriter = Boolean.FALSE;
    }
    protected Boolean isWordWorkBuild(){
        if( this.isSetWordWorkBuild ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueRunnedWordWorkBuild(){
        this.isRunWordWorkBuild = Boolean.TRUE;
    }
    protected void setFalseRunnedWordWorkBuild(){
        this.isRunWordWorkBuild = Boolean.FALSE;
    }
    protected Boolean isRunnedWordWorkBuild(){
        if( this.isRunWordWorkBuild ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * Check for ready and not runned and run workers
     */
    //Check for ready and Run workers in threads
    /*protected void runReadFromWord(){
        if( isWordWorkReader() && !isRunnedWordWorkReader() ){
            /**
             * @todo release workReader Bus names for runned threads names threads
             * for release wait him finish functions
             */
            /*String toStringWorkReader = UUID.randomUUID().toString();
            this.outerIndexRule.addThredNameInQueue(toStringWorkReader);
            Thread thForWorkRead = new Thread(this.workerThWord, this.runnableWorkerWordRead, toStringWorkReader);
            thForWorkRead.start();
        }
    }*/
    protected void runWriteToWord(){
        if( isWordWorkWriter() && !isRunnedWordWorkWriter() ){
            /**
             * @todo release workWriter Bus names for runned threads names threads
             * for release wait him finish functions
             */
            String toStringWorkWriter = UUID.randomUUID().toString();
            this.outerIndexRule.addThredNameInQueue(toStringWorkWriter);
            Thread thForWorkWrite = new Thread(this.workerThWord, this.runnableWorkerWordWrite, toStringWorkWriter);
            thForWorkWrite.start();
        }
    }
    protected void runBuildWordWorkers(){
        if( isWordWorkBuild() && !isRunnedWordWorkBuild() ){
            /**
             * @todo release workWriter Bus names for runned threads names threads
             * for release wait him finish functions
             */
            String toStringWorkBuild = UUID.randomUUID().toString();
            this.outerIndexRule.addThredNameInQueue(toStringWorkBuild);
            Thread thForWorkBuild = new Thread(this.workerThWord, this.runnableWorkerWordBuild, toStringWorkBuild);
            thForWorkBuild.start();
        }
    }
    
}
