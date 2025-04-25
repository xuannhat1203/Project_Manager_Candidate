package ra.edu.business.service.candidate;

import ra.edu.business.dao.candidate.CandidateDao;
import ra.edu.business.dao.candidate.CandidateDaoImp;
import ra.edu.business.model.Application;
import ra.edu.business.model.Candidate;
import ra.edu.business.model.RecruitmentPosition;

import java.util.List;

public class CandidateServiceImp implements CandidateService {
    public final CandidateDao candidateDao;
    public CandidateServiceImp() {
        candidateDao = new CandidateDaoImp();
    }
    @Override
    public boolean updateProfile(int id, String name, String email, String phone, int experience, String gender) {
       return candidateDao.updateProfile(id, name, email, phone, experience, gender);
    }

    @Override
    public boolean changeMyPassword(int id, String password) {
        return candidateDao.changeMyPassword(id, password);
    }

    @Override
    public List<Application> listMyInterview(int id,int page, int size) {
        return candidateDao.listMyInterview(id,page,size);
    }

    @Override
    public RecruitmentPosition getRecruitmentPositionById(int id) {
        return candidateDao.getRecruitmentPositionById(id);
    }

    @Override
    public List<Application> getApplicationsByCandidateId(int candidateId) {
        return candidateDao.getApplicationsByCandidateId(candidateId);
    }

    @Override
    public Candidate getCandidate(String email) {
        return candidateDao.getCandidate(email);
    }

    @Override
    public boolean candidateConfirm(int id) {
        return candidateDao.candidateConfirm(id);
    }

//    @Override
//    public List<Application> getActiveApplications(int page,int size) {
//        return candidateDao.getActiveApplications(page,size);
//    }

    @Override
    public Application getApplicationDetail(int applicationId) {
        return candidateDao.getApplicationDetail(applicationId);
    }

    @Override
    public boolean applyForApplication(int candidateId, int recruitmentPositionId, String cvUrl) {
        return candidateDao.applyForApplication(candidateId, recruitmentPositionId,cvUrl);
    }

    @Override
    public List<RecruitmentPosition> getActiveRecruitmentPositions(int page, int size) {
        return candidateDao.getActiveRecruitmentPositions(page,size);
    }
}
