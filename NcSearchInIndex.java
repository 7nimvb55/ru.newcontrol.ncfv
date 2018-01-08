/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
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
     *
     */
    public NcSearchInIndex() {
        NcIMinFS ncwd = new NcIMinFS();
        
        ncThisManageIDs = ncwd.getNcIndexManageIDs();
        ncDiskInfo = ncwd.getDiskInfo();
    }
    /** 
     * IDs for OR for example T-SQL:
     * dataForInKeyWord = 
     * select id from some.table
     * where field.word like '%keywordinsearch_1%'
     * or like ...
     * or like '%keywordinsearch_N%'
     * or not like  '%keywordoutsearch_1%'
     * or like ...
     * or like '%keywordoutsearch_N%'
     * @param strKeyWordInSearch
     * @param strKeyWordOutSearch
     * @return 
     */ 
    public static void searchWordInIndex(){
        TreeMap<Long, NcDcIdxWordToFile> strHexForInVar = new TreeMap<Long, NcDcIdxWordToFile>();
        TreeMap<Long, NcDcIdxWordToFile> strHexForOutVar = new TreeMap<Long, NcDcIdxWordToFile>();
        TreeMap<Long, NcDcIdxWordToFile> strDistInResult = new TreeMap<Long, NcDcIdxWordToFile>();
        TreeMap<Long, NcDcIdxWordToFile> strDistOutResult = new TreeMap<Long, NcDcIdxWordToFile>();
        
        ArrayList<String> arrKeyWordInSearch = NcEtcKeyWordListManage.getKeyWordInSearchFromFile();
        for( String strItemIn : arrKeyWordInSearch ){
            strHexForInVar.putAll(getHexViewForSearchKeyWord(strItemIn));
        }
        NcAppHelper.outMessage("keyInwords count: " + arrKeyWordInSearch.size());
        strDistInResult = getDistictIDs(strHexForInVar);
        NcAppHelper.outMessage("Distinct for IN count: " + strDistInResult.size());
        ArrayList<String> arrKeyWordOutSearch = NcEtcKeyWordListManage.getKeyWordOutSearchFromFile();
        for( String strItemOut : arrKeyWordOutSearch ){
            strHexForOutVar.putAll(getHexViewForSearchKeyWord(strItemOut));
        }
        NcAppHelper.outMessage("keyOutwords count: " + arrKeyWordOutSearch.size());
        strDistOutResult = getDistictIDs(strHexForOutVar);
        NcAppHelper.outMessage("Distinct for OUT count: " + strDistOutResult.size());
        
        
        TreeMap<Long, NcDcIdxWordToFile> CleanResult = getIdInWithoutOfOutSearchResult(strDistInResult, strDistOutResult);
        NcAppHelper.outMessage("Clean records count: " + CleanResult.size());
        
        TreeMap<Long, NcDcIdxDirListToFileAttr> readedData = new TreeMap<Long, NcDcIdxDirListToFileAttr>();
        
        readedData.putAll(NcIdxDirListManager.getByListIDs(CleanResult));
        
        for( Map.Entry<Long, NcDcIdxDirListToFileAttr> itemReaded : readedData.entrySet() ){
            NcAppHelper.outMessage(itemReaded.getValue().path);
        }
    }
    
    public static void outToConsoleSearchedIDs(TreeMap<Long, NcDcIdxWordToFile> strHexForInVar){
        for( Map.Entry<Long, NcDcIdxWordToFile> itemID : strHexForInVar.entrySet() ){
            NcAppHelper.outMessage("id: " + itemID.getValue().dirListID);
        }
        
    }
    public static TreeMap<Long, NcDcIdxWordToFile> getDistictIDs(TreeMap<Long, NcDcIdxWordToFile> strHexForInVar){
        TreeMap<Long, NcDcIdxWordToFile> inList = new TreeMap<Long, NcDcIdxWordToFile>();
        long newRecId = 0;
        for( Map.Entry<Long, NcDcIdxWordToFile> itemID : strHexForInVar.entrySet() ){
            boolean isExistID = false;
            for( Map.Entry<Long, NcDcIdxWordToFile> itemInListID : inList.entrySet() ){
                isExistID = itemInListID.getValue().dirListID == itemID.getValue().dirListID;
            }
            if( !isExistID ){
                inList.put(newRecId, itemID.getValue());
                newRecId++;
            }
        }
        return inList;
        
    }
    public static TreeMap<Long, NcDcIdxWordToFile> getIdInWithoutOfOutSearchResult(TreeMap<Long, NcDcIdxWordToFile> strHexForInVar, TreeMap<Long, NcDcIdxWordToFile> strHexForOutVar){
        TreeMap<Long, NcDcIdxWordToFile> inList = new TreeMap<Long, NcDcIdxWordToFile>();
        long newRecId = 0;
        for( Map.Entry<Long, NcDcIdxWordToFile> itemID : strHexForInVar.entrySet() ){
            boolean isExistID = false;
            for( Map.Entry<Long, NcDcIdxWordToFile> itemInListForOutID : strHexForOutVar.entrySet() ){
                isExistID = itemInListForOutID.getValue().dirListID == itemID.getValue().dirListID;
            }
            if( !isExistID ){
                inList.put(newRecId, itemID.getValue());
                newRecId++;
            }
        }
        return inList;
    }
    public static void outSearchResult(TreeMap<Long, NcDcIdxWordToFile> strHexForInVar, TreeMap<Long, NcDcIdxWordToFile> strHexForOutVar){
        TreeMap<Long, NcDcIdxWordToFile> CleanResult = getIdInWithoutOfOutSearchResult(strHexForInVar, strHexForOutVar);
        NcAppHelper.outMessage("Count CleanResult records out: " + CleanResult.size());
    }
    
    public TreeMap<Long, NcDcIdxDirListToFileAttr> getWordSearchResult(ArrayList<String> strKeyWordInSearch, ArrayList<String> strKeyWordOutSearch){
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
    private TreeMap<Long, NcDcIdxDirListToFileAttr> getKeyWordForSearch(){
        TreeMap<Long, NcDcIdxDirListToFileAttr> Word;
        Word = new TreeMap<Long, NcDcIdxDirListToFileAttr>();
        TreeMap<Long, NcDcIdxDirListToFileAttr> LongWord;
        LongWord = new TreeMap<Long, NcDcIdxDirListToFileAttr>();

        return Word;
    }

    /**
     *
     * @param inFuncKeyWord
     */
    public static TreeMap<Long, NcDcIdxWordToFile> getHexViewForSearchKeyWord(String inFuncKeyWord){
        TreeMap<Long, NcDcIdxWordToFile> idsDataForKeyWord = new TreeMap<Long, NcDcIdxWordToFile>();
        
        ArrayList<String> strABC = NcPathToArrListStr.NCLVLABC.retArrListStr(inFuncKeyWord);
        idsDataForKeyWord.putAll(getIDsForKeyWord(strABC));
        
        ArrayList<String> strRABC = NcPathToArrListStr.NCLVLRABC.retArrListStr(inFuncKeyWord);
        idsDataForKeyWord.putAll(getIDsForKeyWord(strRABC));

        ArrayList<String> strNUM = NcPathToArrListStr.NCLVLNUM.retArrListStr(inFuncKeyWord);
        idsDataForKeyWord.putAll(getIDsForKeyWord(strNUM));
        
        ArrayList<String> strSYM = NcPathToArrListStr.NCLVLSYM.retArrListStr(inFuncKeyWord);
        idsDataForKeyWord.putAll(getIDsForKeyWord(strSYM));
        
        //ArrayList<String> strSPACE = NcPathToArrListStr.NCLVLSPACE.retArrListStr(inFuncKeyWord);
        return idsDataForKeyWord;
    }
    public static TreeMap<Long, NcDcIdxWordToFile> getIDsForKeyWord(ArrayList<String> inFuncListWord){
        
        ArrayList<String> strHexKeyWord = new ArrayList<String>();
        ArrayList<String> strWordForVar = new ArrayList<String>();
        ArrayList<String> strLongWord = new ArrayList<String>();
        ArrayList<String> strUpperLowerVar = new ArrayList<String>();
        
        TreeMap<Long, NcDcIdxWordToFile> idsDataForReturn = new TreeMap<Long, NcDcIdxWordToFile>();
        
        for( String strItem : inFuncListWord ){
            if( NcIdxLongWordManager.isLongWord(strItem) ){
                strLongWord.add(strItem);
            }
            else{
                
                String strLow = strItem.toLowerCase().toString();
                String strUp = strItem.toUpperCase().toString();
                
                if( !strLow.equals(strUp) ){
                    strWordForVar.add(strItem);
                }
                else{
                    strHexKeyWord.add(NcPathToArrListStr.toStrUTFinHEX(strItem));
                }
            }
        }
        /**
         * @TODO search for LongWord Structure
         */
        if( !strLongWord.isEmpty() ){
            NcAppHelper.outMessage("Not in search: " + strLongWord.size());
        }
        
        TreeMap<Long, NcIdxSubStringVariant> strSearchWordVarList = getUpperLowerCaseVariant(strWordForVar);
        
        strUpperLowerVar = getUpLowerCaseCombineKeyWord(strSearchWordVarList);
        
        for(String itemHexVar : strUpperLowerVar){
            strHexKeyWord.add(itemHexVar);
        }
        
        for( String strHexItemRABC : strHexKeyWord ){
            
            idsDataForReturn.putAll(NcIdxWordManager.getAllDataForWord(strHexItemRABC));
        }
        
        return idsDataForReturn;
    }
    /**
     *
     * @param toSearchABC
     */
    public static ArrayList<String> getUpLowerCaseCombineKeyWord(TreeMap<Long, NcIdxSubStringVariant> toSearchABC){
        ArrayList<String> strWordsVar = new ArrayList<String>();
        
        for(Map.Entry<Long, NcIdxSubStringVariant> itemKeyWord : toSearchABC.entrySet()){
            String strLower = itemKeyWord.getValue().hexForLowerCase;
            String strUpper = itemKeyWord.getValue().hexForUpperCase;
            String toChange = "";
            
            strWordsVar.add(strLower);
            
            String[] strArrLower = strToArray(strLower);
            String[] strArrUpper = strToArray(strUpper);
            String[] strArrChange = new String[strArrUpper.length];
            strArrChange = Arrays.copyOf(strArrLower, strArrLower.length);
            int idx = 0;
            do{
                strArrChange = strArrChangeState(strArrChange, strArrLower, strArrUpper);
                toChange = arrToStr(strArrChange);
                strWordsVar.add(toChange);
                idx++;
            }
            while( !strUpper.equalsIgnoreCase(toChange) ); 
        }
        return strWordsVar;
    }
    /**
     * 
     * @param strArrChange
     * @param strArrDown
     * @param strArrUp
     * @return 
     */
    public static String[] strArrChangeState(String[] strArrChange, String[] strArrDown, String[] strArrUp){
        if( (strArrDown.length == strArrUp.length)
                && (strArrChange.length == strArrDown.length) ){
            boolean changeFlow = false;
            boolean changeNext = false;
            boolean changeFar  = false;
            int prevElSay = -1;
            int nextElSay = -1;
            for(int arrIdx = 0; arrIdx < strArrChange.length; arrIdx++){
                if(arrIdx == 0){
                    changeFlow = strIfUpToDownTrue(strArrChange[arrIdx], strArrDown[arrIdx], strArrUp[arrIdx]);
                    if( changeFlow ){
                        prevElSay = arrIdx + 1;
                        changeFlow = false;
                    }
                    strArrChange[arrIdx] = strArrChangeElement(strArrChange[arrIdx], strArrDown[arrIdx], strArrUp[arrIdx]);
                }
                if( arrIdx == prevElSay ){
                    changeNext = strIfUpToDownTrue(strArrChange[arrIdx], strArrDown[arrIdx], strArrUp[arrIdx]);
                    if( changeNext ){
                        prevElSay = 0;
                        nextElSay = arrIdx + 1;
                        changeNext = false;
                    }
                    strArrChange[arrIdx] = strArrChangeElement(strArrChange[arrIdx], strArrDown[arrIdx], strArrUp[arrIdx]);
                }
                if( arrIdx == nextElSay ){
                    changeFar = strIfUpToDownTrue(strArrChange[arrIdx], strArrDown[arrIdx], strArrUp[arrIdx]);
                    if( changeFar ){
                        prevElSay = arrIdx + 1;
                        changeFar = false;
                    }
                    strArrChange[arrIdx] = strArrChangeElement(strArrChange[arrIdx], strArrDown[arrIdx], strArrUp[arrIdx]);
                }
            }
        }
        return strArrChange;
    }
    /**
     * 
     * @param strChange
     * @param strDown
     * @param strUp
     * @return 
     */
    public static String strArrChangeElement(String strChange, String strDown, String strUp){
        if(strDown.equalsIgnoreCase(strChange)){
            return strUp;
        }
        return strDown;
    }
    /**
     * 
     * @param strForCompare
     * @param strDown
     * @param strUp
     * @return 
     */
    public static boolean strIfUpToDownTrue(String strForCompare, String strDown, String strUp){
        if( strUp.equalsIgnoreCase(strForCompare) ){
            return true;
        }
        return false;
    }
    /**
     * 
     * @param inputArrStr
     * @return 
     */
    public static String arrToStr(String[] inputArrStr){
        String toReturn = "";
        for(int i = 0; i < inputArrStr.length ; i++){
            toReturn = toReturn + inputArrStr[i];
        }
        return toReturn;
    }
    /**
     * 
     * @param inputHexStr
     * @return 
     */
    public static String[] strToArray(String inputHexStr){
        int idxStart = 0;
        int idxEnd = idxStart + 4;
        int idx = 0;
        String[] strToRet = new String[inputHexStr.length() / 4];
        do{
            strToRet [idx] = inputHexStr.substring(idxStart, idxEnd);
            idxStart = idxStart + 4;
            idxEnd = idxStart + 4;
            idx++;
        }
        while( idxEnd <= inputHexStr.length() ); 
        return strToRet;        
    }
    /**
     *
     * @param strInFunc
     * @return
     */
    public static TreeMap<Long, NcIdxSubStringVariant> getUpperLowerCaseVariant(ArrayList<String> strInFunc){
        TreeMap<Long, NcIdxSubStringVariant> strToRet = new TreeMap<Long, NcIdxSubStringVariant>();
        if( strInFunc.isEmpty() ){
            return strToRet;
        }
        long idx = 0;
        for(String itemStr : strInFunc ){
            String toLowerResultStr = itemStr.toLowerCase().toString();
            String toUpperResultStr = itemStr.toUpperCase().toString();
            NcIdxSubStringVariant toAddToReturn = new NcIdxSubStringVariant(toLowerResultStr, toUpperResultStr);
            strToRet.put(idx, toAddToReturn);
            idx++;
        }
        return strToRet;
    }
}
