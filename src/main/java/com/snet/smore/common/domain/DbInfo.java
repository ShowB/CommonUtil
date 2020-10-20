package com.snet.smore.common.domain;

import com.snet.smore.common.util.EncryptUtil;
import lombok.Data;

@Data
public class DbInfo {
    String url;
    String username;
    String password;
    String className;

    public DbInfo(String url, String username, String password, String className) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.className = className;
    }

    public DbInfo() {
    }

    public void setUsernameWithEncrypt(String username) {
        this.username = EncryptUtil.getEncrypt(username);
    }

    public void setPasswordWithEncrypt(String password) {
        this.password = EncryptUtil.getEncrypt(password);
    }

    public String getDecryptedUsername() {
        return EncryptUtil.getDecrypt(username);
    }

    public String getDecryptedPassword() {
        return EncryptUtil.getDecrypt(password);
    }
}
