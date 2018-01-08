/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
            NcAppHelper.outMessage("Path length < 1, is: " + strDataInAppDir.length()
            + "\npath string is: " + strDataInAppDir);
            return false;
        }
        if( NcParamFvManager.isNcParamFvDataEmpty(paramInFuncToWriteCfg)
                || paramInFuncToWriteCfg == null ){
            NcAppHelper.outMessage("Data is Empty and not writed");
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
