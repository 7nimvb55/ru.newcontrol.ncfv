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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    protected static Path getNewLogFile(){
        
        Path toReturn = Paths.get(getAppRWEDCheckedPath().toString(),
        AppFileNamesConstants.LOG_SUB_DIR,
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
      
       DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssMMM");
       
       //formatted value of current Date
       return df.format(currentDate);
    }
    protected static String getNowTimeString(){
        long currentDateTime = System.currentTimeMillis();
      
       //creating Date from millisecond
       Date currentDate = new Date(currentDateTime);
      
       //printing value of Date
       //System.out.println("current Date: " + currentDate);
      
       DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
       
       //formatted value of current Date
       return df.format(currentDate);
    }
}
