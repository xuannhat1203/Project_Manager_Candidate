package ra.edu.presentation;

import ra.edu.business.model.Application;
import ra.edu.business.model.Technology;
import ra.edu.business.service.admin.AdminServiceImp;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static ra.edu.validate.validateProject.inputPositiveInt;

public class ApplicationMenu {
    public static Scanner scanner = new Scanner(System.in);

    public static void applicationMenu() {
        while (true) {
            System.out.println("\n========== QUẢN LÝ ĐƠN ỨNG TUYỂN ==========");
            System.out.println("1. Xem danh sách đơn ứng tuyển");
            System.out.println("2. Lọc đơn theo trạng thái (progress, result)");
            System.out.println("3. Hủy đơn (ghi lý do, cập nhật ngày hủy)");
            System.out.println("4. Xem chi tiết đơn (tự chuyển từ pending -> handling)");
            System.out.println("5. Gửi thông tin phỏng vấn (chuyển sang interviewing)");
            System.out.println("6. Cập nhật kết quả phỏng vấn (done, ghi chú, đậu/rớt)");
            System.out.println("7. Quay về menu chính");
            int choice = inputPositiveInt("Nhập lựa chọn của bạn: ");
            switch (choice) {
                case 1:
                    displayApplication();
                    break;
                case 2:
                    filterByOption();
                    break;
                case 3:
                    destroyInterview();
                    break;
                case 4:
                    showInforInterviewDetail();
                    break;
                case 5:
                    updateInterview();
                    break;
                case 6:
                    completeInterview();
                    break;
                case 7:
                    System.out.println(">> Quay lại menu quản trị...");
                    return;
                default:
                    System.out.println(">> Lựa chọn không hợp lệ!");
            }
        }
    }

    public static void displayApplication() {
        AdminServiceImp adminService = new AdminServiceImp();
        Scanner sc = new Scanner(System.in);
        int page = 1;
        int size = 5;

        while (true) {
            List<Application> applications = adminService.listApplication(page, size);
            System.out.println("\n===== DANH SÁCH ĐƠN ỨNG TUYỂN - TRANG " + page + " =====");
            String format = "| %-4s | %-12s | %-18s | %-11s | %-10s | %-19s | %-20s |\n";
            System.out.format("+------+--------------+--------------------+-------------+------------+---------------------+----------------------+\n");
            System.out.format("| ID   | Candidate ID | Recruitment Pos ID | Tiến trình  | Kết quả    | Thời gian tạo       | Trạng thái           |\n");
            System.out.format("+------+--------------+--------------------+-------------+------------+---------------------+----------------------+\n");

            boolean hasData = false;
            for (Application app : applications) {
                hasData = true;
                String status = (app.getDestroyAt() != null) ? "Đã hủy" : "Đang hoạt động";
                System.out.format(format,
                        app.getId(),
                        app.getCandidateId(),
                        app.getRecruitmentPositionId(),
                        app.getProgress(),
                        app.getInterviewResult() == null ? "null" : app.getInterviewResult(),
                        app.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        status);
            }

            if (!hasData) {
                System.out.println("| Không có dữ liệu đơn ứng tuyển nào ở trang này.                                       |");
            }

            System.out.format("+------+--------------+--------------------+-------------+------------+---------------------+----------------------+\n");
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
                        System.out.println("Bạn đang ở trang đầu tiên.");
                    }
                    break;
                case 3:
                    System.out.println("Thoát xem danh sách.");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
    }

    private static String truncateString(String str, int maxLength) {
        if (str == null) return "";
        return str.length() <= maxLength ? str : str.substring(0, maxLength - 3) + "...";
    }

    public static void filterByOption() {
        System.out.println("1. Lọc theo trạng thái");
        System.out.println("2. Lọc theo kết quả");
        System.out.println("3. Thoát");
        System.out.print("Nhập lựa chọn của bạn: ");
        int choice = inputPositiveInt("Nhập lựa chọn của bạn: ");
        switch (choice) {
            case 1:
                filterByProcess();
                break;
            case 2:
                filterByResult();
                break;
            case 3:
                System.out.println("Thoát");
                return;
            default:
                System.out.println("Lựa chọn không hợp lệ");
                break;
        }
    }

