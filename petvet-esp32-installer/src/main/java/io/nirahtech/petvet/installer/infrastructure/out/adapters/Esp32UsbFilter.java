package io.nirahtech.petvet.installer.infrastructure.out.adapters;

import java.util.function.Predicate;

import com.fazecast.jSerialComm.SerialPort;

public final class Esp32UsbFilter implements Predicate<SerialPort> {

    private static final int PRODUCT_ID = 60_000;
    private static final String DESCRIPTION = "CP2102 USB to UART Bridge Controller";
    private static final String MANUFACTURER = "Silicon Labs";
    private static final int VENDOR_ID = 4_292;


    @Override
    public boolean test(SerialPort serialPort) {
        boolean isEsp = false;
        isEsp = serialPort.getProductID() == PRODUCT_ID;
        if (isEsp) {
            isEsp = serialPort.getPortDescription().equalsIgnoreCase(DESCRIPTION);
        }
        if (isEsp) {
            isEsp = serialPort.getManufacturer().equalsIgnoreCase(MANUFACTURER);
        }
        if (isEsp) {
            isEsp = serialPort.getVendorID() == VENDOR_ID;
        }
        return isEsp;
    }
}
