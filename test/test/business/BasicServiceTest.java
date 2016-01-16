package test.business;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static test.util.Constants.DB_FILE_NAME;

import suncertify.business.AlreadyBookedException;
import suncertify.business.BasicContractorService;
import suncertify.business.ContractorNotFoundException;
import suncertify.business.ContractorService;
import suncertify.db.DBMainExtended;
import suncertify.db.Data;
import suncertify.db.DatabaseAccessException;
import suncertify.db.DatabaseFactory;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPk;
import suncertify.util.ContractorBuilder;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BasicServiceTest {

  private DBMainExtended data;
  private ContractorService services;

  private final String[] firstContractorValues_Booked = new String[] { "Dogs With Tools",
      "Smallville", "Roofing", "7", "$35.00", "12345678" };

  private final String[] firstContractorSearchCriteria = new String[] { "Dogs With Tools",
      "Smallville" };

  private final Contractor firstContractor_Booked = ContractorBuilder
      .build(firstContractorValues_Booked);

  private final ContractorPk NO_SEARCH_CRITERIA = new ContractorPk("", "");
  private final ContractorPk FIRST_CONTRACTOR_SEARCH_CRITERIA = new ContractorPk("Dogs With Tools",
      "Smallville");
  private final ContractorPk NAME_SEARCH_CRITERIA = new ContractorPk("Dogs With Tools", "");
  private final ContractorPk LOCATION_SEARCH_CRITERIA = new ContractorPk("", "Smallville");
  private final ContractorPk NEW_CONTRACTOR_SEARCH_CRITERIA = new ContractorPk("Smack my Itch up",
      "Gotham");

  @Before
  public void setup() throws DatabaseAccessException {
    data = DatabaseFactory.getDatabase(DB_FILE_NAME);
    services = new BasicContractorService(data);
  }

  @After
  public void teardown() throws IOException {
    ((Data) data).clear();
    ((Data) data).loadCache();
  }

  @Test
  public void testBook_availableContractor() throws ContractorNotFoundException, RemoteException,
      IllegalArgumentException, AlreadyBookedException, RecordNotFoundException {
    services.book(firstContractor_Booked);
    assertEquals(28, ((Data) data).getValidEntryStream().count());
    assertEquals(0, data.find(firstContractorSearchCriteria)[0]);
    final String[] actual = data.read(0);
    assertArrayEquals(firstContractorValues_Booked, actual);
  }

  @Test(expected = AlreadyBookedException.class)
  public void testBook_bookedContractor() throws ContractorNotFoundException,
      DatabaseAccessException, RemoteException, IllegalArgumentException, AlreadyBookedException {
    services.book(firstContractor_Booked);
    services.book(firstContractor_Booked);
  }

  @Test(expected = ContractorNotFoundException.class)
  public void testBook_DeletedContractor() throws ContractorNotFoundException,
      DatabaseAccessException, RemoteException, IllegalArgumentException, AlreadyBookedException {
    data.delete(0);
    assertEquals(27, ((Data) data).getValidEntryStream().count());
    services.book(firstContractor_Booked);
  }

  @Test
  public void testFind_AllContractors() throws ContractorNotFoundException, RemoteException {
    final Map<Integer, Contractor> results = services.find(NO_SEARCH_CRITERIA);
    assertEquals(28, results.size());
  }

  @Test
  public void testFind_AllContractors_WithDeletedRecords() throws DatabaseAccessException,
      RemoteException, IllegalArgumentException, ContractorNotFoundException {
    data.delete(0);
    data.delete(12);
    data.delete(21);
    final Map<Integer, Contractor> results = services.find(NO_SEARCH_CRITERIA);
    assertEquals(25, ((Data) data).getValidEntryStream().count());
    assertEquals(25, results.size());
  }

  @Test
  public void testFind_MultipleContractors_LocationSearch()
      throws RemoteException, IllegalArgumentException, ContractorNotFoundException {
    final Map<Integer, Contractor> results = services.find(LOCATION_SEARCH_CRITERIA);
    assertEquals(2, results.size());
  }

  @Test
  public void testFind_MultipleContractors_NameSearch()
      throws ContractorNotFoundException, RemoteException {
    final Map<Integer, Contractor> results = services.find(NAME_SEARCH_CRITERIA);
    assertEquals(6, results.size());
  }

  @Test
  public void testFind_SingleContractor() throws RemoteException, ContractorNotFoundException {
    final Map<Integer, Contractor> results = services.find(FIRST_CONTRACTOR_SEARCH_CRITERIA);
    assertEquals(1, results.size());
  }

  @Test(expected = ContractorNotFoundException.class)
  public void testFind_SingleContractor_WithDeletedRecords() throws DatabaseAccessException,
      RemoteException, IllegalArgumentException, ContractorNotFoundException {
    data.delete(0);
    services.find(FIRST_CONTRACTOR_SEARCH_CRITERIA);
  }

  @Test(expected = ContractorNotFoundException.class)
  public void testFind_UnknownContractor()
      throws RemoteException, IllegalArgumentException, ContractorNotFoundException {
    services.find(NEW_CONTRACTOR_SEARCH_CRITERIA);
  }

}
