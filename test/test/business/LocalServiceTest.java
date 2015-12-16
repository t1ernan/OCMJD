package test.business;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static suncertify.util.Constants.DB_FILE_PATH;

import java.rmi.RemoteException;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import suncertify.business.ContractorNotFoundException;
import suncertify.business.ContractorServices;
import suncertify.business.DuplicateContractorException;
import suncertify.business.ServicesException;
import suncertify.db.DAOFactory;
import suncertify.db.Data;
import suncertify.db.DatabaseException;
import suncertify.domain.Contractor;
import suncertify.local.LocalServiceImpl;
import suncertify.util.ContractorConverter;
import suncertify.util.ContractorPKConverter;

public class LocalServiceTest {

	private Data data;
	private ContractorServices services;

	private final String[] firstContractorValues = new String[] { "Dogs With Tools", "Smallville", "Roofing", "7",
			"$35.00", "" };
	private final String[] newContractorValues = new String[] { "Smack my Itch up", "Gotham",
			"Getting It Done,Horsing It", "12", "$79.00", "87654321" };

	private final Contractor firstContractor = ContractorConverter.toContractor(firstContractorValues);
	private final Contractor newContractor = ContractorConverter.toContractor(newContractorValues);

	private final String[] NO_SEARCH_CRITERIA = new String[] { "", "" };
	private final String[] FIRST_CONTRACTOR_SEARCH_CRITERIA = new String[] { "Dogs With Tools", "Smallville" };
	private final String[] NAME_SEARCH_CRITERIA = new String[] { "Dogs With Tools", "" };
	private final String[] LOCATION_SEARCH_CRITERIA = new String[] { "", "Smallville" };
	private final String[] NEW_CONTRACTOR_SEARCH_CRITERIA = new String[] { "Smack my Itch up", "Gotham" };

	@Before
	public void setup() throws DatabaseException {
		data = DAOFactory.getDbManager(DB_FILE_PATH);
		services = new LocalServiceImpl(data);
	}

	@After
	public void teardown() throws DatabaseException {
		data.clear();
		data.load();
	}

	@Test
	public void testCreateBooking_newContractor() throws ServicesException, DatabaseException, RemoteException {
		services.createBooking(newContractor);
		assertEquals(29, data.getTotalNumberOfRecords());
		assertEquals(29, data.getAllValidRecords().size());
		assertEquals(28, data.find(newContractorValues)[0]);
		assertArrayEquals(newContractorValues, data.read(28));
	}

	@Test(expected = DuplicateContractorException.class)
	public void testCreateBooking_newContractor_duplicate()
			throws ServicesException, DatabaseException, RemoteException {
		services.createBooking(newContractor);
		services.createBooking(newContractor);
	}

	@Test(expected = DuplicateContractorException.class)
	public void testCreateBooking_existingContractor() throws ServicesException, RemoteException {
		services.createBooking(firstContractor);
	}

	@Test
	public void testCreateBooking_existingDeletedContractor()
			throws ServicesException, DatabaseException, RemoteException {
		data.delete(0);
		assertEquals(28, data.getTotalNumberOfRecords());
		assertEquals(27, data.getAllValidRecords().size());
		services.createBooking(firstContractor);
		assertEquals(28, data.getTotalNumberOfRecords());
		assertEquals(28, data.getAllValidRecords().size());
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
