package com.dch.rv;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static android.os.Environment.DIRECTORY_DCIM;
import static android.os.Environment.DIRECTORY_DOCUMENTS;
import static android.os.Environment.getDataDirectory;

public class MainActivity extends AppCompatActivity {

    private ListView mList;
    private TextView tw;
    private int count;
    private Button speakButton;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private ArrayList<String> listOfCommands;
    OutputStream outputStream;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Developed by Dmitriy Chukarin");
        setContentView(R.layout.activity_main);
        speakButton = (Button) findViewById(R.id.button);
        //speakButton.setOnClickListener(this);
        mList = (ListView) findViewById(R.id.myList);
        listOfCommands = new ArrayList<>();
        //tw = (TextView) findViewById(R.id.textView3);
        count=0;

        //listOfCommands.add("some test" + new Random().nextDouble());
        //Toast t = Toast.makeText(getApplicationContext(), getFilesDir().toString(), Toast.LENGTH_SHORT);
        //t.show();
        //System.out.println(getFilesDir());
    }

    public void onClick(View v) {
        startSpeak();
    }

    public void startSpeak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); // намерение для вызова формы обработки речи (ОР)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); // сюда он слушает и запоминает
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Скажите действие");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE); // вызываем активность ОР
        //listOfCommands.add("some test");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList commandList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for (int i = 0; i < commandList.size(); i++) {
                String str = commandList.get(i).toString();
                commandList.remove(i);
                commandList.add((Object) str.toLowerCase());
            }
            mList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, commandList));
            if (commandList.contains("изъят талон номер один не проведено собеседование") || commandList.contains("изъять талон номер один не проведено собеседование") || commandList.contains("изъять талон номер один не проведена собеседование") || commandList.contains("изъять талон номер 1 не проведено собеседование") || commandList.contains("изъять талон номер один не проведено собеседовании")) {
                listOfCommands.add("В нарушение требований п. 3.2. Положения о порядке применения предупредительных талонов по охране труда в ОАО «РЖД», утвержденного распоряжением от 01.10.2015 № 2351р с работником не проведено собеседовании при изъятии талона № 1.");
                comAccept();
            }

            if (commandList.contains("нет записи в ведомости ксот-п первый уровень") || commandList.contains("нет записи ведомости по ксот-п 1 уровень") || commandList.contains("нет записи в ведомости ксот-п 1 уровень") || commandList.contains("нет записи ведомости ксот-п первый уровень") || commandList.contains("нет записей в ведомости ксот-п первый уровень")) {
                listOfCommands.add("В нарушение требований п. 3.1. Методики по организации КСОТ-П в дирекциях управления движением, утвержденной и.о. ЦДГ Райзером Р.В. от 20.12.2016 не оформлена запись о выявленных нарушениях в ведомость несоответствия при проведении первого уровня контроля.");
                comAccept();
            }
            if (commandList.contains("не проведён внеплановый инструктаж") || commandList.contains("и не проведён внеплановый инструктаж") || commandList.contains("а не проведён внеплановый инструктаж") || commandList.contains("нет проведён внеплановый инструктаж") || commandList.contains("не проведен внеплановый инструктаж") || commandList.contains("и не проведен внеплановый инструктаж") || commandList.contains("а не проведен внеплановый инструктаж") || commandList.contains("нет проведен внеплановый инструктаж")) {
                listOfCommands.add("В нарушение требований п. 6.2.1. СТО РЖД 15.011-2015 «Система управления охраной труда в ОАО «РЖД». Организация обучения», утвержденная распоряжением ОАО «РЖД» от 25.12.2015 № 3081р внеплановый инструктаж по несчастному случаю работникам не проведен, запись в журнале регистрации инструктажей отсутствует.");
                comAccept();
            }
            if (commandList.contains("нарушение сроков повторного инструктажа") || commandList.contains("нарушении сроков повторного инструктажа") || commandList.contains("нарушение сроков в повторного инструктажа") || commandList.contains("нарушения сроков повторного инструктажа")) {
                listOfCommands.add("В нарушение требований п. 6.1.1. СТО РЖД 15.011-2015 «Система управления охраной труда в ОАО «РЖД». Организация обучения», утвержденная распоряжением ОАО «РЖД» от 25.12.2015 № 3081р не соблюдаются сроки проведения повторного инструктажа.");
                comAccept();
            }
            if (commandList.contains("не проведение целевого инструктажа") || commandList.contains("не проведения целевого инструктажа") || commandList.contains("ни проведение целевого инструктажа") || commandList.contains("ни проведения целевого инструктажа") || commandList.contains("не проведении целевого инструктажа")) {
                listOfCommands.add("В нарушение требований п. 6.3.1. СТО РЖД 15.011-2015 «Система управления охраной труда в ОАО «РЖД». Организация обучения», утвержденная распоряжением ОАО «РЖД» от 25.12.2015 № 3081р не проведен целевой инструктаж при изменении метеорологических условий.");
                comAccept();
            }
            if (commandList.contains("отсутствуют талоны по охране труда") || commandList.contains("отсутствует талоны по охране труда") || commandList.contains("отсутствует в талоны по охране труда")) {
                listOfCommands.add("В нарушение требований п. 1.5. Положения о порядке применения предупредительных талонов по охране труда в ОАО «РЖД», утвержденного распоряжением от 01.10.2015 № 2351р у работников отсутствуют предупредительные талоны по охране труда при выполнении должностных обязанностей.");
                comAccept();
            }
            if (commandList.contains("при изъятии талона не выдается следующий талон") || commandList.contains("при изъятие талона не выдается следующий талон") || commandList.contains("при изъятие талона не выдается следующие талон") || commandList.contains("при изъятие талона не выдаётся следующий талон")) {
                listOfCommands.add("В нарушение требований п. 2.2. Положения о порядке применения предупредительных талонов по охране труда в ОАО «РЖД», утвержденного распоряжением от 01.10.2015 № 2351р при изъятии предупредительного талона № 1 за нарушение требований охраны труда, предупредительный талон № 2 работнику не выдан.");
                comAccept();
            }
            if (commandList.contains("несвоевременный возврат талона по охране труда") || commandList.contains("несвоевременный возврат в талона по охране труда")) {
                listOfCommands.add("В нарушение требований п. 3.2. Положения о порядке применения предупредительных талонов по охране труда в ОАО «РЖД», утвержденного распоряжением от 01.10.2015 № 2351р по истечении установленного срока не возвращен предупредительный талон № 1.");
                comAccept();
            }
            if (commandList.contains("изъят талон номер два не проведен внеплановый инструктаж") || commandList.contains("изъять талон номер два не проведен внеплановый инструктаж") || commandList.contains("изъять талон номер два не проведён внеплановый инструктаж") || commandList.contains("изъять талон номер 2 не проведен внеплановый инструктаж") || commandList.contains("изъять талон номер 2 не проведён внеплановый инструктаж") || commandList.contains("изъято талон номер 2 не проведён внеплановый инструктаж") || commandList.contains("изъят талон номер 2 не проведён внеплановый инструктаж")) {
                listOfCommands.add("В нарушение требований п. 3.3. Положения о порядке применения предупредительных талонов по охране труда в ОАО «РЖД», утвержденного распоряжением от 01.10.2015 № 2351р работнику не проведен внеплановый инструктаж при изъятии предупредительного талона № 2.");
                comAccept();
            }
            if (commandList.contains("настил внутри колеи не соответствует") || commandList.contains("настил внутри колеи не соответствуют") || commandList.contains("настил внутри коли не соответствует") || commandList.contains("настилы внутри колеи не соответствует") || commandList.contains("настилы внутри колеи не соответствуют") || commandList.contains("nastile внутри колеи не соответствует")) {
                listOfCommands.add("В нарушение требования п. 5.15. СТО РЖД 15-.015-2016 «Проходы служебные на объектах ОАО «РЖД». Технические требования, правила устройства и содержания», утвержденного распоряжением ОАО «РЖД» от 14.12.2016 № 2355р настил внутри колеи ниже головки рельсов.");
                comAccept();
            }
            if (commandList.contains("маршрут служебного прохода не очищен") || commandList.contains("маршрут служебного прохода не очищенный") || commandList.contains("маршрут служебного прохода не очищенные")) {
                listOfCommands.add("В нарушении требований стандарта ОАО «РЖД» «Проходы служебные на объектах ОАО «РЖД». Технических требования, правила устройства и содержания» утв. распоряжением ОАО «РЖД» от 14.12.2016г. №2533р маршрут служебного прохода не очищен от наледи, не посыпан песком, заросли травы.");
                comAccept();
            }
            if (commandList.contains("переход пути не под прямым углом") || commandList.contains("переход пути ни под прямым углом") || commandList.contains("переход в пути не под прямым углом") || commandList.contains("переход в пути ни под прямым углом") || commandList.contains("переход по пути не под прямым углом") || commandList.contains("переход по пути ни под прямым углом") || commandList.contains("переход путь и не под прямым углом")) {
                listOfCommands.add("В нарушение требований  п. 1.43. Правил по охране труда в хозяйстве перевозок ОАО «РЖД», утвержденных распоряжением ОАО «РЖД» от 04.02.2013 № 276р работник переходил железнодорожные пути не под прямым углом.");
                comAccept();
            }
            if (commandList.contains("при движении вагонами вперед не на первой подножке") || commandList.contains("при движении вагонами вперед не на первой подножки") || commandList.contains("при движении вагонами вперед не на первый подножки") || commandList.contains("при движении вагонами вперед нина 1 подножки") || commandList.contains("при движении вагонами вперед ни на 1 подножки") || commandList.contains("при движении вагонами вперед не на 1 подножки")) {
                listOfCommands.add("В нарушение требований  п. 2.1.13 Правил по охране труда в хозяйстве перевозок ОАО «РЖД», утвержденных распоряжением ОАО «РЖД» от 04.02.2013 № 276р составитель поездов при сопровождении состава вагонами вперед находится не на первой по ходу движения специальной подножке грузового вагона.");
                comAccept();
            }
            if (commandList.contains("нарушения сопровождения платформ") || commandList.contains("нарушение сопровождения платформ") || commandList.contains("нарушения сопровождения платформа") || commandList.contains("нарушения сопровождение платформа") || commandList.contains("нарушения сопровождения платформы") || commandList.contains("нарушение сопровождение платформы")) {
                listOfCommands.add("В нарушение требований  п. 2.1.13 Правил по охране труда в хозяйстве перевозок ОАО «РЖД», утвержденных распоряжением ОАО «РЖД» от 04.02.2013 № 276р составитель поездов сопровождал состав стоя на платформе (или сидя на ее бортах).");
                comAccept();
            }
            if (commandList.contains("работник без сиз") || commandList.contains("работник без сезон") || commandList.contains("работник без sis") || commandList.contains("работник без сис") || commandList.contains("работник без cic") || commandList.contains("работник без siz") || commandList.contains("работник без ciz")) {
                listOfCommands.add("В нарушение требований п. 1.33. Правил по охране труда в хозяйстве перевозок ОАО «РЖД», утвержденных распоряжением ОАО «РЖД» от 04.02.2013 № 276р работник не применяет предусмотренные виды спецодежды, спецобуви.");
                comAccept();
            }
            if (commandList.contains("отсутствует логотип на сигнальном жилете") || commandList.contains("отсутствуют логотип на сигнальном жилете") || commandList.contains("отсутствует в логотип на сигнальном жилете") || commandList.contains("отсутствие логотипа на сигнальном жилете") || commandList.contains("отсутствии логотипа на сигнальном жилете") || commandList.contains("отсутствие в логотипа на сигнальном жилете") || commandList.contains("отсутствие логотипы на сигнальном жилете")) {
                listOfCommands.add("В нарушение требований п. 10.1 Положения об организации в ОАО «РЖД» работы по системе информации «Человек на пути», утвержденного распоряжением от 14.03.2016 № 410р на сигнальном жилете отсутствует трафарет указывающий принадлежность к структурному подразделению.");
                comAccept();
            }
            if (commandList.contains("ящик для песка не окрашен") || commandList.contains("ящик для песка неокрашенный") || commandList.contains("ящик для песка неокрашенном") || commandList.contains("ящик для песка ни окрашен") || commandList.contains("ящик для песка неокрашенные")) {
                listOfCommands.add("В нарушение требования п. 11.2. СТО РЖД 15-.015-2016 «Проходы служебные на объектах ОАО «РЖД». Технические требования, правила устройства и содержания» ящик для хранения песка не окрашен в чередующиеся полосы черного и желтого цвета.");
                comAccept();
            }
            if (commandList.contains("не проведена проверка знаний по электробезопасности") || commandList.contains("а не проведена проверка знаний по электробезопасности") || commandList.contains("ни проведена проверка знаний по электробезопасности") || commandList.contains("не проведено проверка знаний по электробезопасности") || commandList.contains("и не проведена проверка знаний по электробезопасности")) {
                listOfCommands.add("В нарушении требований п.5 стандарта ОАО «РЖД» СТО РЖД 15.013-2015 «Система управления охраной труда ОАО «РЖД». Электрическая безопасность. Общие положения» не проведена очередная проверка знаний по нормам и правилам работе в электроустановках.");
                comAccept();
            }
            if (commandList.contains("отсутствуют знаки расцепа на горке") || commandList.contains("отсутствуют знаки astepe на горке") || commandList.contains("отсутствуют знаки расцеп а на горке") || commandList.contains("отсутствует знаки astepe на горке") || commandList.contains("отсутствуют знаки гороскопа на горке") || commandList.contains("отсутствует знаки гороскопа на горке")) {
                listOfCommands.add("В нарушение требований п. 2.4.1. Правил по охране труда в хозяйстве перевозок ОАО «РЖД», утвержденных распоряжением ОАО «РЖД» от 04.02.2013 № 276р рабочая зона расцепа вагонов с горки не обозначена знаками «Начало роспуска» и «Конец роспуска».");
                comAccept();
            }
            if (commandList.contains("неисправный огнетушитель") || commandList.contains("неисправной огнетушитель") || commandList.contains("неисправный огнетушителя") || commandList.contains("неисправные огнетушители") || commandList.contains("неисправные огнетушителя") || commandList.contains("неисправные огнетушителе") || commandList.contains("неисправная огнетушители") || commandList.contains("неисправное огнетушители")) {
                listOfCommands.add("В нарушение требований п.4.3.5.-4.3.6. СП 9.13130.2009 «Свод правил. Техника пожарная. Огнетушители. Требований к эксплуатации», утвержденного приказом МЧС РФ от 25.03.2009 № 179 применяются неисправные огнетушители (без раструбов, без пломб и т.д.).");
                comAccept();
            }
            if (commandList.contains("отсутствуют сведения о проверке огнетушителей") || commandList.contains("отсутствует сведения о проверки огнетушителей") || commandList.contains("отсутствуют сведения о проверки огнетушителей")) {
                listOfCommands.add("В нарушение требований п. 4.3-.4.4 СП 9.13130.2009 «Свод правил. Техника пожарная. Огнетушители. Требований к эксплуатации», утвержденного приказом МЧС РФ от 25.03.2009 № 179  на огнетушителях отсутствуют сведения о  периодической проверке (перезарядке) огнетушителей.");
                comAccept();
            }
            if (commandList.contains("огнетушитель на полу не зафиксирован от падения") || commandList.contains("огнетушители на полу не зафиксирован от падения") || commandList.contains("огнетушители на полу не зафиксированы от падения") || commandList.contains("огнетушители на полу не зафиксирован отпадения") || commandList.contains("огнетушителей на полу не зафиксирован от падения")) {
                listOfCommands.add("В нарушение требований 4.2.7. СП 9.13130.2009 «Свод правил. Техника пожарная. Огнетушители. Требований к эксплуатации», утвержденного приказом МЧС РФ от 25.03.2009 № 179 огнетушители, размещенные на полу,  не имеют надежной фиксации от возможного падения  при случайном воздействии.");
                comAccept();
            }
            if (commandList.contains("захламленность станции") || commandList.contains("захламленность к станции") || commandList.contains("захламленность подстанции") || commandList.contains("захламленность в станции") || commandList.contains("захламленность от станции")) {
                listOfCommands.add("Территория железнодорожной станции не приведена в эстетический вид (захламленность территории).");
                comAccept();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void comAccept() {
        Toast toast = Toast.makeText(getApplicationContext(), "Команда определена", Toast.LENGTH_SHORT);
        toast.show();
        count++;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void save(View v) {
        if (listOfCommands.size()>0) onSave();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void onSave() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        String fileName = "";
        fileName += df.format(c.getTime()) + ".txt";

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)){
            if (Build.VERSION.SDK_INT>=23){
                if (checkPermission()){
                    File sdcard = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS);
                    File dir = new File(sdcard.getAbsolutePath()+"/RZD/");
                    dir.mkdir();
                    File file = new File(dir, fileName);
                    FileOutputStream os = null;
                    try {
                        os = new FileOutputStream(file);
                        for (int i = 0; i < listOfCommands.size(); i++) {
                            os.write(listOfCommands.get(i).getBytes());
                            os.write("\n\n".getBytes());
                        }
                        os.close();
                        MediaScannerConnection.scanFile(this, new String[]{dir.toString()},null,null);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    requestPermission();
                }
            }
            else {
                File sdcard = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS);
                File dir = new File(sdcard.getAbsolutePath()+"/RZD/");
                dir.mkdir();
                File file = new File(dir, fileName+".txt");
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(file);
                    for (int i = 0; i < listOfCommands.size(); i++) {
                        os.write(listOfCommands.get(i).getBytes());
                        os.write("\n\n".getBytes());
                    }
                    os.close();
                    MediaScannerConnection.scanFile(this, new String[]{dir.toString()},null,null);

                }
                catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
        Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        copyFile(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getAbsolutePath()+"/RZD/",fileName+".txt", Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getAbsolutePath()+"/RZD/");
        listOfCommands.clear();


        /*
        FileOutputStream fos = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        String filename = "";
        filename += df.format(c.getTime()) + ".txt";
        //filename = "abcdef.txt";
        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsolutePath() + "/Bang/");
        dir.mkdir();
        File file = new File(dir, System.currentTimeMillis()+".txt");
        try {
            outputStream = new FileOutputStream(file);
            for (int i = 0; i < listOfCommands.size(); i++) {
                outputStream.write(listOfCommands.get(i).getBytes());
                outputStream.write("\n\n".getBytes());
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        listOfCommands.clear();

         */
    }


    private void copyFile(String inputPath, String inputFile, String outputPath){
        InputStream in = null;
        OutputStream out = null;
        try{
            File dir = new File(outputPath);
            if (!dir.exists()){
                dir.mkdirs();
            }
            in = new FileInputStream(inputPath+inputFile);
            out = new FileOutputStream(outputPath+inputFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read=in.read(buffer))!=-1){
                out.write(buffer,0,read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result== PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            return false;
        }
    }

    private void requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this,"Write External Storage permission allows us to create files. Please allow this permission in App Settings.",Toast.LENGTH_LONG).show();
        }
        else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
            break;
        }
    }
}