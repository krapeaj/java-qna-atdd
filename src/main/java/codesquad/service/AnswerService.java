package codesquad.service;

import codesquad.NoSuchEntityException;
import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import codesquad.domain.User;
import codesquad.dto.AnswerDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("answerService")
public class AnswerService {

    @Resource(name = "answerRepository")
    private AnswerRepository answerRepository;

    public Answer findById(long id) {
        return answerRepository.findById(id)
                .filter(answer -> !answer.isDeleted())
                .orElseThrow(NoSuchEntityException::new);
    }

    public Answer addAnswer(long questionId, User writer, AnswerDto answerDto) {
        Answer newAnswer = answerDto.toAnswer();
        newAnswer.writeBy(writer);

        return null;
    }

    public void deleteAnswer(User writer, long id) {


    }
}
