package vn.hoidanit.jobhunter.domain.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResUserUpdateDTO {
     private long id;
    private String name;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant updatedAt;
    private CompanyUser company;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
