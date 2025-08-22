package com._thproject._thproject_web.oracle.service;

import jcifs.CIFSContext;
import jcifs.config.PropertyConfiguration;
import jcifs.context.BaseContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;

@Service
@ConditionalOnProperty(name = "spring.datasource.oracle.enabled", havingValue = "true")
public class ImageStorageService {

    // application.properties에서 설정값을 주입받습니다.
    @Value("${pacs.storage.url}")
    private String storageUrl;
    @Value("${pacs.storage.username}")
    private String username;
    @Value("${pacs.storage.password}")
    private String password;

    /**
     * GraphQL로 조회한 파일 경로(path)와 파일명(fname)을 받아
     * 네트워크 드라이브에서 실제 파일을 읽어오는 메서드
     */
    public Resource loadImageAsResource(String path, String fname) throws IOException {
        // 1. JCIFS-NG의 통신 환경(Context)을 설정합니다.
        Properties properties = new Properties();
        // 최신 SMB 프로토콜(v2, v3)을 사용하도록 설정하여 보안과 성능을 높입니다.
        properties.put("jcifs.smb.client.minVersion", "SMB210");
        properties.put("jcifs.smb.client.maxVersion", "SMB311");
        PropertyConfiguration config = new PropertyConfiguration(properties);
        CIFSContext context = new BaseContext(config);

        // 2. 네트워크 드라이브 인증 정보(아이디/비밀번호)를 생성합니다.
        NtlmPasswordAuthenticator auth = new NtlmPasswordAuthenticator(null, username, password);
        CIFSContext authContext = context.withCredentials(auth);

        // 3. 최종 파일 경로를 조립합니다.
        // DB에 저장된 윈도우 경로 구분자 '\'를 URL 표준인 '/'로 바꿔주는 것이 핵심입니다.
        String fullPath = storageUrl + path.replace("\\", "/") + fname;

        // 4. 인증 정보가 포함된 Context를 사용하여 네트워크 파일에 대한 SmbFile 객체를 생성합니다.
        SmbFile smbFile = new SmbFile(fullPath, authContext);

        // 5. 파일의 내용을 스트림으로 읽어옵니다.
        SmbFileInputStream smbInputStream = new SmbFileInputStream(smbFile);

        // 6. 이 스트림을 Spring의 Resource 형태로 반환하여 컨트롤러에서 사용할 수 있게 합니다.
        return new InputStreamResource(smbInputStream);
    }
}