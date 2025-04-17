package bmi.springframework.springaiintro.services;

import bmi.springframework.springaiintro.model.Answer;
import bmi.springframework.springaiintro.model.GetCapitalRequest;
import bmi.springframework.springaiintro.model.GetCapitalResponse;
import bmi.springframework.springaiintro.model.Question;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OpenAIServiceImpl implements OpenAIService {


    private final ChatClient chatClient;
    private final ChatModel chatModel;
    @Autowired
    ObjectMapper objectMapper;
    @Value("classpath:templates/get-capital-prompt.st")
    private Resource getCapitalPrompt;
    @Value("classpath:templates/get-capital-with-info-prompt.st")
    private Resource getCapitalWithInfoPrompt;

    public OpenAIServiceImpl(ChatClient.Builder chatClientBuilder, ChatModel chatModel) {
        this.chatClient = chatClientBuilder.build();
        this.chatModel = chatModel;
    }

    @Override
    public Answer getAnswer(Question question) {
        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();


        ChatResponse response = chatClient.prompt(prompt).advisors(new SimpleLoggerAdvisor()).call().chatResponse();
        System.out.println("raw response" + response);

        var resp = chatClient.prompt(prompt).advisors(new SimpleLoggerAdvisor()).call();
        String responseContent = resp.content();

        System.out.println("Raw model response: " + response);
//        return response.getResult().getOutput().getText();
        return new Answer(responseContent);
    }

    @Override
    public GetCapitalResponse getCapital(GetCapitalRequest capitalRequest) {
        BeanOutputConverter<GetCapitalResponse> parser = new BeanOutputConverter<>(GetCapitalResponse.class);
        String format = parser.getFormat();
        System.out.println("format: \n " + format);
//        PromptTemplate promptTemplate = new PromptTemplate("what is the capital of " + capitalRequest.stateOrCountry() +"?");
        PromptTemplate promptTemplate = new PromptTemplate(getCapitalPrompt);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", capitalRequest.stateOrCountry(), "format", format));
        ChatResponse response = chatModel.call(prompt);

        System.out.println("raw response" + response.getResult().getOutput().getText());

        return parser.convert(response.getResult().getOutput().getText());
//        return new GetCapitalResponse(responseString);
//        return new Answer(response.getResult().getOutput().getText());
    }

    @Override
    public Answer getCapitalWithInfo(GetCapitalRequest capitalRequest) {
        PromptTemplate promptTemplate = new PromptTemplate(getCapitalWithInfoPrompt);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", capitalRequest.stateOrCountry()));
        ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getText());
    }

    @Override
    public String getAnswer(String question) {
        PromptTemplate promptTemplate = new PromptTemplate(question);
        Prompt prompt = promptTemplate.create();


        ChatResponse response = chatClient.prompt(prompt).advisors(new SimpleLoggerAdvisor()).call().chatResponse();
        System.out.println("raw response" + response);

        var resp = chatClient.prompt(prompt).advisors(new SimpleLoggerAdvisor()).call();
        String responseContent = resp.content();

        System.out.println("Raw model response: " + response);
//        return response.getResult().getOutput().getText();
        return responseContent;
    }
}
