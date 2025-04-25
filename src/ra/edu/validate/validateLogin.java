package ra.edu.validate;

import java.util.Scanner;

public class validateLogin {
    public static Scanner sc = new Scanner(System.in);
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&]).{8,255}$";
    public static String validateEmail(String message) {
        System.out.println(message);
        while (true) {
            String email = sc.nextLine().trim();
            if (email.isEmpty()) {
                System.out.println("Email không được để trống.");
            } else if (!email.matches(EMAIL_REGEX)) {
                System.out.println("Email không hợp lệ. Vui lòng nhập đúng định dạng (vd: example@gmail.com).");
            } else {
                return email;
            }
        }
    }
    public static String validatePassword(String message) {
        System.out.println(message);
        while (true) {
            String password = sc.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("Mật khẩu không được để trống.");
            } else {
                return password;
            }
        }
    }
    public static String validatePassWordRegister(String message) {
        System.out.println(message);
        while (true) {
            String password = sc.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("Mật khẩu không được để trống.");
            } else if (password.length() < 8) {
                System.out.println("Mật khẩu phải có ít nhất 8 ký tự.");
            } else if (!password.matches(".*[A-Z].*")) {
                System.out.println("Mật khẩu phải chứa ít nhất một chữ cái viết hoa.");
            } else if (!password.matches(".*[a-z].*")) {
                System.out.println("Mật khẩu phải chứa ít nhất một chữ cái viết thường.");
            } else if (!password.matches(".*\\d.*")) {
                System.out.println("Mật khẩu phải chứa ít nhất một chữ số.");
            } else if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
                System.out.println("Mật khẩu phải chứa ít nhất một ký tự đặc biệt.");
            } else {
                return password;
            }
        }
    }

}
