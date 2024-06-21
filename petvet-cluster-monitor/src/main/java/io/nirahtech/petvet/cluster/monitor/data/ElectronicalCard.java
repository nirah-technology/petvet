package io.nirahtech.petvet.cluster.monitor.data;

import java.io.Serializable;
import java.net.InetAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;

public final class ElectronicalCard implements Serializable, Comparator<ElectronicalCard>, Comparable<ElectronicalCard> {

    private static final Duration DELAY_TO_BE_OBSOLETE = Duration.ofMinutes(1);
    private static final Set<ElectronicalCard> INSTANCES = new HashSet<>();

    private LocalDateTime lastUpdate = LocalDateTime.now();

    private final UUID id;
    private final MacAddress mac;
    private final InetAddress ip;
    private EmitterMode mode;
    private Duration uptime = null;
    private Float temperatureInCelcus = null;
    private Float consumptionInVolt = null;
    private String location = null;

    private ElectronicalCard(UUID id, MacAddress mac, InetAddress ip, EmitterMode mode) {
        this.id = id;
        this.mac = mac;
        this.ip = ip;
        this.mode = mode;
    }

    public void setMode(EmitterMode mode) {
        this.mode = mode;
        this.lastUpdate = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public MacAddress getMac() {
        return mac;
    }

    public InetAddress getIp() {
        return ip;
    }

    public EmitterMode getMode() {
        return mode;
    }

    public void setUptime(Duration uptime) {
        this.uptime = uptime;
        this.lastUpdate = LocalDateTime.now();
    }

    public void setTemperatureInCelcus(float temperatureInCelcus) {
        this.temperatureInCelcus = temperatureInCelcus;
        this.lastUpdate = LocalDateTime.now();
    }

    public void setConsumptionInVolt(float consumptionInVolt) {
        this.consumptionInVolt = consumptionInVolt;
        this.lastUpdate = LocalDateTime.now();
    }

    public void setLocation(String location) {
        this.location = location;
        this.lastUpdate = LocalDateTime.now();
    }

    public LocalDateTime getLastUpdate() {
        return this.lastUpdate;
    }

    public boolean isObsolete() {
        return LocalDateTime.now().isAfter(this.lastUpdate.plus(DELAY_TO_BE_OBSOLETE));
    }

    public Optional<Duration> getUptime() {
        return Optional.ofNullable(this.uptime);
    }

    public Optional<Float> getTemperatureInCelcus() {
        return Optional.ofNullable(this.temperatureInCelcus);
    }

    public Optional<Float> getConsumptionInVolt() {
        return Optional.ofNullable(this.consumptionInVolt);
    }

    public Optional<String> getLocation() {
        return Optional.ofNullable(this.location);
    }

    public static final ElectronicalCard getOrCreate(UUID id, MacAddress mac, InetAddress ip, EmitterMode mode) {
        ElectronicalCard electronicCard = null;
        Optional<ElectronicalCard> cardFound = INSTANCES.stream()
                .filter(card -> card.getId().equals(id) && card.getMac().equals(mac) && card.ip.equals(ip)).findFirst();
        if (cardFound.isPresent()) {
            electronicCard = cardFound.get();
            electronicCard.setMode(mode);
        } else {
            electronicCard = new ElectronicalCard(id, mac, ip, mode);
            INSTANCES.add(electronicCard);
        }
        return electronicCard;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((mac == null) ? 0 : mac.hashCode());
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        result = prime * result + ((mode == null) ? 0 : mode.hashCode());
        result = prime * result + ((uptime == null) ? 0 : uptime.hashCode());
        result = prime * result + ((temperatureInCelcus == null) ? 0 : temperatureInCelcus.hashCode());
        result = prime * result + ((consumptionInVolt == null) ? 0 : consumptionInVolt.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ElectronicalCard other = (ElectronicalCard) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (mac == null) {
            if (other.mac != null)
                return false;
        } else if (!mac.equals(other.mac))
            return false;
        if (ip == null) {
            if (other.ip != null)
                return false;
        } else if (!ip.equals(other.ip))
            return false;
        if (mode != other.mode)
            return false;
        if (uptime == null) {
            if (other.uptime != null)
                return false;
        } else if (!uptime.equals(other.uptime))
            return false;
        if (temperatureInCelcus == null) {
            if (other.temperatureInCelcus != null)
                return false;
        } else if (!temperatureInCelcus.equals(other.temperatureInCelcus))
            return false;
        if (consumptionInVolt == null) {
            if (other.consumptionInVolt != null)
                return false;
        } else if (!consumptionInVolt.equals(other.consumptionInVolt))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        return true;
    }

    @Override
    public int compareTo(ElectronicalCard other) {
        return this.id.compareTo(other.id);

    }

    @Override
    public int compare(ElectronicalCard arg0, ElectronicalCard arg1) {
        return arg0.compareTo(arg1);

    }

}
