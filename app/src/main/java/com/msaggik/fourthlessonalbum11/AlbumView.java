package com.msaggik.fourthlessonalbum11;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class AlbumView extends View {

    // поля
    private Canvas canvas; // холст для рисования
    private Paint paint, copyPaint; // стиль рисования
    private Path path; // контур рисования
    private Bitmap bitmap; // контейнер, где будет храниться рисунок
    private final int PAINT_COLOR_DEF = 0xFF4C9063; // цвет по умолчанию

    private boolean brush; // поле флага кисти или ластика

    // координаты касания View
    private float xStart = 0, yStart = 0, xMove = 0, yMove = 0, xFinish = 0, yFinish = 0;
    private String figure = "drawing"; // circle - окружность, drawing - рисование

    // сеттер
    public void setFigure(String figure) {
        this.figure = figure;
    }

    // конструктор для кастомизации View
    public AlbumView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // настройки отрисовки View
        path = new Path(); // объект контура

        paint = new Paint(); // объект стиля рисования
        paint.setAntiAlias(true); // сглаживание краёв
        paint.setStyle(Paint.Style.STROKE); // стиль кисти
        paint.setStrokeCap(Paint.Cap.ROUND); // вид концов рисуемых линий
        paint.setStrokeJoin(Paint.Join.ROUND); // стиль объединения рисуемых линий
        paint.setColor(PAINT_COLOR_DEF); // цвет линии
        paint.setStrokeWidth(30); // толщина рисуемой линии
    }

    // размер View (новая ширина, новая длина, старая ширина, старая длина)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // создание пустого изображения с заданной шириной, высотой и цветовой схемой (альфа, красный, зеленый, голубой)
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        // создание холста для созданного изображения
        canvas = new Canvas(bitmap);
    }

    // метод рисования на холсте
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // определение рисунка на холсте
        canvas.drawBitmap(bitmap, 0, 0, copyPaint); // помещение на холст отрисованных линий
        canvas.drawPath(path, paint); // показ отрисованных линий
    }

    // метод обработки касания View
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (figure) {
            case "drawing":
                // обработка событий касания View
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // прикосновение к View
                        // извлечение координат касания View
                        xStart = event.getX();
                        yStart = event.getY();
                        path.moveTo(xStart, yStart); // определение начала контура
                        path.lineTo(xStart, yStart); // задание линии контура
                        break;
                    case MotionEvent.ACTION_MOVE: // движение по экрану
                        // извлечение координат касания View
                        xMove = event.getX();
                        yMove = event.getY();
                        path.lineTo(xMove, yMove); // задание линии контура
                        break;
                    case MotionEvent.ACTION_UP: // отпускание экрана
                        // извлечение координат касания View
                        xFinish = event.getX();
                        yFinish = event.getY();
                        canvas.drawPath(path, paint); // отрисовка на хосте сгенерированных данных
                        path.reset(); // прерывание линии
                        break;
                }
                break;
            case "circle":
                // обработка событий касания View
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // прикосновение к View
                        // извлечение координат касания View
                        xStart = event.getX();
                        yStart = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE: // движение по экрану
                        // извлечение координат касания View
                        xMove = event.getX();
                        yMove = event.getY();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание экрана
                        // извлечение координат касания View
                        xFinish = event.getX();
                        yFinish = event.getY();
                        // определение расстояния между двумя точками
                        float distance = (float) Math.sqrt((xFinish - xStart) * (xFinish - xStart) + (yFinish - yStart) * (yFinish - yStart));
                        // отрисовка окружности (координаты первой точки, радиус, стиль рисования)
                        canvas.drawCircle(xStart, yStart, distance, paint);
                        break;
                }
                break;
            case "line":
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // прикосновение к View
                        // извлечение координат касания View
                        xStart = event.getX();
                        yStart = event.getY();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание экрана
                        // извлечение координат касания View
                        xFinish = event.getX();
                        yFinish = event.getY();

                        canvas.drawLine(xStart, yStart, xFinish, yFinish, paint);
                        break;
                }
                break;
        }

        invalidate(); // обновление View
        return true;
    }

    // метод очистки кастомизированного View
    public void clear() {
        // очистка экрана и заполнение его белым цветом
        canvas.drawColor(0xFFFFFFFF, PorterDuff.Mode.CLEAR);
        // обновление представления View
        invalidate();
    }

    // метод задания цвета
    public void setColor(int color){
        paint.setColor(color); // задаём свойства кисти
    }

    // метод выбора кисти или ластика
    public void setBrush(boolean brush) {
        this.brush = brush;
        // параметр рисования (ластик или кисть)
        if(brush) { // если выбор кисти, то
            paint.setXfermode(null); // обычная кисть
        } else { // иначе ластик
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); // то назначается кисть ластика
        }
    }
    // метод выбора размера кисти и ластика
    public void setSize(int size) {
        paint.setStrokeWidth(size); // задаём свойства кисти/ластика
    }
}