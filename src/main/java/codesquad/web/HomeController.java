package codesquad.web;

import codesquad.domain.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("questions", questionRepository.findByDeleted(false));
        return "home";
    }
}

//TODO: Reduce duplicate code
//TODO: Add test for Question
//TODO: Logout function
//TODO: Edit profile function
//TODO: View profile function
//TODO: Add/Delete Answer + test -- for next step?