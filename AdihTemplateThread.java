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
public class AdihTemplateThread extends Thread{
    private final Integer numberProcessIndexSystem;
    private final AdimRule ruleAdim;
    private final AdilState adilState;
    private final Runnable runnerForWork;
    /**
     * 
     * @param processIndexSystemNumber
     * @param outerRule
     * @param outerRunner 
     * @throws UnsupportedOperationException
     */
    public AdihTemplateThread(final Integer processIndexSystemNumber,
            final AdimRule outerRule,
            final Runnable outerRunner){
        super(outerRunner, UUID.randomUUID().toString());
        if( outerRule == null ){
            throw new UnsupportedOperationException(AdimRule.class.getCanonicalName() 
                    + " object for set in "
                    + AdihTemplateThread.class.getCanonicalName()
                    + " is null");
        }
        this.ruleAdim = (AdimRule) outerRule;
        if( outerRunner == null ){
            throw new UnsupportedOperationException(Runnable.class.getCanonicalName() 
                    + " object for set in "
                    + AdihTemplateThread.class.getCanonicalName()
                    + " is null");
        }
        this.runnerForWork = (Runnable) outerRunner;
        if( processIndexSystemNumber == null ){
            throw new UnsupportedOperationException("processIndexSystemNumber for set in "
                    + AdihTemplateThread.class.getCanonicalName()
                    + " is null");
        }
        if( processIndexSystemNumber < 0 ){
            throw new UnsupportedOperationException("processIndexSystemNumber for set in "
                    + AdihTemplateThread.class.getCanonicalName()
                    + " is not natural ( processIndexSystemNumber < 0 (Zero) )");
        }
        this.numberProcessIndexSystem = processIndexSystemNumber;
        this.adilState = (AdilState) this.ruleAdim.getAdilRule().getAdilState();
    }
    
    @Override
    public void run() {
        String msgToLog = AdilConstants.INFO_LOGIC_POSITION
                + AdilConstants.CANONICALNAME
                + AdihTemplateThread.class.getCanonicalName()
                + AdilConstants.METHOD
                + "run()";
        try {
            this.adilState.putLogLineByProcessNumberMsg(this.numberProcessIndexSystem, 
                msgToLog
                + AdilConstants.START);
            try {
                this.runnerForWork.run();
            } catch(Throwable trEx) {
                this.adilState.putLogLineByProcessNumberMsgExceptions(
                        this.numberProcessIndexSystem, 
                        AdihTemplateThread.class.getCanonicalName(), 
                        this.runnerForWork.getClass().getCanonicalName() + ".run()", 
                        trEx.getMessage());
                //@todo log stack trace to other dir
                //trEx.getStackTrace();
            }
        /*this.adilState.putLogLineByProcessNumberMsg(this.numberProcessIndexSystem, 
                        msgToLog
                        + AdilConstants.STATE
                        + AdilConstants.VARNAME
                        + "typeWordOfBusOutputFunc"
                        + AdilConstants.VARVAL
                        + String.valueOf(typeWordOfBusOutputFunc)
                        + AdilConstants.VARNAME
                        + "itemKey"
                        + AdilConstants.VARVAL
                        + itemKey
                        + AdilConstants.VARNAME
                        + "hexTagName"
                        + AdilConstants.VARVAL
                        + hexTagName
                        + AdilConstants.VARNAME
                        + "subString"
                        + AdilConstants.VARVAL
                        + subString
                    );*/
        } finally {
            this.adilState.putLogLineByProcessNumberMsg(this.numberProcessIndexSystem, 
                msgToLog
                + AdilConstants.FINISH);
        }
    }
}
