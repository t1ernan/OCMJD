package test.business;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static suncertify.util.Constants.DB_FILE_PATH;
import static suncertify.util.Constants.RMI_PORT;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import suncertify.business.AlreadyBookedException;
import suncertify.business.ContractorNotFoundException;
import suncertify.business.ContractorServices;
import suncertify.business.ServicesException;
import suncertify.business.rmi.RMIClient;
import suncertify.business.rmi.RMIServer;
import suncertify.business.rmi.RMIServices;
import suncertify.db.DAOFactory;
import suncertify.db.Data;
import suncertify.db.DatabaseException;
import suncertify.domain.Contractor;
import suncertify.util.ContractorConverter;
import suncertify.util.ContractorPKConverter;

public class RMIServiceTest {

	private static Data data;
	private static RMIServices server;
	private static ContractorServices services;

	private final String[] firstContractorValues = new String[] { "Dogs With Tools", "Smallville", "Roofing", "7",
			"$35.00", "" };
	private final String[] firstContractorValues_Booked = new String[] { "Dogs With Tools", "Smallville", "Roofing",
			"7", "$35.00", "12345678" };
	private final String[] newContractorValues = new String[] { "Smack my Itch up", "Gotham",
			"Getting It Done,Horsing It", "12", "$79.00", "87654321" };

	private final Contractor firstContractor = ContractorConverter.toContractor(firstContractorValues);
	private final Contractor firstContractor_Booked = ContractorConverter.toContractor(firstContractorValues_Booked);
	private final Contractor newContractor = ContractorConverter.toContractor(newContractorValues);

	private final String[] NO_SEARCH_CRITERIA = new String[] { "", "" };
	private final String[] FIRST_CONTRACTOR_SEARCH_CRITERIA = new String[] { "Dogs With Tools", "Smallville" };
	private final String[] NAME_SEARCH_CRITERIA = new String[] { "Dogs With Tools", "" };
	private final String[] LOCATION_SEARCH_CRITERIA = new String[] { "", "Smallville" };
	private final String[] NEW_CONTRACTOR_SEARCH_CRITERIA = new String[] { "Smack my Itch up", "Gotham" };

	@BeforeClass
	public static void setup() throws DatabaseException, RemoteException, NotBoundException, ServicesException {
		data = DAOFactory.getDbManager(DB_FILE_PATH);
		server = new RMIServer(data);
		server.startServer(RMI_PORT);
		services = new RMIClient();
	}

	@After
	public void teardown() throws DatabaseException {
		data.clear();
		data.load();
	}

	@Test
	public void testBook_availableContractor() throws ServicesException, DatabaseException, RemoteException {
		services.book(firstContractor_Booked);
		assertEquals(28, data.getTotalNumberOfRecords());
		assertEquals(28, data.getAllValidRecords().size());
		assertEquals(0, data.find(firstContractorValues_Booked)[0]);
		assertArrayEquals(firstContractorValues_Booked, data.read(0));
	}

	@Test(expected = AlreadyBookedException.class)
	public void testBook_bookedContractor() throws ServicesException, DatabaseException, RemoteException {
		services.book(firstContractor_Booked);
		services.book(firstContractor_Booked);
	}

	@Test(expected = ContractorNotFoundException.class)
	public void testBook_DeletedContractor() throws ServicesException, DatabaseException, RemoteException {
		data.delete(0);
		assertEquals(28, data.getTotalNumberOfRecords());
		assertEquals(27, data.getAllValidRecords().size());
		services.book(firstContractor_Booked);
	}

	@Test
	public void testFind_AllContractors() throws ServicesException, RemoteException {
		Map<Integer, Contractor> results = services.find(ContractorPKConverter.toContractorPK(NO_SEARCH_CRITERIA));
		assertEquals(28, results.size());
	}

	@Test
	public void testFind_SingleContractor() throws ServicesException, RemoteException {
		Map<Integer, Contractor> results = services
				.find(ContractorPKConverter.toContractorPK(FIRST_CONTRACTOR_SEARCH_CRITERIA));
		assertEquals(1, results.size());
	}

	@Test
	public void testFind_AllContractors_WithDeletedRecords()
			throws ServicesException, DatabaseException, RemoteException {
		data.delete(0);
		data.delete(12);
		data.delete(21);
		Map<Integer, Contractor> results = services.find(ContractorPKConverter.toContractorPK(NO_SEARCH_CRITERIA));
		assertEquals(28, data.getTotalNumberOfRecords());
		assertEquals(25, results.size());
	}

	@Test(expected = ContractorNotFoundException.class)
	public void testFind_SingleContractor_WithDeletedRecords()
			throws DatabaseException, ServicesException, RemoteException {
		data.delete(0);
		services.find(ContractorPKConverter.toContractorPK(FIRST_CONTRACTOR_SEARCH_CRITERIA));
	}

	@Test
	public void testFind_MultipleContractors_NameSearch() throws ServicesException, RemoteException {
		Map<Integer, Contractor> results = services.find(ContractorPKConverter.toContractorPK(NAME_SEARCH_CRITERIA));
		assertEquals(6, results.size());
	}

	@Test
	public void testFind_MultipleContractors_LocationSearch() throws ServicesException, RemoteException {
		Map<Integer, Contractor> results = services
				.find(ContractorPKConverter.toContractorPK(LOCATION_SEARCH_CRITERIA));
		assertEquals(2, results.size());
	}

	@Test(expected = ContractorNotFoundException.class)
	public void testFind_UnknownContractor() throws ServicesException, RemoteException {
		services.find(ContractorPKConverter.toContractorPK(NEW_CONTRACTOR_SEARCH_CRITERIA));
	}

}
