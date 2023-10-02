import java.util.HashMap;
import java.util.Map;

public class HttpPostRequest {
    private final String httpVerb;

    private final boolean isVerbose;

    private final String httpUrl;
    private final String serverHost;
    private final String path;
    private final Map<String, String> headers;
    private final String inlineData;
    private final String filePath;

//    public HttpPostRequest() {
//        headers = new HashMap<>();
//    }
    public HttpPostRequest(String httpVerb, String httpUrl, String serverHost, String path, Map<String, String> headers, boolean isVerbose, String inlineData, String filePath) {
        this.httpVerb = httpVerb;
        this.serverHost = serverHost;
        this.path = path;
        this.headers = headers;
        this.isVerbose = isVerbose;
        this.inlineData = inlineData;
        this.filePath = filePath;
        this.httpUrl = httpUrl;
    }

    public String getHttpVerb() {
        return httpVerb;
    }

    public String getHttpURL() {
        return httpUrl;
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

    public String getInlineData() {
        return inlineData;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP Verb: ").append(httpVerb).append("\n");
        sb.append("Server Host: ").append(serverHost).append("\n");
        sb.append("Path: ").append(path).append("\n");
        sb.append("Is Verbose: ").append(isVerbose).append("\n");

        sb.append("Headers:\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        if (inlineData != null) {
            sb.append("Inline Data:\n").append(inlineData).append("\n");
        }

        if (filePath != null) {
            sb.append("File Path: ").append(filePath).append("\n");
        }

        return sb.toString();
    }
}
