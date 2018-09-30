package digipodium.episode2;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private boolean allowConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermissions();
    }

    private void initPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.NFC,
        };

        if (EasyPermissions.hasPermissions(this, permissions)) {
            allowConnection = true;
            showFragment();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "please provide permission",
                    82,
                    permissions);
        }
    }

    private void showFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new BluetoothFragment(), "bT").commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
        allowConnection = true;
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "permission failed", Toast.LENGTH_SHORT).show();
        allowConnection = false;
    }
}
