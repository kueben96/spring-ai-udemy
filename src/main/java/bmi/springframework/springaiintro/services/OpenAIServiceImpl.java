package bmi.springframework.springaiintro.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ai.openai.api.OpenAiApi;

@Service
public class OpenAIServiceImpl implements OpenAIService {


 private final ChatClient chatClient;


      public OpenAIServiceImpl(ChatClient.Builder chatClientBuilder) {
      this.chatClient = chatClientBuilder.build()  ;
    }
    @Override
    public String getAnswer(String question) {
        PromptTemplate promptTemplate = new PromptTemplate(question);
        Prompt prompt = promptTemplate.create();


       ChatResponse response = chatClient.prompt(prompt).advisors(new SimpleLoggerAdvisor()).call().chatResponse();
        System.out.println("raw response" + response);

        var resp = chatClient.prompt(prompt).advisors(new SimpleLoggerAdvisor()).call();
       String responseContent =  resp.content();

      System.out.println("Raw model response: " + response);
//        return response.getResult().getOutput().getText();
        return responseContent;
    }
}
