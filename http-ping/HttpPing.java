/**
 * http-ping
 *
 * For this exercise I chose to use Sun's Java HTTP Server.  Most of the work for this exercise is
 * located in the HttpPingHandler private class.
 *
 * For a larger application I wouldn't go this route; a full-blown Web framework would make more
 * sense, especially if the application is backed by a database.
 *
 * Jim Nelson <jimbonator@gmail.com>
 */

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.time.*;
import com.sun.net.httpserver.*;

public class HttpPing extends Object {
    private final static int BACKLOG = 50;
    
    private class HttpPingHandler extends Object implements HttpHandler {
        public void handle(HttpExchange xchg) throws IOException {
            Headers headers = xchg.getResponseHeaders();
            headers.set("Content-Type", "text/plain; charset=" + Charset.defaultCharset().name());
            
            // Date/time are emitted in UTC ISO-8601 format, remote address in dotted notation.
            byte[] resp = (
                Clock.systemUTC().instant().toString()
                + '\n'
                + xchg.getRemoteAddress().getAddress().getHostAddress().toString()
                + '\n'
                ).getBytes();
            
            xchg.sendResponseHeaders(HttpURLConnection.HTTP_OK, resp.length);
            xchg.getResponseBody().write(resp);
            
            xchg.close();
        }
    }
    
    private final InetSocketAddress localAddr;
    private HttpServer server;
    
    public static void main(String[] args) {
        try {
            new HttpPing(new InetSocketAddress(Integer.decode(args[0]))).run();
        } catch (IOException ioe) {
            System.err.println(ioe.toString());
        } catch (Exception e) {
            System.out.println("http-ping [listen-port]");
        }
    }
    
    public HttpPing(InetSocketAddress localAddr) {
        this.localAddr = localAddr;
    }
    
    public void run() throws IOException {
        server = HttpServer.create(localAddr, BACKLOG);
        server.createContext("/", new HttpPingHandler());
        server.setExecutor(null);
        
        System.out.printf("Starting server, listening on port %d...\n", localAddr.getPort());
        server.start();
    }
}

