/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.smartfactorysystem.Server;

/** @author Muhammad */

import com.smartfactory.proto.InventoryServiceGrpc;
import com.smartfactory.proto.InventoryRequest;
import com.smartfactory.proto.InventoryStatus;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class InventoryServiceImpl extends InventoryServiceGrpc.InventoryServiceImplBase {
    private final String[] itemNames = {"Steel Rod", "Plastic Casing", "Circuit Board", "Sensor Unit", "Cooling Fan"};
    private final Random random = new Random();

    @Override
    public void getInventoryStream(InventoryRequest request, StreamObserver<InventoryStatus> responseObserver) {
        System.out.println("Streaming inventory for warehouse: " + request.getWarehouseId());

        for (int i = 0; i < itemNames.length; i++) {
            InventoryStatus status = InventoryStatus.newBuilder()
                    .setItemId("ITEM-" + (i + 1))
                    .setItemName(itemNames[i])
                    .setQuantity(random.nextInt(200)) // 0-199
                    .setStatus("IN_STOCK")
                    .setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME))
                    .build();

            responseObserver.onNext(status);

            try {
                Thread.sleep(1000); // simulate streaming
            } catch (InterruptedException e) {
                responseObserver.onError(e);
                return;
            }
        }

        responseObserver.onCompleted();
    }
    
}
