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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppFileOperationsSimple {
    protected static Path getUserHomePath() throws IOException{
        String usrHomePath = System.getProperty("user.home");
        Path parentForFS = Paths.get(usrHomePath);
        parentForFS = parentForFS.normalize();
        parentForFS = parentForFS.toAbsolutePath();
        parentForFS = parentForFS.toRealPath(LinkOption.NOFOLLOW_LINKS);
        return parentForFS;
    }
    protected static Path getUserHomeRWEDCheckedPath(){
        Path toReturn = Paths.get(System.getProperty("user.home"));
        try {
            toReturn = AppFileOperationsSimple.getUserHomePath();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("[ERROR] App Path " + toReturn.toString() + ", is not have a real directory " + ex.getMessage());
            System.exit(0);
        }
        
        try {
            pathIsNotDirectory(toReturn);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("[ERROR] Not directory " + toReturn.toString());
            System.exit(0);
        }
        try {
            pathIsNotReadWriteLink(toReturn);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
            System.exit(0);
        }
        return toReturn;
    }
    
    protected static Path getAppRWEDCheckedPath(){
        Path toReturn = Paths.get(System.getProperty("java.class.path"));
        try {
            toReturn = AppFileOperationsSimple.getAppPath();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("[ERROR] App Path " + toReturn.toString() + ", is not have a real directory " + ex.getMessage());
            System.exit(0);
        }
        
        try {
            pathIsNotDirectory(toReturn);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("[ERROR] Not directory " + toReturn.toString());
            System.exit(0);
        }
        try {
            pathIsNotReadWriteLink(toReturn);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
            System.exit(0);
        }
        return toReturn;
    }
    protected static Path getLogSubDir(){
        Path toReturn = Paths.get(getAppRWEDCheckedPath().toString(),
        AppFileNamesConstants.LOG_SUB_DIR);
        if( Files.notExists(toReturn, LinkOption.NOFOLLOW_LINKS) ){
            try {
                Files.createDirectories(toReturn);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
                System.exit(0);
            }
        }
        try {
            pathIsNotReadWriteLink(toReturn);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
            System.exit(0);
        }
        return toReturn;
    }
    protected static Path getLogForHtmlSubDir(){
        Path toReturn = Paths.get(getLogSubDir().toString(),
        AppFileNamesConstants.LOG_HTML_SUB_DIR);
        if( Files.notExists(toReturn, LinkOption.NOFOLLOW_LINKS) ){
            try {
                Files.createDirectories(toReturn);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
                System.exit(0);
            }
        }
        try {
            pathIsNotReadWriteLink(toReturn);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
            System.exit(0);
        }
        return toReturn;
    }
    protected static Path getLogForHtmlCurrentLogAnySubDir(Path currentLogSubDir, String anySubDirName){
        Path toReturn = Paths.get(currentLogSubDir.toString(),
        anySubDirName);
        if( Files.notExists(toReturn, LinkOption.NOFOLLOW_LINKS) ){
            try {
                Files.createDirectories(toReturn);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
                System.exit(0);
            }
        }
        try {
            pathIsNotReadWriteLink(toReturn);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
            System.exit(0);
        }
        return toReturn;
    }
    protected static Path getLogForHtmlCurrentLogSubDir(String currentDateTimeStamp){
        ReentrantLock therelck = new ReentrantLock();
        therelck.lock();
        try{
            Path toReturn = Paths.get(getLogSubDir().toString(),
            currentDateTimeStamp);
            if( Files.notExists(toReturn, LinkOption.NOFOLLOW_LINKS) ){
                try {
                    Files.createDirectories(toReturn);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
                    System.exit(0);
                }
            }
            try {
                pathIsNotReadWriteLink(toReturn);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
                System.exit(0);
            }

            return toReturn;
        } finally {

            therelck.unlock();
        }    
    }
    protected static ConcurrentSkipListMap<String, Path> getNewHtmlLogStorageFileSystem(Path currentDirForLog){
        ConcurrentSkipListMap<String, Path> listFilesForHtmlLog = new ConcurrentSkipListMap<String, Path>();
        Path logForHtmlCurrentLogAnySubDirCSS = getLogForHtmlCurrentLogAnySubDir(currentDirForLog, AppFileNamesConstants.LOG_HTML_CSS_SUB_DIR);
        
        listFilesForHtmlLog.put(AppFileNamesConstants.LOG_HTML_CSS_PREFIX, 
                Paths.get(logForHtmlCurrentLogAnySubDirCSS.toString(),
                AppFileNamesConstants.LOG_HTML_CSS_PREFIX
                + getNowTimeStringWithMS()
                + AppFileNamesConstants.LOG_HTML_CSS_EXT)
        );
        
        Path logForHtmlCurrentLogAnySubDirJS = getLogForHtmlCurrentLogAnySubDir(currentDirForLog, AppFileNamesConstants.LOG_HTML_JS_SUB_DIR);
        
        listFilesForHtmlLog.put(AppFileNamesConstants.LOG_HTML_JS_MENU_PREFIX, 
                Paths.get(logForHtmlCurrentLogAnySubDirJS.toString(),
                AppFileNamesConstants.LOG_HTML_JS_MENU_PREFIX
                + getNowTimeStringWithMS()
                + AppFileNamesConstants.LOG_HTML_JS_EXT)
        );
        
        listFilesForHtmlLog.put(AppFileNamesConstants.LOG_HTML_HEADER_PREFIX, 
                Paths.get(currentDirForLog.toString(),
                AppFileNamesConstants.LOG_HTML_HEADER_PREFIX
                + getNowTimeStringWithMS()
                + AppFileNamesConstants.LOG_HTML_EXT)
        );
        
        listFilesForHtmlLog.put(AppFileNamesConstants.LOG_HTML_FOOTER_PREFIX, 
                Paths.get(currentDirForLog.toString(),
                AppFileNamesConstants.LOG_HTML_FOOTER_PREFIX
                + getNowTimeStringWithMS()
                + AppFileNamesConstants.LOG_HTML_EXT)
        );
        
        listFilesForHtmlLog.put(AppFileNamesConstants.LOG_HTML_MENU_PREFIX, 
                Paths.get(currentDirForLog.toString(),
                AppFileNamesConstants.LOG_HTML_MENU_PREFIX
                + getNowTimeStringWithMS()
                + AppFileNamesConstants.LOG_HTML_EXT)
        );
        
        listFilesForHtmlLog.put(AppFileNamesConstants.LOG_INDEX_PREFIX, 
                Paths.get(currentDirForLog.toString(),
                AppFileNamesConstants.LOG_INDEX_PREFIX
                + getNowTimeStringWithMS()
                + AppFileNamesConstants.LOG_HTML_EXT)
        );
        
        for( Map.Entry<String, Path> elementOfList : listFilesForHtmlLog.entrySet() ){
            Path toReturn = elementOfList.getValue();
            if( Files.exists(toReturn, LinkOption.NOFOLLOW_LINKS) ){
                try {
                    pathIsNotFile(toReturn);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("[ERROR] Not file " + toReturn.toString());
                }
                try {
                    pathIsNotReadWriteLink(toReturn);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
                }
                
            }
            try {
                Files.createFile(toReturn);
                try {
                    pathIsNotFile(toReturn);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("[ERROR] Not file " + toReturn.toString());
                }
                try {
                    pathIsNotReadWriteLink(toReturn);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Can`t createFile " + toReturn.toString());
            }
        }
        return listFilesForHtmlLog;
    }
    protected static ConcurrentSkipListMap<String, Path> getNewLogFileInLogHTML(Path currentDirForLog){
        ConcurrentSkipListMap<String, Path> listFilesForHtmlLog = new ConcurrentSkipListMap<String, Path>();
        Path logForHtmlCurrentLogAnySubDirCSS = getLogForHtmlCurrentLogAnySubDir(currentDirForLog, AppFileNamesConstants.LOG_HTML_CSS_SUB_DIR);
        
        listFilesForHtmlLog.put(AppFileNamesConstants.LOG_HTML_CSS_PREFIX, 
                Paths.get(logForHtmlCurrentLogAnySubDirCSS.toString(),
                AppFileNamesConstants.LOG_HTML_CSS_PREFIX
                + getNowTimeStringWithMS()
                + AppFileNamesConstants.LOG_HTML_CSS_EXT)
        );
        
        Path logForHtmlCurrentLogAnySubDirJS = getLogForHtmlCurrentLogAnySubDir(currentDirForLog, AppFileNamesConstants.LOG_HTML_JS_SUB_DIR);
        
        listFilesForHtmlLog.put(AppFileNamesConstants.LOG_HTML_JS_MENU_PREFIX, 
                Paths.get(logForHtmlCurrentLogAnySubDirJS.toString(),
                AppFileNamesConstants.LOG_HTML_JS_MENU_PREFIX
                + getNowTimeStringWithMS()
                + AppFileNamesConstants.LOG_HTML_JS_EXT)
        );
        
        listFilesForHtmlLog.put(AppFileNamesConstants.LOG_HTML_HEADER_PREFIX, 
                Paths.get(currentDirForLog.toString(),
                AppFileNamesConstants.LOG_HTML_HEADER_PREFIX
                + getNowTimeStringWithMS()
                + AppFileNamesConstants.LOG_HTML_EXT)
        );
        
        listFilesForHtmlLog.put(AppFileNamesConstants.LOG_HTML_FOOTER_PREFIX, 
                Paths.get(currentDirForLog.toString(),
                AppFileNamesConstants.LOG_HTML_FOOTER_PREFIX
                + getNowTimeStringWithMS()
                + AppFileNamesConstants.LOG_HTML_EXT)
        );
        
        listFilesForHtmlLog.put(AppFileNamesConstants.LOG_HTML_MENU_PREFIX, 
                Paths.get(currentDirForLog.toString(),
                AppFileNamesConstants.LOG_HTML_MENU_PREFIX
                + getNowTimeStringWithMS()
                + AppFileNamesConstants.LOG_HTML_EXT)
        );
        
        listFilesForHtmlLog.put(AppFileNamesConstants.LOG_HTML_TABLE_PREFIX, 
                Paths.get(currentDirForLog.toString(),
                AppFileNamesConstants.LOG_HTML_TABLE_PREFIX
                + getNowTimeStringWithMS()
                + AppFileNamesConstants.LOG_HTML_EXT)
        );
        
        listFilesForHtmlLog.put(AppFileNamesConstants.LOG_INDEX_PREFIX, 
                Paths.get(currentDirForLog.toString(),
                AppFileNamesConstants.LOG_INDEX_PREFIX
                + getNowTimeStringWithMS()
                + AppFileNamesConstants.LOG_HTML_EXT)
        );
        
        for( Map.Entry<String, Path> elementOfList : listFilesForHtmlLog.entrySet() ){
            Path toReturn = elementOfList.getValue();
            if( Files.exists(toReturn, LinkOption.NOFOLLOW_LINKS) ){
                try {
                    pathIsNotFile(toReturn);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("[ERROR] Not file " + toReturn.toString());
                }
                try {
                    pathIsNotReadWriteLink(toReturn);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
                }
                
            }
            try {
                Files.createFile(toReturn);
                try {
                    pathIsNotFile(toReturn);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("[ERROR] Not file " + toReturn.toString());
                }
                try {
                    pathIsNotReadWriteLink(toReturn);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Can`t createFile " + toReturn.toString());
            }
        }
        return listFilesForHtmlLog;
    }
    
    protected static Path getNewLogHtmlTableFile(Path currentDirForLog){
        
        Path toReturn = Paths.get(currentDirForLog.toString(),
                AppFileNamesConstants.LOG_HTML_TABLE_PREFIX
                + getNowTimeStringWithMS()
                + AppFileNamesConstants.LOG_HTML_EXT);
        
        if( Files.exists(toReturn, LinkOption.NOFOLLOW_LINKS) ){
            try {
                pathIsNotFile(toReturn);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Not file " + toReturn.toString());
            }
            try {
                pathIsNotReadWriteLink(toReturn);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
            }
            return toReturn;
        }
        try {
            Files.createFile(toReturn);
            try {
                pathIsNotFile(toReturn);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Not file " + toReturn.toString());
            }
            try {
                pathIsNotReadWriteLink(toReturn);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("[ERROR] Can`t createFile " + toReturn.toString());
        }
        return toReturn;
    }
    protected static Path getNewLogFile(){
        
        Path toReturn = Paths.get(getLogSubDir().toString(), 
                getNowTimeStringWithMS()
                + AppFileNamesConstants.LOG_EXT);
        
        if( Files.exists(toReturn, LinkOption.NOFOLLOW_LINKS) ){
            try {
                pathIsNotFile(toReturn);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Not file " + toReturn.toString());
            }
            try {
                pathIsNotReadWriteLink(toReturn);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
            }
            return toReturn;
        }
        try {
            Files.createFile(toReturn);
            try {
                pathIsNotFile(toReturn);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Not file " + toReturn.toString());
            }
            try {
                pathIsNotReadWriteLink(toReturn);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Not readable, writeable or link " + toReturn.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("[ERROR] Can`t createFile " + toReturn.toString());
        }
        return toReturn;
    }
    protected static Path getAppPath() throws IOException{
        String appPath = System.getProperty("java.class.path");
        Path parentForFS = Paths.get(appPath);
        parentForFS = parentForFS.normalize();
        parentForFS = parentForFS.toAbsolutePath();
        parentForFS = parentForFS.toRealPath(LinkOption.NOFOLLOW_LINKS);
        return parentForFS;
    }
    private static void pathIsNotFile(Path innerWorkPath) throws IOException{
        if ( !Files.exists(innerWorkPath, LinkOption.NOFOLLOW_LINKS) ){
            System.out.println("[ERROR] File or Directory not exist: " + innerWorkPath.toString());
            throw new IOException("[ERROR] File or Directory not exist: " + innerWorkPath.toString());
        }
        if ( Files.isDirectory(innerWorkPath, LinkOption.NOFOLLOW_LINKS) ){
            System.out.println("[ERROR] Directory exist and it is not a File: " + innerWorkPath.toString());
            throw new IOException("[ERROR] Directory exist and it is not a File: " + innerWorkPath.toString());
        }
    }
    private static void pathIsNotDirectory(Path innerWorkPath) throws IOException{
        if ( !Files.exists(innerWorkPath, LinkOption.NOFOLLOW_LINKS) ){
            System.out.println("[ERROR] File or Directory exist and it is not a Directory: " + innerWorkPath.toString());
            throw new IOException("[ERROR] File or Directory exist and it is not a Directory: " + innerWorkPath.toString());
        }
        if ( !Files.isDirectory(innerWorkPath, LinkOption.NOFOLLOW_LINKS) ){
            System.out.println("[ERROR] File exist and it is not a Directory: " + innerWorkPath.toString());
            throw new IOException("[ERROR] File exist and it is not a Directory: " + innerWorkPath.toString());
        }
    }
    private static void pathIsNotReadWriteLink(Path innerWorkPath) throws IOException{
        if ( !Files.isReadable(innerWorkPath) ){
            System.out.println("[ERROR] File or Directory exist and it is not a Readable: " + innerWorkPath.toString());
            throw new IOException("[ERROR] File or Directory exist and it is not a Readable: " + innerWorkPath.toString());
        }
        if ( !Files.isWritable(innerWorkPath) ){
            System.out.println("[ERROR] File or Directory exist and it is not a Writable: " + innerWorkPath.toString());
            throw new IOException("[ERROR] File or Directory exist and it is not a Writable: " + innerWorkPath.toString());
        }
        if ( Files.isSymbolicLink(innerWorkPath) ){
            System.out.println("[ERROR] File or Directory exist and it is not a SymbolicLink: " + innerWorkPath.toString());
            throw new IOException("[ERROR] File or Directory exist and it is a SymbolicLink: " + innerWorkPath.toString());
        }
    }
    protected static String getNowTimeStringWithMS(){
        long currentDateTime = System.currentTimeMillis();
      
       //creating Date from millisecond
       Date currentDate = new Date(currentDateTime);
      
       //printing value of Date
       //System.out.println("current Date: " + currentDate);
      
       DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
       
       //formatted value of current Date
       return df.format(currentDate);
    }
    protected static String getNowTimeString(){
        ReentrantLock forGetTimelck = new ReentrantLock();
        forGetTimelck.lock();
        try{
           long currentDateTime = System.currentTimeMillis();

           //creating Date from millisecond
           Date currentDate = new Date(currentDateTime);

           //printing value of Date
           //System.out.println("current Date: " + currentDate);

           DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

           //formatted value of current Date
           return df.format(currentDate);
        } finally {
            forGetTimelck.unlock();
        }
    }
    protected static ArrayList<Path> getFilesByMaskFromDir(Path dirForRead, String maskForReturn){
        ArrayList<Path> toReturn = new ArrayList<Path>();
        int count = 0;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirForRead, maskForReturn)) {
        for (Path entry : stream) {
            count++;
            toReturn.add(entry);
        }
        if( count == 0 ){
            //System.out.println("Directory is Empty, put some " + maskForReturn + " files into " + dirForRead.toString());
        }
        } catch (IOException | DirectoryIteratorException ex) {
            ex.printStackTrace();
        }
        return toReturn;
    }
    protected static void writeToFile(Path fileForWrite, ArrayList<String> strText){
        ReentrantLock forWriteToFilelck = new ReentrantLock();
        forWriteToFilelck.lock();
        try{
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileForWrite.toString())))
            {
                for(String itemStr : strText){
                    String text = itemStr.toString();
                    bw.write(text);
                    bw.newLine();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        } finally {
            forWriteToFilelck.unlock();
        }    
    }
    protected static ArrayList<String> readFromFile(Path fileForWrite){
        ReentrantLock forReadFormFilelck = new ReentrantLock();
        forReadFormFilelck.lock();
        try{
            ArrayList<String> strForReturn;
            strForReturn = new ArrayList<String>();
            try(BufferedReader br = new BufferedReader(new FileReader(fileForWrite.toString())))
            {
                String s;
                while((s=br.readLine())!=null){
                    strForReturn.add(s.trim());
                }
            }
             catch(IOException ex){
                ex.printStackTrace();
            }   
            return strForReturn;
        } finally {
            forReadFormFilelck.unlock();
        }    
    }
    
}
