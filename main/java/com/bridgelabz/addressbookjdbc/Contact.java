package com.bridgelabz.addressbookjdbc;

import java.time.LocalDate;
import java.util.Objects;

public class Contact {
	public String firstName;
	public String lastName;
	public String address;
	public String city;
	public String state;
	public int zip;
	public int phoneNumber;
	public String email;
	public String addressBookName;
	public String addressBookType;
	LocalDate startDate;

	public Contact(String firstName, String lastName, String address, String city, String state, int zip,
			int phoneNumber, String email, String addressBookName, String addressBookType) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.addressBookName = addressBookName;
		this.addressBookType = addressBookType;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Contact that = (Contact) o;
		return firstName.equals(that.firstName) && address.equals(that.address);
	}
	public Contact(String firstName, String lastName, String address, String city, String state, int zip,
			int phoneNumber, String email, String addressBookName, LocalDate startDate) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.addressBookName = addressBookName;
		this.setStartDate(startDate);
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName, address, city, state, zip, phoneNumber, email, addressBookName,
				startDate);
	}
}