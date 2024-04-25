package storage;

import java.net.InetSocketAddress;

public class AddressTranslator {

    public AddressTranslator() {

    }

    public InetSocketAddress translate(InetSocketAddress socket) {
        String hostAddress = socket.getAddress().getHostAddress();
        String hostName = socket.getHostName();
        return switch (hostAddress) {
            case "127.0.0.2" -> new InetSocketAddress("cassandra1", 9042);
            case "127.0.0.3" -> new InetSocketAddress("cassandra2", 9043);
            case "127.0.0.4" -> new InetSocketAddress("cassandra3", 9044);
            default -> throw new RuntimeException("Wrong address");
        };
    }
}
