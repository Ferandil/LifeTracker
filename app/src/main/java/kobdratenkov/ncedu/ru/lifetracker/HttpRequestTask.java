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

import kobdratenkov.ncedu.ru.lifetracker.model.RouteForTransfer;

public class HttpRequestTask extends AsyncTask<Void, Void, Void> {

    Queue<UserCoord> userCoords = new ArrayBlockingQueue<UserCoord>(50);
    private RouteForTransfer routeForTransfer;
    private String serverIp = "192.168.0.100";

    public HttpRequestTask(RouteForTransfer routeForTransfer, String serverIp){
        this.routeForTransfer = routeForTransfer;
        this.serverIp = serverIp;
    }
    @Override
    protected Void doInBackground(Void... params) {
        try{
            String uRL = "http://"+ serverIp +":8080/upload";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("Authorization", "Basic " + routeForTransfer.getToken());
            MultiValueMap<String, RouteForTransfer> postBody = new LinkedMultiValueMap<String, RouteForTransfer>();
            postBody.add("coords", routeForTransfer);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpEntity<RouteForTransfer> post = new HttpEntity<RouteForTransfer>(routeForTransfer, headers);
            ResponseEntity<String> uri = restTemplate.exchange(uRL, HttpMethod.POST, post, String.class);
            uRL = "";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
