package ra.edu.validate;

import java.util.Scanner;
import java.util.regex.Pattern;

public class validateCandidate {
    private static final Scanner sc = new Scanner(System.in);

    public static String validateName(String message) {
        System.out.println(message);
        String regex = "^[\\p{L}\\s'.-]+$";
        while (true) {
            String name = sc.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Không được để trống tên.");
            } else if (name.length() > 100) {
                System.out.println("Tên không được vượt quá 100 ký tự.");
            } else if (!name.matches(regex)) {
                System.out.println("Tên không được chứa ký tự đặc biệt hoặc số.");
            } else {
                return name;
            }
        }
    }

    public static String validateEmail(String message) {
        System.out.println(message);
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        while (true) {
            String email = sc.nextLine().trim();
            if (email.isEmpty()) {
                System.out.println("Email không được để trống.");
            } else if (!email.matches(regex)) {
                System.out.println("Email không hợp lệ. Vui lòng nhập lại.");
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
            } else if (password.length() < 6) {
                System.out.println("Mật khẩu phải có ít nhất 6 ký tự.");
            } else if (password.length() > 255) {
                System.out.println("Mật khẩu không được vượt quá 255 ký tự.");
            } else {
                return password;
            }
        }
    }

    public static String validatePhone(String message) {
        System.out.println(message);
        String regex = "^(\\+84|0)[3|5|7|8|9]\\d{8}$";
        while (true) {
            String phone = sc.nextLine().trim();
            if (phone.isEmpty()) {
                System.out.println("Số điện thoại không được để trống.");
            } else if (!phone.matches(regex)) {
                System.out.println("Số điện thoại không hợp lệ.");
            } else {
                return phone;
            }
        }
    }

    public static int validateExperience(String message) {
        System.out.println(message);
        while (true) {
            try {
                int experience = Integer.parseInt(sc.nextLine().trim());
                if (experience < 0 || experience > 50) {
                    System.out.println("Số năm kinh nghiệm phải nằm trong khoảng 0 - 50.");
                } else {
                    return experience;
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số hợp lệ.");
            }
        }
    }

    public static String validateGender(String message) {
        System.out.println(message + " (Nam/Nữ): ");
        while (true) {
            String gender = sc.nextLine().trim().toLowerCase();
            if (gender.equals("nam") || gender.equals("nữ") || gender.equals("nu")) {
                return gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase(); // Normalize
            } else {
                System.out.println("Giới tính không hợp lệ. Vui lòng nhập 'Nam' hoặc 'Nữ'.");
            }
        }
    }

    public static Boolean validateStatus(String message) {
        System.out.println(message + " (true/false): ");
        while (true) {
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("true")) {
                return true;
            } else if (input.equals("false")) {
                return false;
            } else {
                System.out.println("Giá trị trạng thái không hợp lệ. Vui lòng nhập 'true' hoặc 'false'.");
            }
        }
    }

    public static String validateDescription(String message) {
        System.out.println(message);
        while (true) {
            String description = sc.nextLine().trim();
            if (description.isEmpty()) {
                System.out.println("Mô tả không được để trống.");
            } else if (description.length() > 255) {
                System.out.println("Mô tả không được vượt quá 255 ký tự.");
            } else {
                return description;
            }
        }
    }

    public static String validateDob(String message) {
        System.out.println(message + " (Định dạng: yyyy-MM-dd): ");
        while (true) {
            String dob = sc.nextLine().trim();
            try {
                java.time.LocalDate.parse(dob); // ISO_LOCAL_DATE
                return dob;
            } catch (Exception e) {
                System.out.println("Ngày sinh không hợp lệ. Vui lòng nhập đúng định dạng yyyy-MM-dd.");
            }
        }
    }
}
