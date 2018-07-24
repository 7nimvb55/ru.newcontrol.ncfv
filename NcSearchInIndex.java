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

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *  NcSearchInIndex.getWordSearchResult(strIn, strOut);
 * @author Администратор
 */
public class NcSearchInIndex {
    
    private NcIndexManageIDs ncThisManageIDs;
    private TreeMap<Long, NcDiskInfo> ncDiskInfo;

    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSIMASearchResultTableModel#NcSIMASearchResultTableModel(java.lang.String) }
     * </ul>
     */
    protected NcSearchInIndex() {
        NcIMinFS ncwd = new NcIMinFS();
        
        ncThisManageIDs = ncwd.getNcIndexManageIDs();
        ncDiskInfo = ncwd.getDiskInfo();
    }
    /** 
     * Not used
     * @param strKeyWordInSearch
     * @param strKeyWordOutSearch
     * @return 
     */ 
    private static void searchWordInIndex(){
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
        
        for( Map.Entry<Long, NcDcIdxDirListToFileAttr> itemReaded : readedData.entrySet() ){
            NcAppHelper.outMessage(itemReaded.getValue().path);
        }
    }
    /**
     * Not used
     * @param strKeyWordInSearch
     * @param strKeyWordOutSearch
     * @return 
     */
    private TreeMap<Long, NcDcIdxDirListToFileAttr> getWordSearchResult(ArrayList<String> strKeyWordInSearch, ArrayList<String> strKeyWordOutSearch){
        TreeMap<Long, NcDcIdxWordToFile> dataForInKeyWord = new TreeMap<Long, NcDcIdxWordToFile>();
        TreeMap<Long, NcDcIdxWordToFile> dataForOutKeyWord = new TreeMap<Long, NcDcIdxWordToFile>();
        TreeMap<Long, NcDcIdxDirListToFileAttr> retFormDiskDataResult = new TreeMap<Long, NcDcIdxDirListToFileAttr>();
        
        TreeMap<Long, NcDcIdxDirListToFileAttr> retFilteredDataResult = new TreeMap<Long, NcDcIdxDirListToFileAttr>();
        
        for(String items : strKeyWordInSearch){
            dataForInKeyWord.putAll(getIDsForKeyWord(items));
        }
        
        if(strKeyWordOutSearch.isEmpty()){
            for(Map.Entry<Long, NcDcIdxWordToFile> itemIDSearch : dataForInKeyWord.entrySet()){
                retFormDiskDataResult.putAll(NcIdxDirListFileReader.ncReadFromDirListFile(itemIDSearch.getValue().dirListID));
            
            //
            }
            return retFormDiskDataResult;
            //for all before coment and coment after
            
            /*for(Map.Entry<Long, NcDirListToFilesForIndex> itemClean : retFormDiskDataResult.entrySet()){
                if(itemClean.getValue().i == itemIDSearch.getValue().di){
                    retFilteredDataResult.put(itemClean.getKey(), itemClean.getValue());
                }
            }
            }
            return retFilteredDataResult;*/
        }
        
        for(String items : strKeyWordOutSearch){
            dataForOutKeyWord.putAll(getIDsForKeyWord(items));
        }
        TreeMap<Long, NcDcIdxWordToFile> dataForCompareKeyWord = new TreeMap<Long, NcDcIdxWordToFile>();
        Object clone = dataForInKeyWord.clone();
        dataForCompareKeyWord = (TreeMap<Long, NcDcIdxWordToFile>) clone;
        
        for(Map.Entry<Long, NcDcIdxWordToFile> itemForIn : dataForInKeyWord.entrySet()){
            NcDcIdxWordToFile dataInForCompare = itemForIn.getValue();
            for(Map.Entry<Long, NcDcIdxWordToFile> itemForOut : dataForOutKeyWord.entrySet()){
                NcDcIdxWordToFile dataOutForCompare = itemForOut.getValue();
                if(dataInForCompare.dirListID == dataOutForCompare.dirListID){
                    dataForCompareKeyWord.remove(itemForIn.getKey()) ;
                }
            }
        }
        
        
        
        for(Map.Entry<Long, NcDcIdxWordToFile> itemIDSearch : dataForCompareKeyWord.entrySet()){
            retFormDiskDataResult.putAll(NcIdxDirListFileReader.ncReadFromDirListFile(itemIDSearch.getValue().dirListID));
            for(Map.Entry<Long, NcDcIdxDirListToFileAttr> itemClean : retFormDiskDataResult.entrySet()){
                if(itemClean.getValue().dirListID == itemIDSearch.getValue().dirListID){
                    retFilteredDataResult.put(itemClean.getKey(), itemClean.getValue());
                }
            }
        }
            
        
        return retFilteredDataResult;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSearchInIndex#getWordSearchResult(java.util.ArrayList, java.util.ArrayList) }
     * </ul>
     * @param strKeyWordInSearch
     * @return 
     */
    private TreeMap<Long, NcDcIdxWordToFile> getIDsForKeyWord(String strKeyWordInSearch){
                long searchID = 0;
        
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureABC = NcPathToArrListStr.getStructureToRecord(
                NcPathToArrListStr.NCLVLABC.retArrListStr(strKeyWordInSearch),
                strKeyWordInSearch,
                searchID);
        
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureRABC = NcPathToArrListStr.getStructureToRecord(
                NcPathToArrListStr.NCLVLRABC.retArrListStr(strKeyWordInSearch),
                strKeyWordInSearch,
                searchID);
                
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureNUM = NcPathToArrListStr.getStructureToRecord(
                NcPathToArrListStr.NCLVLNUM.retArrListStr(strKeyWordInSearch),
                strKeyWordInSearch,
                searchID);
                        
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureSYM = NcPathToArrListStr.getStructureToRecord(
                NcPathToArrListStr.NCLVLSYM.retArrListStr(strKeyWordInSearch),
                strKeyWordInSearch,
                searchID);
                                
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureSPACE = NcPathToArrListStr.getStructureToRecord(
                NcPathToArrListStr.NCLVLSPACE.retArrListStr(strKeyWordInSearch),
                strKeyWordInSearch,
                searchID);
        
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureWord = new TreeMap<Long, NcDcIdxSubStringToOperationUse>();
        //For Word Length > 25
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureLongWord = new TreeMap<Long, NcDcIdxSubStringToOperationUse>();
        long lwIdx = 0;
        long wIdx = 0;
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureABC.entrySet()){
            if (item.getValue().strSubString.length() > 25){
                StructureLongWord.put(lwIdx, item.getValue());
                lwIdx++;
            }
            else{
                StructureWord.put(wIdx, item.getValue());
                wIdx++;
            }
        }
        
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureRABC.entrySet()){
            if (item.getValue().strSubString.length() > 25){
                StructureLongWord.put(lwIdx, item.getValue());
                lwIdx++;
            }
            else{
                StructureWord.put(wIdx, item.getValue());
                wIdx++;
            }
        }
        
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureNUM.entrySet()){
            if (item.getValue().strSubString.length() > 25){
                StructureLongWord.put(lwIdx, item.getValue());
                lwIdx++;
            }
            else{
                StructureWord.put(wIdx, item.getValue());
                wIdx++;
            }
        }
        
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureSYM.entrySet()){
            if (item.getValue().strSubString.length() > 25){
                StructureLongWord.put(lwIdx, item.getValue());
                lwIdx++;
            }
            else{
                StructureWord.put(wIdx, item.getValue());
                wIdx++;
            }
        }
        
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureSPACE.entrySet()){
            if (item.getValue().strSubString.length() > 25){
                StructureLongWord.put(lwIdx, item.getValue());
                lwIdx++;
            }
            else{
                StructureWord.put(wIdx, item.getValue());
                wIdx++;
            }
        }
        TreeMap<Long, NcDcIdxWordToFile> searchedWords = new TreeMap<Long, NcDcIdxWordToFile>();
        searchedWords.putAll(NcIdxLongWordManager.getLongWord(StructureLongWord));
        searchedWords.putAll(NcIdxWordManager.getWord(StructureWord));
        
        return searchedWords;
    }
    /**
     * Not used
     * @return 
     */
    private TreeMap<Long, NcDcIdxDirListToFileAttr> getKeyWordForSearch(){
        TreeMap<Long, NcDcIdxDirListToFileAttr> Word;
        Word = new TreeMap<Long, NcDcIdxDirListToFileAttr>();
        TreeMap<Long, NcDcIdxDirListToFileAttr> LongWord;
        LongWord = new TreeMap<Long, NcDcIdxDirListToFileAttr>();

        return Word;
    }
}
