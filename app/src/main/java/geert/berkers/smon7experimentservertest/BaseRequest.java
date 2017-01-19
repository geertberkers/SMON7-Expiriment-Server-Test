package geert.berkers.smon7experimentservertest;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Base request for the API.
 *
 * @param <T> Response type
 */
public abstract class BaseRequest<T> extends Request<T> {

    /**
     * Default charset for JSON request.
     */
    protected static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private static final String BASE_URL = "http://backend.mhermans.com/api/v1/";

    private final Response.Listener<T> responseListener;

    /**
     * Build a request.
     *
     * @param method           Method for the HTTP request.
     *                         Use the {@link com.android.volley.Request.Method} class.
     * @param path             Path of the request. The base URL will be inserted.
     * @param responseListener Response listener
     * @param errorListener    Error listener
     */
    public BaseRequest(int method, String path, Response.Listener<T> responseListener, Response.ErrorListener errorListener) {
        super(method, BASE_URL + path, errorListener);
        this.responseListener = responseListener;
    }

    /**
     * Get the parameters for the request.
     *
     * @return Request body as JSON
     * @throws JSONException
     */
    protected JSONObject getBodyParams() throws JSONException {
        return new JSONObject();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        JSONObject jsonObject = null;
        try {
            jsonObject = getBodyParams();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject == null) jsonObject = new JSONObject();
        String body = jsonObject.toString();
        try {
            return body.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException e) {
            return body.getBytes();
        }
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    protected void deliverResponse(T response) {
        responseListener.onResponse(response);
    }

    /**
     * Parse the string of a {@link NetworkResponse}.
     *
     * @param response NetworkResponse to parse
     * @return String
     */
    protected final String parseString(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }

        return parsed;
    }
}
