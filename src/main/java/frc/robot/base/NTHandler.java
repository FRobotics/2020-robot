package frc.robot.base;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class NTHandler {

    private static NetworkTable robotTable;
    private static NetworkTable visionTable;

    private static HashMap<NetworkTableEntry, Supplier<Object>> updateMap;

    public static <R extends Robot4150<R>> void init (List<Subsystem<?, R>> subsystems) {
        robotTable = NetworkTableInstance.getDefault().getTable("robot");
        visionTable = NetworkTableInstance.getDefault().getTable("vision");
        updateMap = new HashMap<>();
        subsystems.forEach(NTHandler::addSubsystem);
    }

    private static void addSubsystem(Subsystem<?, ?> subsystem) {
        subsystem.createNTMap().forEach(
                (name, valueSupplier) -> updateMap.put(robotTable.getEntry(subsystem.name + "/" + name), valueSupplier)
        );
    }

    public static void update() {
        updateMap.forEach((entry, valueSupplier) -> entry.setValue(valueSupplier.get()));
    }

}
