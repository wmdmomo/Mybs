package com.example.momomo.myapplication.data_save;

import org.litepal.crud.LitePalSupport;

public class trackData extends LitePalSupport {
    private String trackdata;
    private Boolean isProcessed;

    public void setTrackdata(String trackdata){this.trackdata = trackdata;}

    public String getTrackdata() { return trackdata; }

    public void setIsProcessed(Boolean isProcessed){this.isProcessed = isProcessed;  }

    public Boolean getIsProcessed(){ return isProcessed; }
}
