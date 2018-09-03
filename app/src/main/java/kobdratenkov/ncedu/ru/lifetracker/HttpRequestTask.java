package kobdratenkov.ncedu.ru.lifetracker;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class HttpRequestTask extends AsyncTask<Void, Void, Void> {

    Queue<UserCoord> userCoords = new ArrayBlockingQueue<UserCoord>(50);

    public HttpRequestTask(Queue<UserCoord> userCoords){
        this.userCoords = userCoords;
    }
    @Override
    protected Void doInBackground(Void... params) {
        try{
            String uRL = "http://192.168.0.100:8080/upload";
            String uRLToLogin = "http://192.168.0.100:8080/upload/login";
            HttpHeaders headers = new HttpHeaders();
            //headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            UserCoord coordToSend = userCoords.peek();
            UserCoord retCoords;
            MultiValueMap<String, UserCoord> postBody = new LinkedMultiValueMap<String, UserCoord>();
            postBody.add("coords", coordToSend);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpEntity<UserCoord> post = new HttpEntity<UserCoord>(coordToSend, headers);
            ResponseEntity<String> uri = restTemplate.exchange(uRL, HttpMethod.POST, post, String.class);

            User loginData = new User("alex", "1234");
            HttpEntity<User> loginDataHttpEntity = new HttpEntity<User>(loginData, headers);
            ResponseEntity<String> uriLogin = restTemplate.exchange(uRLToLogin, HttpMethod.POST, loginDataHttpEntity, String.class);

            uRL = "";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
