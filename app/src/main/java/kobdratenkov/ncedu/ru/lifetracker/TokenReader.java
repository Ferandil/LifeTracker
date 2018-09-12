package kobdratenkov.ncedu.ru.lifetracker;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class TokenReader implements Runnable {
    private Context context;
    private String token = null;

    public TokenReader(Context context){
        this.context = context;
    }
    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput("token.txt")));
            token = reader.readLine();
        } catch (FileNotFoundException e) {
            token = "";
        } catch (IOException e) {
            token = "";
            e.printStackTrace();
        }
    }

    public String getToken() {
        return token;
    }
}
