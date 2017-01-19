package geert.berkers.smon7experimentservertest;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hang on 30-11-2016.
 * A request class used for adding targets
 */
public class AddTargetRequest extends BaseRequest {

    private static final String PATH = "target/add";
    private int gameId;
    private String image;
    private String qrCode;
    private String targetName;
    private Gson gson;

    public AddTargetRequest(int gameId, String targetName, String imageBase64, String qrCode, Response.Listener<Target> responseListener, Response.ErrorListener errorListener) {
        super(Method.POST, PATH, responseListener, errorListener);
        this.gameId = gameId;
        this.targetName = targetName;
        this.image = imageBase64;
        this.qrCode = qrCode;
        gson = new Gson();
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            JsonObject object = gson.fromJson(parseString(response), JsonObject.class);

            Target target = new Target(
                    object.get("targetId").getAsInt(),
                    object.get("targetName").getAsString(),
                    object.get("targetUrl").getAsString(),
                    object.get("targetQR").getAsString()
            );

            return Response.success(target, null);
        } catch (JsonParseException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected JSONObject getBodyParams() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("gameId", gameId);
        object.put("name", targetName);
        object.put("image", image);
        object.put("qrCode", qrCode);
        return object;
    }
}
