package com.will.basketballrecorder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Objects;

public class GameView extends View implements View.OnTouchListener, EventLabelCallback {

    private Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    private static int DOT_PAINT_RADIUS = 12;
    private static int LINE_PAINT_WIDTH = 8;

    // Youth basketball court dimensions in feet
    private static int COURT_WIDTH = 94;
    private static int COURT_HEIGHT = 50;
    private static int HALF_COURT_RADIUS = 6;
    private static int BASELINE_TO_CENTER_HOOP = 5;
    private static int THREE_POINT_RADIUS = 20;
    private static int LANE_WIDTH = 19;
    private static int LANE_HEIGHT = 12;
    private static int FREETHROW_RADIUS = 6;

    private final String GAME_NAME = "bobs game";
    private final String FILE_NAME = "data.json";

    public GameView(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnTouchListener(this);
    }

    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        initCourtLines();
        initActions();
    }

    private void initActions() {
        try {
            File file = getContext().getFileStreamPath(FILE_NAME);
            if (!file.exists())
                return;
            FileInputStream fis = getContext().openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            JsonReader jsonReader = new JsonReader(isr);
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                jsonReader.nextName();
                String name = jsonReader.nextString();
                System.out.println(name);
                if (Objects.equals(name, GAME_NAME)) {
                    jsonReader.nextName();
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        jsonReader.nextName();
                        String action = jsonReader.nextString();
                        switch(action) {
                            case "score":
                                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.score));
                                break;
                            case "miss":
                                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.miss));
                                break;
                            case "assist":
                                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.assist));
                                break;
                            case "rebound":
                                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.rebound));
                                break;
                            case "steal":
                                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.steal));
                                break;
                            case "foul":
                                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.foul));
                                break;
                            default:
                                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.court));
                                break;
                        }
                        jsonReader.nextName();
                        float x = (float) jsonReader.nextDouble();
                        jsonReader.nextName();
                        float y = (float) jsonReader.nextDouble();
                        jsonReader.endObject();
                        mCanvas.drawCircle(x, y, DOT_PAINT_RADIUS, mPaint);
                    }
                } else {
                    jsonReader.skipValue();
                    jsonReader.skipValue();
                }
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveAction(String action, float x, float y) {
        if (action == null)
            return;
//        String string = "Hello world!";
//        FileOutputStream outputStream;
        try {
//            FileOutputStream fOut = getContext().openFileOutput(FILE_NAME, Context.MODE_WORLD_READABLE);
//            fOut.write(string.getBytes());
//            fOut.close();
//            Toast.makeText(getContext(), "file saved", Toast.LENGTH_SHORT).show();
            File file = getContext().getFileStreamPath(FILE_NAME);
            if (!file.exists()) {
                FileOutputStream fos = getContext().openFileOutput(FILE_NAME, Context.MODE_WORLD_READABLE);
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                JsonWriter jsonWriter = new JsonWriter(osw);
                jsonWriter.beginArray();
                jsonWriter.beginObject();
                jsonWriter.name("name").value(GAME_NAME);
                jsonWriter.name("events");
                jsonWriter.beginArray();
                jsonWriter.beginObject();
                jsonWriter.name("action").value(action);
                jsonWriter.name("x").value(x);
                jsonWriter.name("y").value(y);
                jsonWriter.endObject();
                jsonWriter.endArray();
                jsonWriter.endObject();
                jsonWriter.endArray();
                jsonWriter.close();
                Toast.makeText(getContext(), "Action Saved!", Toast.LENGTH_SHORT).show();
                return;

            }
            FileOutputStream fos = getContext().openFileOutput("tmp_" + FILE_NAME, Context.MODE_WORLD_READABLE);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            JsonWriter jsonWriter = new JsonWriter(osw);
            FileInputStream fis = getContext().openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            JsonReader jsonReader = new JsonReader(isr);
            jsonReader.beginArray();
            jsonWriter.beginArray();
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                jsonWriter.beginObject();
                String name = jsonReader.nextName();
                String game_value = jsonReader.nextString();
                jsonWriter.name(name).value(game_value);
                name = jsonReader.nextName();
                jsonWriter.name(name);
                jsonReader.beginArray();
                jsonWriter.beginArray();
                String value;
                while (jsonReader.hasNext()) {
                    jsonReader.beginObject();
                    jsonWriter.beginObject();
                    name = jsonReader.nextName();
                    value = jsonReader.nextString();
                    jsonWriter.name(name).value(value);
                    name = jsonReader.nextName();
                    Double dbl_value = jsonReader.nextDouble();
                    jsonWriter.name(name).value(dbl_value);
                    name = jsonReader.nextName();
                    dbl_value = jsonReader.nextDouble();
                    jsonWriter.name(name).value(dbl_value);
                    jsonReader.endObject();
                    jsonWriter.endObject();
                }
                if (Objects.equals(game_value, GAME_NAME)) {
                    jsonWriter.beginObject();
                    jsonWriter.name("action").value(action);
                    jsonWriter.name("x").value(x);
                    jsonWriter.name("y").value(y);
                    jsonWriter.endObject();
                }
                jsonReader.endArray();
                jsonWriter.endArray();
                jsonReader.endObject();
                jsonWriter.endObject();
            }
            jsonReader.endArray();
            jsonWriter.endArray();
            jsonReader.close();
            jsonWriter.close();
            File oldfile = getContext().getFileStreamPath(FILE_NAME);
            File newfile = getContext().getFileStreamPath("tmp_" + FILE_NAME);
            newfile.renameTo(oldfile);
            Toast.makeText(getContext(), "Action Saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCourtLines() {
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();

        // calculate scale factor from feet to pixels
        int scale = (int) Math.min(w/(float)COURT_WIDTH, h/(float)COURT_HEIGHT);

        Paint linePaint = new Paint();
        linePaint.setColor(ContextCompat.getColor(getContext(), R.color.lines));
        linePaint.setStrokeWidth(LINE_PAINT_WIDTH);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);

        int courtPixelWidth = scale*COURT_WIDTH;
        int courtPixelHeight = scale*COURT_HEIGHT;

        // translate the court from (0,0) to (originX, originY) so it is centered on screen
        int originX = (w - courtPixelWidth) / 2;
        int originY = (h - courtPixelHeight) / 2;

        // horizontal sidelines
        mCanvas.drawLine(originX, originY, originX+courtPixelWidth, originY, linePaint);
        mCanvas.drawLine(originX, originY+courtPixelHeight,
                originX+courtPixelWidth, originY+courtPixelHeight, linePaint);

        // vertical baselines
        mCanvas.drawLine(originX, originY, originX, originY+courtPixelHeight, linePaint);
        mCanvas.drawLine(originX+courtPixelWidth, originY,
                originX+courtPixelWidth, originY+courtPixelHeight, linePaint);

        // half-court line
        mCanvas.drawLine(originX+courtPixelWidth/2, originY,
                originX+courtPixelWidth/2, originY+courtPixelHeight, linePaint);
        mCanvas.drawCircle(originX+courtPixelWidth/2,
                originY+courtPixelHeight/2, scale*HALF_COURT_RADIUS, linePaint);

        // freethrow lane
        mCanvas.drawRect(originX, originY+courtPixelHeight/2-scale*LANE_HEIGHT/2,
                originX+scale*LANE_WIDTH, originY+courtPixelHeight/2+scale*LANE_HEIGHT/2, linePaint);
        mCanvas.drawRect(originX+courtPixelWidth-scale*LANE_WIDTH, originY+courtPixelHeight/2-scale*LANE_HEIGHT/2,
                originX+courtPixelWidth, originY+courtPixelHeight/2+scale*LANE_HEIGHT/2, linePaint);

        // freethrow circle
        mCanvas.drawCircle(originX+scale*LANE_WIDTH, originY+courtPixelHeight/2, scale*FREETHROW_RADIUS, linePaint);
        mCanvas.drawCircle(originX+courtPixelWidth-scale*LANE_WIDTH, originY+courtPixelHeight/2, scale*FREETHROW_RADIUS, linePaint);


        int sidelineToThreePointLine = (courtPixelHeight - scale*2*THREE_POINT_RADIUS) / 2;
        RectF threePointArc = new RectF(originX + scale*(BASELINE_TO_CENTER_HOOP-THREE_POINT_RADIUS),
                originY + sidelineToThreePointLine,
                originX + scale*(BASELINE_TO_CENTER_HOOP + THREE_POINT_RADIUS),
                originY + courtPixelHeight - sidelineToThreePointLine);

        mCanvas.drawArc(threePointArc, 270, 180, false, linePaint);
        mCanvas.drawLine(originX, originY+sidelineToThreePointLine,
                originX+scale*BASELINE_TO_CENTER_HOOP, originY+sidelineToThreePointLine, linePaint);
        mCanvas.drawLine(originX, originY+courtPixelHeight-sidelineToThreePointLine,
                originX+scale*BASELINE_TO_CENTER_HOOP, originY+courtPixelHeight-sidelineToThreePointLine, linePaint);

        // move threePointArc to the other end of the court and repeat
        threePointArc.offset(courtPixelWidth - 2*scale*BASELINE_TO_CENTER_HOOP, 0);
        mCanvas.drawArc(threePointArc, 90, 180, false, linePaint);
        mCanvas.drawLine(originX+courtPixelWidth, originY+sidelineToThreePointLine,
                originX+courtPixelWidth-scale*BASELINE_TO_CENTER_HOOP,
                originY+sidelineToThreePointLine, linePaint);
        mCanvas.drawLine(originX+courtPixelWidth, originY+courtPixelHeight-sidelineToThreePointLine,
                originX+courtPixelWidth-scale*BASELINE_TO_CENTER_HOOP,
                originY+courtPixelHeight-sidelineToThreePointLine, linePaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getActionMasked();
        float x = event.getX();
        float y = event.getY();

        switch(action) {
            case MotionEvent.ACTION_UP:
                selectEventLabel(x, y, this);
                break;
            default:
                break;
        }
        return true;
    }

    public void onEventLabelled(float x, float y, int which) {
        String action = null;
        switch(which) {
            case 0:
                action = "score";
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.score));
                break;
            case 1:
                action = "miss";
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.miss));
                break;
            case 2:
                action = "assist";
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.assist));
                break;
            case 3:
                action = "rebound";
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.rebound));
                break;
            case 4:
                action = "steal";
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.steal));
                break;
            case 5:
                action = "foul";
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.foul));
                break;
            default:
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.court));
                break;
        }

        mCanvas.drawCircle(x, y, DOT_PAINT_RADIUS, mPaint);
        invalidate();
        saveAction(action, x, y);
        // TODO:  save the coordinates and event type into a file (JSON, SQLlite, or custom format)
    }

    private void selectEventLabel(final float x, final float y, final EventLabelCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        int selected = 0;
        builder.setTitle("Label event");
        // or whatever you want
      builder.setSingleChoiceItems(R.array.event_labels, -1, new DialogInterface.OnClickListener() {
          //                .setItems(R.array.event_labels, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
              callback.onEventLabelled(x, y, which);
          }
      })
                .show();
    }

}
