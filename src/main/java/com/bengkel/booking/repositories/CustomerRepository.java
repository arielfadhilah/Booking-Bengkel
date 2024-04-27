package com.bengkel.booking.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bengkel.booking.models.Car;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Motorcyle;
import com.bengkel.booking.models.Vehicle;

public class CustomerRepository {

	private static List<Customer> listAllCustomers;

    static {
        listAllCustomers = getAllCustomer();
    }

	

	
	public static List<Customer> getAllCustomer(){
		List<Customer> listAllCustomer;
		
		//Customer 1
		Car carCust1 = new Car("B201E", "Silver", "Honda", "Manual", 2000, 4);
		Motorcyle motorCust1 = new Motorcyle("B231E", "Hitam", "Yamaha", "Otomatis", 2017, 155);		
		List<Vehicle> listCust1Vechicle = Arrays.asList(carCust1, motorCust1);
		
		Customer cust1 = new Customer(
			"Cust-001", 
			"Budi", 
			"Bandung", 
			"cust001",
			listCust1Vechicle);
		
		//Customer 2
		Vehicle motorCust2 = new Motorcyle("D6734ZD", "Merah", "Honda", "Otomatis", 2019, 125);		
		List<Vehicle> listCust2Vechicle = Arrays.asList(motorCust2);
		
		Customer cust2 = new Customer("Cust-002", "Yanto", "Bandung", "cust002", listCust2Vechicle);
		
		//Customer 3
		Car carCust3 = new Car("Z4551E", "Biru", "Daihatsu", "Otomatis", 2010, 4);
		Motorcyle motorCust3 = new Motorcyle("Z231DHW", "Hitam", "Suzuki", "Manual", 2012, 135);		
		List<Vehicle> listCust3Vechicle = Arrays.asList(carCust3, motorCust3);
		
		Customer cust3 = new MemberCustomer("Cust-003", "Rahmat", "Garut", "cust003", listCust3Vechicle, 2000000);
		
		listAllCustomer = Arrays.asList(cust1, cust2, cust3);
		
		return listAllCustomer;
	}

	public static Customer findCustomerById(String customerId) {
		for (Customer customer : listAllCustomers) {
			if (customer.getCustomerId().equals(customerId)) {
				return customer;
			}
		}
		return null;
	}

	public static boolean validateLogin(String customerId, String password) {
		Customer customer = findCustomerById(customerId);
		System.out.println("Cek Customer: " + customer); // Pencetakan untuk memeriksa apakah customer ditemukan
		if (customer != null && customer.getPassword().equals(password)) {
			return true;
		}
		return false;
	}

	public static void updateCoinBalance(String customerId, int amount) {
		Customer customer = findCustomerById(customerId);
		if (customer != null && customer instanceof MemberCustomer) {
			MemberCustomer member = (MemberCustomer) customer;
			double newBalance = (double) member.getCoinBalance() + amount;
			member.setCoinBalance(newBalance);
		}
	}
	
}
