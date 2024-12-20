package SJUCapstone.BE.user.dto;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class UserInfoResponse {
    private Long userInfoId;
    private Timestamp LastDiagnoseDate;
    private Integer LastDiagnoseScore;
//    private Long LastDiagnoseStatus;
    private int DiagnoseNum;
    private String UserName;

    public UserInfoResponse(Long userInfoId, Timestamp LastDiagnoseDate, Integer LastDiagnoseScore, int DiagnoseNum, String UserName){
        this.userInfoId = userInfoId;
        this.LastDiagnoseDate = LastDiagnoseDate;
        this.LastDiagnoseScore = LastDiagnoseScore;
//        this.LastDiagnoseStatus = LastDiagnoseStatus;
        this.DiagnoseNum = DiagnoseNum;
        this.UserName = UserName;
    }

    public UserInfoResponse() {
    }
}