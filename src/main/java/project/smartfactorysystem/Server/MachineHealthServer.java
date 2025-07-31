/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.smartfactorysystem.Server;


/** @author Muhammad */

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class MachineHealthServer {
     public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(50051)
                .addService(new MachineHealthServiceImpl())
                .build();

        System.out.println("MachineHealthServer started on port 50051");
        server.start();
        server.awaitTermination();
    }
}
