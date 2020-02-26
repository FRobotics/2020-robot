package frc.robot.base;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.base.subsystem.Subsystem;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NTHandler {

    public static final NetworkTable robotTable = NetworkTableInstance.getDefault().getTable("robot");

    private static HashMap<NetworkTableEntry, Supplier<Object>> setMap;
    private static HashMap<NetworkTableEntry, Consumer<Object>> getMap;

    public static void init (List<Subsystem> subsystems) {
        setMap = new HashMap<>();
        subsystems.forEach(NTHandler::addSubsystem);
    }

    private static void addSubsystem(Subsystem subsystem) {
        subsystem.NTSets().forEach(
            (name, valueSupplier) -> setMap.put(robotTable.getEntry(subsystem.name + "/" + name), valueSupplier)
        );
        subsystem.NTGets().forEach(
            (name, valueConsumer) -> getMap.put(robotTable.getEntry(subsystem.name + "/" + name), valueConsumer)
        );
    }

    public static void update() {
        setMap.forEach((entry, valueSupplier) -> entry.setValue(valueSupplier.get()));
        getMap.forEach((entry, valueConsumer) -> valueConsumer.accept(entry.getValue().getValue()));
    }

}
