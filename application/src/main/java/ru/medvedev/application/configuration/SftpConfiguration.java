package ru.medvedev.application.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.FileListFilter;
import org.springframework.integration.file.filters.FileSystemMarkerFilePresentFileListFilter;
import org.springframework.integration.file.filters.FileSystemPersistentAcceptOnceFileListFilter;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.jdbc.metadata.JdbcMetadataStore;
import org.springframework.integration.metadata.ConcurrentMetadataStore;
import org.springframework.integration.sftp.filters.SftpPersistentAcceptOnceFileListFilter;
import org.springframework.integration.sftp.filters.SftpSimplePatternFileListFilter;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizer;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizingMessageSource;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.MessageHandler;
import ru.medvedev.application.domain.VskCardSourceDto;
import ru.medvedev.application.domain.record.ExcelField;
import ru.medvedev.application.enums.CancellationServiceType;
import ru.medvedev.application.utils.UniversalExcelParser;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SftpConfiguration {
    private static final String TEST_CHANNEL = "test";
    private static final String UPLOADING_FILE_FORMAT = "*.xlsx";
    private static final Pattern UPLOADING_FILE_FORMAT_PATTERN = compile("." + UPLOADING_FILE_FORMAT);
    public static final String METADATA_KEY_PREFIX = "sftpMessageSource";

    private final SftpProperties properties;
    private final DataSource dataSource;

    @Bean
    public SessionFactory<SftpClient.DirEntry> sftpSessionFactory() {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
        factory.setHost(properties.getHost());
        factory.setPort(properties.getPort());
        factory.setUser(properties.getUser());
        factory.setPassword(properties.getPassword());
        factory.setAllowUnknownKeys(true);
        return new CachingSessionFactory<>(factory);
    }

    @Bean
    public ConcurrentMetadataStore metadataStore() {
        return new JdbcMetadataStore(dataSource);
    }

    @Bean
    public SftpInboundFileSynchronizer sftpInboundFileSynchronizer() {
        SftpInboundFileSynchronizer fileSynchronizer = new SftpInboundFileSynchronizer(sftpSessionFactory());
        fileSynchronizer.setDeleteRemoteFiles(false);
        fileSynchronizer.setRemoteDirectory(properties.getReceiveDirectory());
        fileSynchronizer.setFilter(new CompositeFileListFilter<>(List.of(
                new SftpPersistentAcceptOnceFileListFilter(metadataStore(), ""),
                new SftpSimplePatternFileListFilter(UPLOADING_FILE_FORMAT)
        )));
        return fileSynchronizer;
    }

    @Bean
    @InboundChannelAdapter(channel = TEST_CHANNEL, poller = @Poller(cron = "${application.job.sftp-receiving.cron}"))
    public MessageSource<File> sftpMessageSource() {
        SftpInboundFileSynchronizingMessageSource source = new SftpInboundFileSynchronizingMessageSource(sftpInboundFileSynchronizer());
        source.setLocalDirectory(new File("application/src/main/resources/tmp/excel"));
        source.setAutoCreateLocalDirectory(true);
        source.setMaxFetchSize(1);
        return source;
    }

    @Bean
    @ServiceActivator(inputChannel = TEST_CHANNEL)
    public MessageHandler incomingMessageHandler() {
        return message -> {
            File file = (File) message.getPayload();
            if (UPLOADING_FILE_FORMAT_PATTERN.matcher(file.getName()).matches()) {
                try(var fis = new FileInputStream(file)) {
                    var resultDto = UniversalExcelParser.parse(fis, VskCardSourceDto.class, CancellationServiceType.VSK_INSURANCE_CARD);
                    log.info("Handle xlsx file from SFTP, DTO: {}", resultDto);
                    //todo если обработка была без ошибок - то удаляем файл из локал стораджа
                    file.deleteOnExit();
                } catch (Exception e) {
                    //todo при возникновении ошибки, файл не удаляется из локального хранилища, и при перезапуске приложения будет прочитан снова
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
