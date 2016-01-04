package suncertify.db;

public class DAOFactory {

	private static Data data;
	
	public static Data getDbManager(String dbFilePath){
		if(data == null){
			try {
				System.out.println("Creating 1 instance of Data");
				data = new Data(dbFilePath);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
}
