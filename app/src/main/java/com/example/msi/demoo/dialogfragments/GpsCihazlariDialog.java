package com.example.msi.demoo.dialogfragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.msi.demoo.MainActivity;
import com.example.msi.demoo.R;
import com.example.msi.demoo.adapters.GpsCihazlariDialogAdapter;
import com.example.msi.demoo.interfaces.CustomItemClickListener;
import com.example.msi.demoo.models.BtDevice;
import com.example.msi.demoo.models.GpsData;
import com.example.msi.demoo.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;
import static com.example.msi.demoo.MainActivity.mainActivityCurrentObject;

//import android.support.v4.content.res.ColorStateListInflaterCompat;

public class GpsCihazlariDialog extends DialogFragment {

    private static final int REQUEST_ENABLE_BT = 1;

    private static final int STATE_LISTENING = 1;
    private static final int STATE_CONNECTING = 2;
    private static final int STATE_CONNECTED = 3;
    private static final int STATE_CONNECTION_FAILED = 4;
    private static final int STATE_MESSAGE_RECEIVED = 5;

    private ImageView kapat;
    private Button yenile;
    private Switch openCloseSwitch;
    private RecyclerView recyclerViewEslenen;
    private TextView uyarıText;

    private BluetoothAdapter myBluetoothAdapter;

    private List<BtDevice> bluetoothPairedDeviceList = new ArrayList<>();

    private GpsCihazlariDialogAdapter gpsCihazlariDialogAdapter;

    private static BluetoothDevice connectedBluetoothDevice = null;
    private static BluetoothSocket connectionSocket = null;
    private int clickedPosition = -1;

    private static UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    ClientClass clientClass = null;
    SendReceive sendReceive = null;

    private Timer mTimer;
    public static GpsData gpsData = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        View rootView = inflater.inflate(R.layout.dialog_gps_cihazlari, null);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        kapat = rootView.findViewById(R.id.dialog_gps_cihazlari_close);
        yenile = rootView.findViewById(R.id.dialog_gps_cihazlari_yenile);
        openCloseSwitch = rootView.findViewById(R.id.dialog_gps_cihazlari_switch);

        uyarıText = rootView.findViewById(R.id.dialog_gps_cihazlari_no_cihaz);

        recyclerViewEslenen = rootView.findViewById(R.id.dialog_gps_cihazlari_recycler_eslenen);

        kapat.setOnClickListener(onClickListener);
        yenile.setOnClickListener(onClickListener);


        LinearLayoutManager layoutManagerEslenen = new LinearLayoutManager(getActivity());
        layoutManagerEslenen.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewEslenen.setLayoutManager(layoutManagerEslenen);

