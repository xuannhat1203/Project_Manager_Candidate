package ra.edu.presentation;

import ra.edu.business.model.Application;
import ra.edu.business.model.RecruitmentPosition;
import ra.edu.business.service.admin.AdminServiceImp;
import ra.edu.business.service.candidate.CandidateServiceImp;

import java.util.List;
import java.util.Scanner;

import static com.mysql.cj.util.TimeUtil.DATE_FORMATTER;
import static ra.edu.presentation.PersonalInfoMenu.getMyInfor;
import static ra.edu.validate.validateProject.inputPositiveInt;

public class SubmittedApplicationMenu {
    public static Scanner sc = new Scanner(System.in);

    public static void submittedApplicationMenu() {
        while (true) {
            System.out.println("****** Xem đơn đã ứng tuyển ****** ");
            System.out.println("1. Xem danh sách đơn đã nộp");
            System.out.println("2. Xem chi tiết đơn (xác nhận tham gia phỏng vấn nếu có)");
            System.out.println("3. Quay về menu chính");
            int choice = inputPositiveInt("Nhập lựa chọn của bạn: ");
            switch (choice) {
                case 1:
                    displayApplication();
                    break;
                case 2:
                    candidateConfirmInterview();
                    break;
                case 3:
                    return;
                default:
                    System.out.println(">> Lựa chọn không hợp lệ!");
            }
        }
    }

    public static void displayApplication() {
        CandidateServiceImp candidateService = new CandidateServiceImp();
        int id = getMyInfor();
        if (id == -1) return;
        int page = 1;
        int size = 5;
        boolean exit = false;
        while (!exit) {
            List<Application> applications = candidateService.listMyInterview(id,page,size);
            System.out.printf("\n===== DANH SÁCH ĐƠN ỨNG TUYỂN - TRANG %d =====\n", page);
            System.out.println("+------+--------------+--------------------+-------------+------------+---------------------+----------------------+");
            System.out.println("| ID   | Candidate ID | Recruitment Pos ID | Tiến trình  | Kết quả    | Thời gian tạo       | Trạng thái           |");
            System.out.println("+------+--------------+--------------------+-------------+------------+---------------------+----------------------+");

            boolean hasData = false;
            for (Application app : applications) {
                if (app.getCandidateId() == id) {
                    hasData = true;
                    String status = app.getDestroyAt() != null ? "Đã hủy" : "Đang hoạt động";
                    String result = app.getInterviewResult() != null ? app.getInterviewResult() : "null";
                    System.out.printf("| %-4d | %-12d | %-18d | %-11s | %-10s | %-19s | %-20s |\n",
                            app.getId(),
                            app.getCandidateId(),
                            app.getRecruitmentPositionId(),
                            app.getProgress(),
                            result,
                            app.getCreateAt(),
                            status);
                }
            }


            if (!hasData) {
                System.out.printf("| %-112s |\n", "Không có dữ liệu đơn ứng tuyển nào ở trang này.");
            }

            System.out.println("+------+--------------+--------------------+-------------+------------+---------------------+----------------------+");
            System.out.println("\n[1] Trang sau | [2] Trang trước | [3] Thoát");
            System.out.print("Chọn hành động: ");

            String nav = sc.nextLine().trim();
            switch (nav) {
                case "1":
                    page++;
                    break;
                case "2":
                    page = Math.max(1, page - 1);
                    break;
                case "3":
                    exit = true;
                    System.out.println("Thoát xem danh sách.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
    }

    public static void candidateConfirmInterview() {
        AdminServiceImp adminService = new AdminServiceImp();
        CandidateServiceImp candidateService = new CandidateServiceImp();
        int id = inputPositiveInt("Nhập id đơn ứng tuyển cần xem chi tiết: ");
        Application app = adminService.getApplication(id);
        if (app == null) {
            System.out.println("Không tìm thấy đơn ứng tuyển này.");
            return;
        }

        if (app.getProgress() != null && app.getInterviewRequestDate() != null) {
            System.out.println("***** Xác nhận tham gia phỏng vấn ******");
            System.out.println("1. Tôi đồng ý");
            System.out.println("2. Tôi không đồng ý");
            System.out.println("3. Thoát");
            System.out.print("Nhập lựa chọn của bạn: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    if (candidateService.candidateConfirm(app.getId())) {
                        System.out.println("Xác nhận tham gia phỏng vấn thành công.");
                    } else {
                        System.out.println("Xác nhận tham gia phỏng vấn không thành công.");
                    }
                    break;
                case 2:
                    System.out.println("Bạn đã từ chối tham gia phỏng vấn.");
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
                    break;
            }
        } else {
            System.out.println("Đơn này không có yêu cầu phỏng vấn hoặc không đủ điều kiện để xác nhận.");
        }
    }
}
