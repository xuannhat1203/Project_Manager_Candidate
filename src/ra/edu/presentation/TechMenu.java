package ra.edu.presentation;

import ra.edu.business.model.Candidate;
import ra.edu.business.model.Technology;
import ra.edu.business.service.admin.AdminServiceImp;
import ra.edu.validate.validateTechnology;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static ra.edu.validate.validateProject.inputPositiveInt;

public class TechMenu {
    public static Scanner sc = new Scanner(System.in);

    public static void techMenu() {
        while (true) {
            System.out.println("********** Quản lí công nghệ **********");
            System.out.println("1. Xem danh sách công nghệ");
            System.out.println("2. Thêm công nghệ mới");
            System.out.println("3. Sửa công nghệ");
            System.out.println("4. Xoá công nghệ (đổi tên thành _deleted nếu liên kết FK)");
            System.out.println("5. Quay về menu chính");
            int choice = inputPositiveInt("Nhập lựa chọn của bạn: ");
            switch (choice) {
                case 1:
                    displayTechnology();
                    break;
                case 2:
                    addTechnology();
                    break;
                case 3:
                    updateTechnology();
                    break;
                case 4:
                    deleteTechnology();
                    break;
                case 5:
                    System.out.println("Quay lại menu chính");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ");
                    break;
            }
        }
    }
    public static void displayTechnology() {
        AdminServiceImp adminService = new AdminServiceImp();
        int page = 1;
        int size = 5;

        while (true) {
            List<Technology> technologies = adminService.listTechnologyPagination(page, size);
            technologies.stream().sorted(Comparator.comparing(Technology::getName)).toList();
            if (technologies.isEmpty()) {
                System.out.println("Không có công nghệ nào ở trang này.");
            } else {
                System.out.println("\n========== Trang " + page + " ==========");
                System.out.printf("%-10s | %-30s\n", "ID", "Tên Công Nghệ");
                System.out.println("---------------------------------------------");

                for (Technology technology : technologies) {
                    String name = technology.getName();
                    if (name != null && !name.toLowerCase().contains("delete")) {
                        System.out.printf("%-10d | %-30s\n", technology.getId(), name);
                    }
                }

                System.out.println("---------------------------------------------");
            }

            System.out.println("\n[1] Trang sau | [2] Trang trước | [3] Thoát");
            System.out.print("Chọn hành động: ");
            String choice = sc.nextLine().trim().toLowerCase();
            if (choice.equals("1")) {
                page++;
            } else if (choice.equals("2")) {
                if (page > 1) {
                    page--;
                } else {
                    System.out.println("Đang ở trang đầu tiên.");
                }
            } else if (choice.equals("3")) {
                break;
            } else {
                System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    public static void addTechnology() {
        AdminServiceImp adminService = new AdminServiceImp();
        String name = validateTechnology.validateTechnology();
        boolean isAdded = adminService.addTechnology(name);
        if (isAdded) {
            System.out.println("Thêm công nghệ thành công!");
        } else {
            System.out.println("Thêm công nghệ thất bại.");
        }
    }

    public static void updateTechnology() {
        AdminServiceImp adminService = new AdminServiceImp();
        int id = inputPositiveInt("Nhập ID công nghệ cần sửa: ");
        List<Technology> listTech = adminService.listTechnology();
        Optional<Technology> isCheck = listTech.stream().filter(tech -> tech.getId() == id).findFirst();
        if (isCheck.isPresent()) {
            String name = validateTechnology.validateTechnology();
            boolean isUpdated = adminService.updateTechnology(id, name);
            if (isUpdated) {
                System.out.println("Cập nhật công nghệ thành công!");
            } else {
                System.out.println("Cập nhật công nghệ thất bại.");
            }
        }else {
            System.out.println("ID công nghệ không tồn tại");
        }

    }
    public static void deleteTechnology() {
        AdminServiceImp adminService = new AdminServiceImp();
        int id = inputPositiveInt("Nhập ID công nghệ cần xoá: ");
        List<Technology> listTech = adminService.listTechnology();
        Optional<Technology> isCheck = listTech.stream().filter(tech -> tech.getId() == id).findFirst();
        if (isCheck.isPresent()) {
            boolean isUpdated = adminService.deleteTechnology(id);
            if (isUpdated) {
                System.out.println("Xoá công nghệ thành công!");
            } else {
                System.out.println("Xóa công nghệ thất bại.");
            }
        }else {
            System.out.println("ID công nghệ không tồn tại");
        }
    }
}
