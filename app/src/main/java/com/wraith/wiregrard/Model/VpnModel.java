package com.wraith.wiregrard.Model;

public class VpnModel {
    String intentNetwork;
    String privateKey;
    String endPoint;
    String publicKey;
    String countryName;

    public VpnModel(String intentNetwork, String privateKey, String endPoint, String publicKey, String countryName) {
        this.intentNetwork = intentNetwork;
        this.privateKey = privateKey;
        this.endPoint = endPoint;
        this.publicKey = publicKey;
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getIntentNetwork() {
        return intentNetwork;
    }

    public void setIntentNetwork(String intentNetwork) {
        this.intentNetwork = intentNetwork;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
