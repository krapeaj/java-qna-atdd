package codesquad.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import codesquad.UnAuthenticationException;
import codesquad.UnAuthorizedException;
import codesquad.domain.User;
import codesquad.domain.UserRepository;
import codesquad.dto.UserDto;
import org.springframework.web.bind.annotation.GetMapping;

@Service("userService")
public class UserService {
    @Resource(name = "userRepository")
    private UserRepository userRepository;

    public User add(UserDto userDto) {
        return userRepository.save(userDto.toUser());
    }

    public User update(User loginUser, long id, UserDto updatedUser) {
        User original = userRepository.findOne(id);
        original.update(loginUser, updatedUser.toUser());
        return userRepository.save(original);
    }

    public User findById(User loginUser, long id) {
        User user = userRepository.findOne(id);
        if (!user.equals(loginUser)) {
            throw new UnAuthorizedException();
        }
        return user;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User login(String userId, String password) throws UnAuthenticationException {
        User user = userRepository.findByUserId(userId).orElseThrow(UnAuthenticationException::new);
        if (!user.getPassword().equals(password))
            throw new UnAuthenticationException();
        return user;
    }
}
