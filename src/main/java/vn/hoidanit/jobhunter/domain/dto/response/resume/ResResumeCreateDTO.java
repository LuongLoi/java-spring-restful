package vn.hoidanit.jobhunter.domain.dto.response.resume;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResResumeCreateDTO {
    private long id;
    private Instant createdAt;
    private String createBy;
}
