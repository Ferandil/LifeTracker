package kobdratenkov.ncedu.ru.lifetracker;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.List;

import kobdratenkov.ncedu.ru.lifetracker.model.Coordinate;
import kobdratenkov.ncedu.ru.lifetracker.model.Route;

public class Reader implements Runnable {
    private Context context;
    private List<Route> userRoutes;
    private int successfulReading = 2; // 0 - error while reading, 1 - fully readed, 2 - reading in progress

    public Reader(Context context, List<Route> userRoutest){
        this.context = context;
        this.userRoutes = userRoutest;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        BufferedReader bufferedReader = null;
        String userCoordLine;
        UserCoord coordToAddInList;
        File[] routesFiles;
        String[] filesPath;
        Coordinate coordToAdd;
        Route routeToAdd = null;

        try {

            routesFiles = context.getFilesDir().listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.matches("userData\\d+\\.txt");
                }
            });

        } catch (Exception e) {
            successfulReading = 0;
            return;
            //file not found exception
        }
        if(routesFiles.length != 0)
        {
            for (File routefile : routesFiles) {
                try {
                    routeToAdd = new Route();
                    bufferedReader = new BufferedReader(new InputStreamReader(context.openFileInput(routefile.getName())));
                    TypeReference<UserCoord> typeReference = new TypeReference<UserCoord>() {};
                    try{
                        while((userCoordLine = bufferedReader.readLine()) != null){
                            coordToAddInList = mapper.readValue(userCoordLine,typeReference);
                            coordToAdd = new Coordinate(coordToAddInList);
                            routeToAdd.addCoordinate(coordToAdd);
                        }
                        successfulReading = 1;
                    }catch (Exception ex){
                        successfulReading = 0;
                    }
                } catch (FileNotFoundException e) {
                    successfulReading = 0;
                }
                if(routeToAdd != null){
                    userRoutes.add(routeToAdd);
                }
            }
        }
    }
    public int getSuccessfulReading(){
        return this.successfulReading;
    }
}
