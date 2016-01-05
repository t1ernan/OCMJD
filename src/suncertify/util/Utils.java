package suncertify.util;

public class Utils {

	public static boolean isCustomerIdValid(String customerId) {
		return customerId.matches("[0-9]{8}") || customerId.isEmpty();
	}
}
