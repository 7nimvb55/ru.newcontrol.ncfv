/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Администратор
 */

public class NcSIMASearchResultTableModel implements TableModel {
    private Set<TableModelListener> listeners = new HashSet<TableModelListener>();
    private TreeMap<Long, NcDcIdxDirListToFileAttr> ncDirectoryListReader;

    /**
     *
     */
    public NcSIMASearchResultTableModel() {
        
        ncDirectoryListReader = new TreeMap<Long, NcDcIdxDirListToFileAttr>();
    }

    /**
     *
     * @param strKeyWordInSearch
     * @param strKeyWordOutSearch
     */
    public NcSIMASearchResultTableModel(ArrayList<String> strKeyWordInSearch,ArrayList<String>  strKeyWordOutSearch) {
        NcSearchInIndex ncSearchInIndex = new NcSearchInIndex();
        ncDirectoryListReader = ncSearchInIndex.getWordSearchResult(strKeyWordInSearch, strKeyWordOutSearch);
    }
    
    
    @Override
    public int getRowCount() {
        return ncDirectoryListReader.size();
    }

   @Override
    public int getColumnCount() {
        return 16;
    }

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

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

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
                return rowForOutPut.diskLetter + ":\\" + rowForOutPut.path;
            case 5:
                return (rowForOutPut.diskLetter + ":\\" + rowForOutPut.path).hashCode();
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

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }
    
}
