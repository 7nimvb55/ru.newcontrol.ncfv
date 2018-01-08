/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

/**
 *
 * @author Администратор
 */
public class NcIdxSubStringVariant {
    String strInLowerCase;
    int strInLowerCaseHash;
    String hexForLowerCase;
    int hexForLowerCaseHash;
    String strInUpperCase;
    int strInUpperCaseHash;
    String hexForUpperCase;
    int hexForUpperCaseHash;

    /**
     *
     * @param strInLowerCase
     * @param strInUpperCase
     */
    public NcIdxSubStringVariant(String strInLowerCase, String strInUpperCase) {
        this.strInLowerCase = strInLowerCase;
        this.strInLowerCaseHash = this.strInLowerCase.hashCode();
        this.hexForLowerCase = NcPathToArrListStr.toStrUTFinHEX(this.strInLowerCase);
        this.hexForLowerCaseHash = this.hexForLowerCase.hashCode();
        this.strInUpperCase = strInUpperCase;
        this.strInUpperCaseHash = this.strInUpperCase.hashCode();
        this.hexForUpperCase = NcPathToArrListStr.toStrUTFinHEX(this.strInUpperCase);
        this.hexForUpperCaseHash = this.hexForUpperCase.hashCode();
    }
    
}
