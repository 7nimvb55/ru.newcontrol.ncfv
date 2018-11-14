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
            } catch (Exception ex){
                strToOut = strToOut
                    + NcStrLogMsgField.EXCEPTION_MSG.getStr()
                    + ex.getMessage();
            }
            listStrToRet.add(strToOut);
            fieldIdx++;
        }
        return listStrToRet;
    }
    
    protected static void getThreadDebugInfoToHtml(Thread readedThread){
        String nowTimeStringWithMS = 
                AppFileOperationsSimple.getNowTimeStringWithMS();
        Path logForHtmlCurrentLogSubDir = 
                AppFileOperationsSimple.getLogForHtmlCurrentLogSubDir(nowTimeStringWithMS);
        ConcurrentSkipListMap<String, Path> newLogFileInLogHTML = 
                AppFileOperationsSimple.getNewLogFileInLogHTML(logForHtmlCurrentLogSubDir);
        newLogFileInLogHTML.put(AppFileNamesConstants.LOG_HTML_KEY_FOR_CURRENT_SUB_DIR, logForHtmlCurrentLogSubDir);
        //@todo chaos to system out
        
        ConcurrentSkipListMap<String, String> listForLogStrs = new ConcurrentSkipListMap<String, String>();
        
        System.out.println("* * * [Thread] * * *");
        
        System.out.println(
                "[NAME]" + readedThread.getName()
                + "[CLASS]" + readedThread.getClass().getCanonicalName()
                + "[isInstanceOf(AppThWorkDirListRun.class)]" 
                + readedThread.getClass().isInstance(AppThWorkDirListRun.class));
        
        System.out.println("* * * [Thread.getContextClassLoader()] * * *");
        System.out.println("[getContextClassLoader()]" 
                + readedThread.getContextClassLoader().getClass().getCanonicalName());
        System.out.println("[getUncaughtExceptionHandler()]" 
                + readedThread.getUncaughtExceptionHandler().getClass().getCanonicalName());
        
        System.out.println("* * * [Thread.getClass().getClasses()] * * *");
        String strCanonicalNames = "";
        int idxId = 0;
        Class<?>[] classes = readedThread.getClass().getClasses();
        for( Class<?> str : classes ){
            strCanonicalNames = strCanonicalNames + "[" + idxId + "]" + str.getCanonicalName();
            idxId++;
        }
        System.out.println("[getClasses()]" 
                + strCanonicalNames);
        
        System.out.println("* * * [Thread.getStackTrace()] * * *");
        strCanonicalNames = "";
        idxId = 0;
        StackTraceElement[] traceElements = readedThread.getStackTrace();
        
        for(StackTraceElement traceElement : traceElements ){
            strCanonicalNames = strCanonicalNames + "[" + idxId + "]getClass().getCanonicalName()]" + traceElement.getClass().getCanonicalName();
            
            idxId++;
        }
        System.out.println("[getStackTrace().length]"
                + traceElements.length
                + "[getStackTrace()]" 
                + strCanonicalNames);
        
        System.out.println("* * * [Thread.getContextClassLoader().getClass().getClasses()] * * *");
        Class<?>[] classesContextClassLoader = readedThread.getContextClassLoader().getClass().getClasses();
        for( Class<?> str : classesContextClassLoader ){
            strCanonicalNames = strCanonicalNames + "[" + idxId + "]" + str.getCanonicalName();
            idxId++;
        }
        
        
        System.out.println("[getContextClassLoader().getClass().getClasses()]" 
                + strCanonicalNames);
        
        System.out.println("* * * [Thread.getName() | .getClass() and some methods] * * *");
        System.out.println(
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
        // all methods from Thread objects
        
        //readedThread.getClass().asSubclass(clazz);//Class<? extends U> 
        //readedThread.getClass().cast(idxId);//? extends Thread
        boolean desiredAssertionStatus = readedThread.getClass().desiredAssertionStatus();
        //boolean equals = readedThread.getClass().equals(idxId);
        
        AnnotatedType[] annotatedInterfaces = readedThread.getClass().getAnnotatedInterfaces();
        for(AnnotatedType element : annotatedInterfaces){
             System.out.println(
                "getAnnotatedInterfaces()[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        
        AnnotatedType annotatedSuperclass = readedThread.getClass().getAnnotatedSuperclass();
        //readedThread.getClass().getAnnotation(annotationClass); //A
        Annotation[] annotations = readedThread.getClass().getAnnotations();
        for(Annotation element : annotations){
             System.out.println(
                "getAnnotations()[CLASS][getCanonicalName]" + element.annotationType().getClass().getCanonicalName());
        }
        
        //readedThread.getClass().getAnnotationsByType(annotationClass); //A[]
        String canonicalName = readedThread.getClass().getCanonicalName();
        
        //readedThread.getClass().getClass().getClass();//Class<?>
        //readedThread.getClass().getClassLoader();//ClassLoader
        
        
        
        Class<?>[] classes1 = readedThread.getClass().getClasses();
        for(Class element : classes1){
             System.out.println(
                "getClasses()[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        //readedThread.getClass().getComponentType();//Class<?>
        
        //readedThread.getClass().getConstructor(classes);//Constructor <? extends Thread>
        
        Constructor<?>[] constructors = readedThread.getClass().getConstructors();
        for(Constructor element : constructors){
             System.out.println(
                "getConstructors()[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        //readedThread.getClass().getDeclaredAnnotation(annotationClass);//A
        
        Annotation[] declaredAnnotations = readedThread.getClass().getDeclaredAnnotations();
        for(Annotation element : declaredAnnotations){
             System.out.println("getDeclaredAnnotations()[CLASS]"
                + "[toString()]" + element.toString()
                + "[annotationType().getCanonicalName()]" + element.annotationType().getCanonicalName()
                + "[getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        //readedThread.getClass().getDeclaredAnnotationsByType(annotationClass);//A[]
        
        Class<?>[] declaredClasses = readedThread.getClass().getDeclaredClasses();
        for(Class element : declaredClasses){
             System.out.println("getDeclaredClasses()[CLASS]"
                + "[getName()]" + element.getName()
                + "[getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        //readedThread.getClass().getDeclaredConstructor(declaredClasses);//Constructor <? extends Thread>
        
        Constructor<?>[] declaredConstructors = readedThread.getClass().getDeclaredConstructors();
        for(Constructor element : declaredConstructors){
             System.out.println("getDeclaredConstructors()[CLASS]"
                + "[getName()]" + element.getName()
                + "[getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        //readedThread.getClass().getDeclaredField(canonicalName);//Field
        
        Field[] declaredFields = readedThread.getClass().getDeclaredFields();
        for(Field element : declaredFields){
             System.out.println("getDeclaredFields()[CLASS]"
                + "[getName()]" + element.getName()
                + "[getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        //readedThread.getClass().getDeclaredMethod(canonicalName, declaredClasses);//Method
        
        Method[] declaredMethods = readedThread.getClass().getDeclaredMethods();
        for(Method element : declaredMethods){
             System.out.println("getDeclaredMethods()[CLASS]"
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
             System.out.println(
                "getFields()[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        
        Type[] genericInterfaces = readedThread.getClass().getGenericInterfaces();
        for(Type element : genericInterfaces){
             System.out.println(
                "getGenericInterfaces()[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        Type genericSuperclass = readedThread.getClass().getGenericSuperclass(); //Type
        
        Class<?>[] interfaces = readedThread.getClass().getInterfaces();
        for(Class element : interfaces){
             System.out.println(
                "getInterfaces()[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        
        //readedThread.getClass().getMethod(canonicalName, interfaces);//Method
        
        Method[] methods = readedThread.getClass().getMethods();
        for(Method element : methods){
             System.out.println(
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
                if( element != null )
                 System.out.println(
                    "getSigners()[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
            }
        }
        
        
        String simpleName = readedThread.getClass().getSimpleName();
        Class<?> superclass = readedThread.getClass().getSuperclass();
        String typeName = readedThread.getClass().getTypeName();
        
        
        
        TypeVariable<? extends Class<? extends Thread>>[] typeParameters = readedThread.getClass().getTypeParameters();
        for(TypeVariable<? extends Class<? extends Thread>> element : typeParameters){
             System.out.println(
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
        
        //readedThread.getClass().notify();
        //readedThread.getClass().notifyAll();
        //readedThread.getClass().wait();
        //readedThread.getClass().wait(idxId);
        //readedThread.getClass().wait(idxId, idxId);

    }
}
