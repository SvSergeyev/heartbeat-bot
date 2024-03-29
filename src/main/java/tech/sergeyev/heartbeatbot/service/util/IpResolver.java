package tech.sergeyev.heartbeatbot.service.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@UtilityClass
@Slf4j
public class IpResolver {
    private Pattern IPV4_PATTERN = null;
    private final String HTTP_PREFIX = "http://";
    private final String HTTPS_PREFIX = "https://";
    private final String SLASH = "/";
    private final String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";

    static {
        try {
            IPV4_PATTERN = Pattern.compile(ipv4Pattern, Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException e) {
            log.error("Unable to compile pattern", e);
        }
    }

    public boolean isIpAddress(String address) {
        if (address.startsWith(HTTP_PREFIX) || address.startsWith(HTTPS_PREFIX)) {
            address = address.startsWith(HTTP_PREFIX)
                    ? address.replace(HTTP_PREFIX, "")
                    : address.replace(HTTPS_PREFIX, "");
        }
        if (address.endsWith(SLASH)) {
            address = address.replace(SLASH, "");
        }
        return IPV4_PATTERN.matcher(address).matches();
    }
}
