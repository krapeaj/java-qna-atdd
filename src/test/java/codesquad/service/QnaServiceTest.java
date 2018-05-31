package codesquad.service;

import codesquad.UnAuthenticationException;
import codesquad.UnAuthorizedException;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QnaService qnaService;

    @Test
    public void update_Success() throws Exception {
        User loginUser = new User("krapeaj", "password", "name", "krapeaj@slipp.net");

        Question original = new Question("test", "content");
        original.writeBy(loginUser);
        when(questionRepository.findById(original.getId())).thenReturn(Optional.of(original));

        Question updated = new Question("test", "content2");
        when(questionRepository.save(original)).thenReturn(original);

        qnaService.update(loginUser, original.getId(), updated.toQuestionDto());
        assertThat(original.getContents(), is("content2"));
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_Logged_In_But_NOT_Writer() {
        User writer = new User("hello", "password", "name", "hello@slipp.net");

        Question original = new Question("test", "not logged in");
        original.writeBy(writer);
        when(questionRepository.findById(original.getId())).thenReturn(Optional.of(original));

        Question updated = new Question("test", "not logged in 2");

        User loggedInUser = new User("notWriter", "password", "name", "hi@slipp.net");
        qnaService.update(loggedInUser, original.getId(), updated.toQuestionDto());
        assertThat(updated.getContents(), is("not logged in"));
    }

    @Test(expected = UnAuthenticationException.class)
    public void update_Different_Questions() {
        User writer = new User("hello", "password", "name", "hello@slipp.net");

        Question original = new Question("test", "content");
        original.writeBy(writer);
        when(questionRepository.findById(original.getId())).thenReturn(Optional.of(original));

        Question updated = new Question("test2", "content");

        qnaService.update(writer, original.getId(), updated.toQuestionDto());
    }

    @Test
    public void delete() throws Exception {
        User loginUser = new User("krapeaj", "password", "name", "krapeaj@slipp.net");

        Question question = new Question("test", "content");
        question.writeBy(loginUser);
    }


}