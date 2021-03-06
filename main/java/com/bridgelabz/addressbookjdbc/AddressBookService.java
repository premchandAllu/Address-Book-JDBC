package com.bridgelabz.addressbookjdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AddressBookService {

	private static Logger log = Logger.getLogger(AddressBookService.class.getName());
	private List<Contact> contactList;
	private Map<String, Integer> contactByCityOrState;
	private AddressBookDBService addressBookDBService;

	public enum IOService {
		DB_IO, REST_IO
	}

	public AddressBookService(List<Contact> contactList) {
		this();
		this.contactList = new ArrayList<>(contactList);
	}

	public AddressBookService() {
		addressBookDBService = AddressBookDBService.getInstance();
	}

	public List<Contact> readContactData() {
		this.contactList = addressBookDBService.readData();
		return contactList;
	}

	public long countEntries(IOService ioService) {
		return contactList.size();
	}

	public List<Contact> readData(IOService ioService) {
		if (ioService.equals(IOService.DB_IO))
			this.contactList = addressBookDBService.readData();
		return contactList;
	}

	public void updateContactDetails(String name, String address) {
		int result = addressBookDBService.updateContactData(name, address);
		if (result == 0)
			return;
		Contact personInfo = this.getContactData(name);
		if (personInfo != null)
			personInfo.address = address;
	}

	public Contact getContactData(String name) {
		return this.contactList.stream().filter(contact -> contact.firstName.equals(name)).findFirst().orElse(null);
	}

	public boolean checkContactInSyncWithDB(String name) {
		List<Contact> contactList = addressBookDBService.getContactDataByName(name);
		return contactList.get(0).equals(getContactData(name));
	}

	public List<Contact> readContactDataForGivenDateRange(LocalDate startDate, LocalDate endDate) {
		this.contactList = addressBookDBService.getContactForGivenDateRange(startDate, endDate);
		return contactList;
	}

	public Map<String, Integer> readContactByCityOrState() {
		this.contactByCityOrState = addressBookDBService.getContactsByCityOrState();
		return contactByCityOrState;
	}

	public void addContactToDatabase(String firstName, String lastName, String address, String city, String state,
			int zip, int phone, String email, String addressBookName, String addressBookType, LocalDate startDate) {
		contactList.add(addressBookDBService.addContact(firstName, lastName, address, city, state, zip, phone, email,
				addressBookName, startDate));

	}

	public void addContactToDB(String firstName, String lastName, String address, String city, String state, int zip,
			int phone, String email, String addressBookName, LocalDate startDate) {
		contactList.add(addressBookDBService.addContact(firstName, lastName, address, city, state, zip, phone, email,
				addressBookName, startDate));

	}

	public void addContact(List<Contact> contactDataList) {
		contactDataList.forEach(contactData -> {
			log.info("Employee being added : " + contactData.firstName);
			this.addContactToDB(contactData.firstName, contactData.lastName, contactData.address, contactData.city,
					contactData.state, contactData.zip, contactData.phoneNumber, contactData.email,
					contactData.addressBookName, contactData.startDate);
			log.info("Employee added : " + contactData.firstName);
		});
		log.info("" + this.contactList);
	}

	public void addEmployeeToPayrollWithThreads(List<Contact> contactDataList) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<>();
		contactDataList.forEach(contactData -> {
			Runnable task = () -> {
				employeeAdditionStatus.put(contactData.hashCode(), false);
				log.info("Employee being added : " + Thread.currentThread().getName());
				this.addContactToDB(contactData.firstName, contactData.lastName, contactData.address, contactData.city,
						contactData.state, contactData.zip, contactData.phoneNumber, contactData.email,
						contactData.addressBookName, contactData.startDate);
				employeeAdditionStatus.put(contactData.hashCode(), true);
				log.info("Employee added : " + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, contactData.firstName);
			thread.start();
		});
		while (employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		log.info("" + this.contactList);
	}

	public void addContactToAddressBook(Contact contactData, IOService ioService) {
		if (ioService.equals(IOService.REST_IO))
			contactList.add(contactData);
	}

	public void updateContactJsonServer(String firstName, String address, IOService ioService) {
		if (ioService.equals(IOService.REST_IO)) {
			Contact personInfo = this.getContactData(firstName);
			if (personInfo != null)
				personInfo.address = address;
		}
	}

	public void deleteContact(String firstName, IOService ioService) {
		if (ioService.equals(IOService.REST_IO)) {
			Contact contactData = this.getContactData(firstName);
			contactList.remove(contactData);
		}
	}

}