package pl.edu.pwr.lab46.i236764;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

public class Functions {

    public String jsonToStringFromAssetFolder(String fileName, Context context) throws IOException
    {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(fileName);

        byte[] data = new byte[file.available()];
        file.read(data);
        file.close();
        return new String(data);
    }
}
