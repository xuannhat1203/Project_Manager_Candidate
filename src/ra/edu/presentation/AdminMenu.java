package ra.edu.presentation;

import ra.edu.business.model.Login;
import ra.edu.business.service.admin.AdminServiceImp;

import java.util.Scanner;

import static ra.edu.validate.validateProject.inputPositiveInt;

public class AdminMenu {
    // Mã màu ANSI
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";

    public static void adminMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(BLUE + "========== MENU QUẢN TRỊ ==========" + RESET);
            System.out.println(GREEN + "1. Quản lý Công nghệ tuyển dụng" + RESET);
            System.out.println(GREEN + "2. Quản lý Ứng viên" + RESET);
            System.out.println(GREEN + "3. Quản lý Vị trí tuyển dụng" + RESET);
            System.out.println(GREEN + "4. Quản lý Đơn ứng tuyển" + RESET);
            System.out.println(RED + "5. Đăng xuất" + RESET);
            System.out.println(BLUE + "===================================" + RESET);
            int choice = inputPositiveInt(YELLOW + "Nhập lựa chọn của bạn: " + RESET);
            switch (choice) {
                case 1:
                    TechMenu.techMenu();
                    break;
                case 2:
                    CandidateMenu.candidateMenu();
                    break;
                case 3:
                    PositionMenu.positionMenu();
                    break;
                case 4:
                    ApplicationMenu.applicationMenu();
                    break;
                case 5:
                    AdminServiceImp adminServiceImp = new AdminServiceImp();
                    Login account = adminServiceImp.getIsCheckLogin();
                    System.out.println(CYAN + "Đăng xuất thành công" + RESET);
                    adminServiceImp.deleteIsLogin(account.getEmail());
                    System.exit(0);
                    break;
                default:
                    System.out.println(RED + "Lựa chọn không hợp lệ" + RESET);
                    break;
            }
        }
    }
}
