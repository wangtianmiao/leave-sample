package ddd.leave.application.service;

import ddd.leave.domain.leave.entity.Leave;
import ddd.leave.domain.leave.entity.valueobject.Approver;
import ddd.leave.domain.leave.service.LeaveDomainService;
import ddd.leave.domain.person.entity.Person;
import ddd.leave.domain.person.service.PersonDomainService;
import ddd.leave.domain.rule.service.ApprovalRuleDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveApplicationService{

    @Autowired
    LeaveDomainService leaveDomainService;
    @Autowired
    PersonDomainService personDomainService;
    @Autowired
    ApprovalRuleDomainService approvalRuleDomainService;

    /**
     * 创建请假单
     * @param leave
     */
    public void createLeaveInfo(Leave leave){
        // 1. 根据请假单定义的人员类型、请假类型和请假时长从 rule 聚合中获取请假审批规则
        int leaderMaxLevel = approvalRuleDomainService.getLeaderMaxLevel(leave.getApplicant().getPersonType(), leave.getType().toString(), leave.getDuration());
        // 2. 根据请假审批规则，从 person 聚合中获取请假审批人
        Person approver = personDomainService.findFirstApprover(leave.getApplicant().getPersonId(), leaderMaxLevel);
        // 3. 根据请假数据和从 rule 和 person 聚合获取的数据，创建请假单
        leaveDomainService.createLeave(leave, leaderMaxLevel, Approver.fromPerson(approver));
    }

    /**
     * 更新请假单基本信息
     * @param leave
     */
    public void updateLeaveInfo(Leave leave){
        leaveDomainService.updateLeaveInfo(leave);
    }

    /**
     * 提交审批，更新请假单信息
     * @param leave
     */
    public void submitApproval(Leave leave){
        //find next approver
        Person approver = personDomainService.findNextApprover(leave.getApprover().getPersonId(), leave.getLeaderMaxLevel());
        leaveDomainService.submitApproval(leave, Approver.fromPerson(approver));
    }

    public Leave getLeaveInfo(String leaveId){
        return leaveDomainService.getLeaveInfo(leaveId);
    }

    public List<Leave> queryLeaveInfosByApplicant(String applicantId){
        return leaveDomainService.queryLeaveInfosByApplicant(applicantId);
    }

    public List<Leave> queryLeaveInfosByApprover(String approverId){
        return leaveDomainService.queryLeaveInfosByApprover(approverId);
    }
}