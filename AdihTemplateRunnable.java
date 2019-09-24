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

import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AdihTemplateRunnable implements Runnable {

    private final Integer numberProcessIndexSystem;
    private final AdimRule ruleAdim;
    private final AdilState adilState;
    /**
     * 
     * @param processIndexSystemNumber
     * @param outerRule 
     * @throws UnsupportedOperationException
     */
    public AdihTemplateRunnable(final Integer processIndexSystemNumber,
            final AdimRule outerRule){
        if( outerRule == null ){
            throw new UnsupportedOperationException(AdimRule.class.getCanonicalName() 
                    + " object for set in "
                    + AdihTemplateRunnable.class.getCanonicalName()
                    + " is null");
        }
        this.ruleAdim = (AdimRule) outerRule;
        if( processIndexSystemNumber == null ){
            throw new UnsupportedOperationException("processIndexSystemNumber for set in "
                    + AdihTemplateRunnable.class.getCanonicalName()
                    + " is null");
        }
        if( processIndexSystemNumber < 0 ){
            throw new UnsupportedOperationException("processIndexSystemNumber for set in "
                    + AdihTemplateRunnable.class.getCanonicalName()
                    + " is not natural ( processIndexSystemNumber < 0 (Zero) )");
        }
        this.numberProcessIndexSystem = processIndexSystemNumber;
        this.adilState = (AdilState) this.ruleAdim.getAdilRule().getAdilState();
    }
    @Override
    public void run() {
        String msgToLog = AdilConstants.INFO_LOGIC_POSITION
                + AdilConstants.CANONICALNAME
                + AdihTemplateRunnable.class.getCanonicalName()
                + AdilConstants.METHOD
                + "run()";
        AdibProcessCommand adibProcessCommand = this.ruleAdim.getAdibProcessCommand();
        ConcurrentSkipListMap<Integer, Integer> commandsList = adibProcessCommand.getCommandsList();
        try {
            this.adilState.putLogLineByProcessNumberMsg(this.numberProcessIndexSystem, 
                msgToLog
                + AdilConstants.START);
            
            //@todo runner logic here
            Integer commandPoll;
            Integer sizeWait = adibProcessCommand.commandSizeQueue(0, this.numberProcessIndexSystem);
            while( sizeWait != 0 ){
                commandPoll = adibProcessCommand.commandPoll(0, this.numberProcessIndexSystem);
                this.adilState.putLogLineByProcessNumberMsg(this.numberProcessIndexSystem, 
                        msgToLog
                        + AdilConstants.STATE
                        + AdilConstants.VARNAME
                        + "adibProcessCommand.commandPoll(0, this.numberProcessIndexSystem);"
                        + AdilConstants.VARVAL
                        + String.valueOf(commandPoll)
                        + AdilConstants.VARNAME
                        + "this.numberProcessIndexSystem"
                        + AdilConstants.VARVAL
                        + String.valueOf(this.numberProcessIndexSystem)
                    );
                sizeWait = adibProcessCommand.commandSizeQueue(0, this.numberProcessIndexSystem);
            }
            Integer sizeDo = adibProcessCommand.commandSizeQueue(1, this.numberProcessIndexSystem);
            while( sizeDo != 0 ){
                commandPoll = adibProcessCommand.commandPoll(1, this.numberProcessIndexSystem);
                this.adilState.putLogLineByProcessNumberMsg(this.numberProcessIndexSystem, 
                        msgToLog
                        + AdilConstants.STATE
                        + AdilConstants.VARNAME
                        + "adibProcessCommand.commandPoll(1, this.numberProcessIndexSystem);"
                        + AdilConstants.VARVAL
                        + String.valueOf(commandPoll)
                        + AdilConstants.VARNAME
                        + "this.numberProcessIndexSystem"
                        + AdilConstants.VARVAL
                        + String.valueOf(this.numberProcessIndexSystem)
                    );
                sizeDo = adibProcessCommand.commandSizeQueue(1, this.numberProcessIndexSystem);
            }
            Integer sizeReady = adibProcessCommand.commandSizeQueue(2, this.numberProcessIndexSystem);
            while( sizeReady != 0 ){
                commandPoll = adibProcessCommand.commandPoll(2, this.numberProcessIndexSystem);
                this.adilState.putLogLineByProcessNumberMsg(this.numberProcessIndexSystem, 
                        msgToLog
                        + AdilConstants.STATE
                        + AdilConstants.VARNAME
                        + "adibProcessCommand.commandPoll(2, this.numberProcessIndexSystem);"
                        + AdilConstants.VARVAL
                        + String.valueOf(commandPoll)
                        + AdilConstants.VARNAME
                        + "this.numberProcessIndexSystem"
                        + AdilConstants.VARVAL
                        + String.valueOf(this.numberProcessIndexSystem)
                    );
                sizeReady = adibProcessCommand.commandSizeQueue(2, this.numberProcessIndexSystem);
            }
            
            
            Boolean commandListValide = adibProcessCommand.isCommandListValide(commandsList);
            this.adilState.putLogLineByProcessNumberMsg(this.numberProcessIndexSystem, 
                msgToLog
                        + AdilConstants.STATE
                        + AdilConstants.VARNAME
                        + "adibProcessCommand.isCommandListValide(commandsList)"
                        + AdilConstants.VARVAL
                        + String.valueOf(commandListValide)
                        + AdilConstants.VARNAME
                        + "this.numberProcessIndexSystem"
                        + AdilConstants.VARVAL
                        + String.valueOf(this.numberProcessIndexSystem));
        } finally {
            this.adilState.putLogLineByProcessNumberMsg(this.numberProcessIndexSystem, 
                msgToLog
                + AdilConstants.FINISH);
            adibProcessCommand = null;
        }    
    }
    
}
