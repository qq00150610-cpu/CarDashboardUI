package com.example.cardashboard;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends Activity {

    // 状态栏
    private TextView timeText, dateText;

    // 车辆
    private TextView speedText, fuelText, batteryText;

    // 导航
    private TextView navDestination, navTime;
    private Button navStartBtn;

    // 天气
    private TextView weatherTemp, weatherDesc, weatherLocation;

    // 音乐
    private TextView songTitle, artistName;
    private ImageButton playPauseBtn, prevBtn, nextBtn;

    // 底部导航
    private View navDashboard, navNavigation, navMedia, navPhone, navSettings;
    private ImageView iconDashboard, iconNavigation, iconMedia, iconPhone, iconSettings;
    private TextView textDashboard, textNavigation, textMedia, textPhone, textSettings;

    private Handler handler = new Handler();
    private boolean isPlaying = false;
    private Runnable timeRunnable, vehicleRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClickListeners();
        startTimeUpdates();
        startVehicleUpdates();

        // 默认选中仪表盘
        setNavSelected(navDashboard, iconDashboard, textDashboard);
    }

    private void initViews() {
        // 状态栏
        timeText = (TextView) findViewById(R.id.time_text);
        dateText = (TextView) findViewById(R.id.date_text);

        // 速度
        speedText = (TextView) findViewById(R.id.speed_text);

        // 燃油/电池
        fuelText = (TextView) findViewById(R.id.fuel_text);
        batteryText = (TextView) findViewById(R.id.battery_text);

        // 导航
        navDestination = (TextView) findViewById(R.id.nav_destination);
        navTime = (TextView) findViewById(R.id.nav_time);
        navStartBtn = (Button) findViewById(R.id.nav_start_btn);

        // 天气
        weatherTemp = (TextView) findViewById(R.id.weather_temp);
        weatherDesc = (TextView) findViewById(R.id.weather_desc);
        weatherLocation = (TextView) findViewById(R.id.weather_location);

        // 音乐
        songTitle = (TextView) findViewById(R.id.song_title);
        artistName = (TextView) findViewById(R.id.artist_name);
        playPauseBtn = (ImageButton) findViewById(R.id.play_pause_btn);
        prevBtn = (ImageButton) findViewById(R.id.prev_btn);
        nextBtn = (ImageButton) findViewById(R.id.next_btn);

        // 底部导航
        navDashboard = findViewById(R.id.nav_dashboard);
        navNavigation = findViewById(R.id.nav_navigation);
        navMedia = findViewById(R.id.nav_media);
        navPhone = findViewById(R.id.nav_phone);
        navSettings = findViewById(R.id.nav_settings);

        iconDashboard = (ImageView) findViewById(R.id.nav_icon_dashboard);
        iconNavigation = (ImageView) findViewById(R.id.nav_icon_navigation);
        iconMedia = (ImageView) findViewById(R.id.nav_icon_media);
        iconPhone = (ImageView) findViewById(R.id.nav_icon_phone);
        iconSettings = (ImageView) findViewById(R.id.nav_icon_settings);

        textDashboard = (TextView) findViewById(R.id.nav_text_dashboard);
        textNavigation = (TextView) findViewById(R.id.nav_text_navigation);
        textMedia = (TextView) findViewById(R.id.nav_text_media);
        textPhone = (TextView) findViewById(R.id.nav_text_phone);
        textSettings = (TextView) findViewById(R.id.nav_text_settings);
    }

    private void setupClickListeners() {
        // 音乐控制
        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = !isPlaying;
                playPauseBtn.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play_arrow);
            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSong("上一首歌曲", "未知歌手");
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSong("下一首歌曲", "未知歌手");
            }
        });

        // 导航
        navStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navStartBtn.setText("导航中...");
                navStartBtn.setEnabled(false);
            }
        });

        // 底部导航点击
        navDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavSelected(navDashboard, iconDashboard, textDashboard);
            }
        });
        navNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavSelected(navNavigation, iconNavigation, textNavigation);
            }
        });
        navMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavSelected(navMedia, iconMedia, textMedia);
            }
        });
        navPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavSelected(navPhone, iconPhone, textPhone);
            }
        });
        navSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavSelected(navSettings, iconSettings, textSettings);
            }
        });
    }

    private void setNavSelected(View container, ImageView icon, TextView text) {
        // 重置所有
        resetNav(navDashboard, iconDashboard, textDashboard);
        resetNav(navNavigation, iconNavigation, textNavigation);
        resetNav(navMedia, iconMedia, textMedia);
        resetNav(navPhone, iconPhone, textPhone);
        resetNav(navSettings, iconSettings, textSettings);

        // 高亮选中
        icon.setColorFilter(0xFFF0883E);
        text.setTextColor(0xFFF0883E);
    }

    private void resetNav(View container, ImageView icon, TextView text) {
        icon.setColorFilter(0xFF8B949E);
        text.setTextColor(0xFF8B949E);
    }

    private void updateSong(String title, String artist) {
        songTitle.setText(title);
        artistName.setText(artist);
    }

    private void startTimeUpdates() {
        timeRunnable = new Runnable() {
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                timeText.setText(String.format(Locale.getDefault(), "%02d:%02d",
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)));

                String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
                int wd = c.get(Calendar.DAY_OF_WEEK) - 1;
                dateText.setText(String.format(Locale.getDefault(), "%d年%d月%d日 %s",
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
                        c.get(Calendar.DAY_OF_MONTH), weekDays[wd >= 0 ? wd : 0]));

                handler.postDelayed(this, 1000);
            }
        };
        handler.post(timeRunnable);
    }

    private void startVehicleUpdates() {
        vehicleRunnable = new Runnable() {
            @Override
            public void run() {
                // 模拟数据
                speedText.setText(String.valueOf((int) (Math.random() * 120)));
                int fuel = (int) (30 + Math.random() * 50);
                int battery = (int) (50 + Math.random() * 45);
                fuelText.setText(fuel + "%");
                batteryText.setText(battery + "%");

                handler.postDelayed(this, 2000);
            }
        };
        handler.post(vehicleRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timeRunnable);
        handler.removeCallbacks(vehicleRunnable);
    }
}
