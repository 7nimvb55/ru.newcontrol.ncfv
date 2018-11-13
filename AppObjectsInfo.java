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
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.security.ProtectionDomain;
import java.util.TreeMap;

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
    
    protected static void getThreadDebugInfo(Thread outerDirlistReader){
        System.out.println(
                "[NAME]" + outerDirlistReader.getName()
                + "[CLASS]" + outerDirlistReader.getClass().getCanonicalName()
                + "[isInstanceOf(AppThWorkDirListRun.class)]" 
                + outerDirlistReader.getClass().isInstance(AppThWorkDirListRun.class));
        
        System.out.println("[getContextClassLoader()]" 
                + outerDirlistReader.getContextClassLoader().getClass().getCanonicalName());
        System.out.println("[getUncaughtExceptionHandler()]" 
                + outerDirlistReader.getUncaughtExceptionHandler().getClass().getCanonicalName());
        String strCanonicalNames = "";
        int idxId = 0;
        Class<?>[] classes = outerDirlistReader.getClass().getClasses();
        for( Class<?> str : classes ){
            strCanonicalNames = strCanonicalNames + "[" + idxId + "]" + str.getCanonicalName();
            idxId++;
        }
        System.out.println("[getClasses()]" 
                + strCanonicalNames);
        
        strCanonicalNames = "";
        idxId = 0;
        StackTraceElement[] traceElements = outerDirlistReader.getStackTrace();
        for(StackTraceElement traceElement : traceElements ){
            strCanonicalNames = strCanonicalNames + "[" + idxId + "]getClass().getCanonicalName()]" + traceElement.getClass().getCanonicalName();
            
            idxId++;
        }
        System.out.println("[getStackTrace()]" 
                + strCanonicalNames);
        Class<?>[] classesContextClassLoader = outerDirlistReader.getContextClassLoader().getClass().getClasses();
        for( Class<?> str : classesContextClassLoader ){
            strCanonicalNames = strCanonicalNames + "[" + idxId + "]" + str.getCanonicalName();
            idxId++;
        }
        System.out.println("[getContextClassLoader().getClass().getClasses()]" 
                + strCanonicalNames);
        System.out.println(
                "[NAME]" + outerDirlistReader.getName()
                + "[CLASS][getName]" + outerDirlistReader.getClass().getName()
                + "[CLASS][getCanonicalName]" + outerDirlistReader.getClass().getCanonicalName()
                + "[CLASS][getSimpleName]" + outerDirlistReader.getClass().getSimpleName()
                + "[CLASS][getTypeName]" + outerDirlistReader.getClass().getTypeName()
                + "[CLASS][toGenericString]" + outerDirlistReader.getClass().toGenericString()
                + "[CLASS][toString]" + outerDirlistReader.getClass().toString()
                        
                + "[CLASS][getClass().isAssignableFrom(AppThWorkDirListRun.class)]"
                + outerDirlistReader.getClass().isAssignableFrom(AppThWorkDirListRun.class)
                + "[CLASS][getClass().isInstance(AppThWorkDirListRun.class)]"
                        
                + outerDirlistReader.getClass().isInstance(AppThWorkDirListRun.class));
        
        AnnotatedType[] annotatedInterfaces = outerDirlistReader.getClass().getAnnotatedInterfaces();
        for(AnnotatedType element : annotatedInterfaces){
             System.out.println(
                "[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        AnnotatedType annotatedSuperclass = outerDirlistReader.getClass().getAnnotatedSuperclass();
        Annotation[] annotations = outerDirlistReader.getClass().getAnnotations();
        for(Annotation element : annotations){
             System.out.println(
                "[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        Class<?>[] classes1 = outerDirlistReader.getClass().getClasses();
        for(Class element : classes1){
             System.out.println(
                "[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        Constructor<?>[] constructors = outerDirlistReader.getClass().getConstructors();
        for(Constructor element : constructors){
             System.out.println(
                "[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        Annotation[] declaredAnnotations = outerDirlistReader.getClass().getDeclaredAnnotations();
        for(Annotation element : declaredAnnotations){
             System.out.println(
                "[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        Class<?>[] declaredClasses = outerDirlistReader.getClass().getDeclaredClasses();
        for(Class element : declaredClasses){
             System.out.println(
                "[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        Constructor<?>[] declaredConstructors = outerDirlistReader.getClass().getDeclaredConstructors();
        for(Constructor element : declaredConstructors){
             System.out.println(
                "[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        Field[] declaredFields = outerDirlistReader.getClass().getDeclaredFields();
        for(Field element : declaredFields){
             System.out.println(
                "[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        Method[] declaredMethods = outerDirlistReader.getClass().getDeclaredMethods();
        for(Method element : declaredMethods){
             System.out.println(
                "[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        Field[] fields = outerDirlistReader.getClass().getFields();
        for(Field element : fields){
             System.out.println(
                "[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        Type[] genericInterfaces = outerDirlistReader.getClass().getGenericInterfaces();
        for(Type element : genericInterfaces){
             System.out.println(
                "[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        Class<?>[] interfaces = outerDirlistReader.getClass().getInterfaces();
        for(Class element : interfaces){
             System.out.println(
                "[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        Method[] methods = outerDirlistReader.getClass().getMethods();
        for(Method element : methods){
             System.out.println(
                "[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        int modifiers = outerDirlistReader.getClass().getModifiers();
        String name = outerDirlistReader.getClass().getName();
        Package aPackage = outerDirlistReader.getClass().getPackage();
        ProtectionDomain protectionDomain = outerDirlistReader.getClass().getProtectionDomain();
        Object[] signers = outerDirlistReader.getClass().getSigners();
        for(Object element : signers){
             System.out.println(
                "[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        String simpleName = outerDirlistReader.getClass().getSimpleName();
        Class<?> superclass = outerDirlistReader.getClass().getSuperclass();
        String typeName = outerDirlistReader.getClass().getTypeName();
        TypeVariable<? extends Class<? extends Thread>>[] typeParameters = outerDirlistReader.getClass().getTypeParameters();
        for(TypeVariable<? extends Class<? extends Thread>> element : typeParameters){
             System.out.println(
                "[CLASS][getCanonicalName]" + element.getClass().getCanonicalName());
        }
        boolean annotation = outerDirlistReader.getClass().isAnnotation();
        boolean anonymousClass = outerDirlistReader.getClass().isAnonymousClass();
        boolean array = outerDirlistReader.getClass().isArray();
        //outerDirlistReader.getClass().isAssignableFrom(cls);
        boolean aEnum = outerDirlistReader.getClass().isEnum();
        boolean aInterface = outerDirlistReader.getClass().isInterface();
        boolean localClass = outerDirlistReader.getClass().isLocalClass();
        boolean memberClass = outerDirlistReader.getClass().isMemberClass();
        boolean primitive = outerDirlistReader.getClass().isPrimitive();
        boolean synthetic = outerDirlistReader.getClass().isSynthetic();
    }
}
