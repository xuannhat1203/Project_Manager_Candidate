package ra.edu.presentation;

import ra.edu.business.model.Application;
import ra.edu.business.model.RecruitmentPosition;
import ra.edu.business.service.admin.AdminServiceImp;
import ra.edu.business.service.candidate.CandidateServiceImp;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static com.mysql.cj.util.TimeUtil.DATE_FORMATTER;
import static ra.edu.presentation.PersonalInfoMenu.getMyInfor;
import static ra.edu.validate.validateProject.inputPositiveInt;
import static ra.edu.validate.validateProject.isValidURL;

public class ApplyMenu {
    public static Scanner sc = new Scanner(System.in);
    public static void applyMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("***** Xem và Nộp đơn ứng tuyển *****");
            System.out.println("1. Xem danh sách vị trí đang hoạt động");
            System.out.println("2. Xem chi tiết và apply (cung cấp CV URL)");
            System.out.println("3. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");
            int choice = inputPositiveInt("Nhập lựa chọn của bạn: ");
            switch (choice) {
                case 1:
                    displayActiveRecruitmentPositions();
                    break;
                case 2:
                    applyCV();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }
    public static void displayActiveRecruitmentPositions() {
        CandidateServiceImp candidateService = new CandidateServiceImp();
        int page = 1;
        int size = 5;
        boolean exit = false;

        while (!exit) {
            List<RecruitmentPosition> positions = candidateService.getActiveRecruitmentPositions(page, size);
            System.out.printf("\n===== DANH SÁCH VỊ TRÍ TUYỂN DỤNG ĐANG HOẠT ĐỘNG - TRANG %d =====\n", page);
            System.out.println("+----+----------------------------+------------+------------+------------+---------------+------------------+");
            System.out.println("| ID | Tên vị trí                 | Lương Min  | Lương Max  | Kinh nghiệm| Ngày tạo      | Hết hạn          |");
            System.out.println("+----+----------------------------+------------+------------+------------+---------------+------------------+");

            if (positions.isEmpty()) {
                System.out.printf("| %-114s |\n", "Không có vị trí tuyển dụng nào đang hoạt động ở trang này.");
            } else {
                for (RecruitmentPosition pos : positions) {
                    System.out.printf("| %-2d | %-26s | %-10s | %-10s | %-10d | %-13s | %-16s |\n",
                            pos.getId(),
                            pos.getName(),
                            pos.getMinSalary(),
                            pos.getMaxSalary(),
                            pos.getMinExperience(),
                            pos.getCreatedDate(),
                            pos.getExpiredDate());
                }
            }

            System.out.println("+----+----------------------------+------------+------------+------------+---------------+------------------+");
            System.out.println("\n[1] Trang sau | [2] Trang trước | [3] Thoát");
            System.out.print("Chọn hành động: ");
            int choice = inputPositiveInt("Nhập lựa chọn của bạn: ");
            switch (choice) {
                case 1:
                    page++;
                    break;
                case 2:
                    page = Math.max(1, page - 1);
                    break;
                case 3:
                    exit = true;
                    System.out.println("Thoát danh sách vị trí.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
    }

    public static void applyCV() {
        AdminServiceImp adminService = new AdminServiceImp();
        CandidateServiceImp candidateService = new CandidateServiceImp();
        Scanner sc = new Scanner(System.in);
        int recruitmentPositionId = -1;
        while (recruitmentPositionId <= 0) {
            System.out.println("Nhập mã vị trí tuyển dụng bạn muốn ứng tuyển: ");
            if (sc.hasNextInt()) {
                recruitmentPositionId = sc.nextInt();
                sc.nextLine();
                if (recruitmentPositionId <= 0) {
                    System.out.println("ID vị trí tuyển dụng phải lớn hơn 0. Vui lòng thử lại.");
                }
            } else {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập một số nguyên.");
                sc.nextLine();
            }
        }
        Application app = adminService.getApplication(recruitmentPositionId);
        RecruitmentPosition rp = candidateService.getRecruitmentPositionById(app.getRecruitmentPositionId());
        System.out.println("ID vị trí: "+app.getId());
        System.out.println("Tên vị trí: " + rp.getName());
        System.out.println("Trạng thái:" + app.getProgress());
        List<Application> applicationList = adminService.listApplication();
        int finalRecruitmentPositionId = recruitmentPositionId;
        Optional<Application> isCheck = applicationList.stream().filter(a-> a.getId() == finalRecruitmentPositionId).findFirst();
        if (isCheck.isPresent()) {
            int candidateId = getMyInfor();
            String cvUrl = inputValidURL(sc);
            boolean result = candidateService.applyForApplication(candidateId, recruitmentPositionId, cvUrl);
            if (result) {
                System.out.println("Đơn ứng tuyển đã được nộp thành công!");
            }
        }else {
            System.out.println("Không tìm thấy vị trí ứng tuyển`");
        }

    }
    public static String inputValidURL(Scanner sc) {
        String url;
        while (true) {
            System.out.print("Nhập URL CV (bắt đầu bằng http:// hoặc https://): ");
            url = sc.nextLine().trim();
            if (isValidURL(url)) {
                return url;
            } else {
                System.out.println("URL không hợp lệ. Vui lòng thử lại.");
            }
        }
    }
}
