package test;

import java.util.Scanner;

public class Demo {
   public static void main(String[] args) {
	int[] arr={12,1,4,5};
	Scanner sc=new Scanner(System.in);
	System.out.println("qingshuru");
	int a=sc.nextInt();
	for (int i = 0; i < arr.length; i++) {
		if (a!=arr[i]) {
			System.out.println("cuo");
			continue;
		}else{
			System.out.println("dui");
			break;
			
		}
	}
}
}
