/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.smartfactorysystem.Server;

/** @author Muhammad */

import com.smartfactory.proto.ProductionSchedulerServiceGrpc;
import com.smartfactory.proto.Order;
import com.smartfactory.proto.ScheduleSummary;
import com.smartfactory.proto.ScheduleUpdate;
import io.grpc.stub.StreamObserver;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductionSchedulerServiceImpl extends ProductionSchedulerServiceGrpc.ProductionSchedulerServiceImplBase {
    private final Random random = new Random();

    // Client Streaming RPC: Receive a list of orders and respond with a summary
    @Override
    public StreamObserver<Order> sendOrders(StreamObserver<ScheduleSummary> responseObserver) {
        List<Order> orders = new ArrayList<>();

        return new StreamObserver<>() {
            @Override
            public void onNext(Order order) {
                System.out.println("Received order: " + order.getOrderId() + " - " + order.getProductName());
                orders.add(order);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error receiving orders: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                String estimatedTime = LocalTime.now().plusMinutes(orders.size() * 2).toString();

                ScheduleSummary summary = ScheduleSummary.newBuilder()
                        .setTotalOrders(orders.size())
                        .setEstimatedCompletionTime(estimatedTime)
                        .setConfirmationMessage("All orders scheduled successfully!")
                        .build();

                responseObserver.onNext(summary);
                responseObserver.onCompleted();
            }
        };
    }

    // Bidirectional Streaming RPC: Real-time scheduling updates
    @Override
    public StreamObserver<Order> liveSchedule(StreamObserver<ScheduleUpdate> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(Order order) {
                String[] statuses = {"Scheduled", "In Progress", "Completed"};
                for (String status : statuses) {
                    ScheduleUpdate update = ScheduleUpdate.newBuilder()
                            .setOrderId(order.getOrderId())
                            .setStatus(status)
                            .setEstimatedTime(LocalTime.now().plusSeconds(random.nextInt(60)).toString())
                            .build();

                    responseObserver.onNext(update);

                    try {
                        Thread.sleep(1000); // simulate progress
                    } catch (InterruptedException e) {
                        responseObserver.onError(e);
                        return;
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error in live scheduling: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
