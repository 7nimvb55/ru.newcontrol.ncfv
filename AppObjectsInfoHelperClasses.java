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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppObjectsInfoHelperClasses {
    
    protected static void getInitBusInfo(ArrayBlockingQueue<ArrayList<String>> commandsOutPut){
        String nowTimeStringWithMS = 
                AppFileOperationsSimple.getNowTimeStringWithMS();
        int recIndex = 0;
        ArrayList<String> initRecTime = new ArrayList<String>();
        initRecTime.add(nowTimeStringWithMS);
        commandsOutPut.add(initRecTime);
    }
    
    protected static void getThreadName(Thread detectedThread, ArrayBlockingQueue<ArrayList<String>> commandsOutPut){
        String nowTimeStringWithMS = 
                AppFileOperationsSimple.getNowTimeStringWithMS();
        ArrayList<String> strForOut = new ArrayList<String>();
        strForOut.add(nowTimeStringWithMS);
        strForOut.add("Thread.toString()");
        strForOut.add(detectedThread.toString());
        strForOut.add("Thread.getName()");
        strForOut.add(detectedThread.getName());
        strForOut.add("Thread.getPriority()");
        strForOut.add(String.valueOf(detectedThread.getPriority()));
        strForOut.add("Thread.getId()");
        strForOut.add(String.valueOf(detectedThread.getId()));
        strForOut.add("Thread.getState().name()");
        strForOut.add(detectedThread.getState().name());
        strForOut.add("Thread.getState().ordinal()");
        strForOut.add(String.valueOf(detectedThread.getState().ordinal()));
        strForOut.add("Thread.hashCode()");
        strForOut.add(String.valueOf(detectedThread.hashCode()));
        strForOut.add("Thread.isAlive()");
        strForOut.add(String.valueOf(detectedThread.isAlive()));
        strForOut.add("Thread.isDaemon()");
        strForOut.add(String.valueOf(detectedThread.isDaemon()));
        strForOut.add("Thread.isInterrupted()");
        strForOut.add(String.valueOf(detectedThread.isInterrupted()));
        commandsOutPut.add(strForOut);
    }
    
    protected static void getThreadClass(Thread detectedThread, ArrayBlockingQueue<ArrayList<String>> commandsOutPut){
        String nowTimeStringWithMS = 
                AppFileOperationsSimple.getNowTimeStringWithMS();
        ArrayList<String> strForOut = new ArrayList<String>();
        strForOut.add(nowTimeStringWithMS);
        strForOut.add("Thread.getClass().getName()");
        strForOut.add(detectedThread.getClass().getName());
        strForOut.add("Thread.getClass().getCanonicalName()");
        strForOut.add(detectedThread.getClass().getCanonicalName());
        strForOut.add("Thread.getClass().getModifiers()");
        strForOut.add(String.valueOf(detectedThread.getClass().getModifiers()));
        strForOut.add("Thread.getClass().getSimpleName()");
        strForOut.add(detectedThread.getClass().getSimpleName());
        strForOut.add("Thread.getClass().getTypeName()");
        strForOut.add(detectedThread.getClass().getTypeName());
        strForOut.add("Thread.getClass().toGenericString()");
        strForOut.add(detectedThread.getClass().toGenericString());
        strForOut.add("Thread.getClass().hashCode()");
        strForOut.add(String.valueOf(detectedThread.getClass().hashCode()));
        // get all methods returned resuts in array
        strForOut.add("Thread.getClass().getAnnotatedInterfaces().length");
        strForOut.add(String.valueOf(detectedThread.getClass().getAnnotatedInterfaces().length));
        strForOut.add("Thread.getClass().getAnnotations().length");
        strForOut.add(String.valueOf(detectedThread.getClass().getAnnotations().length));
        strForOut.add("Thread.getClass().getClasses().length");
        strForOut.add(String.valueOf(detectedThread.getClass().getClasses().length));
        strForOut.add("Thread.getClass().getConstructors().length");
        strForOut.add(String.valueOf(detectedThread.getClass().getConstructors().length));
        strForOut.add("Thread.getClass().getDeclaredAnnotations().length");
        strForOut.add(String.valueOf(detectedThread.getClass().getDeclaredAnnotations().length));
        strForOut.add("Thread.getClass().getDeclaredClasses().length");
        strForOut.add(String.valueOf(detectedThread.getClass().getDeclaredClasses().length));
        strForOut.add("Thread.getClass().getDeclaredConstructors().length");
        strForOut.add(String.valueOf(detectedThread.getClass().getDeclaredConstructors().length));
        strForOut.add("Thread.getClass().getDeclaredFields().length");
        strForOut.add(String.valueOf(detectedThread.getClass().getDeclaredFields().length));
        strForOut.add("Thread.getClass().getDeclaredMethods().length");
        strForOut.add(String.valueOf(detectedThread.getClass().getDeclaredMethods().length));
//        strForOut.add("Thread.getClass().getEnumConstants().toString()");
//        strForOut.add(String.valueOf(detectedThread.getClass().getEnumConstants().toString()));
        strForOut.add("Thread.getClass().getEnumConstants().length");
        Object resultGetEnumConstants[] = detectedThread.getClass().getEnumConstants();
        if( resultGetEnumConstants != null ){
            strForOut.add(String.valueOf(resultGetEnumConstants.length));
        } else {
            strForOut.add("null");
        }
        strForOut.add("Thread.getClass().getFields().length");
        strForOut.add(String.valueOf(detectedThread.getClass().getFields().length));
        strForOut.add("Thread.getClass().getGenericInterfaces().length");
        strForOut.add(String.valueOf(detectedThread.getClass().getGenericInterfaces().length));
        strForOut.add("Thread.getClass().getInterfaces().length");
        strForOut.add(String.valueOf(detectedThread.getClass().getInterfaces().length));
        strForOut.add("Thread.getClass().getMethods().length");
        strForOut.add(String.valueOf(detectedThread.getClass().getMethods().length));
//        strForOut.add("Thread.getClass().getSigners().length");
//        strForOut.add(String.valueOf(detectedThread.getClass().getSigners().length));
        strForOut.add("Thread.getClass().getSigners().length");
        Object resultGetSigners[] = detectedThread.getClass().getSigners();
        if( resultGetSigners != null ){
            strForOut.add(String.valueOf(resultGetSigners.length));
        } else {
            strForOut.add("null");
        }
        strForOut.add("Thread.getClass().getTypeParameters().length");
        strForOut.add(String.valueOf(detectedThread.getClass().getTypeParameters().length));
        //get all methods and returned result in boolean
        strForOut.add("Thread.getClass().desiredAssertionStatus()");
        strForOut.add(String.valueOf(detectedThread.getClass().desiredAssertionStatus()));
        strForOut.add("Thread.getClass().isAnnotation()");
        strForOut.add(String.valueOf(detectedThread.getClass().isAnnotation()));
        strForOut.add("Thread.getClass().isAnonymousClass()");
        strForOut.add(String.valueOf(detectedThread.getClass().isAnonymousClass()));
        strForOut.add("Thread.getClass().isArray()");
        strForOut.add(String.valueOf(detectedThread.getClass().isArray()));
        strForOut.add("Thread.getClass().isEnum()");
        strForOut.add(String.valueOf(detectedThread.getClass().isEnum()));
        strForOut.add("Thread.getClass().isInterface()");
        strForOut.add(String.valueOf(detectedThread.getClass().isInterface()));
        strForOut.add("Thread.getClass().isLocalClass()");
        strForOut.add(String.valueOf(detectedThread.getClass().isLocalClass()));
        strForOut.add("Thread.getClass().isMemberClass()");
        strForOut.add(String.valueOf(detectedThread.getClass().isMemberClass()));
        strForOut.add("Thread.getClass().isPrimitive()");
        strForOut.add(String.valueOf(detectedThread.getClass().isPrimitive()));
        strForOut.add("Thread.getClass().isSynthetic()");
        strForOut.add(String.valueOf(detectedThread.getClass().isSynthetic()));
        commandsOutPut.add(strForOut);
    }
    protected static void  getThreadClassGetDeclaredMethods(Thread detectedThread, ArrayBlockingQueue<ArrayList<String>> commandsOutPut){
        String nowTimeStringWithMS = 
                AppFileOperationsSimple.getNowTimeStringWithMS();
        ArrayList<String> strForOut = new ArrayList<String>();
        strForOut.add(nowTimeStringWithMS);
        strForOut.add("Thread.getClass().getDeclaredMethods().length");
        Method resultGetDeclaredMethods[] = detectedThread.getClass().getDeclaredMethods();
        if( resultGetDeclaredMethods != null ){
            strForOut.add(String.valueOf(resultGetDeclaredMethods.length));
            int idexOfMethod = 0;
            for(Method elementOfMethods : resultGetDeclaredMethods){
                //Strings results
                strForOut.add("...getDeclaredMethods()[" + idexOfMethod + "].getName()");
                strForOut.add(elementOfMethods.getName());
                strForOut.add("...getDeclaredMethods()[" + idexOfMethod + "].toGenericString()");
                strForOut.add(elementOfMethods.toGenericString());
                strForOut.add("...getDeclaredMethods()[" + idexOfMethod + "].toString()");
                strForOut.add(elementOfMethods.toString());
                //Booleans results
                strForOut.add("...getDeclaredMethods()[" + idexOfMethod + "].isAccessible()");
                strForOut.add(String.valueOf(elementOfMethods.isAccessible()));
                strForOut.add("...getDeclaredMethods()[" + idexOfMethod + "].isBridge()");
                strForOut.add(String.valueOf(elementOfMethods.isBridge()));
                strForOut.add("...getDeclaredMethods()[" + idexOfMethod + "].isDefault()");
                strForOut.add(String.valueOf(elementOfMethods.isDefault()));
                strForOut.add("...getDeclaredMethods()[" + idexOfMethod + "].isSynthetic()");
                strForOut.add(String.valueOf(elementOfMethods.isSynthetic()));
                strForOut.add("...getDeclaredMethods()[" + idexOfMethod + "].isVarArgs()");
                strForOut.add(String.valueOf(elementOfMethods.isVarArgs()));
                //Integer results
                strForOut.add("...getDeclaredMethods()[" + idexOfMethod + "].getModifiers()");
                strForOut.add(String.valueOf(elementOfMethods.getModifiers()));
                strForOut.add("...getDeclaredMethods()[" + idexOfMethod + "].getParameterCount()");
                strForOut.add(String.valueOf(elementOfMethods.getParameterCount()));
                strForOut.add("...getDeclaredMethods()[" + idexOfMethod + "].hashCode()");
                strForOut.add(String.valueOf(elementOfMethods.hashCode()));
                
                strForOut.add("Thread.getClass().getDeclaredMethods()[" + idexOfMethod + "].getParameters().length");
                Parameter[] parameters = elementOfMethods.getParameters();
                if( resultGetDeclaredMethods != null ){
                    strForOut.add(String.valueOf(parameters.length));
                    int indexOfParam = 0;
                    for (Parameter parameter : parameters) {
                        //String results
                        strForOut.add("...[" + idexOfMethod + "].getParameters()[" + indexOfParam + "].getName()");
                        strForOut.add(parameter.getName());
                        strForOut.add("...[" + idexOfMethod + "].getParameters()[" + indexOfParam + "].toString()");
                        strForOut.add(parameter.toString());
                        //Integer results
                        strForOut.add("...[" + idexOfMethod + "].getParameters()[" + indexOfParam + "].getModifiers()");
                        strForOut.add(String.valueOf(parameter.getModifiers()));
                        strForOut.add("...[" + idexOfMethod + "].getParameters()[" + indexOfParam + "].hashCode()");
                        strForOut.add(String.valueOf(parameter.hashCode()));
                        //Boolean results
                        strForOut.add("...[" + idexOfMethod + "].getParameters()[" + indexOfParam + "].isImplicit()");
                        strForOut.add(String.valueOf(parameter.isImplicit()));
                        strForOut.add("...[" + idexOfMethod + "].getParameters()[" + indexOfParam + "].isNamePresent()");
                        strForOut.add(String.valueOf(parameter.isNamePresent()));
                        strForOut.add("...[" + idexOfMethod + "].getParameters()[" + indexOfParam + "].isSynthetic()");
                        strForOut.add(String.valueOf(parameter.isSynthetic()));
                        strForOut.add("...[" + idexOfMethod + "].getParameters()[" + indexOfParam + "].isVarArgs()");
                        strForOut.add(String.valueOf(parameter.isVarArgs()));
                        strForOut.add("...[" + idexOfMethod + "].getParameters()[" + indexOfParam + "].getParameterizedType().getTypeName()");
                        //String results
                        strForOut.add(String.valueOf(parameter.getParameterizedType().getTypeName()));
                        indexOfParam++;
                    }
                } else {
                    strForOut.add("null");
                }
                idexOfMethod++;
            }
            
            
        } else {
            strForOut.add("null");
        }
        
        
        
        commandsOutPut.add(strForOut);
    }
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
    //@todo this and next function recode for getThreadClassFormat
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
}
