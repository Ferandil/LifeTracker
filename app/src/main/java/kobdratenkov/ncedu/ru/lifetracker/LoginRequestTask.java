package kobdratenkov.ncedu.ru.lifetracker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import kobdratenkov.ncedu.ru.lifetracker.model.RouteForTransfer;

public class LoginRequestTask extends AsyncTask<String, Void, Boolean> {
    Context context;
    private Queue<Boolean> resQueue = new ArrayBlockingQueue<Boolean>(1);
    boolean result = false;

    public LoginRequestTask(Context context, Queue<Boolean> resQueue){
        this.resQueue = resQueue;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Boolean doInBackground(String... strings) {
        String uRLToLogin = "Http://192.168.0.100:8080/upload/login";
        String url = "http://192.168.0.100:8080/upload/test";
        User loginData = new User(strings[0], strings[1]);
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        String userDataToSend = strings[0] + ":" + strings[1];

        byte[] bytesToEncrypt = userDataToSend.getBytes();
        String encodedData = Base64.getMimeEncoder().encodeToString(bytesToEncrypt);

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Basic" + encodedData);
        HttpEntity<User> dataForTransfer = new HttpEntity<>(loginData, headers);
        HttpEntity<String> loginDataHttpEntity = new HttpEntity<String>(headers);
        ResponseEntity<User> uriLogin = null;
        try{
            //uriLogin = restTemplate.exchange(url, HttpMethod.GET, loginDataHttpEntity, String.class);
            uriLogin = restTemplate.exchange(uRLToLogin, HttpMethod.POST, dataForTransfer, User.class);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        if(uriLogin.getStatusCode().equals(HttpStatus.OK)){
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(context.openFileOutput("token.txt", Context.MODE_PRIVATE)));
                writer.write(uriLogin.getBody().getLogin());
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
            resQueue.add(true);
            return true;
        }else{
            resQueue.add(false);
            return false;
        }
    }
}
