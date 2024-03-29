package tech.sergeyev.heartbeatbot.service.util;

import com.jcraft.jsch.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
@Slf4j
public class LogFilesCollector {
    private static final int SESSION_TIMEOUT = 10_000;
    private static final int CHANNEL_TIMEOUT = 5000;
    private static final String CHANNEL_TYPE = "sftp";
    private static final int REMOTE_PORT = 22;

    @Value("${ssh.connection.username}")
    private String username;

    @Value("${ssh.connection.private_key}")
    private String privateKey;

    @Value("${ssh.connection.known_hosts_file}")
    private String knownHosts;

    public File getLogFilesAsZip(String url, Services service) {
        var files = getAllLogFiles(url, service.getLogFilesPath());

        final FileOutputStream fos = new FileOutputStream(Paths.get(file1).getParent().toAbsolutePath() + "/compressed.zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (String srcFile : srcFiles) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }

        zipOut.close();
        fos.close();

        try(var fos = new FileOutputStream(Paths.get()))

//        for (var file : files) {
//            try {
//                Files.deleteIfExists(file.toPath());
//            } catch (IOException e) {
//                log.error("Cannot delete file '{}'", file.getName());
//            }
//        }
        return null;
    }

    public Set<File> getAllLogFiles(String url, String dir) {
        var files = new HashSet<File>();
        Session jschSession = null;
        try {
            var jsch = new JSch();
            jsch.setKnownHosts(knownHosts);
            jschSession = jsch.getSession(username, url, REMOTE_PORT);
//             jsch.addIdentity("/home/ssergeev/.ssh/id_rsa");
            jsch.addIdentity(privateKey);
//            jschSession.setPassword(PASSWORD);
            jschSession.connect(SESSION_TIMEOUT);
            var sftp = jschSession.openChannel(CHANNEL_TYPE);
            sftp.connect(CHANNEL_TIMEOUT);
            var channelSftp = (ChannelSftp) sftp;
//            channelSftp.get(remoteFile, localFile);
            var list = channelSftp.ls(dir);
            for (var entry : list) {
                var file = (ChannelSftp.LsEntry) entry;
                var outFile = new File(
                        System.getenv("java.io.tempdir") + File.pathSeparator + file.getFilename());
                try (var bis = new BufferedInputStream(channelSftp.get(file.getFilename()));
                     var os = new FileOutputStream(outFile);
                     var bos = new BufferedOutputStream(os)) {
                    int readCount;
                    var buffer = new byte[1024];
                    while ((readCount = bis.read(buffer)) > 0) {
                        bos.write(buffer, 0, readCount);
                    }
                } catch (IOException e) {
                    log.error("An error occurred: {}", e.getMessage());
                    log.error("More: ", e);
                }
                files.add(outFile);
            }
            channelSftp.exit();
        } catch (JSchException | SftpException e) {
            log.error("An error occurred: {}", e.getMessage());
            log.error("More: ", e);
        } finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }
        return Collections.emptySet();
    }
}
