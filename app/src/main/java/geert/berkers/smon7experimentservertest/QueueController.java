package geert.berkers.smon7experimentservertest;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;



public class QueueController {

    private static final String TAG = "TAG";

    private static QueueController queueController;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private QueueController(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public synchronized static QueueController getInstance(Context context) {
        if (queueController == null) queueController = new QueueController(context);
        return queueController;
    }


    /**
     * Add new requests to the RequestQueue
     *
     * @param request the request that needs to be added to the queue
     * @param tag     a bit of text to recognize the (type of the) request
     */
    public void add(Request request, String tag) {
        // set the default tag if tag is empty
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        requestQueue.add(request);
    }

    /**
     * Add new requests to the RequestQueue
     *
     * @param request the request that needs to be added to the queue
     */
    public void add(Request request) {
        add(request, TAG);
    }

    /**
     * Cancels all request with the chosen still pending in the queue
     *
     * @param tag the tag whereby requests need to be cancelled
     */
    public void cancel(Object tag) {
        requestQueue.cancelAll(tag);
    }
}
