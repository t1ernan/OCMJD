package suncertify.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static suncertify.test.util.Constants.DB_FILE_NAME;
import static suncertify.test.util.Constants.DEFAULT_PORT_NUMBER;
import static suncertify.test.util.Constants.DEFAULT_SERVER_IPADDRESS;

import suncertify.business.AlreadyBookedException;
import suncertify.business.BasicContractorService;
import suncertify.business.ContractorNotFoundException;
import suncertify.business.ContractorService;
import suncertify.business.rmi.RmiClient;
import suncertify.business.rmi.RmiServer;
import suncertify.business.rmi.RmiService;
import suncertify.db.DBMainExtended;
import suncertify.db.Data;
import suncertify.db.DatabaseAccessException;
import suncertify.db.DatabaseFactory;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPk;
import suncertify.util.ContractorConverter;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RMIServiceTest {

  private static Data data = Data.getInstance();
  private static RmiService server;
  private static ContractorService service;

  private final String[] firstContractorValues_Booked = new String[] { "Dogs With Tools",
      "Smallville", "Roofing", "7", "$35.00", "12345678" };

  private final String[] firstContractorSearchCriteria = new String[] { "Dogs With Tools",
      "Smallville" };

  private final Contractor firstContractor_Booked = ContractorConverter
      .toContractor(firstContractorValues_Booked);

  private final ContractorPk NO_SEARCH_CRITERIA = new ContractorPk("", "");
  private final ContractorPk FIRST_CONTRACTOR_SEARCH_CRITERIA = new ContractorPk("Dogs With Tools",
      "Smallville");
  private final ContractorPk NAME_SEARCH_CRITERIA = new ContractorPk("Dogs With Tools", "");
  private final ContractorPk LOCATION_SEARCH_CRITERIA = new ContractorPk("", "Smallville");
  private final ContractorPk NEW_CONTRACTOR_SEARCH_CRITERIA = new ContractorPk("Smack my Itch up",
      "Gotham");

  @BeforeClass
  public static void steartUpServer() throws IllegalArgumentException, DatabaseAccessException, RemoteException{
    data.initialize(DB_FILE_NAME);
    server = new RmiServer(data);
    server.startServer(DEFAULT_PORT_NUMBER);
  }
  
  @Before
  public void setup() throws DatabaseAccessException, RemoteException {
    data.initialize(DB_FILE_NAME);
    service = new RmiClient(DEFAULT_SERVER_IPADDRESS, DEFAULT_PORT_NUMBER);
  }
  
  @After
  public void teardown() throws IOException, IllegalArgumentException, DatabaseAccessException {
    data.initialize(DB_FILE_NAME);
  }

  private long getValidRecordCount() {
    return data.getCache().values().stream().filter(x -> x != null).count();
  }

  @Test
  public void testBook_availableContractor()
      throws DatabaseAccessException, RemoteException, IllegalArgumentException,
      ContractorNotFoundException, AlreadyBookedException, RecordNotFoundException {
    service.book(firstContractor_Booked);
    assertEquals(28, getValidRecordCount());
    assertEquals(0, data.find(firstContractorSearchCriteria)[0]);
    assertArrayEquals(firstContractorValues_Booked, data.read(0));
  }

  @Test(expected = AlreadyBookedException.class)
  public void testBook_bookedContractor() throws DatabaseAccessException, RemoteException,
      IllegalArgumentException, ContractorNotFoundException, AlreadyBookedException {
    service.book(firstContractor_Booked);
    service.book(firstContractor_Booked);
  }

  @Test(expected = ContractorNotFoundException.class)
  public void testBook_DeletedContractor() throws DatabaseAccessException, RemoteException,
      IllegalArgumentException, ContractorNotFoundException, AlreadyBookedException {
    data.delete(0);
    assertEquals(27, getValidRecordCount());
    service.book(firstContractor_Booked);
  }

  @Test
  public void testFind_AllContractors()
      throws RemoteException, IllegalArgumentException, ContractorNotFoundException {
    final Map<Integer, Contractor> results = service.find(NO_SEARCH_CRITERIA);
    assertEquals(28, results.size());
  }

  @Test
  public void testFind_AllContractors_WithDeletedRecords() throws DatabaseAccessException,
      RemoteException, IllegalArgumentException, ContractorNotFoundException {
    data.delete(0);
    data.delete(12);
    data.delete(21);
    final Map<Integer, Contractor> results = service.find(NO_SEARCH_CRITERIA);
    assertEquals(25, getValidRecordCount());
    assertEquals(25, results.size());
  }

  @Test
  public void testFind_MultipleContractors_LocationSearch()
      throws RemoteException, IllegalArgumentException, ContractorNotFoundException {
    final Map<Integer, Contractor> results = service.find(LOCATION_SEARCH_CRITERIA);
    assertEquals(2, results.size());
  }

  @Test
  public void testFind_MultipleContractors_NameSearch()
      throws RemoteException, IllegalArgumentException, ContractorNotFoundException {
    final Map<Integer, Contractor> results = service.find(NAME_SEARCH_CRITERIA);
    assertEquals(6, results.size());
  }

  @Test
  public void testFind_SingleContractor()
      throws RemoteException, IllegalArgumentException, ContractorNotFoundException {
    final Map<Integer, Contractor> results = service.find(FIRST_CONTRACTOR_SEARCH_CRITERIA);
    assertEquals(1, results.size());
  }

  @Test(expected = ContractorNotFoundException.class)
  public void testFind_SingleContractor_WithDeletedRecords() throws DatabaseAccessException,
      RemoteException, IllegalArgumentException, ContractorNotFoundException {
    data.delete(0);
    service.find(FIRST_CONTRACTOR_SEARCH_CRITERIA);
  }

  @Test(expected = ContractorNotFoundException.class)
  public void testFind_UnknownContractor()
      throws RemoteException, IllegalArgumentException, ContractorNotFoundException {
    service.find(NEW_CONTRACTOR_SEARCH_CRITERIA);
  }

}
