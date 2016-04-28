package com.pm.currencymonitor.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CurrencyCodeDataService extends HttpServlet {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 3713262335277371818L;

	@Override
 protected void doGet(HttpServletRequest req, HttpServletResponse resp)
     throws ServletException, IOException {
    	
    	PrintWriter out = resp.getWriter();
    	
    	String json = readFile("./currencymap.json");
    	
    	out.println(json);
    	out.flush();
    	
    	
    }
    
    private String readFile( String file ) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader (file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        try {
            while( ( line = reader.readLine() ) != null ) {
                stringBuilder.append( line );
                stringBuilder.append( ls );
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

}
