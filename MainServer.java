package backend;


import java.io.*;
import java.net.*;
import java.sql.*;

public class MainServer {

    public static void main(String[] args) throws Exception {

        ServerSocket server = new ServerSocket(5500);
        System.out.println("Server Started...");

        while (true) {

            Socket socket = server.accept();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            OutputStream out = socket.getOutputStream();

            String line;
            String crop = "";

            while (!(line = in.readLine()).isEmpty()) {

                if (line.startsWith("POST")) {
                    crop = line.split(" ")[1].replace("/", "");
                }

            }

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT fertilizer, market_price FROM crops WHERE crop_name=?");

            ps.setString(1, crop);

            ResultSet rs = ps.executeQuery();

            String response = "Crop not found";

            if (rs.next()) {

                response = "Fertilizer: " + rs.getString("fertilizer") +
                           "<br>Market Price: " + rs.getInt("market_price");

            }

            String httpResponse =
                    "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Access-Control-Allow-Origin: *\r\n" +
                    "\r\n" +
                    response;

            out.write(httpResponse.getBytes());

            socket.close();
        }
    }
}
    

