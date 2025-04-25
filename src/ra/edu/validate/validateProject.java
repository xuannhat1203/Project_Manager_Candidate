package ra.edu.validate;

import java.util.Scanner;

public class validateProject {
    public static Scanner sc = new Scanner(System.in);

    public static int inputPositiveInt(String message) {
        int number = -1;
        while (number <= 0) {
            System.out.print(message);
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Không được để trống. Vui lòng nhập một số nguyên dương.");
                continue;
            }

            try {
                number = Integer.parseInt(input);
                if (number <= 0) {
                    System.out.println("Vui lòng nhập một số nguyên dương lớn hơn 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Đầu vào không hợp lệ. Vui lòng nhập một số nguyên.");
            }
        }
        return number;
    }

    public static boolean isValidURL(String url) {
        try {
            new java.net.URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
