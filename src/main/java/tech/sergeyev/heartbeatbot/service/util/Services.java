package tech.sergeyev.heartbeatbot.service.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tech.sergeyev.heartbeatbot.exception.ServiceNameConversionException;

@RequiredArgsConstructor
@Getter
public enum Services {
    SERVICE_DISCOVERY("Service Discovery", ""),
    METAPROXY("Metaproxy", ""),
    HELPDESK("Helpdesk", ""),
    QUEUE("Queue", ""),
    LICENSE_MANAGER("Licenses Manager", ""),
    TASKS_STATISTICS("Tasks Statistics", ""),
    NAUCINA("Naucina", ""),
    UI("UI", ""),
    CORE("Core", "/opt/cml-bench/logs"),
    ELEGANCE("Elegans", ""),
    SOLVER("Solver", ""),
    TORMINOSUS("Torminosus", ""),
    NOMINATOR("Nominator", ""),
    PYXIDATA("Pyxidata", ""),
    EDULIS("Edulis", ""),
    AMANITA("Amanita", ""),
    ACCESS_RIGHTS("Accessrights", ""),
    API_GATEWAY("API Gateway", ""),
    AUTHORIZATION("Authorization", ""),
    LDAP("LDAP", ""),
    SECURITY_EVENTS("Security Events", "");

    private final String nameInReleaseInfo;
    private final String logFilesPath;

    public static Services covertNameFromReleaseInfo(String nameInReleaseInfo)
            throws ServiceNameConversionException {
        for (var name : Services.values()) {
            if (name.getNameInReleaseInfo().equals(nameInReleaseInfo)) {
                return name;
            }
        }
        throw new ServiceNameConversionException("Cannot find service with name '" + nameInReleaseInfo + "'");
    }
}
