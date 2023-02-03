/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.DAO;

import cloud.util.Concat;
import cloud.util.Util;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

/**
 *
 * @author Tolotra
 */
public class ObjectBDD {

    private String nomDeTable;
    private boolean autoIncrement = true;
    private String pkey;
    private String sequence;
    private boolean synchronalizable = false;

    public boolean isSynchronalizable() {
        return synchronalizable;
    }

    public void setSynchronalizable(boolean synchronalizable) {
        this.synchronalizable = synchronalizable;
    }
    
    public String getSequence() {
        return sequence;
    }
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
    public String getPkey() {
        return pkey;
    }

    public void setPkey(String pkey) {
        this.pkey = pkey;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public String getNomDeTable() {
        return nomDeTable;
    }

    public void setNomDeTable(String nomDeTable) {
        this.nomDeTable = nomDeTable;
    }
    ///get numero de l'id
        private static String structureId(Connection con, String sequence, String idDim) throws Exception {
        String req = null;
        if(con.getClass().getName().equalsIgnoreCase("com.microsoft.sqlserver.jdbc.SQLServerConnection")){
            
         req = "SELECT next value for "+sequence;
        }
        else{
            req = "select nextval(?)";
        }
        String valiny = idDim;
        PreparedStatement stmt=null;
        
        ResultSet res=null;
        try{
            stmt = con.prepareStatement(req);
            if(!con.getClass().getName().equalsIgnoreCase("com.microsoft.sqlserver.jdbc.SQLServerConnection")){
                stmt.setObject(1, sequence);
            }  
            res = stmt.executeQuery();
        while (res.next()) {
            valiny += res.getString(1);
        }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally{
            if(res!=null)
                res.close();
            if(stmt!=null)
                stmt.close();
        }
        return valiny;
        
    }
    public int lastId(Connection con) throws Exception{
        String req = "select max("+this.pkey+") as id FROM "+this.nomDeTable;
        int valiny = 0;
        PreparedStatement stmt=null;
        ResultSet res=null;
        try{
            stmt = con.prepareStatement(req);
            res = stmt.executeQuery();
        while (res.next()) {
            valiny = res.getInt("id");
        }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally{
            if(res!=null)
                res.close();
            if(stmt!=null)
                stmt.close();
        }
        return valiny;
        
    }
    //PREND L'INDICE CORRESPONDANT A LA METHODE

    public int getIdMethod(String getname) throws Exception {
        int indice = -1;
        for (int i = 0; i < this.getClass().getMethods().length; i++) {
            if (this.getClass().getMethods()[i].getName().equalsIgnoreCase(getname) == true) {
                indice = i;
            }
        }
        if (indice == -1) {
            throw new Exception("Method not found");
        }
        return indice;

    }
    //
    public Method oneMethod(String methodName) throws Exception{
         Method m = null;
        for (int i = 0; i < this.getClass().getMethods().length; i++) {
            if (this.getClass().getMethods()[i].getName().equalsIgnoreCase(methodName) == true) {
                m = this.getClass().getMethods()[i];
            }
        }
        if (m == null) {
            throw new Exception("Method not found");
        }
        return m;
    }
    //Prendre les colonne non null
    public String[] getColumnNotNull(Connection con) throws SQLException, Exception {
        Field[] lField = this.getClass().getDeclaredFields();
        String[] bCol = this.columnName( nomDeTable, con);
        Vector <String> v = new Vector<>();
        for (Field lField1 : lField) {
            if (ObjectBDD.correspend(lField1.getName(), bCol)) {
                int indice = this.getIdMethod("get"+lField1.getName());
                if (this.getClass().getMethods()[indice].invoke(this) != null) {
                    v.add(lField1.getName());
                }
            }
        }
        String[] data = Util.vectorToArray(v);
        return data;

    }

    //PRENDRE LES DONNEES NON NULL
    public String[] getValuesNotNUll(Connection con) throws Exception {
        String[] column = this.getColumnNotNull(con);
        Vector<String> v = new Vector<>();
        for (String column1 : column) {
            int indice = this.getIdMethod("get" + column1);
            if (this.getClass().getDeclaredField(column1).getGenericType().getTypeName().equalsIgnoreCase("java.util.Date") == true) {
                DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                v.add("'" + sdf.format(this.getClass().getMethods()[indice].invoke(this)) + "'");
            } else {
                if (this.getClass().getMethods()[indice].invoke(this) instanceof Number) {
                    v.add(this.getClass().getMethods()[indice].invoke(this).toString());
                } else {
                    v.add("'" + this.getClass().getMethods()[indice].invoke(this).toString() + "'");
                }
            }
        }
        String[] data = Util.vectorToArray(v);
        return data;

    }

    //CHECK SI UN OBJET EST VIDE
    public boolean isEmpty(Connection con) throws Exception {
        Field[] lField = this.getClass().getDeclaredFields();
        String[] col = this.columnName( nomDeTable, con);
        int counter = 0;
        for (String col1 : col) {
            int indice = this.getIdMethod("get" + col1);
            if (this.getClass().getMethods()[indice].invoke(this) == null) {
                counter++;
            }
        }
        if (counter == col.length) {
            return true;
        } else {
            return false;
        }
    }

    ///FONCTION POUR PRENDRE LES DONNEES D'UN OBJET
    ///FONCTION POUR PRENDRE LES ATTRIBUTS D'UN OBJET
    public String[] getField() {
        Field[] lField = this.getClass().getDeclaredFields();
        Vector<String> v = new Vector<>();
        for (Field lField1 : lField) {
            v.add(lField1.getName());
        }
        String[] data = Util.vectorToArray(v);
        return data;
    }

    //nom de colonne d'une table
    public String[] columnName( String nomdeTable, Connection con) throws SQLException {
        java.sql.Statement stmt = null;
        ResultSet rs = null;
        String[] finale = null;
        try {
            Field[] lField = this.getClass().getDeclaredFields();
            String request = "SELECT * FROM " + nomdeTable;
            stmt = con.createStatement();
            rs = stmt.executeQuery(request);
            ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            String[] column = new String[numberOfColumns];
            for (int i = 0; i < column.length; i++) {
                    column[i] = rsmd.getColumnName(i + 1);
            }
            int ind = 0;
            Vector val = new Vector();
            while (ind != numberOfColumns) {
                for (Field lField1 : lField) {
                    if (lField1.getName().equalsIgnoreCase(column[ind]) == true) {
                        val.add(lField1.getName());
                    }
                }
                ind++;
            }
            finale = new String[val.size()];
            for (int iv = 0; iv < finale.length; iv++) {
                finale[iv] = (String) val.get(iv);
            }
        } catch (SecurityException | SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return finale;
    }
    public String[] icolumnName( String nomdeTable, Connection con) throws SQLException {
        java.sql.Statement stmt = null;
        ResultSet rs = null;
        String[] finale = null;
        try {
            Field[] lField = this.getClass().getDeclaredFields();
            String request = "SELECT * FROM " + nomdeTable;
            stmt = con.createStatement();
            rs = stmt.executeQuery(request);
            ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            String[] column = new String[numberOfColumns];
            for (int i = 0; i < column.length; i++) {
                if(this.autoIncrement == true){
                    if(rsmd.getColumnName(i+1).equalsIgnoreCase(this.pkey)){
                        continue;
                    }
                    else{
                        column[i] = rsmd.getColumnName(i + 1);
                    }
                }
                else{
                    column[i] = rsmd.getColumnName(i + 1);
                }
            }
            int ind = 0;
            Vector val = new Vector();
            while (ind != numberOfColumns) {
                for (Field lField1 : lField) {
                    if (lField1.getName().equalsIgnoreCase(column[ind]) == true) {
                        val.add(lField1.getName());
                    }
                }
                ind++;
            }
            finale = new String[val.size()];
            for (int iv = 0; iv < finale.length; iv++) {
                finale[iv] = (String) val.get(iv);
            }
        } catch (SecurityException | SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return finale;
    }
    
    //get data of table
    public String[] getData(Connection con) throws Exception {
        String[] lField = this.columnName( nomDeTable, con);
        Vector<String> v = new Vector<>();
        for (String lField1 : lField) {
            int idMethod = this.getIdMethod("get" + lField1);
            if(lField1.equalsIgnoreCase(this.pkey)){
                continue;
            }
            else if (this.getClass().getMethods()[idMethod].invoke(this) == null) {
                continue;
            } else if (this.getClass().getMethods()[idMethod].invoke(this) instanceof Number) {
                v.add(this.getClass().getMethods()[idMethod].invoke(this).toString());
            } else {
                if (this.getClass().getDeclaredField(lField1).getGenericType().getTypeName().equalsIgnoreCase("java.util.Date") == true) {
                    DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    v.add("'" + sdf.format(this.getClass().getMethods()[idMethod].invoke(this)) + "'");
                } else {
                    v.add("'" + this.getClass().getMethods()[idMethod].invoke(this).toString() + "'");
                }
            }
        }
        String[] data = Util.vectorToArray(v);
        return data;
    }

    //data for insert
    public String[] igetData(Connection con) throws Exception {
        String[] lField = this.columnName( nomDeTable, con);
        Vector<String> v = new Vector<>();
        for (String lField1 : lField) {
            int idMethod = this.getIdMethod("get" + lField1);
            if (lField1.equalsIgnoreCase(pkey) == true) {
                if (autoIncrement == true) {
                        continue;
                }
                else{
                    String seq = ObjectBDD.structureId(con, sequence, nomDeTable.toUpperCase()+"1_");
                    v.add("'"+seq+"'");
                }
            } else if (this.getClass().getMethods()[idMethod].invoke(this) == null) {
                v.add("null");
            } else if (this.getClass().getMethods()[idMethod].invoke(this) instanceof Number) {
                v.add("'"+this.getClass().getMethods()[idMethod].invoke(this).toString()+"'");
            } else {
                if (this.getClass().getDeclaredField(lField1).getGenericType().getTypeName().equalsIgnoreCase("java.util.Date") == true) {
                    DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    v.add("'" + sdf.format(this.getClass().getMethods()[idMethod].invoke(this)) + "'");
                } else {
                    String donne = this.getClass().getMethods()[idMethod].invoke(this).toString();
                    String escape = donne.replace("'", "''");
                    v.add("'" + escape + "'");
                }
            }
        }
        String[] data = Util.vectorToArray(v);
        return data;
    }

    ///FONCTION INSERT POUR INSERER DANS LA BASE
    public void insert(Connection con) throws SQLException, Exception {
        java.sql.Statement stmt = null;
        
       boolean conNull = Util.connectionNull(con);
        if(conNull){
            con = Util.getConnection(); 
        }
        try {
           // con.setAutoCommit(false);
            String[] data = this.igetData(con);
            String[] col = this.icolumnName( nomDeTable, con);
            String colonne = Concat.strcatV(col);
            String dataToInsert = Concat.strcatV(data);
            String dataForSync = Concat.strcatWithCote(data);
            String request = "INSERT INTO " + nomDeTable + "(" + colonne + ") VALUES(" + dataToInsert + ")";
            System.out.print(request);
            stmt = con.createStatement();
            stmt.execute(request);
            String request2 = "INSERT INTO " + nomDeTable + "(" + colonne + ") VALUES(" + dataForSync + ")";
            if(synchronalizable == true){
                stmt.execute("INSERT INTO requetenonenvoye(sql) VALUES('"+request2+"')");
            }
            System.out.println("success");
           // con.commit();
        } catch (Exception e) {
            //con.rollback();
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if(conNull){
                con.close();
            }
        }
    }
    public int insertReturningId(Connection con) throws SQLException, Exception {
        java.sql.Statement stmt = null;
        int returningId = 0;
        ResultSet rs = null;
       boolean conNull = Util.connectionNull(con);
        if(conNull){
            con = Util.getConnection(); 
        }
        try {
           // con.setAutoCommit(false);
            String[] data = this.igetData(con);
            String[] col = this.icolumnName( nomDeTable, con);
            String colonne = Concat.strcatV(col);
            String dataToInsert = Concat.strcatV(data);
            String dataForSync = Concat.strcatWithCote(data);
            String request = "INSERT INTO " + nomDeTable + "(" + colonne + ") VALUES(" + dataToInsert + ") returning "+this.pkey;
            System.out.print(request);
            stmt = con.createStatement();
            rs = stmt.executeQuery(request);
           while(rs.next()){
               returningId = rs.getInt(this.pkey.toLowerCase());
           }
          //  con.commit();
        } catch (Exception e) {
            //con.rollback();
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if(conNull){
                con.close();
            }
        }
        return returningId;
    }

    ///MISE A JOUR
    public void update(String forSearch, Connection con) throws Exception {
        java.sql.Statement stmt = null;
         boolean conNull = Util.connectionNull(con);
        if(conNull){
            con = Util.getConnection(); 
        }
        try {
            String[] data = this.getData(con);
            Vector fields = new Vector();
            Vector dataes = new Vector();
            int idMethod = this.getIdMethod("get" + forSearch);
            String idValue = this.getClass().getMethods()[idMethod].invoke(this).toString();
            String[] col = this.getColumnNotNull(con);
            String[] column = new String[col.length - 1];
            for (String col1 : col) {
                if (col1.equalsIgnoreCase(forSearch) == true) {
                    continue;
                } else {
                    fields.add(col1);
                }
            }
            for (int j = 0; j < fields.size(); j++) {
                column[j] = (String) fields.get(j);

            }
            String settable = Concat.strcatWithEqual(column, data, "=", ",");
            String request = "UPDATE " + nomDeTable + " SET " + settable + " WHERE " + forSearch + "='" + idValue + "'";
            System.out.println(request);
            stmt = con.createStatement();
            stmt.execute(request);
        } catch (Exception e) {
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if(conNull){
                con.close();
            }
        }

    }

    //Fonction mijery field mcorrespondre
    public static boolean correspend(String ray, String[] liste) {
        int counter = 0;
        for (int i = 0; i < liste.length; i++) {
            if (ray.equalsIgnoreCase(liste[i]) == true) {
                counter++;
            }
        }
        if (counter == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static int idcorrespend(String ray, String[] liste) {
        int indice = 0;
        for (int i = 0; i < liste.length; i++) {
            if (ray.equalsIgnoreCase(liste[i]) == true) {
                indice = i;
            }
        }
        return indice;
    }

    //FONCTION SELECT
    public Object[] find(Connection con) throws Exception {
        java.sql.Statement stmt = null;
        ResultSet rs = null;
        Vector valiny = new Vector();
         boolean conNull = Util.connectionNull(con);
        if(conNull){
            con = Util.getConnection(); 
        }
        try {
            stmt = con.createStatement();
            String[] col = this.columnName( nomDeTable, con);

            Field[] lField = this.getClass().getDeclaredFields();
            String request = null;
            if (this.isEmpty(con) == true) {
                request = "SELECT * FROM " + nomDeTable;

            } else {
                String[] column = this.getColumnNotNull(con);
                String[] data = this.getValuesNotNUll(con);
                String[] newdata = new String[data.length];
                System.arraycopy(data, 0, newdata, 0, newdata.length);
                String filter = Concat.strcatWithEqual(column, newdata, "=", " AND ");
                request = "SELECT * FROM " + nomDeTable + " WHERE " + filter;

            }
            System.out.println(request);
            rs = stmt.executeQuery(request);
            while (rs.next()) {

                Object tempo = this.getClass().newInstance();
                for (Field lField1 : lField) {
                    if (ObjectBDD.correspend(lField1.getName(), col) == true) {

                        int indice = this.getIdMethod( "set" + lField1.getName());
                        int corresp = ObjectBDD.idcorrespend(lField1.getName(), col);

                        if (con.getClass().getName().equalsIgnoreCase("oracle.jdbc.driver.T4CConnection") ) {
                            if (lField1.getGenericType().getTypeName().equalsIgnoreCase("java.lang.Integer") == true) {
                                BigDecimal test = (BigDecimal) rs.getObject(corresp + 1);

                                tempo.getClass().getMethods()[indice].invoke(tempo, test.intValue());
                            } else if (lField1.getGenericType().getTypeName().equalsIgnoreCase("int") == true) {
                                BigDecimal test = (BigDecimal) rs.getObject(corresp + 1);

                                tempo.getClass().getMethods()[indice].invoke(tempo, test.intValue());
                            } else if (lField1.getGenericType().getTypeName().equalsIgnoreCase("double") == true) {
                                BigDecimal test = (BigDecimal) rs.getObject(corresp + 1);

                                tempo.getClass().getMethods()[indice].invoke(tempo, test.doubleValue());
                            } else if (lField1.getGenericType().getTypeName().equalsIgnoreCase("java.lang.Double") == true) {
                                BigDecimal test = (BigDecimal) rs.getObject(corresp + 1);

                                tempo.getClass().getMethods()[indice].invoke(tempo, test.doubleValue());
                            } else if (lField1.getGenericType().getTypeName().equalsIgnoreCase("java.util.Date") == true) {
                                tempo.getClass().getMethods()[indice].invoke(tempo, rs.getObject(corresp + 1));
                            } else {
                                tempo.getClass().getMethods()[indice].invoke(tempo, rs.getObject(corresp + 1));
                            }
                        } 
                        else if(con.getClass().getName().equalsIgnoreCase("com.microsoft.sqlserver.jdbc.SQLServerConnection")){
                             if (lField1.getGenericType().getTypeName().equalsIgnoreCase("double") == true) {
                                Float test = (Float) rs.getObject(corresp + 1);
                                tempo.getClass().getMethods()[indice].invoke(tempo, test.doubleValue());
                            } else if (lField1.getGenericType().getTypeName().equalsIgnoreCase("java.lang.Double") == true) {
                                Float test = (Float) rs.getObject(corresp + 1);
                                tempo.getClass().getMethods()[indice].invoke(tempo, test.doubleValue());
                            }  else {
                                tempo.getClass().getMethods()[indice].invoke(tempo, rs.getObject(corresp + 1));
                            }
                        }
                        else {
                            try {
                                //System.out.println(con.getClass().getName());
                                //System.out.println(tempo.getClass().getMethods()[indice]+":"+rs.getObject(corresp + 1)+"("+rs.getObject(corresp + 1).getClass()+")");
                                tempo.getClass().getMethods()[indice].invoke(tempo, rs.getObject(corresp+1));                               
            
                            } catch (IllegalArgumentException e) {
                                DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    tempo.getClass().getMethods()[indice].invoke(tempo, sdf.format(rs.getObject(corresp + 1)));
                                } catch (IllegalArgumentException ex) {
                                    //ex.printStackTrace();
                                    Date d = (Date) rs.getObject(corresp + 1);
                                    tempo.getClass().getMethods()[indice].invoke(tempo, new Timestamp(d.getTime()));
                                    
                                }
                                e.printStackTrace();
                            }
                        }

                    }
                }

                valiny.add(tempo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if(conNull){
                System.out.println("close");
                con.close();
            }
        }

        return valiny.toArray();
    }
    public Object[] find(Connection con,String whereClause) throws Exception {
        java.sql.Statement stmt = null;
        ResultSet rs = null;
        Vector valiny = new Vector();
         boolean conNull = Util.connectionNull(con);
        if(conNull){
            con = Util.getConnection(); 
        }
        try {
            stmt = con.createStatement();
            String[] col = this.columnName( nomDeTable, con);

            Field[] lField = this.getClass().getDeclaredFields();
            String request = null;
            if (this.isEmpty(con) == true) {
                request = "SELECT * FROM " + nomDeTable;
                if(!whereClause.equals("")){
                    request+="  WHERE "+whereClause;
                }

            } else {
                String[] column = this.getColumnNotNull(con);
                String[] data = this.getValuesNotNUll(con);
                String[] newdata = new String[data.length];
                System.arraycopy(data, 0, newdata, 0, newdata.length);
                String filter = Concat.strcatWithEqual(column, newdata, "=", " AND ");
                request = "SELECT * FROM " + nomDeTable + " WHERE " + filter;
                if(!whereClause.equals("")){
                    request+="  AND "+whereClause;
                }

            }
            System.out.println(request);
            rs = stmt.executeQuery(request);
            while (rs.next()) {

                Object tempo = this.getClass().newInstance();
                for (Field lField1 : lField) {
                    if (ObjectBDD.correspend(lField1.getName(), col) == true) {

                        int indice = this.getIdMethod( "set" + lField1.getName());
                        int corresp = ObjectBDD.idcorrespend(lField1.getName(), col);

                        if (con.getClass().getName().equalsIgnoreCase("oracle.jdbc.driver.T4CConnection") ) {
                            if (lField1.getGenericType().getTypeName().equalsIgnoreCase("java.lang.Integer") == true) {
                                BigDecimal test = (BigDecimal) rs.getObject(corresp + 1);

                                tempo.getClass().getMethods()[indice].invoke(tempo, test.intValue());
                            } else if (lField1.getGenericType().getTypeName().equalsIgnoreCase("int") == true) {
                                BigDecimal test = (BigDecimal) rs.getObject(corresp + 1);

                                tempo.getClass().getMethods()[indice].invoke(tempo, test.intValue());
                            } else if (lField1.getGenericType().getTypeName().equalsIgnoreCase("double") == true) {
                                BigDecimal test = (BigDecimal) rs.getObject(corresp + 1);

                                tempo.getClass().getMethods()[indice].invoke(tempo, test.doubleValue());
                            } else if (lField1.getGenericType().getTypeName().equalsIgnoreCase("java.lang.Double") == true) {
                                BigDecimal test = (BigDecimal) rs.getObject(corresp + 1);

                                tempo.getClass().getMethods()[indice].invoke(tempo, test.doubleValue());
                            } else if (lField1.getGenericType().getTypeName().equalsIgnoreCase("java.util.Date") == true) {
                                tempo.getClass().getMethods()[indice].invoke(tempo, rs.getObject(corresp + 1));
                            } else {
                                tempo.getClass().getMethods()[indice].invoke(tempo, rs.getObject(corresp + 1));
                            }
                        } 
                        else if(con.getClass().getName().equalsIgnoreCase("com.microsoft.sqlserver.jdbc.SQLServerConnection")){
                             if (lField1.getGenericType().getTypeName().equalsIgnoreCase("double") == true) {
                                Float test = (Float) rs.getObject(corresp + 1);
                                tempo.getClass().getMethods()[indice].invoke(tempo, test.doubleValue());
                            } else if (lField1.getGenericType().getTypeName().equalsIgnoreCase("java.lang.Double") == true) {
                                Float test = (Float) rs.getObject(corresp + 1);
                                tempo.getClass().getMethods()[indice].invoke(tempo, test.doubleValue());
                            }  else {
                                tempo.getClass().getMethods()[indice].invoke(tempo, rs.getObject(corresp + 1));
                            }
                        }
                        else {
                            try {
                                //System.out.println(con.getClass().getName());
                                //System.out.println(tempo.getClass().getMethods()[indice]+":"+rs.getObject(corresp + 1)+"("+rs.getObject(corresp + 1).getClass()+")");
                                tempo.getClass().getMethods()[indice].invoke(tempo, rs.getObject(corresp+1));                               
            
                            } catch (IllegalArgumentException e) {
                                DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    tempo.getClass().getMethods()[indice].invoke(tempo, sdf.format(rs.getObject(corresp + 1)));
                                } catch (IllegalArgumentException ex) {
                                    //ex.printStackTrace();
                                    Date d = (Date) rs.getObject(corresp + 1);
                                    tempo.getClass().getMethods()[indice].invoke(tempo, new Timestamp(d.getTime()));
                                    
                                }
                                e.printStackTrace();
                            }
                        }

                    }
                }

                valiny.add(tempo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if(conNull){
                con.close();
            }
        }

        return valiny.toArray();
    }

   
}
