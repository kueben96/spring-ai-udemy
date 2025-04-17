package bmi.springframework.springaiintro.controllers;


import bmi.springframework.springaiintro.model.Answer;
import bmi.springframework.springaiintro.model.GetCapitalRequest;
import bmi.springframework.springaiintro.model.GetCapitalResponse;
import bmi.springframework.springaiintro.model.Question;
import bmi.springframework.springaiintro.services.OpenAIService;
import bmi.springframework.springaiintro.services.OpenAIServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionsController {

    private final OpenAIService openAIService;

    public QuestionsController(OpenAIServiceImpl openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        System.out.println("question" + question);
        return openAIService.getAnswer(question);
    }

    @PostMapping("/capital")
    public GetCapitalResponse getCapital(@RequestBody GetCapitalRequest capitalRequest) {
        return openAIService.getCapital(capitalRequest);
    }


    @PostMapping("/capitalWithInfo")
    public Answer getCapitalWithInfo(@RequestBody GetCapitalRequest capitalRequest) {
        return openAIService.getCapitalWithInfo(capitalRequest);
    }
}
