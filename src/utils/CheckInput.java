package utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CheckInput {
	
	public static int checkInput(int min, int max) {
		Scanner sc=new Scanner(System.in);
		int option = 0;
		
		do {
			try {
				option=sc.nextInt();
				if(option>=min && option<=max)break;
			}catch(InputMismatchException e) {
				sc.next();
			}
			System.out.println("Selecciona una opcion valida");
		}while(true);
		
		return option;
	}
}
