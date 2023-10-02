import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Httpc {

//    public static void main(String[] args) {
//        if (args.length != 1) {
//            System.out.println("Usage: java httpc Help <command>");
//            System.exit(1);
//        }
//
//        String command = args[0];
//        //Invalid commands
//        if (!command.startsWith("httpc")) {
//            System.out.println("Invalid Command");
//            return;
//        }
//
//        //Call Appropriate commands
//        callAppropriateFunctions(command);
//    }

/*
Help functions
 */
    private static void callAppropriateFunctions(String command) {
        String[] splits = command.split("");
        if (splits[1].equals("help")) {
            //handle help methods
            handleHelpCommand(command);
        } else if (splits[1].equals("get")) {
            //handle get related commands
            HttpRequest httpRequest = parseHttpRequest(command);
            HttpcLibrary.sendHttpGetRequest(httpRequest.getServerHost(), httpRequest.isVerbose(),httpRequest.getPath(),
                    httpRequest.getHeaders());
        } else if (splits[1].equals("post")) {
            //handle post related commands
            HttpPostRequest httpRequest = parseHttpPostRequest(command);
            HttpcLibrary.sendPostRequest(httpRequest);
        }
    }
    public static HttpPostRequest parseHttpPostRequest(String input) {
        // Define a regular expression pattern to match the input string
        Pattern pattern = Pattern.compile("httpc\\s+(get|post)\\s+(-v\\s+)?(?:\\s+-h\\s+'([^:]+):\\s+([^']+)(?:'\\s+|$))*\\s*(?:-d\\s+'([^']+)'\\s+)?(?:-f\\s+'([^']+)'\\s+)?\\s+'([^']+)'");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            System.out.println("Here");
            String httpVerb = matcher.group(1);
            boolean isVerbose = matcher.group(2) != null && matcher.group(2).trim().equals("-v");
            String serverHost = matcher.group(4);
            String path = matcher.group(5);

            Map<String, String> headers = new HashMap<>();
            if (matcher.group(6) != null && matcher.group(7) != null) {
                String headerName = matcher.group(6);
                String headerValue = matcher.group(7);

                while (headerName != null && headerValue != null) {
                    headers.put(headerName, headerValue);
                    if (matcher.find()) {
                        headerName = matcher.group(6);
                        headerValue = matcher.group(7);
                    } else {
                        break;
                    }
                }
            }

            String inlineData = matcher.group(8); // Capture inline data
            String filePath = matcher.group(9); // Capture file path

            HttpPostRequest req = new HttpPostRequest(httpVerb, "", serverHost, path, headers, isVerbose, inlineData, filePath);

            System.out.println(req.toString());

            return req;
        }

        return null;

    }

    public static void parseRequest(String command) {
        //Pattern verbose = Pattern.compile();
        //String headerPattern = "-h\\s+([^:]+):\\s+(.+)";
        ///Type of Request

        String serverHost = "";
        String path = "";
        //Server Host and path
        Pattern urlPattern = Pattern.compile("http://([^/]+)(/.*)");
        Matcher urlMatcher = urlPattern.matcher(command);
        String httpUrl = "";
        if (urlMatcher.find()) {
            serverHost = urlMatcher.group(1);
            path = urlMatcher.group(2);
            httpUrl = serverHost + path;
            System.out.println("Http URL" + httpUrl);
            System.out.println("Server Host: " + serverHost);
            System.out.println("Path: " + path);
        } else {
            System.err.println("Invalid URL format");
        }
        boolean isVerbose = command.contains("-v");
        //System.out.println("No "+ urlMatcher.group(0));

        //Headers
        String patternHeader = "-h\\s*([^:]+):\\s*([^\\s]+)";
        Pattern headerPattern = Pattern.compile(patternHeader);
        Matcher headerMatcher = headerPattern.matcher(command);
        Map<String, String> headers = new HashMap<>();
        while (headerMatcher.find()) {
            System.out.println("hey");
            headers.put(headerMatcher.group(1), headerMatcher.group(2));
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + ": " + value);
        }

        // Inline Data
        String patternInLine = "-d\\s*'([^']+)'";
        Pattern inLinePattern = Pattern.compile(patternInLine);
        Matcher inLineMatcher = inLinePattern.matcher(command);

        String inLineData = "";
        if (inLineMatcher.find()) {
            inLineData = inLineMatcher.group(1);
        }
        HttpcLibrary.sendPostRequest(new HttpPostRequest("POST", httpUrl, serverHost, path ,headers, isVerbose, inLineData, ""));
    }



    private static void handleHelpCommand(String command) {
        if (command.equals("httpc help")) {
            printHttpcHelp();
        } else if (command.equals("httpc help get")) {
            printHttpGetHelp();
        } else if (command.equals("httpc help post")) {
            printHttpPostHelp();
        } else {
            System.out.println("Unknown command: " + command);
        }
    }

    private static void printHttpcHelp() {
        System.out.println("httpc is a curl-like application but supports HTTP protocol only.");
        System.out.println("Usage:");
        System.out.println(" httpc command [arguments]");
        System.out.println("The commands are:");
        System.out.println(" get executes a HTTP GET request and prints the response.");
        System.out.println(" post executes a HTTP POST request and prints the response.");
        System.out.println(" help prints this screen.");
        System.out.println("Use \"httpc help [command]\" for more information about a command.");
    }

    private static void printHttpGetHelp() {
        System.out.println("usage: httpc get [-v] [-h key:value] URL");
        System.out.println("Get executes a HTTP GET request for a given URL.");
        System.out.println(" -v Prints the detail of the response such as protocol, status, and headers.");
        System.out.println(" -h key:value Associates headers to HTTP Request with the format 'key:value'.");
    }

    private static void printHttpPostHelp() {
        System.out.println("usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL");
        System.out.println("Post executes a HTTP POST request for a given URL with inline data or from file.");
        System.out.println(" -v Prints the detail of the response such as protocol, status, and headers.");
        System.out.println(" -h key:value Associates headers to HTTP Request with the format 'key:value'.");
        System.out.println(" -d string Associates an inline data to the body HTTP POST request.");
        System.out.println(" -f file Associates the content of a file to the body HTTP POST request.");
        System.out.println("Either [-d] or [-f] can be used but not both.");
    }


    public static void main(String[] args) {
        String input1 = "httpc get 'http://httpbin.org/get?course=networking&assignment=1'";
        //String input1 = "httpc get -v 'http://api.example.com/resource?param1=value1&param2=value2' -h Authorization: Bearer your-access-token -h User-Agent: MyCurlClient";
        //HttpRequest httpRequest = parseHttpRequest(input1);

        String input2 = "httpc post -v -h Content-Type:application/json -h User-Agent: Concordia-HTTP/1.0 --d '{\"Assignment\": 1}' http://httpbin.org/post";

//        if (httpRequest != null) {
//            System.out.println("Request 1:");
//            //HttpRequest httpRequest = parseHttpRequest(command);
//            HttpcLibrary.sendHttpGetRequest(httpRequest.getServerHost(), httpRequest.isVerbose(),httpRequest.getPath(),
//                    httpRequest.getHeaders());
//
//        }
//        HttpPostRequest httpPostRequest = parseHttpPostRequest(input2);
//        if (httpPostRequest != null) {
//            //HttpPostRequest httpRequest = parseHttpPostRequest(command);
//            HttpcLibrary.sendPostRequest(httpPostRequest);
//        }
        parseRequest(input2);
    }

    public static HttpRequest parseHttpRequest(String input) {
        // Define a regular expression pattern to match the input string
        Pattern pattern = Pattern.compile("httpc\\s+(get|post)\\s+(-v\\s+)?(?:\\s+-h\\s+'([^:]+):\\s+([^']+)(?:'\\s+|$))*\\s*(-d\\s+'([^']+)'\\s+)?(-f\\s+'([^']+)'\\s+)?\\s+'([^']+)'");

        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String httpVerb = matcher.group(1);
            boolean isVerbose = matcher.group(2) != null && matcher.group(2).equals("-v");
            String serverHost = matcher.group(3);
            String path = matcher.group(4);

            Map<String, String> headers = new HashMap<>();

            // Check if headers were matched in the input
            String headersString = matcher.group(5);
            System.out.println("yo-no");
            if (headersString != null) {
                System.out.println("yo-yes");
                Pattern headerPattern = Pattern.compile("-h\\s+([^:]+):\\s+([^\\s]+)");

                Matcher headerMatcher = headerPattern.matcher(input);

                while (headerMatcher.find()) {
                    System.out.println("yo");
                    String headerName = headerMatcher.group(1);
                    String headerValue = headerMatcher.group(2);
                    headers.put(headerName, headerValue);
                }
            }
            HttpRequest request = new HttpRequest(httpVerb, serverHost, path, headers, isVerbose);
            printHttpRequest(request);
            return request;
        }

        return null;
    }

    public static void printHttpRequest(HttpRequest httpRequest) {
        System.out.println("HTTP Verb: " + httpRequest.getHttpVerb());
        System.out.println("Verbose: " + httpRequest.isVerbose());
        System.out.println("Server Host: " + httpRequest.getServerHost());
        System.out.println("Path: " + httpRequest.getPath());

        System.out.println("Headers:");
        for (Map.Entry<String, String> header : httpRequest.getHeaders().entrySet()) {
            String headerName = header.getKey();
            System.out.println(headerName + ": " + header.getValue());
        }
    }
}

class HttpRequest {
    private final String httpVerb;
    private final boolean isVerbose;
    private final String serverHost;
    private final String path;
    private final Map<String, String> headers;

    public HttpRequest(String httpVerb, String serverHost, String path, Map<String, String> headers, boolean isVerbose) {
        this.httpVerb = httpVerb;
        this.serverHost = serverHost;
        this.path = path;
        this.headers = headers;
        this.isVerbose = isVerbose;
    }

    public String getHttpVerb() {
        return httpVerb;
    }

    public boolean isVerbose() {
        return isVerbose;
    }

    public String getServerHost() {
        return serverHost;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}