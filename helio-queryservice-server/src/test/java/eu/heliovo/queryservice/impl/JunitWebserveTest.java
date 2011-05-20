package eu.heliovo.queryservice.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.Endpoint;

import org.apache.catalina.startup.Embedded;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import eu.heliovo.queryservice.common.dao.CommonDaoFactory;
import eu.heliovo.queryservice.common.dao.interfaces.CommonDao;
import eu.heliovo.queryservice.common.transfer.FileResultTO;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;
import eu.heliovo.queryservice.common.util.CommonUtils;
import eu.heliovo.queryservice.common.util.ConnectionManager;
import eu.heliovo.queryservice.common.util.InstanceHolders;
import eu.heliovo.queryservice.server.util.QueryThreadAnalizer;

@SuppressWarnings("unused")
public class JunitWebserveTest {

	private final Logger logger = Logger.getLogger(this.getClass());

	// TODO: any reason why they are static?
	protected static Endpoint ep;
	protected static String address;
	protected static URL wsdlURL;
	protected static QName serviceName;
	protected static QName portName;
	protected static Embedded server;

	/**
	 * The executor is used to synchronize query analyzer threads
	 */
	private ExecutorService executor = Executors.newSingleThreadExecutor();

	/**
	 * Setup the test environment, i.e. hsqldb and log4j.
	 */
	@Before
	public void setUp() throws Exception {
		File projectRoot = new File(".").getAbsoluteFile();

		// one-time initialization code
		ClassLoader loader = JunitWebserveTest.class.getClassLoader();
		String helioDbPath;
		
		helioDbPath = new File(projectRoot, "target/helio-queryservice-impl/WEB-INF/").getCanonicalPath();
		//System.out.println(" : helio db file path  : "+helioDbPath);
		InstanceHolders.getInstance().setProperty("hsqldb.database.path", helioDbPath);
		
		// setup log4j
		Properties props = new Properties();
		InputStream configStream = JunitWebserveTest.class.getResourceAsStream("/log4j.properties");
		props.load(configStream);
		configStream.close();

		props.setProperty("log4j.appender.LOGFILE.File", helioDbPath + "/helio_query.log");
		LogManager.resetConfiguration();
		PropertyConfigurator.configure(props);
		
		Connection connection = ConnectionManager.getConnection();
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE PUBLIC.HELIO(URL VARCHAR(200),START_DATE VARCHAR(100),END_DATE VARCHAR(100));");
		
		assertEquals(1, statement.executeUpdate("INSERT INTO HELIO VALUES('http://msslxw.mssl.ucl.ac.uk:8080/HelioICS/HelioQueryService?TIME=1890-10-20T20:30:56/2009-10-20T20:30:56','1890-10-20T20:30:56','2009-10-20T20:30:56')"));
		assertEquals(1, statement.executeUpdate("INSERT INTO HELIO VALUES('http://msslxw.mssl.ucl.ac.uk:8080/HelioICS/HelioQueryService?TIME=1990-10-20T20:30:56/2009-10-20T20:30:56','1990-10-20T20:30:56','2009-10-20T20:30:56')"));
		assertEquals(1, statement.executeUpdate("INSERT INTO HELIO VALUES('http://msslxw.mssl.ucl.ac.uk:8080/HelioICS/HelioQueryService?TIME=1900-10-20T20:30:56/2009-10-20T20:30:56','1900-10-20T20:30:56','2009-10-20T20:30:56')"));
		connection.commit();
		statement.close();
		connection.close();
	}

	/**
	 * Clean up environment
	 * 
	 * @throws Exception
	 *             if anything goes wrong.
	 */
	@After
	public void tearDown() throws Exception {
		InstanceHolders.getInstance().setProperty("hsqldb.database.path", null);
		Connection connection = ConnectionManager.getConnection();
		Statement statement = connection.createStatement();
		statement.execute("DROP TABLE PUBLIC.HELIO;");

	}

	/**
	 * Submit a full query to the server.
	 * 
	 * @throws Exception
	 *             if anything goes wrong.
	 */
	
