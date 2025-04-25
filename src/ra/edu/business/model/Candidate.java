package ra.edu.business.model;

import ra.edu.validate.validateCandidate;
import ra.edu.validate.validateLogin;

import java.sql.Date;

public class Candidate {
    private static int autoincrement = 0;
    private int id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private int experience;
    private String gender;
    private String status;
    private String description;
    private Date dob;

    public Candidate() {
    }

    public Candidate(String name, String email, String password, String phone, int experience, String gender, String status, String description, java.sql.Date dob) {
        this.id = ++autoincrement;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.experience = experience;
        this.gender = gender;
        this.status = status;
        this.description = description;
        this.dob = dob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
    public void inputData() {
        String email = validateCandidate.validateEmail("Nhập email: ");
        setEmail(email);
        String name = validateCandidate.validateName("Nhập tên: ");
        setName(name);
        String password = validateLogin.validatePassword("Nhập mật khẩu: ");
        setPassword(password);
        String phone = validateCandidate.validatePhone("Nhập số điện thoại: ");
        setPhone(phone);
        int exp = validateCandidate.validateExperience("Nhập số năm kinh nghiệm: ");
        setExperience(exp);
        String gender = validateCandidate.validateGender("Nhập giới tính (Nam/Nữ): ");
        setGender(gender);
        String status = String.valueOf(validateCandidate.validateStatus("Nhập trạng thái (Active/Inactive): "));
        setStatus(status);
        String description = validateCandidate.validateDescription("Nhập mô tả bản thân: ");
        setDescription(description);
        Date dob = Date.valueOf(validateCandidate.validateDob("Nhập ngày sinh (yyyy-MM-dd): "));
        setDob(dob);
    }
    public void inputData2() {
        String email = validateCandidate.validateEmail("Nhập email: ");
        setEmail(email);
        String name = validateCandidate.validateName("Nhập tên: ");
        setName(name);
        String password = validateLogin.validatePassWordRegister("Nhập mật khẩu: ");
        setPassword(password);
        String phone = validateCandidate.validatePhone("Nhập số điện thoại: ");
        setPhone(phone);
        int exp = validateCandidate.validateExperience("Nhập số năm kinh nghiệm: ");
        setExperience(exp);
        String gender = validateCandidate.validateGender("Nhập giới tính (Nam/Nữ): ");
        setGender(gender);
        String status = String.valueOf(validateCandidate.validateStatus("Nhập trạng thái (Active/Inactive): "));
        setStatus(status);
        String description = validateCandidate.validateDescription("Nhập mô tả bản thân: ");
        setDescription(description);
        Date dob = Date.valueOf(validateCandidate.validateDob("Nhập ngày sinh (yyyy-MM-dd): "));
        setDob(dob);
    }
    @Override
    public String toString() {
        return String.format("+-------------------+-------------------------------------------------+\n" +
                        "| %-17s | %-47s |\n" +
                        "| %-17s | %-47s |\n" +
                        "| %-17s | %-47s |\n" +
                        "| %-17s | %-47s |\n" +
                        "| %-17s | %-47d |\n" +
                        "| %-17s | %-47s |\n" +
                        "| %-17s | %-47s |\n" +
                        "| %-17s | %-47s |\n" +
                        "| %-17s | %-47s |\n" +
                        "+-------------------+-------------------------------------------------+\n",
                "ID", String.valueOf(id),
                "Name", name,
                "Email", email,
                "Phone", phone,
                "Experience", experience,
                "Gender", gender,
                "Status", status,
                "Description", description,
                "DOB", dob != null ? dob.toString() : "N/A");
    }

}

