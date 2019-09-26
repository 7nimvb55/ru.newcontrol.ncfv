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
public class AdimProcessCommand {
    /**
     * Check list of commands if it is valide return list result numbers
     * {@code <Integer commandRunnerInterpritation, Integer numberProcess>}
     * @param ruleAdimInputed
     * @param numberProcessInputed
     * @return 
     */
    protected static ConcurrentSkipListMap<Integer, Integer> commandDetector(AdimRule ruleAdimInputed, Integer numberProcessInputed){
        ConcurrentSkipListMap<Integer, Integer> resultProcessorCommand = new ConcurrentSkipListMap<Integer, Integer>();
        if( ruleAdimInputed == null ){
            return resultProcessorCommand;
        }
        if( numberProcessInputed == null ){
            return resultProcessorCommand;
        }
        Integer numberProcess = numberProcessInputed;
        AdimRule ruleAdimFunc = (AdimRule) ruleAdimInputed;
        AdilState adilStateFunc = (AdilState) ruleAdimFunc.getAdilRule().getAdilState();
        AdibProcessCommand adibProcessCommand = (AdibProcessCommand) ruleAdimFunc.getAdibProcessCommand();
        ConcurrentSkipListMap<Integer, Integer> commandsList = adibProcessCommand.getCommandsList();
        Boolean commandListValide = adibProcessCommand.isCommandListValide(commandsList);
        String msgLog = new String().concat(AdilConstants.CANONICALNAME
                .concat(AdimProcessCommand.class.getCanonicalName()))
                .concat(AdilConstants.METHOD)
                .concat("commandDetector()");
        Integer commandType = 0;
        Integer commandQueueSize = 0;
        Integer commandPoll = Integer.MIN_VALUE;
        Integer startCommandCode = commandsList.get(0);
        Integer stopCommandCode = commandsList.get(1);
        Integer pauseFromUserCommandCode = commandsList.get(2);
        try {
            adilStateFunc.putLogLineByProcessNumberMsgInfo(numberProcess, msgLog.concat(AdilConstants.START));
            if( commandListValide ){
                for( commandType = 0; commandType < 3; commandType++ ){
                    do {
                        commandPoll = adibProcessCommand.commandPoll(commandType, numberProcess);
                        commandQueueSize = adibProcessCommand.commandSizeQueue(commandType, numberProcess);
                        if( startCommandCode.equals(commandPoll) ){
                            adilStateFunc.putLogLineByProcessNumberMsgInfo(numberProcess, 
                                    msgLog.concat(AdilHelper.variableNameValue(new String[]{
                                        "commandPoll",
                                        String.valueOf(commandPoll),
                                        "commandQueueSize",
                                        String.valueOf(commandQueueSize),
                                    })).concat(AdilConstants.DESCRIPTION).concat("commandStart"));
                            adilStateFunc.logStackTrace(numberProcess);
                            //@todo AdimFactory logic procedure key number put into returned list for exit from this procedure
                            resultProcessorCommand.put(0, numberProcess);
                        }
                        if( stopCommandCode.equals(commandPoll) ){
                            adilStateFunc.putLogLineByProcessNumberMsgInfo(numberProcess, 
                                    msgLog.concat(AdilHelper.variableNameValue(new String[]{
                                        "commandPoll",
                                        String.valueOf(commandPoll),
                                        "commandQueueSize",
                                        String.valueOf(commandQueueSize),
                                    })).concat(AdilConstants.DESCRIPTION).concat("commandStop"));
                            adilStateFunc.logStackTrace(numberProcess);
                            //@todo AdimFactory logic procedure key number put into returned list for exit from this procedure
                            resultProcessorCommand.put(1, numberProcess);
                        }
                        if( pauseFromUserCommandCode.equals(commandPoll) ){
                            adilStateFunc.putLogLineByProcessNumberMsgInfo(numberProcess, 
                                    msgLog.concat(AdilHelper.variableNameValue(new String[]{
                                        "commandPoll",
                                        String.valueOf(commandPoll),
                                        "commandQueueSize",
                                        String.valueOf(commandQueueSize),
                                    })).concat(AdilConstants.DESCRIPTION).concat("pauseFromUser"));
                            adilStateFunc.logStackTrace(numberProcess);
                            AdimFactory.workerSleep();
                            adilStateFunc.logStackTrace(numberProcess);
                            //@todo AdimFactory logic procedure key number put into returned list for exit from this procedure
                            resultProcessorCommand.put(1, numberProcess);
                        }
                    }while( commandQueueSize > 0 );
                }
            }
            return resultProcessorCommand;
        } finally {
            adilStateFunc.putLogLineByProcessNumberMsgInfo(numberProcess, msgLog.concat(AdilConstants.FINISH));
            numberProcess = null;
            ruleAdimFunc = null;
            adilStateFunc = null;
            adibProcessCommand = null;
            commandsList = null;
            commandListValide = null;
            AdihUtilization.utilizeStringValues(new String[] {msgLog});
        }
    }
}
