package ddd.leave.interfaces.dto;

import lombok.Data;

import java.util.List;

@Data
public class LeaveDTO {

    private String leaveId;
    private ApplicantDTO applicantDTO;
    private ApproverDTO approverDTO;
    private String leaveType;
    private ApprovalInfoDTO currentApprovalInfoDTO;
    private List<ApprovalInfoDTO> historyApprovalInfoDTOList;
    private String startTime;
    private String endTime;
    private long duration;
    private String status;
}
