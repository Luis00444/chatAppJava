package fr.ensea.rts.luis.tests;

import fr.ensea.rts.luis.classes.UDPServer;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

import static java.lang.Integer.min;
import static java.lang.Thread.sleep;

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
        static ByteArrayOutputStream myOut;
        @BeforeAll
        static void redirectStandardOutput(){
            myOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(myOut));
        }
        @AfterEach
        void tearDown() {
            thread1.interrupt();
            myOut.reset();
        }
        @Test
        void launch_correct() throws IOException {
            UDPServer server =  new UDPServer(1236);
            Assertions.assertNotNull(server);
            thread1 = new Thread(() -> Assertions.assertDoesNotThrow(server::launch,"Unintended throw at launch"));
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
                    send_package_and_test(message, packet);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    socket.close();
                }
            });
            thread2.start();
            thread1.start();

        }

        private void send_package_and_test(String message, DatagramPacket packet) throws IOException, InterruptedException {
            socket.send(packet);

            sleep(1000);

            myOut.flush();

            String[] shown = myOut.toString().split("\n");
            String received = shown[shown.length - 1].split("\r")[0];
            Assertions.assertEquals("<<< " + message.substring(0,  min(1024,message.length())), received);
        }

        @Test
        void launch_incorrect() throws IOException, InterruptedException {
            UDPServer server =  new UDPServer(1237);
            Assertions.assertNotNull(server);
            thread1 = new Thread(() -> Assertions.assertDoesNotThrow(server::launch,"Unintended throw at launch"));
            thread1.start();

            try {
                socket = new DatagramSocket(1400);
            }
            catch (SocketException e) {
                throw new RuntimeException(e);
            }

            String message = "wide length text || ".repeat(100);
            DatagramPacket packet = new DatagramPacket(message.getBytes(StandardCharsets.UTF_8), message.length());
            packet.setAddress(InetAddress.getLoopbackAddress());
            packet.setPort(1237);
            try {
                send_package_and_test(message, packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            finally {
                socket.close();
            }


        }
    }


}