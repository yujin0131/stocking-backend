package bis.stock.back.domain.user;

import bis.stock.back.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserInfo(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));
    }
}
