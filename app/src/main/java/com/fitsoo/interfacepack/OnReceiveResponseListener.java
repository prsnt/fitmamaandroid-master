package com.fitsoo.interfacepack;

/**
 * Created by xyz on 28/7/17.
 */

public interface OnReceiveResponseListener {
    /**
     * Here Request type is passed as we can use this same interface only once where there will be multiple
     * service request so in order to differentiate between those request
     * @param requestType the type of the service request that was performed for example "Login" , "Register" etc
     * @param response The response that was obtained from that request
     */
    void onResponseReceived(String requestType, String response);
}
