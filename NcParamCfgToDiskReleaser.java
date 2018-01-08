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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Администратор
 */
public class NcParamCfgToDiskReleaser {
    
    /**
     *
     * @param strIndexPath
     * @return
     */
    public static boolean checkOrCreateIdxDirStructure(String strIndexPath){
        File fileWorkDir = new File(strIndexPath);
        boolean boolResultCreation = false;
        
        boolResultCreation = NcIdxFileManager.dirExistRWAccessChecker(fileWorkDir);
        if( !boolResultCreation ){
            boolResultCreation = fileWorkDir.mkdirs();
        }
        String[] strSubDirs = NcManageCfg.getWorkSubDirList();
        for( String itemSubDir : strSubDirs ){
            boolResultCreation = boolResultCreation && createSubDir(fileWorkDir, itemSubDir);
        }
        return boolResultCreation;
    }

    /**
     *
     * @param existParentDir
     * @param subDirName
     * @return
     */
    public static boolean createSubDir(File existParentDir, String subDirName){
        String strPathName = NcIdxFileManager.strPathCombiner(existParentDir.getAbsolutePath(), subDirName);
        File fileForCreateDir = new File(strPathName);
        boolean boolCheck = NcIdxFileManager.dirExistRWAccessChecker(fileForCreateDir);
        if( !boolCheck ){
            return fileForCreateDir.mkdirs();
        }
        return boolCheck;
    }

    /**
     *
     * @param strFileName
     * @return
     */
    public static boolean checkOrCreateFiles(String strFileName){
        
        File fileForCreateDir = new File(strFileName);
        if( fileForCreateDir.isDirectory() ){
            return false;
        }
        boolean boolCheck = NcIdxFileManager.fileExistRWAccessChecker(fileForCreateDir);
        if( !boolCheck ){
            if( !NcIdxFileManager.dirExistRWAccessChecker(fileForCreateDir.getParentFile()) ){
                fileForCreateDir.getParentFile().mkdirs();
            }
            try {
                if( !fileForCreateDir.exists() ){
                    return fileForCreateDir.createNewFile();
                }
            } catch (IOException ex) {
                Logger.getLogger(NcParamCfgToDiskReleaser.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return boolCheck;
    }

    /**
     *
     * @param strFileName
     */
    public static void getWorkFileParams(String strFileName){
        File fileWork = new File(strFileName);
        
        NcAppHelper.outMessage("Path of work file: \n" + fileWork.getAbsolutePath()
                + "Exist: " + fileWork.exists() + "\n"
                + "canRead: " + fileWork.canRead() + "\n"
                + "canWrite: " + fileWork.canWrite() + "\n");
        
    }    

    /**
     *
     * @param strIndexPath
     */
    public static void getIdxDirStructure(String strIndexPath){
        File fileWorkDir = new File(strIndexPath);
        
        NcAppHelper.outMessage("Path of index directory: \n" + fileWorkDir.getAbsolutePath()
                + "Exist: " + fileWorkDir.exists() + "\n"
                + "canRead: " + fileWorkDir.canRead() + "\n"
                + "canWrite: " + fileWorkDir.canWrite() + "\n");
        
        
        String[] strSubDirs = NcManageCfg.getWorkSubDirList();
        for( String itemSubDir : strSubDirs ){
            String strPathSubDir = NcIdxFileManager.strPathCombiner(fileWorkDir.getAbsolutePath(), itemSubDir);
            File pathSubDir = new File(strPathSubDir);
            NcAppHelper.outMessage("Path of subDir: \n" + pathSubDir.getAbsolutePath()
                    + "Exist: " + pathSubDir.exists() + "\n"
                    + "canRead: " + pathSubDir.canRead() + "\n"
                    + "canWrite: " + pathSubDir.canWrite() + "\n");
        }
        
    }
}
