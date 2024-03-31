package tech.sergeyev.heartbeatbot.service.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import tech.sergeyev.heartbeatbot.dto.ReleaseInfo;
import tech.sergeyev.heartbeatbot.exception.ReleaseInfoParsingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
@Slf4j
public class ReleaseInfoCollector {
    private static final String REQUEST_METHOD = "GET";
    private static final String URL_TEMPLATE = "http://%s/release_info.txt";
    private static final String HEALTHCHECK_URL_TEMPLATE = "http://%s/";
    private static final int BRANCH_INDEX = 3;
    private static final int COMMIT_INDEX = 4;
    private static final int TIME_INDEX = 5;
    private static final String BRANCH_PREFIX = " Branch: ";
    private static final String COMMIT_PREFIX = " Commit hash: ";
    private static final String TIME_PREFIX = " Build time: ";

    public List<ReleaseInfo> collectAll(String url) throws ReleaseInfoParsingException {
        try {
//            var response = getReleaseInfo(url);
            var info = new ArrayList<ReleaseInfo>();
            for (var service : Services.values()) {
                info.add(parseOne(getReleaseInfo(url), service));
            }
            return info;
        } catch (IOException e) {
            log.error("Cannot connect to {}", url);
            log.error("Error: ", e);
        }
        return null; // todo null is too bad
    }

    public ReleaseInfo collectOne(String url, Services name) throws ReleaseInfoParsingException {
        try {
            var response = getReleaseInfo(url);
            return parseOne(response, name);
        } catch (IOException e) {
            log.error("Cannot connect to {}", url);
            log.error("Error: ", e);
        }
        return null; // todo null is too bad
    }

    private String getFormattedUrl(String url) {
        return String.format(URL_TEMPLATE, url);
    }

    private String getHealthcheckUrl(String url) {
        return String.format(HEALTHCHECK_URL_TEMPLATE, url);
    }

    private ReleaseInfo parseOne(InputStream content, Services serviceName)
            throws ReleaseInfoParsingException {
        var dataContainingInformationAboutService = getInformationAboutService(content, serviceName);
        var dataContainingUnformattedInfo = dataContainingInformationAboutService.split("\\|");
        var branch = formatValue(dataContainingUnformattedInfo[BRANCH_INDEX], BRANCH_PREFIX);
        var commit = formatValue(dataContainingUnformattedInfo[COMMIT_INDEX], COMMIT_PREFIX);
        var time = formatValue(dataContainingUnformattedInfo[TIME_INDEX], TIME_PREFIX);
        return new ReleaseInfo(serviceName, branch, commit, time);
    }

    private String formatValue(String value, String prefix) {
        var result = value.replace(prefix, "").trim();
        for (var name : Services.values()) {
            if (result.endsWith(name.getNameInReleaseInfo())) {
                return result.replace(name.getNameInReleaseInfo(), "");
            }
        }
        return result;
    }

    private String getInformationAboutService(InputStream content, Services serviceName)
            throws ReleaseInfoParsingException {
        String body;
        try (var br = new BufferedReader(new InputStreamReader(content))) {
            var sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            body = sb.toString();
        } catch (IOException e) {
            log.error("Error: ", e);
            throw new ReleaseInfoParsingException("Cannot read response");
        }
        return body.split(serviceName.getNameInReleaseInfo())[1];
    }

    private InputStream getReleaseInfo(String url) throws IOException {
        if (!isServiceActive(url)) {
            return null;
        }
        var connection = (HttpURLConnection) new URL(getFormattedUrl(url)).openConnection();
        connection.setRequestMethod(REQUEST_METHOD);
        return connection.getInputStream();
    }

    private boolean isServiceActive(String url) {
        try {
            var connection = (HttpURLConnection) new URL(getHealthcheckUrl(url)).openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.getResponseCode();
            return Boolean.TRUE;
        } catch (IOException e) {
            return Boolean.FALSE;
        }
    }
}
