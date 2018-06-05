package codesquad.service;

import codesquad.NoSuchEntityException;
import codesquad.domain.*;
import codesquad.dto.AnswerDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QnaService qnaService;

    @Test
    public void findById_exists() {
        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(new Answer("test")));

        Answer answer = qnaService.findAnswerById(anyLong());
        verify(answerRepository).findById(anyLong());
        assertEquals("test", answer.getContents());
    }

    @Test(expected = NoSuchEntityException.class)
    public void findById_does_NOT_exist() {
        when(answerRepository.findById(anyLong())).thenReturn(Optional.empty());

        qnaService.findAnswerById(anyLong());
        verify(answerRepository).findById(anyLong());
    }

    @Test
    public void addAnswer_quesetion_exists() {
        User writer = new User("testUser", "password", "name", "email");
        Question question = new Question("test", "content");
        AnswerDto answerDto = new AnswerDto("answer content");
        when(questionRepository.findById(3L)).thenReturn(Optional.of(question));

        qnaService.addAnswer(writer, 3L, answerDto);
    }

    @Test(expected = NoSuchEntityException.class)
    public void addAnswer_quesetion_does_NOT_exist() {
        User writer = new User("testUser", "password", "name", "email");
        AnswerDto answerDto = new AnswerDto("answer content");
        when(questionRepository.findById(3L)).thenReturn(Optional.empty());

        qnaService.addAnswer(writer, 3L, answerDto);
    }

    @Test
    public void deleteAnswer() {
    }
}