package ra.edu.validate;

import ra.edu.business.model.RecruitmentPosition;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JobPositionValidator {
    private static final Scanner sc = new Scanner(System.in);
    private static final List<RecruitmentPosition> listPosition = new ArrayList<>();
    public static String validatePositionName(String message) {
        String positionName;
        while (true) {
            System.out.print(message);
            positionName = sc.nextLine().trim();

            if (positionName.isEmpty()) {
                System.out.println("❌ Tên vị trí không được để trống.");
                continue;
            }

            String finalPositionName = positionName;
            boolean exists = listPosition.stream()
                    .anyMatch(p -> p.getName().equalsIgnoreCase(finalPositionName));
            if (exists) {
                System.out.println("❌ Tên vị trí đã tồn tại. Vui lòng nhập tên khác.");
            } else {
                return positionName;
            }
        }
    }
    public static BigDecimal validatePositioMinSalary(String message) {
        BigDecimal minSalary;
        while (true) {
            System.out.print(message);
            try {
                minSalary = new BigDecimal(sc.nextLine().trim());
                if (minSalary.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("❌ Mức lương tối thiểu phải lớn hơn hoặc bằng 0.");
                } else {
                    return minSalary;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Định dạng lương không hợp lệ. Vui lòng nhập số.");
            }
        }
    }
    public static BigDecimal validatePositionMaxSalary(String message, BigDecimal minSalary) {
        BigDecimal maxSalary;
        while (true) {
            System.out.print(message);
            try {
                maxSalary = new BigDecimal(sc.nextLine().trim());
                if (maxSalary.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("❌ Mức lương tối đa phải lớn hơn hoặc bằng 0.");
                } else if (maxSalary.compareTo(minSalary) < 0) {
                    System.out.println("❌ Mức lương tối đa phải lớn hơn mức lương tối thiểu.");
                } else {
                    return maxSalary;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Định dạng lương không hợp lệ. Vui lòng nhập số.");
            }
        }
    }
    public static LocalDate validateExpiredDate(String message, LocalDate createdDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            try {
                LocalDate expiredDate = LocalDate.parse(input, formatter);
                if (expiredDate.isAfter(createdDate)) {
                    return expiredDate;
                } else {
                    System.out.println("❌ Ngày hết hạn phải sau ngày tạo. Vui lòng nhập lại.");
                }
            } catch (Exception e) {
                System.out.println("❌ Sai định dạng ngày. Vui lòng nhập theo định dạng yyyy-MM-dd.");
            }
        }
    }
    public static String validateStringInput(String message) {
        String input;
        while (true) {
            System.out.print(message);
            input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("❌ Không được để trống. Vui lòng nhập lại.");
            } else {
                return input;
            }
        }
    }
    public static int validatePositiveInt(String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            try {
                int number = Integer.parseInt(input);
                if (number < 0) {
                    System.out.println("❌ Giá trị phải >= 0. Vui lòng nhập lại.");
                } else {
                    return number;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Định dạng không hợp lệ. Vui lòng nhập số nguyên.");
            }
        }
    }

}
