package bmi.springframework.springaiintro.services;

import bmi.springframework.springaiintro.model.Answer;
import bmi.springframework.springaiintro.model.GetCapitalRequest;
import bmi.springframework.springaiintro.model.GetCapitalResponse;
import bmi.springframework.springaiintro.model.Question;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIServiceImpl implements OpenAIService {


    private final ChatClient chatClient;
    private final ChatModel chatModel;

    @Value("classpath:templates/get-capital-prompt.st")
    private Resource getCapitalPrompt;
    @Value("classpath:templates/get-capital-with-info-prompt.st")
    private Resource getCapitalWithInfoPrompt;


    @Value("${my.openai.model2}")
    private String model2;

    public OpenAIServiceImpl(ChatClient.Builder chatClientBuilder, ChatModel chatModel) {
        this.chatClient = chatClientBuilder.build();
        this.chatModel = chatModel;
    }

    @Override
    public Answer getAnswer(Question question) {
        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();

//        OpenAiChatProperties openAiChatProperties = new OpenAiChatProperties();
//        OpenAiChatOptions openAiChatOptions = new OpenAiChatOptions.builder(openAiChatProperties.getOptions()).;

        String systemPrompt = """
                You're a helpful assistant. Your role is a city tourism guide. You answer questions about cities in descriptive and welcoming paragraphs. You hope the user will visit and enjoy the city.
                """;
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPrompt);
        Message systemMessage = systemPromptTemplate.createMessage();
        Message userMessage = promptTemplate.createMessage();

        List<Message> messages = List.of(systemMessage, userMessage);
        Prompt listedPrompts = new Prompt(messages);


//        ChatResponse response = chatClient.prompt(prompt).advisors(new SimpleLoggerAdvisor()).call().chatResponse();
        ChatResponse response = chatClient.prompt(listedPrompts).advisors(new SimpleLoggerAdvisor()).call().chatResponse();
        System.out.println("raw response" + response);

        var resp = chatClient.prompt(prompt).advisors(new SimpleLoggerAdvisor()).call();
        String responseContent = resp.content();

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


        Prompt prompt = promptTemplate.create(
                Map.of("stateOrCountry", capitalRequest.stateOrCountry(), "format", format),
                OpenAiChatOptions.builder().model(model2).temperature(0.1).build()
        );
        System.out.println("model2" + model2);
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
