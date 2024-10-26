package vn.hoidanit.jobhunter.domain.dto.response.resume;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.ResumeEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResResumeFetchDTO {
    private long id;
    private String email;
    private String url;
    private ResumeEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private String companyName;
    private UserResume user;
    private JobResume job;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResume {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobResume {
        private long id;
        private String name;
    }
}
