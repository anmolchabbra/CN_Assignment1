import java.io.*;
import java.net.Socket;
import java.util.*;

public class HttpcLibrary {

//    private static String createGetRequestString(String serverHost, boolean isVerbose, String path, Map<String, String> headers) {
//        // Construct the HTTP GET request with headers, query parameters, and "User-Agent" header
//        StringBuilder getRequest = new StringBuilder();
//        getRequest.append("GET " + path + " HTTP/1.0\r\n");
//
////        // Add query parameters to the URL if they exist
////        if (queryParams != null && !queryParams.isEmpty()) {
////            getRequest.append("?");
////            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
////                getRequest.append(entry.getKey() + "=" + entry.getValue() + "&");
////            }
////            getRequest.deleteCharAt(getRequest.length() - 1); // Remove the trailing "&"
////        }
//
//        //getRequest.append(" HTTP/1.0\r\n");
//
//        // Add the "Host" header
//        getRequest.append("Host: " + serverHost + "\r\n");
//
//        // Add the "User-Agent" header with the specific value
//        //getRequest.append("User-Agent: Concordia-HTTP/1.0\r\n");
//
//        // Add other headers if they exist
//        if (headers != null && !headers.isEmpty()) {
//            for (Map.Entry<String, String> entry : headers.entrySet()) {
//                getRequest.append(entry.getKey() + ": " + entry.getValue() + "\r\n");
//            }
//        }
//
//        // Add the "Connection" header and the end of the request
//        getRequest.append("Connection: close\r\n\r\n");
//
//        return getRequest.toString();
//    }

    public static String constructRequestString(HttpUrlRequest request) {
        StringBuilder requestBuilder = new StringBuilder();
        //System.out.println(request);
        requestBuilder.append(request.getHttpVerb()).append(" ").append(request.getPath()).append(" HTTP/1.1\r\n");
        requestBuilder.append("Host: ").append(request.getServerHost()).append("\r\n");

        for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
            requestBuilder.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }
        //requestBuilder.append("User-Agent: Concordia-HTTP/1.0\r\n");

        if (request.getHttpVerb().equalsIgnoreCase("post")) {
            if (request.getInlineData() != null) {
                requestBuilder.append("Content-Length: ").append(request.getInlineData().length()).append("\r\n");
                //requestBuilder.append("\r\n");
                requestBuilder.append("\r\n" + request.getInlineData());
            } else if (request.getFilePath() != null) {
                // You can implement file upload here
                // Read the file content and send it as the request body
            }
        } else {
            requestBuilder.append("Connection: close\r\n\r\n");
        }

        requestBuilder.append("Connection: close\r\n\r\n");

        return requestBuilder.toString();
    }

    public static void sendHttpGetRequest(HttpUrlRequest request) {
        String Request = constructRequestString(request);
        System.out.println(Request);

        int serverPort = 80;

        // Define the query parameters
        //String queryParams = "param1=value1&param2=value2";

        // Construct the HTTP GET request with headers and query parameters
        try (Socket socket = new Socket("httpbin.org", 80);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Construct the GET request
            String getRequest = "GET /get?course=networking&assignment=1 HTTP/1.0\r\n" +
                    "Host: httpbin.org\r\n" +
                    "User-Agent: MyJavaClient\r\n" +
                    "Connection: close\r\n" +
                    "\r\n";
            System.out.println(getRequest);
            // Send the GET request
            out.println(getRequest);

            // Receive and print the response
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }
            System.out.println("Response:\n" + response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   // String postRequest = "POST /post HTTP/1.1\r\n" +
//                    "Host: httpbin.org\r\n" +
//                    "User-Agent: MyJavaClient\r\n" +
//                    "Content-Type: application/json\r\n" +
//                    "Content-Length: " + request.getInlineData().length() + "\r\n" +
//                    "Connection: close\r\n" +
//                    "\r\n" +
//                    request.getInlineData();
    // Construct the request

    public static String sendRequest(HttpUrlRequest request) {
        String requestString = constructRequestString(request);
        System.out.println("Request String: " + requestString);
        //sendHttpGetRequest(request);
        int serverPort = 80;
        try (Socket socket = new Socket(request.getServerHost(), serverPort)) {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(requestString.getBytes());
            outputStream.flush();
            //System.out.println("Inside socket");
            // Receive and print the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                //System.out.println("Inside socket");
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try (Socket socket = new Socket("httpbin.org", 80);
//             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
//
//            // Construct the GET request
//            String getRequest = "GET /get HTTP/1.1\r\n" +
//                    "Host: httpbin.org\r\n" +
//                    "User-Agent: MyJavaClient\r\n" +
//                    "Connection: close\r\n" +
//                    "\r\n";
//            System.out.println(getRequest);
//            // Send the GET request
//            out.println(getRequest);
//
//            // Receive and print the response
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = in.readLine()) != null) {
//                response.append(line).append("\n");
//            }
//            System.out.println("Response:\n" + response.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }

}




