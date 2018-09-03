package kobdratenkov.ncedu.ru.lifetracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Queue;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Writer implements Runnable{
    Queue<UserCoord> dataQueue;
    Context context;

    public Writer (Context context, Queue<UserCoord> dataQueue){
        this.dataQueue = dataQueue;
        this.context = context;
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString;
        UserCoord userCoord;
        File sdFile = null;
        File file = new File("/Android/data/lifetracker","userData");

        String testfilepath;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdPath = Environment.getExternalStorageDirectory();
            sdPath = new File(sdPath.getAbsolutePath() + "/" + "lifeTracker");
            sdPath.mkdirs();
            sdFile = new File(sdPath, "userCoord.txt");
        }
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter sdBufferedWriter = null;
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(context.openFileOutput("userData.txt", Context.MODE_APPEND)));
            if(sdFile != null){
                sdBufferedWriter = new BufferedWriter(new FileWriter(sdFile,true));
            }
            Iterator<UserCoord> queueIterator = dataQueue.iterator();
            while(queueIterator.hasNext()){
                userCoord = queueIterator.next();
                jsonString = mapper.writeValueAsString(userCoord) + "\n";
                bufferedWriter.write(jsonString);
                if(sdFile != null){
                    sdBufferedWriter.write(jsonString);
                }
                dataQueue.poll();
            }
            sdBufferedWriter.close();
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
