package fr.ensea.rts.luis.tests;

import fr.ensea.rts.luis.classes.UDPClient;
import fr.ensea.rts.luis.classes.UDPServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.engine.discovery.predicates.IsNestedTestClass;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.BufferOverflowException;
import java.nio.charset.StandardCharsets;

class UDPServerTest {


    @Test
    void test_creation() {
        UDPServer server;
        server = Assertions.assertDoesNotThrow(() -> new UDPServer(),"Unintended throw at default constructor");
        Assertions.assertNotNull(server);
        Assertions.assertDoesNotThrow(() -> new UDPServer(3435),"Unintended throw at creation");
        Assertions.assertThrowsExactly(IllegalArgumentException.class,()->new UDPServer(-1), "Unintended continuation when port is negative");
        Assertions.assertThrowsExactly(IllegalArgumentException.class,()->new UDPServer(999999999), "Unintended continuation when port is bigger than maximum");
    }

    @Nested
    class launch{
        Thread thread1;
        Thread thread2;
        DatagramSocket socket;
        @AfterEach
        void tearDown() {
            thread1.interrupt();

        }
        @Test
        void launch_correct() throws IOException {
            UDPServer server =  new UDPServer(1236);
            Assertions.assertNotNull(server);
            thread1 = new Thread(() -> {
                Assertions.assertDoesNotThrow(server::launch,"Unintended throw at launch");
            });
            thread2 = new Thread(() -> {
                try {
                    socket = new DatagramSocket(1235);
                } catch (SocketException e) {
                    throw new RuntimeException(e);
                }

                String message = "test send";
                DatagramPacket packet = new DatagramPacket(message.getBytes(StandardCharsets.UTF_8), message.length());
                packet.setAddress(InetAddress.getLoopbackAddress());
                packet.setPort(1236);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                finally {
                    socket.close();
                }
            });
            thread2.start();
            thread1.start();

        }

        @Test
        void launch_incorrect() throws IOException, InterruptedException {
            UDPServer server =  new UDPServer(1237);
            Assertions.assertNotNull(server);
            thread1 = new Thread(() -> {
                Assertions.assertDoesNotThrow(server::launch,"Unintended throw at launch");
            });
            thread1.start();

            try {
                socket = new DatagramSocket(1235);
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }

            String message = "wide length text || ".repeat(100);
            DatagramPacket packet = new DatagramPacket(message.getBytes(StandardCharsets.UTF_8), message.length());
            packet.setAddress(InetAddress.getLoopbackAddress());
            packet.setPort(1237);
            try {
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            finally {
                socket.close();
            }


        }
    }


}