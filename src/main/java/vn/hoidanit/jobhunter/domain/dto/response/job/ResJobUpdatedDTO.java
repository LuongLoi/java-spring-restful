package vn.hoidanit.jobhunter.domain.dto.response.job;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.LevelEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResJobUpdatedDTO {
    private long id;
    private String name;
    private String location; 
    private double salary; 
    private int quantity;
    private LevelEnum level;
    private String description;
    private Instant startDate; 
    private Instant endDate; 
    private boolean active;
    private Instant updatedAt;
    private String updatedBy;
    private List<String> skills;
}
