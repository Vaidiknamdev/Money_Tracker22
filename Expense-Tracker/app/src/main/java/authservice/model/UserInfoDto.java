package authservice.model;

import authservice.entities.UserInfo;
// import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInfoDto extends UserInfo {

    private String firstName; // first_name

    private String lastName; // last_name

    private Long phoneNumber;

    private String email; // email

    private String username;

    private String password;

}
