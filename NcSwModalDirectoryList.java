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

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwModalDirectoryList {
    
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwMenuItems#getDirListViewer(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * </ul>
     * @param lComp
     * @return 
     */
    protected static void showModalDirectoryList(JFrame mainGUI){
        String strTitle = "Directory List";
        JComponent[] forShow = new JComponent[1];
        JScrollPane scrollWithFileNames = (JScrollPane) getDirectoryListTable();
        JScrollPane scrollWithElement = (JScrollPane) getDirListElementContent();
        JPanel panelForTables = new JPanel();
        
        JSplitPane slider = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
        scrollWithFileNames,
        scrollWithElement);

        panelForTables.add(slider);
        forShow[0] = panelForTables;
        JOptionPane.showMessageDialog(mainGUI, forShow, strTitle, JOptionPane.INFORMATION_MESSAGE);
    }
    private static JComponent getDirListElementContent(){
        JTable toViewTable = getDirectoryListArrStr();
        JScrollPane toRetPane = new JScrollPane(toViewTable);
        toViewTable.setFillsViewportHeight(true);
        return toRetPane;
    }
    private static JComponent getDirectoryListTable(){
        JTable toViewTable = getDirectoryListArrStr();
        JScrollPane toRetPane = new JScrollPane(toViewTable);
        toViewTable.setFillsViewportHeight(true);
        return toRetPane;
    }
    private static JTable getDirectoryListArrStr(){
        /*randomUUID = UUID.randomUUID();
        this.file = file;
        this.fileName = fileName;
        this.fileRealPath = fileRealPath;
        this.hashCode = hashCode;
        this.posixFilePermissions = posixFilePermissions;
        this.creationTime = creationTime;
        this.lastAccessTime = lastAccessTime;
        this.lastModifiedTime = lastModifiedTime;
        this.sizeFromAttr = sizeFromAttr;
        this.sizeFromFiles = sizeFromFiles;
        this.directory = directory;
        this.readable = readable;
        this.writable = writable;
        this.executable = executable;
        this.other = other;
        this.symbolicLink = symbolicLink;
        this.absolute = absolute;
        this.notExists = notExists;
        this.exists = exists;
        this.regularFile = regularFile;
        this.hidden = hidden;
        this.exHidden = exHidden;
        this.exPosix = exPosix;
        this.exReal = exReal;
        this.exSize = exSize;
        this.notEqualSize = notEqualSize;*/
        String[] columnName = 
            {"UUID",
            "file",
            "fileName",
            "fileRealPath",
            "hashCode",
            "posixFilePermissions",
            "creationTime",
            "lastAccessTime",
            "lastModifiedTime",
            "sizeFromAttr",
            "sizeFromFiles",
            "directory",
            "readable",
            "writable",
            "executable",
            "other",
            "symbolicLink",
            "absolute",
            "notExists",
            "exists",
            "regularFile",
            "hidden",
            "exHidden",
            "exPosix",
            "exReal",
            "exSize",
            "notEqualSize"};
        int toRetSize = 100;
        //String[][] toRetStr = getDataFromDirListFile(columnName.length);
        String[][] toRetStr = getDataFromDirListFile(1); 
        String[] oneColumn = {"file name"};
        //JTable toRetTable = new JTable(toRetStr, columnName);
        JTable toRetTable = new JTable(toRetStr, oneColumn);
        return toRetTable;
    }
    
    private static String[][] getDataFromDirListFile(int lengthCol){
        CopyOnWriteArrayList<Path> filesFromDirListStorage = NcIdxNioDirListManager.getFilesFromDirListStorage();
        
        Object[] toArray = filesFromDirListStorage.toArray();
        String[][] toRetStr = new String[toArray.length][lengthCol];
        int idxRec = 0;
        for (Object object : toArray) {
            Path pathDirElement = (Path) object;
            String strPath = "<HTML><BODY><a href=" + pathDirElement.toUri().toString()+ ">"
                    + pathDirElement.getFileName().toString() + "</a>" + "</BODY></HTML>";
            toRetStr[idxRec][0] = strPath;
            idxRec++;
        }
        
        
        return toRetStr;
    }
    
    private static JTable getStorageWordArrStr(){
        String[] columnName = {"Key", "Word", "Hex", "Count", "ID"};
        
        TreeMap<Long, NcDcIdxStorageWordToFile> fromStorageWordAllRecords = 
                NcIdxStorageWordManager.getFromStorageWordAllRecords("NCLVLABC");
        TreeMap<Long, NcDcIdxStorageWordToFile> fromStorageWordAllRecords1 = 
                NcIdxStorageWordManager.getFromStorageWordAllRecords("NCLVLNUM");
        TreeMap<Long, NcDcIdxStorageWordToFile> fromStorageWordAllRecords2 = 
                NcIdxStorageWordManager.getFromStorageWordAllRecords("NCLVLRABC");
        TreeMap<Long, NcDcIdxStorageWordToFile> fromStorageWordAllRecords3 = 
                NcIdxStorageWordManager.getFromStorageWordAllRecords("NCLVLSPACE");
        TreeMap<Long, NcDcIdxStorageWordToFile> fromStorageWordAllRecords4 = 
                NcIdxStorageWordManager.getFromStorageWordAllRecords("NCLVLSYM");
        
        int toRetSize = fromStorageWordAllRecords.size();
        toRetSize = toRetSize + fromStorageWordAllRecords.size();
        toRetSize = toRetSize + fromStorageWordAllRecords1.size();
        toRetSize = toRetSize + fromStorageWordAllRecords2.size();
        toRetSize = toRetSize + fromStorageWordAllRecords3.size();
        toRetSize = toRetSize + fromStorageWordAllRecords4.size();
        
        String[][] toRetStr = new String[toRetSize][5];
        
        int idx = 0;
        for (Map.Entry<Long, NcDcIdxStorageWordToFile> fromStorageWordAllRecord : 
                fromStorageWordAllRecords.entrySet()) {
            toRetStr[idx][0] = fromStorageWordAllRecord.getKey().toString();
            toRetStr[idx][1] = fromStorageWordAllRecord.getValue().word;
            toRetStr[idx][2] = fromStorageWordAllRecord.getValue().wordInHex;
            toRetStr[idx][3] = String.valueOf(fromStorageWordAllRecord.getValue().wordCount);
            toRetStr[idx][4] = String.valueOf(fromStorageWordAllRecord.getValue().wordId);
            idx++;
        }
        
        idx = 0;
        for (Map.Entry<Long, NcDcIdxStorageWordToFile> fromStorageWordAllRecord : 
                fromStorageWordAllRecords1.entrySet()) {
            toRetStr[idx][0] = fromStorageWordAllRecord.getKey().toString();
            toRetStr[idx][1] = fromStorageWordAllRecord.getValue().word;
            toRetStr[idx][2] = fromStorageWordAllRecord.getValue().wordInHex;
            toRetStr[idx][3] = String.valueOf(fromStorageWordAllRecord.getValue().wordCount);
            toRetStr[idx][4] = String.valueOf(fromStorageWordAllRecord.getValue().wordId);
            idx++;
        }
        
        idx = 0;
        for (Map.Entry<Long, NcDcIdxStorageWordToFile> fromStorageWordAllRecord : 
                fromStorageWordAllRecords2.entrySet()) {
            toRetStr[idx][0] = fromStorageWordAllRecord.getKey().toString();
            toRetStr[idx][1] = fromStorageWordAllRecord.getValue().word;
            toRetStr[idx][2] = fromStorageWordAllRecord.getValue().wordInHex;
            toRetStr[idx][3] = String.valueOf(fromStorageWordAllRecord.getValue().wordCount);
            toRetStr[idx][4] = String.valueOf(fromStorageWordAllRecord.getValue().wordId);
            idx++;
        }
        
        idx = 0;
        for (Map.Entry<Long, NcDcIdxStorageWordToFile> fromStorageWordAllRecord : 
                fromStorageWordAllRecords3.entrySet()) {
            toRetStr[idx][0] = fromStorageWordAllRecord.getKey().toString();
            toRetStr[idx][1] = fromStorageWordAllRecord.getValue().word;
            toRetStr[idx][2] = fromStorageWordAllRecord.getValue().wordInHex;
            toRetStr[idx][3] = String.valueOf(fromStorageWordAllRecord.getValue().wordCount);
            toRetStr[idx][4] = String.valueOf(fromStorageWordAllRecord.getValue().wordId);
            idx++;
        }
        
        idx = 0;
        for (Map.Entry<Long, NcDcIdxStorageWordToFile> fromStorageWordAllRecord : 
                fromStorageWordAllRecords4.entrySet()) {
            toRetStr[idx][0] = fromStorageWordAllRecord.getKey().toString();
            toRetStr[idx][1] = fromStorageWordAllRecord.getValue().word;
            toRetStr[idx][2] = fromStorageWordAllRecord.getValue().wordInHex;
            toRetStr[idx][3] = String.valueOf(fromStorageWordAllRecord.getValue().wordCount);
            toRetStr[idx][4] = String.valueOf(fromStorageWordAllRecord.getValue().wordId);
            idx++;
        }

        
        JTable toRetTable = new JTable(toRetStr, columnName);
        return toRetTable;
    }
}
