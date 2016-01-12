package test.business;

import static test.util.Constants.DEFAULT_DB_LOCATION_SERVER;
import static test.util.Constants.DEFAULT_DB_LOCATION_STANDALONE;
import static test.util.Constants.DEFAULT_PORT_NUMBER;
import static test.util.Constants.DEFAULT_SERVER_IPADDRESS;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import suncertify.business.AlreadyBookedException;
import suncertify.business.ContractorNotFoundException;
import suncertify.business.ContractorService;
import suncertify.business.rmi.RmiClient;
import suncertify.business.rmi.RmiServer;
import suncertify.db.DatabaseFactory;
import suncertify.db.DBMainExtended;
import suncertify.db.DatabaseException;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.Contractor;
import suncertify.util.ContractorBuilder;

public class TestRMIBusinessService {

	private class BookThread implements Runnable {
		private final int id;
		private final String customer;
		private boolean endRun;
		private boolean noContractor;

		public BookThread(final int id) {
			this.id = id;
			customer = String.format("%1$08d", id);
			endRun = false;
			noContractor = false;
		}

		@Override
		public void run() {
			int recNo = 0;
			while (!endRun) {
				try {
					try {
						final Contractor contractor = ContractorBuilder.build(data.read(recNo));
						contractor.setCustomerId(customer);
						final ContractorService service = new RmiClient(DEFAULT_SERVER_IPADDRESS, DEFAULT_PORT_NUMBER);
						service.book(contractor);
						endRun = true;
					} catch (final RecordNotFoundException e) {
						throw new ContractorNotFoundException();
					}
				} catch (final RemoteException e) {
					System.out.println(e);
				} catch (final ContractorNotFoundException e) {
					endRun = true;
					noContractor = true;
				} catch (final AlreadyBookedException e) {
				}
				if (!endRun) {
					recNo++;
				}
			}
			if (noContractor)

			{
				System.out.println(id + " booked no contractor");
			} else

			{
				System.out.println(id + " booked contractor " + recNo);
			}
		}
	}

	private static DBMainExtended data;

	public static void main(final String[] args) throws RemoteException, DatabaseException {
		// start your RMI-server
		final ContractorService service = new RmiServer(DatabaseFactory.getDatabase(DEFAULT_DB_LOCATION_SERVER));
		((RmiServer) service).startServer(DEFAULT_PORT_NUMBER);
		new TestRMIBusinessService(data).startTests();
	}

	public TestRMIBusinessService(final DBMainExtended data) throws DatabaseException {
		TestRMIBusinessService.data = DatabaseFactory.getDatabase(DEFAULT_DB_LOCATION_STANDALONE);
	}

	public void startTests() {
		final List<Thread> threads = new ArrayList<Thread>();
		try {
			// create book-threads
			for (int i = 0; i < 35; i++) {
				threads.add(new Thread(new BookThread(12345678), String.valueOf(i)));
			}
			// random order
			Collections.shuffle(threads);
			// start threads
			for (final Thread thread : threads) {
				thread.start();
			}
			// sleep until all threads are finished
			for (final Thread thread : threads) {
				thread.join();
			}
		} catch (final Exception e) {
			System.out.println(e);
		}
		System.out.println("Done.");
	}

}