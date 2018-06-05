package codesquad.web;

import codesquad.domain.User;
import codesquad.dto.AnswerDto;
import codesquad.security.LoginUser;
import codesquad.service.AnswerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class AnswerController {

    @Resource(name = "answerService")
    private AnswerService answerService;

    @PostMapping("")
    public String add(@LoginUser User loginUser, @PathVariable long questionId, AnswerDto answerDto) {

        return null;
    }

    @DeleteMapping("{id}")
    public void delete(@LoginUser User loginUser, @PathVariable long id) {

    }
}
