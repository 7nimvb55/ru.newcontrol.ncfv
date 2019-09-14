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
import java.net.URI;
import java.nio.file.ClosedFileSystemException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.ProviderNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedTransferQueue;
import java.util.regex.PatternSyntaxException;

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
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Directory create not complete path is " 
                        + inputedDirName.toString() 
                        + AdilConstants.EXCEPTION_MSG 
                        + exAlreadyExist.getMessage());
                exAlreadyExist.printStackTrace();
            } catch (SecurityException exSecurity) {
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Directory create not complete path is " 
                        + inputedDirName.toString() 
                        + AdilConstants.EXCEPTION_MSG 
                        + exSecurity.getMessage());
                exSecurity.printStackTrace();
            } catch (UnsupportedOperationException exUnSupp) {
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Directory create not complete path is " 
                        + inputedDirName.toString() 
                        + AdilConstants.EXCEPTION_MSG 
                        + exUnSupp.getMessage());
                exUnSupp.printStackTrace();
            } catch (IOException exIoExist) {
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Directory create not complete path is " 
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
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] File or Directory exist check not complete path is " 
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
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] File or Directory exist check not complete path is " 
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
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] File or Directory exist check not complete path is " 
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
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] File or Directory exist check not complite path is " 
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
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] File or Directory readable check not complete path is " 
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
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] File or Directory readable check not complete path is " 
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
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] File or Directory readable check not complete path is " 
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
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "getApplicationPath returned null");
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
     * 
     * @return User home path or null if check not successful
     */
    protected static Path getUserHomeCheckedPath(){
        Path toReturn;
        try {
            toReturn = getUserHomePath();
            if( toReturn == null ){
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "getUserHomeCheckedPath returned null");
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
            System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "getForLogDirectory returned " 
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
            System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "getForLogDirectory returned " 
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
     * @return String to normalize, absolute, real path or null if exceptions or errors for normalize... etc
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
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Application Directory build not complete path is " 
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
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Application Directory absolute path build not complete path is " 
                        + processedStrPath
                        + AdilConstants.EXCEPTION_MSG 
                        + exSec.getMessage()
                );
                exSec.printStackTrace();
                return null;
            } catch(IOError errIo) {
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Application Directory absolute path build not complete path is " 
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
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Application Directory absolute path build not complete path is " 
                        + processedStrPath
                        + AdilConstants.EXCEPTION_MSG 
                        + exSec.getMessage()
                );
                exSec.printStackTrace();
                return null;
            } catch(IOException exIo) {
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Application Directory absolute path build not complete path is " 
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
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Application Directory build not complete path is " 
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
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Application Directory absolute path build not complete path is " 
                        + appPath 
                        + AdilConstants.EXCEPTION_MSG 
                        + exSec.getMessage()
                );
                exSec.printStackTrace();
                return null;
            } catch(IOError errIo) {
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Application Directory absolute path build not complete path is " 
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
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Application Directory absolute path build not complete path is " 
                        + appPath 
                        + AdilConstants.EXCEPTION_MSG 
                        + exSec.getMessage()
                );
                exSec.printStackTrace();
                return null;
            } catch(IOException exIo) {
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Application Directory absolute path build not complete path is " 
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
     * @return User home path or null if exceptions or errors for normalize... etc
     */
    protected static Path getUserHomePath(){
        String userHomePath = new String();
        Path parentForFs;
        Path parentForFsNormal;
        Path parentForFsAbsolute;
        Path parentForFsReal;
        try {
            userHomePath = getSystemPropertyUserHome();
            try {
                parentForFs = Paths.get(userHomePath);
            } catch(InvalidPathException exInvPath) {
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Application Directory build not complete path is " 
                        + userHomePath 
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
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Application Directory absolute path build not complete path is " 
                        + userHomePath 
                        + AdilConstants.EXCEPTION_MSG 
                        + exSec.getMessage()
                );
                exSec.printStackTrace();
                return null;
            } catch(IOError errIo) {
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Application Directory absolute path build not complete path is " 
                        + userHomePath 
                        + AdilConstants.EXCEPTION_MSG 
                        + errIo.getMessage()
                );
                errIo.printStackTrace();
                return null;
            }
            try {
                parentForFsReal = parentForFsAbsolute.toRealPath(LinkOption.NOFOLLOW_LINKS);
            } catch(SecurityException exSec) {
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Application Directory absolute path build not complete path is " 
                        + userHomePath 
                        + AdilConstants.EXCEPTION_MSG 
                        + exSec.getMessage()
                );
                exSec.printStackTrace();
                return null;
            } catch(IOException exIo) {
                System.err.println(AdihFileOperations.class.getCanonicalName() 
                        + "[ERROR] Application Directory absolute path build not complete path is " 
                        + userHomePath 
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
            AdihUtilization.utilizeStringValues(new String[]{userHomePath});
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
    /**
     * 
     * @param storageFileOuter
     * @param fsForOpenOuter
     * @return FileSystem or null if it not must be opened
     */
    protected static FileSystem getStorageFileSystem(Path storageFileOuter, URI fsForOpenOuter){
        if( storageFileOuter == null ){
            return null;
        }
        if( fsForOpenOuter == null ){
            return null;
        }
        FileSystem fileSystemOpened = null;
        Path storageFile = null;
        URI fsForOpen = null;
        Boolean pathIsFile = Boolean.TRUE;
        try {
            storageFile = (Path) storageFileOuter;
            fsForOpen = (URI) fsForOpenOuter;

            pathIsFile = AdihFileOperations.pathIsFile(storageFile);
            if(pathIsFile){
                try {
                    fileSystemOpened = FileSystems.getFileSystem(fsForOpen);
                } catch(FileSystemAlreadyExistsException exAlExist){
                    System.err.println(AdihFileOperations.class.getCanonicalName() 
                            + " error for open storage for index in file "
                            + pathIsFile.toString() + ", reason " 
                            + exAlExist.getMessage());
                    exAlExist.printStackTrace();
                } catch(FileSystemNotFoundException exFsNotExist){
                    System.err.println(AdihFileOperations.class.getCanonicalName() 
                            + " error for open storage for index in file "
                            + pathIsFile.toString() + ", reason " 
                            + exFsNotExist.getMessage());
                    exFsNotExist.printStackTrace();
                } catch(ProviderNotFoundException exProvNotFound){
                    System.err.println(AdihFileOperations.class.getCanonicalName() 
                            + " error for open storage for index in file "
                            + pathIsFile.toString() + ", reason "
                            + exProvNotFound.getMessage());
                    exProvNotFound.printStackTrace();
                } catch(IllegalArgumentException exIllArg){
                    System.err.println(AdihFileOperations.class.getCanonicalName() 
                            + " error for open storage for index in file "
                            + pathIsFile.toString() + ", reason "
                            + exIllArg.getMessage());
                    exIllArg.printStackTrace();
                } catch(SecurityException exSec){
                    System.err.println(AdihFileOperations.class.getCanonicalName() 
                            + " error for open storage for index in file "
                            + pathIsFile.toString() + ", reason "
                            + exSec.getMessage());
                    exSec.printStackTrace();
                }
                if( fileSystemOpened != null ){
                    return null;
                }
                if( fileSystemOpened.isOpen() ){
                    return fileSystemOpened;
                } else {
                    try {
                        fileSystemOpened = FileSystems.newFileSystem(fsForOpen, getFsPropExist());
                        if( fileSystemOpened.isOpen() ){
                            return fileSystemOpened;
                        }
                    } catch(FileSystemAlreadyExistsException exAlExist){
                        System.err.println(AdihFileOperations.class.getCanonicalName() 
                                + " error for open storage for index in file "
                                + pathIsFile.toString() + ", reason " 
                                + exAlExist.getMessage());
                        exAlExist.printStackTrace();
                    } catch(FileSystemNotFoundException exFsNotExist){
                        System.err.println(AdihFileOperations.class.getCanonicalName() 
                                + " error for open storage for index in file "
                                + pathIsFile.toString() + ", reason " 
                                + exFsNotExist.getMessage());
                        exFsNotExist.printStackTrace();
                    } catch(ProviderNotFoundException exProvNotFound){
                        System.err.println(AdihFileOperations.class.getCanonicalName() 
                                + " error for open storage for index in file "
                                + pathIsFile.toString() + ", reason " 
                                + exProvNotFound.getMessage());
                        exProvNotFound.printStackTrace();
                    } catch(IllegalArgumentException exIllArg){
                        System.err.println(AdihFileOperations.class.getCanonicalName() 
                                + " error for open storage for index in file "
                                + pathIsFile.toString() + ", reason "
                                + exIllArg.getMessage());
                        exIllArg.printStackTrace();
                    } catch(SecurityException exSec){
                        System.err.println(AdihFileOperations.class.getCanonicalName() 
                                + " error for open storage for index in file "
                                + pathIsFile.toString() + ", reason " 
                                + exSec.getMessage());
                        exSec.printStackTrace();
                    } catch (IOException exIo) {
                        System.err.println(AdihFileOperations.class.getCanonicalName() 
                                + " error for open storage for index in file "
                                + pathIsFile.toString() + ", reason "
                                + exIo.getMessage());
                        exIo.printStackTrace();
                    }
                }
            } else {
                try {
                    fileSystemOpened = FileSystems.newFileSystem(fsForOpen, getFsPropCreate());
                    if( fileSystemOpened.isOpen() ){
                        return fileSystemOpened;
                    }
                } catch(FileSystemAlreadyExistsException exAlExist){
                    System.err.println(AdihFileOperations.class.getCanonicalName() 
                            + " error for open storage for index in file "
                            + pathIsFile.toString() + ", reason "
                            + exAlExist.getMessage());
                    exAlExist.printStackTrace();
                } catch(FileSystemNotFoundException exFsNotExist){
                    System.err.println(AdihFileOperations.class.getCanonicalName() 
                            + " error for open storage for index in file "
                            + pathIsFile.toString() + ", reason "
                            + exFsNotExist.getMessage());
                    exFsNotExist.printStackTrace();
                } catch(ProviderNotFoundException exProvNotFound){
                    System.err.println(AdihFileOperations.class.getCanonicalName() 
                            + " error for open storage for index in file "
                            + pathIsFile.toString() + ", reason "
                            + exProvNotFound.getMessage());
                    exProvNotFound.printStackTrace();
                } catch(IllegalArgumentException exIllArg){
                    System.err.println(AdihFileOperations.class.getCanonicalName() 
                            + " error for open storage for index in file "
                            + pathIsFile.toString() + ", reason "
                            + exIllArg.getMessage());
                    exIllArg.printStackTrace();
                } catch(SecurityException exSec){
                    System.err.println(AdihFileOperations.class.getCanonicalName() 
                            + " error for open storage for index in file "
                            + pathIsFile.toString() + ", reason "
                            + exSec.getMessage());
                    exSec.printStackTrace();
                } catch (IOException exIo) {
                    System.err.println(AdihFileOperations.class.getCanonicalName() 
                            + " error for open storage for index in file "
                            + pathIsFile.toString() + ", reason "
                            + exIo.getMessage());
                    exIo.printStackTrace();
                }
            }
            return fileSystemOpened;
        } finally {
            fileSystemOpened = null;
            storageFile = null;
            fsForOpen = null;
            pathIsFile = null;
        }
    }
    /**
     * 
     * @param openedStorage
     * @return 
     */
    protected static Boolean closeOpenedStorage(FileSystem openedStorage){
        String openedStorageToString = openedStorage.toString();
        Boolean isOpened = openedStorage.isOpen();
        try {
            if(isOpened){
                try {
                    openedStorage.close();
                    return Boolean.TRUE;
                } catch(ClosedFileSystemException exClose){
                    System.err.println(AdihFileOperations.class.getCanonicalName() 
                            + " error for close storage for index in file "
                            + openedStorageToString + ", reason "
                            + exClose.getMessage());
                    exClose.printStackTrace();
                } catch(UnsupportedOperationException exUnSupEx){
                    System.err.println(AdihFileOperations.class.getCanonicalName() 
                            + " error for close storage for index in file "
                            + openedStorageToString + ", reason "
                            + exUnSupEx.getMessage());
                    exUnSupEx.printStackTrace();
                }  catch(SecurityException exSec){
                    System.err.println(AdihFileOperations.class.getCanonicalName() 
                            + " error for close storage for index in file "
                            + openedStorageToString + ", reason "
                            + exSec.getMessage());
                    exSec.printStackTrace();
                } catch (IOException exIo) {
                    System.err.println(AdihFileOperations.class.getCanonicalName() 
                            + " error for close storage for index in file "
                            + openedStorageToString + ", reason "
                            + exIo.getMessage());
                    exIo.printStackTrace();
                }
            }
            return Boolean.FALSE;
        } finally {
            isOpened = null;
            AdihUtilization.utilizeStringValues(new String[]{openedStorageToString});
            openedStorageToString = null;
}
    }
    /**
     * 
     * @param prefixForFound
     * @return
     * @throws IOException 
     */
    protected static Path searchinIndexDirStorageByPrefix(Path searchedDirectory, String prefixForFound){
        if( searchedDirectory == null ){
            return null;
        }
        if( prefixForFound == null ){
            return null;
        }
        if( prefixForFound.isEmpty() ){
            return null;
        }
        Path indexFolder;
        LinkedTransferQueue<Path> filesByMaskFromDir = null;
        Path getMaxCompared = (Path) searchedDirectory;
        Path itemMask = null;
        Integer compareResult = 0;
        Boolean isSomeOneFound = Boolean.FALSE;
        try {
            indexFolder = (Path) searchedDirectory;
            filesByMaskFromDir = getFilesByMaskFromDir(indexFolder, 
                    "{" + prefixForFound + "}*");
            if( filesByMaskFromDir == null ){
                return null;
            }
            if( filesByMaskFromDir.isEmpty() ){
                return null;
            }
            do {
                itemMask = filesByMaskFromDir.poll();
                if( itemMask != null ){
                    try{
                        compareResult = getMaxCompared.compareTo(itemMask);
                    } catch (ClassCastException exClassCast) {
                        System.err.println(AdihFileOperations.class.getCanonicalName() 
                            + " error for compare, searched in index directory, file names "
                            + ", reason "
                            + exClassCast.getMessage());
                        exClassCast.printStackTrace();
                    }
                    if( compareResult > 0 ){
                        getMaxCompared = itemMask;
                        isSomeOneFound = Boolean.TRUE;
                    }
                }
            } while( !filesByMaskFromDir.isEmpty() );
            if( isSomeOneFound ){
                return getMaxCompared;
            }
            return null;
        } finally {
            indexFolder = null;
            AdihUtilization.utilizeLinkedTransferQueue(filesByMaskFromDir);
            filesByMaskFromDir = null;
            getMaxCompared = null;
            itemMask = null;
            compareResult = null;
            isSomeOneFound = null;
        }
    }
    /**
     * Return contained in directory files, directories... etc (Path object)
     * by mask equal of regular expression
     * @param dirForRead
     * @param maskForReturn
     * @return 
     */
    protected static LinkedTransferQueue<Path> getFilesByMaskFromDir(Path dirForRead, String maskForReturn){
        LinkedTransferQueue<Path> toReturn = new LinkedTransferQueue<Path>();
        try {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirForRead, maskForReturn)) {
                for (Path entry : stream) {
                    toReturn.add(entry);
                }
            } catch (PatternSyntaxException exPatternSyntax) {
                System.err.println(AdihHelper.class.getCanonicalName() 
                            + " error for search files by prefix "
                            + maskForReturn + " index in directory "
                            + dirForRead.toString() + ", reason "
                            + exPatternSyntax.getMessage());
                exPatternSyntax.printStackTrace();
                return null;
            } catch (NotDirectoryException  exNotDir) {
                System.err.println(AdihHelper.class.getCanonicalName() 
                            + " error for search files by prefix "
                            + maskForReturn + " index in directory "
                            + dirForRead.toString() + ", reason "
                            + exNotDir.getMessage());
                exNotDir.printStackTrace();
                return null;
            } catch (DirectoryIteratorException exDirIter) {
                System.err.println(AdihHelper.class.getCanonicalName() 
                            + " error for search files by prefix "
                            + maskForReturn + " index in directory "
                            + dirForRead.toString() + ", reason "
                            + exDirIter.getMessage());
                exDirIter.printStackTrace();
                return null;
            } catch (SecurityException  exSec) {
                System.err.println(AdihHelper.class.getCanonicalName() 
                            + " error for search files by prefix "
                            + maskForReturn + " index in directory "
                            + dirForRead.toString() + ", reason "
                            + exSec.getMessage());
                exSec.printStackTrace();
                return null;
            } catch (IOException exIo) {
                System.err.println(AdihHelper.class.getCanonicalName() 
                            + " error for search files by prefix "
                            + maskForReturn + " index in directory "
                            + dirForRead.toString() + ", reason "
                            + exIo.getMessage());
                exIo.printStackTrace();
                return null;
            }
            return toReturn;
        } finally {
            AdihUtilization.utilizeLinkedTransferQueue(toReturn);
            toReturn = null;
        }
    }
    /**
     * 
     * @return 
     */
    private static Map<String, String> getFsPropCreate(){
        Map<String, String> zipfsPropeties = new HashMap<>();
        zipfsPropeties.put("create","true");
        zipfsPropeties.put("encoding","UTF-8");
        
        return zipfsPropeties;
    }
    /**
     * 
     * @return 
     */
    private static Map<String, String> getFsPropExist(){
        Map<String, String> zipfsPropeties = new HashMap<>();
        zipfsPropeties.put("create","false");
        zipfsPropeties.put("encoding","UTF-8");
        return zipfsPropeties;
    }
}
