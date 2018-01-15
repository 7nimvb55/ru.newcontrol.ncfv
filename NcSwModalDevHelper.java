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
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import static ru.newcontrol.ncfv.NcAppHelper.outMessageToConsole;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwModalDevHelper {
    public static void showModalEnvironment(){
        String strTitle = "Environment variables";
        JComponent forShow = getEnvVarTable();
        
        int reply  = JOptionPane.showConfirmDialog(null, forShow, strTitle, JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
    }
    private static JComponent getEnvVarTable(){
        String[] columnName = {"Property", "Value"};
        JTable toRetTable = new JTable(getEnvArrStr(), columnName);
        JScrollPane toRetPane = new JScrollPane(toRetTable);
        toRetTable.setFillsViewportHeight(true);
        return toRetPane;
    }
    private static String[][] getEnvArrStr(){
        Properties sProp = System.getProperties();
        Set<String> strPropName = sProp.stringPropertyNames();
        Map<String, String> sEnv = System.getenv();
        int toRetSize = sProp.size() + sEnv.size() + 2;
        String[][] toRetStr = new String[toRetSize][2];
        int idx = 0;
        toRetStr[idx][0] = "Properties";
        idx++;
        for( String itemPorperties : strPropName ){
            toRetStr[idx][0] =  itemPorperties;
            toRetStr[idx][1] = sProp.getProperty(itemPorperties);
            idx++;
        }
        toRetStr[idx][0] = "Environments";
        idx++;
        for(Map.Entry<String, String> itemEnv : sEnv.entrySet()){
            toRetStr[idx][0] = itemEnv.getKey();
            toRetStr[idx][1] = itemEnv.getValue();
            idx++;
        }
        return toRetStr;
    }
}
