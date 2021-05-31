package bis.stock.back.domain.auth.dto;

import lombok.Getter;

@Getter
public class JoinDto {

    private String email;
    private String password;
    private String nickname;
}
