package kobdratenkov.ncedu.ru.lifetracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Queue;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

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
        String addToFileName =((Long)Calendar.getInstance().getTimeInMillis()).toString();
        String fileName = "userData" + addToFileName + ".txt";
        /*File file = new File("/Android/data/lifetracker",fileName);

        String testfilepath;

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_APPEND)));
            Iterator<UserCoord> queueIterator = dataQueue.iterator();
            while(queueIterator.hasNext()){
                userCoord = queueIterator.next();
                jsonString = mapper.writeValueAsString(userCoord) + "\n";
                bufferedWriter.write(jsonString);
                dataQueue.poll();
            }
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
