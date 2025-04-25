package ra.edu.business.dao.candidate;

import ra.edu.business.model.Application;
import ra.edu.business.model.Candidate;
import ra.edu.business.model.RecruitmentPosition;

import java.util.List;

public interface CandidateDao {
    boolean updateProfile(int id,String name,String email,String phone, int experience, String gender);
    boolean changeMyPassword(int id, String password);
    Candidate getCandidate(String email);
    boolean candidateConfirm(int id);
//    List<Application> getActiveApplications(int page, int size);
    Application getApplicationDetail(int applicationId);
    boolean applyForApplication(int candidateId, int recruitmentPositionId, String cvUrl);
    List<RecruitmentPosition> getActiveRecruitmentPositions(int page, int size);
    List<Application> listMyInterview(int id, int page, int size);
    public RecruitmentPosition getRecruitmentPositionById(int id);
    public List<Application> getApplicationsByCandidateId(int candidateId);
}
