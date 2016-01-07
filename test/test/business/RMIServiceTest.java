package test.business;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static test.util.Constants.DB_FILE_NAME;
import static test.util.Constants.DEFAULT_SERVER_IPADDRESS;
import static test.util.Constants.DEFAULT_PORT_NUMBER;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import suncertify.business.AlreadyBookedException;
import suncertify.business.ContractorNotFoundException;
import suncertify.business.ContractorService;
import suncertify.business.ServiceException;
import suncertify.business.rmi.RMIClient;
import suncertify.business.rmi.RMIServer;
import suncertify.business.rmi.RMIService;
import suncertify.db.DBMainExtended;
import suncertify.db.Data;
import suncertify.db.DatabaseException;
import suncertify.db.DatabaseManagerFactory;
import suncertify.dto.Contractor;
import suncertify.util.Converter;

public class RMIServiceTest {

	private static DBMainExtended data;
	private static RMIService server;
	private static ContractorService service;

	private final String[] firstContractorValues = new String[] { "Dogs With Tools", "Smallville", "Roofing", "7",
			"$35.00", "" };
	private final String[] firstContractorValues_Booked = new String[] { "Dogs With Tools", "Smallville", "Roofing",
			"7", "$35.00", "12345678" };
	private final String[] newContractorValues = new String[] { "Smack my Itch up", "Gotham",
			"Getting It Done,Horsing It", "12", "$79.00", "87654321" };

	private final Contractor firstContractor = Converter.stringArrayToContractor(firstContractorValues);
	private final Contractor firstContractor_Booked = Converter.stringArrayToContractor(firstContractorValues_Booked);
	private final Contractor newContractor = Converter.stringArrayToContractor(newContractorValues);

	private final String[] NO_SEARCH_CRITERIA = new String[] { "", "" };
	private final String[] FIRST_CONTRACTOR_SEARCH_CRITERIA = new String[] { "Dogs With Tools", "Smallville" };
	private final String[] NAME_SEARCH_CRITERIA = new String[] { "Dogs With Tools", "" };
	private final String[] LOCATION_SEARCH_CRITERIA = new String[] { "", "Smallville" };
	private final String[] NEW_CONTRACTOR_SEARCH_CRITERIA = new String[] { "Smack my Itch up", "Gotham" };

	@BeforeClass
	public static void setup() throws DatabaseException, RemoteException, NotBoundException, ServiceException {
		data = DatabaseManagerFactory.getDatabaseManager(DB_FILE_NAME);
		server = new RMIServer(data);
		server.startServer(DEFAULT_PORT_NUMBER);
		service = new RMIClient(DEFAULT_SERVER_IPADDRESS, DEFAULT_PORT_NUMBER);
	}

	@After
	public void teardown() throws DatabaseException {
		((Data) data).clear();
		((Data) data).load();
	}

	@Test
	public void testBook_availableContractor() throws ServiceException, DatabaseException, RemoteException {
		service.book(firstContractor_Booked);
		assertEquals(28, ((Data) data).getTotalNumberOfRecords());
		// assertEquals(28, data.getAllValidRecords().size());
		assertEquals(0, data.find(firstContractorValues_Booked)[0]);
		assertArrayEquals(firstContractorValues_Booked, data.read(0));
	}

	@Test(expected = AlreadyBookedException.class)
	public void testBook_bookedContractor() throws ServiceException, DatabaseException, RemoteException {
		service.book(firstContractor_Booked);
		service.book(firstContractor_Booked);
	}

	@Test(expected = ContractorNotFoundException.class)
	public void testBook_DeletedContractor() throws ServiceException, DatabaseException, RemoteException {
		data.delete(0);
		assertEquals(28, ((Data) data).getTotalNumberOfRecords());
		// assertEquals(27, data.getAllValidRecords().size());
		service.book(firstContractor_Booked);
	}

	@Test
	public void testFind_AllContractors() throws ServiceException, RemoteException {
		Map<Integer, Contractor> results = service.find(Converter.stringArrayToContractorPK(NO_SEARCH_CRITERIA));
		assertEquals(28, results.size());
	}

	@Test
	public void testFind_SingleContractor() throws ServiceException, RemoteException {
		Map<Integer, Contractor> results = service
				.find(Converter.stringArrayToContractorPK(FIRST_CONTRACTOR_SEARCH_CRITERIA));
		assertEquals(1, results.size());
	}

	@Test
	public void testFind_AllContractors_WithDeletedRecords()
			throws ServiceException, DatabaseException, RemoteException {
		data.delete(0);
		data.delete(12);
		data.delete(21);
		Map<Integer, Contractor> results = service.find(Converter.stringArrayToContractorPK(NO_SEARCH_CRITERIA));
		assertEquals(28, ((Data) data).getTotalNumberOfRecords());
		assertEquals(25, results.size());
	}

	@Test(expected = ContractorNotFoundException.class)
	public void testFind_SingleContractor_WithDeletedRecords()
			throws DatabaseException, ServiceException, RemoteException {
		data.delete(0);
		service.find(Converter.stringArrayToContractorPK(FIRST_CONTRACTOR_SEARCH_CRITERIA));
	}

	@Test
	public void testFind_MultipleContractors_NameSearch() throws ServiceException, RemoteException {
		Map<Integer, Contractor> results = service.find(Converter.stringArrayToContractorPK(NAME_SEARCH_CRITERIA));
		assertEquals(6, results.size());
	}

	@Test
	public void testFind_MultipleContractors_LocationSearch() throws ServiceException, RemoteException {
		Map<Integer, Contractor> results = service.find(Converter.stringArrayToContractorPK(LOCATION_SEARCH_CRITERIA));
		assertEquals(2, results.size());
	}

	@Test(expected = ContractorNotFoundException.class)
	public void testFind_UnknownContractor() throws ServiceException, RemoteException {
		service.find(Converter.stringArrayToContractorPK(NEW_CONTRACTOR_SEARCH_CRITERIA));
	}

}
