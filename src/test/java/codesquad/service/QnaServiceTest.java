package codesquad.service;

import codesquad.CannotDeleteException;
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

    @Mock
    private DeleteHistoryService deleteHistoryService;

    @InjectMocks
    private QnaService qnaService;

    @Test
    public void update_Success() throws Exception {
        User loginUser = createTestUser("krapeaj");

        Question original = createTestQuestion("test", "content");
        Question updated = createTestQuestion("test", "content 2");
        original.writeBy(loginUser);

        when(questionRepository.findById(original.getId())).thenReturn(Optional.of(original));
        when(questionRepository.save(original)).thenReturn(original);

        qnaService.update(loginUser, original.getId(), updated.toQuestionDto());
        assertThat(original.getContents(), is("content 2"));
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_Logged_In_But_NOT_Writer() {
        User writer = createTestUser("writer");
        User loggedInUser = new User("notWriter", "password", "name", "hi@slipp.net");

        Question original = createTestQuestion("test", "content");
        Question updated = new Question("test", "content 2");
        original.writeBy(writer);

        when(questionRepository.findById(original.getId())).thenReturn(Optional.of(original));

        qnaService.update(loggedInUser, original.getId(), updated.toQuestionDto());
        assertThat(updated.getContents(), is("content"));
    }

    @Test(expected = UnAuthenticationException.class)
    public void update_Question_Does_Not_Exist() {
        User user = createTestUser("user");
        Question original = createTestQuestion("test", "content");
        Question updated = createTestQuestion("test", "content 2");

        when(questionRepository.findById(original.getId())).thenReturn(Optional.empty());

        qnaService.update(user, original.getId(), updated.toQuestionDto());
        assertThat(original.getContents(), is("content"));
    }

    @Test(expected = UnAuthenticationException.class)
    public void update_Different_Questions() {
        User writer = createTestUser("writer");

        Question original = createTestQuestion("test", "content");
        Question updated = createTestQuestion("test 2", "different question");
        original.writeBy(writer);

        when(questionRepository.findById(original.getId())).thenReturn(Optional.of(original));

        qnaService.update(writer, original.getId(), updated.toQuestionDto());
        assertThat(original.getContents(), is("different question"));
    }

    private User createTestUser(String userId) {
        return new User(userId, "password", "name", "test@slipp.net");
    }

    private Question createTestQuestion(String title, String content) {
        return new Question(title, content);
    }

    @Test
    public void delete_Success() throws Exception {
        User loginUser = createTestUser("krapeaj");

        Question question = createTestQuestion("test", "content");
        question.writeBy(loginUser);

        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(loginUser, question.getId());

        verify(deleteHistoryService,times(1)).saveAll(anyList());
        assertThat(question.isDeleted(), is(true));
    }

    @Test(expected = UnAuthorizedException.class)
    public void delete_Logged_In_But_NOT_Writer() {
        User writer = createTestUser("writer");
        User loginUser = createTestUser("loginUser");
        Question question = createTestQuestion("test", "content");
        question.writeBy(writer);

        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(loginUser, question.getId());
        assertThat(question.isDeleted(), is(false));
    }

    @Test(expected = CannotDeleteException.class)
    public void delete_Question_Does_Not_Exist() {
        User user = createTestUser("loginUser");
        Question question = createTestQuestion("test", "content");

        when(questionRepository.findById(question.getId())).thenReturn(Optional.empty());

        qnaService.deleteQuestion(user, question.getId());
        assertThat(question.isDeleted(), is(false));
    }

    @Test(expected = CannotDeleteException.class)
    public void delete_Already_Deleted() {
        User user = createTestUser("loginUser");
        Question question = createTestQuestion("test", "content");
        question.writeBy(user);
        question.deleteQuestion(user);

        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(user, question.getId());
        verify(deleteHistoryService, times(0)).saveAll(anyList());
    }
}