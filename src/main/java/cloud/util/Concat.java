/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.util;

/**
 *
 * @author Tolotra
 */
public class Concat {
    public static String strcat(String[] ls) {
        String Mitambatra = ls[0];
        for (int i = 0; i < ls.length - 1; i++) {
            Mitambatra = Mitambatra.concat(ls[i + 1]);
        }
        return Mitambatra;
    }

    public static String strcatV(String[] ls) {
        String Mitambatra = ls[0];
        for (int i = 0; i < ls.length - 1; i++) {
            Mitambatra = Mitambatra.concat("," + ls[i + 1]);
        }
        return Mitambatra;
    }

    public static String strcatWithCote(String[] ls) {
        String Mitambatra = "'" + ls[0] + "'";
        for (int i = 0; i < ls.length - 1; i++) {
            if(ls[i+1].equals("null")){
                continue;
            }
            Mitambatra = Mitambatra.concat(",'" + ls[i + 1] + "'");
        }
        return Mitambatra;
    }
    public static String strcatWithEscapeCote(String[] ls) {
        String Mitambatra = "'" + ls[0] + "'";
        for (int i = 0; i < ls.length - 1; i++) {
            Mitambatra = Mitambatra.concat(",''" + ls[i + 1] + "''");
        }
        return Mitambatra;
    }

    public static String strcatWithEqual(String[] col, String[] value, String operator, String separator) {
        String Mitambatra = col[0] + operator + value[0];
        for (int i = 0; i < col.length - 1; i++) {
            Mitambatra = Mitambatra.concat(separator + col[i + 1] + operator + value[i + 1]);
        }
        return Mitambatra;
    }

}
