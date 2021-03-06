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
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcParamFvReader {
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcEtcKeyWordListManager#getKeyWordInSearchFromFile() }
     * <li>{@link ru.newcontrol.ncfv.NcEtcKeyWordListManager#getKeyWordOutSearchFromFile() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getIndexWorkSubDirFilesList() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getTmpIdsFile() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#checkInIndexFolderContent() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcPreRunFileViewer#getCurrentWorkCfg() }
     * </ul>
     * Read data from serializable 
     * {@link ru.newcontrol.ncfv.NcParamFv}
     * saved on the disk in *.dat file
     * and return
     * @return empty object if it not read or not set before
     */
    protected static NcParamFv readDataFromWorkCfg(){
        NcParamFv readedDiskInfo;
        String strDataInAppDir = NcIdxFileManager.getWorkCfgPath();
        if( strDataInAppDir.length() < 1 ){
            NcLogLogicCfg.NcParamFvReaderReadDataGenerate();
            return new NcParamFv();
        }
        File fileJornalDisk = new File(strDataInAppDir);
        if( !NcIdxFileManager.fileExistRWAccessChecker(fileJornalDisk) ){
            NcLogLogicCfg.NcParamFvReaderReadDataGenerate();
            return new NcParamFv();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(strDataInAppDir)))
        {
            readedDiskInfo = (NcParamFv)ois.readObject();
            NcParamFvManager.checkFromRead(readedDiskInfo);
        }
        catch(Exception ex){
            NcAppHelper.logException(
                    NcPreRunFileViewer.class.getCanonicalName(), ex);
            NcLogLogicCfg.NcParamFvReaderReadDataGenerate();
            return new NcParamFv();
        }
        NcLogLogicCfg.NcParamFvReaderReadDataRead();
        return readedDiskInfo;
    }

}
