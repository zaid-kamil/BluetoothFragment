package digipodium.episode2;

import android.bluetooth.BluetoothDevice;

public class Device {
    BluetoothDevice device;

    public Device(BluetoothDevice device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return device.getName() + "\n" + device.getAddress();
    }
}
