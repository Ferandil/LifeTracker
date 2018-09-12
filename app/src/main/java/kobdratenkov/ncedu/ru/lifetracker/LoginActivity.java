package kobdratenkov.ncedu.ru.lifetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout loginWrapper;
    private TextInputLayout passwordWrapper;
    private EditText loginEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView resoltTextView;
    private Queue<Boolean> resQueue = new ArrayBlockingQueue<Boolean>(1);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        loginWrapper = (TextInputLayout) findViewById(R.id.loginWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        loginEditText = findViewById(R.id.login);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        resoltTextView = findViewById((R.id.resultTextView));
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    public void onClickLoginButton(View view) throws ExecutionException, InterruptedException {
        Boolean loginSucces;
        if(loginEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")){
            return;
        }
        new LoginRequestTask(getApplicationContext(), resQueue).execute(loginEditText.getText().toString(), passwordEditText.getText().toString());
        while (resQueue.isEmpty()){continue;}
        boolean loginRes = resQueue.peek();
        //(new LoginRequestTask(getApplicationContext())).doInBackground(loginEditText.getText().toString(), passwordEditText.getText().toString());
        if(loginRes){
            resoltTextView.setText("Succsess");
            setResult(101);
            //Intent intent = new Intent(getBaseContext(), MainActivity.class);
            //startActivity(intent);
            //getApplicationContext().startActivity(intent);
            finish();
        }else{
            resoltTextView.setText("Wrong password or login");
        }
    }
}
