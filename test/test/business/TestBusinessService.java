package test.business;

import static test.util.Constants.DEFAULT_DB_LOCATION_SERVER;
import static test.util.Constants.DEFAULT_DB_LOCATION_STANDALONE;
import static test.util.Constants.DEFAULT_SERVER_IPADDRESS;
import static test.util.Constants.DEFAULT_PORT_NUMBER;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import suncertify.business.AlreadyBookedException;
import suncertify.business.ContractorNotFoundException;
import suncertify.business.ContractorService;
import suncertify.business.rmi.RMIClient;
import suncertify.business.rmi.RMIServer;
import suncertify.db.DBMainExtended;
import suncertify.db.DatabaseException;
import suncertify.db.DBFactory;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.Contractor;
import suncertify.util.ContractorBuilder;
import suncertify.util.Utils;

public class TestBusinessService {

	private static DBMainExtended data;

	public TestBusinessService(DBMainExtended data) throws DatabaseException {
		this.data = DBFactory.getDatabase(DEFAULT_DB_LOCATION_STANDALONE);
	}

	public static void main(final String[] args) throws RemoteException, DatabaseException {
		// start your RMI-server
		ContractorService service = new RMIServer(
				DBFactory.getDatabase(DEFAULT_DB_LOCATION_SERVER));
		((RMIServer) service).startServer(DEFAULT_PORT_NUMBER);
		new TestBusinessService(data).startTests();
	}

	public void startTests() {
		List<Thread> threads = new ArrayList<Thread>();
		try {
			// create book-threads
			for (int i = 0; i < 35; i++) {
				threads.add(new Thread(new BookThread(i), String.valueOf(i)));
			}
			// random order
			Collections.shuffle(threads);
			// start threads
			for (Thread thread : threads) {
				thread.start();
			}
			// sleep until all threads are finished
			for (Thread thread : threads) {
				thread.join();
			}
		} catch (final Exception e) {
			System.out.println(e);
		}
		System.out.println("Done.");
	}

	private class BookThread implements Runnable {
		private int id;
		private String customer;
		private boolean endRun;
		private boolean noContractor;

		public BookThread(final int id) {
			this.id = id;
			this.customer = String.format("%1$08d", id);
			this.endRun = false;
			this.noContractor = false;
		}

		@Override
		public void run() {
			int recNo = 0;
			while (!endRun) {
				try {
					try {
						Contractor contractor = ContractorBuilder.build(data.read(recNo));
						contractor.setCustomerId("54120584");
						ContractorService service = new RMIClient(DEFAULT_SERVER_IPADDRESS, DEFAULT_PORT_NUMBER);
						service.book(contractor);
						endRun = true;
					} catch (RecordNotFoundException e) {
						throw new ContractorNotFoundException();
					} catch (NotBoundException e) {
						e.printStackTrace();
					}
				} catch (RemoteException e) {
					System.out.println(e);
				} catch (ContractorNotFoundException e) {
					endRun = true;
					noContractor = true;
				} catch (AlreadyBookedException e) {
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

}