/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
                        NcAppHelper.outMessage("Empty NcParamFv hash true");
                    }
                    else{
                        NcAppHelper.outMessage("Empty NcParamFv hash false");
                        NcParamFvManager.ncParamFvDataOutPut(appEmpty);
                    }
                    if( !NcParamFvManager.isNcParamFvDataHashTrue(appWorkCfg) ){
                        NcAppHelper.outMessage("Work config values");
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
                        NcAppHelper.outMessage(getMessageRunIn());
                        NcAppHelper.outMessage("Init before start...");
                        NcParamFv appWorkCfg = NcPreRunFileViewer.getCurrentWorkCfg();
                        if( !NcParamFvManager.isNcParamFvDataHashTrue(appWorkCfg) ){
                            NcAppHelper.appExitWithMessage("Work config hash is not valid, exit");
                        }
                        if( NcParamFvManager.isNcParamFvDataEmpty(appWorkCfg) ){
                            NcAppHelper.appExitWithMessage("Work config Empty, exit");
                        }
                        NcParamFvManager.ncParamFvDataOutPut(appWorkCfg);
                        NcAppHelper.outMessage("Start make index for: " + strPath + " ... wait for end of process");
                        NcIndexPreProcessFiles ncIdxPreReturn = new NcIndexPreProcessFiles();
                        long count = ncIdxPreReturn.makeIndexRecursive(new File(strPath));
                        NcAppHelper.outMessage("For directory: " + strPath + " create " + count + " records in index");
                    }
                    else{
                        NcAppHelper.outMessage("Directory: " + strPath + " not exist or has not access to read");
                    }
                    break;
                case "-pv":
                    oneofAppRun = true;
                    NcAppHelper.outMessage(NcPathFromUserChecker.strInputAppWorkDirFromUser(args[1].trim().toString(), "/defaultValueDir"));
                    break;
                case "-fv":
                    oneofAppRun = true;
                    NcAppHelper.outMessage(NcPathFromUserChecker.strInputAppWorkFileFromUser(args[1].trim().toString(), "defaultValue.File"));
                    break;
                case "-s":
                    oneofAppRun = true;
                    NcSearchInIndex.searchWordInIndex();
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
        NcAppHelper.outMessage("Help to usage:");
        NcAppHelper.outMessage("Run this Application without parameters in command line to");
        NcAppHelper.outMessage(" use GUI (Graphics User Interface\n");
        NcAppHelper.outMessage("\t-help for output this message");
        NcAppHelper.outMessage("\t-help-dev for output help message for developers");
        NcAppHelper.outMessage("\t-getenv param to run Application in console with output environment and System properties");
        NcAppHelper.outMessage(" mode, for functional and additional parameters in this");
        NcAppHelper.outMessage(" mode see Aplication code, or wait for changes this messages");
        NcAppHelper.outMessage(" in new releases");
    }
    private static void consoleOutHelpUsageMessageForDev(){
        NcAppHelper.outMessage("Help to usage:");
        NcAppHelper.outMessage("Run this Application without parameters in command line to");
        NcAppHelper.outMessage(" use GUI (Graphics User Interface\n");
        NcAppHelper.outMessage("\t-console param to run Application in console");
        NcAppHelper.outMessage("\t-getenv param to run Application in console with output environment and System properties");
        NcAppHelper.outMessage(" mode, for functional and additional parameters in this");
        NcAppHelper.outMessage(" mode see Aplication code, or wait for changes this messages");
        NcAppHelper.outMessage(" in new releases");
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
