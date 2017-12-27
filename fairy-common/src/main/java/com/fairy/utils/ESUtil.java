package com.fairy.utils;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fairy_LFen on 2017/2/13.
 */
public class ESUtil {
    private static ESUtil esUtil;
    private TransportClient client = null;
    public static ESUtil getInstance(){
        if(esUtil == null){
            esUtil = new ESUtil();
            return esUtil;
        }else{
            return esUtil;
        }
    }

    public TransportClient getTransportClient() throws UnknownHostException {
        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

        return client;
    }

    public List<String> getHourlyTimeList(){
        List<String> time_list = new ArrayList<String>();
        String hourStr = null;
        for(int i = 0; i < 24; i++){
            if(i < 10){
                hourStr = "0" + i;
            }else { hourStr = i+"";}

            String timeStr_start = String.format("2016-01-04 %s:00:00", hourStr);
            String timeStr_end = String.format("2016-01-04 %s:59:59", hourStr);
            time_list.add(timeStr_start);
            time_list.add(timeStr_end);
        }
        return time_list;
    }

}
