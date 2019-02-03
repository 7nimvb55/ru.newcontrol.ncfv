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
public class ThFileListRule {
    private final ThreadGroup workerThFileList;
    private final ThIndexRule indexRule;
    /**
     * ThFileListState
     */
    private ThFileListState currentFileListState;
    private Boolean isSetFileListState;
    /**
     * ThFileListWorkBuild
     */
    private ThFileListWorkBuild runnableWorkerFileListBuild;
    private Boolean isSetFileListWorkBuild;
    private Boolean isRunFileListWorkBuild;
    
    ThFileListRule(final ThIndexRule outerRuleIndex) {
        this.indexRule = (ThIndexRule) outerRuleIndex;
        this.workerThFileList = new ThreadGroup(UUID.randomUUID().toString());
        /**
         * ThFileListState
         */
        setFalseFileListState();
        /**
         * ThFileListWorkBuild
         */
        setFalseFileListWorkBuild();
        setFalseRunnedFileListWorkBuild();
    }
    /**
     * ThIndexRule
     * @return 
     */
    protected ThIndexRule getIndexRule(){
        return (ThIndexRule) this.indexRule;
    }
    /**
     * ThFileListState
     * @return 
     */
    protected ThFileListState getFileListState(){
        if( !this.isFileListState() ){
            throw new IllegalArgumentException(ThFileListState.class.getCanonicalName() + " object not set in " + ThFileListRule.class.getCanonicalName());
        }
        return this.currentFileListState;
    }
    protected void setFileListState(final ThFileListState stateWordOuter){
        this.currentFileListState = stateWordOuter;
        setTrueFileListState();
    }
    protected void setTrueFileListState(){
        this.isSetFileListState = Boolean.TRUE;
    }
    protected void setFalseFileListState(){
        this.isSetFileListState = Boolean.FALSE;
    }
    protected Boolean isFileListState(){
        if( this.isSetFileListState ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * ThFileListWorkBuild
     * @return 
     */
    protected ThFileListWorkBuild getFileListWorkBuild(){
        if( !this.isFileListWorkBuild() ){
            throw new IllegalArgumentException(ThFileListWorkBuild.class.getCanonicalName() 
                    + " object not set in " 
                    + ThFileListRule.class.getCanonicalName()
            );
        }
        return this.runnableWorkerFileListBuild;
    }
    protected void setFileListWorkBuild(final ThFileListWorkBuild runnableWorkerFileListBuildOuter){
        this.runnableWorkerFileListBuild = (ThFileListWorkBuild) runnableWorkerFileListBuildOuter;
        setTrueFileListWorkBuild();
    }
    protected void setTrueFileListWorkBuild(){
        this.isSetFileListWorkBuild = Boolean.TRUE;
    }
    protected void setFalseFileListWorkBuild(){
        this.isSetFileListWorkBuild = Boolean.FALSE;
    }
    protected Boolean isFileListWorkBuild(){
        if( this.isSetFileListWorkBuild ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueRunnedFileListWorkBuild(){
        this.isRunFileListWorkBuild = Boolean.TRUE;
    }
    protected void setFalseRunnedFileListWorkBuild(){
        this.isRunFileListWorkBuild = Boolean.FALSE;
    }
    protected Boolean isRunnedFileListWorkBuild(){
        if( this.isRunFileListWorkBuild ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void runBuildFileListWork(){
        if( isFileListWorkBuild() && !isRunnedFileListWorkBuild() ){
            /**
             * @todo release workWriter Bus names for runned threads names threads
             * for release wait him finish functions
             */
            String toStringFileListWorkBuild = UUID.randomUUID().toString();
            this.indexRule.addThredNameInQueue(toStringFileListWorkBuild);
            Thread thForWorkWrite = new Thread(this.workerThFileList, this.runnableWorkerFileListBuild, toStringFileListWorkBuild);
            thForWorkWrite.start();
        }
    }
}
