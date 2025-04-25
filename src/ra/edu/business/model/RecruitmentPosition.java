package ra.edu.business.model;

import ra.edu.validate.JobPositionValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class RecruitmentPosition {
    private int id;
    private String name;
    private String description;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private int minExperience;
    private LocalDate createdDate;
    private LocalDate expiredDate;
    private String technologyIds; // ✅ Thêm thuộc tính mới

    public RecruitmentPosition() {
    }

    public RecruitmentPosition(String name, String description, BigDecimal minSalary,
                               BigDecimal maxSalary, int minExperience, LocalDate createdDate,
                               LocalDate expiredDate, String technologyIds) {
        this.name = name;
        this.description = description;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.minExperience = minExperience;
        this.createdDate = createdDate;
        this.expiredDate = expiredDate;
        this.technologyIds = technologyIds;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    public int getMinExperience() {
        return minExperience;
    }

    public void setMinExperience(int minExperience) {
        this.minExperience = minExperience;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(LocalDate expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getTechnologyIds() {
        return technologyIds;
    }

    public void setTechnologyIds(String technologyIds) {
        this.technologyIds = technologyIds;
    }

    public void inputData() {
        Scanner sc = new Scanner(System.in);
        this.name = JobPositionValidator.validatePositionName("Nhập tên vị trí: ");
        this.description = JobPositionValidator.validateStringInput("Nhập mô tả: ");
        this.minSalary = JobPositionValidator.validatePositioMinSalary("Nhập mức lương tối thiểu: ");
        this.maxSalary = JobPositionValidator.validatePositionMaxSalary("Nhập mức lương tối đa: ", minSalary);
        this.minExperience = JobPositionValidator.validatePositiveInt("Nhập năm kinh nghiệm: ");
        this.createdDate = LocalDate.now();
        this.expiredDate = JobPositionValidator.validateExpiredDate("Nhập ngày hết hạn (yyyy-MM-dd): ", createdDate);
        this.technologyIds = JobPositionValidator.validateStringInput("Nhập danh sách công nghệ (VD: (1,1),(1,2)): ");
    }


    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", minSalary=" + minSalary +
                ", maxSalary=" + maxSalary +
                ", minExperience=" + minExperience +
                ", createdDate=" + createdDate +
                ", expiredDate=" + expiredDate +
                ", technologyIds='" + technologyIds + '\'';
    }
}
