package test.business;

import static test.util.Constants.DEFAULT_DB_LOCATION_SERVER;
import static test.util.Constants.DEFAULT_DB_LOCATION_STANDALONE;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import suncertify.business.AlreadyBookedException;
import suncertify.business.BasicContractorService;
import suncertify.business.ContractorNotFoundException;
import suncertify.business.ContractorService;
import suncertify.db.DatabaseFactory;
import suncertify.db.DBMainExtended;
import suncertify.db.DatabaseAccessException;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.Contractor;
import suncertify.util.ContractorBuilder;

public class TestBusinessService {

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

	private static ContractorService service;

	public static void main(final String[] args) throws RemoteException, DatabaseAccessException {
		// start your RMI-server
		service = new BasicContractorService(DatabaseFactory.getDatabase(DEFAULT_DB_LOCATION_SERVER));
		new TestBusinessService(data).startTests();
	}

	public TestBusinessService(final DBMainExtended data) throws DatabaseAccessException {
		TestBusinessService.data = DatabaseFactory.getDatabase(DEFAULT_DB_LOCATION_STANDALONE);
	}

	public void startTests() {
		final List<Thread> threads = new ArrayList<Thread>();
		try {
			// create book-threads
			for (int i = 0; i < 35; i++) {
				threads.add(new Thread(new BookThread(54120584), String.valueOf(i)));
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