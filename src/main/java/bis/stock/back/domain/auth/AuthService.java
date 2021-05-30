package bis.stock.back.domain.auth;

import bis.stock.back.domain.auth.dto.JoinDto;
import bis.stock.back.domain.user.User;
import bis.stock.back.domain.user.UserRepository;
import bis.stock.back.domain.user.UserRole;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    public Long join(JoinDto joinDto) {

        try {
            return userRepository.save(User.builder()
                    .email(joinDto.getEmail())
                    .password(joinDto.getPassword())
                    .role(UserRole.ROLE_USER)
                    .build()).getId();
        } catch (Exception e) {
            throw e;
        }
    }
}
