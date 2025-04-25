package ra.edu.presentation;

import ra.edu.business.model.Candidate;
import ra.edu.business.service.admin.AdminServiceImp;
import ra.edu.validate.validateProject;

import java.util.*;

import static ra.edu.validate.validateProject.inputPositiveInt;

public class CandidateMenu {
    private static final AdminServiceImp adminServiceImp = new AdminServiceImp();
    private static final Scanner sc = new Scanner(System.in);

    public static void candidateMenu() {
        while (true) {
            System.out.println("\n========== QUẢN LÝ ỨNG VIÊN ==========");
            System.out.println("1. Hiển thị danh sách ứng viên");
            System.out.println("2. Khóa/Mở khóa tài khoản ứng viên");
            System.out.println("3. Reset mật khẩu ứng viên");
            System.out.println("4. Tìm kiếm theo tên");
            System.out.println("5. Lọc theo kinh nghiệm, tuổi, giới tính, công nghệ");
            System.out.println("6. Quay lại menu chính");
            System.out.print("Nhập lựa chọn: ");
            int choice = inputInt(1, 6);
            if (choice == 1) {
                displayCandidateList();
            } else if (choice == 2) {
                changeStatus();
            } else if (choice == 3) {
                resetPassword();
            } else if (choice == 4) {
                searchByName();
            } else if (choice == 5) {
                filterMenu();
            } else {
                break;
            }
        }
    }

