package com.snet.smore.common.util;

import com.snet.smore.common.domain.DbInfo;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class DbUtil {

    private static Connection conn;

    public synchronized static Connection getConnection(DbInfo info) {

        try {
            if (conn != null && !conn.isClosed()) {
                DatabaseMetaData metaData = conn.getMetaData();
                if (metaData.getURL().equals(info.getUrl())
                        && metaData.getUserName().equals(info.getDecryptedUsername())) {
                    return conn;
                }
            }

            String url = info.getUrl();
            String user = info.getDecryptedUsername();
            String password = info.getDecryptedPassword();

            if (StringUtil.isNotBlank(info.getClassName())) {
                Class.forName(info.getClassName());
            }

            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);
        } catch (Exception e) {
            log.error("An error occurred while getting database connection.", e);
        }

        return conn;
    }

    public synchronized static void disconnect(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            log.error("An error occurred while disconnecting database.", e);
        }
    }

    public synchronized static void disconnect(PreparedStatement pstmt) {
        try {
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            log.error("An error occurred while disconnecting database.", e);
        }
    }

    public synchronized static void disconnect(PreparedStatement pstmt, Connection conn) {
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            log.error("An error occurred while disconnecting database.", e);
        }
    }
}
