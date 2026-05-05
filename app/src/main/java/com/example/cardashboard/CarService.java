package com.example.cardashboard;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class CarService extends Service {
    private Timer carDataTimer;
    private CarDataListener carDataListener;

    public interface CarDataListener {
        void onCarDataUpdated(CarData carData);
    }

    public static class CarData {
        private int speed;
        private int fuelLevel;
        private int batteryLevel;
        private double engineTemp;
        private double outsideTemp;
        private boolean isEngineRunning;
        private boolean isLightsOn;

        public CarData(int speed, int fuelLevel, int batteryLevel, 
                      double engineTemp, double outsideTemp, 
                      boolean isEngineRunning, boolean isLightsOn) {
            this.speed = speed;
            this.fuelLevel = fuelLevel;
            this.batteryLevel = batteryLevel;
            this.engineTemp = engineTemp;
            this.outsideTemp = outsideTemp;
            this.isEngineRunning = isEngineRunning;
            this.isLightsOn = isLightsOn;
        }

        // Getters
        public int getSpeed() { return speed; }
        public int getFuelLevel() { return fuelLevel; }
        public int getBatteryLevel() { return batteryLevel; }
        public double getEngineTemp() { return engineTemp; }
        public double getOutsideTemp() { return outsideTemp; }
        public boolean isEngineRunning() { return isEngineRunning; }
        public boolean isLightsOn() { return isLightsOn; }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startCarDataCollection();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void startCarDataCollection() {
        carDataTimer = new Timer();
        carDataTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateCarData();
            }
        }, 0, 2000); // 每2秒更新一次车辆数据
    }

    private void updateCarData() {
        // 模拟车辆数据更新
        CarData carData = new CarData(
            (int) (60 + Math.random() * 80), // 速度: 60-140 km/h
            (int) (30 + Math.random() * 50), // 燃油: 30-80%
            (int) (70 + Math.random() * 25), // 电池: 70-95%
            80 + Math.random() * 20,         // 引擎温度: 80-100°C
            15 + Math.random() * 10,         // 外部温度: 15-25°C
            Math.random() > 0.1,             // 引擎运行 (90%概率)
            Math.random() > 0.8              // 车灯开启 (20%概率)
        );

        if (carDataListener != null) {
            carDataListener.onCarDataUpdated(carData);
        }
    }

    public void setCarDataListener(CarDataListener listener) {
        this.carDataListener = listener;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (carDataTimer != null) {
            carDataTimer.cancel();
            carDataTimer = null;
        }
    }
}