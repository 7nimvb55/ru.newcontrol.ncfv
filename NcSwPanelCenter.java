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
    public static JPanel getPanel(NcSwGUIComponentStatus lComp){
        JPanel centerPanel = new JPanel();
        String componentPath = NcStrGUIComponent.SMAIN.getStr()
            + NcStrGUIComponent.SJFRAME.getStr()
            + NcStrGUIComponent.SJPANEL.getStr()
            + NcStrGUIComponent.SCENTER.getStr();
        lComp.putComponents(componentPath, centerPanel);
        Border centerBorder = BorderFactory.createTitledBorder("CENTER panel");
        centerPanel.setBorder(centerBorder);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
//        ArrayList<String> strKeyWordInSearch = new ArrayList<String>();
//        ArrayList<String> strKeyWordOutSearch = new ArrayList<String>();
        
//        strKeyWordInSearch.add(" ");
//       strKeyWordInSearch.add("01");
//       strKeyWordInSearch.add("freebsd");
//        strKeyWordInSearch.add("freebsd");
//        strKeyWordOutSearch.add("newcontrol");
        
        TableModel ncTableModel = new NcSIMASearchResultTableModel(new ArrayList<String>(), new ArrayList<String>());
        
        JTable ncTable = new JTable(ncTableModel);
        componentPath = NcStrGUIComponent.SMAIN.getStr()
            + NcStrGUIComponent.SJFRAME.getStr()
            + NcStrGUIComponent.SJPANEL.getStr()
            + NcStrGUIComponent.SCENTER.getStr()
            + NcStrGUIComponent.SJTABLE.getStr();
        lComp.putComponents(componentPath, ncTable);
        
        
        JScrollPane ncScrollTable = new JScrollPane(ncTable);
        
        
        centerPanel.add(ncScrollTable);
        
        ncScrollTable.revalidate();
        
        
        toLALRgetPanel();
        return centerPanel;
    }
    private static void toLALRgetPanel(){
        if( NcfvRunVariables.isLALRNcSwPanelPageStartgetPanel() ){
            String strLogMsg = NcStrLogMsgField.INFO.getStr()
                + NcStrLogMsgField.APP_LOGIC_NOW.getStr()
                + NcStrLogLogicVar.LA_JPANEL_CENTER.getStr()
                + NcStrLogMsgField.APP_LOGIC_NEXT_WAY_VAR.getStr()
                + NcStrLogLogicVar.LA_SET_VISIBLE_GUI.getStr();
            NcAppHelper.outMessage(strLogMsg);
        }
    }
}
