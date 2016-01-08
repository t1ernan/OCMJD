package suncertify.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import suncertify.dto.Contractor;
import suncertify.dto.ContractorPK;

// TODO: Auto-generated Javadoc
/**
 * The Class Converter.
 */
public class Converter {

	/** The Constant CHARACTER_ENCODING. */
	public static final String CHARACTER_ENCODING = "US-ASCII";

	/** The Constant RECORD_FIELDS. */
	private static final int RECORD_FIELDS = 6;

	/** The Constant PRIMARY_KEY_FIELDS. */
	private static final int PRIMARY_KEY_FIELDS = 2;

	public static String[] contractorToStringArray(Contractor contractor) {
		final String[] fieldValues = new String[RECORD_FIELDS];
		ContractorPK contractorPK = contractor.getPrimaryKey();
		fieldValues[0] = contractorPK.getName();
		fieldValues[1] = contractorPK.getLocation();
		fieldValues[2] = contractor.getSpecialties();
		fieldValues[3] = String.valueOf(contractor.getSize());
		fieldValues[4] = contractor.getRate();
		fieldValues[5] = contractor.getCustomerId();
		return fieldValues;
	}

	public static Contractor stringArrayToContractor(String[] fieldValues) {
		if (fieldValues.length < RECORD_FIELDS) {
			throw new IllegalArgumentException("Must have at least " + RECORD_FIELDS
					+ " elements in the string array argument to create a Contractor object");
		}
		final Contractor contractor = new Contractor();
		final ContractorPK primaryKey = Converter.stringArrayToContractorPK(fieldValues);
		contractor.setPrimaryKey(primaryKey);
		contractor.setSpecialities(fieldValues[2]);
		try {
			final int size = Integer.parseInt(fieldValues[3]);
			if (size < 0) {
				throw new IllegalArgumentException("The size field must be a positive integer value");
			} else {
				contractor.setSize(size);
			}

		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("The size field must be a positive integer value");
		}
		contractor.setRate(fieldValues[4]);
		contractor.setCustomerId(fieldValues[5]);
		return contractor;
	}

	public static String[] contractorPKToStringArray(ContractorPK contractorPK) {
		final String[] fieldValues = new String[PRIMARY_KEY_FIELDS];
		fieldValues[0] = contractorPK.getName();
		fieldValues[1] = contractorPK.getLocation();
		return fieldValues;
	}

	public static ContractorPK stringArrayToContractorPK(String[] fieldValues) {
		if (fieldValues.length < PRIMARY_KEY_FIELDS) {
			throw new IllegalArgumentException("Must have at least" + PRIMARY_KEY_FIELDS
					+ " elements in the string array argument to create a ContractorPK object");
		}
		final ContractorPK contractorPK = new ContractorPK();
		contractorPK.setName(fieldValues[0]);
		contractorPK.setLocation(fieldValues[1]);
		return contractorPK;
	}

	public static int[] convertIntegerListToIntArray(List<Integer> list) {
		final int[] intArray = new int[list.size()];
		for (int index = 0; index < list.size(); index++) {
			intArray[index] = list.get(index);
		}
		return intArray;
	}

	public static String convertBytesToString(byte[] valueBytes) throws IOException {
		return new String(valueBytes, Charset.forName(CHARACTER_ENCODING));
	}

	public static byte[] convertStringToBytes(String message) {
		return message.getBytes(Charset.forName(CHARACTER_ENCODING));
	}
}
