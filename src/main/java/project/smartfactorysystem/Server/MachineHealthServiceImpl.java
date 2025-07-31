/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.smartfactorysystem.Server;

/** @author Muhammad */

import com.smartfactory.proto.MachineHealthServiceGrpc;
import com.smartfactory.proto.MachineRequest;
import com.smartfactory.proto.MachineStatus;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class MachineHealthServiceImpl extends MachineHealthServiceGrpc.MachineHealthServiceImplBase {
    private final Random random = new Random();

    // Unary RPC
    @Override
    public void checkStatus(MachineRequest request, StreamObserver<MachineStatus> responseObserver) {
        MachineStatus status = generateStatus(request.getMachineId());
        responseObserver.onNext(status);
        responseObserver.onCompleted();
    }

    // Server Streaming RPC
    @Override
    public void streamHealthData(MachineRequest request, StreamObserver<MachineStatus> responseObserver) {
        for (int i = 0; i < 5; i++) { // Simulate 5 status updates
            MachineStatus status = generateStatus(request.getMachineId());
            responseObserver.onNext(status);

            try {
                Thread.sleep(1000); // 1-second delay
            } catch (InterruptedException e) {
                responseObserver.onError(e);
                return;
            }
        }
        responseObserver.onCompleted();
    }

    private MachineStatus generateStatus(String machineId) {
        double temperature = 60 + random.nextDouble() * 40;  // Between 60-100
        double vibration = 0.1 + random.nextDouble() * 2;    // Between 0.1-2.1
        boolean isOperational = temperature < 90 && vibration < 1.5;

        return MachineStatus.newBuilder()
                .setMachineId(machineId)
                .setTemperature(temperature)
                .setVibration(vibration)
                .setIsOperational(isOperational)
                .setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME))
                .build();
    }
    
}
