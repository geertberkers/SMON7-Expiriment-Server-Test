package geert.berkers.smon7experimentservertest;

import java.io.Serializable;

/**
 * Model for a target.
 */
public class Target implements Serializable {

    private final String imageUrl;
    private int id;
    private String name;
    private String qrCode;
    private String request;

    public Target(int id, String name, String imageUrl, String qrCode) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.qrCode = qrCode;
    }

    public Target(String imageUrl, String request) {
        this.imageUrl = imageUrl;
        this.request = request;
    }

    /**
     * Returns the id of the target
     *
     * @return the id of the target
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the target
     *
     * @return the name of the target
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the image belonging to the target
     *
     * @return the image belonging to the target
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Returns the QR-code belonging to the target
     *
     * @return the QR-code belonging to the target
     */
    public String getQrCode() {
        return qrCode;
    }

    public String getRequest() {
        return request;
    }
}
