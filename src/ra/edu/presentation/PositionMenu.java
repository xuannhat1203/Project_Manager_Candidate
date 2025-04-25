package ra.edu.presentation;

import ra.edu.business.model.RecruitmentPosition;
import ra.edu.business.model.Technology;
import ra.edu.business.service.admin.AdminServiceImp;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import static ra.edu.validate.validateProject.inputPositiveInt;

public class PositionMenu {
    public static void positionMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n========== QUẢN LÝ VỊ TRÍ TUYỂN DỤNG ==========");
            System.out.println("1. Thêm vị trí tuyển dụng mới");
            System.out.println("2. Cập nhật vị trí tuyển dụng");
            System.out.println("3. Xoá vị trí (đổi tên thành _deleted nếu có FK)");
            System.out.println("4. Xem danh sách vị trí đang hoạt động");
            System.out.println("5. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");
            int choice = inputPositiveInt("Nhập lựa chọn của bạn: ");
            switch (choice) {
                case 1:
                    addPosition();
                    break;
                case 2:
                    updatePosition();
                    break;
                case 3:
                    deletePosition();
                    break;
                case 4:
                    displayRecruitmentPosition();
                    break;
                case 5:
                    System.out.println(">> Quay lại menu quản trị...");
                    return;
                default:
                    System.out.println(">> Lựa chọn không hợp lệ!");
            }
        }
    }

    public static void addPosition() {
        AdminServiceImp adminService = new AdminServiceImp();
        Scanner sc = new Scanner(System.in);
        RecruitmentPosition newPosition = new RecruitmentPosition();
        System.out.println("Nhập các thông tin vị trí mới");
        newPosition.inputData();
        if (newPosition.getName().trim().isEmpty()) {
            System.out.println("Tên vị trí không được để trống hoặc chỉ có khoảng trắng.");
            return;
        }
        if (newPosition.getDescription() != null && newPosition.getDescription().trim().isEmpty()) {
            System.out.println("Mô tả không được chỉ có khoảng trắng.");
            return;
        }
        List<RecruitmentPosition> recruitmentPositionList = adminService.listRecruitmentPosition();
        Optional<RecruitmentPosition> check = recruitmentPositionList.stream().filter(p -> p.getName().equals(newPosition.getName())).findFirst();
        boolean isCheck = adminService.addNewPosition(
                newPosition.getName(),
                newPosition.getDescription(),
                newPosition.getMinSalary(),
                newPosition.getMaxSalary(),
                newPosition.getMinExperience(),
                newPosition.getExpiredDate(),
                newPosition.getTechnologyIds()
        );
        if (isCheck) {
            System.out.println("Thêm vị trí thành công");
        } else {
            System.out.println("Lỗi không xác định");
        }
    }


    public static void updatePosition() {
        AdminServiceImp adminService = new AdminServiceImp();
        List<RecruitmentPosition> list = adminService.listRecruitmentPosition();
        int id = inputPositiveInt("Nhập id vị trí muốn cập nhật: ");
        Optional<RecruitmentPosition> checkId = list.stream().filter(p -> p.getId() == id).findFirst();
        if (checkId.isPresent()) {
            RecruitmentPosition newPosition = new RecruitmentPosition();
            System.out.println("Nhập các thông tin mới của vị trí");
            newPosition.inputData();
            boolean isCheck = adminService.updatePosition(
                    id,
                    newPosition.getName(),
                    newPosition.getDescription(),
                    newPosition.getMinSalary(),
                    newPosition.getMaxSalary(),
                    newPosition.getMinExperience(),
                    newPosition.getExpiredDate()
            );
            if (isCheck) {
                System.out.println("Cập nhật thông tin thành công");
            } else {
                System.out.println("Lỗi không xác định");
            }
        } else {
            System.out.println("Vị trí không tồn tại");
        }
    }

    public static void deletePosition() {
        AdminServiceImp adminService = new AdminServiceImp();
        List<RecruitmentPosition> list = adminService.listRecruitmentPosition();
        int id = inputPositiveInt("Nhập id vị trí muốn xóa: ");
        Optional<RecruitmentPosition> checkId = list.stream().filter(p -> p.getId() == id).findFirst();
        if (checkId.isPresent()) {
            adminService.deletePosition(id);
            System.out.println("Xóa thành công");
        } else {
            System.out.println("Xóa không thành công");
        }
    }

    public static void displayRecruitmentPosition() {
        Scanner sc = new Scanner(System.in);
        AdminServiceImp adminService = new AdminServiceImp();
        int page = 1;
        int size = 5;
        String formatHeader = "| %-4s | %-30s | %-15s | %-15s | %-10s |%n";
        String formatRow = "| %-4d | %-30s | %-15s | %-15s | %-10d |%n";

        while (true) {
            List<RecruitmentPosition> recruitmentPositions = adminService.listPosition(page, size);
            recruitmentPositions = recruitmentPositions.stream()
                    .sorted(Comparator.comparing(RecruitmentPosition::getName))
                    .toList();
            if (recruitmentPositions.isEmpty()) {
                System.out.println("Không có vị trí nào ở trang này.");
            } else {
                System.out.printf("\n========== Danh sách vị trí - Trang %d ==========%n", page);
                System.out.format("+------+--------------------------------+-----------------+-----------------+------------+%n");
                System.out.format(formatHeader, "ID", "Tên vị trí", "Lương tối thiểu", "Lương tối đa", "Kinh nghiệm");
                System.out.format("+------+--------------------------------+-----------------+-----------------+------------+%n");

                for (RecruitmentPosition recruitment : recruitmentPositions) {
                    if (recruitment.getName() != null && !recruitment.getName().toLowerCase().contains("delete")) {
                        System.out.format(formatRow,
                                recruitment.getId(),
                                recruitment.getName(),
                                recruitment.getMinSalary() != null ? recruitment.getMinSalary() + " VNĐ" : "N/A",
                                recruitment.getMaxSalary() != null ? recruitment.getMaxSalary() + " VNĐ" : "N/A",
                                recruitment.getMinExperience());
                    }
                }
                System.out.format("+------+--------------------------------+-----------------+-----------------+------------+%n");
            }

            System.out.println("\n[1] Trang sau | [2] Trang trước | [3] Thoát");
            System.out.print("Chọn hành động: ");
            int choice = inputPositiveInt("Nhập lựa chọn của bạn: ");
            switch (choice) {
                case 1:
                    page++;
                    break;
                case 2:
                    if (page > 1) {
                        page--;
                    } else {
                        System.out.println(">> Bạn đang ở trang đầu tiên.");
                    }
                    break;
                case 3:
                    return;
                default:
                    System.out.println(">> Lựa chọn không hợp lệ!");
            }
        }
    }

}
