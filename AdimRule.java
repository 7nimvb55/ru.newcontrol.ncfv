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
 * Adiwl
 * <ul>
 * <li>Automated
 * <li>data
 * <li>indexing
 * <li>mechanic
 * <li>Rule
 * <ul>
 * Contain methods for provided access for created objects
 * @author wladimirowichbiaran
 */
public class AdimRule {
    /**
     * AdibProcessCommand
     */
    private AdibProcessCommand commandProcess;
    private Boolean isSetAdibProcessCommand;
    /**
     * AdilRule
     */
    private AdilRule loggerRule;
    private Boolean isSetAdilRule;
    public AdimRule(){
        setFalseAdilRule();
        setFalseAdibProcessCommand();
    }
    /**
     * AdilRule
     * @return 
     * @throws IllegalArgumentException if not set
     */
    protected AdilRule getAdilRule(){
        if( !this.isAdilRule() ){
            throw new IllegalArgumentException(AdilRule.class.getCanonicalName() 
                    + " object not set in " 
                    + AdimRule.class.getCanonicalName());
        }
        return this.loggerRule;
    }
    /**
     * 
     * @param loggerAdilRuleOuter 
     */
    protected void setAdilRule(final AdilRule loggerAdilRuleOuter){
        if( loggerAdilRuleOuter != null){
            this.loggerRule = (AdilRule) loggerAdilRuleOuter;
            setTrueAdilRule();
        } else {
            throw new NullPointerException(AdilRule.class.getCanonicalName() 
                    + " object for set in " 
                    + AdimRule.class.getCanonicalName() 
                    + " is null");
        }
    }
    /**
     * Set in field <code>true</code>
     */
    protected void setTrueAdilRule(){
        this.isSetAdilRule = Boolean.TRUE;
    }
    /**
     * Set in field <code>false</code>
     */
    protected void setFalseAdilRule(){
        this.isSetAdilRule = Boolean.FALSE;
    }
    /**
     * 
     * @return true if AdilRule object set
     */
    protected Boolean isAdilRule(){
        if( this.isSetAdilRule ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * AdibProcessCommand
     * @return 
     * @throws IllegalArgumentException if not set
     */
    protected AdibProcessCommand getAdibProcessCommand(){
        if( !this.isAdibProcessCommand() ){
            throw new IllegalArgumentException(AdibProcessCommand.class.getCanonicalName() 
                    + " object not set in " 
                    + AdimRule.class.getCanonicalName());
        }
        return this.commandProcess;
    }
    /**
     * 
     * @param loggerAdibProcessCommandOuter 
     */
    protected void setAdibProcessCommand(final AdibProcessCommand loggerAdibProcessCommandOuter){
        if( loggerAdibProcessCommandOuter != null){
            this.commandProcess = (AdibProcessCommand) loggerAdibProcessCommandOuter;
            setTrueAdibProcessCommand();
        } else {
            throw new NullPointerException(AdibProcessCommand.class.getCanonicalName() 
                    + " object for set in " 
                    + AdimRule.class.getCanonicalName() 
                    + " is null");
        }
    }
    /**
     * Set in field <code>true</code>
     */
    protected void setTrueAdibProcessCommand(){
        this.isSetAdibProcessCommand = Boolean.TRUE;
    }
    /**
     * Set in field <code>false</code>
     */
    protected void setFalseAdibProcessCommand(){
        this.isSetAdibProcessCommand = Boolean.FALSE;
    }
    /**
     * 
     * @return true if AdibProcessCommand object set
     */
    protected Boolean isAdibProcessCommand(){
        if( this.isSetAdibProcessCommand ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
