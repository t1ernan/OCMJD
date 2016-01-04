package test.business;

import static suncertify.util.Constants.DEFAULT_DB_LOCATION_STANDALONE;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import suncertify.business.AlreadyBookedException;
import suncertify.business.BasicServiceImpl;
import suncertify.business.ContractorNotFoundException;
import suncertify.db.DBMain;
import suncertify.db.DBMainFactory;
import suncertify.db.DatabaseException;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.Contractor;
import suncertify.util.ContractorConverter;

public class TestBusinessService {

	private static DBMain data;

	public TestBusinessService(DBMain data) throws DatabaseException {
		this.data = DBMainFactory.getDatabase(DEFAULT_DB_LOCATION_STANDALONE);
	}

	public static void main(final String[] args) throws RemoteException, DatabaseException {
		// start your RMI-server
		// RMIServices service = new
		// RMIServer(DAOFactory.getDbManager(DB_FILE_PATH));
		// service.startServer(RMI_PORT);
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
						Contractor contractor = ContractorConverter.toContractor(data.read(recNo));
						contractor.setOwner("54120584");
						BasicServiceImpl service = new BasicServiceImpl(data);
						service.book(contractor);
						endRun = true;
					} catch (RecordNotFoundException e) {
						throw new ContractorNotFoundException();
					}
				} catch (RemoteException e) {
					System.out.println(e);
				} catch (ContractorNotFoundException e) {
					endRun = true;
					noContractor = true;
				} catch (AlreadyBookedException e) {
					// expected to occur
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