/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableModel;



/**
 *
 * @author Администратор
 */
public class NcSwingIndexManagerApp {
    private static final String APP_TITLE = "Index Managment";
    private static final long ncForGB = 1024*1024*1024;
    private static JFrame frame;
    
    /**
     *
     */
    public NcSwingIndexManagerApp() {
        

    }
    
    /**
     *
     */
    public static void NcRunSIMA(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Significantly improves the look of the output in
                    // terms of the file names returned by FileSystemView!
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch(Exception weTried) {
                    weTried.getMessage();
                    weTried.getStackTrace();
                }
                createGui();
                
            }
        });
    }

    /**
     *
     */
    public static void createGui(){
        frame = new JFrame(APP_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        mainPanel.add(getNorthPanel(), BorderLayout.NORTH);
        mainPanel.add(getSouthPanel(), BorderLayout.SOUTH);
        mainPanel.add(getWestPanel(), BorderLayout.WEST);
        mainPanel.add(getEastPanel(), BorderLayout.EAST);
        mainPanel.add(getCenterPanel(), BorderLayout.CENTER);
        
        frame.getContentPane().add(mainPanel);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     *
     * @return
     */
    public static JLabel createEmptyLabel() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(100, 30));
        return label;
    }
    
    /**
     *
     * @return
     */
    public static JPanel getNorthPanel(){
        JPanel northPanel = new JPanel();
        Border northBorder = BorderFactory.createTitledBorder("NORTH panel");
        northPanel.setBorder(northBorder);
        northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JTextField addNorthWordSearch = new JTextField();
        addNorthWordSearch.setPreferredSize(new Dimension(300, 20));
        
        northPanel.add(addNorthWordSearch);
        northPanel.add(createButton("Search",null,""));
        return northPanel;
    }
    
    /**
     *
     * @return
     */
    public static JPanel getSouthPanel(){
        JPanel southPanel = new JPanel();
        Border southBorder = BorderFactory.createTitledBorder("SOUTH panel");
        southPanel.setBorder(southBorder);
        
        JLabel txtFromIndex = createEmptyLabel();
        
        southPanel.add(txtFromIndex);
        return southPanel;
    }

    /**
     *
     * @return
     */
    public static JPanel getEastPanel(){
        String[] forTextToolTip = {
            "For search with keyword, input it and press \"+\" Button",
            "For search with out keyword, input it and press \"+\" Button",
            "Help about keyword in Search",
            "Help about keyword out of Search",
        };
        String[] forComponentText = {
            "How to use",
        };
    
        Dimension textFiledForSearchDimension = new Dimension(100, 20);        
        
        JPanel eastPanel = new JPanel();
        Border eastBorder = BorderFactory.createTitledBorder("EAST panel");
        eastPanel.setBorder(eastBorder);
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.PAGE_AXIS));
        
        eastPanel.setAlignmentX(JComponent.TOP_ALIGNMENT);
        eastPanel.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        
        JButton ncHowToInSearch = createButton(forComponentText[0],null,forTextToolTip[2]);
        
        eastPanel.add(ncHowToInSearch);
        
        JPanel textInSearchPanel = getTextFieldForSearchPanel(textFiledForSearchDimension, forTextToolTip[0]);
        eastPanel.add(textInSearchPanel);
        
        eastPanel.add(getAndOrButtonPanel());
        
        JList wiSearch = createJListWordInSearch();
        JScrollPane wiScroll = new JScrollPane(wiSearch);
        wiScroll.setPreferredSize(new Dimension(100, 100));
        
        eastPanel.add(wiScroll);
        
        JPanel buttonPanel1 = getAddDelButtonPanel();
        eastPanel.add(buttonPanel1);
        
        JSeparator ncSeparator = new JSeparator(JSeparator.HORIZONTAL);
        eastPanel.add(ncSeparator);
        
        eastPanel.add(createButton(forComponentText[0],null,forTextToolTip[3]));
        
        JPanel textOutSearchPanel = getTextFieldForSearchPanel(textFiledForSearchDimension, forTextToolTip[1]);
        eastPanel.add(textOutSearchPanel);
        
        eastPanel.add(getAndOrButtonPanel());
        
        JList wniSearch = createJListWordNotInSearch();
        JScrollPane wniScroll = new JScrollPane(wniSearch);
        wniScroll.setPreferredSize(new Dimension(100, 100));
        
        eastPanel.add(wniScroll);
        
        JPanel buttonPanel2 = getAddDelButtonPanel();
        eastPanel.add(buttonPanel2);
        return eastPanel;
    }

    /**
     *
     * @return
     */
    public static JPanel getAndOrButtonPanel(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(createButton("&&",null,""));
        buttonPanel.add(createButton("||",null,""));
        return buttonPanel;
    }    

    /**
     *
     * @return
     */
    public static JPanel getAddDelButtonPanel(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(createButton("+",null,""));
        buttonPanel.add(createButton("-",null,""));
        return buttonPanel;
    }

    /**
     *
     * @param textSize
     * @param strToolTip
     * @return
     */
    public static JPanel getTextFieldForSearchPanel(Dimension textSize, String strToolTip){
        JPanel textFieldForSearchPanel = new JPanel();
        textFieldForSearchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JTextField textWordForSearch = new JTextField();
        textWordForSearch.setPreferredSize(textSize);
        textWordForSearch.setToolTipText(strToolTip);
        
        textFieldForSearchPanel.add(textWordForSearch);
        return textFieldForSearchPanel;
    }

    /**
     *
     * @return
     */
    public static JPanel getCenterPanel(){
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
        JScrollPane ncScrollTable= new JScrollPane(ncTable);
        
        
        centerPanel.add(ncScrollTable);
        
        ncScrollTable.revalidate();
        return centerPanel;
    }
    
    /**
     *
     * @return
     */
    public static JPanel getWestPanel(){
        JPanel westPanel = new JPanel();
        Border westBorder = BorderFactory.createTitledBorder("WEST panel");
        westPanel.setBorder(westBorder);
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.PAGE_AXIS));
        
        ArrayList<JButton> ncAllDisk = getRootButtons();

        for(JButton itemDisk : ncAllDisk){
            westPanel.add(itemDisk);
        }
        return westPanel;
    }

    /**
     *
     * @return
     */
    public static ArrayList<JButton> getRootButtons(){
        ArrayList<JButton> toReturn = new ArrayList<JButton>();
        
        FileSystemView fileSystemView;
        fileSystemView = FileSystemView.getFileSystemView();
        //File[] ncDisks = fileSystemView.getRoots();
        File[] ncDisks = File.listRoots();
       
        
        for(File itemDisk : ncDisks){
            
            Icon itemIcon = fileSystemView.getSystemIcon(itemDisk);
            String itemDisplayName = itemDisk.getAbsolutePath().substring(0, 2) + " - " +
                    fileSystemView.getSystemTypeDescription(itemDisk);
                    

            
            long ncDriveFreeGb = itemDisk.getFreeSpace()/ncForGB;
            long ncDriveTotalGb = itemDisk.getTotalSpace()/ncForGB;
            
            String itemToolTipText = 
                    "Free: " + ncDriveFreeGb +
                    " Gb Total: " + ncDriveTotalGb + " Gb";
            
            if(ncDriveTotalGb != 0){
                double ncDriveFreePercent = (((double) itemDisk.getFreeSpace() / itemDisk.getTotalSpace()) * 100);
                String strPercent= Double.toString(ncDriveFreePercent).substring(0, 5);
                itemToolTipText = "Free: " + strPercent +
                    " % " + itemToolTipText;
            }
            
            String strDescr = fileSystemView.getSystemDisplayName(itemDisk);
            if(strDescr.length() > 0 ){
                itemToolTipText = strDescr + " " + itemToolTipText;
            }
            
            JButton ncButton = createButton(itemDisplayName, itemIcon, itemToolTipText);
         
           
            toReturn.add(ncButton);
        }

        
        return toReturn;
    }

    /**
     *
     * @param ncName
     * @param ncIcon
     * @param ncToolTipText
     * @return
     */
    public static JButton createButton(String ncName, Icon ncIcon, String ncToolTipText) {
        JButton ncButton = new JButton(ncName);
        if(ncIcon != null){
            ncButton.setIcon(ncIcon);
        }
        ncButton.setToolTipText(ncToolTipText);
        return ncButton;
    }

    /**
     *
     * @return
     */
    public static JList createJListWordInSearch(){
        DefaultListModel listWordInSearch = new DefaultListModel();
          
        listWordInSearch.addElement("* All *");
        
        JList ncList = new JList(listWordInSearch);
        ncList.setVisibleRowCount(7);
        ncList.setFocusable(false);
        return ncList;
    }

    /**
     *
     * @return
     */
    public static JList createJListWordNotInSearch(){
        DefaultListModel listWordNotInSearch = new DefaultListModel();
        listWordNotInSearch.addElement("* Nothing *");
        JList ncList = new JList(listWordNotInSearch);
        ncList.setVisibleRowCount(7);
        ncList.setFocusable(false);
        return ncList;
    }

    /**
     *
     * @param ncJList
     * @return
     */
    public static JList addJListElement(JList ncJList){
        return ncJList;
    }
}
