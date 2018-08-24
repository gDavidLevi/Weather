package ru.davidlevi.weather.ui;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import es.dmoral.toasty.Toasty;
import retrofit2.Response;
import ru.davidlevi.weather.R;
import ru.davidlevi.weather.firebase.Constant;
import ru.davidlevi.weather.retrofit.data.DataManager;
import ru.davidlevi.weather.retrofit.data.IDataManager;
import ru.davidlevi.weather.retrofit.data.WeatherRequest;
import ru.davidlevi.weather.service.Coordinates;
import ru.davidlevi.weather.service.LocationService;
import ru.davidlevi.weather.sqlite.HistoryTable;
import ru.davidlevi.weather.sqlite.WeatherTable;
import ru.davidlevi.weather.sqlite.model.WeatherData;
import ru.davidlevi.weather.ui.fragments.AboutFragment;
import ru.davidlevi.weather.ui.fragments.IBaseFragment;
import ru.davidlevi.weather.ui.fragments.ListCitiesFragments;
import ru.davidlevi.weather.ui.fragments.RegistrationConfirmFragment;
import ru.davidlevi.weather.ui.fragments.RegistrationFragment;
import ru.davidlevi.weather.ui.fragments.SettingsFragment;
import ru.davidlevi.weather.ui.fragments.WeatherFragment;

