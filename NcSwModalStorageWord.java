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

import java.util.Map;
import java.util.TreeMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwModalStorageWord {
    protected static void showModalStorageWord(JFrame mainGUI){
        String strTitle = "Storage word";
        JComponent[] forShow = new JComponent[1];
        forShow[0] = getStorageWordTable();
        
        JOptionPane.showMessageDialog(mainGUI, forShow, strTitle, JOptionPane.INFORMATION_MESSAGE);
    }
    private static JComponent getStorageWordTable(){
        JTable toViewTable = getStorageWordArrStr();
        JScrollPane toRetPane = new JScrollPane(toViewTable);
        toViewTable.setFillsViewportHeight(true);
        return toRetPane;
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
