package com.example.assistant;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1 ;
    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    private TextView textView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);
        textView=findViewById(R.id.text_view);
        textToSpeech= new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
            }
        });

        intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }
            @Override
            public void onBeginningOfSpeech() {
            }
            @Override
            public void onRmsChanged(float v) {
            }
            @Override
            public void onBufferReceived(byte[] bytes) {
            }
            @Override
            public void onEndOfSpeech() {
            }
            @Override
            public void onError(int i) {
            }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResults(Bundle result) {
                ArrayList<String> matches=result.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String string;
                textView.setText("");
                if (matches!=null) {
                    string = matches.get(0);
                    textView.setText(string);
                    //copied text at desktop
                    try {
                        if(string.contains("open") || string.contains("launch") || string.contains("play")) {
                            String[] speech = string.split(" ", 2);
                            String appName = speech[1];
                           /* Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.setPackage("com.whatsapp");
                            startActivity(sendIntent);*/
                            if(appName =="WhatsApp") {
                                textToSpeech.speak("Hai Madonna, what can I do for you", TextToSpeech.QUEUE_FLUSH, null, null);
                                PackageManager pm = getPackageManager();
                                String pkgName = pm.getInstallerPackageName(appName);

                                Intent launchApp = getPackageManager().getLaunchIntentForPackage(pkgName);
                                startActivity(launchApp);
                            }
                            else {
                                textToSpeech.speak("hi", TextToSpeech.QUEUE_FLUSH, null, null);
                            }



                        }
                    } catch (Exception ex) {
                        Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    switch (string){
                        case "create a file": case"create file": case "can you create a file": case "create a file if you can": case "blue create a file":
                            createMethod();
                            break;

                        case "open contact":

                            dialPhoneNumber("");
                            break;
                        case "what else can you do":
                            textToSpeech.speak("I can open apps for you, set alarm , create a text, try using open followed by the app name"
                                    ,TextToSpeech.QUEUE_FLUSH,null,null);
                            break;
                        case "thank you":
                            textToSpeech.speak("bye madonna see you later"
                                    ,TextToSpeech.QUEUE_FLUSH,null,null);
                            break;
                        case "open Google":
                            searchWeb(string);
                            break;
                        case "open camera" :
                            capturePhoto();
                            break;
                        case "open camera in video mode":
                            captureVideo();
                            break;
                        case "set alarm":
                            createAlarm();
                            break;
                        case "open maps":
                            showMap();
                            break;
                        case "open Wi-Fi settings":
                            openWifiSettings();
                            break;
                        case "open aeroplane mode":
                            openAirPlaneModeSettings();
                            break;
                        case "open APN settings":
                            openApnSettings();
                            break;
                        case "open Bluetooth settings":
                            openBluetoothSettings();
                            break;
                        case "open date settings":
                            openDateSettings();
                            break;
                        case "open display settings":
                            openDisplaySettings();
                            break;
                        case "open internal memory settings":
                            openInternalSettings();
                            break;
                        case "open memory card settings":
                            openMemoryCardSettings();
                        case "open input method settings":
                            openIOSettings();
                            break;
                        case "open local settings" :
                            openlocalSettings();
                            break;
                        case "open location settings":
                            openLocationSettings();
                            break;
                        case "open settings":
                            openSettings();
                            break;
                        case "open security settings":
                            openSecuritySettings();
                            break;
                        case "open wireless settings":
                            openWirelessSettings();
                            break;

                        default:
                            //throw new IllegalStateException("Unexpected value: " + string);
                            textToSpeech.speak("sorry, i don't have an answer for that ! ",TextToSpeech.QUEUE_FLUSH,null,null);
                    }

                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {
            }
            @Override
            public void onEvent(int i, Bundle bundle) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startspeaking(View view){



        textToSpeech.speak("Hai Madonna, what can I do for you", TextToSpeech.QUEUE_FLUSH, null, null);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        speechRecognizer.startListening(intent);
    }
    public void listen(View view){
       /*
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        speechRecognizer.startListening(intent);
    }
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void createMethod(){
        File file=new File(Environment.getExternalStorageDirectory()+File.separator+"personalAssistant.txt");
        try{

            if (!file.exists()) {
                file.createNewFile();

            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append("my first personal assistant ");
            fileWriter.flush();
            fileWriter.close();


        }catch ( IOException e){
            e.printStackTrace();
            return;
        }
        textToSpeech.speak("the file has been created",TextToSpeech.QUEUE_FLUSH,null,null);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("opening contact",TextToSpeech.QUEUE_FLUSH,null,null);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void searchWeb(String query) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }


        textToSpeech.speak("opening google",TextToSpeech.QUEUE_FLUSH,null,null);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void capturePhoto() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
        textToSpeech.speak("opening camera",TextToSpeech.QUEUE_FLUSH,null,null);

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void captureVideo() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
        textToSpeech.speak("opening camera in video mode",TextToSpeech.QUEUE_FLUSH,null,null);

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createAlarm() {
        String message="";
        int hour=0;
        int minutes=0;
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("okay",TextToSpeech.QUEUE_FLUSH,null,null);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showMap() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri geoLocation=null;
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("opening google map",TextToSpeech.QUEUE_FLUSH,null,null);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openWifiSettings() {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("opening Wi-Fi settings",TextToSpeech.QUEUE_FLUSH,null,null);

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openSettings() {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("opening settings",TextToSpeech.QUEUE_FLUSH,null,null);

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openWirelessSettings() {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("opening wireless settings",TextToSpeech.QUEUE_FLUSH,null,null);

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openAirPlaneModeSettings() {
        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("opening airplane mode settings",TextToSpeech.QUEUE_FLUSH,null,null);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openApnSettings() {
        Intent intent = new Intent(Settings.ACTION_APN_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("opening A P N settings",TextToSpeech.QUEUE_FLUSH,null,null);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openBluetoothSettings() {
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("opening bluetooth settings",TextToSpeech.QUEUE_FLUSH,null,null);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openDateSettings() {
        Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("opening date settings",TextToSpeech.QUEUE_FLUSH,null,null);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openlocalSettings() {
        Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("opening local settings",TextToSpeech.QUEUE_FLUSH,null,null);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openIOSettings() {
        Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("opening input settings",TextToSpeech.QUEUE_FLUSH,null,null);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openDisplaySettings() {
        Intent intent = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("opening display settings",TextToSpeech.QUEUE_FLUSH,null,null);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openSecuritySettings() {
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("opening security settings",TextToSpeech.QUEUE_FLUSH,null,null);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openLocationSettings() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("opening location settings",TextToSpeech.QUEUE_FLUSH,null,null);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openInternalSettings() {
        Intent intent = new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("opening internal settings",TextToSpeech.QUEUE_FLUSH,null,null);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openMemoryCardSettings() {
        Intent intent = new Intent(Settings.ACTION_MEMORY_CARD_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        textToSpeech.speak("opening memory card settings",TextToSpeech.QUEUE_FLUSH,null,null);

    }
    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void listening(){

        textToSpeech.speak("Hai Madonna how can I help you",TextToSpeech.QUEUE_FLUSH,null,null);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        speechRecognizer.startListening(intent);
    }*/



}

