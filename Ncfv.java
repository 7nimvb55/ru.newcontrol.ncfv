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
        AppListOfObjects obectsForApp = new AppListOfObjects();
        AppThManager loggerByThreads = new AppThManager(obectsForApp);
        System.out.println("logger test ");
        String strForPut = new String("logging for test");
        
        loggerByThreads.putLogInfoMessage(strForPut);
        loggerByThreads.doLogger();
        //runVersionOfAppBeforeThreadsInUse(args);
    }
    private static void runVersionOfAppBeforeThreadsInUse(String[] args){
                NcAppLoader.loadApp();
        if (args.length == 0){
            isRunInSwing = true;
            oneofAppRun = true;
            toLALRMain();
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.Ncfv#main(java.lang.String[]) }
     * </ul>
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
    /**
     * Not used
     */
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.Ncfv#getMessageRunIn() }
     * <li>{@link ru.newcontrol.ncfv.NcAppHelper#outMessage(java.lang.String) }
     * </ul>
     * @return
     */
    protected static boolean getRunIsSwing(){
        return isRunInSwing;
    }

    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.Ncfv#main(java.lang.String[]) }
     * </ul>
     * @return
     */
    private static String getMessageRunIn(){
        if(getRunIsSwing()){
            return "Application runned in window mode provided by Swing";
        }
        return "Application runned in console mode";
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.Ncfv#main(java.lang.String[]) }
     * </ul>
     * toLogAppLogicRecord(LALR) - toLALRMethodName make record in log file
     * first
     * second
     * third
     * fouth
     * fifth
     * sixth
     * seventh
     * eigth
     * ninth
     * elevrnth
     * twelfth
     * tertiary
     * fourteenth
     * fifteenth
     * sixteenth
     * seventeenth
     * eighteenth
     * nineteenth
     * twentieth
     */
    private static void toLALRMain(){
        if( NcfvRunVariables.isLALRMakeMain() ){
            String strLogMsg = NcStrLogMsgField.INFO.getStr()
                + NcStrLogMsgField.APP_LOGIC_NOW.getStr()
                + NcStrLogMsgText.RUN_WITH_OUT_ARGS.getStr()
                + NcStrLogMsgField.APP_LOGIC_NEXT_WAY_VAR.getStr()
                + NcStrLogMsgText.APP_GUI_START.getStr();
            NcAppHelper.outMessage(strLogMsg);
        }
    }
    
}
