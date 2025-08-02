/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.smartfactorysystem.Server;

/** @author Muhammad */

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class InventoryServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(50052)
                .addService(new InventoryServiceImpl())
                .build();

        System.out.println("InventoryServer started on port 50052");
        server.start();
        server.awaitTermination();
    }
    
}