    public static void filterByProcess() {
        AdminServiceImp adminService = new AdminServiceImp();
        System.out.print("Nhập trạng thái tiến trình muốn lọc (pending, handling, interviewing, done): ");
        String process = scanner.nextLine().trim().toLowerCase();

        if (!process.equals("pending") &&
                !process.equals("handling") &&
                !process.equals("interviewing") &&
                !process.equals("done")) {
            System.out.println("Trạng thái không hợp lệ. Chọn từ: pending, handling, interviewing, done.");
            return;
        }

        List<Application> filteredList = adminService.listFilterByProcess(process);
        if (filteredList.isEmpty()) {
            System.out.println("Không tìm thấy đơn nào với trạng thái: " + process);
        } else {
            displayPaginatedApplications(filteredList, "TRẠNG THÁI: " + process);
        }
    }
    public static void filterByResult() {
        AdminServiceImp adminService = new AdminServiceImp();
        System.out.print("Nhập kết quả muốn lọc (pass, fail, null): ");
        String result = scanner.nextLine().trim().toLowerCase();

        if (!result.equals("pass") && !result.equals("fail") && !result.equals("null")) {
            System.out.println("Kết quả không hợp lệ. Chọn từ: pass, fail, null.");
            return;
        }
        List<Application> filteredList = adminService.listApplicationByResult(result);
        if (filteredList.isEmpty()) {
            System.out.println("Không tìm thấy đơn nào với kết quả: " + result);
        } else {
            displayPaginatedApplications(filteredList, "KẾT QUẢ: " + result);
        }
    }

