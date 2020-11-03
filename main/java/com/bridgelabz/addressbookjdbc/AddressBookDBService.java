package com.bridgelabz.addressbookjdbc;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class AddressBookDBService {

	private static AddressBookDBService addressBookDBService;
	private static Logger log = Logger.getLogger(AddressBookDBService.class.getName());

	private AddressBookDBService() {
	}

	public static AddressBookDBService getInstance() {
		if (addressBookDBService == null)
			addressBookDBService = new AddressBookDBService();
		return addressBookDBService;
	}

	public List<Contact> readData() {
		String sql = "SELECT * from address_book";
		return this.getContactDetailsUsingSqlQuery(sql);
	}

	private List<Contact> getContactDetailsUsingSqlQuery(String sql) {
		List<Contact> ContactList = null;
		try (Connection connection = addressBookDBService.getConnection();) {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet result = preparedStatement.executeQuery(sql);
			ContactList = this.getAddressBookData(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ContactList;
	}

	private List<Contact> getAddressBookData(ResultSet resultSet) {
		List<Contact> contactList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				String firstName = resultSet.getString("firstName");
				String lastName = resultSet.getString("lastName");
				String address = resultSet.getString("Address");
				String city = resultSet.getString("City");
				String state = resultSet.getString("State");
				int zip = resultSet.getInt("zip");
				int phoneNumber = resultSet.getInt("Phone_Number");
				String email = resultSet.getString("email");
				String addressBookType = resultSet.getString("Address_Book_Type");
				String addressBookName = resultSet.getString("Address_Book_Name");
				contactList.add(new Contact(firstName, lastName, address, city, state, zip, phoneNumber, email,
						addressBookType, addressBookName));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service";
		String userName = "root";
		String password = "Prem@1234";
		Connection connection;
		log.info("Connecting to database: " + jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		log.info("Connection successful: " + connection);
		return connection;
	}
}