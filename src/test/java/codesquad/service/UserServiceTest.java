package codesquad.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import codesquad.dto.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import codesquad.UnAuthenticationException;
import codesquad.domain.User;
import codesquad.domain.UserRepository;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void add() {
        UserDto userDto = new UserDto("krapeaj", "password", "name", "hi@hi.com");
        when(userRepository.findByUserId(userDto.getUserId())).thenReturn(Optional.of(userDto.toUser()));

        userService.add(userDto);
        verify(userRepository).save(new User("krapeaj", "password", "name", "hi@hi.com"));
    }

    @Test
    public void login_success() throws Exception {
        User user = new User("sanjigi", "password", "name", "javajigi@slipp.net");
        when(userRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(user));

        User loginUser = userService.login(user.getUserId(), user.getPassword());
        assertThat(loginUser, is(user));
    }

    @Test(expected = UnAuthenticationException.class)
    public void login_failed_when_user_not_found() throws Exception {
        when(userRepository.findByUserId("sanjigi")).thenReturn(Optional.empty());

        userService.login("sanjigi", "password");
    }

    @Test(expected = UnAuthenticationException.class)
    public void login_failed_when_mismatch_password() throws Exception {
        User user = new User("sanjigi", "password", "name", "javajigi@slipp.net");
        when(userRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(user));

        userService.login(user.getUserId(), user.getPassword() + "2");
    }

}