/**
 * Базовая активити
 */
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IBaseFragment {
    // Logcat
    private static final String TAG = "Finder";

    // BroadcastReceivers
    private BroadcastReceiver firebaseBroadcastReceiver;
    private BroadcastReceiver locationBroadcastReceiver;
    private Context context;

    // Координаты устройства
    private Coordinates currentCoordinates = new Coordinates();

    // Поля
    private TextView headerUsername;
    private TextView headerEmail;

    // Настройки
    private SharedPreferences sharedPreferences;
    private String currentCity;

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    /**
     * Создание/пересоздание активити
     * См. onSaveInstanceState(Bundle...
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        order(savedInstanceState);
    }

    /**
     * Порядок запуска
     *
     * @param bundle Saved instance state
     */
    private void order(Bundle bundle) {
        context = getApplicationContext();

        // Получим пермиссии
        getPermissions();

        // Инициализация LocationService
        initLocationService();

        // Инициализируем вьюшеки
        initViews();

        // Инициализация SharedPreferences
        initSharedPreferences();

        // Отобразим WeatherFragment
        showWeatherFragment();

        // Инициализируем Firebase
        initPushNotification();
    }

    /**
     * Инициализация SharedPreferences
     */
    private void initSharedPreferences() {
        sharedPreferences = getPreferences(Activity.MODE_PRIVATE);

        // Если в SharedPreferences есть данные, то используем их, иначе создаем xml-файл SharedPreferences
        if (sharedPreferences.contains(Constants.CURRENT_CITY))
            currentCity = sharedPreferences.getString(Constants.CURRENT_CITY, Constants.CURRENT_CITY_DEFAULT_VALUE);
        else
            sharedPreferences.edit()
                    .putString(Constants.CURRENT_CITY, Constants.CURRENT_CITY_DEFAULT_VALUE)
                    .putString(Constants.CURRENT_USER, Constants.CURRENT_USER_DEFAULT_VALUE)
                    .putString(Constants.CURRENT_USER_EMAIL, Constants.CURRENT_USER_EMAIL_DEFAULT_VALUE)
                    .apply();

        // Заполним виджеты значениями из SharedPreferences
        setValuesToWidgets();
    }

    /**
     * Метод инициализирует LocationService на прием местоположения
     */
    private void initLocationService() {
        // Сартуем службу
        startService(new Intent(context, LocationService.class));
        // Стартуем слушателя
        locationBroadcastReceiver = new BroadcastReceiver() { // приемник
            @Override
            public void onReceive(Context context, Intent intent) {
                currentCoordinates = (Coordinates) intent.getSerializableExtra(LocationService.INTENT_LOCATION_NAME);
                Log.d(TAG, currentCoordinates.toString());
            }
        };
        registerReceiver(locationBroadcastReceiver, new IntentFilter(LocationService.ACTION_BROADCAST_LOCATION));
    }

    /**
     * Активити уничтожилась
     */
    @Override
    protected void onDestroy() {
        unregisterReceiver(locationBroadcastReceiver); // обязательно надо разрегистрировать!
        super.onDestroy();
    }

    /**
     * Проверка на доступы
     */
    private void getPermissions() {
        // Если разрешения даны, то granted();
        if (ActivityCompat.checkSelfPermission(this, Constants.PERMISSION1) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Constants.PERMISSION2) == PackageManager.PERMISSION_GRANTED) {
            // && потому, что нам сразу нужно 2 разрешения...
            if (ActivityCompat.checkSelfPermission(this, Constants.PERMISSION1) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Constants.PERMISSION2) != PackageManager.PERMISSION_GRANTED) {
                unaccepted(); //..., но если их нет, то...
            }
            granted();
        } else {//, иначе запросить у пользователя доступ:
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                // Местоположение
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Constants.PERMISSION1, Constants.PERMISSION2},
                        Constants.REQUEST_CODE_LOCATION);
            }
        }
    }

    /**
     * Здесь происходит обработка ответа после запроса разрешения у пользователя
     *
     * @param requestCode  int
     * @param permissions  @NonNull String[]
     * @param grantResults @NonNull int[]
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Местоположение
        if (requestCode == Constants.REQUEST_CODE_LOCATION)
            if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    || grantResults[1] == PackageManager.PERMISSION_GRANTED))
                granted();
    }

    /**
     * Доступ к определению местоположения предоставлен
     */
    private void granted() {
        Toasty.info(getBaseContext(), "LOCATION PERMISSION GRANTED", Toast.LENGTH_LONG, true).show();
    }

    /**
     * Доступ к определению местоположения НЕ предоставлен
     */
    private void unaccepted() {
        Toasty.warning(getBaseContext(), "LOCATION PERMISSION UNACCEPTED", Toast.LENGTH_LONG, true).show();
    }

    /**
     * Инициализация широковещательного приемника для push-уведомлений от Firebase
     */
    private void initPushNotification() {
        firebaseBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Если зарегистрировано, то...
                if (intent.getAction().equals(Constant.FIREBASE_REGISTRATION_COMPLETE)) {
                    // подпишем на глобальный топик для того, чтобы приложение получало уведомления
                    FirebaseMessaging.getInstance().subscribeToTopic(Constant.FIREBASE_TOPIC_GLOBAL);
                } else if (intent.getAction().equals(Constant.FIREBASE_PUSH_NOTIFICATION)) { // если получено новое push-уведомление
                    String newPushNotification = intent.getStringExtra(Constant.FIREBASE_INTENT_MESSAGE);
                    //todo Здесь получаем сообщение с сервера Firebase
                    Toasty.info(context, "Push notification Firebase: " + newPushNotification).show();
                }
            }
        };
    }

    /**
     * Сохраним состояние*
     * См. onCreate(Bundle...
     *
     * @param outState Bundle
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        sharedPreferences.edit()
                .putString(Constants.CURRENT_CITY, currentCity)
                .putString(Constants.CURRENT_USER, headerUsername.getText().toString())
                .putString(Constants.CURRENT_USER_EMAIL, headerEmail.getText().toString())
                .apply();
    }

    /**
     * Инициируем элементы макета
     */
    private void initViews() {
        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // FloatingActionButton
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new android.view.View.OnClickListener() {
//            @Override
//            public void onClick(android.view.View view) {
//                // Snackbar
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null)
//                        .show();
//            }
//        });

        // DrawerLayout (вертикальное меню с логотипом и информацией о пользователе)
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View viewHeaderView = navigationView.getHeaderView(0); // обращаемся к тегу внутри View в макете

        // Данные о пользователе в NavigationView
        headerUsername = viewHeaderView.findViewById(R.id.header_username);
        headerEmail = viewHeaderView.findViewById(R.id.header_email);

        // PopupMenu для картинки logo
        ImageView avatar = viewHeaderView.findViewById(R.id.menu_logo);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getBaseContext(), v);
                popupMenu.inflate(R.menu.avatar_popupmenu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_popup_1:
                                Toast.makeText(getBaseContext(), "R.id.item_popup_1", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.item_popup_2:
                                Toast.makeText(getBaseContext(), "R.id.item_popup_2", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
            }
        });
    }

    /**
     * @see 'initViews(), setSettings()
     */
    private void setValuesToWidgets() {
        headerUsername.setText(sharedPreferences.getString(Constants.CURRENT_USER, Constants.CURRENT_USER_DEFAULT_VALUE));
        headerEmail.setText(sharedPreferences.getString(Constants.CURRENT_USER_EMAIL, Constants.CURRENT_USER_EMAIL_DEFAULT_VALUE));
    }

    /**
     * Обработка полученных данных от Retrofit
     *
     * @param response Response<WeatherRequest>
     */
    private void handlerResponseData(@NonNull Response<WeatherRequest> response) {
        WeatherData weatherData = new WeatherData();
        weatherData.setCity(response.body().getName());
        weatherData.setTemperature(Float.toString(response.body().getMain().getTemp()));
        weatherData.setPressure(Integer.toString(response.body().getMain().getPressure()));
        weatherData.setHumidity(Integer.toString(response.body().getMain().getHumidity()));
        weatherData.setDescription(response.body().getWeather()[0].getDescription());
        weatherData.setIcon(response.body().getWeather()[0].getIcon());

        // Сохраним в погодную таблицу
        WeatherTable weatherTable = new WeatherTable(context);
        weatherTable.open();
        weatherTable.save(weatherData);
        weatherTable.close();

        // Теперь пишем в историю
        HistoryTable historyTable = new HistoryTable(context);
        historyTable.open();
        historyTable.addOrUpdateIfExists(weatherData);
        historyTable.close();

        // Установим текущий город как city
        currentCity = weatherData.getCity();

        // Сохраним в SharedPreferences
        sharedPreferences.edit()
                .putString(Constants.CURRENT_CITY, weatherData.getCity())
                .apply();

        // Показать фрагмент
        showFragment(new WeatherFragment());
    }

    /**
     * Получим даннные с погодного сервера и запустим фрагмент WeatherFragment
     *
     * @param city String
     */
    private void getDataAndShowWeatherFragment(final String city) {
        IDataManager dataManager = new DataManager(); // см. модуль Retrofit
        dataManager.initRetrofit(new DataManager.OnEvent() {
            @Override
            public void getData(@NonNull Response<WeatherRequest> response) {
                handlerResponseData(response);
            }

            @Override
            public void getError(String failureMessage) {
                Log.d("Finder", "Retrofit getError: " + failureMessage);
            }
        });
        dataManager.requestRetrofitCity(city,
                Constants.RETROFIT_UNITS,
                Constants.RETROFIT_APIKEY);
    }

    /**
     * Получим даннные с погодного сервера и запустим фрагмент WeatherFragment
     *
     * @param coordinates Coordinates
     */
    private void getDataAndShowWeatherFragment(final Coordinates coordinates) {
        IDataManager dataManager = new DataManager(); // см. модуль Retrofit
        dataManager.initRetrofit(new DataManager.OnEvent() {
            @Override
            public void getData(@NonNull Response<WeatherRequest> response) {
                handlerResponseData(response);
            }

            @Override
            public void getError(String failureMessage) {
                Log.d("Finder", "Retrofit getError: " + failureMessage);
            }
        });
        dataManager.requestRetrofitCoordinates(coordinates.getLatitude(), coordinates.getLongitude(),
                Constants.RETROFIT_UNITS,
                Constants.RETROFIT_APIKEY);
    }

    /**
     * Отображает WeatherFragment
     */
    public void showWeatherFragment() {
        getDataAndShowWeatherFragment(currentCity);
    }

    /**
     * Отображает ItemsListFragment
     */
    private void showListCitiesFragment() {
        showFragment(new ListCitiesFragments());
    }

    /**
     * Отображает SettingsFragment
     */
    private void showSettingsFragment() {
        showFragment(new SettingsFragment());
    }

    /**
     * Отображает AboutFragment
     */
    private void showAboutFragment() {
        showFragment(new AboutFragment());
    }

    /**
     * Метод отображает фрагмент в контейнере макета
     * См. макет app_bar_weather.xml
     *
     * @param fragment Fragment
     */
    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragments_container, fragment)
                .commit();
    }

    /**
     * Отображает RegistrationConfirmFragment
     */
    private void showRegistrationConfirmFragment() {
        showFragment(new RegistrationConfirmFragment());
    }

    /**
     * Отображает RegisrationFragment
     */
    private void showRegisrationFragment() {
        showFragment(new RegistrationFragment());
    }

    /**
     * Обработка нажатя кнопки BACK
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Надуем трехточечное меню, справа сверху, макетом menu.xml
     *
     * @param menu Menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Обработчик нажатия на пункт трехточечного меню
     *
     * @param item MenuItem
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.item_action_settings:
                showSettingsFragment();
                //return true;
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Показать диалог поиска города
     */
    private void showInputDialogFindCityAndAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.input_city);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton(R.string.add_city, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getDataAndShowWeatherFragment(input.getText().toString());
            }
        });
        builder.show();
    }

    /**
     * Показать погоду по текущим координатам
     */
    private void showWeatherHere() {
        if (currentCoordinates.isNotNull())
            getDataAndShowWeatherFragment(currentCoordinates);
        else
            Toasty.error(this, "Местоположение неопределено").show();
    }

    /**
     * Обработчик нажатий на пункты навигационного меню
     *
     * @param item MenuItem
     * @return boolean
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_registration:
                showRegisrationFragment();
                break;
            case R.id.nav_here:
                showWeatherHere();
                break;
            case R.id.nav_add_city:
                showInputDialogFindCityAndAdd();
                break;
            case R.id.nav_history_city:
                showListCitiesFragment();
                break;
            case R.id.nav_settings:
                showSettingsFragment();
                break;
            case R.id.nav_about:
                showAboutFragment();
                break;
            default:
                break;
        }
        // Скрыть меню после нажатия на пункт меню
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Реализация интерфейса
     *
     * @see IBaseFragment
     */
    @Override
    public void onFragmentAttached() {
    }

    /**
     * Реализация интерфейса
     *
     * @see IBaseFragment
     */
    @Override
    public void onFragmentDetached(String tag) {
    }

    /**
     * Реализация интерфейса
     *
     * @see IBaseFragment
     */
    @Override
    public void startFragment(Class fragment) {
        Log.d(TAG, "startFragment :: " + fragment.getSimpleName());
        switch (fragment.getSimpleName()) {
            case "WeatherFragment":
                showWeatherFragment();
                break;
            case "RegistrationConfirmFragment":
                showRegistrationConfirmFragment();
                break;
            default:
                break;
        }
    }

    /**
     * Реализация интерфейса
     *
     * @see IBaseFragment
     */
    @Override
    public void setSettings() {
        setValuesToWidgets();
        showWeatherFragment();
    }


    /**
     * Когда приложение на паузе
     */
    @Override
    protected void onPause() {
        // Разрегистрировать получателя
        LocalBroadcastManager.getInstance(this).unregisterReceiver(firebaseBroadcastReceiver);
        super.onPause();
    }

    /**
     * Когда приложение восстановило работу
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Регистрация получателя на фильтр "регистрация завершена"
        LocalBroadcastManager.getInstance(this).registerReceiver(
                firebaseBroadcastReceiver,
                new IntentFilter(Constant.FIREBASE_REGISTRATION_COMPLETE));
        // Регистрация получателя на фильтр "пришло новое push-уведомление"
        LocalBroadcastManager.getInstance(this).registerReceiver(
                firebaseBroadcastReceiver,
                new IntentFilter(Constant.FIREBASE_PUSH_NOTIFICATION));
        // Очистить зону уведомления
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }
}