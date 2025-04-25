package ra.edu;

import ra.edu.business.model.AccountAdmin;
import ra.edu.business.model.Candidate;
import ra.edu.business.model.Login;
import ra.edu.business.service.admin.AdminServiceImp;
import ra.edu.presentation.AdminMenu;
import ra.edu.presentation.CandidateMainMenu;
import ra.edu.validate.validateLogin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

import static ra.edu.validate.validateProject.inputPositiveInt;

public class MainApplication {
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        if (!checkLogin()) {
            while (true) {
                System.out.println("========== HỆ THỐNG TUYỂN DỤNG ==========");
                System.out.println("1. Đăng nhập");
                System.out.println("2. Đăng ký tài khoản Ứng viên");
                System.out.println("3. Thoát");
                System.out.println("==========================================");
                int choice = inputPositiveInt("Nhập lựa chọn của bạn: ");
                switch (choice) {
                    case 1:
                        loginApplication();
                        break;
                    case 2:
                        registerCandidate();
                        break;
                    case 3:
                        System.out.println("Thoát chương trình");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ");
                        break;
                }
            }
        }
    }
    public static void loginApplication() {
        AdminServiceImp adminServiceImp = new AdminServiceImp();
        String email = validateLogin.validateEmail("Nhập email: ");
        String password = validateLogin.validatePassword("Password: ");
        AccountAdmin admin = adminServiceImp.getAccountAdmin();

        if (admin != null && admin.getEmail().equals(email)) {
            if (checkPassword(password, admin.getPassword(), admin.getEmail())) {
                if (adminServiceImp.insertIsLogin(email, password)) {
                    System.out.println("Đăng nhập thành công");
                    AdminMenu.adminMenu();
                } else {
                    System.out.println("Lỗi không xác định");
                }
            } else {
                System.out.println("Mật khẩu không đúng hoặc tài khoản bị khóa");
            }
            return;
        }
        List<Candidate> listCandidate = adminServiceImp.listCandidate();
        if (listCandidate != null) {
            Optional<Candidate> isCheckLogin = listCandidate.stream()
                    .filter(c -> c.getEmail().equals(email))
                    .findFirst();

            if (isCheckLogin.isPresent()) {
                Candidate candidate = isCheckLogin.get();
                if (checkPassword(password, candidate.getPassword(), candidate.getEmail())) {
                    if (adminServiceImp.insertIsLogin(email, password)) {
                        System.out.println("Đăng nhập thành công");
                        CandidateMainMenu.candidateMainMenu();
                    } else {
                        System.out.println("Lỗi không xác định");
                    }
                } else {
                    System.out.println("Mật khẩu không đúng hoặc tài khoản bị khóa");
                }
            } else {
                System.out.println("Tài khoản không tồn tại");
            }
        }
    }
    public static boolean checkLogin() {
        AdminServiceImp adminServiceImp = new AdminServiceImp();
        Login login = adminServiceImp.getIsCheckLogin();
        if (login == null) {
            return false;
        }
        String emailLogin = login.getEmail();
        AccountAdmin admin = adminServiceImp.getAccountAdmin();
        if (admin != null && admin.getEmail().equals(emailLogin)) {
            System.out.println("Đăng nhập với tư cách quản trị viên");
            AdminMenu.adminMenu();
            return true;
        }
        List<Candidate> candidates = adminServiceImp.listCandidate();
        Optional<Candidate> candidate = candidates.stream()
                .filter(c -> c.getEmail().equals(emailLogin))
                .findFirst();

        if (candidate.isPresent()) {
            System.out.println("Đăng nhập với tư cách ứng viên");
            CandidateMainMenu.candidateMainMenu();
            return true;
        }
        return false;
    }
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi mã hóa mật khẩu", e);
        }
    }
    public static boolean checkPassword(String password, String hashedPassword,String email) {
        AdminServiceImp adminService = new AdminServiceImp();
        String hashedInputPassword = hashPassword(password);
        if (hashedPassword.equals(hashedInputPassword) && Objects.equals(adminService.getStatusAccount(email), "active")){
            return true;
        };
        return false;
    }
    public static void registerCandidate() {
        Candidate candidate = new Candidate();
        System.out.println("Nhập thông tin ứng viên");
        candidate.inputData2();
        AdminServiceImp adminServiceImp = new AdminServiceImp();
        boolean isCheck = adminServiceImp.addNewCandidate(candidate.getName(),candidate.getEmail(), candidate.getPassword(), candidate.getPhone(), candidate.getExperience(), candidate.getGender(), candidate.getDescription(), candidate.getDob()) ;
        if (isCheck) {
            System.out.println("Đăng kí tài khoản thành công");
        }else {
            System.out.println("Đăng kí tài khoản không thành công");
        }
    }

}
