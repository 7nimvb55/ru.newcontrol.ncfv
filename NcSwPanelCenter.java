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
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableModel;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwPanelCenter {
    
    /**
     *
     * @return
     */
    public static NcSwGUIComponentStatus getCenterPanel(){
        JPanel centerPanel = new JPanel();
        Border centerBorder = BorderFactory.createTitledBorder("CENTER panel");
        centerPanel.setBorder(centerBorder);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        ArrayList<String> strKeyWordInSearch = new ArrayList<String>();
        ArrayList<String> strKeyWordOutSearch = new ArrayList<String>();
        
//        strKeyWordInSearch.add(" ");
        strKeyWordInSearch.add("01");
//       strKeyWordInSearch.add("freebsd");
//        strKeyWordInSearch.add("freebsd");
//        strKeyWordOutSearch.add("newcontrol");
        
        TableModel ncTableModel = new NcSIMASearchResultTableModel(strKeyWordInSearch, strKeyWordOutSearch);
        
        JTable ncTable = new JTable(ncTableModel);
        JScrollPane ncScrollTable = new JScrollPane(ncTable);
        
        
        centerPanel.add(ncScrollTable);
        
        ncScrollTable.revalidate();
        
        NcSwGUIComponentStatus retComp = new NcSwGUIComponentStatus(
        
        ncTableModel,
        ncScrollTable,
        ncTable,
        centerPanel);
        
        return retComp;
    }
}
