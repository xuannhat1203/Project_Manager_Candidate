package ra.edu.business.service.admin;

import ra.edu.business.dao.admin.AdminDao;
import ra.edu.business.dao.admin.AdminDaoImp;
import ra.edu.business.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class AdminServiceImp implements AdminService {
    public final AdminDao adminDao;
    public AdminServiceImp() {
        adminDao = new AdminDaoImp();
    }
    @Override
    public AccountAdmin getAccountAdmin() {
        return adminDao.getAccountAdmin();
    }
    @Override
    public List<Candidate> listCandidate() {
        return adminDao.listCandidate();
    }
    @Override
    public List<Candidate> listCandidatePagination(int pageNumber, int pageSize) {
        return adminDao.listCandidatePagination(pageNumber, pageSize);
    }

    @Override
    public boolean changeStatusOfCandidate(int id) {
        return adminDao.changeStatusOfCandidate(id);
    }

    @Override
    public boolean resetPassword(int id) {
        return adminDao.resetPassword(id);
    }

    @Override
    public List<Candidate> findCandidate(String candidate_name) {
        return adminDao.findCandidate(candidate_name);
    }

    @Override
    public List<Candidate> filterExperience(int min, int max) {
        return adminDao.filterExperience(min, max);
    }

    @Override
    public List<Candidate> filterAge(int min, int max) {
        return adminDao.filterAge(min, max);
    }

    @Override
    public List<Candidate> filterGender(String gender_filter) {
       return adminDao.filterGender(gender_filter);
    }

    @Override
    public List<Candidate> filterTechnology(String technology_filter) {
       return adminDao.filterTechnology(technology_filter);
    }

    @Override
    public boolean addNewPosition(String name,
                                  String description,
                                  BigDecimal minSalary,
                                  BigDecimal maxSalary,
                                  int minExperience,
                                  LocalDate expiredDate,
                                  String technologyIds) {
        return adminDao.addNewPosition(name, description, minSalary, maxSalary, minExperience, expiredDate, technologyIds);
    }

    @Override
    public boolean updatePosition(int id, String name, String description, BigDecimal minSalary, BigDecimal maxSalary, int minExperience, LocalDate expiredDate) {
        return adminDao.updatePosition(id, name, description, minSalary, maxSalary, minExperience, expiredDate);
    }

    @Override
    public boolean deletePosition(int id) {
        return adminDao.deletePosition(id);
    }

    @Override
    public List<RecruitmentPosition> listPosition(int pageNumber, int pageSize) {
        return adminDao.listPosition(pageNumber, pageSize);
    }

    @Override
    public List<RecruitmentPosition> listRecruitmentPosition() {
        return adminDao.listRecruitmentPosition();
    }

    @Override
    public List<Application> listApplication(int pageNumber, int pageSize) {
        return adminDao.listApplication(pageNumber, pageSize);
    }

    @Override
    public List<Application> listApplication() {
        return adminDao.listApplication();
    }

    @Override
    public List<Application> listFilterByProcess(String process) {
       return adminDao.listFilterByProcess(process);
    }

    @Override
    public List<Application> listApplicationByResult(String result) {
        return adminDao.listApplicationByResult(result);
    }

    @Override
    public Application getApplication(int id) {
        return adminDao.getApplication(id);
    }

    @Override
    public boolean interviewCandidate(int id, String link, LocalDateTime interviewDateTime) {
        return adminDao.interviewCandidate(id, link, interviewDateTime);
    }

    @Override
    public boolean updateResultInterview(int id, String note, String result) {
        return adminDao.updateResultInterview(id, note, result);
    }

    @Override
    public boolean deleteResultInterview(int id, String result) {
        return adminDao.deleteResultInterview(id,result);
    }

    @Override
    public String getStatusAccount(String email) {
        return adminDao.getStatusAccount(email);
    }

    @Override
    public boolean insertIsLogin(String email, String password) {
        return adminDao.insertIsLogin(email, password);
    }

    @Override
    public Login getIsCheckLogin() {
        return adminDao.getIsCheckLogin();
    }

    @Override
    public boolean deleteIsLogin(String email) {
        return adminDao.deleteIsLogin(email);
    }

    @Override
    public boolean addNewCandidate(String name, String email, String password, String phone, int experience, String gender, String description, Date dob) {
        return adminDao.addNewCandidate(name, email, password, phone, experience, gender, description, dob);
    }

    @Override
    public List<Technology> listTechnologyPagination(int pageNumber, int pageSize) {
        return adminDao.listTechnologyPagination(pageNumber, pageSize);
    }

    @Override
    public boolean addTechnology(String name) {
        return adminDao.addTechnology(name);
    }

    @Override
    public boolean updateTechnology(int id, String name) {
        return adminDao.updateTechnology(id, name);
    }

    @Override
    public boolean deleteTechnology(int id) {
        return adminDao.deleteTechnology(id);
    }

    @Override
    public List<Technology> listTechnology() {
        return adminDao.listTechnology();
    }
}