	@Test
	public void testFullQueryQname() throws Exception {
		PipedReader pr = new PipedReader();
		PipedWriter pw = new PipedWriter(pr);

		try {
			// System.out.println("Testing Full Query Service ....");

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);

			DocumentBuilder builder = factory.newDocumentBuilder();

			CommonCriteriaTO comCriteriaTO = new CommonCriteriaTO();
			String[] arrStartTime = new String[1];
			String[] arrEndTime = new String[1];
			// Start time
			arrStartTime[0] = "1890-10-20T20:30:56";
			arrEndTime[0] = "2009-10-20T20:30:56";
			// Setting data
			comCriteriaTO.setStartDateTimeList(arrStartTime);
			comCriteriaTO.setEndDateTimeList(arrEndTime);
			String[] arr = new String[1];
			arr[0] = "helio";
			comCriteriaTO.setListTableName(arr);
			comCriteriaTO.setStatus("WebService");
			comCriteriaTO.setPrintWriter(pw);

			String result = runJob(comCriteriaTO, pr);
			// TODO: test if result is valid

		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	
	@Test
	public void testMaxRecordsQueryQname() throws Exception {

		PipedReader pr = new PipedReader();
		PipedWriter pw = new PipedWriter(pr);

		try {
			// System.out.println("Testing Full Query Service ....");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();

			//
			CommonCriteriaTO comCriteriaTO = new CommonCriteriaTO();
			String[] arrStartTime = new String[1];
			String[] arrEndTime = new String[1];
			// Start time
			arrStartTime[0] = "1890-10-20T20:30:56";
			arrEndTime[0] = "2009-10-20T20:30:56";
			// Setting data
			comCriteriaTO.setStartDateTimeList(arrStartTime);
			comCriteriaTO.setEndDateTimeList(arrEndTime);
			String[] arr = new String[1];
			arr[0] = "helio";
			comCriteriaTO.setListTableName(arr);
			comCriteriaTO.setStatus("WebService");
			comCriteriaTO.setNoOfRows("1");
			comCriteriaTO.setPrintWriter(pw);
			
			// System.out.println("Creating a VOTable ....");
			String result = runJob(comCriteriaTO, pr);
			
		} finally {

			if (pw != null) {
				pw.close();
			}
		}
	}

	
	@Test
	public void testStartIndexAndMaxRecordsQueryQname() throws Exception {

		PipedReader pr = new PipedReader();
		PipedWriter pw = new PipedWriter(pr);

		try {
			// System.out.println("Testing Full Query Service ....");
			ClassLoader loader = this.getClass().getClassLoader();
			String helioDbPath = loader.getResource("test.txt").getFile();
			helioDbPath = helioDbPath.replace("target/classes/test.txt", "") + "src/main" + "/webapp/WEB-INF";
			// System.out.println(" : helio db file path  : "+helioDbPath);
			InstanceHolders.getInstance().setProperty("hsqldb.database.path", helioDbPath);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			//
			CommonCriteriaTO comCriteriaTO = new CommonCriteriaTO();

			String[] arrStartTime = new String[1];
			String[] arrEndTime = new String[1];
			// Start time
			arrStartTime[0] = "1890-10-20T20:30:56";
			arrEndTime[0] = "2009-10-20T20:30:56";
			// Setting data
			comCriteriaTO.setStartDateTimeList(arrStartTime);
			comCriteriaTO.setEndDateTimeList(arrEndTime);
			String[] arr = new String[1];
			arr[0] = "helio";
			comCriteriaTO.setListTableName(arr);
			comCriteriaTO.setStatus("WebService");
			comCriteriaTO.setNoOfRows("2");
			comCriteriaTO.setStartRow("1");
			comCriteriaTO.setPrintWriter(pw);
			// System.out.println("Creating a VOTable ....");
			String result = runJob(comCriteriaTO, pr);
			
		}

		finally {

			if (pw != null) {
				pw.close();
			}
			// Setting hsqldb.database.path to null.
			InstanceHolders.getInstance().setProperty("hsqldb.database.path", null);
		}

	}

	
	@Test
	public void testLongRunningQueryQname() throws Exception {

		PipedReader pr = new PipedReader();
		PipedWriter pw = new PipedWriter(pr);
		BufferedWriter out = new BufferedWriter(pw);
		try {
			// System.out.println("Testing Long Running Query Service ....");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			// Passing values to Trasfer Object.
			CommonCriteriaTO comCriteriaTO = new CommonCriteriaTO();
			String[] arrStartTime = new String[1];
			String[] arrEndTime = new String[1];
			// Start time
			arrStartTime[0] = "1890-10-20T20:30:56";
			arrEndTime[0] = "2009-10-20T20:30:56";
			// Setting data
			comCriteriaTO.setStartDateTimeList(arrStartTime);
			comCriteriaTO.setEndDateTimeList(arrEndTime);
			String[] arr = new String[1];
			arr[0] = "helio";
			comCriteriaTO.setListTableName(arr);
			comCriteriaTO.setStatus("WebService");
			comCriteriaTO.setLongRunningQueryStatus("LongRunning");
			comCriteriaTO.setPrintWriter(out);
			// Creating UUID
			UUID uuid = UUID.randomUUID();
			String randomUUIDString = uuid.toString();
			// file TO
			FileResultTO fileTO = new FileResultTO();
			fileTO.setRandomUUIDString(randomUUIDString);
			String xmlString = CommonUtils.createXmlForWebService(fileTO);
			// System.out.println(" : XML String : "+xmlString);

			// Setting piped reader
			comCriteriaTO.setLongRunningPrintWriter(pw);
			// Set data to print writer.
			comCriteriaTO.setDataXml(xmlString);
			// System.out.println("Creating response XML ....");
			// Thread created to load data into response.
			CommonDao commonNameDao = CommonDaoFactory.getInstance().getCommonDAO();
			commonNameDao.generatelongRunningQueryXML(comCriteriaTO);
			// System.out.println("Response XML done ....");
			// Print print reader.
			// System.out.println("Printing response ....");
			printPrintReader(pr);

		} catch (Exception e) {
			System.out.println(" Exception occured in testQueryQname : " + e);
		}

		finally {

			if (pw != null) {
				pw.close();
			}
		}

	}

	
	@Test
	public void testLongRunningQueryAssertEquals() throws Exception {

		PipedReader pr = new PipedReader();
		PipedWriter pw = new PipedWriter(pr);
		BufferedWriter out = new BufferedWriter(pw);
		try {
			// System.out.println("Testing Long Running Query Service ....");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			// Passing values to Trasfer Object.
			CommonCriteriaTO comCriteriaTO = new CommonCriteriaTO();
			String[] arrStartTime = new String[1];
			String[] arrEndTime = new String[1];
			// Start time
			arrStartTime[0] = "1890-10-20T20:30:56";
			arrEndTime[0] = "2009-10-20T20:30:56";
			// Setting data
			comCriteriaTO.setStartDateTimeList(arrStartTime);
			comCriteriaTO.setEndDateTimeList(arrEndTime);
			String[] arr = new String[1];
			arr[0] = "helio";
			comCriteriaTO.setListTableName(arr);
			comCriteriaTO.setStatus("WebService");
			comCriteriaTO.setLongRunningQueryStatus("LongRunning");
			comCriteriaTO.setPrintWriter(out);
			// Creating UUID
			UUID uuid = UUID.randomUUID();
			String randomUUIDString = uuid.toString();
			// file TO
			FileResultTO fileTO = new FileResultTO();
			fileTO.setRandomUUIDString(randomUUIDString);
			String xmlString = CommonUtils.createXmlForWebService(fileTO);
			// System.out.println(" : XML String : "+xmlString);

			// Setting piped reader
			comCriteriaTO.setLongRunningPrintWriter(pw);
			// Set data to print writer.
			comCriteriaTO.setDataXml(xmlString);
			// System.out.println("Creating response XML ....");
			// Thread created to load data into response.
			CommonDao commonNameDao = CommonDaoFactory.getInstance().getCommonDAO();
			commonNameDao.generatelongRunningQueryXML(comCriteriaTO);
			// System.out.println("Response XML done ....");
			String sComStr = "<helio:resultResponse xmlns:helio=\"http://helio-vo.eu/xml/LongQueryService/v0.1\">"
					+ "<ResultInfo><ID>" + randomUUIDString + "</ID></ResultInfo></helio:resultResponse>";
			// Print print reader.
			// System.out.println("Checking for assert equals...");
			assertEquals(sComStr, printPrintReaderStringRes(pr));
			// System.out.println("Success....!!!");
		} catch (Exception e) {
			System.out.println(" Exception occured in testQueryQname : " + e);
		}

		finally {

			if (pw != null) {
				pw.close();
			}
		}

	}

	
	@Test
	public void testTwoTableQueryQname() throws Exception {

		PipedReader pr = new PipedReader();
		PipedWriter pw = new PipedWriter(pr);

		try {
			// System.out.println(" Querying on two tables ....");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			//
			CommonCriteriaTO comCriteriaTO = new CommonCriteriaTO();
			String[] arrStartTime = new String[1];
			String[] arrEndTime = new String[1];
			// Start time
			arrStartTime[0] = "1890-10-20T20:30:56";
			arrEndTime[0] = "2009-10-20T20:30:56";
			// Setting data
			comCriteriaTO.setStartDateTimeList(arrStartTime);
			comCriteriaTO.setEndDateTimeList(arrEndTime);
			String[] arr = new String[2];
			arr[0] = "helio";
			arr[1] = "helio";
			comCriteriaTO.setListTableName(arr);
			comCriteriaTO.setStatus("WebService");
			comCriteriaTO.setPrintWriter(pw);
			// System.out.println("Creating a VOTable ....");
			new QueryThreadAnalizer(comCriteriaTO).start();
			Thread.sleep(20000);
			// Print reader
			// System.out.println("Printing VOTable ....");
			printPrintReader(pr);

		} catch (Exception e) {
			System.out.println(" Exception occured in testQueryQname : " + e);
		}

		finally {

			if (pw != null) {
				pw.close();
			}
			// Setting hsqldb.database.path to null.
			InstanceHolders.getInstance().setProperty("hsqldb.database.path", null);
		}

	}

	
	@Test
	public void testArrayStartAndEndTimeQueryQname() throws Exception {

		PipedReader pr = new PipedReader();
		PipedWriter pw = new PipedWriter(pr);

		try {
			// System.out.println("Testing array of start and end time ....");
			ClassLoader loader = this.getClass().getClassLoader();
			String helioDbPath = loader.getResource("test.txt").getFile();
			helioDbPath = helioDbPath.replace("target/classes/test.txt", "") + "src/main" + "/webapp/WEB-INF";
			// System.out.println(" : helio db file path  : "+helioDbPath);
			InstanceHolders.getInstance().setProperty("hsqldb.database.path", helioDbPath);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			//
			CommonCriteriaTO comCriteriaTO = new CommonCriteriaTO();
			String[] arrStartTime = new String[2];
			String[] arrEndTime = new String[2];
			// Start time and End time.
			arrStartTime[0] = "1890-10-20T20:30:56";
			arrEndTime[0] = "2009-10-20T20:30:56";
			// Start time and End time
			arrStartTime[1] = "1990-10-20T20:30:56";
			arrEndTime[1] = "2009-10-20T20:30:56";
			// Setting data
			comCriteriaTO.setStartDateTimeList(arrStartTime);
			comCriteriaTO.setEndDateTimeList(arrEndTime);
			String[] arr = new String[2];
			arr[0] = "helio";
			arr[1] = "helio";
			comCriteriaTO.setListTableName(arr);
			comCriteriaTO.setStatus("WebService");
			comCriteriaTO.setPrintWriter(pw);
			// System.out.println("Creating a VOTable ....");
			new QueryThreadAnalizer(comCriteriaTO).start();
			Thread.sleep(20000);
			// Print reader
			// System.out.println("Printing VOTable ....");
			printPrintReader(pr);

		} catch (Exception e) {
			System.out.println(" Exception occured in testQueryQname : " + e);
		}

		finally {

			if (pw != null) {
				pw.close();
			}
			// Setting hsqldb.database.path to null.
			InstanceHolders.getInstance().setProperty("hsqldb.database.path", null);
		}

	}

	
	@Test
	public void testJoinTables() throws Exception {

		PipedReader pr = new PipedReader();
		PipedWriter pw = new PipedWriter(pr);

		try {
			// System.out.println("Joining 2 tables ....");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			//
			CommonCriteriaTO comCriteriaTO = new CommonCriteriaTO();
			String[] arrStartTime = new String[2];
			String[] arrEndTime = new String[2];
			// Start time and End time.
			arrStartTime[0] = "1890-10-20T20:30:56";
			arrEndTime[0] = "2009-10-20T20:30:56";
			// Start time and End time
			arrStartTime[1] = "1990-10-20T20:30:56";
			arrEndTime[1] = "2009-10-20T20:30:56";
			// Setting data
			comCriteriaTO.setStartDateTimeList(arrStartTime);
			comCriteriaTO.setEndDateTimeList(arrEndTime);
			// Joining 2 tables
			comCriteriaTO.setJoin("yes");
			String[] arr = new String[2];
			arr[0] = "helio";
			arr[1] = "helio";
			comCriteriaTO.setListTableName(arr);
			comCriteriaTO.setStatus("WebService");
			comCriteriaTO.setPrintWriter(pw);
			// System.out.println("Creating a VOTable ....");
			new QueryThreadAnalizer(comCriteriaTO).start();
			Thread.sleep(20000);
			// Print reader
			// System.out.println("Printing VOTable ....");
			printPrintReader(pr);

		} catch (Exception e) {
			System.out.println(" Exception occured in testQueryQname : " + e);
		}

		finally {

			if (pw != null) {
				pw.close();
			}
			// Setting hsqldb.database.path to null.
			InstanceHolders.getInstance().setProperty("hsqldb.database.path", null);
		}

	}

	
	@Test
	public void testTablesWithoutJoin() throws Exception {

		PipedReader pr = new PipedReader();
		PipedWriter pw = new PipedWriter(pr);

		try {
			// System.out.println("Array of start and end time; without join....");
			ClassLoader loader = this.getClass().getClassLoader();
			String helioDbPath = loader.getResource("test.txt").getFile();
			helioDbPath = helioDbPath.replace("target/classes/test.txt", "") + "src/main" + "/webapp/WEB-INF";
			// System.out.println(" : helio db file path  : "+helioDbPath);
			InstanceHolders.getInstance().setProperty("hsqldb.database.path", helioDbPath);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			//
			CommonCriteriaTO comCriteriaTO = new CommonCriteriaTO();
			String[] arrStartTime = new String[2];
			String[] arrEndTime = new String[2];
			// Start time and End time.
			arrStartTime[0] = "1890-10-20T20:30:56";
			arrEndTime[0] = "2009-10-20T20:30:56";
			// Start time and End time
			arrStartTime[1] = "1990-10-20T20:30:56";
			arrEndTime[1] = "2009-10-20T20:30:56";
			// Setting data
			comCriteriaTO.setStartDateTimeList(arrStartTime);
			comCriteriaTO.setEndDateTimeList(arrEndTime);
			// Joining 2 tables
			comCriteriaTO.setJoin("no");
			String[] arr = new String[2];
			arr[0] = "helio";
			arr[1] = "helio";
			comCriteriaTO.setListTableName(arr);
			comCriteriaTO.setStatus("WebService");
			comCriteriaTO.setPrintWriter(pw);
			// System.out.println("Creating a VOTable ....");
			new QueryThreadAnalizer(comCriteriaTO).start();
			Thread.sleep(20000);
			// Print reader
			// System.out.println("Printing VOTable ....");
			printPrintReader(pr);

		} catch (Exception e) {
			System.out.println(" Exception occured in testQueryQname : " + e);
		}

		finally {

			if (pw != null) {
				pw.close();
			}
			// Setting hsqldb.database.path to null.
			InstanceHolders.getInstance().setProperty("hsqldb.database.path", null);
		}

	}

	
	@Test
	public void testJoinTablesWhere() throws Exception {

		PipedReader pr = new PipedReader();
		PipedWriter pw = new PipedWriter(pr);

		try {
			// System.out.println("Joining 2 tables with where clause....");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			//
			CommonCriteriaTO comCriteriaTO = new CommonCriteriaTO();
			String[] arrStartTime = new String[2];
			String[] arrEndTime = new String[2];
			// Start time and End time.
			arrStartTime[0] = "1890-10-20T20:30:56";
			arrEndTime[0] = "2009-10-20T20:30:56";
			// Start time and End time
			arrStartTime[1] = "1990-10-20T20:30:56";
			arrEndTime[1] = "2009-10-20T20:30:56";
			// Setting data
			comCriteriaTO.setStartDateTimeList(arrStartTime);
			comCriteriaTO.setEndDateTimeList(arrEndTime);
			// Joining 2 tables
			comCriteriaTO.setJoin("yes");
			comCriteriaTO.setWhereClause("helio.URL,*HelioICS*");
			String[] arr = new String[2];
			arr[0] = "helio";
			arr[1] = "helio";
			comCriteriaTO.setListTableName(arr);
			comCriteriaTO.setStatus("WebService");
			comCriteriaTO.setPrintWriter(pw);
			// System.out.println("Creating a VOTable ....");
			new QueryThreadAnalizer(comCriteriaTO).start();
			Thread.sleep(20000);
			// Print reader
			// System.out.println("Printing VOTable ....");
			printPrintReader(pr);

		} catch (Exception e) {
			System.out.println(" Exception occured in testQueryQname : " + e);
		}

		finally {

			if (pw != null) {
				pw.close();
			}
			// Setting hsqldb.database.path to null.
			InstanceHolders.getInstance().setProperty("hsqldb.database.path", null);
		}

	}

	
	@Test
	public void testTablesWithoutJoinAndWithWhereClause() throws Exception {

		PipedReader pr = new PipedReader();
		PipedWriter pw = new PipedWriter(pr);

		try {
			// System.out.println("Joining 2 tables withpit where cluase....");

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			//
			CommonCriteriaTO comCriteriaTO = new CommonCriteriaTO();
			String[] arrStartTime = new String[2];
			String[] arrEndTime = new String[2];
			// Start time and End time.
			arrStartTime[0] = "1890-10-20T20:30:56";
			arrEndTime[0] = "2009-10-20T20:30:56";
			// Start time and End time
			arrStartTime[1] = "1990-10-20T20:30:56";
			arrEndTime[1] = "2009-10-20T20:30:56";
			// Setting data
			comCriteriaTO.setStartDateTimeList(arrStartTime);
			comCriteriaTO.setEndDateTimeList(arrEndTime);
			// Joining 2 tables
			comCriteriaTO.setJoin("no");
			comCriteriaTO.setWhereClause("helio.URL,*HelioICS*");
			String[] arr = new String[2];
			arr[0] = "helio";
			arr[1] = "helio";
			comCriteriaTO.setListTableName(arr);
			comCriteriaTO.setStatus("WebService");
			comCriteriaTO.setPrintWriter(pw);
			// System.out.println("Creating a VOTable ....");
			new QueryThreadAnalizer(comCriteriaTO).start();
			Thread.sleep(20000);
			// Print reader
			// System.out.println("Printing VOTable ....");
			printPrintReader(pr);

		} catch (Exception e) {
			System.out.println(" Exception occured in testQueryQname : " + e);
		}

		finally {

			if (pw != null) {
				pw.close();
			}
			// Setting hsqldb.database.path to null.
			InstanceHolders.getInstance().setProperty("hsqldb.database.path", null);
		}

	}

	private String runJob(CommonCriteriaTO comCriteriaTO, PipedReader pr) throws Exception {
		QueryThreadAnalizer queryThreadAnalyzer = new QueryThreadAnalizer(comCriteriaTO);
		Future<?> job = executor.submit(queryThreadAnalyzer);
		// try to get result from db for 2 seconds
		job.get(20, TimeUnit.MINUTES);
		assertTrue(job.isDone());
		// Print reader
		// System.out.println("Printing VOTable ....");

		String result = printPrintReader(pr);
		assertNotNull(result);
		// TODO: test if result is valid
		return result;
	}

	/**
	 * Convert read data to string.
	 * 
	 * @param reader
	 *            the reader to read from
	 * @throws IOException
	 *             if anything goes wrong.
	 */
	private String printPrintReader(PipedReader reader) throws IOException {
		StringWriter writer = new StringWriter();
		try {
			while (reader.ready()) {
				writer.write(reader.read());
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return writer.toString();
	}

	/**
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	private String printPrintReaderStringRes(PipedReader reader) throws IOException {
		try {
			StringBuffer sb = new StringBuffer();
			while (reader.ready()) {
				Thread.sleep(10);
				sb.append((char) reader.read());
			}

			return sb.toString();
		} catch (Exception e) {
			System.out.println(" Exception in printPrintReader : " + e);
			e.printStackTrace();
		}

		finally {

			if (reader != null) {
				reader.close();
			}
		}
		return "";
	}
}
