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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.Ncfv#main(java.lang.String[]) }
     * </ul>
     * @param strInputedKeyWord 
     */
    protected static void outToConsoleSearchByKeyFromInput(String strInputedKeyWord){
        TreeMap<Long, NcDcIdxDirListToFileAttr> searchedData;
        searchedData = makeSearchByKeyFromInput(strInputedKeyWord);
        for( Map.Entry<Long, NcDcIdxDirListToFileAttr> itemReaded : searchedData.entrySet() ){
            NcAppHelper.outMessageToConsole(itemReaded.getValue().path);
        }
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSIMASearchResultTableModel#NcSIMASearchResultTableModel(java.lang.String) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSrchGetResult#outToConsoleSearchByKeyFromInput(java.lang.String) }
     * </ul>
     * @param strForSearch
     * @return 
     */
    protected static TreeMap<Long, NcDcIdxDirListToFileAttr> makeSearchByKeyFromInput(String strForSearch){
        TreeMap<Long, NcDcIdxWordToFile> strHexForInVar = new TreeMap<Long, NcDcIdxWordToFile>();
        TreeMap<Long, NcDcIdxWordToFile> strDistInResult = new TreeMap<Long, NcDcIdxWordToFile>();
        
        strHexForInVar.putAll(NcSrchKeyWordInput.getDirListRecordByKeyWord(strForSearch));
        strDistInResult = NcSrchFileDataCompare.getDistictIDs(strHexForInVar);

        TreeMap<Long, NcDcIdxDirListToFileAttr> readedData = new TreeMap<Long, NcDcIdxDirListToFileAttr>();
        
        readedData.putAll(NcIdxDirListManager.getByListIDs(strDistInResult));
        
        return readedData;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.Ncfv#main(java.lang.String[]) }
     * </ul>
     */
    protected static void outToConsoleSearchByKeyFromFile(){
        TreeMap<Long, NcDcIdxDirListToFileAttr> searchedData;
        searchedData = makeSearchByKeyFromFile();
        for( Map.Entry<Long, NcDcIdxDirListToFileAttr> itemReaded : searchedData.entrySet() ){
            NcAppHelper.outMessageToConsole(itemReaded.getValue().path);
        }
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSIMASearchResultTableModel#NcSIMASearchResultTableModel()  }
     * <li>{@link ru.newcontrol.ncfv.NcSIMASearchResultTableModel#NcSIMASearchResultTableModel(java.util.ArrayList, java.util.ArrayList) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSrchGetResult#outToConsoleSearchByKeyFromFile() }
     * </ul>
     * Get KeyWordIn(Out)Search from file and output serch results
     * @return 
     */
    protected static TreeMap<Long, NcDcIdxDirListToFileAttr> makeSearchByKeyFromFile(){
        TreeMap<Long, NcDcIdxWordToFile> strHexForInVar = new TreeMap<Long, NcDcIdxWordToFile>();
        TreeMap<Long, NcDcIdxWordToFile> strHexForOutVar = new TreeMap<Long, NcDcIdxWordToFile>();
        TreeMap<Long, NcDcIdxWordToFile> strDistInResult = new TreeMap<Long, NcDcIdxWordToFile>();
        TreeMap<Long, NcDcIdxWordToFile> strDistOutResult = new TreeMap<Long, NcDcIdxWordToFile>();
        
        ArrayList<String> arrKeyWordInSearch = NcEtcKeyWordListManager.getKeyWordInSearchFromFile();
        for( String strItemIn : arrKeyWordInSearch ){
            strHexForInVar.putAll(NcSrchKeyWordInput.getDirListRecordByKeyWord(strItemIn));
        }
        
        strDistInResult = NcSrchFileDataCompare.getDistictIDs(strHexForInVar);
        
        ArrayList<String> arrKeyWordOutSearch = NcEtcKeyWordListManager.getKeyWordOutSearchFromFile();
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
     * Not used
     * @param strHexForInVar 
     */
    private static void outToConsoleSearchedIDs(TreeMap<Long, NcDcIdxWordToFile> strHexForInVar){
        for( Map.Entry<Long, NcDcIdxWordToFile> itemID : strHexForInVar.entrySet() ){
            NcAppHelper.outMessageToConsole("id: " + itemID.getValue().dirListID);
        }
        
    }
    /**
     * Not used
     * @param strHexForInVar
     * @param strHexForOutVar 
     */
    private static void outSearchResult(TreeMap<Long, NcDcIdxWordToFile> strHexForInVar, TreeMap<Long, NcDcIdxWordToFile> strHexForOutVar){
        TreeMap<Long, NcDcIdxWordToFile> CleanResult = NcSrchFileDataCompare.getIdInWithoutOfOutSearchResult(strHexForInVar, strHexForOutVar);
        NcAppHelper.outMessage(
            NcStrLogMsgField.INFO.getStr()
            + NcStrVarDescription.CLEAN_RESULT.getStr()
            + NcStrServiceMsg.COUNT_OF_SEARCHED_RECORDS.getStr() + CleanResult.size());
    }
}
