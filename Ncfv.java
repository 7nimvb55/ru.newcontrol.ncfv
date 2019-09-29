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
import java.nio.file.FileSystem;
import java.security.AccessControlException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;


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
        
        AppEtcSecurityHelper.createNewSecurity();
        ThIndexRule thIndexRule = new ThIndexRule();
        AdihZipStorages storeNew = new AdihZipStorages(thIndexRule);
        try {
            System.out.println("*|*|* *|*|* *|*|* print created storages");
            //storeNew.printAllList();
            //storeNew.updateStorageList();
            //System.out.println("*|*|* *|*|* *|*|* print updated list of storages");
            FileSystem storeFileSystemByNumber = storeNew.getStoreFileSystemByNumber(3);
            System.out.println("*|"
                    + storeFileSystemByNumber.toString()
                    + "*|* *| opened "
                    + storeFileSystemByNumber.isOpen());
            FileSystem storeFileSystemByNumber1 = storeNew.getStoreFileSystemByNumber(10);
            System.out.println("*|"
                    + storeFileSystemByNumber1.toString()
                    + "*|* *| opened "
                    + storeFileSystemByNumber1.isOpen());
            FileSystem storeFileSystemByNumber2 = storeNew.getStoreFileSystemByNumber(11);
            System.out.println("*|"
                    + storeFileSystemByNumber2.toString()
                    + "*|* *| opened "
                    + storeFileSystemByNumber2.isOpen());
            storeNew.printAllList();

            storeNew.utilizeAllLists();
            System.out.println("*|*|* *|*|* *|*|* print utilized list of storages");
            storeNew.printAllList();
            
            tempRunNewConcept();
            System.out.println("end run *** *** *** tempRunNewConcept()");
        //runIndexMakeWordIntoZipByThreads();
        //runIndexMakeAndDirList();
        //outputToConsoleStrings();
        } catch(Throwable exTrow) {
            System.err.println(exTrow.getMessage());
            exTrow.printStackTrace();
        } finally {
            try {
                storeNew.utilizeAllLists();
            } catch(Throwable exTrowSecond) {
                System.err.println(exTrowSecond.getMessage());
                exTrowSecond.printStackTrace();
            }
        }
    }
    private static void tempRunNewConcept(){
        ThIndexRule thIndexRule = new ThIndexRule();
        
        AdilRule loggerRule = new AdilRule(thIndexRule);
        thIndexRule.setAdilRule(loggerRule);
        AdilState loggerState = new AdilState(loggerRule);
        loggerState.setTrueLogWithTrace();
        loggerRule.setAdilState(loggerState);
        AdilWorkerWrite loggerWorker = new AdilWorkerWrite(loggerRule);
        loggerRule.setAdilWorkWrite(loggerWorker);
        
        AdimRule ruleAdim = new AdimRule();
        ruleAdim.setAdilRule(loggerRule);
        
        AdibProcessCommand processCommandAdim = new AdibProcessCommand();
        ruleAdim.setAdibProcessCommand(processCommandAdim);
        AdifControlFlag controlFlag = new AdifControlFlag(15, ruleAdim);
        ruleAdim.setAdifControlFlag(controlFlag);
        
        processCommandAdim.commandPut(0, 1, 0);
        
        processCommandAdim.commandPut(1, 2, 0);
        
        processCommandAdim.commandPut(1, 3, 0);
        
        processCommandAdim.commandPut(0, 3, 0);
        
        processCommandAdim.commandPut(0, 1, 1);
        
        processCommandAdim.commandPut(1, 2, 1);
        
        processCommandAdim.commandPut(1, 3, 1);
        
        processCommandAdim.commandPut(0, 3, 1);
        
        processCommandAdim.commandPut(1, 7, 2);
        processCommandAdim.commandPut(1, 5, 2);
        
        processCommandAdim.commandPut(1, 9, 2);
        System.out.println("--- --- --- all commands in processCommand bus");
        AdibWorker workersTest = new AdibWorker(ruleAdim);
        workersTest.runAllWorker();
        loggerRule.runAdilWorkWrite();
        Integer countSleepRouter = 0;
        Integer countSleepFileListBuild = 0;
        Integer countSleepWordStorageWriter = 0;
        Integer countloggerWriter = 0;
        do {
            try {
                Thread currentThread = Thread.currentThread();
                currentThread.sleep(5000);
                countSleepRouter++;
                countSleepFileListBuild++;
                countSleepWordStorageWriter++;
                countloggerWriter++;
            } catch (InterruptedException ex){
                    ex.printStackTrace();
                    System.err.println(ex.getMessage());
            }
            if( countSleepRouter.equals(20) ){
                processCommandAdim.commandPut(1, 7, 3);
                System.out.println("send inDoBus for WordStorageRouter command CancelPauseFromUser");
            }
            if( countSleepFileListBuild.equals(950) ){
                processCommandAdim.commandPut(1, 5, 0);
                System.out.println("send inDoBus for FileListBuild command Start");
            }
            if( countSleepWordStorageWriter.equals(1550) ){
                processCommandAdim.commandPut(1, 9, 1);
                System.out.println("send inDoBus for WordStorageWriter command Stop");
            }
            if( countloggerWriter.equals(500) ){
                countloggerWriter = 0;
                loggerRule.runAdilWorkWrite();
            }
            System.out.println("workersTest.isHasRunnedWorkers() ".concat(String.valueOf(workersTest.isHasRunnedWorkers())));
        } while( workersTest.isHasRunnedWorkers() );
        
        /**
         * @todo add to logger linked transfer queue list all created names for thread
         * finished workWrite new procedure find in stack trace not finished threads
         * wait for finish and write log
         */
        loggerRule.runAdilWorkWrite();
    }
    private static void runIndexMakeAndDirList(){
        /**
         * if run DirListWork(Logic)Manager, get info about storages content
         * after than run need part... do it in the ThIndexManager
         */
        
        ThIndexState thIndexStateObj = new ThIndexState();
        ThDirListBusReaded thDirListBusReaded = new ThDirListBusReaded();
        thIndexStateObj.setBusJobForRead(thDirListBusReaded);
        ThIndexRule thIndexRule = new ThIndexRule();
        
        AdilRule loggerRule = new AdilRule(thIndexRule);
        thIndexRule.setAdilRule(loggerRule);
        AdilState loggerState = new AdilState(loggerRule);
        loggerRule.setAdilState(loggerState);
        AdilWorkerWrite loggerWorker = new AdilWorkerWrite(loggerRule);
        loggerRule.setAdilWorkWrite(loggerWorker);
        
        
        thIndexRule.setIndexState(thIndexStateObj);
        ThIndexMaker thIndexMaker = new ThIndexMaker(thIndexRule);
        ThIndexDirList thIndexDirList = new ThIndexDirList(thIndexRule);
        ThIndexWord thIndexWord = new ThIndexWord(thIndexRule);
        ThIndexFileList thIndexFileList = new ThIndexFileList(thIndexRule);
        ThIndexStorageWord thIndexStorageWord = new ThIndexStorageWord(thIndexRule);
        
        ThIndexStatistic thIndexStatistic = new ThIndexStatistic(thIndexRule);
        thIndexRule.setIndexStatistic(thIndexStatistic);
        
        thIndexRule.setThreadIndexMaker(thIndexMaker);
        thIndexRule.setThreadIndexDirList(thIndexDirList);
        thIndexRule.setThreadIndexWord(thIndexWord);
        
        /**
         * @todo when storage index create and not need for new create not run for this methods
         */
        //thIndexMaker.start();
        //waitForFinishedThread();
        /**
         * @todo append flag updated process, this ma used in while( updatedProcess ) { wait for end update }
         * after create storages workers... need release for storages (file systems) workers...
         */
        
        
        //thIndexStateObj.currentIndexStorages().updateMapForStorages();
        thIndexDirList.start();
        waitForFinishedIndexDirListThread(thIndexRule);
        
        
        thIndexFileList.start();
        thIndexStorageWord.start();
        thIndexWord.start();
    }
    /**
     * jobWalkerStorageType - job types:
     *  - - - scanNotLimited, typeWordStorage
     *  - - - scanLimited, typeWordStorage
     *  - - - scanAllFiles, typeWordStorage
     *  + + + not released in this bus version moveFilesDirectories, typeWordStorage
     *  - - - createDirectoryTypeWord, typeWordStorage
     */
    private static void outputToConsoleStrings(){

        System.out.println("isFlowInWriteBus - " + "isFlowInWriteBus".hashCode());
        
        System.out.println("isFlowInReadBus - " + "isFlowInReadBus".hashCode());
        System.out.println("isNeedDeleteOldFile - " + "isNeedDeleteOldFile".hashCode());
        
        System.out.println("isOldFileDeleted - " + "isOldFileDeleted".hashCode());
        System.out.println("deletedFileName - " + "deletedFileName".hashCode());
        System.out.println("flowFileNamePrefix - " + "flowFileNamePrefix".hashCode());
        System.out.println("CacheToLimitFileSystemLimit - " + "CacheToLimitFileSystemLimit".hashCode());
        System.out.println("FlagsProcess - " + "FlagsProcess".hashCode());
        
        System.out.println("countRecordsOnFileSystem - " + "countRecordsOnFileSystem".hashCode());
        System.out.println("volumeNumber - " + "volumeNumber".hashCode());
        System.out.println("currentFileName - " + "currentFileName".hashCode());
        System.out.println("newFileName - " + "newFileName".hashCode());
        System.out.println("lastAccessNanotime - " + "lastAccessNanotime".hashCode());
        System.out.println("countDataUseIterationsSummary - " + "countDataUseIterationsSummary".hashCode());
        System.out.println("currentInCache - " + "currentInCache".hashCode());
        System.out.println("addNeedToFileSystemLimit - " + "addNeedToFileSystemLimit".hashCode());
        System.out.println("indexSystemLimitOnStorage - " + "indexSystemLimitOnStorage".hashCode());
        System.out.println("isWriteProcess - " + "isWriteProcess".hashCode());
        System.out.println("isReadProcess - " + "isReadProcess".hashCode());
        System.out.println("isCachedData - " + "isCachedData".hashCode());
        System.out.println("isCalculatedData - " + "isCalculatedData".hashCode());
        System.out.println("isUdatedDataInHashMap - " + "isUdatedDataInHashMap".hashCode());
    }
    /*private static void runIndexMakeWordIntoZipByThreads(){
        
        //ThIndexManager thIndexManager = new ThIndexManager();
        
        //thIndexManager.start();
        //read data from dir list, after make word index write data to index storages
        ThIndexDirList thIndexDirList = new ThIndexDirList();
        thIndexDirList.start();
        try{
            thIndexDirList.join();
        } catch(InterruptedException ex){
            ex.printStackTrace();
        }
        //make word index
        ThIndexWord thIndexWord = new ThIndexWord();
        thIndexWord.start();
    }*/
    private static void waitForFinishedThread(){
        Thread currentThread = Thread.currentThread();
                
        Boolean eqNames = Boolean.FALSE;
        try{
            currentThread.sleep(60*1000);
            do{
                eqNames = Boolean.FALSE;
                
                Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
                for( Entry<Thread, StackTraceElement[]> stackItem : allStackTraces.entrySet() ){

                    if( stackItem.getKey().getName().toLowerCase().contains("IndexStorage".toLowerCase())){
                        eqNames = Boolean.TRUE;
                    }
                    if( stackItem.getKey().getName().toLowerCase().contains("DirlistReader".toLowerCase())){
                        eqNames = Boolean.TRUE;
                    }
                    if( stackItem.getKey().getName().toLowerCase().contains("DirlistTacker".toLowerCase())){
                        eqNames = Boolean.TRUE;
                    }
                    if( stackItem.getKey().getName().toLowerCase().contains("DirListPacker".toLowerCase())){
                        eqNames = Boolean.TRUE;
                    }
                    if( stackItem.getKey().getName().toLowerCase().contains("DirListWriter".toLowerCase())){
                        eqNames = Boolean.TRUE;
                    }

                }
                currentThread.sleep(5*1000);

                System.out.println(" _|_|_|_|_ wait for index process finished _|_|_|_|_-+-+-+-+|+-+|+-");
            } while (eqNames);
        } catch (InterruptedException ex){
                ex.printStackTrace();
                System.out.println(ex.getMessage());
        }
    }
    private static void waitForFinishedIndexDirListThread(ThIndexRule thIndexRuleOuter){
        Thread currentThread = Thread.currentThread();
                
        Boolean eqNames = Boolean.FALSE;
        try{
            currentThread.sleep(60*1000);
            do{
                eqNames = Boolean.FALSE;
                Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
                for( Entry<Thread, StackTraceElement[]> stackItem : allStackTraces.entrySet() ){
                    ArrayBlockingQueue<String> queueThreadNames = thIndexRuleOuter.getQueueThreadNames();
                    for(String itemOfThredName : queueThreadNames){
                        if( stackItem.getKey().getName().toLowerCase().contains(itemOfThredName.toLowerCase())){
                            eqNames = Boolean.TRUE;
                            System.out.println(" _|_|_|_|_ wait for thread " 
                                    + itemOfThredName 
                                    + " finished _|_|_|_|_-+-+-+-+|+-+|+-");
                        }
                    }
                }
                currentThread.sleep(5*1000);
            } while (eqNames);
        } catch (InterruptedException ex){
                ex.printStackTrace();
                System.out.println(ex.getMessage());
        }
    }
    /*private static String runIndexMakeIntoZipByThreads(){
        ThIndexMaker thIndexMaker = new ThIndexMaker();
        thIndexMaker.setName(UUID.randomUUID().toString());
        thIndexMaker.start();
        String nameThreadIndex = thIndexMaker.getName();
        return nameThreadIndex;
    }*/
    protected static void logInitState(AppThManager outerAppThManager){
        AppObjectsList objectsForApp = outerAppThManager.getListOfObjects();
        
        String strForPut = new String("start Application");
        objectsForApp.putLogMessageInfo(strForPut);
        objectsForApp.putLogMessageInfo("[RUN]System.getenv()");
        for (Map.Entry<String,String> arg : System.getenv().entrySet()) {
            String key = arg.getKey();
            String value = arg.getValue();
            objectsForApp.putLogMessageInfo("[KEY]" + key + "[VALUE]" + value);
        }
        objectsForApp.putLogMessageInfo("[RUN]System.getProperties().stringPropertyNames()[FOR]System.getProperty(namesKey)");
        try{
            Properties properties = System.getProperties();
        } catch(AccessControlException exAC){
            exAC.printStackTrace();
        }
        Properties properties = new Properties();
        
        for (Object namesKey : properties.keySet()) {
            String strKeys = new String((String) namesKey.toString());
            String value = System.getProperty(strKeys);
            objectsForApp.putLogMessageInfo("[KEY]" + strKeys + "[VALUE]" + value);
        }
        SecurityManager securityManager = System.getSecurityManager();
        if( securityManager == null ){
            objectsForApp.putLogMessageInfo("[RUN]System.getSecurityManager()[VALUE]NULL");
        } else{
            String toString = securityManager.getSecurityContext().toString();
            objectsForApp.putLogMessageInfo("[RUN]securityManager.getSecurityContext().toString()[VALUE]" + toString);
        }
        String threadInfoToString = NcAppHelper.getThreadInfoToString(Thread.currentThread());
        objectsForApp.putLogMessageInfo("[RUN]NcAppHelper.getThreadInfoToString(Thread.currentThread())[VALUE]" + threadInfoToString);
        String classInfoToString = NcAppHelper.getClassInfoToString(objectsForApp.getClass());
        objectsForApp.putLogMessageInfo("[RUN]NcAppHelper.getClassInfoToString(obectsForApp.getClass())[VALUE]" + classInfoToString);

        objectsForApp.putLogMessageInfo(strForPut);
        //obectsForApp.doLogger();
        
        /**
         * @todo code for finish and release all created resurses
         * 
         * 
         * 
         * see state of threads
        for ( Map.Entry<String, Thread> workerElement : obectsForApp.getWorkerList().entrySet() ){
            System.out.println(workerElement.getValue().getState().toString());
        }
         * treminaion threads
        for ( Map.Entry<String, Thread> workerElement : obectsForApp.getWorkerList().entrySet() ){
            
            try{
                workerElement.getValue().getState().notify(); 
                workerElement.getValue().wait();
            } catch (InterruptedException ex){
                String interruptedThreadInfoToString = NcAppHelper.getThreadInfoToString(workerElement.getValue());
                System.out.println(interruptedThreadInfoToString
                        + "[InterruptedException]" + ex.getMessage());
                ex.printStackTrace();
            }
            //workerElement.getValue().getThreadGroup().destroy();
        }
        */
        
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
