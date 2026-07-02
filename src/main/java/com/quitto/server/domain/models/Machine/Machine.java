package com.quitto.server.domain.models.Machine;

public class Machine {
    private Long id;
    private String hostname;
    private String tailscaleNodeKey;
    private String currentIp;
    private String macAddress;
    private boolean wolEnabled;
    private boolean status;
    private String OS;

    public Machine() {
    }

    public Machine(Long id, String hostname, String tailsclae_node_key, String curent_ip, String mac_adress,
            boolean status, boolean wol_enabled, String oS) {
        this.id = id;
        this.hostname = hostname;
        this.tailscaleNodeKey = tailsclae_node_key;
        this.currentIp = curent_ip;
        this.macAddress = mac_adress;
        this.status = status;
        this.wolEnabled = wol_enabled;
        this.OS = oS;
    }

    public Long getId() {
        return id;
    }

    public String getHostname() {
        return hostname;
    }

    public String getTailscaleNodeKey() {
        return tailscaleNodeKey;
    }

    public String getCurrentIp() {
        return currentIp;
    }

    public void setCurrentIp(String curent_ip) {
        this.currentIp = curent_ip;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public boolean isWolEnabled() {
        return wolEnabled;
    }

    public void setWolEnabled(boolean wol_enabled) {
        this.wolEnabled = wol_enabled;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String oS) {
        OS = oS;
    }
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || getClass() != obj.getClass()){
            return false;
        }

        if (obj == this){
            return true;
        }

        Machine other = (Machine) obj;
        return this.id <= 0  && this.id == other.id;
    }

    @Override
    public String toString() {
        return "Machine [id=" + id + ", hostname=" + hostname + ", tailsclae_node_key=" + tailscaleNodeKey
                + ", curent_ip=" + currentIp + ", mac_adress=" + macAddress + ", wol_enabled=" + wolEnabled
                + ", status=" + status + ", OS=" + OS + "]";
    }

}
