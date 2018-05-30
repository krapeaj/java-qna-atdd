package codesquad.service;

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
    public void update() throws Exception {
        User loginUser = new User("krapeaj", "password", "name", "krapeaj@slipp.net");

        Question original = new Question("test", "content");
        original.writeBy(loginUser);
        when(questionRepository.findById(original.getId()))
                .thenReturn(Optional.of(original));

        Question updated = new Question("test", "content2");
        when(questionRepository.save(original)).thenReturn(original);

        qnaService.update(loginUser, original.getId(), updated.toQuestionDto());
        assertThat(original.getContents(), is("content2"));
    }

}