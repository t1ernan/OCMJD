package suncertify.util;

// TODO: Auto-generated Javadoc
/**
 * The Class Utils.
 */
public class Utils {

	/**
	 * Checks if is customer id valid.
	 *
	 * @param customerId the customer id
	 * @return true, if is customer id valid
	 */
	public static boolean isCustomerIdValid(String customerId) {
		return customerId.matches("[0-9]{8}") || customerId.isEmpty();
	}
}
