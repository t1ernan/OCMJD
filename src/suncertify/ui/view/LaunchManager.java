package suncertify.ui.view;

import suncertify.business.ContractorService;
import suncertify.ui.LaunchException;

public interface LaunchManager {
	
	public ContractorService getService() throws LaunchException;
	
	public void saveConfig();

	public void launch() throws LaunchException;
}
