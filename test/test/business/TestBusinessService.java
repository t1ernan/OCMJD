package test.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static suncertify.util.Constants.*;

import suncertify.business.AlreadyBookedException;
import suncertify.business.BasicServiceImpl;
import suncertify.business.ContractorNotFoundException;
import suncertify.business.ContractorServices;
import suncertify.business.ServicesException;
import suncertify.business.rmi.RMIServer;
import suncertify.business.rmi.RMIServices;
import suncertify.db.DAOFactory;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;

public class TestBusinessService {

	public static void main(final String[] args) throws RemoteException, ServicesException {
		// start your RMI-server
//		RMIServices service = new RMIServer(DAOFactory.getDbManager(DB_FILE_PATH));
//		service.startServer(RMI_PORT);
		new TestBusinessService().startTests();
	}

	public void startTests() {
		List<Thread> threads = new ArrayList<Thread>();
		try {
			// create book-threads
			for (int i = 0; i < 35; i++) {
				threads.add(new Thread(new BookThread(i * 100), String.valueOf(i * 100)));
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
		private boolean noRoom;

		public BookThread(final int id) {
			this.id = id;
			this.customer = String.format("%1$08d", id);
			this.endRun = false;
			this.noRoom = false;
		}

		@Override
		public void run() {
			int recNo = 0;
			while (!endRun) {
				try {
					final ContractorPK primaryKey = new ContractorPK();
					primaryKey.setName("Palace");
					primaryKey.setLocation("Smallville");
					final Contractor contractor = new Contractor();
					contractor.setPrimaryKey(primaryKey);
					contractor.setSize(2);
					contractor.setSpecialities("Getting stuff done, horsing it");
					contractor.setRate("$150.00");
					contractor.setOwner("54120584");
					BasicServiceImpl service = new BasicServiceImpl(
							DAOFactory.getDbManager(DEFAULT_DB_LOCATION_STANDALONE));
					service.book(contractor);
					endRun = true;
				} catch (RemoteException e) {
					System.out.println(e);
				} catch (ContractorNotFoundException e) {
					endRun = true;
					noRoom = true;
				} catch (AlreadyBookedException e) {
					// expected to occur
				}
				if (!endRun) {
					recNo++;
				}
			}
			if (noRoom) {
				System.out.println(id + " booked no room");
			} else {
				System.out.println(id + " booked room " + recNo);
			}
		}
	}

}