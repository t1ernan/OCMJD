package test.db;

import static suncertify.util.Constants.DEFAULT_DB_LOCATION_STANDALONE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import suncertify.db.DBMain;
import suncertify.db.DBMainFactory;
import suncertify.db.DatabaseException;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;
import suncertify.util.ContractorConverter;

public class DataConcurrencyTest {

	private static DBMain data;

	public DataConcurrencyTest(DBMain data) throws DatabaseException {
		this.data = DBMainFactory.getDatabase(DEFAULT_DB_LOCATION_STANDALONE);
	}

	/*
	 * If any preparation has to be done before using the Data class, it can be
	 * done in a static block; in this case, before using the Data class, the
	 * loadDbRecords method has to be called prior to any other operation, so
	 * the records in the physical .db file can be placed in the Map that keeps
	 * them in memory; I also have a method called persistDbRecords, which
	 * writes each record back to the physical .db file, but this test aims only
	 * to test the functionalities without altering the database, so this method
	 * is never called anywhere
	 */
	static {

	}

	public static void main(String[] args) throws DatabaseException {
		new DataConcurrencyTest(data).startTests();
	}

	public void startTests() {
		try {

			/*
			 * Practically, it is not necessary to execute this loop more than 1
			 * time, but if you want, you can increase the controller variable,
			 * so it is executed as many times as you want
			 */
			List<Thread> threads = new ArrayList<Thread>();
			for (int i = 0; i < 10000; i++) {
				threads.add(new UpdatingRandomRecordThread());
				threads.add(new UpdatingRecord1Thread());
				threads.add(new CreatingRecordThread());
				threads.add(new DeletingRecord1Thread());
				threads.add(new DeletingRecord28Thread());
				threads.add(new FindingRecordsThread());
			}

			Collections.shuffle(threads);
			// start threads
			for (Thread thread : threads) {
				thread.start();
			}
			// sleep until all threads are finished
			for (Thread thread : threads) {
				thread.join();
			}
			System.out.println("//////////////////////////////////////////////////////////////////////////////////");
			System.out.println("//////////////////////////////////////////////////////////////////////////////////");
			System.out.println("//////////////////////////////////////////////////////////////////////////////////");
			System.out.println("FINISHED");
			System.out.println("//////////////////////////////////////////////////////////////////////////////////");
			System.out.println("//////////////////////////////////////////////////////////////////////////////////");
			System.out.println("//////////////////////////////////////////////////////////////////////////////////");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private class UpdatingRandomRecordThread extends Thread {

		@Override
		@SuppressWarnings("deprecation")
		public void run() {
			final ContractorPK primaryKey = new ContractorPK();
			primaryKey.setName("Palace");
			primaryKey.setLocation("Smallville");
			final Contractor contractor = new Contractor();
			contractor.setPrimaryKey(primaryKey);
			contractor.setSize(2);
			contractor.setSpecialities("Getting stuff done, horsing it");
			contractor.setRate("$150.00");
			contractor.setOwner("54120584");

			final int recNo = (int) (Math.random() * 50);
			try {
				System.out.println(Thread.currentThread().getId() + " trying to lock record #" + recNo
						+ " on UpdatingRandomRecordThread");

				/*
				 * The generated record number may not exist in the database, so
				 * a RecordNotFoundException must be thrown by the lock method.
				 * Since the database records are in a cache, it is not
				 * necessary to put the unlock instruction in a finally block,
				 * because an exception can only occur when calling the lock
				 * method (not when calling the update/delete methods),
				 * therefore it is not necessary to call the unlock method in a
				 * finally block, but you can customize this code according to
				 * your reality
				 */
				data.lock(recNo);
				System.out.println(Thread.currentThread().getId() + " trying to update record #" + recNo
						+ " on UpdatingRandomRecordThread");

				/*
				 * An exception cannot occur here, otherwise, the unlock
				 * instruction will not be reached, and the record will be
				 * locked forever. In this case, I created a class called
				 * RoomRetriever, which transforms from Room to String array,
				 * and vice-versa, but it could also be done this way:
				 *
				 * data.update(recNo, new String[] {"Palace", "Smallville", "2",
				 * "Y", "$150.00", "2005/07/27", null});
				 */
				data.update(recNo, ContractorConverter.toFieldValues(contractor));
				System.out.println(Thread.currentThread().getId() + " trying to unlock record #" + recNo
						+ " on UpdatingRandomRecordThread");
				data.unlock(recNo);
			} catch (Exception e) {
				System.out.println(Thread.currentThread().getId() + " couldn't update record " + recNo + ": " + e);
			}
		}
	}

	private class UpdatingRecord1Thread extends Thread {

		@Override
		@SuppressWarnings("deprecation")
		public void run() {
			final ContractorPK primaryKey = new ContractorPK();
			primaryKey.setName("Castle");
			primaryKey.setLocation("Digitopolis");
			final Contractor contractor = new Contractor();
			contractor.setPrimaryKey(primaryKey);
			contractor.setSize(2);
			contractor.setSpecialities("Living life, loving music");
			contractor.setRate("$90.00");
			contractor.setOwner("88006644");

			try {
				System.out.println(
						Thread.currentThread().getId() + " trying to lock record #1 on" + " UpdatingRecord1Thread");
				data.lock(1);
				System.out.println(
						Thread.currentThread().getId() + " trying to update record #1 on" + " UpdatingRecord1Thread");
				data.update(1, ContractorConverter.toFieldValues(contractor));
				System.out.println(
						Thread.currentThread().getId() + " trying to unlock record #1 on" + "UpdatingRecord1Thread");

				/*
				 * In order to see the deadlock, this instruction can be
				 * commented, and the other Threads, waiting to update/delete
				 * record #1 will wait forever and the deadlock will occur
				 */
				data.unlock(1);
			} catch (Exception e) {
				System.out.println(Thread.currentThread().getId() + " couldn't update record #1: " + e);
			}
		}
	}

	private class CreatingRecordThread extends Thread {

		@Override
		@SuppressWarnings("deprecation")
		public void run() {
			final ContractorPK primaryKey = new ContractorPK();
			primaryKey.setName("Elephant Inn");
			primaryKey.setLocation("EmeraldCity");
			final Contractor contractor = new Contractor();
			contractor.setPrimaryKey(primaryKey);
			contractor.setSize(6);
			contractor.setSpecialities("hating peanuts, shouting loud");
			contractor.setRate("$120.00");

			try {
				System.out.println(Thread.currentThread().getId() + " trying to create a record");
				data.create(ContractorConverter.toFieldValues(contractor));
			} catch (Exception e) {
				System.out.println(Thread.currentThread().getId() + " couldn't create record: " + e);
			}
		}
	}

	private class DeletingRecord1Thread extends Thread {

		@Override
		public void run() {
			try {
				System.out.println(
						Thread.currentThread().getId() + " trying to lock record #1 on " + "DeletingRecord1Thread");
				data.lock(1);
				System.out.println(
						Thread.currentThread().getId() + " trying to delete record #1 on " + "DeletingRecord1Thread");
				data.delete(1);
				System.out.println(
						Thread.currentThread().getId() + " trying to unlock record #1 on " + "DeletingRecord1Thread");
				data.unlock(1);
			} catch (Exception e) {
				System.out.println(Thread.currentThread().getId() + " couldn't delete record#1: " + e);
			}
		}
	}

	private class DeletingRecord28Thread extends Thread {

		@Override
		public void run() {
			try {
				System.out.println(
						Thread.currentThread().getId() + " trying to lock record #28 on " + "DeletingRecord1Thread");
				data.lock(28);
				System.out.println(
						Thread.currentThread().getId() + " trying to delete record #28 on " + "DeletingRecord1Thread");
				data.delete(28);
				System.out.println(
						Thread.currentThread().getId() + " trying to unlock record #28 on " + "DeletingRecord1Thread");
				data.unlock(28);
			} catch (Exception e) {
				System.out.println(Thread.currentThread().getId() + " couldn't delete record#28: " + e);
			}
		}
	}

	private class FindingRecordsThread extends Thread {

		@Override
		public void run() {
			try {
				System.out.println(Thread.currentThread().getId() + " trying to find records");
				final String[] criteria = { "Palace", "Smallville" };
				final int[] results = data.find(criteria);

				for (int i = 0; i < results.length; i++) {
					System.out.println(results.length + " results found.");
					try {
						final String message = Thread.currentThread().getId() + " going to read record #" + results[i]
								+ " in FindingRecordsThread - still " + ((results.length - 1) - i) + " to go.";
						System.out.println(message);
						final String[] room = data.read(results[i]);
						System.out.println("Booking (FindingRecordsThread): " + room[0]);
						System.out.println("Has next? " + (i < (results.length - 1)));
					} catch (Exception e) {
						/*
						 * In case a record was found during the execution of
						 * the find method, but deleted before the execution of
						 * the read instruction, a RecordNotFoundException will
						 * occur, which would be normal then
						 */
						System.out.println("Exception in " + "FindingRecordsThread - " + e);
					}
				}
				System.out.println("Exiting for loop");
			} catch (Exception e) {
				System.out.println(Thread.currentThread().getId() + " couldn't find record: " + e);
			}
		}
	}
}
