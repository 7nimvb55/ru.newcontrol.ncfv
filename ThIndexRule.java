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
public class ThIndexRule {
    private ThIndexDirList threadIndexDirList;
    private Boolean isSetThreadIndexDirList;
    private Boolean isRunThreadIndexDirList;
    
    private ThIndexMaker threadIndexMaker;
    private Boolean isSetThreadIndexMaker;
    private Boolean isRunThreadIndexMaker;

    public ThIndexRule() {
        setFalseThreadIndexDirList();
        setFalseRunnedThreadIndexDirList();
        setFalseThreadIndexMaker();
        setFalseRunnedThreadIndexMaker();
    }
    
    /**
     * ThIndexDirList
     * @return 
     */
    protected ThIndexDirList getThreadIndexDirList(){
        if( !this.isThreadIndexDirList() ){
            throw new IllegalArgumentException(ThIndexDirList.class.getCanonicalName() + " object not set in " + ThIndexRule.class.getCanonicalName());
        }
        return this.threadIndexDirList;
    }
    protected void setThreadIndexDirList(final ThIndexDirList threadIndexDirListOuter){
        this.threadIndexDirList = threadIndexDirListOuter;
        setTrueThreadIndexDirList();
    }
    protected void setTrueThreadIndexDirList(){
        this.isSetThreadIndexDirList = Boolean.TRUE;
    }
    protected void setFalseThreadIndexDirList(){
        this.isSetThreadIndexDirList = Boolean.FALSE;
    }
    protected Boolean isThreadIndexDirList(){
        if( this.isSetThreadIndexDirList ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueRunnedThreadIndexDirList(){
        this.isRunThreadIndexDirList = Boolean.TRUE;
    }
    protected void setFalseRunnedThreadIndexDirList(){
        this.isRunThreadIndexDirList = Boolean.FALSE;
    }
    protected Boolean isRunnedThreadIndexDirList(){
        if( this.isRunThreadIndexDirList ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
        
    /**
     * ThIndexMaker
     * @return 
     */
    protected ThIndexMaker getThreadIndexMaker(){
        if( !this.isThreadIndexMaker() ){
            throw new IllegalArgumentException(ThIndexMaker.class.getCanonicalName() + " object not set in " + ThIndexRule.class.getCanonicalName());
        }
        return this.threadIndexMaker;
    }
    protected void setThreadIndexMaker(final ThIndexMaker threadIndexMakerOuter){
        this.threadIndexMaker = threadIndexMakerOuter;
        setTrueThreadIndexMaker();
    }
    protected void setTrueThreadIndexMaker(){
        this.isSetThreadIndexMaker = Boolean.TRUE;
    }
    protected void setFalseThreadIndexMaker(){
        this.isSetThreadIndexMaker = Boolean.FALSE;
    }
    protected Boolean isThreadIndexMaker(){
        if( this.isSetThreadIndexMaker ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueRunnedThreadIndexMaker(){
        this.isRunThreadIndexMaker = Boolean.TRUE;
    }
    protected void setFalseRunnedThreadIndexMaker(){
        this.isRunThreadIndexMaker = Boolean.FALSE;
    }
    protected Boolean isRunnedThreadIndexMaker(){
        if( this.isRunThreadIndexMaker ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
