package codesquad.service;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class QnaServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QnaService qnaService;

    @Test
    public void create_logged_in() {
        Question question = new Question("test", "content");
        when(questionRepository.save(question)).thenReturn(question);

        User loginUser = new User("sanjigi", "password", "name", "javajigi@slipp.net");


//        verify(qnaService).create();
    }

}