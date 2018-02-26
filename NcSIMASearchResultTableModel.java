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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Администратор
 */

public class NcSIMASearchResultTableModel implements TableModel {
    private Set<TableModelListener> listeners = new HashSet<TableModelListener>();
    private TreeMap<Long, NcDcIdxDirListToFileAttr> ncDirectoryListReader;

    /**
     * Not used
     */
    private NcSIMASearchResultTableModel() {
        ncDirectoryListReader = NcSrchGetResult.makeSearchByKeyFromFile();
    }
    
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwThreadManager#setToViewSearchedResult(ru.newcontrol.ncfv.NcSwGUIComponentStatus, java.lang.String) }
     * </ul>
     */
    protected NcSIMASearchResultTableModel(String strSearchInput) {
        ncDirectoryListReader = NcSrchGetResult.makeSearchByKeyFromInput(strSearchInput);
    }
    protected NcSIMASearchResultTableModel(TreeMap<Long, NcDcIdxDirListToFileAttr> ncInFuncData) {
        ncDirectoryListReader = ncInFuncData;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelCenter#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * </ul>
     * @param strKeyWordInSearch
     * @param strKeyWordOutSearch
     */
    protected NcSIMASearchResultTableModel(ArrayList<String> strKeyWordInSearch,ArrayList<String>  strKeyWordOutSearch) {
        NcSearchInIndex ncSearchInIndex = new NcSearchInIndex();
        ncDirectoryListReader = NcSrchGetResult.makeSearchByKeyFromFile();
    }
    
    /**
     * Not used
     * @return 
     */
    @Override
    public int getRowCount() {
        return ncDirectoryListReader.size();
    }
    /**
     * Not used
     * @return 
     */
    @Override
    public int getColumnCount() {
        return 16;
    }
    /**
     * Not used
     * @param columnIndex
     * @return 
     */
    @Override
    public String getColumnName(int columnIndex) {
            switch (columnIndex) {
            case 0:
                return "ID";
            case 1:
                return "Disk S/N";
            case 2:
                return "Disk S/N (hash)";
            case 3:
                return "Disk letter";
            case 4:
                return "Path";
            case 5:
                return "Path (hash)";
            case 6:
                return "Path with out Disk letter";
            case 7:
                return "Path woDL (hash)";
            case 8:
                return "Length";
            case 9:
                return "R";
            case 10:
                return "W";
            case 11:
                return "X";
            case 12:
                return "H";
            case 13:
                return "lmDate";
            case 14:
                return "D";
            case 15:
                return "F";

            }
            return "";
    }
    /**
     * Not used
     * @param columnIndex
     * @return 
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
    /**
     * Not used
     * @param rowIndex
     * @param columnIndex
     * @return 
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    /**
     * Not used
     * @param rowIndex
     * @param columnIndex
     * @return 
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        boolean returnNulls = true;
        NcDcIdxDirListToFileAttr rowForOutPut = null;
        //rowForOutPut = ncDirectoryListReader.entrySet().;
        long rowCounter = 0;
        
        for(Map.Entry<Long, NcDcIdxDirListToFileAttr> itemClean : ncDirectoryListReader.entrySet()){
            if(rowIndex == rowCounter){
                rowForOutPut = itemClean.getValue();
            }
            
            rowCounter++;
        }
        if(rowForOutPut != null){
            switch (columnIndex) {
            case 0:
                return rowForOutPut.dirListID;
            case 1:
                return rowForOutPut.diskSnHex;
            case 2:
                return rowForOutPut.diskSnHexHash;
            case 3:
                return rowForOutPut.diskLetter;
            case 4:
                if( NcAppHelper.isWindows() ){
                    return rowForOutPut.diskLetter + ":\\" + rowForOutPut.path;
                }
                else{
                    return rowForOutPut.path;
                }
                
            case 5:
                if( NcAppHelper.isWindows() ){
                    return (rowForOutPut.diskLetter + ":\\" + rowForOutPut.path).hashCode();
                }
                else{
                    return (rowForOutPut.path).hashCode();
                }    
            case 6:
                return rowForOutPut.path;
            case 7:
                return rowForOutPut.pathHash;
            case 8:
                return rowForOutPut.fileLength;
            case 9:
                return rowForOutPut.fileCanRead;
            case 10:
                return rowForOutPut.fileCanWrite;
            case 11:
                return rowForOutPut.fileCanExecute;
            case 12:
                return rowForOutPut.fileIsHidden;
            case 13:
                return rowForOutPut.fileLastModified;
            case 14:
                return rowForOutPut.fileIsDirectory;
            case 15:
                return rowForOutPut.fileIsFile;

            }
            return "";
            
        }
        
        return new Object();
    }
    /**
     * Not used
     * @param aValue
     * @param rowIndex
     * @param columnIndex 
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        
    }
    /**
     * Not used
     * @param l 
     */
    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }
    /**
     * Not used
     * @param l 
     */
    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }
    
}
