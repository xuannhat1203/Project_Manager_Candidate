package ra.edu.presentation;
import ra.edu.business.model.Candidate;
import ra.edu.business.model.Login;
import ra.edu.business.service.admin.AdminServiceImp;
import ra.edu.business.service.candidate.CandidateServiceImp;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static ra.edu.validate.validateCandidate.*;
import static ra.edu.validate.validateLogin.validateEmail;

public class PersonalInfoMenu {
    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String EMAIL_SMTP_HOST = "smtp.gmail.com";
    private static final int EMAIL_SMTP_PORT = 587;
    private static final String FROM_EMAIL = "xuannhatvn211@gmail.com";
    private static final String EMAIL_PASSWORD = "zman voae ljju krgd";

    public static int getMyInfor() {
        AdminServiceImp adminService = new AdminServiceImp();
        CandidateServiceImp candidateService = new CandidateServiceImp();
        Login login = adminService.getIsCheckLogin();
        if (login == null) {
            System.out.println("Không tìm thấy thông tin đăng nhập.");
            return -1;
        }

        Candidate candidate = candidateService.getCandidate(login.getEmail());
        if (candidate == null) {
            System.out.println("Không tìm thấy ứng viên.");
            return -1;
        }
        return candidate.getId();
    }

    public static void personalInfoMenu() {
        AdminServiceImp adminService = new AdminServiceImp();
        CandidateServiceImp candidateService = new CandidateServiceImp();
        Login login = adminService.getIsCheckLogin();
        Candidate candidate = new Candidate();
        if (login != null) {
            candidate = candidateService.getCandidate(login.getEmail());
        }
        System.out.println("Họ và tên: "+candidate.getName());
        System.out.println("Email: "+candidate.getEmail());
        System.out.println("Ngày sinh: "+candidate.getDob());
        System.out.println("Năm kinh nghiệm: "+candidate.getExperience());
        System.out.println("Số điện thoại: "+candidate.getPhone());
        System.out.println("Mô tả bản thân: "+candidate.getDescription());
        System.out.println("Giới tính: "+candidate.getGender());
        while (true) {
            System.out.println("******* Quản lý Thông tin cá nhân *******");
            System.out.println("1. Cập nhật thông tin cá nhân");
            System.out.println("2. Đổi mật khẩu (gửi OTP qua email)");
            System.out.println("3. Quay lại menu trước");
            System.out.print("Nhập lựa chọn: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    updateProfile();
                    break;
                case "2":
                    changePasswordFlow();
                    break;
                case "3":
                    return;
                default:
                    System.out.println(">> Lựa chọn không hợp lệ!");
            }
        }
    }
    public static void updateProfile() {
        CandidateServiceImp candidateService = new CandidateServiceImp();
        int id = getMyInfor();
        if (id == -1) return;

        System.out.println("\n--- CẬP NHẬT THÔNG TIN CÁ NHÂN ---");
        String name = validateName("Nhập tên mới: ");
        String email = validateEmail("Nhập email mới: ");
        String phone = validatePhone("Nhập số điện thoại mới: ");
        int experience = validateExperience("Nhập số năm kinh nghiệm mới: ");
        String gender = validateGender("Nhập giới tính: ");

        if (candidateService.updateProfile(id, name, email, phone, experience, gender)) {
            System.out.println("Cập nhật thông tin thành công");
        } else {
            System.out.println("Cập nhật thông tin thất bại");
        }
    }

    public static void changePasswordFlow() {
        AdminServiceImp adminService = new AdminServiceImp();
        CandidateServiceImp candidateService = new CandidateServiceImp();
        Login login = adminService.getIsCheckLogin();

        if (login == null) {
            System.out.println("Bạn chưa đăng nhập.");
            return;
        }

        String email = login.getEmail();
        String otp = generateOtp();
        if (!sendOtpEmail(email, otp)) {
            System.out.println("Gửi OTP thất bại. Vui lòng thử lại sau.");
            return;
        }

        System.out.print("Nhập mã OTP được gửi đến email của bạn: ");
        String enteredOtp = sc.nextLine().trim();
        if (!otp.equals(enteredOtp)) {
            System.out.println("Mã OTP không đúng.");
            return;
        }

        String newPassword = validatePassword("Nhập mật khẩu mới: ");
        int id = getMyInfor();
        if (id == -1) return;

        if (candidateService.changeMyPassword(id, newPassword)) {
            System.out.println("Đổi mật khẩu thành công.");
        } else {
            System.out.println("Đổi mật khẩu thất bại.");
        }
    }

    private static String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private static boolean sendOtpEmail(String toEmail, String otp) {
        Properties props = new Properties();
        props.put("mail.smtp.host", EMAIL_SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(EMAIL_SMTP_PORT));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã OTP thay đổi mật khẩu");
            message.setText("Xin chào,\n\nMã OTP của bạn là: " + otp + "\n\nVui lòng không chia sẻ mã này với bất kỳ ai.");
            Transport.send(message);
            System.out.println("Gửi email thành công.");
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
