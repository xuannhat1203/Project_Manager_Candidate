package ra.edu.business.dao.admin;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminDaoImp implements AdminDao {

    @Override
    public AccountAdmin getAccountAdmin() {
        Connection con = null;
        CallableStatement cstmt = null;
        AccountAdmin admin = null;
        try {
            con = ConnectionDB.openConnection();
            cstmt = con.prepareCall("{call get_account_admin()}");
            ResultSet rs = cstmt.executeQuery();
            while (rs.next()) {
                admin = new AccountAdmin(
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
            return admin;
        }catch (SQLException sql) {
            System.out.println(sql.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return admin;
    }
    @Override
    public List<Candidate> listCandidate() {
        Connection con = null;
        CallableStatement cstmt = null;
        List<Candidate> listCandidate = new ArrayList<>();
        try {
            con = ConnectionDB.openConnection();
            cstmt = con.prepareCall("{call get_list_candidate()}");
            ResultSet rs = cstmt.executeQuery();
            while (rs.next()) {
                Candidate c = new Candidate();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setEmail(rs.getString("email"));
                c.setPassword(rs.getString("password"));
                c.setPhone(rs.getString("phone"));
                c.setExperience(rs.getInt("experience"));
                c.setGender(rs.getString("gender"));
                c.setStatus(rs.getString("status"));
                c.setDescription(rs.getString("description"));
                c.setDob(rs.getDate("dob"));
                listCandidate.add(c);
            }
            return listCandidate;
        }catch (SQLException sql) {
            System.out.println(sql.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return listCandidate;
    }
    @Override
    public boolean insertIsLogin(String email, String password) {
        Connection con = null;
        CallableStatement cstmt = null;
        try {
            con = ConnectionDB.openConnection();
            cstmt = con.prepareCall("{call insert_into_isLogin(?, ?)}");
            cstmt.setString(1, email);
            cstmt.setString(2, password);
            cstmt.executeUpdate();
            return true;
        }catch (SQLException sql) {
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Login getIsCheckLogin() {
        Connection con = null;
        CallableStatement cstmt = null;
        Login login = null;
        try {
            con = ConnectionDB.openConnection();
            cstmt = con.prepareCall("{call get_is_check_login()}");
            ResultSet rs = cstmt.executeQuery();
            while (rs.next()) {
                login = new Login();
                login.setEmail(rs.getString("email"));
                login.setPassword(rs.getString("password"));
            }
            return login;
        }catch (SQLException sql){
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return login;
    }

    @Override
    public boolean deleteIsLogin(String email) {
        Connection con = null;
        CallableStatement cstmt = null;
        try {
            con = ConnectionDB.openConnection();
            cstmt = con.prepareCall("{call delete_is_check_login(?)}");
            cstmt.setString(1, email);
            cstmt.executeUpdate();
            return true;
        }catch (SQLException sql) {
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean addNewCandidate(String name, String email, String password, String phone, int experience, String gender, String description, Date dob) {
        Connection con = null;
        CallableStatement cstmt = null;
        try {
            con = ConnectionDB.openConnection();
            cstmt = con.prepareCall("{call register_candidate(?,?,?,?,?,?,?,?)}");
            cstmt.setString(1, name);
            cstmt.setString(2, email);
            cstmt.setString(3, password);
            cstmt.setString(4, phone);
            cstmt.setInt(5, experience);
            cstmt.setString(6, gender);
            cstmt.setString(7, description);
            cstmt.setDate(8, (java.sql.Date) dob);
            cstmt.executeUpdate();
            return true;
        }catch (SQLException sql){
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public List<Technology> listTechnologyPagination(int pageNumber, int pageSize) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Technology> listTechnology = new ArrayList<>();
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call get_technology_pagination(?, ?)}");
            cs.setInt(1, pageNumber);
            cs.setInt(2, pageSize);
            rs = cs.executeQuery();
            while (rs.next()) {
                Technology technology = new Technology();
                technology.setId(rs.getInt("id"));
                technology.setName(rs.getString("name"));
                listTechnology.add(technology);
            }
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return listTechnology;
    }
    @Override
    public boolean addTechnology(String name) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call add_technology(?)}");
            cs.setString(1, name);
            cs.execute();
            return true;
        }catch (SQLException sql){
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateTechnology(int id,String name) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call update_technology(?,?)}");
            cs.setInt(1, id);
            cs.setString(2, name);
            cs.execute();
            return true;
        }catch (SQLException sql){
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteTechnology(int id) {
        Connection con = null;
        CallableStatement cs = null;
        try{
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call delete_technology(?)}");
            cs.setInt(1, id);
            cs.execute();
            return true;
        }catch (SQLException sql){
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    } @Override
    public List<Technology> listTechnology() {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Technology> listTechnology = new ArrayList<>();
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call get_all_technology()}");
            rs = cs.executeQuery();
            while (rs.next()) {
                Technology technology = new Technology();
                technology.setId(rs.getInt("id"));
                technology.setName(rs.getString("name"));
                listTechnology.add(technology);
            }
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return listTechnology;
    }
    @Override
    public List<Candidate> listCandidatePagination(int pageNumber, int pageSize) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Candidate> listCandidate = new ArrayList<>();
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call get_candidate_pagination(?, ?)}");
            cs.setInt(1, pageNumber);
            cs.setInt(2, pageSize);
            rs = cs.executeQuery();
            while (rs.next()) {
                Candidate c = new Candidate();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setEmail(rs.getString("email"));
                c.setPassword(rs.getString("password"));
                c.setPhone(rs.getString("phone"));
                c.setExperience(rs.getInt("experience"));
                c.setGender(rs.getString("gender"));
                c.setStatus(rs.getString("status"));
                c.setDescription(rs.getString("description"));
                c.setDob(rs.getDate("dob"));
                listCandidate.add(c);
            }
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return listCandidate;
    }

    @Override
    public boolean changeStatusOfCandidate(int id) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call changeStatusAccountCandidate(?)}");
            cs.setInt(1, id);
            int rowsAffected = cs.executeUpdate();
            return rowsAffected > 0;
        }catch (SQLException sql) {
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean resetPassword(int id) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call resetpassword(?)}");
            cs.setInt(1, id);
            int rowsAffected = cs.executeUpdate();
            return rowsAffected > 0;
        }catch (SQLException sql) {
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public List<Candidate> findCandidate(String candidate_name) {
        Connection con = null;
        CallableStatement cs = null;
        List<Candidate> listCandidate = new ArrayList<>();
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call findCandidateByName(?)}");
            cs.setString(1, candidate_name);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Candidate c = new Candidate();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setEmail(rs.getString("email"));
                c.setPassword(rs.getString("password"));
                c.setPhone(rs.getString("phone"));
                c.setExperience(rs.getInt("experience"));
                c.setGender(rs.getString("gender"));
                c.setStatus(rs.getString("status"));
                c.setDescription(rs.getString("description"));
                c.setDob(rs.getDate("dob"));
                listCandidate.add(c);
            }
            return listCandidate;
        }catch (SQLException sql) {
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return listCandidate;
    }

    @Override
    public List<Candidate> filterExperience(int min, int max) {
        Connection con = null;
        CallableStatement cs = null;
        List<Candidate> listCandidate = new ArrayList<>();
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call filterToExperience(?, ?)}");
            cs.setInt(1, min);
            cs.setInt(2, max);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Candidate c = new Candidate();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setEmail(rs.getString("email"));
                c.setPassword(rs.getString("password"));
                c.setPhone(rs.getString("phone"));
                c.setExperience(rs.getInt("experience"));
                c.setGender(rs.getString("gender"));
                c.setStatus(rs.getString("status"));
                c.setDescription(rs.getString("description"));
                c.setDob(rs.getDate("dob"));
                listCandidate.add(c);
            }
            return listCandidate;
        }catch (SQLException sql) {
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return listCandidate;
    }

    @Override
    public List<Candidate> filterAge(int min, int max) {
        Connection con = null;
        CallableStatement cs = null;
        List<Candidate> listCandidate = new ArrayList<>();
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call filterToAge(?, ?)}");
            cs.setInt(1, min);
            cs.setInt(2, max);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Candidate c = new Candidate();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setEmail(rs.getString("email"));
                c.setPassword(rs.getString("password"));
                c.setPhone(rs.getString("phone"));
                c.setExperience(rs.getInt("experience"));
                c.setGender(rs.getString("gender"));
                c.setStatus(rs.getString("status"));
                c.setDescription(rs.getString("description"));
                c.setDob(rs.getDate("dob"));
                listCandidate.add(c);
            }
            return listCandidate;
        }catch (SQLException sql) {
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return listCandidate;
    }
    @Override
    public List<Candidate> filterGender(String gender_filter) {
        Connection con = null;
        CallableStatement cs = null;
        List<Candidate> listCandidate = new ArrayList<>();
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call filterToGender(?)}");
            cs.setString(1, gender_filter);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Candidate c = new Candidate();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setEmail(rs.getString("email"));
                c.setPassword(rs.getString("password"));
                c.setPhone(rs.getString("phone"));
                c.setExperience(rs.getInt("experience"));
                c.setGender(rs.getString("gender"));
                c.setStatus(rs.getString("status"));
                c.setDescription(rs.getString("description"));
                c.setDob(rs.getDate("dob"));
                listCandidate.add(c);
            }
            return listCandidate;
        }catch (SQLException sql) {
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return listCandidate;
    }

    @Override
    public List<Candidate> filterTechnology(String technology_filter) {
        Connection con = null;
        CallableStatement cs = null;
        List<Candidate> listCandidate = new ArrayList<>();
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call filterToTechnology(?)}");
            cs.setString(1, technology_filter);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Candidate c = new Candidate();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setEmail(rs.getString("email"));
                c.setPassword(rs.getString("password"));
                c.setPhone(rs.getString("phone"));
                c.setExperience(rs.getInt("experience"));
                c.setGender(rs.getString("gender"));
                c.setStatus(rs.getString("status"));
                c.setDescription(rs.getString("description"));
                c.setDob(rs.getDate("dob"));
                listCandidate.add(c);
            }
            return listCandidate;
        }catch (SQLException sql) {
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return listCandidate;
    }

    @Override
    public boolean addNewPosition(String name,
                                  String description,
                                  BigDecimal minSalary,
                                  BigDecimal maxSalary,
                                  int minExperience,
                                  LocalDate expiredDate,
                                  String technologyIds) {
        try (Connection con = ConnectionDB.openConnection();
             CallableStatement cs = con.prepareCall("{ call add_recruitment_position(?,?,?,?,?,?,?) }")) {

            cs.setString(1, name);
            cs.setString(2, description);
            cs.setBigDecimal(3, minSalary);
            cs.setBigDecimal(4, maxSalary);
            cs.setInt(5, minExperience);
            cs.setDate(6, java.sql.Date.valueOf(expiredDate));
            cs.setString(7, technologyIds);
            return cs.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error adding position: " + e.getMessage());
            return false;
        }
    }



    @Override
    public boolean updatePosition(int id, String name, String description, BigDecimal minSalary, BigDecimal maxSalary, int minExperience, LocalDate expiredDate) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call update_recruitment_position(?, ?, ?, ?, ?, ?, ?)}");
            cs.setInt(1, id);
            cs.setString(2, name);
            cs.setString(3, description);
            cs.setBigDecimal(4, minSalary);
            cs.setBigDecimal(5, maxSalary);
            cs.setInt(6, minExperience);
            cs.setDate(7, java.sql.Date.valueOf(expiredDate));
            int rowsAffected = cs.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    @Override
    public boolean deletePosition(int id) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call delete_recruitment_position(?)}");
            cs.setInt(1, id);
            int rowsAffected = cs.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<RecruitmentPosition> listPosition(int pageNumber,int pageSize) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        List<RecruitmentPosition> listRecruitmentPosition = new ArrayList<>();
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call get_recruitment_positions(?, ?)}");
            cs.setInt(1, pageNumber);
            cs.setInt(2, pageSize);
            rs = cs.executeQuery();
            while (rs.next()) {
                RecruitmentPosition position = new RecruitmentPosition();
                position.setId(rs.getInt("id"));
                position.setName(rs.getString("name"));
                position.setDescription(rs.getString("description"));
                position.setMinSalary(rs.getBigDecimal("minSalary"));
                position.setMaxSalary(rs.getBigDecimal("maxSalary"));
                position.setMinExperience(rs.getInt("minExperience"));
                position.setCreatedDate(rs.getDate("createdDate").toLocalDate());
                position.setExpiredDate(rs.getDate("expiredDate").toLocalDate());
                listRecruitmentPosition.add(position);
            }
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return listRecruitmentPosition;
    }

    @Override
    public List<RecruitmentPosition> listRecruitmentPosition() {
        Connection con = null;
        CallableStatement cs = null;
        List<RecruitmentPosition> listRecruitmentPosition = new ArrayList<>();
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call get_all_position()}");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                RecruitmentPosition position = new RecruitmentPosition();
                position.setId(rs.getInt("id"));
                position.setName(rs.getString("name"));
                position.setDescription(rs.getString("description"));
                position.setMinSalary(rs.getBigDecimal("minSalary"));
                position.setMaxSalary(rs.getBigDecimal("maxSalary"));
                position.setMinExperience(rs.getInt("minExperience"));
                position.setCreatedDate(rs.getDate("createdDate").toLocalDate());
                position.setExpiredDate(rs.getDate("expiredDate").toLocalDate());
                listRecruitmentPosition.add(position);
            }
            return listRecruitmentPosition;
        }catch (SQLException sql) {
            sql.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return listRecruitmentPosition;
    }

    @Override
    public List<Application> listApplication(int pageNumber, int pageSize) {
        List<Application> listApplications = new ArrayList<>();
        try (Connection con = ConnectionDB.openConnection();
             CallableStatement cs = con.prepareCall("{call get_application_pagination(?, ?)}")) {
            cs.setInt(1, pageNumber);
            cs.setInt(2, pageSize);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Application app = new Application();
                app.setId(rs.getInt("id"));
                app.setCandidateId(rs.getInt("candidateId"));
                app.setRecruitmentPositionId(rs.getInt("recruitmentPositionId"));
                app.setCvUrl(rs.getString("cvUrl"));
                app.setProgress(rs.getString("progress"));

                Timestamp interviewRequestDate = rs.getTimestamp("interviewRequestDate");
                if (interviewRequestDate != null) {
                    app.setInterviewRequestDate(interviewRequestDate.toLocalDateTime());
                }

                app.setInterviewRequestResult(rs.getString("interviewRequestResult"));
                app.setInterviewLink(rs.getString("interviewLink"));

                Timestamp interviewTime = rs.getTimestamp("interviewTime");
                if (interviewTime != null) {
                    app.setInterviewTime(interviewTime.toLocalDateTime());
                }

                app.setInterviewResult(rs.getString("interviewResult"));
                app.setInterviewResultNote(rs.getString("interviewResultNote"));

                Timestamp destroyAt = rs.getTimestamp("destroyAt");
                if (destroyAt != null) {
                    app.setDestroyAt(destroyAt.toLocalDateTime());
                }

                Timestamp createAt = rs.getTimestamp("createAt");
                if (createAt != null) {
                    app.setCreateAt(createAt.toLocalDateTime());
                }

                Timestamp updateAt = rs.getTimestamp("updateAt");
                if (updateAt != null) {
                    app.setUpdateAt(updateAt.toLocalDateTime());
                }

                app.setDestroyReason(rs.getString("destroyReason"));
                listApplications.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listApplications;
    }


    @Override
    public List<Application> listApplication() {
        Connection con = null;
        CallableStatement cs = null;
        List<Application> listApplications = new ArrayList<>();
        try{
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call get_all_application()}");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Application app = new Application();
                app.setId(rs.getInt("id"));
                app.setCandidateId(rs.getInt("candidateId"));
                app.setRecruitmentPositionId(rs.getInt("recruitmentPositionId"));
                app.setCvUrl(rs.getString("cvUrl"));
                app.setProgress(rs.getString("progress"));

                Timestamp interviewRequestDate = rs.getTimestamp("interviewRequestDate");
                if (interviewRequestDate != null) {
                    app.setInterviewRequestDate(interviewRequestDate.toLocalDateTime());
                }

                app.setInterviewRequestResult(rs.getString("interviewRequestResult"));
                app.setInterviewLink(rs.getString("interviewLink"));

                Timestamp interviewTime = rs.getTimestamp("interviewTime");
                if (interviewTime != null) {
                    app.setInterviewTime(interviewTime.toLocalDateTime());
                }

                app.setInterviewResult(rs.getString("interviewResult"));
                app.setInterviewResultNote(rs.getString("interviewResultNote"));

                Timestamp destroyAt = rs.getTimestamp("destroyAt");
                if (destroyAt != null) {
                    app.setDestroyAt(destroyAt.toLocalDateTime());
                }

                Timestamp createAt = rs.getTimestamp("createAt");
                if (createAt != null) {
                    app.setCreateAt(createAt.toLocalDateTime());
                }

                Timestamp updateAt = rs.getTimestamp("updateAt");
                if (updateAt != null) {
                    app.setUpdateAt(updateAt.toLocalDateTime());
                }

                app.setDestroyReason(rs.getString("destroyReason"));
                listApplications.add(app);
            }
            return listApplications;
        }catch (Exception e){
            System.out.println("Lỗi không xác định");
        }
        return  listApplications;
    }

    @Override
    public List<Application> listFilterByProcess(String process) {
        List<Application> list = new ArrayList<>();
        try (Connection con = ConnectionDB.openConnection();
             CallableStatement cs = con.prepareCall("{call filterByProcess(?)}")) {
            cs.setString(1, process);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Application app = new Application();
                app.setId(rs.getInt("id"));
                app.setCandidateId(rs.getInt("candidateId"));
                app.setRecruitmentPositionId(rs.getInt("recruitmentPositionId"));
                app.setCvUrl(rs.getString("cvUrl"));
                app.setProgress(rs.getString("progress"));

                Timestamp interviewRequestDate = rs.getTimestamp("interviewRequestDate");
                if (interviewRequestDate != null) {
                    app.setInterviewRequestDate(interviewRequestDate.toLocalDateTime());
                }

                app.setInterviewRequestResult(rs.getString("interviewRequestResult"));
                app.setInterviewLink(rs.getString("interviewLink"));

                Timestamp interviewTime = rs.getTimestamp("interviewTime");
                if (interviewTime != null) {
                    app.setInterviewTime(interviewTime.toLocalDateTime());
                }

                app.setInterviewResult(rs.getString("interviewResult"));
                app.setInterviewResultNote(rs.getString("interviewResultNote"));

                Timestamp destroyAt = rs.getTimestamp("destroyAt");
                if (destroyAt != null) {
                    app.setDestroyAt(destroyAt.toLocalDateTime());
                }

                Timestamp createAt = rs.getTimestamp("createAt");
                if (createAt != null) {
                    app.setCreateAt(createAt.toLocalDateTime());
                }

                Timestamp updateAt = rs.getTimestamp("updateAt");
                if (updateAt != null) {
                    app.setUpdateAt(updateAt.toLocalDateTime());
                }

                app.setDestroyReason(rs.getString("destroyReason"));
                list.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Application> listApplicationByResult(String result) {
        List<Application> list = new ArrayList<>();
        try (Connection con = ConnectionDB.openConnection();
             CallableStatement cs = con.prepareCall("{call filterByResult(?)}")) {
            cs.setString(1, result);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Application app = new Application();
                app.setId(rs.getInt("id"));
                app.setCandidateId(rs.getInt("candidateId"));
                app.setRecruitmentPositionId(rs.getInt("recruitmentPositionId"));
                app.setCvUrl(rs.getString("cvUrl"));
                app.setProgress(rs.getString("progress"));

                Timestamp interviewRequestDate = rs.getTimestamp("interviewRequestDate");
                if (interviewRequestDate != null) {
                    app.setInterviewRequestDate(interviewRequestDate.toLocalDateTime());
                }

                app.setInterviewRequestResult(rs.getString("interviewRequestResult"));
                app.setInterviewLink(rs.getString("interviewLink"));

                Timestamp interviewTime = rs.getTimestamp("interviewTime");
                if (interviewTime != null) {
                    app.setInterviewTime(interviewTime.toLocalDateTime());
                }

                app.setInterviewResult(rs.getString("interviewResult"));
                app.setInterviewResultNote(rs.getString("interviewResultNote"));

                Timestamp destroyAt = rs.getTimestamp("destroyAt");
                if (destroyAt != null) {
                    app.setDestroyAt(destroyAt.toLocalDateTime());
                }

                Timestamp createAt = rs.getTimestamp("createAt");
                if (createAt != null) {
                    app.setCreateAt(createAt.toLocalDateTime());
                }

                Timestamp updateAt = rs.getTimestamp("updateAt");
                if (updateAt != null) {
                    app.setUpdateAt(updateAt.toLocalDateTime());
                }

                app.setDestroyReason(rs.getString("destroyReason"));
                list.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Application getApplication(int id) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        Application app = null;

        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call get_detail_application(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();

            if (rs.next()) {
                app = new Application();
                app.setId(rs.getInt("id"));
                app.setCandidateId(rs.getInt("candidateId"));
                app.setRecruitmentPositionId(rs.getInt("recruitmentPositionId"));
                app.setCvUrl(rs.getString("cvUrl"));
                app.setProgress(rs.getString("progress"));

                Timestamp ts;

                ts = rs.getTimestamp("interviewRequestDate");
                if (ts != null) app.setInterviewRequestDate(ts.toLocalDateTime());

                app.setInterviewRequestResult(rs.getString("interviewRequestResult"));
                app.setInterviewLink(rs.getString("interviewLink"));

                ts = rs.getTimestamp("interviewTime");
                if (ts != null) app.setInterviewTime(ts.toLocalDateTime());

                app.setInterviewResult(rs.getString("interviewResult"));
                app.setInterviewResultNote(rs.getString("interviewResultNote"));

                ts = rs.getTimestamp("destroyAt");
                if (ts != null) app.setDestroyAt(ts.toLocalDateTime());

                ts = rs.getTimestamp("createAt");
                if (ts != null) app.setCreateAt(ts.toLocalDateTime());

                ts = rs.getTimestamp("updateAt");
                if (ts != null) app.setUpdateAt(ts.toLocalDateTime());

                app.setDestroyReason(rs.getString("destroyReason"));
            }
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception e) {
                System.out.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }

        return app;
    }

    @Override
    public boolean interviewCandidate(int id, String link, LocalDateTime interviewDateTime) {
        try (Connection con = ConnectionDB.openConnection();
             CallableStatement cs = con.prepareCall("{call interviewing(?, ?, ?)}")) {
            cs.setInt(1, id);
            cs.setString(2, link);
            cs.setTimestamp(3, Timestamp.valueOf(interviewDateTime));
            return cs.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean updateResultInterview(int id, String note, String result) {
        try (Connection con = ConnectionDB.openConnection();
             CallableStatement cs = con.prepareCall("{call completeInrerview(?, ?, ?)}")) {
            cs.setInt(1, id);
            cs.setString(2, note);
            cs.setString(3, result);
            return cs.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteResultInterview(int id, String result) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call destroyInterview(?, ?)}");
            cs.setInt(1, id);
            cs.setString(2, result);
            return cs.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public String getStatusAccount(String email) {
        Connection con = null;
        CallableStatement cs = null;
        String status = null;
        try {
            con = ConnectionDB.openConnection();
            cs = con.prepareCall("{call get_status_candidate(?)}");
            cs.setString(1, email);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                status = rs.getString("status");
            }
            return status;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