        gpsCihazlariDialogAdapter = new GpsCihazlariDialogAdapter(bluetoothPairedDeviceList , new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("Eşlenen position", "Tıklanan Pozisyon:" + position);
                BluetoothDevice bluetoothDevice = bluetoothPairedDeviceList.get(position).getBluetoothDevice();

                if(clickedPosition == -1){
                    clientClass = new ClientClass(bluetoothDevice);
                    clientClass.start();

                    bluetoothPairedDeviceList.get(position).setConnectInfo("Bağlanıyor...");
                    gpsCihazlariDialogAdapter.notifyDataSetChanged();
                }else if(clickedPosition != position || !bluetoothPairedDeviceList.get(position).isConnectStatu()){
                    clientClass.interrupt();
                    clientClass = new ClientClass(bluetoothDevice);
                    clientClass.start();

                    bluetoothPairedDeviceList.get(position).setConnectInfo("Bağlanıyor...");

                    for(int i=0; i<bluetoothPairedDeviceList.size(); i++){
                        if(i != position){
                            bluetoothPairedDeviceList.get(i).setConnectStatu(false);
                            bluetoothPairedDeviceList.get(i).setConnectInfo("");
                        }
                    }
                    gpsCihazlariDialogAdapter.notifyDataSetChanged();
                }
                clickedPosition = position;
            }
        }, getActivity(),getContext());

        recyclerViewEslenen.setHasFixedSize(true);
        recyclerViewEslenen.setAdapter(gpsCihazlariDialogAdapter);
        recyclerViewEslenen.setItemAnimator(new DefaultItemAnimator());

        bluetoothControl();

        return rootView;
    }

    @Override
    public void onStart() {
        setStatus();
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.dialog_gps_cihazlari_yenile:
                    bluetoothPairedDeviceList.clear();
                    listPairedDevices();
                    break;

                case R.id.dialog_gps_cihazlari_close:
                    dismiss();
                    break;
            }
        }
    };

    public void setStatus() {
        if (myBluetoothAdapter != null) {

            if(myBluetoothAdapter.isEnabled()){
                openCloseSwitch.setChecked(true);
                yenile.setVisibility(View.VISIBLE);
                yenile.setEnabled(true);
                recyclerViewEslenen.setVisibility(View.VISIBLE);
                uyarıText.setVisibility(View.GONE);
            }else {
                openCloseSwitch.setChecked(false);
                yenile.setVisibility(View.INVISIBLE);
                yenile.setEnabled(false);
                recyclerViewEslenen.setVisibility(View.GONE);
                uyarıText.setText("Bluetooth Kapalı");
                uyarıText.setVisibility(View.VISIBLE);
            }
        }
    }

    private void bluetoothControl(){
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Cihazın Bluetooth özeliğini destekleyip desteklemediğini, myBluetoothAdapter degerini kontrol ederek tespit ettık
        if (myBluetoothAdapter == null) {
            //Bluetooth özeliğini desteklemediği durumda arayuz butonlarımızı pasif yaptık
            openCloseSwitch.setEnabled(false);
            yenile.setVisibility(View.GONE);

            uyarıText.setText("Cihazın bluetooth desteği yok!");
            Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_ERROR, "Cihazın bluetooth desteği yok!");
            uyarıText.setVisibility(View.VISIBLE);

            recyclerViewEslenen.setVisibility(View.GONE);
        } else {

            openCloseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(openCloseSwitch.isChecked()){
                        boolean isReq = onBluetooth();

                        if(!isReq){
                            listPairedDevices();
                        }

                        setStatus();
                    }else{
                        offBluetooth();
                        setStatus();
                    }
                }
            });
        }
    }

    public boolean onBluetooth() {
        boolean isRequireRequest = !myBluetoothAdapter.isEnabled();

        if (isRequireRequest) {
            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
        }

        return isRequireRequest;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            // BluetoothAdapter.STATE_DISCONNECTED==0 ise Bluetooth kapalı
            if (resultCode == BluetoothAdapter.STATE_DISCONNECTED) {
//                Toast.makeText(getActivity(), "Bluetooth disable",Toast.LENGTH_LONG).show();
            }else{
                //Bluetooth açık durumu
                Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_SUCCESS, "Bluetooth açıldı.");

                setStatus();
                listPairedDevices();
            }
        }
    }

    public void offBluetooth() {
        //BluetoothAdapter etkisiz hale getirdik
        myBluetoothAdapter.disable();

        Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_SUCCESS, "Bluetooth kapatıldı.");
    }

    public void listPairedDevices() {
        //Eşleşmiş Bluetooth cihazlarını  çektik ve pairedDevices değişkenine atadım
        Set<BluetoothDevice> pairedDeviceSet = myBluetoothAdapter.getBondedDevices();

        bluetoothPairedDeviceList .clear();
        for (BluetoothDevice device : pairedDeviceSet)
            bluetoothPairedDeviceList.add(new BtDevice(device, false, ""));

        for(int i=0; i<bluetoothPairedDeviceList.size(); i++)
            if(connectedBluetoothDevice != null && connectedBluetoothDevice.equals(bluetoothPairedDeviceList.get(i).getBluetoothDevice())){
                if(connectionSocket != null && connectionSocket.isConnected()){
                    bluetoothPairedDeviceList.get(i).setConnectStatu(true);
                    bluetoothPairedDeviceList.get(i).setConnectInfo("Bağlandı.");
                }
            }

        gpsCihazlariDialogAdapter.notifyDataSetChanged();
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what)
            {
                case STATE_LISTENING:
                    Log.e(TAG, "handleMessage: STATE_LISTENING" );
                    break;
                case STATE_CONNECTING:
                    Log.e(TAG, "handleMessage: STATE_CONNECTING" );
                    break;
                case STATE_CONNECTED:
                    Log.e(TAG, "handleMessage: STATE_CONNECTED" );
                    bluetoothPairedDeviceList.get(clickedPosition).setConnectStatu(true);
                    bluetoothPairedDeviceList.get(clickedPosition).setConnectInfo("Bağlandı.");
                    gpsCihazlariDialogAdapter.notifyDataSetChanged();

                    for(int i=0; i<bluetoothPairedDeviceList.size(); i++){
                        if(i != clickedPosition){
                            bluetoothPairedDeviceList.get(i).setConnectStatu(false);
                            bluetoothPairedDeviceList.get(i).setConnectInfo("");
                        }
                    }
                    gpsCihazlariDialogAdapter.notifyDataSetChanged();

                    break;
                case STATE_CONNECTION_FAILED:
                    Log.e(TAG, "handleMessage: STATE_CONNECTION_FAILED" );
                    bluetoothPairedDeviceList.get(clickedPosition).setConnectStatu(false);
                    bluetoothPairedDeviceList.get(clickedPosition).setConnectInfo("Bağlantı Başarısız.");
                    gpsCihazlariDialogAdapter.notifyDataSetChanged();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bluetoothPairedDeviceList.get(clickedPosition).setConnectStatu(false);
                            bluetoothPairedDeviceList.get(clickedPosition).setConnectInfo("");
                            gpsCihazlariDialogAdapter.notifyDataSetChanged();
                        }
                    },1500);

                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff = (byte[]) msg.obj;
                    String tempMesg = new String(readBuff, 0,msg.arg1);


                    Log.e(TAG, "handleMessage: STATE_MESSAGE_RECEIVED\nMESAJ : " + tempMesg );

                    if(tempMesg.contains("Error!") || tempMesg.equals("AT")){
                        if(connectedBluetoothDevice != null && connectionSocket != null && connectionSocket.isConnected())
                            startTimer();

                        if(mainActivityCurrentObject.gpsConnectedImage.getVisibility() == View.VISIBLE)
                            mainActivityCurrentObject.gpsConnectedImage.setVisibility(View.GONE);

                    } else {
                        stopTimer();
                        if(!bluetoothPairedDeviceList.get(clickedPosition).isConnectStatu())
                        {
                            bluetoothPairedDeviceList.get(clickedPosition).setConnectStatu(true);
                            bluetoothPairedDeviceList.get(clickedPosition).setConnectInfo("Bağlandı.");
                            gpsCihazlariDialogAdapter.notifyDataSetChanged();

                            for (int i = 0; i < bluetoothPairedDeviceList.size(); i++) {
                                if (i != clickedPosition) {
                                    bluetoothPairedDeviceList.get(i).setConnectStatu(false);
                                    bluetoothPairedDeviceList.get(i).setConnectInfo("");
                                }
                            }
                            gpsCihazlariDialogAdapter.notifyDataSetChanged();
                        }

                        if(mainActivityCurrentObject.gpsConnectedImage.getVisibility() == View.GONE)
                            mainActivityCurrentObject.gpsConnectedImage.setVisibility(View.VISIBLE);

                        String[] messageParts = tempMesg.split("\\r?\\n");
                        for(int i=0; i<messageParts.length; i++)
                            analyzeNmeaPart(messageParts[i]);
                    }
                    break;
            }
            return true;
        }
    });

    private class ClientClass extends Thread
    {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass(BluetoothDevice device1){
            this.device = device1;

            try{
                socket = device.createRfcommSocketToServiceRecord(MY_UUID_INSECURE);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        public void run(){
            try {
                socket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);

                connectedBluetoothDevice = device;
                connectionSocket = socket;

                sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
                this.interrupt();
            }
        }

        @Override
        public void interrupt() {
            try {
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(message);

                socket.close();
                connectionSocket = null;
                connectedBluetoothDevice = null;
                Log.e(TAG, " mSocket.close() KAPATILDI");
            } catch (IOException closeException) {
                closeException.printStackTrace();
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            super.interrupt();
        }
    }

    private class SendReceive extends  Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive (BluetoothSocket socket)
        {
            this.bluetoothSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut = null;

            try{
                tempIn = bluetoothSocket.getInputStream();
                tempOut = bluetoothSocket.getOutputStream();

            }catch (IOException e){
                e.printStackTrace();
                this.interrupt();
            }

            inputStream = tempIn;
            outputStream = tempOut;
        }

        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;

            while(true){
                try {
                    bytes = inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            this.interrupt();
        }

        @Override
        public void interrupt() {
            try {
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(message);

                bluetoothSocket.close();
                connectionSocket = null;
                connectedBluetoothDevice = null;
                Log.e(TAG, " mSocket.close() KAPATILDI");

                MainActivity.mainActivityCurrentObject.updateLocationWithGpsDevice(false);

//                MainActivity.mainActivityObject.removeGpsLocationPoint();
            } catch (IOException closeException) {
                closeException.printStackTrace();
                Log.e(TAG, "Could not close the client socket", closeException);
            } catch (Exception e){
                e.printStackTrace();
            }

            super.interrupt();
        }

        public void write(byte[] bytes){
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {}
        }

    }

    private void analyzeNmeaPart(String nmea) {
        Log.e(TAG, "analyzeNmeaPart: NMEA : " + nmea);
        boolean isNotify = false;

        String[] params = nmea.split(",");
        if(params.length > 1) {
            String nmeaType = params[0];
            if (nmeaType.contains("$") && nmeaType.length() == 6)
                nmeaType = nmeaType.substring(nmeaType.length() - 3, nmeaType.length());

            Log.e(TAG, "analyzeNmeaPart: NMEA TYPE : " + nmeaType);

            //Fix veri geldiğinde
            if (nmeaType.equals("GGA") && params.length > 10)
            {
                if (!params[2].isEmpty() && !params[4].isEmpty() && !params[9].isEmpty())
                {
                    String lat = params[2];
                    String lon = params[4];
                    String alt = params[9];

                    Log.e(TAG, "analyzeNmeaPart: GGA --> \nlat : " + lat + "\nlon : " + lon + "\n alt : " + alt);

                    if(Pattern.compile("[0-9]*\\.?[0-9]+").matcher(lat).lookingAt()  && Pattern.compile("[0-9]*\\.?[0-9]+").matcher(lon).lookingAt() && Pattern.compile("[+-]?[0-9]*\\.?[0-9]+").matcher(lon).lookingAt()) {
                        try {
                            createOrUpdateGpsDataObje(convertNmeaToLatLon(lat), convertNmeaToLatLon(lon), Double.parseDouble(alt));
                            isNotify = true;
                            Log.e(TAG, "analyzeNmeaPart: REGEXe GİRDİ!!!");
                        }catch (NumberFormatException e){ e.printStackTrace(); }
                    }
                }
            }

            //Gelen veri boş gelmiyor ve bozuk değilse verileri paylaşıyoruz.
            if (isNotify) {
                if (gpsData != null && gpsData.getLatitude() != 0 && gpsData.getLongitude() != 0) {
                    MainActivity.mainActivityCurrentObject.updateLocationWithGpsDevice(true);

                    Log.e(TAG, "analyzeNMEA UPDATING --> \nlatitude : " + gpsData.getLatitude() + "\nlongitude : " + gpsData.getLongitude() + "\naltitude : " + gpsData.getAltitude());
                }
            }
        }
    }


    private void createOrUpdateGpsDataObje(Double latitude, Double longitude, Double altitude){
        if(gpsData == null){
            gpsData = new GpsData(latitude, longitude, altitude, System.currentTimeMillis());
        }else{
            if(latitude != 0 && !latitude.equals(gpsData.getLatitude())){
                gpsData.setLatitude(latitude);
                gpsData.setMessageTime(System.currentTimeMillis());
            }

            if(longitude != 0 && !longitude.equals(gpsData.getLongitude())){
                gpsData.setLongitude(longitude);
                gpsData.setMessageTime(System.currentTimeMillis());
            }

            if(!altitude.equals(gpsData.getAltitude())){
                gpsData.setAltitude(altitude);
                gpsData.setMessageTime(System.currentTimeMillis());
            }
        }

        Log.e(TAG, "createOrUpdateGpsDataObje: GPS DATA : " + gpsData.toString());
    }


    //gelen NMEA mesajlarındaki konum bilgisi parse etmek için kullanılan metod
    //4105.5443399 gelen veriyi  41.092405665 bu hale getiriyor. Alsında çok basit bir yöntem kullanılıyor.
    //https://community.oracle.com/thread/3619431 buradan nasıl parse edilmesi gerektiğine bakabilirsiniz.
    //noktanın 2 solundaki veriyiden son veriye kadar yani 055443399 itibaren alıyor ve 60'a bölüyoruz
    //oluşan veri 0.092405665 şekilnde oluyor bunuda 41'e ekliyoruz.
    private double convertNmeaToLatLon(String nmea) {
        try {
            int i = nmea.indexOf(".");
            String beforeNewDot = nmea.substring(0, i - 2);
            String afterNewDot = nmea.substring(i - 2, nmea.length());

            double ilkval = Double.valueOf(beforeNewDot);
            double sonval = Double.valueOf(afterNewDot);
            return ilkval + (sonval / 60);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }


    private void startTimer() {
        if (mTimer == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Log.e(TAG, "run: Timer Çalışıyor...");
                            if(connectionSocket != null && connectionSocket.isConnected())
                                sendReceive.interrupt();
                            stopTimer();
                        }
                    }, 3000);
                }
            }).start();
        }
    }
    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
            Log.e(TAG, "stopTimer: Stop Timer Çalıştı...");
        }
    }
}
