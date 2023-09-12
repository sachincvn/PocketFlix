package com.king.flixa.ExpPlayer;

import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.MediaDrmCallback;

import java.util.UUID;

public class MyDRMCallback implements MediaDrmCallback {

    String keyString;

    @Override
    public byte[] executeProvisionRequest(UUID uuid, ExoMediaDrm.ProvisionRequest request) {
        return new byte[0];
    }

    public MyDRMCallback(String keyId, String keyValue) {
        this.keyString = "{\"keys\":[{\"kty\":\"oct\",\"k\":\""+keyValue+"\",\"kid\":\""+keyId+"\"}],\"type\":\"temporary\"}";;
    }

    @Override
    public byte[] executeKeyRequest(UUID uuid, ExoMediaDrm.KeyRequest request) {
        return keyString.getBytes();
    }
}