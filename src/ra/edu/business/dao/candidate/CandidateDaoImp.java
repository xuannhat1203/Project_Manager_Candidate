package ra.edu.business.dao.candidate;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Application;
import ra.edu.business.model.Candidate;
import ra.edu.business.model.RecruitmentPosition;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CandidateDaoImp implements CandidateDao {
    @Override
    public boolean updateProfile(int id, String name, String email, String phone, int experience, String gender) {
        Connection con = null;
        CallableStatement cstmt = null;
        try {
            con = ConnectionDB.openConnection();
            cstmt = con.prepareCall("{call change_personal_information(?, ?, ?, ?, ?, ?)}");
            cstmt.setInt(1, id);
            cstmt.setString(2, name);
            cstmt.setString(3, email);
            cstmt.setString(4, phone);
            cstmt.setInt(5, experience);
            cstmt.setString(6, gender);
            cstmt.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean changeMyPassword(int id, String password) {
        Connection con = null;
        CallableStatement cstmt = null;
        try {
            con = ConnectionDB.openConnection();
            cstmt = con.prepareCall("{call change_password(?, ?)}");
            cstmt.setInt(1, id);
            cstmt.setString(2, password);
            cstmt.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public List<Application> listMyInterview(int candidateId, int page, int size) {
        List<Application> listApplications = new ArrayList<>();
        try (Connection con = ConnectionDB.openConnection();
             CallableStatement cstmt = con.prepareCall("{call get_applications_by_candidate(?,?,?)}")) {
            cstmt.setInt(1, candidateId);
            cstmt.setInt(2, page);
            cstmt.setInt(3, size);

            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    Application app = new Application();
                    app.setId(rs.getInt("application_id"));
                    app.setCandidateId(rs.getInt("candidate_id"));
                    app.setRecruitmentPositionId(rs.getInt("recruitment_position_id"));
                    app.setProgress(rs.getString("progress"));
                    app.setInterviewResult(rs.getString("interview_result"));
                    app.setCreateAt(rs.getTimestamp("created_at").toLocalDateTime());
                    String destroyAtStr = rs.getString("destroyAt");
                    if (destroyAtStr != null) {
                        app.setDestroyAt(LocalDateTime.parse(destroyAtStr));
                    } else {
                        app.setDestroyAt(null);
                    }
                    listApplications.add(app);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listApplications;
    }

    @Override
    public RecruitmentPosition getRecruitmentPositionById(int id) {
        Connection con = null;
        CallableStatement cstmt = null;
        RecruitmentPosition recruitmentPosition = null;
        try {
            con = ConnectionDB.openConnection();
            cstmt = con.prepareCall("{call get_recruitment_position_by_id(?)}");
            cstmt.setInt(1, id);
            ResultSet rs = cstmt.executeQuery();
            if (rs.next()) {
                recruitmentPosition = new RecruitmentPosition();
                recruitmentPosition.setId(rs.getInt("id"));
                recruitmentPosition.setName(rs.getString("name"));
                recruitmentPosition.setDescription(rs.getString("description"));
                recruitmentPosition.setMinSalary(rs.getBigDecimal("minSalary"));
                recruitmentPosition.setMaxSalary(rs.getBigDecimal("maxSalary"));
                recruitmentPosition.setMinExperience(rs.getInt("minExperience"));
                recruitmentPosition.setCreatedDate(rs.getDate("createdDate").toLocalDate());
                recruitmentPosition.setExpiredDate(rs.getDate("expiredDate").toLocalDate());
            }
            return recruitmentPosition;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Application> getApplicationsByCandidateId(int candidateId) {
        Connection con = null;
        CallableStatement cstmt = null;
        List<Application> listApplications = new ArrayList<>();
        try {
            con = ConnectionDB.openConnection();
            cstmt = con.prepareCall("{call get_applications_by_candidate(?)}");
            cstmt.setInt(1, candidateId);
            ResultSet rs = cstmt.executeQuery();
            while (rs.next()) {
                Application app = new Application();
                app.setId(rs.getInt("application_id"));
                app.setCandidateId(rs.getInt("candidate_id"));
                app.setRecruitmentPositionId(rs.getInt("recruitment_position_id"));
                app.setProgress(rs.getString("progress"));
                app.setInterviewResult(rs.getString("interview_result"));
                String destroyAtStr = rs.getString("destroyAt");
                if (app.getCreateAt() != null) {
                    app.setCreateAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                if (destroyAtStr != null) {
                    app.setDestroyAt(LocalDateTime.parse(destroyAtStr));
                } else {
                    app.setDestroyAt(null);
                }
                listApplications.add(app);
            }
            return listApplications;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listApplications;
    }

    @Override
    public Candidate getCandidate(String email) {
        Connection con = null;
        CallableStatement cstmt = null;
        Candidate candidate = null;
        try {
            con = ConnectionDB.openConnection();
            cstmt = con.prepareCall("{call get_candidate(?)}");
            cstmt.setString(1, email);
            ResultSet rs = cstmt.executeQuery();
            if (rs.next()) {
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
                candidate = c;
            }
            return candidate;
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return candidate;
    }

    @Override
    public boolean candidateConfirm(int id) {
        Connection con = null;
        CallableStatement cstmt = null;
        try {
            con = ConnectionDB.openConnection();
            cstmt = con.prepareCall("{call confirmInterview(?)}");
            cstmt.setInt(1, id);
            cstmt.execute();
            return true;
        }catch (Exception e) {
            System.out.println("Lỗi không xác định");
        }
        return false;
    }
    @Override
    public List<RecruitmentPosition> getActiveRecruitmentPositions(int page, int size) {
        List<RecruitmentPosition> positions = new ArrayList<>();

        try (Connection con = ConnectionDB.openConnection();
             CallableStatement cstmt = con.prepareCall("{CALL get_active_recruitment_positions(?, ?)}")) {

            cstmt.setInt(1, page);
            cstmt.setInt(2, size);

            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    RecruitmentPosition pos = new RecruitmentPosition();
                    pos.setId(rs.getInt("recruitment_position_id"));
                    pos.setName(rs.getString("recruitment_position_name"));
                    pos.setDescription(rs.getString("description"));
                    pos.setMinSalary(rs.getBigDecimal("minSalary"));
                    pos.setMaxSalary(rs.getBigDecimal("maxSalary"));
                    pos.setMinExperience(rs.getInt("minExperience"));
                    pos.setCreatedDate(rs.getDate("createdDate").toLocalDate());
                    pos.setExpiredDate(rs.getDate("expiredDate").toLocalDate());
                    positions.add(pos);
                }
            }

        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách vị trí hoạt động: " + e.getMessage());
        }

        return positions;
    }


    @Override
    public boolean applyForApplication(int candidateId, int recruitmentPositionId, String cvUrl) {
        Connection con = null;
        CallableStatement cstmt = null;

        try {
            con = ConnectionDB.openConnection();
            cstmt = con.prepareCall("{call insert_application(?, ?, ?)}");
            cstmt.setInt(1, candidateId);
            cstmt.setInt(2, recruitmentPositionId);
            cstmt.setString(3, cvUrl);
            cstmt.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi nộp đơn ứng tuyển: " + e.getMessage());
        } finally {
            try {
                if (cstmt != null) cstmt.close();
                if (con != null) con.close();
            } catch (Exception e) {
                System.out.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
        return false;
    }
    @Override
    public Application getApplicationDetail(int applicationId) {
        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        Application app = null;
        try {
            con = ConnectionDB.openConnection();
            cstmt = con.prepareCall("{call getapplicationdetail(?)}");
            cstmt.setInt(1, applicationId);
            rs = cstmt.executeQuery();
            if (rs.next()) {
                app = new Application();
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
            }
        } catch (Exception e) {
            System.out.println("Lỗi không xác định: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cstmt != null) cstmt.close();
                if (con != null) con.close();
            } catch (Exception e) {
                System.out.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }

        return app;
    }

}
