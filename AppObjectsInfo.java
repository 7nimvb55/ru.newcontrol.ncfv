/*
 * Copyright 2018 wladimirowichbiaran.
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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.nio.file.Path;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppObjectsInfo {
    protected static String getThreadInfoToString(Thread forStrBuild){
        ThreadGroup threadGroup = forStrBuild.getThreadGroup();
        String nameThreadGroup = threadGroup.getName();
        int activeCountThreadGroup = threadGroup.activeCount();
        int activeGroupCount = threadGroup.activeGroupCount();
        Class<?> aClass = forStrBuild.getClass();
        return NcStrLogMsgField.INFO.getStr()
                    + NcStrLogMsgField.THREAD_GROUP_NAME.getStr()
                    + nameThreadGroup
                    + NcStrLogMsgField.ACTIVE.getStr()        
                    + NcStrLogMsgField.COUNT.getStr()
                    + String.valueOf(activeCountThreadGroup)
                    + NcStrLogMsgField.ACTIVE.getStr()
                    + NcStrLogMsgField.GROUP.getStr()
                    + NcStrLogMsgField.COUNT.getStr()
                    + String.valueOf(activeGroupCount)
                    + NcStrLogMsgField.THREAD.getStr()
                    + NcStrLogMsgField.ID.getStr()
                    + String.valueOf(forStrBuild.getId())
                    + NcStrLogMsgField.PRIORITY.getStr()        
                    + String.valueOf(forStrBuild.getPriority())
                    + NcStrLogMsgField.NAME.getStr()
                    + forStrBuild.getName()
                    + NcStrLogMsgField.CANONICALNAME.getStr()
                    + aClass.getCanonicalName()
                    + NcStrLogMsgField.GENERICSTRING.getStr()
                    + aClass.toGenericString();
    }
    protected static String getClassInfoToString(Class<?> forStrBuild){
        return NcStrLogMsgField.INFO.getStr()
            + NcStrLogMsgField.CLASSNAME.getStr()
            + forStrBuild.getName()
            + NcStrLogMsgField.TYPENAME.getStr()
            + forStrBuild.getTypeName()
            + NcStrLogMsgField.CANONICALNAME.getStr()
            + forStrBuild.getCanonicalName()
            + NcStrLogMsgField.GENERICSTRING.getStr()
            + forStrBuild.toGenericString();
    }
    protected static void outCreateObjectMessage(String strMsg, Class<?> forStrBuild){
        String classInfoToString = NcAppHelper.getClassInfoToString(forStrBuild);
            NcAppHelper.outMessage( NcStrLogMsgField.INFO.getStr()
                    + NcStrLogMsgField.CREATE.getStr()
                    + strMsg
                    + classInfoToString);
    }
    protected static TreeMap<Long, String> getThreadStackTraceToString(Thread t){
        String strTimeAndMsg = AppFileOperationsSimple.getNowTimeStringWithMS();
        TreeMap<Long, String> strForLog = new TreeMap<Long, String>();


        StackTraceElement[] nowT = t.getStackTrace();
        long idx = 0;
        strForLog.put(idx, strTimeAndMsg);
        idx++;
        String strThread = NcStrLogMsgField.THREAD.getStr()
        + NcStrLogMsgField.COUNT.getStr()
        + Thread.activeCount()
        + NcStrLogMsgField.THREAD_GROUP_NAME.getStr()
        + t.getThreadGroup().getName()
        + NcStrLogMsgField.ACTIVE.getStr()        
        + NcStrLogMsgField.COUNT.getStr()
        + t.getThreadGroup().activeCount()
        + NcStrLogMsgField.ACTIVE.getStr()
        + NcStrLogMsgField.GROUP.getStr()
        + NcStrLogMsgField.COUNT.getStr()
        + t.getThreadGroup().activeGroupCount();
        strForLog.put(idx, strThread);
        idx++;
        String strLoader = NcStrLogMsgField.CLASSLOADER.getStr()
            + NcStrLogMsgField.CANONICALNAME.getStr()
            + t.getContextClassLoader().getClass().getCanonicalName();
        strForLog.put(idx, strLoader);
        idx++;
        strForLog.put(idx, NcStrLogMsgField.THREAD.getStr()
            + NcStrLogMsgField.TOSTRING.getStr()
            + t.toString());
        idx++;
        strForLog.put(idx, NcStrLogMsgField.THREAD.getStr()
            + NcStrLogMsgField.NAME.getStr()
            + t.getName());
        idx++;
        strForLog.put(idx, NcStrLogMsgField.THREAD.getStr()
            + NcStrLogMsgField.CANONICALNAME.getStr()
            + t.getClass().getCanonicalName());
        idx++;
        strForLog.put(idx, NcStrLogMsgField.THREAD.getStr()
                + NcStrLogMsgField.ID.getStr() + t.getId());
        idx++;
        strForLog.put(idx, NcStrLogMsgField.THREAD.getStr()
            + NcStrLogMsgField.STATE.getStr()
            + NcStrLogMsgField.NAME.getStr() + t.getState().name());
        idx++;
        String strTrace = "";
        int stackIdx = 0;
        for(StackTraceElement itemT : nowT ){
            if( stackIdx > 1
                || NcfvRunVariables.isOutToLogFileTraceWithPrintFunc() ){

                String strOutFile = "";
                if( NcfvRunVariables.isOutToLogFileIncludeFile() ){

                    strOutFile = NcStrLogMsgField.FILENAME.getStr()
                        + itemT.getFileName();
                }
                String strOut = 
                    NcStrLogMsgField.CLASSNAME.getStr()
                    + itemT.getClassName()
                    + NcStrLogMsgField.METHODNAME.getStr()
                    + itemT.getMethodName()
                    + NcStrLogMsgField.LINENUM.getStr()
                    + itemT.getLineNumber()
                    + (itemT.isNativeMethod()
                        ? NcStrLogMsgField.NATIVE.getStr() : "");

                strTrace = NcStrLogMsgField.ELEMENTNUM.getStr()
                        + stackIdx + strOutFile + strOut;
                stackIdx++;
            }
            if( strTrace.length() > 0 ){

                strForLog.put(idx, strTrace);
            }
            strTrace = "";
            idx++;
        }
       
        return strForLog;   
    }
    
    protected static ArrayList<String> getAllStack(){
        ArrayList<String> listStrToRet = new ArrayList<String>();
        
        Map<Thread, StackTraceElement[]> allStackTraces = 
                Thread.getAllStackTraces();
        for (Map.Entry<Thread, StackTraceElement[]> allStackTrace
                : allStackTraces.entrySet()) {
            listStrToRet.addAll(
                    getThreadInfo(allStackTrace.getKey()));
            listStrToRet.addAll(
                    getStackTraceInfo(allStackTrace.getValue()));
        }
        return listStrToRet;
    }
    private static ArrayList<String> getThreadInfo(Thread inFuncThread){
        ArrayList<String> listStrToRet = new ArrayList<String>();
        String strToOut = "";
        long id = inFuncThread.getId();
        String name = inFuncThread.getName();
        int priority = inFuncThread.getPriority();
        String stateName = inFuncThread.getState().name();
        strToOut = NcStrLogMsgField.THREAD.getStr()
                + NcStrLogMsgField.ID.getStr()
                + Long.toString(id)
                + NcStrLogMsgField.NAME.getStr()
                + name
                + NcStrLogMsgField.PRIORITY.getStr()
                + Integer.toString(priority)
                + NcStrLogMsgField.STATE.getStr()
                + NcStrLogMsgField.NAME.getStr()
                + stateName;
        listStrToRet.add(strToOut);
        ThreadGroup threadGroup = inFuncThread.getThreadGroup();
        listStrToRet.addAll(getThreadGroupInfo(threadGroup));
        return listStrToRet;
    }
    private static ArrayList<String> getThreadGroupInfo(ThreadGroup inFuncThreadGroup){
        ArrayList<String> listStrToRet = new ArrayList<String>();
        String strToOut = "";
        int activeCount = inFuncThreadGroup.activeCount();
        int activeGroupCount = inFuncThreadGroup.activeGroupCount();
        int maxPriority = inFuncThreadGroup.getMaxPriority();
        String name = inFuncThreadGroup.getName();
        boolean daemon = inFuncThreadGroup.isDaemon();
        String strDaemon = daemon ? "true" : "false";
        boolean destroyed = inFuncThreadGroup.isDestroyed();
        String strDestroyed = destroyed ? "true" : "false";
        strToOut = NcStrLogMsgField.THREAD_GROUP.getStr()
                + NcStrLogMsgField.NAME.getStr()
                + name
                + NcStrLogMsgField.MAX.getStr()
                + NcStrLogMsgField.PRIORITY.getStr()
                + Integer.toString(maxPriority)
                + NcStrLogMsgField.ACTIVE.getStr()
                + NcStrLogMsgField.COUNT.getStr()
                + Integer.toString(activeCount)
                + NcStrLogMsgField.ACTIVE.getStr()
                + NcStrLogMsgField.GROUP.getStr()
                + NcStrLogMsgField.COUNT.getStr()
                + Integer.toString(activeGroupCount)
                + NcStrLogMsgField.IS.getStr()
                + NcStrLogMsgField.DAEMON.getStr()
                + strDaemon
                + NcStrLogMsgField.IS.getStr()
                + strDestroyed
                + NcStrLogMsgField.DESTROYED.getStr();
        
        listStrToRet.add(strToOut);
        
        return listStrToRet;
    }
    private static ArrayList<String> getStackTraceInfo(StackTraceElement[] inFuncStackTrace){
        ArrayList<String> listStrToRet = new ArrayList<String>();
        String strToOut = "";
        int idx = 0;
        String strToOutPref = NcStrLogMsgField.STACK.getStr()
            + NcStrLogMsgField.TRACE.getStr()
            + NcStrLogMsgField.ELEMENT.getStr();
        for (StackTraceElement stackItem : inFuncStackTrace) {

            Class<?> classItem = stackItem.getClass();
            strToOut = strToOutPref
                    + NcStrLogMsgField.NUM.getStr() 
                    + idx
                    + NcStrLogMsgField.CLASSNAME.getStr()
                    + stackItem.getClassName();
            listStrToRet.add(strToOut);
            
            ArrayList<String> declMeth = getDeclaredMethodsInfo(classItem);
            for (String strMeth : declMeth) {
                strToOut = strToOutPref
                    + NcStrLogMsgField.NUM.getStr() 
                    + idx
                    + strMeth;
                listStrToRet.add(strToOut);
            }
            
            ArrayList<String> declField = getDeclaredFieldsInfo(classItem);
            for (String strField : declField) {
                strToOut = strToOutPref
                    + NcStrLogMsgField.NUM.getStr() 
                    + idx
                    + strField;
                listStrToRet.add(strToOut);
            }
            
            idx++;
        }
        return listStrToRet;
    }
    private static ArrayList<String> getDeclaredMethodsInfo(Class<?> classInFunc){
        ArrayList<String> listStrToRet = new ArrayList<String>();
        String strToOut = "";
        Method[] declaredMethods = classInFunc.getClass().getDeclaredMethods();
        int methodIdx = 0;
        for (Method declaredMethod : declaredMethods) {
            String strName = declaredMethod.getName();
            strToOut = NcStrLogMsgField.METHOD.getStr()
                + NcStrLogMsgField.NUM.getStr()
                + Integer.toString(methodIdx)
                + NcStrLogMsgField.NAME.getStr()
                + strName;
            listStrToRet.add(strToOut);
            Parameter[] parameters = declaredMethod.getParameters();
            int paramIdx = 0;
            for (Parameter parameter : parameters) {
                String paramName = parameter.getName();
                String paramType = parameter.getType().getCanonicalName();
                strToOut = NcStrLogMsgField.PARAMETER.getStr()
                + NcStrLogMsgField.NUM.getStr()
                + Integer.toString(paramIdx)
                + NcStrLogMsgField.NAME.getStr()
                + paramName
                + NcStrLogMsgField.TYPE.getStr()
                + paramType;
                paramIdx++;
            }
            methodIdx++;
        }
        return listStrToRet;
    }
    private static ArrayList<String> getDeclaredFieldsInfo(Class<?> classInFunc){
        ArrayList<String> listStrToRet = new ArrayList<String>();
        String strToOut = "";
        int fieldIdx = 0;
        Field[] declaredFields = classInFunc.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            strToOut = NcStrLogMsgField.FIELD.getStr()
                + NcStrLogMsgField.NUM.getStr()
                + fieldIdx;
            try {
                boolean boolAccValFlag = declaredField.isAccessible();
                declaredField.setAccessible(true);
                strToOut = strToOut
                    + NcStrLogMsgField.TYPE.getStr()
                    + declaredField.getType().getCanonicalName();
                
                strToOut = strToOut
                    + NcStrLogMsgField.NAME.getStr()
                    + declaredField.getName();
                
                strToOut = strToOut
                    + NcStrLogMsgField.VALUE.getStr()
                    + declaredField.get(classInFunc.getClass()).toString();
                declaredField.setAccessible(boolAccValFlag);
            } catch (IllegalAccessException | IllegalArgumentException | SecurityException ex){
                strToOut = strToOut
                    + NcStrLogMsgField.EXCEPTION_MSG.getStr()
                    + ex.getMessage();
            }
            listStrToRet.add(strToOut);
            fieldIdx++;
        }
        return listStrToRet;
    }
    protected static ArrayList<String> getStringsToJSFile(){
        ArrayList<String> strToFile = new ArrayList<String>();
        strToFile.add("body{");
        strToFile.add("background-color: #666666;");
        strToFile.add("}");
        strToFile.add("frame-table{");
        strToFile.add("border: 1px solid #d6e9c6;");
        strToFile.add("}");
        return strToFile;
    }
    protected static ArrayList<String> getStringsToCSSFile(){
        ArrayList<String> strToFile = new ArrayList<String>();
        strToFile.add("body{");
        strToFile.add("background-color: #666666;");
        strToFile.add("}");
        strToFile.add("frame-table{");
        strToFile.add("border: 1px solid #d6e9c6;");
        strToFile.add("}");
        return strToFile;
    }
    protected static void getThreadDebugInfoToHtml(Thread readedThread){
        String nowTimeStringWithMS = 
                AppFileOperationsSimple.getNowTimeStringWithMS();
        Path logForHtmlCurrentLogSubDir = 
                AppFileOperationsSimple.getLogForHtmlCurrentLogSubDir(nowTimeStringWithMS);
        ConcurrentSkipListMap<String, Path> newLogFileInLogHTML = 
                AppFileOperationsSimple.getNewLogFileInLogHTML(logForHtmlCurrentLogSubDir);
        newLogFileInLogHTML.put(AppFileNamesConstants.LOG_HTML_KEY_FOR_CURRENT_SUB_DIR, logForHtmlCurrentLogSubDir);
        
        TreeMap<Integer, String> listForLogStrs = new TreeMap<Integer, String>();
        ConcurrentSkipListMap<Integer, String> listForRunnableLogStrs = new ConcurrentSkipListMap<Integer, String>();
        Path newLogHtmlTableFile = newLogFileInLogHTML.get(AppFileNamesConstants.LOG_HTML_TABLE_PREFIX);
        AppLoggerToHTMLRunnable loggerToHtml = new AppLoggerToHTMLRunnable(
                listForRunnableLogStrs,
                newLogHtmlTableFile
        );
        
        
        //@todo chaos to system out
        
        
        int indexLinesToFile = 0;
        

        
        indexLinesToFile++;
        listForLogStrs.put(indexLinesToFile,
                "[NAME]" + readedThread.getName()
                + "[CLASS]" + readedThread.getClass().getCanonicalName()
                + "[isInstanceOf(AppThWorkDirListRun.class)]" 
                + readedThread.getClass().isInstance(AppThWorkDirListRun.class));
        
        indexLinesToFile++;
        listForLogStrs.put(indexLinesToFile,"* * * [Thread.getContextClassLoader()] * * *");
        indexLinesToFile++;
        listForLogStrs.put(indexLinesToFile,"[getContextClassLoader()]" 
                + readedThread.getContextClassLoader().getClass().getCanonicalName());
        indexLinesToFile++;
        listForLogStrs.put(indexLinesToFile,"[getUncaughtExceptionHandler()]" 
                + readedThread.getUncaughtExceptionHandler().getClass().getCanonicalName());
        
        indexLinesToFile++;
        listForLogStrs.put(indexLinesToFile,"* * * [Thread.getClass().getClasses()] * * *");
        String strCanonicalNames = "";
        int idxId = 0;
        Class<?>[] classes = readedThread.getClass().getClasses();
        for( Class<?> str : classes ){
            strCanonicalNames = strCanonicalNames + "[" + idxId + "]" + str.getCanonicalName();
            idxId++;
        }
        indexLinesToFile++;
        listForLogStrs.put(indexLinesToFile,"[getClasses()]" 
                + strCanonicalNames);
        
        indexLinesToFile++;
        listForLogStrs.put(indexLinesToFile,"* * * [Thread.getStackTrace()] * * *");
        strCanonicalNames = "";
        idxId = 0;
        StackTraceElement[] traceElements = readedThread.getStackTrace();
        
        for(StackTraceElement traceElement : traceElements ){
            strCanonicalNames = strCanonicalNames + "[" + idxId + "]getClass().getCanonicalName()]" + traceElement.getClass().getCanonicalName();
            
            idxId++;
        }
        indexLinesToFile++;
        listForLogStrs.put(indexLinesToFile,"[getStackTrace().length]"
                + traceElements.length
                + "[getStackTrace()]" 
                + strCanonicalNames);
        
        indexLinesToFile++;
        listForLogStrs.put(indexLinesToFile,"* * * [Thread.getContextClassLoader().getClass().getClasses()] * * *");
        Class<?>[] classesContextClassLoader = readedThread.getContextClassLoader().getClass().getClasses();
        for( Class<?> str : classesContextClassLoader ){
            strCanonicalNames = strCanonicalNames + "[" + idxId + "]" + str.getCanonicalName();
            idxId++;
        }
        
        
        indexLinesToFile++;
        listForLogStrs.put(indexLinesToFile,"[getContextClassLoader().getClass().getClasses()]" 
                + strCanonicalNames);
        
        indexLinesToFile++;
        listForLogStrs.put(indexLinesToFile,"* * * [Thread.getName() | .getClass() and some methods] * * *");
        indexLinesToFile++;
        listForLogStrs.put(indexLinesToFile,
                "[NAME]" + readedThread.getName()
                + "[CLASS][getName]" + readedThread.getClass().getName()
                + "[CLASS][getCanonicalName]" + readedThread.getClass().getCanonicalName()
                + "[CLASS][getSimpleName]" + readedThread.getClass().getSimpleName()
                + "[CLASS][getTypeName]" + readedThread.getClass().getTypeName()
                + "[CLASS][toGenericString]" + readedThread.getClass().toGenericString()
                + "[CLASS][toString]" + readedThread.getClass().toString()
                        
                + "[CLASS][getClass().isAssignableFrom(AppThWorkDirListRun.class)]"
                + readedThread.getClass().isAssignableFrom(AppThWorkDirListRun.class)
                + "[CLASS][getClass().isInstance(AppThWorkDirListRun.class)]"
                        
                + readedThread.getClass().isInstance(AppThWorkDirListRun.class));
        indexLinesToFile++;
        /*int indexForRunnableList = 0;
        listForRunnableLogStrs.put(indexForRunnableList,"<TABLE>");
        indexForRunnableList++;
        for( Map.Entry<Integer, String> lines: listForLogStrs.entrySet()){
            
                
                listForRunnableLogStrs.put(indexForRunnableList, "<tr>" + lines.getValue() + "</tr>");
                indexForRunnableList++;
            
        }
        listForRunnableLogStrs.put(indexForRunnableList,"</TABLE>");*/
        listForRunnableLogStrs = getStringListForSaveTable(listForRunnableLogStrs, listForLogStrs, "readedThread.getStackTrace()");
        Thread logToHtmlTable = new Thread(loggerToHtml);
        logToHtmlTable.start();
        
        try{
            logToHtmlTable.join();
            while( !loggerToHtml.isJobDone() ){
                Thread curThr = Thread.currentThread();
                curThr.sleep(50);
            }
        } catch(InterruptedException ex){
            ex.printStackTrace();
        } catch(SecurityException ex){
            ex.printStackTrace();
        }
        //**************
        newLogHtmlTableFile = AppFileOperationsSimple.getNewLogHtmlTableFile(logForHtmlCurrentLogSubDir);
        while( !loggerToHtml.isLogFileNameChanged() ){
            loggerToHtml.setNewLogFileName(newLogHtmlTableFile);
        }
        listForRunnableLogStrs.clear();
        
        //**************
        listForLogStrs.clear();
        listForLogStrs = new TreeMap<Integer, String>();
        //listForLogStrs.clear();
        
        
        // all methods from Thread objects
        
        //readedThread.getClass().asSubclass(clazz);//Class<? extends U> 
        //readedThread.getClass().cast(idxId);//? extends Thread
        boolean desiredAssertionStatus = readedThread.getClass().desiredAssertionStatus();
        //boolean equals = readedThread.getClass().equals(idxId);
        indexLinesToFile = 0;
        AnnotatedType[] annotatedInterfaces = readedThread.getClass().getAnnotatedInterfaces();
        for(AnnotatedType element : annotatedInterfaces){
            indexLinesToFile++;
            listForLogStrs.put(indexLinesToFile,
                "getAnnotatedInterfaces()[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        
        AnnotatedType annotatedSuperclass = readedThread.getClass().getAnnotatedSuperclass();
        //readedThread.getClass().getAnnotation(annotationClass); //A
        Annotation[] annotations = readedThread.getClass().getAnnotations();
        for(Annotation element : annotations){
            indexLinesToFile++;
            listForLogStrs.put(indexLinesToFile,
                "getAnnotations()[CLASS][getCanonicalName]" + element.annotationType().getClass().getCanonicalName());
        }
        
        //readedThread.getClass().getAnnotationsByType(annotationClass); //A[]
        String canonicalName = readedThread.getClass().getCanonicalName();
        
        //readedThread.getClass().getClass().getClass();//Class<?>
        //readedThread.getClass().getClassLoader();//ClassLoader
        
        
        
        Class<?>[] classes1 = readedThread.getClass().getClasses();
        for(Class element : classes1){
            indexLinesToFile++;
            listForLogStrs.put(indexLinesToFile,
                "getClasses()[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        //readedThread.getClass().getComponentType();//Class<?>
        
        //readedThread.getClass().getConstructor(classes);//Constructor <? extends Thread>
        
        Constructor<?>[] constructors = readedThread.getClass().getConstructors();
        for(Constructor element : constructors){
            indexLinesToFile++;
            listForLogStrs.put(indexLinesToFile,
                "getConstructors()[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        //readedThread.getClass().getDeclaredAnnotation(annotationClass);//A
        
        Annotation[] declaredAnnotations = readedThread.getClass().getDeclaredAnnotations();
        for(Annotation element : declaredAnnotations){
            indexLinesToFile++;
            listForLogStrs.put(indexLinesToFile,"getDeclaredAnnotations()[CLASS]"
                + "[toString()]" + element.toString()
                + "[annotationType().getCanonicalName()]" + element.annotationType().getCanonicalName()
                + "[getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        //readedThread.getClass().getDeclaredAnnotationsByType(annotationClass);//A[]
        
        Class<?>[] declaredClasses = readedThread.getClass().getDeclaredClasses();
        for(Class element : declaredClasses){
            indexLinesToFile++;
            listForLogStrs.put(indexLinesToFile,"getDeclaredClasses()[CLASS]"
                + "[getName()]" + element.getName()
                + "[getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        //readedThread.getClass().getDeclaredConstructor(declaredClasses);//Constructor <? extends Thread>
        
        Constructor<?>[] declaredConstructors = readedThread.getClass().getDeclaredConstructors();
        for(Constructor element : declaredConstructors){
            indexLinesToFile++;
            listForLogStrs.put(indexLinesToFile,"getDeclaredConstructors()[CLASS]"
                + "[getName()]" + element.getName()
                + "[getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        //readedThread.getClass().getDeclaredField(canonicalName);//Field
        
        Field[] declaredFields = readedThread.getClass().getDeclaredFields();
        for(Field element : declaredFields){
            indexLinesToFile++;
            listForLogStrs.put(indexLinesToFile,"getDeclaredFields()[CLASS]"
                + "[getName()]" + element.getName()
                + "[getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        //readedThread.getClass().getDeclaredMethod(canonicalName, declaredClasses);//Method
        
        Method[] declaredMethods = readedThread.getClass().getDeclaredMethods();
        for(Method element : declaredMethods){
            indexLinesToFile++;
            listForLogStrs.put(indexLinesToFile,"getDeclaredMethods()[CLASS]"
                + "[getName()]" + element.getName()
                + "[getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        Class<?> declaringClass = readedThread.getClass().getDeclaringClass(); //Class<?>
        Class<?> enclosingClass = readedThread.getClass().getEnclosingClass(); //Class<?>
        Constructor<?> enclosingConstructor = readedThread.getClass().getEnclosingConstructor(); //Constructor<?>
        Method enclosingMethod = readedThread.getClass().getEnclosingMethod(); //Method
        //readedThread.getClass().getEnumConstants();//? extends Thread[]
        
        
        Field[] fields = readedThread.getClass().getFields();
        for(Field element : fields){
            indexLinesToFile++;
            listForLogStrs.put(indexLinesToFile,
                "getFields()[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        
        Type[] genericInterfaces = readedThread.getClass().getGenericInterfaces();
        for(Type element : genericInterfaces){
            indexLinesToFile++;
            listForLogStrs.put(indexLinesToFile,
                "getGenericInterfaces()[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        Type genericSuperclass = readedThread.getClass().getGenericSuperclass(); //Type
        
        Class<?>[] interfaces = readedThread.getClass().getInterfaces();
        for(Class element : interfaces){
            indexLinesToFile++;
            listForLogStrs.put(indexLinesToFile,
                "getInterfaces()[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        //readedThread.getClass().getMethod(canonicalName, interfaces);//Method
        
        Method[] methods = readedThread.getClass().getMethods();
        for(Method element : methods){
            indexLinesToFile++;
            listForLogStrs.put(indexLinesToFile,
                "getMethods()[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        
        
        int modifiers = readedThread.getClass().getModifiers();
        String name = readedThread.getClass().getName();
        Package aPackage = readedThread.getClass().getPackage();
        ProtectionDomain protectionDomain = readedThread.getClass().getProtectionDomain();
        readedThread.getClass().getResource(canonicalName);//URL
        
        readedThread.getClass().getResourceAsStream(name);//InputStream
        
        
        
        Object[] signers = readedThread.getClass().getSigners();
        if( signers != null  ){
            for(Object element : signers){
                if( element != null ){
                    indexLinesToFile++;
                    listForLogStrs.put(indexLinesToFile,
                        "getSigners()[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
                }
            }
        }
        
        
        String simpleName = readedThread.getClass().getSimpleName();
        Class<?> superclass = readedThread.getClass().getSuperclass();
        String typeName = readedThread.getClass().getTypeName();
        
        
        
        TypeVariable<? extends Class<? extends Thread>>[] typeParameters = readedThread.getClass().getTypeParameters();
        for(TypeVariable<? extends Class<? extends Thread>> element : typeParameters){
            indexLinesToFile++;
            listForLogStrs.put(indexLinesToFile,
                "getTypeParameters()[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        readedThread.getClass().hashCode();
        boolean annotation = readedThread.getClass().isAnnotation();
        //readedThread.getClass().isAnnotationPresent(annotationClass);//Boolean
        boolean anonymousClass = readedThread.getClass().isAnonymousClass();
        boolean array = readedThread.getClass().isArray();
        //readedThread.getClass().isAssignableFrom(cls);
        boolean aEnum = readedThread.getClass().isEnum();
        boolean aInterface = readedThread.getClass().isInterface();
        boolean localClass = readedThread.getClass().isLocalClass();
        boolean memberClass = readedThread.getClass().isMemberClass();
        boolean primitive = readedThread.getClass().isPrimitive();
        boolean synthetic = readedThread.getClass().isSynthetic();
        try{
            Thread newInstance = readedThread.getClass().newInstance(); //? extends Thread
        } catch(IllegalAccessException ex){
            ex.getMessage();
            ex.printStackTrace();
        } catch(InstantiationException ex){
            ex.getMessage();
            ex.printStackTrace();
        }
        readedThread.getClass().toGenericString();
        readedThread.getClass().toString();
        
        listForRunnableLogStrs.clear();
        /*indexForRunnableList = 0;
        listForRunnableLogStrs.put(indexForRunnableList,"<TABLE>");
        indexForRunnableList++;
        for( Map.Entry<Integer, String> lines: listForLogStrs.entrySet()){
            
                
                listForRunnableLogStrs.put(indexForRunnableList, "<tr>" + lines.getValue() + "</tr>");
                indexForRunnableList++;
            
        }
        listForRunnableLogStrs.put(indexForRunnableList,"</TABLE>");*/
        listForRunnableLogStrs = getStringListForSaveTable(listForRunnableLogStrs, listForLogStrs, "readedThread.getClass().getTypeParameters()");
        
        logToHtmlTable = new Thread(loggerToHtml);
        logToHtmlTable.start();
        
        try{
            logToHtmlTable.join();
            while( !loggerToHtml.isJobDone() ){
                Thread curThr = Thread.currentThread();
                curThr.sleep(50);
            }
        } catch(InterruptedException ex){
            ex.printStackTrace();
        } catch(SecurityException ex){
            ex.printStackTrace();
        }
        //**************
        //readedThread.getClass().notify();
        //readedThread.getClass().notifyAll();
        //readedThread.getClass().wait();
        //readedThread.getClass().wait(idxId);
        //readedThread.getClass().wait(idxId, idxId);
        ConcurrentSkipListMap<Integer, String> generateIndexFile = generateIndexFile(newLogFileInLogHTML);
        if( generateIndexFile.size() > 0 ){
            newLogHtmlTableFile = newLogFileInLogHTML.get(AppFileNamesConstants.LOG_INDEX_PREFIX);
            while( !loggerToHtml.isLogFileNameChanged() ){
                loggerToHtml.setNewLogFileName(newLogHtmlTableFile);
            }
            listForRunnableLogStrs.clear();
            for(Map.Entry<Integer, String> elementForLog : generateIndexFile.entrySet()){
                listForRunnableLogStrs.put(elementForLog.getKey(), elementForLog.getValue());
            }
            logToHtmlTable = new Thread(loggerToHtml);
            logToHtmlTable.start();

            try{
                logToHtmlTable.join();
                while( !loggerToHtml.isJobDone() ){
                    Thread curThr = Thread.currentThread();
                    curThr.sleep(50);
                }
            } catch(InterruptedException ex){
                ex.printStackTrace();
            } catch(SecurityException ex){
                ex.printStackTrace();
            }
        }
            
    }
    
    protected static ConcurrentSkipListMap<Integer, String> generateIndexFile(ConcurrentSkipListMap<String, Path> newLogFileInLogHTML){
        Path dirForRead = newLogFileInLogHTML.get(AppFileNamesConstants.LOG_HTML_KEY_FOR_CURRENT_SUB_DIR);
        ArrayList<Path> filesByMaskFromDir = AppFileOperationsSimple.getFilesByMaskFromDir(
                dirForRead,
                "{" + AppFileNamesConstants.LOG_HTML_TABLE_PREFIX + "}*");
        ConcurrentSkipListMap<Integer, String> readedLinesFromLogHTML = new ConcurrentSkipListMap<Integer, String>();
        TreeMap<Path, TreeMap<Integer, String>> filePathlinesFromReadedHtmlTable = 
                new TreeMap<Path, TreeMap<Integer, String>>();
        if( filesByMaskFromDir.size() > 0 ){
            TreeMap<Integer, String> linesFromReadedHtmlTable = new TreeMap<Integer, String>();
            Path forFirstRead = filesByMaskFromDir.get(0);
            AppLoggerFromHTMLRunnable readerFromHtmlFile = new AppLoggerFromHTMLRunnable(
                    readedLinesFromLogHTML,
                    forFirstRead);
            Thread readFromHtmlTable = new Thread(readerFromHtmlFile);
            readFromHtmlTable.start();

            try{
                readFromHtmlTable.join();
                while( !readerFromHtmlFile.isJobDone() ){
                    Thread curThr = Thread.currentThread();
                    curThr.sleep(50);
                }
                
                Map.Entry<Integer, String> pollFirstEntry;
                do{
                    pollFirstEntry = readedLinesFromLogHTML.pollFirstEntry();
                    if( pollFirstEntry != null ){
                        linesFromReadedHtmlTable.put(pollFirstEntry.getKey(), pollFirstEntry.getValue());
                    }
                }while(pollFirstEntry != null);
                
                filePathlinesFromReadedHtmlTable.put(forFirstRead, linesFromReadedHtmlTable);
                
            } catch(InterruptedException ex){
                ex.printStackTrace();
            } catch(SecurityException ex){
                ex.printStackTrace();
            }
            for( Path fileForRead : filesByMaskFromDir ){
                if( forFirstRead.compareTo(fileForRead) != 0 ){
                    linesFromReadedHtmlTable.clear();
                    while( !readerFromHtmlFile.isLogFileNameChanged() ){
                        readerFromHtmlFile.setNewLogFileName(fileForRead);
                    }
                    readFromHtmlTable = new Thread(readerFromHtmlFile);
                    readFromHtmlTable.start();

                    try{
                        readFromHtmlTable.join();
                        while( !readerFromHtmlFile.isJobDone() ){
                            Thread curThr = Thread.currentThread();
                            curThr.sleep(50);
                        }
                        
                        Map.Entry<Integer, String> pollFirstEntry;
                        do{
                            pollFirstEntry = readedLinesFromLogHTML.pollFirstEntry();
                            if( pollFirstEntry != null ){
                                linesFromReadedHtmlTable.put(pollFirstEntry.getKey(), pollFirstEntry.getValue());
                            }
                        }while(pollFirstEntry != null);

                        filePathlinesFromReadedHtmlTable.put(fileForRead, linesFromReadedHtmlTable);
                        
                    } catch(InterruptedException ex){
                        ex.printStackTrace();
                    } catch(SecurityException ex){
                        ex.printStackTrace();
                    }
                }
            }
        }
        if( filePathlinesFromReadedHtmlTable.size() > 0 ){
            ConcurrentSkipListMap<Integer, String> topLines = getLinesForTopSaveIndex();
            int indexOfLines = topLines.lastKey();
            indexOfLines++;
            for( Map.Entry<Path, TreeMap<Integer, String>> element : filePathlinesFromReadedHtmlTable.entrySet() ){
                indexOfLines++;
                topLines.put(indexOfLines, "<h2>" + element.getKey().toString() + "</h2>");
                indexOfLines++;
                for( Map.Entry<Integer, String> elementOfLines : element.getValue().entrySet() ){
                    topLines.put(indexOfLines, elementOfLines.getValue() );
                    indexOfLines++;
                }
            }
            indexOfLines++;
            ConcurrentSkipListMap<Integer, String> bottomLines = getLinesForBottomSaveIndex();
            for( Map.Entry<Integer, String> elementOfLines : bottomLines.entrySet() ){
                    topLines.put(indexOfLines, elementOfLines.getValue() );
                    indexOfLines++;
            }
            
            return topLines;
        }
        return new ConcurrentSkipListMap<Integer, String>();
    }
    
    protected static TreeMap<Integer, String> getStringListForSaveTableAddThead(String headString,TreeMap<Integer, String> listForLogStrs){
        TreeMap<Integer, String> withTheadLogStrs = new TreeMap<Integer, String>();
        int indexStrs = 0;
        withTheadLogStrs.put(indexStrs, "<TABLE>");
        indexStrs++;
        
        withTheadLogStrs.put(indexStrs, "<THEAD>");
        indexStrs++;
        withTheadLogStrs.put(indexStrs, "<TR><TH>" + headString + "</TH></TR>");
        indexStrs++;
        withTheadLogStrs.put(indexStrs, "</THEAD>");
        indexStrs++;
        
        withTheadLogStrs.put(indexStrs, "<TBODY>");
        indexStrs++;
        for( Map.Entry<Integer, String> lines: listForLogStrs.entrySet()){
            withTheadLogStrs.put(indexStrs, "<TR><TD>" + lines.getValue() + "</TD></TR>");
            indexStrs++;
        }
        withTheadLogStrs.put(indexStrs, "</TBODY>");
        indexStrs++;
        withTheadLogStrs.put(indexStrs, "</TABLE>");
        indexStrs++;
        return withTheadLogStrs;
    }
    
    protected static ConcurrentSkipListMap<Integer, String> getStringListForSaveTable(
            ConcurrentSkipListMap<Integer, String> listForRunnableLogStrs,
            TreeMap<Integer, String> srcDataLogStrs,
            String runnedCmdStr){
        
        TreeMap<Integer, String> listForLogStrs = getStringListForSaveTableAddThead(runnedCmdStr, srcDataLogStrs);
        listForRunnableLogStrs.putAll(listForLogStrs);
        return listForRunnableLogStrs;
    }
    
    protected static ConcurrentSkipListMap<Integer, String> getLinesForTopSaveIndex(){
        ConcurrentSkipListMap<Integer, String> listForRunnableLogStrs = new ConcurrentSkipListMap<Integer, String>();
        int strIndex = 0;
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<html lang=\"en-US\" xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-US\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<title>Log report for created Thread Object</title>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<script src=\"./js/menu.js\" type=\"text/javascript\" defer=\"YES\"></script>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<link rel=\"stylesheet\" href=\"./css/report.css\" type=\"text/css\"></link>");

        strIndex++;
        listForRunnableLogStrs.put(strIndex,"</head>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<body class=\"body\" onload=\"allClose()\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <div id=\"header-content\" class=\"content-header\">header page Report for threads state");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <div id=\"menu-content\" class=\"content-menu-items\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <ul id=\"menu\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 1</a>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                <ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 1</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 2</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 3</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 4</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 5</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 6</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 7</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"               </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            </li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 2</a>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                <ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 1</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 2</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 3</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 4</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 5</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 6</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 7</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"               </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            </li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 3</a>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                <ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 1</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 2</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 3</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 4</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 5</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 6</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 7</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"               </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            </li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 4</a>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                <ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 1</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 2</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 3</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 4</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 5</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 6</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 7</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"               </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            </li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <div id=\"page-content\" class=\"content-imported-page\">");
        
        return listForRunnableLogStrs;
    }
    protected static ConcurrentSkipListMap<Integer, String> getLinesForBottomSaveIndex(){
        ConcurrentSkipListMap<Integer, String> listForRunnableLogStrs = new ConcurrentSkipListMap<Integer, String>();
        int strIndex = 0;
        
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <div id=\"footer-content\" class=\"footer-page\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            footer of page report");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    </body>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"</html>");
        return listForRunnableLogStrs;
    }
    
    protected static ConcurrentSkipListMap<Integer, String> getLinesForSaveIndex(){
        ConcurrentSkipListMap<Integer, String> listForRunnableLogStrs = new ConcurrentSkipListMap<Integer, String>();
        int strIndex = 0;
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<html lang=\"en-US\" xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-US\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<title>Log report for created Thread Object</title>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<script src=\"./js/menu.js\" type=\"text/javascript\" defer=\"YES\"></script>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<link rel=\"stylesheet\" href=\"./css/report.css\" type=\"text/css\"></link>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<link rel=\"import\" href=\"table-20181115100212827.html\"></link>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"</head>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<body class=\"body\" onload=\"allClose()\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <div id=\"header-content\" class=\"content-header\">Лось жывотное коварное");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <div id=\"menu-content\" class=\"content-menu-items\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <ul id=\"menu\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 1</a>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                <ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 1</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 2</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 3</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 4</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 5</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 6</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 7</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"               </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            </li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 2</a>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                <ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 1</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 2</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 3</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 4</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 5</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 6</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 7</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"               </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            </li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 3</a>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                <ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 1</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 2</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 3</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 4</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 5</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 6</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 7</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"               </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            </li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 4</a>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                <ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 1</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 2</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 3</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 4</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 5</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 6</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 7</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"               </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            </li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <div id=\"page-content\" class=\"content-imported-page\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <div id=\"item-content1\"><iframe id=\"datatable\" src=\"./table-20181115100212827.html\" class=\"frame-table\" width=\"70%\"></iframe></div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <div id=\"item-content2\"><iframe id=\"datatable\" src=\"./table-20181115100212944.html\" class=\"frame-table\" width=\"70%\"></iframe></div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <div id=\"footer-content\" class=\"footer-page\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            footer of page report");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    </body>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"</html>");
        return listForRunnableLogStrs;
    }
    
    
    
    protected static ConcurrentSkipListMap<Integer, String> getLinesForSaveCss(){
        ConcurrentSkipListMap<Integer, String> listForRunnableLogStrs = new ConcurrentSkipListMap<Integer, String>();
        int strIndex = 0;
        strIndex++;
        listForRunnableLogStrs.put(strIndex,".body{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    padding:0;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    margin:0;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    background-color: #666666;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    text-align: center;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"frame-table{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    border: 1px solid #d6e9c6;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#header-content{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    background: #FF8000;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    padding: 24px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    border-bottom: 3px solid #B5B5B5;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    min-width: 355px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#page-content{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    align-content: flex-end;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    text-decoration: underline;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    height:500px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    padding: 29px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    background: #888888;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    min-width: 355px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#item-content-1{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    height:250px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    margin-right: 350px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    background: #f6cf65;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    display: inline-block;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#item-content-2{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    height:250px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    margin-right: 350px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    background: #f6cf65;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    display: inline-block;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#menu-content{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    height: 500px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    width: 300px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    float: left;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    overflow: auto;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#menu{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    background:#80FF00;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    width:280px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    list-style-type:none;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    padding:0;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    margin:0");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#menu li{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    border-bottom:1px solid #FFFFFF;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    padding:3px");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#menu li a{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    color:#000000;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    font-family:verdana,arial,sans-serif;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    text-decoration:none");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#menu li ul{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    border-top:1px solid #FFFFFF;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    padding:0;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    margin:0;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    list-style-type:square;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    list-style-position:inside");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#menu li ul li{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    border:0;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    list-style-type:square;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    color:#FFFFFF;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    list-style-position:inside");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#footer-content{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    background: #FF8000;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    padding: 11px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    min-width: 355px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        return listForRunnableLogStrs;
    }
    protected static ConcurrentSkipListMap<Integer, String> getLinesForSaveJsMenu(){
        ConcurrentSkipListMap<Integer, String> listForRunnableLogStrs = new ConcurrentSkipListMap<Integer, String>();
        int strIndex = 0;
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"function openMenu(node){");
	strIndex++;
        listForRunnableLogStrs.put(strIndex,"var subMenu = node.parentNode.getElementsByTagName(\"ul\")[0];");
	strIndex++;
        listForRunnableLogStrs.put(strIndex,"subMenu.style.display === \"none\" ? subMenu.style.display = \"block\" : subMenu.style.display = \"none\";");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"function allClose(){");
	strIndex++;
        listForRunnableLogStrs.put(strIndex,"var list = document.getElementById(\"menu\").getElementsByTagName(\"ul\");");
	strIndex++;
        listForRunnableLogStrs.put(strIndex,"for(var i=0;i<list.length;i++){");
	strIndex++;
        listForRunnableLogStrs.put(strIndex,"	list[i].style.display = \"none\";");
	strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        return listForRunnableLogStrs;
    }
    protected static ConcurrentSkipListMap<Integer, String> getLinesForSaveJsLoadHtml(){
        ConcurrentSkipListMap<Integer, String> listForRunnableLogStrs = new ConcurrentSkipListMap<Integer, String>();
        int strIndex = 0;
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"function importTable20181115100212944(){");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"var link = document.createElement('link');");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"link.rel = 'import';");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"link.href = 'table-20181115100212944.html';");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"link.onload = function(this.e){console.log('Loaded import: ' + e.target.href);};");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"link.onerror = function(this.e){console.log('Error loading import: ' + e.target.href);};");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"function importTable001(){");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"var content = document.querySelector('link[rel=\"import\"]').import;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"alert(content);");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"document.body.appendChild(content.cloneNode(true));");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        return listForRunnableLogStrs;
    }
}
