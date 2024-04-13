package com.msaggik.fourthlessonalbum11;

import static android.view.View.INVISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    // поля
    private ImageView buttonTools;
    private LinearLayout buttons;
    private ImageView buttonCircle, buttonLine, buttonPalette, buttonClearScreen;
    private AlbumView album; // создание объекта представления View

    private Bitmap bitmapPalette; // поле растрового изображения палитры

    private boolean isBrush = true; // задание поля кисти

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // привязка кнопок к разметке
        buttonTools = findViewById(R.id.button_tools);
        buttonCircle = findViewById(R.id.button_circle);
        buttonLine = findViewById(R.id.button_line);
        buttons = findViewById(R.id.buttons);
        buttonPalette = findViewById(R.id.button_palette);
        buttonClearScreen = findViewById(R.id.button_clear);
        album = findViewById(R.id.album); // создание объекта представления View

        // обработка нажатия кнопок
        buttonTools.setOnClickListener(listener);
        buttonCircle.setOnClickListener(listener);
        buttonPalette.setOnClickListener(listener);
        buttonClearScreen.setOnClickListener(listener);
        buttonLine.setOnClickListener(listenerLineButton);
    }

    private final View.OnClickListener listenerLineButton = view -> {
        album.setFigure("line");

    };

    // слушатель для кнопок
    private final View.OnClickListener listener = new View.OnClickListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.button_tools:
                    if (buttons.getVisibility() == INVISIBLE) {
                        buttons.setVisibility(View.VISIBLE);
                    } else {
                        buttons.setVisibility(INVISIBLE);
                    }
                    break;
                case R.id.button_circle:
                    album.setFigure("circle");
                    break;
                case R.id.button_palette:
                    // код для палитры
                    album.setFigure("drawing");
                    // диалоговое окно
                    Dialog dialogAlbumPalette = new Dialog(MainActivity.this, R.style.DialogPalette); // диалоговое окно палитры
                    dialogAlbumPalette.setTitle("Палитра рисования:"); // установление заголовка диалогового окна
                    dialogAlbumPalette.setContentView(R.layout.album_palette); // загрузка созданной ранее разметки

                    // выбор цвета и вывод его во View с id = colorInfoView
                    ImageView colorSelection = dialogAlbumPalette.findViewById(R.id.colorSelection); // создание поля картинки палитры и привязка к разметке по id
                    View colorInfoView = dialogAlbumPalette.findViewById(R.id.colorInfoView); // создание поля View для выбранного цвета и привязка к разметке по id

                    colorSelection.setDrawingCacheEnabled(true); // включение задания кэша картинки
                    colorSelection.buildDrawingCache(true); // включение постройки кэша картинки

                    // задание слушателя обработки касания картинки
                    colorSelection.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            // если пользователь коснулся экрана или поводил по нему пальцем, то
                            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

                                bitmapPalette = colorSelection.getDrawingCache(); // запись пикселя картинки в растровое изображение

                                int pixel = bitmapPalette.getPixel((int)motionEvent.getX(),(int)motionEvent.getY()); // определение цвета выбранного пикселя
                                // считывание отдельных цветовых параметров пикселя
                                int a = Color.alpha(pixel);
                                int r = Color.red(pixel);
                                int g = Color.green(pixel);
                                int b = Color.blue(pixel);

                                // задание цвета фона для View выбора цвета
                                colorInfoView.setBackgroundColor(Color.argb(a, r, g, b));

                                // задание нового цвета для кисти
                                album.setColor(pixel);
                            }
                            return true;
                        }
                    });

                    // определение ластика/кисти
                    @SuppressLint("UseSwitchCompatOrMaterialCode")
                    Switch eraserSwitch = dialogAlbumPalette.findViewById(R.id.eraserChecked); // создание поля и привязка к нему id разметки ластика

                    eraserSwitch.setChecked(!isBrush); // задание положения тумблера
                    // слушатель положения тумблера
                    eraserSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
                            isBrush = !flag; // присваивание поля выбора ластика изменённому значению тумблера
                            album.setBrush(isBrush); // задание выбора ластика или кисти
                        }
                    });

                    // выбор размера ластика/кисти
                    RadioGroup size = dialogAlbumPalette.findViewById(R.id.size); // создание поля и привязка к нему id разметки размеров кисти и ластика
                    size.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            switch (i) {
                                case R.id.radioSize15:
                                    album.setSize(25); // задание выбора размера кисти и ластика
                                    break;
                                case R.id.radioSize30:
                                    album.setSize(50); // задание выбора размера кисти и ластика
                                    break;
                                case R.id.radioSize50:
                                    album.setSize(75); // задание выбора размера кисти и ластика
                                    break;
                                case R.id.radioSize70:
                                    album.setSize(100); // задание выбора размера кисти и ластика
                                    break;
                                case R.id.radioSize90:
                                    album.setSize(125); // задание выбора размера кисти и ластика
                                    break;
                            }
                        }
                    });

                    // обработка сохранения внесённых изменений
                    Button btnYes = dialogAlbumPalette.findViewById(R.id.btnYes);
                    Button btnNo = dialogAlbumPalette.findViewById(R.id.btnNo);
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View viewYes) {
                            album.invalidate(); // обновление View
                            dialogAlbumPalette.dismiss(); // закрытие диалога
                        }
                    });
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View viewNo) {
                            album.invalidate(); // обновление View
                            dialogAlbumPalette.cancel(); // выход из диалога
                        }
                    });

                    dialogAlbumPalette.show(); // показ диалогового окна
                    break;
                case R.id.button_clear:
                    // код для очистки View
                    AlertDialog.Builder broomDialog = new AlertDialog.Builder(MainActivity.this); // создание диалогового окна типа AlertDialog
                    broomDialog.setTitle("Очистка рисунка"); // заголовок диалогового окна
                    broomDialog.setMessage("Очистить область рисования (имеющийся рисунок будет удалён)?"); // сообщение диалога

                    broomDialog.setPositiveButton("Да", new DialogInterface.OnClickListener(){ // пункт выбора "да"
                        public void onClick(DialogInterface dialog, int which){
                            album.clear(); // метод очистки кастомизированного View
                            dialog.dismiss(); // закрыть диалог
                        }
                    });
                    broomDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener(){  // пункт выбора "нет"
                        public void onClick(DialogInterface dialog, int which){
                            dialog.cancel(); // выход из диалога
                        }
                    });
                    broomDialog.show(); // отображение на экране данного диалога
                    break;
            }
        }
    };
}