    private static void displayPaginatedApplications(List<Application> applications, String title) {
        int page = 1;
        int size = 5;
        Scanner sc = new Scanner(System.in);
        int totalPage = (int) Math.ceil((double) applications.size() / size);
        while (true) {
            int start = (page - 1) * size;
            int end = Math.min(start + size, applications.size());
            List<Application> pageList = applications.subList(start, end);
            System.out.println("\n===== DANH SÁCH ĐƠN ỨNG TUYỂN (" + title + ") - TRANG " + page + "/" + totalPage + " =====");
            String format = "| %-4s | %-12s | %-18s | %-11s | %-10s | %-20s |\n";
            System.out.format("+------+--------------+--------------------+-------------+------------+----------------------+\n");
            System.out.format("| ID   | Candidate ID | Recruitment Pos ID | Tiến trình  | Kết quả    | Lý do hủy            |\n");
            System.out.format("+------+--------------+--------------------+-------------+------------+----------------------+\n");
            for (Application app : pageList) {
                System.out.format(format,
                        app.getId(),
                        app.getCandidateId(),
                        app.getRecruitmentPositionId(),
                        app.getProgress(),
                        app.getInterviewResult() == null ? "null" : app.getInterviewResult(),
                        truncateString(app.getDestroyReason(), 20));
            }

            System.out.format("+------+--------------+--------------------+-------------+------------+----------------------+\n");
            System.out.println("\n[1] Trang sau | [2] Trang trước | [3] Thoát");
            System.out.print("Chọn hành động: ");
            int choice = inputPositiveInt("Nhập lựa chọn của bạn: ");
            switch (choice) {
                case 1:
                    if (page < totalPage) {
                        page++;
                    } else {
                        System.out.println("Đang ở trang cuối cùng.");
                    }
                    break;
                case 2:
                    if (page > 1) {
                        page--;
                    } else {
                        System.out.println("Đang ở trang đầu tiên.");
                    }
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    public static void destroyInterview() {
        AdminServiceImp adminService = new AdminServiceImp();
        int id = inputPositiveInt("Nhập id muốn hủy phỏng vấn: ");
        System.out.print("Nhập lý do hủy phỏng vấn: ");
        String reason = scanner.nextLine().trim();
        if (reason.isEmpty()) {
            System.out.println("Lý do hủy không được để trống.");
            return;
        }

        List<Application> list = adminService.listApplication();
        boolean found = false;
        for (Application app : list) {
            if (app.getId() == id) {
                found = true;
                break;
            }
        }

        if (found) {
            adminService.deleteResultInterview(id, reason);
            System.out.println("Hủy lịch phỏng vấn thành công.");
        } else {
            System.out.println("Không tìm thấy cuộc phỏng vấn với ID: " + id);
        }
    }


    public static void showInforInterviewDetail() {
        AdminServiceImp adminService = new AdminServiceImp();
        int id = inputPositiveInt("Nhập id cuộc phỏng vấn muốn xem: ");
        Application application = adminService.getApplication(id);
        if (application != null) {
            System.out.println("\n===== CHI TIẾT CUỘC PHỎNG VẤN =====");
            System.out.println("ID: " + application.getId());
            System.out.println("Candidate ID: " + application.getCandidateId());
            System.out.println("Recruitment Position ID: " + application.getRecruitmentPositionId());
            System.out.println("Tiến trình: " + application.getProgress());
            String res = application.getInterviewResult();
            System.out.println("Kết quả phỏng vấn: " + (res == null ? "Chưa có" : res));
            String reason = application.getDestroyReason();
            System.out.println("Lý do huỷ (nếu có): " + (reason == null ? "Không có" : reason));
            LocalDateTime dAt = application.getDestroyAt();
            System.out.println("Ngày huỷ (nếu có): " + (dAt == null ? "Không có" : dAt));
        } else {
            System.out.println("Không tìm thấy cuộc phỏng vấn với ID: " + id);
        }
    }

    public static void updateInterview() {
        AdminServiceImp adminService = new AdminServiceImp();
        int id = inputPositiveInt("Nhập id cuộc phỏng vấn: ");
        System.out.print("Nhập link phỏng vấn: ");
        String link;
        while (true) {
            link = scanner.nextLine().trim();
            if (link.isEmpty()) {
                System.out.println("Link phỏng vấn không được để trống.");
            } else if (!isValidURL(link)) {
                System.out.println("Link phỏng vấn không đúng định dạng.");
            } else {
                break;
            }
        }
        System.out.print("Nhập thời gian phỏng vấn (yyyy-MM-dd HH:mm): ");
        String dateInput = scanner.nextLine().trim();
        try {
            LocalDateTime dateTime = LocalDateTime.parse(
                    dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            );
            boolean success = adminService.interviewCandidate(id, link, dateTime);
            System.out.println(
                    success ? "Cập nhật thông tin phỏng vấn thành công."
                            : "Không tìm thấy cuộc phỏng vấn hoặc xảy ra lỗi."
            );
        } catch (DateTimeParseException e) {
            System.out.println("Định dạng ngày giờ không hợp lệ. Hủy thao tác.");
        }
    }

    public static void completeInterview() {
        AdminServiceImp adminService = new AdminServiceImp();
        int id = inputPositiveInt("Nhập id cuộc phỏng vấn: ");

        String note;
        while (true) {
            System.out.print("Nhập lưu ý: ");
            note = scanner.nextLine().trim();
            if (!note.isEmpty()) break;
            System.out.println("Lưu ý không được để trống.");
        }

        String result;
        while (true) {
            System.out.print("Nhập kết quả phỏng vấn (pass/fail): ");
            result = scanner.nextLine().trim().toLowerCase();
            if (result.equals("pass") || result.equals("fail")) break;
            System.out.println("Kết quả không hợp lệ. Chỉ nhận 'pass' hoặc 'fail'.");
        }

        boolean updated = adminService.updateResultInterview(id, note, result);
        System.out.println(
                updated ? "Cập nhật kết quả phỏng vấn thành công."
                        : "Không thể cập nhật kết quả. Vui lòng kiểm tra lại ID."
        );
    }

    public static boolean isValidURL(String urlString) {
        String urlPattern = "^(https?|ftp)://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,}(\\/\\S*)?$";
        return urlString.matches(urlPattern);
    }
}
