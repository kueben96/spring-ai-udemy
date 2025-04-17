package bmi.springframework.springaiintro.services;

import bmi.springframework.springaiintro.model.Answer;
import bmi.springframework.springaiintro.model.GetCapitalRequest;
import bmi.springframework.springaiintro.model.Question;

public interface OpenAIService {

    Answer getCapital(GetCapitalRequest capitalRequest);

    Answer getCapitalWithInfo(GetCapitalRequest capitalRequest);

    String getAnswer(String question);

    Answer getAnswer(Question question);
}