    private static void displayCandidateList() {
        int page = 1;
        int size = 5;

        while (true) {
            List<Candidate> candidates = adminServiceImp.listCandidatePagination(page, size);
            candidates.sort(Comparator.comparing(Candidate::getName));
            if (candidates.isEmpty()) {
                System.out.println("Không có ứng viên nào.");
            } else {
                System.out.println("\n========== Trang " + page + " ==========");
                System.out.printf("%-5s | %-20s | %-25s | %-15s | %-10s\n",
                        "ID", "Họ và tên", "Email", "SĐT", "Trạng thái");
                System.out.println("--------------------------------------------------------------------------");

                for (Candidate candidate : candidates) {
                    if (candidate != null) {
                        System.out.printf("%-5d | %-20s | %-25s | %-15s | %-10s\n",
                                candidate.getId(),
                                candidate.getName(),
                                candidate.getEmail(),
                                candidate.getPhone(),
                                candidate.getStatus());
                    }
                }

                System.out.println("--------------------------------------------------------------------------");
            }

            System.out.println("\n[1] Trang sau | [2] Trang trước | [3] Thoát");
            int option = validateProject.inputPositiveInt("Nhập lựa chọn của bạn: ");
            if (option == 1) {
                page++;
            } else if (option == 2) {
                if (page > 1) {
                    page--;
                } else {
                    System.out.println("Đang ở trang đầu.");
                }
            } else if (option == 3) {
                break;
            } else {
                System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }


    private static void changeStatus() {
        int id = validateProject.inputPositiveInt("Nhập ID ứng viên cần đổi trạng thái: ");
        boolean result = adminServiceImp.changeStatusOfCandidate(id);
        System.out.println(result ? "Đổi trạng thái thành công." : "Không tìm thấy ứng viên.");
    }

    private static void resetPassword() {
        List<Candidate> candidates = adminServiceImp.listCandidate();
        int id = validateProject.inputPositiveInt("Nhập ID ứng viên cần reset mật khẩu: ");
        Optional<Candidate> candidate = candidates.stream().filter(c -> c.getId() == id).findFirst();
        if (candidate.isPresent()) {
            boolean result = adminServiceImp.resetPassword(id);
            if (result) {
                System.out.println("Reset mật khẩu thành công");
            }
        }else{
            System.out.println("Không tìm thấy ứng viên");
        }
    }

    private static void searchByName() {
        System.out.print("Nhập tên ứng viên cần tìm: ");
        String name = inputLimitedString(50);
        List<Candidate> result = adminServiceImp.findCandidate(name);
        if (result.isEmpty()) {
            System.out.println("Không tìm thấy ứng viên nào.");
        } else {
            paginateResult(result);
        }
    }

    private static void filterMenu() {
        while (true) {
            System.out.println("\n*** Lọc ứng viên ***");
            System.out.println("1. Theo kinh nghiệm");
            System.out.println("2. Theo độ tuổi");
            System.out.println("3. Theo giới tính");
            System.out.println("4. Theo công nghệ");
            System.out.println("5. Quay lại");
            System.out.print("Chọn bộ lọc: ");
            int option = inputInt(1, 5);
            List<Candidate> result = new ArrayList<>();

            if (option == 1) {
                int min = inputValidatedRange("Nhập kinh nghiệm tối thiểu: ", 0, 40);
                int max = inputValidatedRange("Nhập kinh nghiệm tối đa: ", min, 40);
                result = adminServiceImp.filterExperience(min, max);
            } else if (option == 2) {
                int min = inputValidatedRange("Nhập tuổi tối thiểu: ", 18, 60);
                int max = inputValidatedRange("Nhập tuổi tối đa: ", min, 60);
                result = adminServiceImp.filterAge(min, max);
            } else if (option == 3) {
                System.out.print("Nhập giới tính (Nam/Nữ): ");
                String gender = inputGender();
                result = adminServiceImp.filterGender(gender);
            } else if (option == 4) {
                System.out.print("Nhập công nghệ: ");
                String tech = inputLimitedString(30);
                result = adminServiceImp.filterTechnology(tech);
            } else {
                break;
            }

            if (result == null || result.isEmpty()) {
                System.out.println("Không tìm thấy kết quả phù hợp.");
            } else {
                paginateResult(result);
            }
        }
    }

    private static void paginateResult(List<Candidate> candidates) {
        int page = 1;
        int size = 5;
        int totalPage = (int) Math.ceil((double) candidates.size() / size);
        while (true) {
            int start = (page - 1) * size;
            int end = Math.min(start + size, candidates.size());
            System.out.println("\n--- Trang " + page + "/" + totalPage + " ---");
            for (int i = start; i < end; i++) {
                System.out.println(candidates.get(i));
            }

            System.out.println("\n[1] Trang sau | [2] Trang trước | [3] Thoát");
            System.out.print("Chọn hành động: ");
            String option = sc.nextLine();
            if (option.equals("1")) {
                if (page < totalPage) {
                    page++;
                } else {
                    System.out.println("Đang ở trang cuối.");
                }
            } else if (option.equals("2")) {
                if (page > 1) {
                    page--;
                } else {
                    System.out.println("Đang ở trang đầu.");
                }
            } else if (option.equals("3")) {
                break;
            } else {
                System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }
    private static int inputInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.print("Vui lòng nhập số nguyên hợp lệ: ");
            }
        }
    }

    private static int inputInt(int min, int max) {
        while (true) {
            int value = inputInt();
            if (value >= min && value <= max) {
                return value;
            } else {
                System.out.printf("Vui lòng nhập trong khoảng [%d - %d]: ", min, max);
            }
        }
    }

    private static int inputPositiveInt() {
        while (true) {
            int value = inputInt();
            if (value > 0) {
                return value;
            } else {
                System.out.print("Giá trị phải lớn hơn 0: ");
            }
        }
    }

    private static String inputNonEmptyString() {
        while (true) {
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.print("Không được để trống. Nhập lại: ");
            }
        }
    }
    private static String inputLimitedString(int maxLength) {
        while (true) {
            String input = inputNonEmptyString();
            if (input.length() <= maxLength) {
                return input;
            } else {
                System.out.printf("Không vượt quá %d ký tự. Nhập lại: ", maxLength);
            }
        }
    }
    private static String inputGender() {
        while (true) {
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("nam") || input.equals("nữ")) {
                return input.substring(0, 1).toUpperCase() + input.substring(1);
            } else {
                System.out.print("Giới tính không hợp lệ (chỉ Nam/Nữ): ");
            }
        }
    }
    private static int inputValidatedRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            int value = inputInt();
            if (value >= min && value <= max) {
                return value;
            } else {
                System.out.printf("Nhập số trong khoảng [%d - %d].\n", min, max);
            }
        }
    }
}
