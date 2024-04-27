package com.bengkel.booking.services;

import java.util.Scanner;

import com.bengkel.booking.models.Customer;
import com.bengkel.booking.repositories.CustomerRepository;

public class MenuService {
	private static String currentCustomerId = null;
	private static Scanner input = new Scanner(System.in);
	private static boolean isLooping = true;

	public static void run() {
		do {
            login();
            if (!isLooping) {
                break;
            }
            mainMenu();
        } while (isLooping);
    }
	
	public static void login() {
		boolean isValid = false;
		int attemptCount = 0;
		String idCustomer = null;
		
		do {
			System.out.print("Masukkan ID Customer: ");
			 idCustomer = input.nextLine();
			System.out.print("Masukkan Password: ");
			String password = input.nextLine();
			
			// Cek apakah idCustomer dan password cocok
			for (Customer customer : CustomerRepository.getAllCustomer()) {
				if (customer.getCustomerId().equals(idCustomer) && customer.getPassword().equals(password)) {
					isValid = true;
					break;
				}
			}
			
			if (!isValid) {
				System.out.println("ID Customer atau Password salah!");
				attemptCount++;
			}
			
			if (attemptCount >= 3) {
				System.out.println("Anda sudah mencoba login sebanyak 3 kali. Aplikasi akan keluar.");
				System.exit(0);
			}
		} while (!isValid && attemptCount < 3);

		if (isValid) {
			System.out.println("Login berhasil!");
			currentCustomerId = idCustomer; // update currentCustomerId dengan ID Customer yang berhasil login
			BengkelService.infoCustomer(currentCustomerId);
		}
	}

	
	public static void mainMenu() {
		String[] listMenu = {"Informasi Customer", "Booking Bengkel", "Top Up Bengkel Coin", "Informasi Booking", "Logout"};
		int menuChoice = 0;
		boolean isLooping = true;
		
		do {
			PrintService.printMenu(listMenu, "Booking Bengkel Menu");
			menuChoice = Validation.validasiNumberWithRange("Masukan Pilihan Menu:", "Input Harus Berupa Angka!", "^[0-9]+$", listMenu.length-1, 0);
			System.out.println(menuChoice);
			
			switch (menuChoice) {
			case 1:
				//panggil fitur Informasi Customer
				BengkelService.infoCustomer(currentCustomerId);
				break;
			case 2:
				BengkelService.bookingService(currentCustomerId);
				break;
			case 3:
				//panggil fitur Top Up Saldo Coin
				BengkelService.topUpSaldoCoin(currentCustomerId);
				break;
			case 4:
				
				break;
			default:
				System.out.println("Logout");
				isLooping = false;
				break;
			}
		} while (isLooping);
		
	}
	
	//Silahkan tambahkan kodingan untuk keperluan Menu Aplikasi
}
