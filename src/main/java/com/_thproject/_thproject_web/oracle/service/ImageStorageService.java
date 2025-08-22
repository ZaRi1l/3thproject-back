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

    private final String storageUrl;
    private final CIFSContext authContext; // 인증 정보를 담고 있는 Context

    // 서비스가 생성될 때 딱 한 번만 인증 정보를 설정합니다. (성능 최적화)
    public ImageStorageService(
            @Value("${pacs.storage.url}") String storageUrl,
            @Value("${pacs.storage.username}") String username,
            @Value("${pacs.storage.password}") String password) {
        
        this.storageUrl = storageUrl;

        try {
            Properties properties = new Properties();
            properties.put("jcifs.smb.client.minVersion", "SMB210");
            properties.put("jcifs.smb.client.maxVersion", "SMB311");
            PropertyConfiguration config = new PropertyConfiguration(properties);
            CIFSContext context = new BaseContext(config);
            NtlmPasswordAuthenticator auth = new NtlmPasswordAuthenticator(null, username, password);
            this.authContext = context.withCredentials(auth); // 인증된 Context를 멤버 변수에 저장
        } catch (Exception e) {
            // 초기화 실패 시 서버 시작을 막기 위해 런타임 예외를 발생시킵니다.
            throw new RuntimeException("네트워크 드라이브 인증 Context 초기화 실패", e);
        }
    }

    /**
     * 디렉토리 경로와 파일 이름을 받아 네트워크 드라이브에서 파일을 읽어옵니다.
     */
    public Resource loadImageAsResource(String directoryPath, String fileName) throws IOException {
        // 1. 윈도우 경로 구분자 '\'를 URL 표준인 '/'로 바꿔줍니다.
        String convertedPath = directoryPath.replace("\\", "/");

        // 2. 최종 파일 경로를 안전하게 조립합니다.
        // storageUrl 끝에 '/'가 없어도, convertedPath 시작에 '/'가 없어도大丈夫.
        String fullPath = String.join("/", this.storageUrl, convertedPath, fileName);
        
        // 3. 생성자에서 미리 만들어 둔 인증 정보(authContext)를 사용합니다.
        SmbFile smbFile = new SmbFile(fullPath, this.authContext);
        
        if (!smbFile.exists()) {
            throw new IOException("네트워크 드라이브에서 파일을 찾을 수 없습니다: " + fullPath);
        }

        SmbFileInputStream smbInputStream = new SmbFileInputStream(smbFile);
        return new InputStreamResource(smbInputStream);
    }
}