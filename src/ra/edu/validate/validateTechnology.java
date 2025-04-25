package ra.edu.validate;

import ra.edu.business.model.Technology;
import ra.edu.business.service.admin.AdminServiceImp;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class validateTechnology {
    private static final String TECHNOLOGY_NAME_REGEX = "^[a-zA-Z0-9\\-\\s]{1,100}$";
    public static String validateTechnology() {
        AdminServiceImp adminService = new AdminServiceImp();
        List<Technology> technologies = adminService.listTechnology();
        Scanner scanner = new Scanner(System.in);
        String technology;
        while (true) {
            System.out.print("Nhập tên công nghệ: ");
            technology = scanner.nextLine().trim();
            if (technology.isEmpty()) {
                System.out.println("Tên công nghệ không được để trống.");
                continue;
            }
            if (technology.length() > 100) {
                System.out.println("Tên công nghệ không được dài quá 100 ký tự.");
                continue;
            }
            if (!technology.matches(TECHNOLOGY_NAME_REGEX)) {
                System.out.println("Tên công nghệ chỉ được chứa chữ cái, số, khoảng trắng hoặc dấu gạch ngang.");
                continue;
            }
            String finalTechnology = technology;
            Optional<Technology> isDuplicate = technologies.stream()
                    .filter(t -> t.getName().equalsIgnoreCase(finalTechnology))
                    .findFirst();
            if (isDuplicate.isPresent()) {
                System.out.println("Công nghệ này đã tồn tại. Vui lòng nhập công nghệ khác.");
                continue;
            }
            break;
        }
        return technology;
    }
}
