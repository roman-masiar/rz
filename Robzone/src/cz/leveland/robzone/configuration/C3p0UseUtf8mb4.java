package cz.leveland.robzone.configuration;

import java.sql.Connection;
import java.sql.Statement;

import com.mchange.v2.c3p0.AbstractConnectionCustomizer;

public class C3p0UseUtf8mb4 extends  AbstractConnectionCustomizer{
    @Override
   public void onAcquire(Connection c, String parentDataSourceIdentityToken)   
       throws Exception {
    	
       super.onAcquire(c, parentDataSourceIdentityToken);
       
      //System.out.println("setting utf8mb4");

       try(Statement stmt = c.createStatement()) {
           stmt.executeQuery("SET NAMES utf8mb4");
       }
   }
}