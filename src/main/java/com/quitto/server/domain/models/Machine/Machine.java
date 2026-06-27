package com.quitto.server.domain.models.Machine;

public class Machine {
    private int id;
    private String hostname;
    private String tailsclae_node_key;
    private String curent_ip;
    private String mac_adress;
    private boolean wol_enabled;
    private boolean status;
    private String OS;

    public Machine() {
    }

    public Machine(int id, String hostname, String tailsclae_node_key, String curent_ip, String mac_adress,
            boolean status, boolean wol_enabled, String oS) {
        this.id = id;
        this.hostname = hostname;
        this.tailsclae_node_key = tailsclae_node_key;
        this.curent_ip = curent_ip;
        this.mac_adress = mac_adress;
        this.status = status;
        this.wol_enabled = wol_enabled;
        OS = oS;
    }

    public int getId() {
        return id;
    }

    public String getHostname() {
        return hostname;
    }

    public String getTailsclae_node_key() {
        return tailsclae_node_key;
    }

    public String getCurent_ip() {
        return curent_ip;
    }

    public void setCurent_ip(String curent_ip) {
        this.curent_ip = curent_ip;
    }

    public String getMac_adress() {
        return mac_adress;
    }

    public boolean isWol_enabled() {
        return wol_enabled;
    }

    public void setWol_enabled(boolean wol_enabled) {
        this.wol_enabled = wol_enabled;
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
        return "Machine [id=" + id + ", hostname=" + hostname + ", tailsclae_node_key=" + tailsclae_node_key
                + ", curent_ip=" + curent_ip + ", mac_adress=" + mac_adress + ", wol_enabled=" + wol_enabled
                + ", status=" + status + ", OS=" + OS + "]";
    }

}
