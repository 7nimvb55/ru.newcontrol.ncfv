/*
 *  Copyright 2017 Administrator of development departament newcontrol.ru .
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package ru.newcontrol.ncfv;

import java.io.File;


/**
 * File View - application for view files and dirictories on the disks
 * and make his index for search in his names and path
 * <ul>
 * <li>Make index
 * <li>Search in files and dirictories path
 * </ul>
 * @author Administrators NewControl.ru
 * @version 2017-12-03
 */
public class Ncfv {
    private static boolean oneofAppRun = false;
    private static String argsFirst = "";
    private static boolean isRunInSwing = false;
    /**
     * If the Application run without arguments then run the Swing provided part
     * If the Application run with arguments set to "-console"
     * @param args the command line arguments
     *  "-console"
     */
    
    public static void main(String[] args) {
        
        if (args.length == 0){
            isRunInSwing = true;
            oneofAppRun = true;
            NcSwingIndexManagerApp.NcRunSIMA();
        }
        if (args.length > 0){
            argsFirst = args[0].trim().toLowerCase();
        }

        if (args.length == 1){
            switch (argsFirst){
                case "-getenv":
                    oneofAppRun = true;
                    NcAppHelper.getNcSysProperties();
                    break;
                case "-newcfg":
                    oneofAppRun = true;
                    NcPreRunFileViewer.createNewCfg();
                    break;
                case "-testinit":
                    oneofAppRun = true;
                    NcParamFv appWorkCfg = NcPreRunFileViewer.getCurrentWorkCfg();
                    NcParamFv appEmpty = new NcParamFv();
                    if( NcParamFvManager.isNcParamFvDataHashTrue(appEmpty) ){
                        NcAppHelper.outMessageToConsole("Empty NcParamFv hash true");
                    }
                    else{
                        NcAppHelper.outMessageToConsole("Empty NcParamFv hash false");
                        NcParamFvManager.ncParamFvDataOutPut(appEmpty);
                    }
                    if( !NcParamFvManager.isNcParamFvDataHashTrue(appWorkCfg) ){
                        NcAppHelper.outMessageToConsole("Work config values");
                        NcParamFvManager.ncParamFvDataOutPut(appWorkCfg);
                        NcAppHelper.appExitWithMessage("Work config hash is not valid, exit");
                    }
                    if( NcParamFvManager.isNcParamFvDataEmpty(appWorkCfg) ){
                        NcAppHelper.appExitWithMessage("Work config Empty, exit");
                    }
                    NcParamFvManager.ncParamFvDataOutPut(appWorkCfg);
                    break;
                case "-watchsubdir":
                    oneofAppRun = true;
                    //NcPreIdxWork.checkInIndexFolerContent();
                    NcPreIdxWork.outToConsoleIdxDirs();
                    break;
                case "-getdisks":
                    oneofAppRun = true;
                    NcAppHelper.outPutToConsoleDiskInfo();
                    break;
                case "-dev":
                    oneofAppRun = true;
                    NcPreIdxWork.getNotEqualsRecordDirListAttrVsExist();
                    break;
                case "-sf":
                    oneofAppRun = true;
                    NcSrchGetResult.outToConsoleSearchByKeyFromFile();
                    break;
                default:
                    consoleOutHelpUsageMessage();
                    break;
            }
        }
        
        if (args.length == 2){
            switch (argsFirst){
                case "-m":
                    String strPath = args[1].trim().toString();
                    if(NcIdxFileManager.dirOrFileExistRAccessChecker(strPath)){
                        oneofAppRun = true;
                        NcAppHelper.outMessageToConsole(getMessageRunIn());
                        NcAppHelper.outMessageToConsole("Init before start...");
                        NcParamFv appWorkCfg = NcPreRunFileViewer.getCurrentWorkCfg();
                        if( !NcParamFvManager.isNcParamFvDataHashTrue(appWorkCfg) ){
                            NcAppHelper.appExitWithMessage("Work config hash is not valid, exit");
                        }
                        if( NcParamFvManager.isNcParamFvDataEmpty(appWorkCfg) ){
                            NcAppHelper.appExitWithMessage("Work config Empty, exit");
                        }
                        NcParamFvManager.ncParamFvDataOutPut(appWorkCfg);
                        NcAppHelper.outMessageToConsole("Start make index for: " + strPath + " ... wait for end of process");
                        NcIndexPreProcessFiles ncIdxPreReturn = new NcIndexPreProcessFiles();
                        long count = ncIdxPreReturn.makeIndexRecursive(new File(strPath));
                        NcAppHelper.outMessageToConsole("For directory: " + strPath + " create " + count + " records in index");
                    }
                    else{
                        NcAppHelper.outMessageToConsole("Directory: " + strPath + " not exist or has not access to read");
                    }
                    break;
                case "-pv":
                    oneofAppRun = true;
                    NcAppHelper.outMessageToConsole(NcPathFromUserChecker.strInputAppWorkDirFromUser(args[1].trim().toString(), "/defaultValueDir"));
                    break;
                case "-fv":
                    oneofAppRun = true;
                    NcAppHelper.outMessageToConsole(NcPathFromUserChecker.strInputAppWorkFileFromUser(args[1].trim().toString(), "defaultValue.File"));
                    break;
                case "-s":
                    oneofAppRun = true;
                    String strKeyWord = args[1].trim().toString();
                    NcSrchGetResult.outToConsoleSearchByKeyFromInput(strKeyWord);
                    break;
                default:
                    consoleOutHelpUsageMessage();
                    break;
            }
        }
        
        
        
        if ( !oneofAppRun 
                || argsFirst.equals("-help")
                || argsFirst.equals("-h")
                || argsFirst.equals("/h")
                || argsFirst.equals("-?")
                || argsFirst.equals("?")
                || argsFirst.equals("/?")
                || argsFirst.equals("help")){
            consoleOutHelpUsageMessage();
        }
        if ( argsFirst.equals("-help-dev") ){
            consoleOutHelpUsageMessage();
        }
    }

/**
 * Method for putput in console "help to usage" messages
 */    
    private static void consoleOutHelpUsageMessage(){
        NcAppHelper.outMessageToConsole("Help to usage:");
        NcAppHelper.outMessageToConsole("Run this Application without parameters in command line to");
        NcAppHelper.outMessageToConsole(" use GUI (Graphics User Interface\n");
        NcAppHelper.outMessageToConsole("\t-help\n for output this message");
        NcAppHelper.outMessageToConsole("\t-m path\n for make index of path folder");
        NcAppHelper.outMessageToConsole("\t-sf\n for search in index by keyWords from files");
        NcAppHelper.outMessageToConsole("\t-s keyWord\n for search in index by keyWord input from parameter");
        NcAppHelper.outMessageToConsole("for functional and additional parameters in this");
        NcAppHelper.outMessageToConsole(" mode see Aplication code, or wait for changes this messages");
        NcAppHelper.outMessageToConsole(" in new releases");
    }
    private static void consoleOutHelpUsageMessageForDev(){
        NcAppHelper.outMessageToConsole("Help to usage:");
        NcAppHelper.outMessageToConsole("Run this Application without parameters in command line to");
        NcAppHelper.outMessageToConsole(" use GUI (Graphics User Interface\n");
        NcAppHelper.outMessageToConsole("\t-console param to run Application in console");
        NcAppHelper.outMessageToConsole("\t-getenv param to run Application in console with output environment and System properties");
        NcAppHelper.outMessageToConsole(" mode, for functional and additional parameters in this");
        NcAppHelper.outMessageToConsole(" mode see Aplication code, or wait for changes this messages");
        NcAppHelper.outMessageToConsole(" in new releases");
    }
    /**
     *
     * @return
     */
    public static boolean getRunIsSwing(){
        return isRunInSwing;
    }

    /**
     *
     * @return
     */
    public static String getMessageRunIn(){
        if(getRunIsSwing()){
            return "Application runned in window mode provided by Swing";
        }
        return "Application runned in console mode";
    }
    
}
