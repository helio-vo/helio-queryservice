package eu.heliovo.queryservice.server.util;

import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import eu.heliovo.queryservice.common.util.ConfigurationProfiler;
import eu.heliovo.queryservice.common.util.RegistryUtils;

public class ServiceInfo extends Thread {
	
	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	private Writer pw;
	private String tablename=null;
	
	
	public ServiceInfo(Writer writer)
	{
		pw=writer;
	}
	
	public ServiceInfo(String table, Writer writer)
	{
		tablename=table;
		pw=writer;
	}
		
	public void run(){
		if(tablename==null){
			getTableNames(pw);
		}
		else{
			getTableFields(tablename,pw);
		}
	}
	
	

	private void getTableNames(Writer pw) {
//		StarTableFactory factory = new StarTableFactory();
		String[] tablenames;
		try {
			tablenames=RegistryUtils.getInstance().getTableNames();
			this.writeHeaders(pw);
			pw.write( "<TABLE name=\"Table Names\">\n" );
			pw.write( "<FIELD arraysize=\"*\" datatype=\"char\" name=\"table_names\">\n" );
			pw.write( "  <DESCRIPTION>Names of tables for this service. Input as 'FROM' in functions</DESCRIPTION>\n" );
			pw.write( "</FIELD>\n" );
			pw.write( "<DATA>\n" );
			pw.write( "<TABLEDATA>\n" );
			for(int i=0; i<tablenames.length; i++){
			  pw.write( "  <TR>\n" );
			  pw.write( "    <TD>" );
			  pw.write(tablenames[i].trim());
			  pw.write( "</TD>\n" );
			  pw.write( "  </TR>\n" );
			}
			pw.write( "</TABLEDATA>\n" );
			pw.write( "</DATA>\n" );
			pw.write( "</TABLE>\n" );
			this.writeFooters(pw);
			pw.flush();
			pw.close();
//			StarTable table = factory.makeStarTable( tablenames.toString() );
//			table.setName("TableNames");
//			VOSerializer.makeSerializer( DataFormat.TABLEDATA, table ).writeInlineTableElement( (BufferedWriter)pw );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void getTableFields(String table_name, Writer pw) {
		String name=table_name.trim();
//		StarTableFactory factory = new StarTableFactory();
		String[] fieldnames;
		try {
			if(checkTablename(name)){
				fieldnames=RegistryUtils.getInstance().getColumnNames(name);
				this.writeHeaders(pw);
				pw.write( "<TABLE name=\"Table Fields for table '"+ name+"'\">\n" );
				pw.write( "<FIELD arraysize=\"*\" datatype=\"char\" name=\"field_names\">\n" );
				pw.write( "  <DESCRIPTION>Names of fields for table " +name+". Input as part of 'WHERE' in functions</DESCRIPTION>\n" );
				pw.write( "</FIELD>\n" );
				pw.write( "<DATA>\n" );
				pw.write( "<TABLEDATA>\n" );
				for(int i=0; i<fieldnames.length; i++){
				  pw.write( "  <TR>\n" );
				  pw.write( "    <TD>" );
				  pw.write(fieldnames[i]);
				  pw.write( "</TD>\n" );
				  pw.write( "  </TR>\n" );
				}
				pw.write( "</TABLEDATA>\n" );
				pw.write( "</DATA>\n" );
				pw.write( "</TABLE>\n" );
				this.writeFooters(pw);
			}
			else {
				writeErrorPage(name, pw);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void writeErrorPage(String name, Writer pw) throws Exception {
		pw.write("<helio:queryResponse xmlns:helio=\"http://helio-vo.eu/xml/QueryService/v0.1\">");
		pw.write( "<VOTABLE version='1.1' xmlns=\"http://www.ivoa.net/xml/VOTable/v1.1\">\n" );
		pw.write( "<RESOURCE>\n" );
	    pw.write( "<DESCRIPTION>"+ConfigurationProfiler.getInstance().getProperty("sql.votable.head.desc")+"</DESCRIPTION>\n" );
	    pw.write( "<INFO name=\"QUERY_STATUS\" value=\"ERROR\">\n");
	    pw.write( "  <DESCRIPTION>Table name '"+name+"' not recognized for this service.</DESCRIPTION>\n" );
	    pw.write( "</INFO>\n" ); 
	    pw.write( "<INFO name=\"EXECUTED_AT\" value=\""+now()+"\"/>\n");
	    this.writeFooters(pw);
		
	}

	private boolean checkTablename(String table_name) throws Exception {
		String[] tablenames=RegistryUtils.getInstance().getTableNames();
		for (int i=0;i<tablenames.length; i++){
			if (tablenames[i].trim().intern()==table_name.trim().intern()){
				return true;
			}
		}
		return false;
	}

	private void writeHeaders(Writer pw) throws Exception{
		pw.write("<helio:queryResponse xmlns:helio=\"http://helio-vo.eu/xml/QueryService/v0.1\">");
		pw.write( "<VOTABLE version='1.1' xmlns=\"http://www.ivoa.net/xml/VOTable/v1.1\">\n" );
		pw.write( "<RESOURCE>\n" );
	    pw.write( "<DESCRIPTION>"+ConfigurationProfiler.getInstance().getProperty("sql.votable.head.desc")+"</DESCRIPTION>\n" );
	    pw.write( "<INFO name=\"QUERY_STATUS\" value=\"OK\"/>\n");
	    pw.write( "<INFO name=\"EXECUTED_AT\" value=\""+now()+"\"/>\n");
		
	}
	
	private void writeFooters(Writer pw) throws Exception{
		pw.write( "</RESOURCE>\n" );
		pw.write( "</VOTABLE>\n" );
		pw.write("</helio:queryResponse>");
		
	}
	
	private static String now() {
    	
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());

      }
	
}