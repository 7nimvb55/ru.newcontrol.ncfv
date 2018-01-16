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
import java.util.Properties;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwModalDevHelper {
    public static void showModalEnvironment(JFrame mainGUI){
        String strTitle = "Environment variables";
        JComponent[] forShow = new JComponent[1];
        forShow[0] = getEnvVarTable();
        
        JOptionPane.showMessageDialog(mainGUI, forShow, strTitle, JOptionPane.INFORMATION_MESSAGE);
    }
    private static JComponent getEnvVarTable(){
        JTable toViewTable = getEnvArrStr();
        JScrollPane toRetPane = new JScrollPane(toViewTable);
        toViewTable.setFillsViewportHeight(true);
        return toRetPane;
    }
    private static JTable getEnvArrStr(){
        String[] columnName = {"Property", "Value"};
        
        Map<String, String> sEnv = System.getenv();
        int toRetSize = sEnv.size();
        String[][] toRetStr = new String[toRetSize][2];
        
        int idx = 0;
        for(Map.Entry<String, String> itemEnv : sEnv.entrySet()){
            toRetStr[idx][0] = itemEnv.getKey();
            toRetStr[idx][1] = itemEnv.getValue();
            idx++;
        }
        
        JTable toRetTable = new JTable(toRetStr, columnName);
        return toRetTable;
    }
    public static void showModalProperties(JFrame mainGUI){
        String strTitle = "System properties";
        JComponent[] forShow = new JComponent[1];
        forShow[0] = getPropVarTable();
        JOptionPane.showMessageDialog(mainGUI, forShow, strTitle, JOptionPane.INFORMATION_MESSAGE);
    }
    private static JComponent getPropVarTable(){
        JTable toViewTable = getPropArrStr();
        JScrollPane toRetPane = new JScrollPane(toViewTable);
        toViewTable.setFillsViewportHeight(true);
        return toRetPane;
    }
    private static JTable getPropArrStr(){
        String[] columnName = {"Property", "Value"};
        
        Properties sProp = System.getProperties();
        Set<String> strPropName = sProp.stringPropertyNames();
        
        int toRetSize = sProp.size();
        String[][] toRetStr = new String[toRetSize][2];
        
        int idx = 0;
        for( String itemPorperties : strPropName ){
            toRetStr[idx][0] =  itemPorperties;
            toRetStr[idx][1] = sProp.getProperty(itemPorperties);
            idx++;
        }
        JTable toRetTable = new JTable(toRetStr, columnName);
        return toRetTable;
    }
}
