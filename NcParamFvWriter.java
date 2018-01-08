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

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcParamFvWriter {
    /**
     *
     * @param paramInFuncToWriteCfg
     * @return
     */
    public static boolean wirteDataInWorkCfg(NcParamFv paramInFuncToWriteCfg){
        String strDataInAppDir = NcIdxFileManager.getWorkCfgPath();
        if( strDataInAppDir.length() < 1 ){
            return false;
        }
        if( NcParamFvManager.isNcParamFvDataEmpty(paramInFuncToWriteCfg)
                || paramInFuncToWriteCfg == null ){
            return false;
        }
        boolean isHash = NcParamFvManager.isNcParamFvDataHashTrue(paramInFuncToWriteCfg);
        if( !isHash ){
            NcParamFvManager.ncParamFvDataOutPut(paramInFuncToWriteCfg);
            return false;
        }
        try(ObjectOutputStream oos = 
                new ObjectOutputStream(
                new FileOutputStream(strDataInAppDir)))
        {
            oos.writeObject(paramInFuncToWriteCfg);
        }
        catch(Exception ex){
            Logger.getLogger(NcPreRunFileViewer.class.getName()).log(Level.SEVERE, null, ex); 
            return false;
        } 
        return true;
    }
}
