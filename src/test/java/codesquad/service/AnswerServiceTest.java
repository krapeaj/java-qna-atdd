package codesquad.service;

import codesquad.NoSuchEntityException;
import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
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

    @InjectMocks
    private AnswerService answerService;

    @Test
    public void findById_exists() {
        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(new Answer("test")));

        Answer answer = answerService.findById(anyLong());
        verify(answerRepository, times(1)).findById(anyLong());
        assertEquals("test", answer.getContents());
    }

    @Test(expected = NoSuchEntityException.class)
    public void findById_does_not_exist() {
        when(answerRepository.findById(anyLong())).thenReturn(Optional.empty());

        answerService.findById(anyLong());
        verify(answerRepository, times(1)).findById(anyLong());
    }

    @Test
    public void addAnswer() {
    }

    @Test
    public void deleteAnswer() {
    }
}