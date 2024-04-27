package com.bengkel.booking.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;
import com.bengkel.booking.repositories.CustomerRepository;
import com.bengkel.booking.repositories.ItemServiceRepository;

public class BengkelService {
	
	// Silahkan tambahkan fitur-fitur utama aplikasi disini
	
	// Login
	
	// Info Customer
	public static void infoCustomer(String customerId) {
        Customer customer = null;
        // Cari customer berdasarkan customerId
        for (Customer cust : CustomerRepository.getAllCustomer()) {
            if (cust.getCustomerId().equals(customerId)) {
                customer = cust;
                break;
            }
        }
        
        if (customer != null) {
            System.out.println("Informasi Customer:");
            System.out.println("Customer Id: " + customer.getCustomerId());
            System.out.println("Nama: " + customer.getName());
            System.out.println("Alamat: " + customer.getAddress());
            System.out.println("Customer Status: " + (customer instanceof MemberCustomer ? "Member" : "Non Member"));
            if (customer instanceof MemberCustomer) {
                MemberCustomer member = (MemberCustomer) customer;
                System.out.println("Saldo Koin: " + member.getCoinBalance());
            }
            System.out.println("List Kendaraan:");
            for (Vehicle vehicle : customer.getVehicles()) {
                System.out.println(" - " + vehicle.getVehicleType() + " " + vehicle.getBrand());
            }
        } else {
            System.out.println("Customer tidak ditemukan.");
        }
    }
	
	// Booking atau Reservation
	public static void bookingService(String customerId) {
		Scanner input = new Scanner(System.in);
	
		// Cari customer berdasarkan customerId
		Customer customer = null;
		for (Customer cust : CustomerRepository.getAllCustomer()) {
			if (cust.getCustomerId().equals(customerId)) {
				customer = cust;
				break;
			}
		}
	
		if (customer != null) {
			System.out.println("Booking Service Bengkel");
			System.out.print("Masukkan Vehicle Id: ");
	
			if (input.hasNextLine()) {
				String vehicleId = input.nextLine();
	
				Vehicle selectedVehicle = null;
				for (Vehicle vehicle : customer.getVehicles()) {
					if (vehicle.getVehiclesId().equals(vehicleId)) {
						selectedVehicle = vehicle;
						break;
					}
				}
	
				if (selectedVehicle != null) {
					List<ItemService> listAllItemService = ItemServiceRepository.getAllItemService();
					List<ItemService> availableItemServices = new ArrayList<>();
					for (ItemService itemService : listAllItemService) {
						if (itemService.getVehicleType().equals(selectedVehicle.getVehicleType())) {
							availableItemServices.add(itemService);
						}
					}
	
					if (availableItemServices.isEmpty()) {
						System.out.println("Tidak ada layanan yang tersedia untuk kendaraan ini.");
					} else {
						System.out.println("List Item Service yang Tersedia:");
						for (int i = 0; i < availableItemServices.size(); i++) {
							ItemService itemService = availableItemServices.get(i);
							System.out.println((i + 1) + ". " + itemService.getServiceId() + ": " + itemService.getServiceName());
						}
	
						int maxService = customer instanceof MemberCustomer ? 2 : 1;
						System.out.println("Anda dapat memilih maksimal " + maxService + " item service.");
	
						List<ItemService> selectedServices = new ArrayList<>();
						for (int i = 0; i < maxService; i++) {
							System.out.print("Pilih service ke-" + (i + 1) + ": ");
							int choice = Validation.validasiNumberWithRange("Masukan Pilihan Service:", "Input Harus Berupa Angka!", "^[0-9]+$", availableItemServices.size(), 1);
							selectedServices.add(availableItemServices.get(choice - 1));
						}
	
						// Hitung total harga service yang dipilih
						int totalHarga = 0;
						for (ItemService itemService : selectedServices) {
							totalHarga += itemService.getPrice();
						}
	
						// Tampilkan total harga
						System.out.println("Total Harga: " + totalHarga);
	
						// Memilih metode pembayaran
						int maxMetodePembayaran = customer instanceof MemberCustomer ? 2 : 1;
						System.out.println("Pilih Metode Pembayaran:");
						System.out.println("1. Cash");
						if (customer instanceof MemberCustomer) {
							System.out.println("2. Saldo Coin");
						}
	
						int metodePembayaran = Validation.validasiNumberWithRange("Masukkan Pilihan Metode Pembayaran:", "Input Harus Berupa Angka!", "^[0-9]+$", maxMetodePembayaran, 1);
	
						// Proses pembayaran
						if (metodePembayaran == 1) {
							// Cash, tidak ada diskon
							System.out.println("Pembayaran dengan Cash.");
						} else if (metodePembayaran == 2) {
							// Saldo Coin
							if (customer instanceof MemberCustomer) {
								MemberCustomer member = (MemberCustomer) customer;
								if (member.getCoinBalance() >= totalHarga) {
									// Ada saldo cukup untuk pembayaran
									member.setCoinBalance(member.getCoinBalance() - totalHarga);
									System.out.println("Pembayaran dengan Saldo Coin, diskon 10%.");
									System.out.println("Saldo Coin Anda sekarang: " + member.getCoinBalance());
									
									// Update saldo koin dalam repository
									CustomerRepository.updateCoinBalance(customerId, -totalHarga);
								} else {
									// Saldo tidak mencukupi
									System.out.println("Saldo Coin tidak mencukupi.");
								}
							}
						}
	
						// Lakukan sesuatu dengan selectedServices, misalnya simpan ke dalam booking atau tampilkan ringkasan booking
					}
				} else {
					System.out.println("Kendaraan dengan Vehicle Id " + vehicleId + " tidak ditemukan.");
				}
			} else {
				System.out.println("Masukan tidak tersedia.");
			}
		} else {
			System.out.println("Customer tidak ditemukan.");
		}

	}
	
	// Top Up Saldo Coin Untuk Member Customer
	public static void topUpSaldoCoin(String customerId) {
		Customer customer = CustomerRepository.findCustomerById(customerId);
		if (customer != null) {
			if (customer instanceof MemberCustomer) {
				MemberCustomer member = (MemberCustomer) customer;
				Scanner input = new Scanner(System.in);
				System.out.print("Masukkan jumlah saldo coin yang ingin ditambahkan: ");
				int saldoBaru = Validation.validasiNumberWithRange("Masukkan jumlah saldo coin yang ingin ditambahkan: ", "Input Harus Berupa Angka!", "^[0-9]+$", Integer.MAX_VALUE, 0);
				member.setCoinBalance(member.getCoinBalance() + saldoBaru);
				System.out.println("Saldo coin berhasil ditambahkan. Saldo coin sekarang: " + member.getCoinBalance());
			} else {
				System.out.println("Maaf fitur ini hanya untuk Member saja!");
			}
		} else {
			System.out.println("Customer tidak ditemukan.");
		}
	}
	// Booking Order
	
	// Logout
	
}
