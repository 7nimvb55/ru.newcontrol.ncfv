/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.util.ArrayList;
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
public class NcSwModalSettingsHelper {
    protected static void showModalProperties(JFrame mainGUI){
        String strTitle = "Application properties";
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
        
        //Properties sProp = System.getProperties();
        ArrayList<String> strPropName = NcPreRunFileViewer.getEtcCfgLinesFromDisk();
        
        int toRetSize = strPropName.size();
        String[][] toRetStr = new String[toRetSize][2];
        
        int idx = 0;
        for( String itemPorperties : strPropName ){
            String[] split = itemPorperties.split("=");
            if( split.length == 2 ){
                toRetStr[idx][0] =  split[0];
                toRetStr[idx][1] = split[1];
            }
            else{
                toRetStr[idx][0] =  "***All String***";
                toRetStr[idx][1] = itemPorperties;
            }
            
            idx++;
        }
        JTable toRetTable = new JTable(toRetStr, columnName);
        return toRetTable;
    }
}
