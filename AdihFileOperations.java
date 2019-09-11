/*
 * Copyright 2019 wladimirowichbiaran.
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

import java.io.IOError;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Adih
 * <ul>
 * <li>Automated
 * <li>data
 * <li>indexing
 * <li>helper File Operations
 * </ul>
 * @author wladimirowichbiaran
 */
public class AdihFileOperations {
    /**
     * TRUE if directory exist or create
     * @param inputedDirName
     * @return FALSE if directory not exist and not create
     */
    protected static Boolean createDirIfNotExist(Path inputedDirName){
         if( Files.notExists(inputedDirName, LinkOption.NOFOLLOW_LINKS) ){
            try{
                Files.createDirectories(inputedDirName);
                return Boolean.TRUE;
            } catch (FileAlreadyExistsException exAlreadyExist) {
                System.err.println("[ERROR] Directory create not complete path is " 
                        + inputedDirName.toString() 
                        + AdilConstants.EXCEPTION_MSG 
                        + exAlreadyExist.getMessage());
                exAlreadyExist.printStackTrace();
            } catch (SecurityException exSecurity) {
                System.err.println("[ERROR] Directory create not complete path is " 
                        + inputedDirName.toString() 
                        + AdilConstants.EXCEPTION_MSG 
                        + exSecurity.getMessage());
                exSecurity.printStackTrace();
            } catch (UnsupportedOperationException exUnSupp) {
                System.err.println("[ERROR] Directory create not complete path is " 
                        + inputedDirName.toString() 
                        + AdilConstants.EXCEPTION_MSG 
                        + exUnSupp.getMessage());
                exUnSupp.printStackTrace();
            } catch (IOException exIoExist) {
                System.err.println("[ERROR] Directory create not complete path is " 
                        + inputedDirName.toString() 
                        + AdilConstants.EXCEPTION_MSG 
                        + exIoExist.getMessage());
                exIoExist.printStackTrace();
            } 
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    /**
     * 
     * @param innerWorkPath
     */
    protected static Boolean pathIsFile(Path innerWorkPath){
        Boolean isExist = Boolean.FALSE;
        Boolean isDirectory = Boolean.FALSE;
        try {
            try {
                isExist = Files.exists(innerWorkPath, LinkOption.NOFOLLOW_LINKS);
            } catch(SecurityException exSecury) {
                System.err.println("[ERROR] File or Directory exist check not complete path is " 
                        + innerWorkPath.toString() 
                        + AdilConstants.EXCEPTION_MSG 
                        + exSecury.getMessage()
                );
                exSecury.printStackTrace();
            }
            if ( !isExist ){
                return Boolean.FALSE;
            }
            try {
                isDirectory = Files.isDirectory(innerWorkPath, LinkOption.NOFOLLOW_LINKS);
            } catch(SecurityException exSecury) {
                System.err.println("[ERROR] File or Directory exist check not complete path is " 
                        + innerWorkPath.toString() + " "
                        + AdilConstants.EXCEPTION_MSG 
                        + exSecury.getMessage()
                );
                exSecury.printStackTrace();
            }
            if ( isDirectory ){
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        } finally {
            isExist = null;
            isDirectory = null;
        }
    }
    /**
     * 
     * @param innerWorkPath
     */
    protected static Boolean pathIsDirectory(Path innerWorkPath){
        Boolean isExist = Boolean.FALSE;
        Boolean isDirectory = Boolean.FALSE;
        try {
            try {
                isExist = Files.exists(innerWorkPath, LinkOption.NOFOLLOW_LINKS);
            } catch(SecurityException exSecury) {
                System.err.println("[ERROR] File or Directory exist check not complete path is " 
                        + innerWorkPath.toString() 
                        + AdilConstants.EXCEPTION_MSG 
                        + exSecury.getMessage()
                );
                exSecury.printStackTrace();
            }
            if ( !isExist ){
                return Boolean.FALSE;
            }
            try {
                isDirectory = Files.isDirectory(innerWorkPath, LinkOption.NOFOLLOW_LINKS);
            } catch(SecurityException exSecury) {
                System.err.println("[ERROR] File or Directory exist check not complite path is " 
                        + innerWorkPath.toString() + " "
                        + AdilConstants.EXCEPTION_MSG 
                        + exSecury.getMessage()
                );
                exSecury.printStackTrace();
            }
            if ( isDirectory ){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } finally {
            isExist = null;
            isDirectory = null;
        }
    }
    /**
     * 
     * @param innerWorkPath
     */
    protected static Boolean pathIsReadWriteNotLink(Path innerWorkPath){
        Boolean isReadable = Boolean.FALSE;
        Boolean isWritable = Boolean.FALSE;
        Boolean isSymbolicLink = Boolean.FALSE;
        try {
            try {
                isReadable = Files.isReadable(innerWorkPath);
            } catch(SecurityException exSecury) {
                System.err.println("[ERROR] File or Directory readable check not complete path is " 
                        + innerWorkPath.toString() 
                        + AdilConstants.EXCEPTION_MSG 
                        + exSecury.getMessage()
                );
                exSecury.printStackTrace();
            }
            if ( !isReadable ){
                return Boolean.FALSE;
            }
            try {
                isWritable = Files.isWritable(innerWorkPath);
            } catch(SecurityException exSecury) {
                System.err.println("[ERROR] File or Directory readable check not complete path is " 
                        + innerWorkPath.toString() 
                        + AdilConstants.EXCEPTION_MSG 
                        + exSecury.getMessage()
                );
                exSecury.printStackTrace();
            }
            if ( !isWritable ){
                return Boolean.FALSE;
            }
            try {
                isSymbolicLink = Files.isSymbolicLink(innerWorkPath);
            } catch(SecurityException exSecury) {
                System.err.println("[ERROR] File or Directory readable check not complete path is " 
                        + innerWorkPath.toString() 
                        + AdilConstants.EXCEPTION_MSG 
                        + exSecury.getMessage()
                );
                exSecury.printStackTrace();
            }
            if ( isSymbolicLink ){
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        } finally {
            isReadable = null;
            isWritable = null;
            isSymbolicLink = null;
        }
    }
    /**
     * 
     * @return Application path or null if check not successful
     */
    protected static Path getAppCheckedPath(){
        Path toReturn;
        try {
            toReturn = getApplicationPath();
            if( toReturn == null ){
                System.err.println("getApplicationPath returned null");
                System.exit(0);
            }
            Boolean isDirectory = AdihFileOperations.pathIsDirectory(toReturn);
            Boolean isReadWriteNotLink = AdihFileOperations.pathIsReadWriteNotLink(toReturn);
            if( isDirectory ){
                if( isReadWriteNotLink ) {
                    return toReturn;
                }
            }
            return null;
        } finally {
            toReturn = null;
        }
    }
    /**
     * Check class path directory if not read, write or link
     * than check and return user.home
     * @return 
     */
    protected static Path getForLogDirectory(){
        Path toReturn;
        String systemPropertyPath;
        Boolean isDirectory;
        Boolean isReadWriteNotLink;
        try {
            systemPropertyPath = getSystemPropertyClassPath();
            toReturn = getNormAbsRealPath(systemPropertyPath);
            isDirectory = AdihFileOperations.pathIsDirectory(toReturn);
            isReadWriteNotLink = AdihFileOperations.pathIsReadWriteNotLink(toReturn);
            if( isDirectory ){
                if( isReadWriteNotLink ) {
                    return toReturn;
                }
            }
            System.err.println("getForLogDirectory returned " 
                    + toReturn.toString()
                    + " isDirectory " + String.valueOf(isDirectory)
                    + " isReadWriteNotLink " + String.valueOf(isReadWriteNotLink)
            );
            systemPropertyPath = getSystemPropertyUserHome();
            toReturn = getNormAbsRealPath(systemPropertyPath);
            isDirectory = AdihFileOperations.pathIsDirectory(toReturn);
            isReadWriteNotLink = AdihFileOperations.pathIsReadWriteNotLink(toReturn);
            if( isDirectory ){
                if( isReadWriteNotLink ) {
                    return toReturn;
                }
            }
            System.err.println("getForLogDirectory returned " 
                    + toReturn.toString()
                    + " isDirectory " + String.valueOf(isDirectory)
                    + " isReadWriteNotLink " + String.valueOf(isReadWriteNotLink)
            );
            return toReturn;
        } finally {
            toReturn = null;
            systemPropertyPath = null;
            isDirectory = null;
            isReadWriteNotLink = null;
        }
    }
    /**
     * 
     * @return Application path or null if exceptions or errors for normalize... etc
     */
    protected static Path getNormAbsRealPath(String inputedPath){
        String processedStrPath = new String();
        Path parentForFs;
        Path parentForFsNormal;
        Path parentForFsAbsolute;
        Path parentForFsReal;
        try {
            processedStrPath = inputedPath;
            try {
                parentForFs = Paths.get(processedStrPath);
            } catch(InvalidPathException exInvPath) {
                System.err.println("[ERROR] Application Directory build not complete path is " 
                        + processedStrPath
                        + AdilConstants.EXCEPTION_MSG 
                        + exInvPath.getMessage()
                );
                exInvPath.printStackTrace();
                return null;
            }
            parentForFsNormal = parentForFs.normalize();
            try {
                parentForFsAbsolute = parentForFsNormal.toAbsolutePath();
            } catch(SecurityException exSec) {
                System.err.println("[ERROR] Application Directory absolute path build not complete path is " 
                        + processedStrPath
                        + AdilConstants.EXCEPTION_MSG 
                        + exSec.getMessage()
                );
                exSec.printStackTrace();
                return null;
            } catch(IOError errIo) {
                System.err.println("[ERROR] Application Directory absolute path build not complete path is " 
                        + processedStrPath 
                        + AdilConstants.EXCEPTION_MSG 
                        + errIo.getMessage()
                );
                errIo.printStackTrace();
                return null;
            }
            try {
                parentForFsReal = parentForFsAbsolute.toRealPath(LinkOption.NOFOLLOW_LINKS);
            } catch(SecurityException exSec) {
                System.err.println("[ERROR] Application Directory absolute path build not complete path is " 
                        + processedStrPath
                        + AdilConstants.EXCEPTION_MSG 
                        + exSec.getMessage()
                );
                exSec.printStackTrace();
                return null;
            } catch(IOException exIo) {
                System.err.println("[ERROR] Application Directory absolute path build not complete path is " 
                        + processedStrPath 
                        + AdilConstants.EXCEPTION_MSG 
                        + exIo.getMessage()
                );
                exIo.printStackTrace();
                return null;
            }
            return parentForFsReal;
        } finally {
            parentForFs = null;
            parentForFsNormal = null;
            parentForFsAbsolute = null;
            parentForFsReal = null;
            AdihUtilization.utilizeStringValues(new String[]{processedStrPath});
        }
    }
    /**
     * 
     * @return Application path or null if exceptions or errors for normalize... etc
     */
    protected static Path getApplicationPath(){
        String appPath = new String();
        Path parentForFs;
        Path parentForFsNormal;
        Path parentForFsAbsolute;
        Path parentForFsReal;
        try {
            appPath = getSystemPropertyClassPath();
            try {
                parentForFs = Paths.get(appPath);
            } catch(InvalidPathException exInvPath) {
                System.err.println("[ERROR] Application Directory build not complete path is " 
                        + appPath 
                        + AdilConstants.EXCEPTION_MSG 
                        + exInvPath.getMessage()
                );
                exInvPath.printStackTrace();
                return null;
            }
            parentForFsNormal = parentForFs.normalize();
            try {
                parentForFsAbsolute = parentForFsNormal.toAbsolutePath();
            } catch(SecurityException exSec) {
                System.err.println("[ERROR] Application Directory absolute path build not complete path is " 
                        + appPath 
                        + AdilConstants.EXCEPTION_MSG 
                        + exSec.getMessage()
                );
                exSec.printStackTrace();
                return null;
            } catch(IOError errIo) {
                System.err.println("[ERROR] Application Directory absolute path build not complete path is " 
                        + appPath 
                        + AdilConstants.EXCEPTION_MSG 
                        + errIo.getMessage()
                );
                errIo.printStackTrace();
                return null;
            }
            try {
                parentForFsReal = parentForFsAbsolute.toRealPath(LinkOption.NOFOLLOW_LINKS);
            } catch(SecurityException exSec) {
                System.err.println("[ERROR] Application Directory absolute path build not complete path is " 
                        + appPath 
                        + AdilConstants.EXCEPTION_MSG 
                        + exSec.getMessage()
                );
                exSec.printStackTrace();
                return null;
            } catch(IOException exIo) {
                System.err.println("[ERROR] Application Directory absolute path build not complete path is " 
                        + appPath 
                        + AdilConstants.EXCEPTION_MSG 
                        + exIo.getMessage()
                );
                exIo.printStackTrace();
                return null;
            }
            return parentForFsReal;
        } finally {
            parentForFs = null;
            parentForFsNormal = null;
            parentForFsAbsolute = null;
            parentForFsReal = null;
            AdihUtilization.utilizeStringValues(new String[]{appPath});
        }
    }
    
    /**
     * 
     * @return 
     */
    protected static String getSystemPropertyClassPath(){
        String property = new String();
        try {
            property = System.getProperty("java.class.path");
            return property;
        } finally {
            AdihUtilization.utilizeStringValues(new String[]{property});
        }
    }
    /**
     * 
     * @return 
     */
    protected static String getSystemPropertyUserHome(){
        String property = new String();
        try {
            property = System.getProperty("user.home");
            return property;
        } finally {
            AdihUtilization.utilizeStringValues(new String[]{property});
        }
    }
}