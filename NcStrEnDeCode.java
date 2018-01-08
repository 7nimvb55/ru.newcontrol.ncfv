/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;
import java.io.*;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
/**
 *
 * @author Администратор
 */
public class NcStrEnDeCode {
    
    NcStrEnDeCode(){}
/**
 * Use in ru.newcontrol.ncfv.Ncfv.main() @method=main of @File=Ncfv.java provides class name @Class=Ncfv
 */    
    public static void getCodePageInfo(){
        for (Map.Entry e : Charset.availableCharsets().entrySet()) {
            System.out.println("Code pages:");
            System.out.println(e.getKey());
        }
        
        // Sonderzeichen: &auml; &ouml; &uuml; &szlig; &sect; &euro; &frac12; &sup2; &radic; &sum;
      //String sonderzeichen = ( args.length > 0 ) ? args[0] :
      //                      "\u00E4\u00F6\u00FC\u00DF\u00A7\u20AC\u00BD\u00B2\u221A\u2211";
      String sonderzeichen = "\u00E4\u00F6\u00FC\u00DF\u00A7\u20AC\u00BD\u00B2\u221A\u2211";
      String osn  = System.getProperty( "os.name" );
      String fcp  = System.getProperty( "file.encoding" );
      String ccp  = System.getProperty( "console.encoding" );
      String ccps = ccp;

      if( ccp == null ) {
         // Wir raten die Codepage der Konsole
         // (Cp850 ist nur fuer westeuropaeische Laender korrekt):
         ccp = ( osn != null && osn.contains( "Windows" ) ) ? "IBM866" : fcp;
         ccps = ccp + " (OEM - russian)";
      }

      System.out.println( "os.name:          " + osn );
      System.out.println( "default-charset:  " + Charset.defaultCharset() );
      System.out.println( "file.encoding:    " + fcp );
      System.out.println( "console.encoding: " + ccps );
      
      

      // Ausgabe mit Standard-System.out und unveraenderter Codepage:
      System.out.println( "Mit System.out:   " + sonderzeichen );

        try {
            // Ausgabe mit PrintWriter und gesetzter Konsolen-Codepage:
            PrintWriter pw = new PrintWriter( new OutputStreamWriter( System.out, ccp ) );
            pw.println( "Mit PrintWriter:  " + sonderzeichen );
            
            pw.flush();
            
            // System.out-Ausgabe mit neuem PrintStream mit gesetzter Konsolen-Codepage:
            System.setOut( new PrintStream( System.out, true, ccp ) );
            System.setErr( new PrintStream( System.err, true, ccp ) );
            System.out.println( "Mit PrintStream:  " + sonderzeichen );
            
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(NcStrEnDeCode.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

    }
    
}
