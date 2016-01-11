package test.business;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static test.util.Constants.DB_FILE_NAME;

import java.rmi.RemoteException;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import suncertify.business.AlreadyBookedException;
import suncertify.business.BasicContractorService;
import suncertify.business.ContractorNotFoundException;
import suncertify.business.ContractorService;
import suncertify.business.ServiceException;
import suncertify.db.DBFactory;
import suncertify.db.DBMainExtended;
import suncertify.db.Data;
import suncertify.db.DatabaseException;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;
import suncertify.util.ContractorBuilder;

public class BasicServiceTest {

	private DBMainExtended data;
	private ContractorService services;

	private final String[] firstContractorValues = new String[] { "Dogs With Tools", "Smallville", "Roofing", "7",
			"$35.00", "" };
	private final String[] firstContractorValues_Booked = new String[] { "Dogs With Tools", "Smallville", "Roofing",
			"7", "$35.00", "12345678" };
	private final String[] newContractorValues = new String[] { "Smack my Itch up", "Gotham",
			"Getting It Done,Horsing It", "12", "$79.00", "87654321" };

	private final String[] firstContractorSearchCriteria = new String[] { "Dogs With Tools", "Smallville" };

	private final Contractor firstContractor = ContractorBuilder.build(firstContractorValues);
	private final Contractor firstContractor_Booked = ContractorBuilder.build(firstContractorValues_Booked);
	private final Contractor newContractor = ContractorBuilder.build(newContractorValues);

	private final ContractorPK NO_SEARCH_CRITERIA = new ContractorPK("", "");
	private final ContractorPK FIRST_CONTRACTOR_SEARCH_CRITERIA = new ContractorPK("Dogs With Tools", "Smallville");
	private final ContractorPK NAME_SEARCH_CRITERIA = new ContractorPK("Dogs With Tools", "");
	private final ContractorPK LOCATION_SEARCH_CRITERIA = new ContractorPK("", "Smallville");
	private final ContractorPK NEW_CONTRACTOR_SEARCH_CRITERIA = new ContractorPK("Smack my Itch up", "Gotham");

	@Before
	public void setup() throws DatabaseException {
		data = DBFactory.getDatabase(DB_FILE_NAME);
		services = new BasicContractorService(data);
	}

	@After
	public void teardown() throws DatabaseException {
		((Data) data).clear();
		((Data) data).load();
	}

	@Test
	public void testBook_availableContractor() throws ServiceException, DatabaseException, RemoteException {
		services.book(firstContractor_Booked);
		assertEquals(28, ((Data) data).getTotalNumberOfRecords());
		assertEquals(28, ((Data) data).getAllValidRecords().size());
		assertEquals(0, data.find(firstContractorSearchCriteria)[0]);
		final String[] actual = data.read(0);
		assertArrayEquals(firstContractorValues_Booked, actual);
	}

	@Test(expected = AlreadyBookedException.class)
	public void testBook_bookedContractor() throws ServiceException, DatabaseException, RemoteException {
		services.book(firstContractor_Booked);
		services.book(firstContractor_Booked);
	}

	@Test(expected = ContractorNotFoundException.class)
	public void testBook_DeletedContractor() throws ServiceException, DatabaseException, RemoteException {
		data.delete(0);
		assertEquals(28, ((Data) data).getTotalNumberOfRecords());
		assertEquals(27, ((Data) data).getAllValidRecords().size());
		services.book(firstContractor_Booked);
	}

	@Test
	public void testFind_AllContractors() throws ServiceException, RemoteException {
		Map<Integer, Contractor> results = services.find(NO_SEARCH_CRITERIA);
		assertEquals(28, results.size());
	}

	@Test
	public void testFind_SingleContractor() throws ServiceException, RemoteException {
		Map<Integer, Contractor> results = services.find(FIRST_CONTRACTOR_SEARCH_CRITERIA);
		assertEquals(1, results.size());
	}

	@Test
	public void testFind_AllContractors_WithDeletedRecords()
			throws ServiceException, DatabaseException, RemoteException {
		data.delete(0);
		data.delete(12);
		data.delete(21);
		Map<Integer, Contractor> results = services.find(NO_SEARCH_CRITERIA);
		assertEquals(28, ((Data) data).getTotalNumberOfRecords());
		assertEquals(25, results.size());
	}

	@Test(expected = ContractorNotFoundException.class)
	public void testFind_SingleContractor_WithDeletedRecords()
			throws DatabaseException, ServiceException, RemoteException {
		data.delete(0);
		services.find(FIRST_CONTRACTOR_SEARCH_CRITERIA);
	}

	@Test
	public void testFind_MultipleContractors_NameSearch() throws ServiceException, RemoteException {
		Map<Integer, Contractor> results = services.find(NAME_SEARCH_CRITERIA);
		assertEquals(6, results.size());
	}

	@Test
	public void testFind_MultipleContractors_LocationSearch() throws ServiceException, RemoteException {
		Map<Integer, Contractor> results = services.find(LOCATION_SEARCH_CRITERIA);
		assertEquals(2, results.size());
	}

	@Test(expected = ContractorNotFoundException.class)
	public void testFind_UnknownContractor() throws ServiceException, RemoteException {
		services.find(NEW_CONTRACTOR_SEARCH_CRITERIA);
	}

}
