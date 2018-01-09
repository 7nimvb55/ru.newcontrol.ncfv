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

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSrchGetResult {
    /**
     * Get KeyWordIn(Out)Search from file and output serch results
     * @return 
     */
    public static TreeMap<Long, NcDcIdxDirListToFileAttr> makeSearchByKeyFromFile(){
        TreeMap<Long, NcDcIdxWordToFile> strHexForInVar = new TreeMap<Long, NcDcIdxWordToFile>();
        TreeMap<Long, NcDcIdxWordToFile> strHexForOutVar = new TreeMap<Long, NcDcIdxWordToFile>();
        TreeMap<Long, NcDcIdxWordToFile> strDistInResult = new TreeMap<Long, NcDcIdxWordToFile>();
        TreeMap<Long, NcDcIdxWordToFile> strDistOutResult = new TreeMap<Long, NcDcIdxWordToFile>();
        
        ArrayList<String> arrKeyWordInSearch = NcEtcKeyWordListManage.getKeyWordInSearchFromFile();
        for( String strItemIn : arrKeyWordInSearch ){
            strHexForInVar.putAll(NcSrchKeyWordInput.getDirListRecordByKeyWord(strItemIn));
        }
        
        strDistInResult = NcSrchFileDataCompare.getDistictIDs(strHexForInVar);
        
        ArrayList<String> arrKeyWordOutSearch = NcEtcKeyWordListManage.getKeyWordOutSearchFromFile();
        for( String strItemOut : arrKeyWordOutSearch ){
            strHexForOutVar.putAll(NcSrchKeyWordInput.getDirListRecordByKeyWord(strItemOut));
        }
        
        strDistOutResult = NcSrchFileDataCompare.getDistictIDs(strHexForOutVar);
        
        
        
        TreeMap<Long, NcDcIdxWordToFile> CleanResult = NcSrchFileDataCompare.getIdInWithoutOfOutSearchResult(strDistInResult, strDistOutResult);
        
        
        TreeMap<Long, NcDcIdxDirListToFileAttr> readedData = new TreeMap<Long, NcDcIdxDirListToFileAttr>();
        
        readedData.putAll(NcIdxDirListManager.getByListIDs(CleanResult));
        
        return readedData;
    }
    /**
     * 
     * @param strHexForInVar 
     */
    public static void outToConsoleSearchedIDs(TreeMap<Long, NcDcIdxWordToFile> strHexForInVar){
        for( Map.Entry<Long, NcDcIdxWordToFile> itemID : strHexForInVar.entrySet() ){
            NcAppHelper.outMessage("id: " + itemID.getValue().dirListID);
        }
        
    }
    /**
     * 
     * @param strHexForInVar
     * @param strHexForOutVar 
     */
    public static void outSearchResult(TreeMap<Long, NcDcIdxWordToFile> strHexForInVar, TreeMap<Long, NcDcIdxWordToFile> strHexForOutVar){
        TreeMap<Long, NcDcIdxWordToFile> CleanResult = NcSrchFileDataCompare.getIdInWithoutOfOutSearchResult(strHexForInVar, strHexForOutVar);
        NcAppHelper.outMessage("Count CleanResult records out: " + CleanResult.size());
    }
}
