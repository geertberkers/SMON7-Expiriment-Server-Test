package geert.berkers.smon7experimentservertest;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private final static int TARGET_COUNT = 1;

    private final static int GAME_ID = 999;
    private final static String NAME = "Target";
    private final static String QR_CODE_NAME = "ServerTest";

    private String base64Image;
    private int targetsAdded = 0;

    private long start;

    private Button post;
    private TextView result;

    /*
     * Post: postAddTarget information
     * url: http://backend.mhermans.com/api/v1/target/add
     * body: { gameId : int, name : "string", image : "base64 encoded" qrCode: "string"}
     * return 200: {targetId : int, targetName: "string", targetUrl: "string", targetQR: "string"} 400 :{ error: "string" }
     */

    // Delete added targets from MySQL with the following statement
    // DELETE FROM `targets` WHERE game_id = '999';


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initControls();
    }

    private void initControls() {
        post = (Button) findViewById(R.id.postButton);
        result = (TextView) findViewById(R.id.resultPost);

        getBase64Image();
    }

    private void postAddTarget() {
        String targetName = NAME + targetsAdded;
        String qrCode = QR_CODE_NAME + targetsAdded;


        QueueController.getInstance(this).add(new AddTargetRequest(GAME_ID, targetName, base64Image, qrCode, new Response.Listener<Target>() {
            @Override
            public void onResponse(Target response) {
                //Increase targets added!
                targetsAdded++;

                result.setText("Responses: " + targetsAdded);

                if (targetsAdded == TARGET_COUNT) {
                    targetsAdded = 0;
                    long end = System.currentTimeMillis();
                    long duration = end - start;

                    post.setEnabled(true);
                    result.setText("Responsetime: " + (duration / TARGET_COUNT) + " milliseconds for " + TARGET_COUNT + " targets");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    error.printStackTrace();
                } else {
                    if (error.networkResponse.statusCode == 404) {
                        Toast.makeText(MainActivity.this, R.string.msg_no_internet, Toast.LENGTH_SHORT).show();
                    }
                }
                showNetworkError(error);
            }
        }));
    }

    /**
     * Show network error dialog.
     *
     * @param error Error to show
     */
    private void showNetworkError(VolleyError error) {
        String message = getString(R.string.create_target_error_message);
        if (error.networkResponse == null) {
            message += "\n\n" + error.getMessage();
        } else {
            message += "\n\n" + error.getMessage() + ". Code: " + error.networkResponse.statusCode;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.create_target_error_title)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void getBase64Image() {
        Resources resources = this.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.image);
        base64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public void startPost(View view) {
        post.setEnabled(false);
        start = System.currentTimeMillis();

        for (int i = 0; i < TARGET_COUNT; i++) {
            postAddTarget();
        }
    }
}
