package tr.edu.alparslan.havadrumu;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.model.GlideUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class MainActivity extends AppCompatActivity {


    private TextView txt_sehir,txt_aciklama,txt_sicaklik,txt_weather;
    private Button button;
    private EditText editText;
    private ImageView imageView;
    ImageView imageView2;
    String sehir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txt_sehir=(TextView)findViewById(R.id.txt_sehir);
        txt_aciklama=(TextView)findViewById(R.id.txt_aciklama);
        txt_sicaklik=(TextView)findViewById(R.id.txt_sicaklik);
        txt_weather=(TextView)findViewById(R.id.txt_weather);
        button=(Button)findViewById(R.id.button);
        editText=(EditText)findViewById(R.id.editText);
        imageView=(ImageView)findViewById(R.id.imageView);
        imageView2=(ImageView)findViewById(R.id.imageView2);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonParse jsonParse = new JsonParse();
                sehir = String.valueOf(editText.getText());
                new JsonParse().execute();
            }
        });

    }

    protected class JsonParse extends AsyncTask<Void, Void, Void> {

        String result_main ="";
        String result_description = "";
        String result_icon = "";
        int result_temp;
        String result_city;
        Bitmap bitImage;


        @Override
        protected Void doInBackground(Void... params) {
            String result="";
            try {
                URL weather_url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+sehir+"&appid=3187adfaab1683a544f8e9d4a6ef9a42");//Url'mizi
                 BufferedReader bufferedReader = null;
                bufferedReader = new BufferedReader(new InputStreamReader(weather_url.openStream()));//url'yi okuyacak bufferReader'a gönderdik
                String line = null;
                while((line = bufferedReader.readLine()) != null){//satırları tek tek aldık ve ekledik
                    result += line;
                }
                bufferedReader.close();

                JSONObject jsonObject = new JSONObject(result);

                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                JSONObject jsonObject_weather = jsonArray.getJSONObject(0);

                result_main = jsonObject_weather.getString("main");
                result_description = jsonObject_weather.getString("description");
                result_icon = jsonObject_weather.getString("icon");

                JSONObject jsonObject_main = jsonObject.getJSONObject("main");
                Double temp = jsonObject_main.getDouble("temp");
                result_city = jsonObject.getString("name");
                result_temp = (int) (temp-273);

                URL icon_url = new URL("http://openweathermap.org/img/w/"+result_icon+".png");

                bitImage = BitmapFactory.decodeStream(icon_url.openConnection().getInputStream());


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            txt_sicaklik.setText(String.valueOf(result_temp));
            txt_weather.setText(result_main);
            txt_sehir.setText(result_city);
            txt_aciklama.setText(result_description);
            imageView.setImageBitmap(bitImage);
            if (result_main.equals("Clear")) {
                Glide.with(MainActivity.this)
                        .load("https://st3.depositphotos.com/1162190/14632/i/1600/depositphotos_146328963-stock-photo-blue-sea-and-sky-white.jpg")
                        .into(imageView2);
            } if (result_main.equals("Clouds")) {
                Glide.with(MainActivity.this)
                        .load("https://www.cybernewspr.com/web/images/media/2018-01B/NUBOSIDAD-ARCHIVO-1.jpg")
                        .into(imageView2);

            }  if (result_main.equals("rainy")) {
                Glide.with(MainActivity.this)
                        .load("https://images.pexels.com/photos/68357/pexels-photo-68357.jpeg?cs=srgb&dl=rain-rain-on-window-raindrops-68357.jpg&fm=jpg")
                        .into(imageView2);
            }  if (result_main.equals("Partly clouds")) {
                Glide.with(MainActivity.this)
                        .load("https://upload.wikimedia.org/wikipedia/commons/0/00/Flickr_-_Nicholas_T_-_Partly_Cloudy.jpg")
                        .into(imageView2);
            }  if (result_main.equals("Dust")) {
                Glide.with(MainActivity.this)
                        .load("https://images.pexels.com/photos/45222/forest-fog-nature-winter-45222.jpeg?cs=srgb&dl=cloudy-fog-foggy-45222.jpg&fm=jpg")
                        .into(imageView2);

            }



            super.onPostExecute(aVoid);

            //tek tek gerekli olan kısımlara yerleştirdik aldığımız verileri
        }


    }


}

