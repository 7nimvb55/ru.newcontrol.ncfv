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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppObjectsInfo {
    
    
    

    protected static void getThreadDebugInfoToHtml(Thread readedThread){
        String nowTimeStringWithMS = 
                AppFileOperationsSimple.getNowTimeStringWithMS();
        Path logForHtmlCurrentLogSubDir = 
                AppFileOperationsSimple.getLogForHtmlCurrentLogSubDir(nowTimeStringWithMS);
        ConcurrentSkipListMap<String, Path> newLogFileInLogHTML = 
                AppFileOperationsSimple.getNewLogFileInLogHTML(logForHtmlCurrentLogSubDir);
        newLogFileInLogHTML.put(AppFileNamesConstants.LOG_HTML_KEY_FOR_CURRENT_SUB_DIR, logForHtmlCurrentLogSubDir);
        
        
        Integer messagesQueueSize = 10000;
        ArrayBlockingQueue<ArrayList<String>> commandsOutPut = new ArrayBlockingQueue<ArrayList<String>>(messagesQueueSize);
        AppObjectsInfoHelperClasses.getInitBusInfo(commandsOutPut);
        
        ArrayBlockingQueue<String> listForRunnableLogStrs = new ArrayBlockingQueue<String>(messagesQueueSize);
        
        TreeMap<Integer, String> listForLogStrs = new TreeMap<Integer, String>();
        
        Path newLogHtmlTableFile = newLogFileInLogHTML.get(AppFileNamesConstants.LOG_HTML_TABLE_PREFIX);
        
        AppLoggerToHTMLRunnable loggerToHtml = new AppLoggerToHTMLRunnable(
                listForRunnableLogStrs,
                newLogHtmlTableFile
        );
        
        
        //@todo chaos to system out
        
        
        int indexLinesToFile = 0;
        
        
        AppObjectsInfoHelperClasses.getThreadName(readedThread, commandsOutPut);
        AppObjectsInfoHelperHtml.commandOutPutBusToHtml(commandsOutPut,listForRunnableLogStrs);
        
        writeLinesToFileByRunnable(listForRunnableLogStrs, loggerToHtml, newLogHtmlTableFile);
        
        newLogHtmlTableFile = AppFileOperationsSimple.getNewLogHtmlTableFile(logForHtmlCurrentLogSubDir);
        
        AppObjectsInfoHelperClasses.getThreadClass(readedThread, commandsOutPut);
        AppObjectsInfoHelperHtml.commandOutPutBusToHtml(commandsOutPut,listForRunnableLogStrs);
        writeLinesToFileByRunnable(listForRunnableLogStrs, loggerToHtml, newLogHtmlTableFile);
        
        newLogHtmlTableFile = AppFileOperationsSimple.getNewLogHtmlTableFile(logForHtmlCurrentLogSubDir);
        
        AppObjectsInfoHelperClasses.getThreadClassGetDeclaredMethods(readedThread, commandsOutPut);
        AppObjectsInfoHelperHtml.commandOutPutBusToHtml(commandsOutPut,listForRunnableLogStrs);
        writeLinesToFileByRunnable(listForRunnableLogStrs, loggerToHtml, newLogHtmlTableFile);
        
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
        newLogHtmlTableFile = AppFileOperationsSimple.getNewLogHtmlTableFile(logForHtmlCurrentLogSubDir);
        AppObjectsInfoHelperHtml.getStringListForSaveTable(listForRunnableLogStrs, listForLogStrs, "readedThread.getStackTrace()");
        System.out.println("for first record " + listForRunnableLogStrs.size() + "file name" + newLogHtmlTableFile.toString());
        writeLinesToFileByRunnable(listForRunnableLogStrs, loggerToHtml, newLogHtmlTableFile);
        listForLogStrs.clear();        
        
        
        //************** ************** ************** ************** **************
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
        // end for first block lines into recording list
        newLogHtmlTableFile = AppFileOperationsSimple.getNewLogHtmlTableFile(logForHtmlCurrentLogSubDir);
        
        AppObjectsInfoHelperHtml.getStringListForSaveTable(listForRunnableLogStrs, listForLogStrs, "readedThread.getClass().getTypeParameters()");
        listForLogStrs.clear();
        listForLogStrs = new TreeMap<Integer, String>();
        System.out.println("for second record " + listForRunnableLogStrs.size() + newLogHtmlTableFile.toString());
        
        writeLinesToFileByRunnable(listForRunnableLogStrs, loggerToHtml, newLogHtmlTableFile);
        // end for write first block lines into file
        newLogHtmlTableFile = newLogFileInLogHTML.get(AppFileNamesConstants.LOG_HTML_JS_MENU_PREFIX);
        listForRunnableLogStrs.clear();
        
        
        ArrayBlockingQueue<String> linesForSaveJsMenu = AppObjectsInfoHelperHtml.getLinesForSaveJsMenu();
        String pollFirstForSaveJsMenu = "";
            do{
                pollFirstForSaveJsMenu = linesForSaveJsMenu.poll();
                if( pollFirstForSaveJsMenu != null ){
                    listForRunnableLogStrs.add(pollFirstForSaveJsMenu);
                }
            }while( !linesForSaveJsMenu.isEmpty() );
        
        writeLinesToFileByRunnable(listForRunnableLogStrs, loggerToHtml, newLogHtmlTableFile);
        //********* ************* ************ ************** ************** ***************
        newLogHtmlTableFile = newLogFileInLogHTML.get(AppFileNamesConstants.LOG_HTML_CSS_PREFIX);
        listForRunnableLogStrs.clear();
        ArrayBlockingQueue<String> linesForSaveCss = AppObjectsInfoHelperHtml.getLinesForSaveCss();
        String pollFirstForSaveCss = "";
        do{
            pollFirstForSaveCss = linesForSaveCss.poll();
            if( pollFirstForSaveCss != null ){
                listForRunnableLogStrs.add(pollFirstForSaveCss);
            }
        }while( !linesForSaveCss.isEmpty() );
        
        writeLinesToFileByRunnable(listForRunnableLogStrs, loggerToHtml, newLogHtmlTableFile);
        //********* ************* ************ ************** ************** ***************
        
        ArrayBlockingQueue<String> generatedLinesForIndexFile = new ArrayBlockingQueue<String>(messagesQueueSize);
        /*ConcurrentSkipListMap<Integer, String> generatedLinesForIndexFile =
                new ConcurrentSkipListMap<Integer, String>();*/
        generateIndexFile(generatedLinesForIndexFile, newLogFileInLogHTML);
        // make index file
        if( generatedLinesForIndexFile.size() > 0 ){
            newLogHtmlTableFile = newLogFileInLogHTML.get(AppFileNamesConstants.LOG_INDEX_PREFIX);

            listForRunnableLogStrs.clear();

            String pollFirstEntryIndexFile = "";
            do{
                pollFirstEntryIndexFile = generatedLinesForIndexFile.poll();
                if( pollFirstEntryIndexFile != null ){
                    listForRunnableLogStrs.add(pollFirstEntryIndexFile);
                }
            }while( !generatedLinesForIndexFile.isEmpty() );
            
            System.out.println(" for index record " + listForRunnableLogStrs.size() + " open in browser " + newLogHtmlTableFile.toString());
            writeLinesToFileByRunnable(listForRunnableLogStrs, loggerToHtml, newLogHtmlTableFile);
            
        }
        
    }
    protected static void writeLinesToFileByRunnable(ArrayBlockingQueue<String> listStrForLog,
            AppLoggerToHTMLRunnable writerToHtmlRunnable,
            Path fileForWrite){
        
        if (listStrForLog.size() > 0){
            String nowTimeStringWithMS = 
                    AppFileOperationsSimple.getNowTimeStringWithMS();
            ThreadGroup newJobThreadGroup = new ThreadGroup("TmpGroup-" + nowTimeStringWithMS);
            
            if( !writerToHtmlRunnable.isNewRunner() ){
                //Check for old job is done
                try{
                    while( !writerToHtmlRunnable.isJobDone() ){
                        Thread curThr = Thread.currentThread();
                        curThr.sleep(50);
                    }
                } catch(InterruptedException ex){
                    ex.printStackTrace();
                } catch(SecurityException ex){
                    ex.printStackTrace();
                }
            }
            
            
            while( !writerToHtmlRunnable.isLogFileNameChanged() ){
                writerToHtmlRunnable.setNewLogFileName(fileForWrite);
            }
            
            
            Thread writeToHtmlByThread = new Thread(newJobThreadGroup, writerToHtmlRunnable, "writerToHtml-" + nowTimeStringWithMS);
            System.out.println("State writer " + writeToHtmlByThread.getState().name());
            //Thread writeToHtmlByThread = new Thread(writerToHtmlRunnable, "writerToHtml-" + nowTimeStringWithMS);
            writeToHtmlByThread.start();
            System.out.println("State writer " + writeToHtmlByThread.getState().name());
            //Check for now job is done
            try{
                writeToHtmlByThread.join();
                while( !writerToHtmlRunnable.isJobDone() ){
                    Thread curThr = Thread.currentThread();
                    curThr.sleep(50);
                    System.out.println("State writer " + writeToHtmlByThread.getState().name());
                }
            } catch(InterruptedException ex){
                ex.printStackTrace();
            } catch(SecurityException ex){
                ex.printStackTrace();
            }
            System.out.println("State writer " + writeToHtmlByThread.getState().name());
            //@todo destroy for threadgroups...

            
        }
    }
    
    protected static void readLinesFromFileByRunnable(ArrayBlockingQueue<String> listStrFromFile,
            AppLoggerFromHTMLRunnable readerFromHtmlFile,
            Path fileForWrite){
        
        if (listStrFromFile.size() == 0){
            String nowTimeStringWithMS = 
                    AppFileOperationsSimple.getNowTimeStringWithMS();
            //@todo check stack trace to see for runned threads by his names and classes
            ThreadGroup newJobThreadGroup = new ThreadGroup("TmpGroupReadFile-" + nowTimeStringWithMS);
            if( !readerFromHtmlFile.isNewRunner() ){
                //Check for old job is done
                try{
                    while( !readerFromHtmlFile.isJobDone() ){
                        Thread curThr = Thread.currentThread();
                        curThr.sleep(50);
                    }
                } catch(InterruptedException ex){
                    ex.printStackTrace();
                } catch(SecurityException ex){
                    ex.printStackTrace();
                }
            }
            
            while( !readerFromHtmlFile.isLogFileNameChanged() ){
                readerFromHtmlFile.setNewLogFileName(fileForWrite);
            }
            
            
            Thread readFromHtmlByThread = new Thread(newJobThreadGroup, readerFromHtmlFile, "readerFromHtml-" + nowTimeStringWithMS);
            System.out.println("State reader " + readFromHtmlByThread.getState().name());
            
            readFromHtmlByThread.start();
            System.out.println("State reader " + readFromHtmlByThread.getState().name());
            //Check for now job is done
            try{
                readFromHtmlByThread.join();
                while( !readerFromHtmlFile.isJobDone() ){
                    Thread curThr = Thread.currentThread();
                    curThr.sleep(50);
                    //curThr.notifyAll();
                    System.out.println("State reader " + readFromHtmlByThread.getState().name());
                }
            } catch(InterruptedException ex){
                ex.printStackTrace();
            } catch(SecurityException ex){
                ex.printStackTrace();
            }
            System.out.println("State reader " + readFromHtmlByThread.getState().name());
            //@todo destroy for threadgroups... see core threadgroup.destroy

        }
    }
    
    protected static void generateIndexFile(
            ArrayBlockingQueue<String> returnedLinesForIndexFile,
            ConcurrentSkipListMap<String, Path> listOfFileInLogHTML){
        
        Path dirForRead = listOfFileInLogHTML.get(AppFileNamesConstants.LOG_HTML_KEY_FOR_CURRENT_SUB_DIR);
        ArrayList<Path> filesByMaskFromDir = AppFileOperationsSimple.getFilesByMaskFromDir(
                dirForRead,
                "{" + AppFileNamesConstants.LOG_HTML_TABLE_PREFIX + "}*");
        Integer messagesQueueSize = 10000;
        ArrayBlockingQueue<String> readedLinesFromLogHTML = new ArrayBlockingQueue<String>(messagesQueueSize);

        if( filesByMaskFromDir.size() > 0 ){
            Path forFirstRead = filesByMaskFromDir.get(0);
            AppLoggerFromHTMLRunnable readerFromHtmlFile = new AppLoggerFromHTMLRunnable(
                    readedLinesFromLogHTML,
                    forFirstRead);
            Path fileJsMenuPrefix = listOfFileInLogHTML.get(AppFileNamesConstants.LOG_HTML_JS_MENU_PREFIX).getFileName();
            Path fileCssPrefix = listOfFileInLogHTML.get(AppFileNamesConstants.LOG_HTML_CSS_PREFIX).getFileName();
            AppObjectsInfoHelperHtml.getLinesForTopSaveIndex(returnedLinesForIndexFile, fileJsMenuPrefix, fileCssPrefix, filesByMaskFromDir);        
            for( Path fileForRead : filesByMaskFromDir ){
                String strForAncor = "<p><a name=\"" + fileForRead.getFileName().toString().split("\\.")[0] + "\">"
                        + fileForRead.toString() + "</a></p>";
                returnedLinesForIndexFile.add(strForAncor);
                readLinesFromFileByRunnable(readedLinesFromLogHTML, readerFromHtmlFile, fileForRead);
                
                readedLinesFromLogHTML.add(strForAncor);
                returnedLinesForIndexFile.addAll(readedLinesFromLogHTML);
                readedLinesFromLogHTML.clear();
            }
            //indexOfLines++;
            AppObjectsInfoHelperHtml.getLinesForBottomSaveIndex(returnedLinesForIndexFile);
        }
    }
}
