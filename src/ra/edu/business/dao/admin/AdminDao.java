package ra.edu.business.dao.admin;

import ra.edu.business.model.*;

import javax.swing.text.Position;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface AdminDao {
    AccountAdmin getAccountAdmin();

    List<Candidate> listCandidate();

    boolean insertIsLogin(String email, String password);

    Login getIsCheckLogin();

    boolean deleteIsLogin(String email);

    boolean addNewCandidate(String name, String email, String password, String phone, int experience, String gender, String description, Date dob);

    List<Technology> listTechnologyPagination(int pageNumber, int pageSize);

    boolean addTechnology(String name);

    boolean updateTechnology(int id, String name);

    boolean deleteTechnology(int id);

    List<Technology> listTechnology();

    List<Candidate> listCandidatePagination(int pageNumber, int pageSize);

    boolean changeStatusOfCandidate(int id);

    boolean resetPassword(int id);

    List<Candidate> findCandidate(String candidate_name);

    List<Candidate> filterExperience(int min, int max);

    List<Candidate> filterAge(int min, int max);

    List<Candidate> filterGender(String gender_filter);

    List<Candidate> filterTechnology(String technology_filter);

    boolean addNewPosition(String name,
                           String description,
                           BigDecimal minSalary,
                           BigDecimal maxSalary,
                           int minExperience,
                           LocalDate expiredDate,
                           String technologyIds);

    boolean updatePosition(int id, String name, String description, BigDecimal minSalary, BigDecimal maxSalary, int minExperience, LocalDate expiredDate);

    boolean deletePosition(int id);

    List<RecruitmentPosition> listPosition(int pageNumber, int pageSize);
    List<RecruitmentPosition> listRecruitmentPosition();
    List<Application> listApplication(int pageNumber, int pageSize);
    List<Application> listApplication();
    List<Application> listFilterByProcess(String process);
    List<Application> listApplicationByResult(String result);
    Application getApplication(int id);
    boolean interviewCandidate(int id, String link, LocalDateTime interviewDateTime);
    boolean updateResultInterview(int id, String note, String result);
    boolean deleteResultInterview(int id,String result);
    String getStatusAccount(String email);
}
