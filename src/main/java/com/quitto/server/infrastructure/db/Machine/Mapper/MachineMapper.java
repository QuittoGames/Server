package com.quitto.server.infrastructure.db.Machine.Mapper;

import com.quitto.server.domain.models.Machine.Machine;
import com.quitto.server.infrastructure.db.Machine.Entity.MachineEntity;

public class MachineMapper {

    public static Machine toDomain(MachineEntity machineEntity) throws IllegalArgumentException {
        if (machineEntity == null) {
            throw new IllegalArgumentException("Machine Paramter of Entity JPA is null");
        }

        return new Machine(
                machineEntity.getId(),
                machineEntity.getHostname(),
                machineEntity.getTailscaleNodeKey(),
                machineEntity.getCurrentIp(),
                machineEntity.getMacAddress(),
                machineEntity.isWolEnabled(),
                machineEntity.isStatus(),
            machineEntity.getOS(),
            machineEntity.getUserId());
    }

    public static MachineEntity toInfra(Machine machine) throws IllegalArgumentException {
        if (machine == null) {
            throw new IllegalArgumentException("Machine Paramter of class domain is null");
        }

        return new MachineEntity(
                machine.getId(),
                machine.getHostname(),
                machine.getTailscaleNodeKey(),
                machine.getCurrentIp(),
                machine.getMacAddress(),
                machine.isWolEnabled(),
                machine.isStatus(),
            machine.getOS(),
            machine.getUserId()
            );
    }
}
